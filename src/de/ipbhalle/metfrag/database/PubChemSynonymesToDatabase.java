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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.zip.GZIPInputStream;

import de.ipbhalle.metfrag.main.Config;

public class PubChemSynonymesToDatabase {
	
	private static Connection con;
	
	public static void main(String[] args) {
		
		String driver = "com.mysql.jdbc.Driver"; 
		String path = "/home/swolf/workspaceGalileo/CID-Synonym.gz";	
		
		try {
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
			
	        String ret = "";
		    String line;
		    // Now read lines of text: the BufferedReader puts them in lines,
		    // the InputStreamReader does Unicode conversion, and the
		    // GZipInputStream "gunzip"s the data from the FileInputStream.
	    
	    	FileInputStream fin = new FileInputStream(path);
		    GZIPInputStream gzis = new GZIPInputStream(fin);
		    InputStreamReader xover = new InputStreamReader(gzis);
		    BufferedReader is = new BufferedReader(xover);
		    boolean hack = true;
			while ((line = is.readLine()) != null)
			{
				if(hack)
				{
					hack = false;
					continue;
				}
				String[] lineSplit = line.split("\t");
				PreparedStatement pstmtName = con.prepareStatement("INSERT INTO CH_NAME (ID, NAME) VALUES (?,?)");
		        pstmtName.setString(1, lineSplit[0]);
		        pstmtName.setString(2, lineSplit[1]);
		        pstmtName.executeUpdate();				                
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        
        
		
	}

}
