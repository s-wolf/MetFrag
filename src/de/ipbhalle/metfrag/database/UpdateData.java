package de.ipbhalle.metfrag.database;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.aromaticity.CDKHueckelAromaticityDetector;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IChemModel;
import org.openscience.cdk.interfaces.IChemSequence;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;

import de.ipbhalle.metfrag.main.Config;

public class UpdateData {
	
	Map<String, Boolean> map;
	
	public UpdateData()
	{
		map = new HashMap<String, Boolean>(2500000);
		generateMap();
	}
	
	private void generateMap() {
		try{
		    // Open the file that is the first 
		    FileInputStream fstream = new FileInputStream("/home/swolf/metfragLongs.csv");
		    // Get the object of DataInputStream
		    DataInputStream in = new DataInputStream(fstream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    String id;
		    //Read File Line By Line
		    while ((id = br.readLine()) != null)   {
//		      System.out.println (id);
		      map.put(id, true);
		    }
		    in.close();
	    }catch (Exception e){//Catch exception if any
	      System.err.println("Error: " + e.getMessage());
	      
	    }
	}
	
	public boolean isContained(String id)
	{
		boolean ret = false;
		ret = map.containsKey(id);
		return ret;
	}
	
	
	
	public static void main(String[] args) {
		
//		UpdateData test = new UpdateData();
//		test.generateMap();
//		System.out.println(test.isContained("C16687"));
		
		
		Connection con;
		String driver = "com.mysql.jdbc.Driver"; 
	
		try{
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
			
			
		    // Open the file that is the first 
		    FileInputStream fstream = new FileInputStream("/home/swolf/metfragLongs.csv");
		    // Get the object of DataInputStream
		    DataInputStream in = new DataInputStream(fstream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    String id;
		    //Read File Line By Line
		    while ((id = br.readLine()) != null)   {
		      
		      File file = null;
		      String path = null;
		      //kegg
		      if(id.startsWith("C"))
		      {
//		    	  	System.out.println (id);
			  		path = "/vol/mirrors/kegg/mol/";		
			  		InputStream ins = null;
			  		try
			  		{
			  			file = new File(path + id + ".mol");
			  			ins = new BufferedInputStream(new FileInputStream(path + file.getName()));
			  		}
			  		catch(FileNotFoundException ee)
			  		{
			  			System.err.println("File not found: " + path + id + ".mol");
			  			continue;
			  		}
		  			
		  			
		  			
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
				    
				    SmilesGenerator generatorSmiles = new SmilesGenerator();
				    IAtomContainer molecule = AtomContainerManipulator.removeHydrogens(molRead);
				    String smiles = generatorSmiles.createSMILES(new Molecule(molecule));
				    
				    PreparedStatement pstmt = con.prepareStatement("UPDATE RECORD SET smiles = ? WHERE id = ?");
			        pstmt.setString(1, smiles);
			        pstmt.setString(2, id);
//			        System.out.println(pstmt.toString());
			        pstmt.executeUpdate();
		      }
		      //Beilstein
		      else if(id.startsWith("B"))
		      {
//		    	  	path = "/vol/mirrors/INP/";
//		    	  	file = new File(path + id + ".mol");
		      }
		      //PubChem
		      else
		      {
		    	  
		      }
		      
		      
		      	
			  
		    	  
		    }
		    //Close the input stream
		    in.close();
		    con.close();
	    }catch (Exception e){//Catch exception if any
	      System.err.println("Error: " + e.getMessage());
	    }
	}

}
