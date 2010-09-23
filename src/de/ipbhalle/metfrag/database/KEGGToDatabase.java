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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.sf.jniinchi.INCHI_KEY;
import net.sf.jniinchi.JniInchiInput;
import net.sf.jniinchi.JniInchiWrapper;
import net.sf.jniinchi.LoadNativeLibraryException;

import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.DefaultChemObjectBuilder;
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
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

import de.ipbhalle.metfrag.fragmenter.Fragmenter;
import de.ipbhalle.metfrag.keggWebservice.KeggWebservice;
import de.ipbhalle.metfrag.main.Config;
import de.ipbhalle.metfrag.massbankParser.Peak;
import de.ipbhalle.metfrag.tools.MolecularFormulaTools;
import de.ipbhalle.metfrag.tools.Number;

public class KEGGToDatabase {
	
	private String folder = "";
	private static Connection con;
	
	public KEGGToDatabase(String folder)
	{
		this.folder = folder;
	}
	
	
	public void run(File file)
	{
		try{
			
			
			
			System.out.println("Processing: " + folder + file.getName());
			
			String keggID = file.getName().split("\\.")[0];
			
			Statement stmt = null;
		    stmt = con.createStatement();
			
		    //InputStream ins = new BufferedInputStream(new FileInputStream(path + file.getName()));
		    InputStream ins = new BufferedInputStream(new FileInputStream(folder + file.getName()));
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
	        
						
			Date date = new Date();
	        java.sql.Date dateSQL = new java.sql.Date(date.getTime());
			
	        
	        IMolecularFormula formulaOrig = MolecularFormulaManipulator.getMolecularFormula(molRead);
			String formulaStringOrig = MolecularFormulaManipulator.getString(formulaOrig);
			Double mass = MolecularFormulaTools.getMonoisotopicMass(formulaOrig);			
			
	        InChIGenerator gen = InChIGeneratorFactory.getInstance().getInChIGenerator(molRead);
	        String inchi = gen.getInchi();
			String inchiKey = gen.getInchiKey();
			String inchiKeyArray[] = inchiKey.split("-");
	        
			
			SmilesGenerator generatorSmiles = new SmilesGenerator();
		    IAtomContainer molecule = AtomContainerManipulator.removeHydrogens(molRead);
		    String smiles = generatorSmiles.createSMILES(new Molecule(molecule));
	        
			ResultSet rs = stmt.executeQuery("SELECT nextval('compound_compound_id_seq')");
		    rs.next();
		    Integer compoundID = rs.getInt(1);
		    
		    PreparedStatement pstmtCompound = con.prepareStatement("INSERT INTO compound (compound_id, mol_structure, exact_mass, formula, smiles, inchi, inchi_key_1, inchi_key_2, inchi_key_3) VALUES (?,cast(? as molecule),?,?,?,?,?,?,?)");
		    pstmtCompound.setInt(1, compoundID);
	        pstmtCompound.setString(2, inchi);
	        pstmtCompound.setDouble(3, mass);
	        pstmtCompound.setString(4, formulaStringOrig);
	        pstmtCompound.setString(5, smiles);
	        pstmtCompound.setString(6, inchi);
	        pstmtCompound.setString(7, inchiKeyArray[0]);
	        pstmtCompound.setString(8, inchiKeyArray[1]);
	        pstmtCompound.setString(9, "");
	        pstmtCompound.executeUpdate();
			
	        
	        
	        //now get the next substance_id
		    rs = stmt.executeQuery("SELECT nextval('substance_substance_id_seq')");
		    rs.next();
		    Integer substanceID = rs.getInt(1);
	        
	        //insert the information also in substance table
	        
	        //pubchem has library id = 2
		    PreparedStatement pstmtSubstance = con.prepareStatement("INSERT INTO substance (substance_id, library_id, compound_id, accession) VALUES (?,?,?,?)");
		    pstmtSubstance.setInt(1, substanceID);
	        pstmtSubstance.setInt(2, 1);
	        pstmtSubstance.setInt(3, compoundID);
	        pstmtSubstance.setString(4, keggID);	
	        pstmtSubstance.executeUpdate();
	        
	        //now insert all strings into database
			String namesArray[] = KeggWebservice.KEGGgetNameByCpdLocally(keggID, "/vol/mirrors/kegg/compound");
			for (int i = 0; i < namesArray.length; i++) {
				PreparedStatement pstmtName = con.prepareStatement("INSERT INTO name (substance_id, name) VALUES (?,?)");
		        pstmtName.setInt(1, substanceID);
		        pstmtName.setString(2, namesArray[i]);
		        pstmtName.executeUpdate();
			}
			
			
			
			

		} catch (NumberFormatException e) {
			System.err.println("File: " + file.getName());
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.err.println("File: " + file.getName());
			e.printStackTrace();
		} catch (SQLException e) {
			System.err.println("File: " + file.getName());
			e.printStackTrace();
		} catch (CDKException e) {
			System.err.println("File: " + file.getName());
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("File: " + file.getName());
			e.printStackTrace();
		}
	
	}
	
	public static void main(String[] args) {
		String[] files = null;
		
		if(args[0] != null)
		{
			files = args[0].split("-");
		}
		else
		{
			System.err.println("Error no argument (filename) given!");
			System.exit(1);
		}
		
		String driver = "com.mysql.jdbc.Driver"; 
		String path = "/vol/mirrors/kegg/mol/";		
		
		
		try
		{
			Class.forName(driver); 
			DriverManager.registerDriver (new com.mysql.jdbc.Driver()); 
	        // JDBC-driver
	        Class.forName(driver);
	        //databse data
	        Config c = new Config();
	        String url = c.getJdbcPostgres();
	        String username = c.getUsernamePostgres();
	        String password = c.getPasswordPostgres();
	        con = DriverManager.getConnection(url, username, password);
		    
	        
	        KEGGToDatabase k = new KEGGToDatabase(path);
			
			for (int i = 0; i < files.length; i++) {
				File file = new File(path + files[i]);
				k.run(file);
			}
			
				
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
