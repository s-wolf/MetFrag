package de.ipbhalle.metfrag.fragmenter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.io.SDFWriter;

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
	
	
	public static void main(String[] args) {
		String databaseUrl = "jdbc:postgresql://rdbms2:5432/metchem";
		try {
			List<CandidateMetChem> res = queryMass("pubchem", Double.parseDouble(args[0]), Double.parseDouble(args[1]), databaseUrl, args[2], "");
			Query query = new Query(args[2], "", databaseUrl);
			query.openConnection();
			IAtomContainerSet set = new AtomContainerSet();
			for (CandidateMetChem canidate : res) {
				set.addAtomContainer(query.getCompoundConnectionOpened(canidate.getCompoundID()));
			}
			query.closeConnection();
			
			SDFWriter sdf = new SDFWriter(new FileOutputStream(new File(args[3])));
			sdf.write(set);
			sdf.close();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CDKException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
