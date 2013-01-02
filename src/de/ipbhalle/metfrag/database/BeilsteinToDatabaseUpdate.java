/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package de.ipbhalle.metfrag.database;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

import de.ipbhalle.metfrag.keggWebservice.KeggWebservice;
import de.ipbhalle.metfrag.main.Config;
import de.ipbhalle.metfrag.tools.MolecularFormulaTools;
import de.ipbhalle.metfrag.tools.Number;

public class BeilsteinToDatabaseUpdate {

	private String folder = "";
	private static Connection con;
	
	public BeilsteinToDatabaseUpdate(String folder)
	{
		this.folder = folder;
	}
	
	
	public void run(File file)
	{
			System.out.println("Processing: " + folder + file.getName());
			
			String path = "/vol/mirrors/INP/";
			//loop over all files in folder
			File f = new File(path);
			File files[] = f.listFiles();
			Arrays.sort(files);
			List<File> filesDirsOnly = new ArrayList<File>();
			for (int i = 0; i < files.length; i++) {
				if(files[i].isDirectory() && (files[i].getName().startsWith("0") || files[i].getName().startsWith("1")))
					filesDirsOnly.add(files[i]);
			}
			
			
			
			
			
			
			// Open the file that is the first 
		    // command line parameter
		    FileInputStream fstream;
			try {
				fstream = new FileInputStream(folder + file);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return;
			}
		    // Get the object of DataInputStream
		    DataInputStream in = new DataInputStream(fstream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    String strLine;

		    
		    boolean first = true;
		    String currentMolFolder = "000000";
		    String ID = "";
		    
		    UpdateData ud = new UpdateData();
		    
		    //Read File Line By Line
		    try {
				while ((strLine = br.readLine()) != null)   {
					
					try
					{
				    	if(first)
				    	{
				    		first = false;
				    		continue;
				    	}
				    	
				    	//tab seperated data
				    	String[] lineArray = strLine.split("\t");
				    	if(lineArray.length < 3)
				    		continue;
				    	
				    	if(Integer.parseInt(lineArray[0]) < 1000)
				    		ID = Number.numberToFixedLength(Integer.parseInt(lineArray[0]), 5);
				    	else 
				    		ID = lineArray[0];
				    	
				    	
				    	//only update the shortened ids
				    	if(!ud.isContained("BE" + Number.numberToFixedLength(Integer.parseInt(ID), 6)))
				    		continue;
				    	
				    	if(Integer.parseInt(ID) > Integer.parseInt(currentMolFolder))
				    	{				    		
				    		for (int i = 0; i < (filesDirsOnly.size() - 1); i++) {
				    			
				    			if(i==filesDirsOnly.size() - 2 && Integer.parseInt(filesDirsOnly.get(i + 1).getName()) >  Integer.parseInt(ID))
				    			{
				    				currentMolFolder = filesDirsOnly.get(i).getName();
				    				break;
				    			}
				    			else if(i==filesDirsOnly.size() - 2)
				    			{
				    				currentMolFolder = filesDirsOnly.get(i + 1).getName();
				    				break;
				    			}
				    				
				    			
				    			String temp = filesDirsOnly.get(i).getName();
				    			String temp1 = filesDirsOnly.get(i+1).getName();
				    			if(Integer.parseInt(ID) >= Integer.parseInt(temp) && Integer.parseInt(ID) < Integer.parseInt(temp1))
				    			{
				    				currentMolFolder = filesDirsOnly.get(i).getName();
				    				break;
				    			}
							}
				    	}
				    	
				    	
				    	String[] namesArray = lineArray[3].split(";");
						
						for (int i = 0; i < namesArray.length; i++) {
							Pattern p1 = Pattern.compile("^\\s+");
							Matcher m1 = p1.matcher(namesArray[i]);
							namesArray[i] = m1.replaceAll("");
						}
						
						
						
						Statement stmt = null;
					    stmt = con.createStatement();
						
					    //InputStream ins = new BufferedInputStream(new FileInputStream(path + file.getName()));
					    InputStream ins = null;
					    File ftemp = new File(path + currentMolFolder + "/");
						String temp = ftemp.listFiles()[0].getName().split("_")[1];
					    
					    ins = new BufferedInputStream(new FileInputStream(path + currentMolFolder + "/BS0302AB_" + temp + "_" + ID + ".mol"));
					  
					    
					    MDLV2000Reader reader = new MDLV2000Reader(ins);
				        ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());
				        IChemSequence sequence = chemFile.getChemSequence(0);
				        IChemModel model = sequence.getChemModel(0);
				        IAtomContainer molRead = model.getMoleculeSet().getAtomContainer(0);
						
						CDKHueckelAromaticityDetector.detectAromaticity(molRead);				        
						AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molRead);
						
				        CDKHydrogenAdder hAdder = CDKHydrogenAdder.getInstance(molRead.getBuilder());
				        hAdder.addImplicitHydrogens(molRead);
				        AtomContainerManipulator.convertImplicitToExplicitHydrogens(molRead);
				        
									
						Date date = new Date();
				        java.sql.Date dateSQL = new java.sql.Date(date.getTime());
						
				        
				        IMolecularFormula formulaOrig = MolecularFormulaManipulator.getMolecularFormula(molRead);
						String formulaStringOrig = MolecularFormulaManipulator.getString(formulaOrig);
						Double mass = MolecularFormulaTools.getMonoisotopicMass(formulaOrig);
						int chonsp = Tools.checkCHONSP(formulaStringOrig);
						
						
//				        InChIGenerator gen = InChIGeneratorFactory.getInstance().getInChIGenerator(molRead);
//				        String iupac = gen.getInchi();
						
						
						SmilesGenerator generatorSmiles = new SmilesGenerator();
					    IAtomContainer molecule = AtomContainerManipulator.removeHydrogens(molRead);
					    String smiles = generatorSmiles.createSMILES(new Molecule(molecule));
				        
					    
					    PreparedStatement pstmt = con.prepareStatement("UPDATE RECORD SET smiles = ? where id = ?");
				        pstmt.setString(1, smiles);
//				        pstmt.setString(2, iupac);
				        pstmt.setString(2, "BE" + Number.numberToFixedLength(Integer.parseInt(ID), 6));
//				        System.out.println(pstmt.toString());
				        pstmt.executeUpdate();
					
				    } catch (NumberFormatException e) {
						System.err.println("File: " + ID);
						e.printStackTrace();
					} catch (FileNotFoundException e) {
						System.err.println("File: " + ID);
						e.printStackTrace();
					} catch (SQLException e) {
						System.err.println("File: " + ID);
						e.printStackTrace();
					} catch (CDKException e) {
						System.err.println("File: " + ID);
						e.printStackTrace();
					} catch (Exception e) {
						System.err.println("File: " + ID);
						e.printStackTrace();
					}
					
				}
				
				in.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    //Close the input stream
		    

	}
	
	public static void main(String[] args) {
		String file = null;
		
		if(args[0] != null)
		{
			file = args[0];
		}
		else
		{
			System.err.println("Error no argument (filename) given!");
			System.exit(1);
		}
		
		String driver = "com.mysql.jdbc.Driver"; 
		String path = "/vol/mirrors/INP/data/";		
		
		
		try
		{
			Class.forName(driver); 
			DriverManager.registerDriver (new com.mysql.jdbc.Driver()); 
	        // JDBC-driver
	        Class.forName(driver);
	        //databse data
	        Config c = new Config();
	        String url = c.getJdbc();
	        String username = c.getUsername();
	        String password = c.getPassword();
	        con = DriverManager.getConnection(url, username, password);
		    
	        
	        BeilsteinToDatabaseUpdate k = new BeilsteinToDatabaseUpdate(path);
			k.run(new File(file));
			
			
				
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
