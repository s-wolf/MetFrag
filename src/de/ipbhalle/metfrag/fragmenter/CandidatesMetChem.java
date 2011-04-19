package de.ipbhalle.metfrag.fragmenter;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;

import de.ipbhalle.metfrag.databaseMetChem.CandidateMetChem;
import de.ipbhalle.metfrag.databaseMetChem.Query;
import de.ipbhalle.metfrag.tools.PPMTool;

public class CandidatesMetChem {	
	
	/**
	 * Gets the candidates locally using a local database.
	 * 
	 * @param database the database
	 * @param exactMass the exact mass
	 * @param searchPPM the search ppm
	 * @param databaseUrl the database url
	 * @param username the username
	 * @param password the password
	 * 
	 * @return the locally
	 * 
	 * @throws SQLException the SQL exception
	 * @throws ClassNotFoundException the class not found exception
	 * @throws RemoteException the remote exception
	 */
	public static List<CandidateMetChem> queryMass(String database, double exactMass, double searchPPM, String databaseUrl, String username, String password) throws SQLException, ClassNotFoundException, RemoteException
	{
		List<CandidateMetChem> candidates = new ArrayList<CandidateMetChem>();
		double deviation =PPMTool.getPPMDeviation(exactMass, searchPPM);
		double lowerBound = (exactMass - deviation);
		double upperBound = (exactMass + deviation);
		Query query = new Query(username, password, databaseUrl);
		candidates = query.queryByMass(lowerBound, upperBound, database);		
		return candidates;
	}
	
	/**
	 * Gets the candidates locally using a local database.
	 * 
	 * @param database the database
	 * @param exactMass the exact mass
	 * @param searchPPM the search ppm
	 * @param databaseUrl the database url
	 * @param username the username
	 * @param password the password
	 * 
	 * @return the locally
	 * 
	 * @throws SQLException the SQL exception
	 * @throws ClassNotFoundException the class not found exception
	 * @throws RemoteException the remote exception
	 */
	public static List<CandidateMetChem> queryFormula(String database, String formula, String databaseUrl, String username, String password) throws SQLException, ClassNotFoundException, RemoteException
	{
		List<CandidateMetChem> candidates = new ArrayList<CandidateMetChem>();
		Query query = new Query(username, password, databaseUrl);
		candidates = query.queryByFormula(formula, database);		
		return candidates;
	}

	
	/**
	 * Gets the compound using the webservice.
	 *
	 * @param candidate the candidate
	 * @param databaseUrl the database url
	 * @param username the username
	 * @param password the password
	 * @return the compound
	 * @throws RemoteException the remote exception
	 * @throws CDKException the cDK exception
	 * @throws SQLException the sQL exception
	 */
	public static IAtomContainer getCompound(Integer candidate, String databaseUrl, String username, String password) throws RemoteException, CDKException, SQLException
	{
		Query query = new Query(username, password, databaseUrl);
		IAtomContainer molecule = query.getCompound(candidate);		
		return molecule;
	}	
	
}
