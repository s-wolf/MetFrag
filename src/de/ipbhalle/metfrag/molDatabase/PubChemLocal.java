package de.ipbhalle.metfrag.molDatabase;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.smiles.SmilesParser;



public class PubChemLocal {

	private String url = ""; 
    private String username = ""; 
    private String password = ""; 
	private String lastSmiles = "";
	
	public PubChemLocal(String databseUrl, String username, String password)
	{
		this.url = databseUrl;
		this.username = username;
		this.password = password;
		
	}
	
	/**
	 * Gets the hits (only the pubchem id's) from a local database with a MassBank schema
	 * and with specified lower and upper bound.
	 * 
	 * @param limit the limit
	 * @param lowerBound the lower bound
	 * @param upperBound the higher bound
	 * 
	 * @return the hits
	 * 
	 * @throws SQLException the SQL exception
	 * @throws ClassNotFoundException the class not found exception
	 */
	public List<String> getHits(double lowerBound, double upperBound) throws SQLException, ClassNotFoundException
	{
		List<String> candidatesString = new ArrayList<String>();    
		System.out.println("Lower bound: " + lowerBound + " Upper bound: " + upperBound);
		//now retrieve the search results
        String driver = "com.mysql.jdbc.Driver"; 
        Connection con = null; 
		Class.forName(driver); 
		DriverManager.registerDriver (new com.mysql.jdbc.Driver()); 
        // JDBC-driver
        Class.forName(driver);
        con = DriverManager.getConnection(url, username, password);
	    
        Statement stmt = null;
	    stmt = con.createStatement();
	    //now select only the pubchem entries!
	    ResultSet rs = stmt.executeQuery("SELECT ID FROM RECORD WHERE SUBSTRING(ID,1,1) != 'C' and SUBSTRING(ID,1,1) != 'B' and EXACT_MASS >= '" + lowerBound + "' and EXACT_MASS <= '" + upperBound + "' order by  CAST(ID as UNSIGNED)");

	    while(rs.next())
	    {
	    	candidatesString.add(rs.getString("id"));
	    }
        con.close();
        
        return candidatesString;
	}
	
	/**
	 * Gets the hits (only the pubchem id's) from a local database with a MassBank schema
	 * and with specified lower and upper bound.
	 * 
	 * @param limit the limit
	 * @param lowerBound the lower bound
	 * @param upperBound the higher bound
	 * 
	 * @return the hits
	 * 
	 * @throws SQLException the SQL exception
	 * @throws ClassNotFoundException the class not found exception
	 */
	public Vector<String> getHitsVector(double lowerBound, double upperBound) throws SQLException, ClassNotFoundException
	{
		Vector<String> candidatesString = new Vector<String>();    
		System.out.println("Lower bound: " + lowerBound + " Upper bound: " + upperBound);
		//now retrieve the search results
        String driver = "com.mysql.jdbc.Driver"; 
        Connection con = null; 
		Class.forName(driver); 
		DriverManager.registerDriver (new com.mysql.jdbc.Driver()); 
        // JDBC-driver
        Class.forName(driver);
        con = DriverManager.getConnection(url, username, password);
	    
        Statement stmt = null;
	    stmt = con.createStatement();
	    //now select only the pubchem entries!
	    ResultSet rs = stmt.executeQuery("SELECT ID FROM RECORD WHERE SUBSTRING(ID,1,1) != 'C' and SUBSTRING(ID,1,1) != 'B' and EXACT_MASS >= '" + lowerBound + "' and EXACT_MASS <= '" + upperBound + "' order by  CAST(ID as UNSIGNED)");

	    while(rs.next())
	    {
	    	candidatesString.add(rs.getString("id"));
	    }
        con.close();
        
        return candidatesString;
	}
	
	
	/**
	 * Gets the mol.
	 * 
	 * @param pubChemID the pub chem id
	 * @param getALL gets the molecule also if not biological compound
	 * 
	 * @return the mol
	 * 
	 * @throws SQLException the SQL exception
	 * @throws ClassNotFoundException the class not found exception
	 * @throws InvalidSmilesException the invalid smiles exception
	 */
	public IAtomContainer getMol(String pubChemID, boolean getALL) throws SQLException, ClassNotFoundException, InvalidSmilesException
	{
		//now retrieve the search results
        String driver = "com.mysql.jdbc.Driver"; 
        Connection con = null; 
		Class.forName(driver); 
		DriverManager.registerDriver (new com.mysql.jdbc.Driver()); 
        // JDBC-driver
        Class.forName(driver);
        con = DriverManager.getConnection(url, username, password);
        Statement stmt = con.createStatement();
	    ResultSet rs = stmt.executeQuery("SELECT SMILES, CHONSP FROM RECORD WHERE SUBSTRING(ID,1,1) != 'C' and SUBSTRING(ID,1,1) != 'B' and ID = '" + pubChemID + "' limit 1;");
	    SmilesParser sp1 = new SmilesParser(DefaultChemObjectBuilder.getInstance());
	    IAtomContainer molecule = null;
	    Boolean chonsp = false;
	    while(rs.next())
	    {
	    	//Name
	    	System.out.print(pubChemID);
	    	String smiles = rs.getString("smiles");
	    	this.lastSmiles = smiles;
	    	boolean bioTest = smiles.contains("C") && (!smiles.contains("O") && !smiles.contains("N") && !smiles.contains("S") && !smiles.contains("P"));
	    	//boolean bioTest = false;
	    	if((rs.getInt("chonsp") == 1 && !bioTest) || getALL)
	    		molecule = sp1.parseSmiles(smiles);
	    		
	    }
	    con.close();
	    
	    return molecule;
	}
	
