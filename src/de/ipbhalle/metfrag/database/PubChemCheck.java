package de.ipbhalle.metfrag.database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.ipbhalle.metfrag.main.Config;

public class PubChemCheck {

	private static Connection con;
	
	public static void main(String[] args) {
			
		String[] files = null;
		
		String driver = "com.mysql.jdbc.Driver"; 
		String path = "/vol/mirrors/pubchem/";		
		
		
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
		    
	        
	        UpdateData ud = new UpdateData();
	        Map<String, Boolean> map = ud.getMap();
	        for (String string : map.keySet()) {
	        	PreparedStatement pstmt = con.prepareStatement("SELECT ID FROM RECORD WHERE ID = ? and DATE < '2010' limit 1");
			    pstmt.setString(1, string);
			    ResultSet rs = pstmt.executeQuery();
			    while(rs.next())
	            {
			    	System.out.println(rs.getString("ID"));
	            }
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
