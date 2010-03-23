package de.ipbhalle.metfrag.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;





public class MysqlMassbank {
	private String driver = "com.mysql.jdbc.Driver"; 
	private Connection con; 
	
	/**
	 * Instantiates a new mysql massbank connection. Opens connection.
	 */
	public MysqlMassbank(String url, String username, String password)
	{
		try{
			Class.forName(driver); 
			DriverManager.registerDriver (new com.mysql.jdbc.Driver()); 
	        // load jdbc driver 
	        Class.forName(driver);

            //connect
            con = DriverManager.getConnection(url, username, password);
		}
		catch (Exception e)
		{
			System.err.println("Error with connection: " + e.getMessage());
		}
	}
	
	/**
	 * Gets the mol to the coressponding massbank reord
	 * 
	 * @param record the record; e.g.: "PB000122" for naringenin 
	 * 
	 * @return the mol file
	 */
	public String getMol(String record)
	{
		String mol = "";
		
		try
		{
		    ResultSet rs = null;		    
		    
		    PreparedStatement pstmt = con.prepareStatement("select * from CH_NAME c join MOLFILE m using (NAME) where c.ID = ?;");
		    pstmt.setString(1, record);
		    
		    rs = pstmt.executeQuery();
		    while(rs.next())
		    {
		    	//Name
		    	System.out.print(rs.getString(1) + "\t");
		    	//Massbank Entry
		    	System.out.print(rs.getString(2) + "\t");
		    	//Mol file
		    	System.out.print(rs.getString(3) + "\t");
		    	mol = rs.getString(3);
		    	System.out.print("\n");
		    }
		}
		catch(SQLException ex)
		{
			System.err.println("Error SQL: " + ex.getMessage());
		}
		
		return mol;
	}
	
	
	/**
	 * Gets the exact mass. (Bugfix for formula weight in the massbank records)
	 * 
	 * @param record the record
	 * 
	 * @return the exact mass
	 */
	public double getExactMass(String record)
	{
		double mass = 0.0;
		
		try
		{
		    ResultSet rs = null;
		    
		    PreparedStatement pstmt = con.prepareStatement("select EXACT_MASS from RECORD where ID = ?;");
		    pstmt.setString(1, record);

		    rs = pstmt.executeQuery();
		    while(rs.next())
		    {
		    	//Exakte MAsse
		    	//System.out.println("Exakte Masse: " + rs.getDouble(1));
		    	mass = rs.getDouble(1);
		    }
		}
		catch(SQLException ex)
		{
			System.err.println("Error SQL: " + ex.getMessage());
		}
		
		return mass;
	}
	
	
	/**
	 * Mysql close.
	 */
	public void MysqlMassbankClose()
	{
		try
		{
			// Verbindung schließen 
	        con.close();
		}
		catch(SQLException e)
		{
			System.err.println("Error with connection: " + e.getMessage());
		}
	}
	
	
	
	
	
	public static void main(String[] args) {

        String driver = "com.mysql.jdbc.Driver"; 
        // Loads the Driver 		
		
		try {
			Class.forName(driver); 
			DriverManager.registerDriver (new com.mysql.jdbc.Driver()); 
	        // JDBC-Treiber laden 
	        Class.forName(driver);
	        
	        //Daten für den Aufbau zur Datenbank 
            String url = "jdbc:mysql://localhost/MassbankNEW"; 

            //java.sql.Driver d = (java.sql.Driver)Class.forName(driver).newInstance (); 
            //System.out.println(" Does this driver accept my URL? " + d.acceptsURL(url) ); 
        
           
            String username = "root"; 
            String password = "+i&CzXve"; 

             
            // Verbindung aufbauen 
            Connection con; 
            con = DriverManager.getConnection(url, username, password);
		    
            Statement stmt = null;
		    ResultSet rs = null;
		    stmt = con.createStatement();
		    rs = stmt.executeQuery("select * from CH_NAME join MOLFILE using (NAME);");
		    while(rs.next())
		    {
		    	//Name
		    	System.out.print(rs.getString(1) + "\t");
		    	//Massbank Entry
		    	System.out.print(rs.getString(2) + "\t");
		    	//Mol file
		    	System.out.print(rs.getString(3) + "\t");
		    	System.out.print("\n");
		    }
		    
		    // or alternatively, if you don't know ahead of time that
		    // the query will be a SELECT...
		    //if (stmt.execute("SELECT * FROM SPECTRUM")) {
		    //    rs = stmt.getResultSet();
		    //    System.out.println(rs.getString(1));
		    //}
		    
		 // Verbindung schließen 
            con.close();
		}
		catch (ClassNotFoundException ex) 
        { 
          System.err.println("Unable to load Driver!"); 
          System.err.println(ex); 
          System.exit(1); 
        } 
		catch (SQLException ex) {
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
		catch (InstantiationError ex) {
			System.err.println("ERROR: " + ex.getMessage());
		}
	}
}
