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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.iterator.IteratingMDLReader;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

import de.ipbhalle.metfrag.main.Config;



import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class PubChemToDatabaseParallel implements Runnable {
	
	private static Connection con;
	private String path;
	private String file;
	
	public PubChemToDatabaseParallel(String path, String filename) {
		this.path = path;
		this.file = filename;
	}
	
	//insert the data in postgres database
	@Override public void run()
	{
		try {
			System.out.println("Processing: " + path + file);
			
			Statement stmt = null;
		    stmt = con.createStatement();
			
			File sdfFile = new File(path + file);
			IteratingMDLReader reader = new IteratingMDLReader(new GZIPInputStream(new FileInputStream(sdfFile)), DefaultChemObjectBuilder.getInstance());

			int count = 0;
					
			while (reader.hasNext()) {
				IAtomContainer molecule = (IAtomContainer)reader.next();
				  
			    Map<Object, Object> properties = molecule.getProperties();
			    
			    //RECORD data
			    String smiles = (String)properties.get("PUBCHEM_OPENEYE_CAN_SMILES");
			    Integer pubChemID = Integer.parseInt((String)properties.get("PUBCHEM_COMPOUND_CID"));			    
			    double exactMass = Double.parseDouble((String)properties.get("PUBCHEM_EXACT_MASS"));
			    String molecularFormula = (String)properties.get("PUBCHEM_MOLECULAR_FORMULA");
			    String inchi = (String)properties.get("PUBCHEM_NIST_INCHI");
			    String inchiKey = (String)properties.get("PUBCHEM_NIST_INCHIKEY");
			    //this should plit in 3 parts
			    String inchiKeyArray[] = inchiKey.split("-");
			    
			    //first check if the compound already exists (do not insert the same compound from different databases in the compound table)
			    PreparedStatement pstmtCheck = con.prepareStatement("SELECT compound_id from compound where inchi_key_1 = ? and inchi_key_2 = ? and inchi_key_3 = ?");
		        pstmtCheck.setString(1, inchiKeyArray[0]);
		        pstmtCheck.setString(2, inchiKeyArray[1]);
		        pstmtCheck.setString(3, inchiKeyArray[2]);
		        ResultSet res = pstmtCheck.executeQuery();
		        Integer compoundID = null;
		        while(res.next()){
		        	compoundID = res.getInt(1);
		        }
		        
		        //no previously inserted compound matches
		        if(compoundID == null || compoundID == 0)
		        {
		        	//now get the next insert id
				    ResultSet rs = stmt.executeQuery("SELECT nextval('compound_compound_id_seq')");
				    rs.next();
				    compoundID = rs.getInt(1);
				    
				    PreparedStatement pstmt = con.prepareStatement("INSERT INTO compound (compound_id, mol_structure, exact_mass, formula, smiles, inchi, inchi_key_1, inchi_key_2, inchi_key_3) VALUES (?, '" + inchi + "' ,?,?,?,?,?,?,?)");
			        pstmt.setInt(1, compoundID);
			        pstmt.setDouble(2, exactMass);
			        pstmt.setString(3, molecularFormula);
			        pstmt.setString(4, smiles);
			        pstmt.setString(5, inchi);
			        pstmt.setString(6, inchiKeyArray[0]);
			        pstmt.setString(7, inchiKeyArray[1]);
			        pstmt.setString(8, inchiKeyArray[2]);
			        
			        pstmt.executeUpdate();
				    
		        }
		        
		        //now get the next substance_id
			    ResultSet rs = stmt.executeQuery("SELECT nextval('substance_substance_id_seq')");
			    rs.next();
			    Integer substanceID = rs.getInt(1);
		        
		        //insert the information also in substance table
		        PreparedStatement pstmt = con.prepareStatement("INSERT INTO substance (substance_id, library_id, compound_id, accession) VALUES (?,?,?,?)");
		        //pubchem has library id = 2
		        pstmt.setInt(1, substanceID);
		        pstmt.setInt(2, 2);
		        pstmt.setInt(3, compoundID);
		        pstmt.setString(4, Integer.toString(pubChemID));		        
		        pstmt.executeUpdate();

		        //iupac name data
			    String name = (String)properties.get("PUBCHEM_IUPAC_NAME");
				if(name != null && name != "")
				{
					PreparedStatement pstmtName = con.prepareStatement("INSERT INTO name (substance_id, name) VALUES (?,?)");
			        pstmtName.setInt(1, substanceID);
			        pstmtName.setString(2, name);
			        pstmtName.executeUpdate();
				}
			    count++;
			}
			System.out.println("DONE: " + path + file);
			System.out.println("Got " + count + " structures!");
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
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
	
	
//	@Override public void run()
//	{
//		try {
//			System.out.println("Processing: " + path + file);
//			
//			Statement stmt = null;
//		    stmt = con.createStatement();
//			
//			File sdfFile = new File(path + file);
//			IteratingMDLReader reader = new IteratingMDLReader(new GZIPInputStream(new FileInputStream(sdfFile)), DefaultChemObjectBuilder.getInstance());
//
//			int count = 0;
//					
//			while (reader.hasNext()) {
//				IAtomContainer molecule = (IAtomContainer)reader.next();
//				  
//			    Map<Object, Object> properties = molecule.getProperties();
//			    
//			    //RECORD data
//			    String smiles = (String)properties.get("PUBCHEM_OPENEYE_CAN_SMILES");
//			    Integer pubChemID = Integer.parseInt((String)properties.get("PUBCHEM_COMPOUND_CID"));			    
//			    double exactMass = Double.parseDouble((String)properties.get("PUBCHEM_EXACT_MASS"));
//			    String molecularFormula = (String)properties.get("PUBCHEM_MOLECULAR_FORMULA");
//			    String inchi = (String)properties.get("PUBCHEM_NIST_INCHI");
//			    int chonsp = Tools.checkCHONSP(molecularFormula);
//			    
//		        Date date = new Date();
//		        java.sql.Date dateSQL = new java.sql.Date(date.getTime());
//			    
//		        
////		        stmt.executeUpdate("INSERT INTO RECORD (ID, DATE, FORMULA, EXACT_MASS, SMILES, IUPAC, CHONSP) " +
////		        		"VALUES ('" + pubChemID.toString() +  "','" + dateSQL + "','" + molecularFormula + "'," + exactMass + ",'" + smiles + "','" + inchi + "',"  + chonsp + ")");
//		        
//		        PreparedStatement pstmt = con.prepareStatement("INSERT INTO RECORD (ID, DATE, FORMULA, EXACT_MASS, SMILES, IUPAC, CHONSP) VALUES (?,?,?,?,?,?,?)");
//		        pstmt.setString(1, pubChemID.toString());
//		        pstmt.setDate(2, dateSQL);
//		        pstmt.setString(3, molecularFormula);
//		        pstmt.setDouble(4, exactMass);
//		        pstmt.setString(5, smiles);
//		        pstmt.setString(6, inchi);
//		        pstmt.setInt(7, chonsp);
//		        
//		        pstmt.executeUpdate();
//
//		        
//		        
//		        //name data
//			    Map<String, String> names = new HashMap<String, String>();
//			    names.put("PUBCHEM_IUPAC_CAS_NAME", (String)properties.get("PUBCHEM_IUPAC_CAS_NAME"));
//			    names.put("PUBCHEM_IUPAC_OPENEYE_NAME", (String)properties.get("PUBCHEM_IUPAC_OPENEYE_NAME"));
//			    names.put("PUBCHEM_IUPAC_CAS_NAME", (String)properties.get("PUBCHEM_IUPAC_CAS_NAME"));
//			    names.put("PUBCHEM_IUPAC_NAME", (String)properties.get("PUBCHEM_IUPAC_NAME"));
//			    names.put("PUBCHEM_IUPAC_SYSTEMATIC_NAME", (String)properties.get("PUBCHEM_IUPAC_SYSTEMATIC_NAME"));
//			    names.put("PUBCHEM_IUPAC_TRADITIONAL_NAME", (String)properties.get("PUBCHEM_IUPAC_TRADITIONAL_NAME"));
//			    for (String name : names.values()) {
//					if(name != null && name != "")
//					{
//						PreparedStatement pstmtName = con.prepareStatement("INSERT INTO CH_NAME (ID, NAME) VALUES (?,?)");
//				        pstmtName.setString(1, pubChemID.toString());
//				        pstmtName.setString(2, name);
//				        pstmtName.executeUpdate();
////						stmt.executeUpdate("INSERT INTO CH_NAME (ID, NAME) VALUES (\"" + pubChemID.toString() + "\",\"" + name + "\")");
//					}
//				}
//			    
//			    
//			    //link data
//			    PreparedStatement pstmtLink = con.prepareStatement("INSERT INTO CH_LINK (ID, PUBCHEM) VALUES (?,?)");
//		        pstmtLink.setString(1, pubChemID.toString());
//		        pstmtLink.setString(2, "CID:" + pubChemID.toString());
//		        pstmtLink.executeUpdate();
////			    stmt.executeUpdate("INSERT INTO CH_LINK (ID, PUBCHEM) VALUES ('" + pubChemID.toString() + "','" + "CID:" + pubChemID.toString() + "')");
//
//
//			    count++;
//			}
//			System.out.println("DONE: " + path + file);
//			System.out.println("Got " + count + " structures!");
//		} catch (NumberFormatException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	
	public static void main(String[] args) {
		
		String[] files = null;
		
		if(args[0] != null)
		{
			files = args[0].split(";");
		}
		else
		{
			System.err.println("Error no argument (filename) given!");
			System.exit(1);
		}
		
		boolean isMysql = false;
		String path = "/vol/mirrors/pubchem/";		
		//starting from this id the files are read in again!
		//be shure to delete larger ids!!!
		String startID = "000000001";
		
		// Connection
	    java.sql.Connection conTemp = null; 
	    
	    //number of threads depending on the available processors
	    int threads = Runtime.getRuntime().availableProcessors();
	    
	    //thread executor
	    ExecutorService threadExecutor = null;
		
		try
		{
			if(isMysql)
			{
				String driver = "com.mysql.jdbc.Driver"; 
				Class.forName(driver); 
				DriverManager.registerDriver(new com.mysql.jdbc.Driver()); 
		        // JDBC-driver
		        Class.forName(driver);
		        //databse data
		        Config c = new Config();
		        String url = c.getJdbc();
		        String username = c.getUsername();
		        String password = c.getPassword();
		        con = DriverManager.getConnection(url, username, password);
			    
		
				//readCompounds = new HashMap<Integer, IAtomContainer>();
				
				//loop over all files in folder
//				File f = new File(path);
//				File files[] = f.listFiles();
//				Arrays.sort(files);
				
				//queue stores all files to be read in
				Queue<String> queue = new LinkedList<String>();
				for (int i = 0; i < files.length; i++) {
					queue.offer(files[i]);
				}
				
				threadExecutor = Executors.newFixedThreadPool(threads);
				
				while(!queue.isEmpty())
				{
					threadExecutor.execute(new PubChemToDatabaseParallel(path, queue.poll()));
				}
				
				threadExecutor.shutdown();
			}
			else
			{
				String driver = "org.postgresql.Driver"; 
				Class.forName(driver); 
				DriverManager.registerDriver(new org.postgresql.Driver()); 
		        //databse data
		        Config c = new Config();
		        String url = c.getJdbcPostgres();
		        String username = c.getUsernamePostgres();
		        String password = c.getPasswordPostgres();
		        con = DriverManager.getConnection(url, username, password);
			    
		
				//readCompounds = new HashMap<Integer, IAtomContainer>();
				
				//loop over all files in folder
//				File f = new File(path);
//				File files[] = f.listFiles();
//				Arrays.sort(files);
				
				//queue stores all files to be read in
				Queue<String> queue = new LinkedList<String>();
				for (int i = 0; i < files.length; i++) {
					queue.offer(files[i]);
				}
				
				threadExecutor = Executors.newFixedThreadPool(threads);
				
				while(!queue.isEmpty())
				{
					threadExecutor.execute(new PubChemToDatabaseParallel(path, queue.poll()));
				}
				
				while(!threadExecutor.isTerminated())
				{
					try {
					       Thread.currentThread().sleep(1000);
					}
					catch (InterruptedException e) {
					    e.printStackTrace();
					}
				}
				threadExecutor.shutdown();
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
		finally 
		{
		      try 
		      {
		    	  if(con != null)
		    		  con.close();
		      } 
		      catch(SQLException e) {}
		}

		
		while(!threadExecutor.isTerminated())
		{
			try {
				Thread.currentThread().sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//sleep for 1000 ms
		}
		
		threadExecutor.shutdown();
		
	}

}
