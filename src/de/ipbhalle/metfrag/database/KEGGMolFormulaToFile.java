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
package de.ipbhalle.metfrag.database;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.aromaticity.CDKHueckelAromaticityDetector;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.inchi.InChIGenerator;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IChemModel;
import org.openscience.cdk.interfaces.IChemSequence;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

import de.ipbhalle.metfrag.tools.MolecularFormulaTools;

public class KEGGMolFormulaToFile {
	
	private String folder = "";
	private String outputFolder = "";
	
	public KEGGMolFormulaToFile(String keggFolder, String outputFolder)
	{
		this.folder = keggFolder;
		this.outputFolder = outputFolder;
	}
	
	
	public void run()
	{
		try{
			System.out.println("Processing: " + folder + "compound");
			
			String ret = "";
			
			// Open the file that is the first 
		    // command line parameter
		    FileInputStream fstream = new FileInputStream(this.folder + "compound");
		    // Get the object of DataInputStream
		    DataInputStream in = new DataInputStream(fstream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    String strLine;

		    BufferedWriter out = new BufferedWriter(new FileWriter(this.outputFolder, true)); 
		    
		    
		    String keggID = "";
	    	String formula = "";
	    	String mass = "";
		    boolean checkFirst = false;
		    boolean checkSecond = false;
		    boolean checkThird = false;
	    	
		    //Read File Line By Line
		    while ((strLine = br.readLine()) != null)   {
		    	
	    		//kegg id
	    		if(strLine.startsWith("ENTRY"))
	    		{
	    			checkFirst = true;
	    			keggID = strLine.substring(12,18);
	    		}
	    		if(strLine.startsWith("FORMULA"))
	    		{
	    			checkSecond = true;
	    			formula = strLine.substring(12);
	    		}
	    		
	    		if(strLine.startsWith("MASS"))
	    		{
	    			checkThird = true;
	    			mass = strLine.substring(12);
	    		}
	    			
	    		if(checkFirst && checkSecond && checkThird)
	    		{
	    			checkFirst = false;
	    			checkSecond = false;
	    			checkThird = false;
	    			out.write(keggID + "," + formula + "," + mass + "\n"); 
	    		}
		    }
		    
		    out.close();
		    
		    //Close the input stream
		    in.close();
			
			
//			String keggID = file.getName().split("\\.")[0];
			
			
		    //InputStream ins = new BufferedInputStream(new FileInputStream(path + file.getName()));
//		    InputStream ins = new BufferedInputStream(new FileInputStream(folder + file.getName()));
//		    MDLV2000Reader reader = new MDLV2000Reader(ins);
//	        ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());
//	        IChemSequence sequence = chemFile.getChemSequence(0);
//	        IChemModel model = sequence.getChemModel(0);
//	        IAtomContainer molRead = model.getMoleculeSet().getAtomContainer(0);
//			
//			CDKHueckelAromaticityDetector.detectAromaticity(molRead);
//	        CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(molRead.getBuilder());
//	        for (IAtom atom : molRead.atoms()) {
//				IAtomType type = matcher.findMatchingAtomType(molRead, atom);
//		        AtomTypeManipulator.configure(atom, type);
//			}
//	        
//	        CDKHydrogenAdder hAdder = CDKHydrogenAdder.getInstance(molRead.getBuilder());
//	        hAdder.addImplicitHydrogens(molRead);
//	        AtomContainerManipulator.convertImplicitToExplicitHydrogens(molRead);
//	        
//							        
//	        IMolecularFormula formulaOrig = MolecularFormulaManipulator.getMolecularFormula(molRead);
//			String formulaStringOrig = MolecularFormulaManipulator.getString(formulaOrig);
//			Double mass = MolecularFormulaTools.getMonoisotopicMass(formulaOrig);
//			int chonsp = Tools.checkCHONSP(formulaStringOrig);
			
			
//	        InChIGenerator gen = InChIGeneratorFactory.getInstance().getInChIGenerator(molRead);
//	        String iupac = gen.getInchi();
			
			
//			SmilesGenerator generatorSmiles = new SmilesGenerator();
//		    IAtomContainer molecule = AtomContainerManipulator.removeHydrogens(molRead);
//		    String smiles = generatorSmiles.createSMILES(new Molecule(molecule));
	        
		   

		} catch (NumberFormatException e) {
			System.err.println("Error: ");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.err.println("Error: ");
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("Error: ");
			e.printStackTrace();
		}
	
	}
	
	public static void main(String[] args) {
		String keggFolder = null;
		String output = null;
		
		if(args[0] != null && args[1] != null)
		{
			keggFolder = args[0];
			output = args[1];
		}
		else
		{
			System.err.println("Error no argument (filename) given! \n Standard: \"/vol/mirrors/kegg/mol/\" \"/home/swolf/carsten/kegg.csv\"");
			System.exit(1);
		}
		
		
		KEGGMolFormulaToFile k = new KEGGMolFormulaToFile(keggFolder, output);
		k.run();
//		File f = new File(keggFolder);
//		File[] files = f.listFiles();
//		for (int i = 0; i < files.length; i++) {
//			File file = new File(keggFolder + files[i]);
//			
//		}
	}

}
