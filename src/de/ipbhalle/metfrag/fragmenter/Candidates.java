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


package de.ipbhalle.metfrag.fragmenter;

import java.io.StringReader;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.MDLReader;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;

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
			candidates = pl.getHits((exactMass - deviation), (exactMass + deviation));
		}
		
		return candidates;
	}

	
	/**
	 * Gets the compound using the webservice.
	 * 
	 * @param database the database
	 * @param candidate the candidate
	 * @param pw the pw
	 * 
	 * @return the compound
	 * @throws RemoteException 
	 * @throws CDKException 
	 */
	public static IAtomContainer getCompound(String database, String candidate, PubChemWebService pw, String chemspiderToken) throws RemoteException, CDKException
	{
		IAtomContainer molecule = null;
		
		if(database.equals("kegg"))
		{	
			if(candidate.startsWith("cpd:"))
				candidate = candidate.substring(4);
			
			String candidateMol = KeggWebservice.KEGGgetMol(candidate, "");
			MDLReader reader;
			List<IAtomContainer> containersList;
			
	        reader = new MDLReader(new StringReader(candidateMol));
	        ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());
	        containersList = ChemFileManipulator.getAllAtomContainers(chemFile);
	        molecule = containersList.get(0);
			
		}
		else if(database.equals("chemspider"))
		{
			String candidateMol = ChemSpider.getMolByID(candidate, chemspiderToken);
			
			MDLReader reader;
			List<IAtomContainer> containersList;
			
	        reader = new MDLReader(new StringReader(candidateMol));
	        ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());
	        containersList = ChemFileManipulator.getAllAtomContainers(chemFile);
	        molecule = containersList.get(0);
	        
		}
		else if(database.equals("pubchem"))
		{
			molecule = pw.getMol(candidate);
		}
		else
		{
			System.err.println("No database selected or wrong database name?");
		}
		
		return molecule;
	}
	
	/**
	 * Gets the compound locally.
	 * 
	 * @param database the database
	 * @param candidate the candidate
	 * @param jdbc the jdbc
	 * @param username the username
	 * @param password the password
	 * @param getAll the get all
	 * 
	 * @return the compound locally
	 * 
	 * @throws ClassNotFoundException the class not found exception
	 * @throws SQLException the SQL exception
	 * @throws CDKException the CDK exception
	 * @throws RemoteException the remote exception
	 */
	public static IAtomContainer getCompoundLocally(String database, String candidate, String jdbc, String username, String password, boolean getAll, String chemspiderToken) throws SQLException, ClassNotFoundException, RemoteException, CDKException
	{
		IAtomContainer molecule = null;

		if(database.equals("kegg"))
		{
			KEGGLocal kl = new KEGGLocal(jdbc, username, password);
			molecule = kl.getMol(candidate);
		}
		else if(database.equals("chemspider"))
		{
			molecule = ChemSpider.getMol(candidate, true, chemspiderToken);
		}
		else if(database.equals("pubchem"))
		{
			PubChemLocal pl = new PubChemLocal(jdbc, username, password);
			molecule = pl.getMol(candidate, getAll);
		}
		
		return molecule;
	}

}
