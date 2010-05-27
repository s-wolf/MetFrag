package de.ipbhalle.metfrag.fragmenter;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import de.ipbhalle.metfrag.chemspiderClient.ChemSpider;
import de.ipbhalle.metfrag.keggWebservice.KeggWebservice;
import de.ipbhalle.metfrag.molDatabase.KEGGLocal;
import de.ipbhalle.metfrag.molDatabase.PubChemLocal;
import de.ipbhalle.metfrag.pubchem.PubChemWebService;
import de.ipbhalle.metfrag.tools.PPMTool;

public class Candidates {
	
	
	/**
	 * Gets the candidates online using the webservice interface from the databases
	 * 
	 * @param database the database
	 * @param databaseID the database id
	 * @param molecularFormula the molecular formula
	 * @param exactMass the exact mass
	 * @param searchPPM the search ppm
	 * @param useIPBProxy the use ipb proxy
	 * 
	 * @return the online
	 * 
	 * @throws Exception the exception
	 */
	public static Vector<String> getOnline(String database, String databaseID, String molecularFormula, double exactMass, double searchPPM, boolean useIPBProxy, PubChemWebService pubchem) throws Exception
	{
		Vector<String> candidates = new Vector<String>();
		
		if(database.equals("kegg") && databaseID.equals(""))
		{
			if(molecularFormula != "")
				candidates = KeggWebservice.KEGGbySumFormula(molecularFormula);
			else
				candidates = KeggWebservice.KEGGbyMass(exactMass, (PPMTool.getPPMDeviation(exactMass, searchPPM)));
		}
		else if(database.equals("chemspider") && databaseID.equals(""))
		{
			if(molecularFormula != "")
				candidates = ChemSpider.getChemspiderBySumFormula(molecularFormula);
			else
				candidates = ChemSpider.getChemspiderByMass(exactMass, (PPMTool.getPPMDeviation(exactMass, searchPPM)));
		}
		else if(database.equals("pubchem") && databaseID.equals(""))
		{
			if(molecularFormula != "")
				candidates = pubchem.getHitsbySumFormula(molecularFormula, useIPBProxy);
			else
				candidates = pubchem.getHitsByMass(exactMass, (PPMTool.getPPMDeviation(exactMass, searchPPM)), Integer.MAX_VALUE, useIPBProxy);
		}
		else if (!databaseID.equals(""))
		{
			candidates = new Vector<String>();
			candidates.add(databaseID);
		}
		
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
	public static List<String> getLocally(String database, double exactMass, double searchPPM, String databaseUrl, String username, String password) throws SQLException, ClassNotFoundException, RemoteException
	{
		List<String> candidates = new ArrayList<String>();
		
		if(database.equals("kegg"))
		{
			KEGGLocal kl = new KEGGLocal(databaseUrl, username, password);
			double deviation =PPMTool.getPPMDeviation(exactMass, searchPPM);
			candidates = kl.getHits(Integer.MAX_VALUE, (exactMass - deviation) , (exactMass + deviation));				
		}
		else if(database.equals("chemspider"))
		{
			candidates = ChemSpider.getChemspiderByMass(exactMass, (PPMTool.getPPMDeviation(exactMass, searchPPM)));
		}
		else if(database.equals("pubchem"))
		{
			PubChemLocal pl = new PubChemLocal(databaseUrl, username, password);
			double deviation =PPMTool.getPPMDeviation(exactMass, searchPPM);
			pl.getHits((exactMass - deviation), (exactMass + deviation));
		}
		
		return candidates;
	}

}
