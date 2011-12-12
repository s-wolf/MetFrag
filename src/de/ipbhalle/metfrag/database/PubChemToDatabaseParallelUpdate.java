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

public class PubChemToDatabaseParallelUpdate implements Runnable {
	
	private static Connection con;
	private String path;
	private String file;
	
	public PubChemToDatabaseParallelUpdate(String path, String filename) {
		this.path = path;
		this.file = filename;
	}
	
	
	public void run()
	{
		try {
			System.out.println("Processing: " + path + file);
			
			Statement stmt = null;
		    stmt = con.createStatement();
			
			File sdfFile = new File(path + file);
			IteratingMDLReader reader = new IteratingMDLReader(new GZIPInputStream(new FileInputStream(sdfFile)), DefaultChemObjectBuilder.getInstance());

			int count = 0;
			
			UpdateData ud = new UpdateData();
			
					
			while (reader.hasNext()) {
				IAtomContainer molecule = (IAtomContainer)reader.next();
				  
			    Map<Object, Object> properties = molecule.getProperties();
			    
			    //RECORD data
			    String smiles = (String)properties.get("PUBCHEM_OPENEYE_CAN_SMILES");
			    
			    Integer pubChemID = Integer.parseInt((String)properties.get("PUBCHEM_COMPOUND_CID"));			    
			    if(!ud.isContained(pubChemID.toString()))
			    	continue;
			    
			    double exactMass = Double.parseDouble((String)properties.get("PUBCHEM_EXACT_MASS"));
			    String molecularFormula = (String)properties.get("PUBCHEM_MOLECULAR_FORMULA");
			    String inchi = (String)properties.get("PUBCHEM_NIST_INCHI");
			    int chonsp = Tools.checkCHONSP(molecularFormula);
			    
		        Date date = new Date();
		        java.sql.Date dateSQL = new java.sql.Date(date.getTime());
			    
		        
//		        stmt.executeUpdate("INSERT INTO RECORD (ID, DATE, FORMULA, EXACT_MASS, SMILES, IUPAC, CHONSP) " +
//		        		"VALUES ('" + pubChemID.toString() +  "','" + dateSQL + "','" + molecularFormula + "'," + exactMass + ",'" + smiles + "','" + inchi + "',"  + chonsp + ")");
		        
		        PreparedStatement pstmt = con.prepareStatement("UPDATE RECORD SET smiles = ?, date = ? where id = ?");
		        pstmt.setString(1, smiles);
		        pstmt.setDate(2, dateSQL);
		        pstmt.setString(3, pubChemID.toString());
//		        System.out.println(pstmt.toString());
		        pstmt.executeUpdate();

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
		
		String driver = "com.mysql.jdbc.Driver"; 
		String path = "/vol/mirrors/pubchem/";		
		//starting from this id the files are read in again!
		//be shure to delete larger ids!!!
		String startID = "00000001";
		
		// Connection
	    java.sql.Connection conTemp = null; 
	    
	    //number of threads depending on the available processors
	    int threads = Runtime.getRuntime().availableProcessors();
	    
	    //thread executor
	    ExecutorService threadExecutor = null;
		
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
		    
	
			//readCompounds = new HashMap<Integer, IAtomContainer>();
			
			//loop over all files in folder
//			File f = new File(path);
//			File files[] = f.listFiles();
//			Arrays.sort(files);
			
			//queue stores all files to be read in
			Queue<String> queue = new LinkedList<String>();
			for (int i = 0; i < files.length; i++) {
				queue.offer(files[i]);
			}
			
			threadExecutor = Executors.newFixedThreadPool(threads);
			
			while(!queue.isEmpty())
			{
				threadExecutor.execute(new PubChemToDatabaseParallelUpdate(path, queue.poll()));
			}
			
			threadExecutor.shutdown();
			
				
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
