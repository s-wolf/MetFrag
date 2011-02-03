/*
*
* Copyright (C) 2009-2010 IPB Halle, Sebastian Wolf
*
* Contact: swolf@ipb-halle.de
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*
*/
package de.ipbhalle.metfrag.keggWebservice;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.aromaticity.CDKHueckelAromaticityDetector;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.inchi.InChIGenerator;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IChemModel;
import org.openscience.cdk.interfaces.IChemSequence;
import org.openscience.cdk.io.INChIPlainTextReader;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.io.iterator.IteratingMDLReader;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

import de.ipbhalle.metfrag.database.PubChemToDatabaseParallel;
import de.ipbhalle.metfrag.fragmenter.Fragmenter;
import de.ipbhalle.metfrag.main.Config;
import de.ipbhalle.metfrag.massbankParser.Peak;
import de.ipbhalle.metfrag.tools.MolecularFormulaTools;
import de.ipbhalle.metfrag.tools.Number;
import de.ipbhalle.metfrag.tools.renderer.StructureRenderer;

public class KeggPreprocess {
	
	private static Connection con;
	private String path;
	
	public KeggPreprocess(String path, Connection con) {
		this.path = path;
		this.con = con;
	}
	
	
	public void run(File file, Double smallestPeak)
	{
		try {
			System.out.println("Processing: " + path + file.getName());
			
			String keggID = file.getName().split("\\.")[0];
			
			Statement stmt = null;
		    stmt = con.createStatement();
			
		    //InputStream ins = new BufferedInputStream(new FileInputStream(path + file.getName()));
		    InputStream ins = new BufferedInputStream(new FileInputStream(path + "C00509.mol"));
		    MDLV2000Reader reader = new MDLV2000Reader(ins);
	        ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());
	        IChemSequence sequence = chemFile.getChemSequence(0);
	        IChemModel model = sequence.getChemModel(0);
	        IAtomContainer molRead = model.getMoleculeSet().getAtomContainer(0);
			
			CDKHueckelAromaticityDetector.detectAromaticity(molRead);
	        CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(molRead.getBuilder());
	        for (IAtom atom : molRead.atoms()) {
				IAtomType type = matcher.findMatchingAtomType(molRead, atom);
		        AtomTypeManipulator.configure(atom, type);
			}
	        
	        CDKHydrogenAdder hAdder = CDKHydrogenAdder.getInstance(molRead.getBuilder());
	        hAdder.addImplicitHydrogens(molRead);
	        AtomContainerManipulator.convertImplicitToExplicitHydrogens(molRead);
	        
			//fake peak list containing the smallest peak
			Vector<Peak> peakList = new Vector<Peak>();
			peakList.add(new Peak(smallestPeak, smallestPeak, 0));
			Fragmenter frags = new Fragmenter(peakList, 0.01, 10.0, 1, true, false, true, false);
			List<IAtomContainer> fragsGenerated = frags.generateFragmentsInMemory(molRead, false, 1, false);
			
			
			List<HashMap<Double, String>> posNegMode = new ArrayList<HashMap<Double,String>>();
	        HashMap<Double, String> peaksMap = new HashMap<Double, String>();
	        
	        int count = 0;
	        String peaks = "PK$PEAK: m/z int. rel.int.\n";
			//Render.Draw(molRead, fragsGenerated, file.getName());
			for (IAtomContainer fragment : fragsGenerated) {
				IMolecularFormula formula = MolecularFormulaManipulator.getMolecularFormula(fragment);
				Double mass = Math.round(MolecularFormulaTools.getMonoisotopicMass(formula) * 10000.0)/10000.0;
				
				//System.out.println("ID: " + keggID + " MZ: " + mass + " Intensity: 10000 Relative: 999 " + "Sum Formula: " + formulaString + " Mol/Smiles/InChi: ");
				count++;
				
				SmilesGenerator sg = new SmilesGenerator();
				String smilesFrag =sg.createSMILES(new Molecule(fragment));
				
				SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
				try {
					IAtomContainer molecule = sp.parseSmiles(smilesFrag);
					new StructureRenderer(molecule, "read in smiles");
				}
				catch (Exception e) {
					e.printStackTrace();
				}
		        new StructureRenderer(fragment, "test");
		        peaksMap.put(mass, " 10000.000 999\n");
		        
			}
			
			//now sort the peaks and write into string
			Double[] peakKeys = peaksMap.keySet().toArray(new Double[peaksMap.size()]);
			Arrays.sort(peakKeys);
			
			for (int i = 0; i < peakKeys.length; i++) {
				peaks += "  " + peakKeys[i] + peaksMap.get(peakKeys[i]); 
			}
			
			
			String accesion = "KC" + Number.numberToFixedLength(Integer.parseInt(keggID.substring(1)), 6);
			String date = "2009.03.25";
			String author = "MetFrag Revision: 388";
			String copyright = "Institute of Plant Biochemistry, Halle, Germany";
			String namesArray[] = KeggWebservice.KEGGgetNameByCpd(keggID);
			String name = "";
			for (int i = 0; i < namesArray.length; i++) {
				name += "CH$NAME: " + namesArray[i] + "\n";
			}
			
			IMolecularFormula formulaOrig = MolecularFormulaManipulator.getMolecularFormula(molRead);
			String formulaStringOrig = MolecularFormulaManipulator.getString(formulaOrig);
			Double mass = MolecularFormulaTools.getMonoisotopicMass(formulaOrig);
			
	        InChIGenerator gen = InChIGeneratorFactory.getInstance().getInChIGenerator(molRead);
	        String iupac = gen.getInchi();
			
			
			SmilesGenerator generatorSmiles = new SmilesGenerator();
		    IAtomContainer molecule = AtomContainerManipulator.removeHydrogens(molRead);
		    String smiles = generatorSmiles.createSMILES(new Molecule(molecule));
			
		    String instrument = "Synthetical fragmentation";
			String collisionEnergy = "0 eV";
			
			peaks = "PK$NUM_PEAK: " + count + "\n" +peaks + "//";
			
			FileWriter fstream = new FileWriter(keggID + ".txt");
	        BufferedWriter out = new BufferedWriter(fstream);
	        out.write("ACCESSION: " + accesion + "\nRECORD_TITLE: " + keggID + "\nDATE: " + date + "\nAUTHORS: " + author + "\nCOPYRIGHT: " + copyright
	        		+ "\n" + name + "CH$FORMULA: " + formulaStringOrig + "\nCH$EXACT_MASS: " + mass + "\nCH$SMILES: " + smiles
	        		+ "\nCH$IUPAC: " + iupac + "\nCH$LINK: KEGG " + keggID + "\nAC$INSTRUMENT: " + instrument + "\nAC$ANALYTICAL_CONDITION: COLLISION_ENERGY " + collisionEnergy
	        		+ "\n" + peaks);
	        //Close the output stream
	        out.close();
			
			
			
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CDKException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
	
		String driver = "com.mysql.jdbc.Driver"; 
		String path = "/vol/data/pathways/kegg/mol/";
		
		// Connection
	    java.sql.Connection conTemp = null; 
	    
	    //number of threads depending on the available processors
	    //int threads = Runtime.getRuntime().availableProcessors();
	    
	    //thread executor
	    //ExecutorService threadExecutor = null;
		
		try
		{
			//args[0] - start ID the number of the file, an Integer
			Integer start = Integer.parseInt(args[0]);
			//args[1] - end ID --> Integer
			Integer end = Integer.parseInt(args[1]);
			//args[1] - end ID --> smallest peak
			Double smallestPeak = Double.parseDouble(args[2]);
			
			
			Class.forName(driver); 
			DriverManager.registerDriver (new com.mysql.jdbc.Driver()); 
	        // JDBC-driver
	        Class.forName(driver);
	        //databse data
	        
	        try
	        {
		        Config c = new Config();
		        String url = c.getJdbc();
		        String username = c.getUsername();
		        String password = c.getPassword();
		        conTemp = DriverManager.getConnection(url, username, password);
	        }
	        catch (Exception e) {
				e.printStackTrace();
			}
	        
	        KeggPreprocess keggPP = new KeggPreprocess(path, conTemp);
	
			//readCompounds = new HashMap<Integer, IAtomContainer>();
			
			//loop over all files in folder
			File f = new File(path);
			File files[] = f.listFiles();
			Arrays.sort(files);
			

			for (int i = start; i < end; i++) {
				if(files[i].isFile())
					keggPP.run(files[i], smallestPeak);
			}
			
				
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
