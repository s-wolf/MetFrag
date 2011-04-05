package de.ipbhalle.metfrag.databaseMetChem;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.io.MDLV3000Reader;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;

import de.ipbhalle.metfrag.main.Config;

public class Query {
	
	private Connection con;
	private Map<String, Integer> databaseToID;
	String url;
    String username;
    String password;
	
	/**
	 * Instantiates a new query.
	 *
	 * @param username the username
	 * @param password the password
	 * @param url the url
	 */
	public Query(String username, String password, String url)
	{
		String driver = "org.postgresql.Driver"; 
		try {
			Class.forName(driver);
			DriverManager.registerDriver(new org.postgresql.Driver()); 
	        //database data
	        this.url = url;
	        this.username = username;
	        this.password = password;
	        con = DriverManager.getConnection(url, username, password);
	        
	        databaseToID = new HashMap<String, Integer>();
	        PreparedStatement pstmtDatabase = con.prepareStatement("SELECT library_id, library_name from library;");
	        ResultSet res = pstmtDatabase.executeQuery();
	        while(res.next()){
	        	databaseToID.put(res.getString(2), res.getInt(1));
	        }
	        con.close();
	        
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Query by mass. Returns a list with all compound ID's matching the
	 * given exact mass.
	 *
	 * @param exactMass the exact mass
	 * @param database the database
	 * @return the list
	 * @throws SQLException 
	 */
	public List<String> queryByMass(double lowerBound, double upperBound, String database) throws SQLException
	{
		if(this.databaseToID.get(database) == null)
		{
			String possible = "";
			for (String db : this.databaseToID.keySet()) {
				possible += "\"" + db + "\" ";
			}
			System.err.println("Wrong database name given! Possible are: " + possible);
			return null;
		}
		
		con = DriverManager.getConnection(url, username, password);
		List<String> candidatesString = new ArrayList<String>(); 
		
		//SELECT c.compound_id from compound c inner join substance s on s.compound_id = c.compound_id 
		//where exact_mass >= 272.071 and exact_mass <= 272.079 and s.library_id = 2;
		
	    PreparedStatement pstmt = con.prepareStatement("SELECT c.compound_id from compound c inner join substance s on s.compound_id = c.compound_id " +
	    		"where exact_mass >= ? and exact_mass <= ? and s.library_id = ?;");
	    pstmt.setDouble(1, lowerBound);
	    pstmt.setDouble(2, upperBound);
	    pstmt.setInt(3, this.databaseToID.get(database));
	    
        ResultSet res = pstmt.executeQuery();
        while(res.next()){
        	candidatesString.add(Integer.toString(res.getInt(1)));
        }
        con.close();
        
        return candidatesString;
	}
	
	
	/**
	 * Query by formula.
	 *
	 * @param formula the formula
	 * @param database the database
	 * @return the list
	 * @throws SQLException the sQL exception
	 */
	public List<String> queryByFormula(String formula, String database) throws SQLException
	{
		if(this.databaseToID.get(database) == null)
		{
			String possible = "";
			for (String db : this.databaseToID.keySet()) {
				possible += "\"" + db + "\" ";
			}
			System.err.println("Wrong database name given! Possible are: " + possible);
			return null;
		}
		
		con = DriverManager.getConnection(url, username, password);
		List<String> candidatesString = new ArrayList<String>(); 
		PreparedStatement pstmt = con.prepareStatement("SELECT c.compound_id from compound c inner join substance s on s.compound_id = c.compound_id " +
		"where formula = ? and s.library_id = ?;");
	    pstmt.setString(1, formula);
	    pstmt.setInt(2, this.databaseToID.get(database));
	    
        ResultSet res = pstmt.executeQuery();
        while(res.next()){
        	candidatesString.add(Integer.toString(res.getInt(1)));
        }
        pstmt.close();
        con.close();
        
        return candidatesString;
	}
	
	
	/**
	 * Gets the compound.
	 *
	 * @param compoundID the compound id
	 * @return the compound
	 * @throws SQLException 
	 * @throws CDKException 
	 */
	public IAtomContainer getCompound(Integer compoundID) throws SQLException, CDKException
	{
		IAtomContainer ret = null;
		con = DriverManager.getConnection(url, username, password);
		PreparedStatement pstmt = con.prepareStatement("SELECT v3000(mol_structure) from compound c where compound_id = ? limit 1;");
	    pstmt.setInt(1, compoundID);
	    
        ResultSet res = pstmt.executeQuery();
        while(res.next()){
        	String molString = res.getString(1);
        	MDLV2000Reader reader = new MDLV2000Reader(new ByteArrayInputStream(molString.getBytes()));
        	ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());
	        List<IAtomContainer> containersList = ChemFileManipulator.getAllAtomContainers(chemFile);
	        for (IAtomContainer container: containersList) {
	        	ret = container;
			}
        }
        pstmt.close();
        con.close();
        
        return ret;
	}
	
	
	public static void main(String[] args) {

		try {
			Config c = new Config("outside");
			Query test = new Query(c.getUsernamePostgres(),c.getPasswordPostgres(),c.getJdbcPostgres());
			System.out.println(test.queryByFormula("C15H12O5", "pubchem").size());
//			System.out.println(test.queryByMass(272.02, 272.03, "pubchem").size());
			System.out.println(test.getCompound(12337526));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CDKException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
