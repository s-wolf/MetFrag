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
import de.ipbhalle.metfrag.tools.Render;

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
			int chonsp = Tools.checkCHONSP(formulaStringOrig);
			
			
			InChIGeneratorFactory factory = new InChIGeneratorFactory();
	        InChIGenerator gen = factory.getInChIGenerator(molRead);
	        String iupac = gen.getInchi();
			
			
			SmilesGenerator generatorSmiles = new SmilesGenerator();
		    IAtomContainer molecule = AtomContainerManipulator.removeHydrogens(molRead);
		    String smiles = generatorSmiles.createSMILES(new Molecule(molecule));
	        
		    
		    PreparedStatement pstmt = con.prepareStatement("INSERT INTO RECORD (ID, DATE, FORMULA, EXACT_MASS, SMILES, IUPAC, CHONSP) VALUES (?,?,?,?,?,?,?)");
	        pstmt.setString(1, keggID);
	        pstmt.setDate(2, dateSQL);
	        pstmt.setString(3, formulaStringOrig);
	        pstmt.setDouble(4, mass);
	        pstmt.setString(5, smiles);
	        pstmt.setString(6, iupac);
	        pstmt.setInt(7, chonsp);
	        pstmt.executeUpdate();
	        
	        //now insert all strings into database
			String namesArray[] = KeggWebservice.KEGGgetNameByCpdLocally(keggID, "/vol/mirrors/kegg/compound");
			for (int i = 0; i < namesArray.length; i++) {
				PreparedStatement pstmtName = con.prepareStatement("INSERT INTO CH_NAME (ID, NAME) VALUES (?,?)");
		        pstmtName.setString(1, keggID);
		        pstmtName.setString(2, namesArray[i]);
		        pstmtName.executeUpdate();
			}
			
			
			//link data
		    PreparedStatement pstmtLink = con.prepareStatement("INSERT INTO CH_LINK (ID, KEGG) VALUES (?,?)");
	        pstmtLink.setString(1, keggID);
	        pstmtLink.setString(2, keggID);
	        pstmtLink.executeUpdate();
			

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
	        String url = c.getJdbc();
	        String username = c.getUsername();
	        String password = c.getPassword();
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