	/**
	 * Gets the last smiles which was just retrieeved from the database.
	 * 
	 * @return the last smiles
	 */
	public String getLastSmiles()
	{
		return this.lastSmiles;
	}
	

	/**
	 * Gets the names to a specified pubChem ID using the local pubchem snapshot!
	 * 
	 * @param pubChemID the pub chem id
	 * 
	 * @return the names
	 * 
	 * @throws SQLException the SQL exception
	 * @throws ClassNotFoundException the class not found exception
	 * @throws InvalidSmilesException the invalid smiles exception
	 */
	public List<String> getNames(String pubChemID) throws SQLException, ClassNotFoundException, InvalidSmilesException
	{
		List<String> ret = new ArrayList<String>();
		//now retrieve the search results
        String driver = "com.mysql.jdbc.Driver"; 
        Connection con = null; 
		Class.forName(driver); 
		DriverManager.registerDriver (new com.mysql.jdbc.Driver()); 
        // JDBC-driver
        Class.forName(driver);
        con = DriverManager.getConnection(url, username, password);
        Statement stmt = con.createStatement();
	    ResultSet rs = stmt.executeQuery("select NAME from CH_NAME where ID = '" + pubChemID + "' limit 10;");
	    
	    while(rs.next())
	    {
	    	//Name
	    	//System.out.print(pubChemID);
	    	ret.add(rs.getString("NAME"));
	    		
	    }
	    con.close();
	    
	    return ret;
	}
	
	
	/**
	 * Gets the synonym for a specified PubChem ID.
	 * 
	 * @param PubChemID the pub chem id
	 * @param filename the filename
	 * 
	 * @return the synonym
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public static String getSynonym(String PubChemID, String filename) throws NumberFormatException, IOException
	{
		String ret = "";
	    String line;
	    // Now read lines of text: the BufferedReader puts them in lines,
	    // the InputStreamReader does Unicode conversion, and the
	    // GZipInputStream "gunzip"s the data from the FileInputStream.
    
    	FileInputStream fin = new FileInputStream(filename);
	    GZIPInputStream gzis = new GZIPInputStream(fin);
	    InputStreamReader xover = new InputStreamReader(gzis);
	    BufferedReader is = new BufferedReader(xover);
		while ((line = is.readLine()) != null)
		{
			String[] lineSplit = line.split("\t");
			if(lineSplit[0].equals(PubChemID))
			{
				ret = lineSplit[1];
				return ret;
			}
			else if(Integer.parseInt(lineSplit[0]) > Integer.parseInt(PubChemID))
				break;
				                
		}
		
		
		return ret;
	}
}
