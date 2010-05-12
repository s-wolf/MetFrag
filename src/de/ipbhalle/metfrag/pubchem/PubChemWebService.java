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
package de.ipbhalle.metfrag.pubchem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.rmi.RemoteException;
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

import javax.xml.rpc.ServiceException;

import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.formula.MolecularFormula;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemFile;
import org.openscience.cdk.interfaces.IElement;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.io.IChemObjectReader;
import org.openscience.cdk.io.ISimpleChemObjectReader;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.io.ReaderFactory;
import org.openscience.cdk.io.formats.IChemFormat;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;
import org.xml.sax.SAXParseException;

import de.ipbhalle.metfrag.main.Config;

import gov.nih.nlm.ncbi.pubchem.CompressType;
import gov.nih.nlm.ncbi.pubchem.EntrezKey;
import gov.nih.nlm.ncbi.pubchem.FormatType;
import gov.nih.nlm.ncbi.pubchem.MFSearchOptions;
import gov.nih.nlm.ncbi.pubchem.PCIDType;
import gov.nih.nlm.ncbi.pubchem.PUGLocator;
import gov.nih.nlm.ncbi.pubchem.PUGSoap;
import gov.nih.nlm.ncbi.pubchem.StatusType;
import gov.nih.nlm.ncbi.www.soap.eutils.EUtilsServiceLocator;
import gov.nih.nlm.ncbi.www.soap.eutils.EUtilsServiceSoap;
import gov.nih.nlm.ncbi.www.soap.eutils.esearch.ESearchRequest;
import gov.nih.nlm.ncbi.www.soap.eutils.esearch.ESearchResult;

public class PubChemWebService {
	
	EUtilsServiceLocator eutils_locator;
	EUtilsServiceSoap eutils_soap;
	PUGLocator pug_locator;
	PUGSoap pug_soap;
	List<IAtomContainer> containers;
	HashMap<Integer, String> retrievedHits = null;
	
	/**
	 * Instantiates a new pub chem web service.
	 * 
	 * @throws ServiceException the service exception
	 */
	public PubChemWebService() throws ServiceException
	{
		eutils_locator = new EUtilsServiceLocator();
		eutils_soap = eutils_locator.geteUtilsServiceSoap();
		pug_locator = new PUGLocator();
		pug_soap = pug_locator.getPUGSoap();
		this.retrievedHits = new HashMap<Integer, String>();
		this.containers = new ArrayList<IAtomContainer>();
	}
	
	
	
	public IAtomContainer getSingleMol(String cid, boolean useProxy) throws CDKException, InterruptedException, IOException
	{
		IAtomContainer ac = null;
		int[] cids = new int[1];
		cids[0] = Integer.parseInt(cid);

        String listKey = pug_soap.inputList(cids, PCIDType.eID_CID);
//        System.out.println("ListKey = " + listKey);
//        System.out.println("number of compounds = " + pug_soap.getListItemsCount(listKey));
        
        // Initialize the download; request SDF with gzip compression
        String downloadKey = pug_soap.download(listKey, 
            FormatType.eFormat_SDF, CompressType.eCompress_GZip, false);
//        System.out.println("DownloadKey = " + downloadKey);
        
        // Wait for the download to be prepared
        StatusType status;
        while ((status = pug_soap.getOperationStatus(downloadKey)) == StatusType.eStatus_Running || 
               status == StatusType.eStatus_Queued) 
        {
//            System.out.println("Waiting for download to finish...");
            Thread.sleep(5000);
        }
        
        // On success, get the download URL, save to local file
        if (status == StatusType.eStatus_Success) {
        	
        	// PROXY
        	if(useProxy)
        	{
        		System.getProperties().put( "ftp.proxySet", "true" );
    		    System.getProperties().put( "ftp.proxyHost", "www.ipb-halle.de" );
    		    System.getProperties().put( "ftp.proxyPort", "3128" );
        	}
		    
            URL url = new URL(pug_soap.getDownloadUrl(downloadKey));
//            System.out.println("Success! Download URL = " + url.toString());
            
            // get input stream from URL
            URLConnection fetch = url.openConnection();
            InputStream input = fetch.getInputStream();
            
            // open local file based on the URL file name
			String filename = "/tmp"
					+ url.getFile().substring(url.getFile().lastIndexOf('/'));
			FileOutputStream output = new FileOutputStream(filename);
//			System.out.println("Writing data to " + filename);

			// buffered read/write
			byte[] buffer = new byte[10000];
			int n;
			while ((n = input.read(buffer)) > 0)
				output.write(buffer, 0, n);
			
			//now read in the file
			FileInputStream in = null;
	        in = new FileInputStream(filename);
	        GZIPInputStream gin = new GZIPInputStream(in);
	        
	        //IChemObjectReader cor = null;
	        //cor = new ReaderFactory().createReader(in);
	       
	        MDLV2000Reader reader = new MDLV2000Reader(gin);
	        ChemFile fileContents = (ChemFile)reader.read(new ChemFile());
	        System.out.println("Got " + fileContents.getChemSequence(0).getChemModelCount() + " atom containers");
	        ac = fileContents.getChemSequence(0).getChemModel(0).getMoleculeSet().getAtomContainer(0);
        } else {
            System.out.println("Error: ");            
        }
        
        return ac;
	}
	
	
	/**
	 * PubChem get hits by sum formula. TODO: fix inefficient smiles generation
	 * 
	 * @param sumFormula the sum formula
	 * 
	 * @return the vector< string>
	 * 
	 * @throws Exception the exception
	 */
	public Vector<String> getHitsbySumFormula(String sumFormula, boolean useProxy) throws Exception
	{
		Vector<String> candidatesString = new Vector<String>();        
		
		PUGLocator pug_locator = new PUGLocator();
		PUGSoap pug_soap = pug_locator.getPUGSoap();
		
		
		MFSearchOptions mf_options = new MFSearchOptions();
		mf_options.setAllowOtherElements(false);
		String listKey = pug_soap.MFSearch(sumFormula, mf_options, null);
		
		System.out.println("MFSearch " + sumFormula + " " + listKey);
		
		StatusType status;
		while ((status = pug_soap.getOperationStatus(listKey)) == StatusType.eStatus_Running
				|| status == StatusType.eStatus_Queued) {
			System.out.println("Waiting for query to finish...");
			Thread.sleep(10000);
		}
		
		int[] cids = null;
		//get cids
		try
		{
			cids = pug_soap.getIDList(listKey);
		}
		catch(RemoteException e)
		{
			System.err.println("Error: No hit!?" + e.getMessage());
			return candidatesString;
		}
		
		String listkey = pug_soap.inputList(cids, PCIDType.eID_CID);
		String downloadKey = pug_soap.download(listkey, FormatType.eFormat_SDF,
				CompressType.eCompress_None, false);
		System.out.println("DownloadKey = " + downloadKey);
		status = null;
		while ((status = pug_soap.getOperationStatus(downloadKey)) == StatusType.eStatus_Running
				|| status == StatusType.eStatus_Queued) {
			System.out.println("Waiting for download to finish...");
			Thread.sleep(1000);
		}
		
		// On success, get the download URL, save to local file
		if (status == StatusType.eStatus_Success) {
			
			// PROXY
			if(useProxy)
			{
				System.getProperties().put( "ftp.proxySet", "true" );
			    System.getProperties().put( "ftp.proxyHost", "www.ipb-halle.de" );
			    System.getProperties().put( "ftp.proxyPort", "3128" );
			}
		    
			URL url = new URL(pug_soap.getDownloadUrl(downloadKey));
			System.out.println("Success! Download URL = " + url.toString());

			// get input stream from URL
			URLConnection fetch = url.openConnection();
			InputStream input = fetch.getInputStream();

			// open local file based on the URL file name
			String filename = "/tmp"
					+ url.getFile().substring(url.getFile().lastIndexOf('/'));
			FileOutputStream output = new FileOutputStream(filename);
			System.out.println("Writing data to " + filename);

			// buffered read/write
			byte[] buffer = new byte[10000];
			int n;
			while ((n = input.read(buffer)) > 0)
				output.write(buffer, 0, n);
			
			//read the file
			FileInputStream in = null;
	        in = new FileInputStream(filename);
	        
	        //IChemObjectReader cor = null;
	        //cor = new ReaderFactory().createReader(in);
	       
	        MDLV2000Reader reader = new MDLV2000Reader(in);
	        ChemFile fileContents = (ChemFile)reader.read(new ChemFile());
	        System.out.println("Got " + fileContents.getChemSequence(0).getChemModelCount() + " atom containers");

		   
	        
	        //ReaderFactory factory = new ReaderFactory();
	        //ISimpleChemObjectReader reader = factory.createReader(in);
	        //IChemFile content = (IChemFile)reader.read(new ChemFile());
	        
	        //IChemFile content = (IChemFile)cor.read(DefaultChemObjectBuilder.getInstance().newChemFile());
	        
	        System.out.println("Read the file");
	        this.containers = ChemFileManipulator.getAllAtomContainers(fileContents);
	        System.out.println("Got " + containers.size() + " atom containers");
	        
	        // Retrieve CIDs
	        SmilesGenerator generatorSmiles = new SmilesGenerator();
			for (int i = 0; i < cids.length; i++) {
				candidatesString.add(cids[i] + "");
				System.out.println(cids[i]);
				this.retrievedHits.put(cids[i], generatorSmiles.createSMILES(fileContents.getChemSequence(0).getChemModel(i).getMoleculeSet().getMolecule(0)));
			}
	        
	        new File("/tmp"	+ url.getFile().substring(url.getFile().lastIndexOf('/'))).delete();
	        System.out.println("Temp file deleted!");
	        
		}
		 else {
			System.out.println("Error: "
					+ pug_soap.getStatusMessage(downloadKey));
		}
		
		return candidatesString;
	}
	
	
	/**
	 * Search pubchem by exact mass.
	 * 
	 * @param mass the mass
	 * 
	 * @return the pubchem by mass
	 * 
	 * @throws Exception the exception
	 */
	public Vector<String> getHitsByMass(double mass, double error, Integer limit, boolean useProxy) throws Exception
	{
		Vector<String> pubchemCIDs = new Vector<String>();
		
		PUGSoap pug_soap = this.pug_locator.getPUGSoap();
		
        EUtilsServiceLocator eutils_locator = new EUtilsServiceLocator();
        EUtilsServiceSoap eutils_soap = eutils_locator.geteUtilsServiceSoap();


		
		//search "aspirin" in PubChem Compound
		ESearchRequest request = new ESearchRequest();
		String db = new String("pccompound");
		request.setDb(db);
		
		double min = mass - error;
		double max = mass + error;
		
		System.out.println("Min: " + min + " Max: " + max);
		request.setTerm(min + ":" + max + "[EMAS]");
		// create a history item, and don't return any actual ids in the
		// SOAP response
		request.setUsehistory("y");
		request.setRetMax(limit.toString());

		ESearchResult result = eutils_soap.run_eSearch(request);
		
		//String[] idList = result.getIdList();
		if (result.getQueryKey() == null || result.getQueryKey().length() == 0
				|| result.getWebEnv() == null
				|| result.getWebEnv().length() == 0) {
			throw new Exception("ESearch failed to return query_key and WebEnv");
		}
		System.out.println("ESearch returned " + result.getCount() + " hits");
		
		
		// give this Entrez History info to PUG SOAP
		EntrezKey entrezKey = new EntrezKey(db, result.getQueryKey(), result.getWebEnv());	
		String listKey = pug_soap.inputEntrez(entrezKey);
		
		System.out.println("ListKey = " + listKey);
		
		//int[] ids = pug_soap.getIDList(entrezKey.getKey());
		
		// Initialize the download; request SDF with gzip compression
		String downloadKey = pug_soap.download(listKey, FormatType.eFormat_SDF,
				CompressType.eCompress_None, false);
		System.out.println("DownloadKey = " + downloadKey);

		// Wait for the download to be prepared
		StatusType status;
		while ((status = pug_soap.getOperationStatus(downloadKey)) == StatusType.eStatus_Running
				|| status == StatusType.eStatus_Queued) {
			System.out.println("Waiting for download to finish...");
			Thread.sleep(10000);
		}

		// On success, get the download URL, save to local file
		if (status == StatusType.eStatus_Success) {
			
			// PROXY
			if(useProxy)
			{
				System.getProperties().put( "ftp.proxySet", "true" );
			    System.getProperties().put( "ftp.proxyHost", "www.ipb-halle.de" );
			    System.getProperties().put( "ftp.proxyPort", "3128" );
			}
		    
			URL url = new URL(pug_soap.getDownloadUrl(downloadKey));
			System.out.println("Success! Download URL = " + url.toString());

			// get input stream from URL
			URLConnection fetch = url.openConnection();
			InputStream input = fetch.getInputStream();

			// open local file based on the URL file name
			String filename = "/tmp"
					+ url.getFile().substring(url.getFile().lastIndexOf('/'));
			FileOutputStream output = new FileOutputStream(filename);
			System.out.println("Writing data to " + filename);

			// buffered read/write
			byte[] buffer = new byte[10000];
			int n;
			while ((n = input.read(buffer)) > 0)
				output.write(buffer, 0, n);
			
			//now read in the file
			FileInputStream in = null;
	        in = new FileInputStream(filename);
	        
	        //IChemObjectReader cor = null;
	        //cor = new ReaderFactory().createReader(in);
	       
	        MDLV2000Reader reader = new MDLV2000Reader(in);
	        ChemFile fileContents = (ChemFile)reader.read(new ChemFile());
	        System.out.println("Got " + fileContents.getChemSequence(0).getChemModelCount() + " atom containers");
	        
	        SmilesGenerator generatorSmiles = new SmilesGenerator();
	        for (int i = 0; i < fileContents.getChemSequence(0).getChemModelCount(); i++) {
				this.containers.add(fileContents.getChemSequence(0).getChemModel(i).getMoleculeSet().getAtomContainer(0));
				Map<Object, Object> properties = fileContents.getChemSequence(0).getChemModel(i).getMoleculeSet().getAtomContainer(0).getProperties();
		        pubchemCIDs.add((String) properties.get("PUBCHEM_COMPOUND_CID"));
		        System.out.println((String) properties.get("PUBCHEM_COMPOUND_CID"));
		        this.retrievedHits.put(Integer.parseInt(properties.get("PUBCHEM_COMPOUND_CID").toString()), generatorSmiles.createSMILES(fileContents.getChemSequence(0).getChemModel(i).getMoleculeSet().getMolecule(0)));
			}

	        System.out.println("Read the file");
	        
	        new File("/tmp"	+ url.getFile().substring(url.getFile().lastIndexOf('/'))).delete();
	        
	        System.out.println("Temp file deleted!");
			
		} else {
			System.out.println("Error: "
					+ pug_soap.getStatusMessage(downloadKey));
		}
		
		return pubchemCIDs;
		
	}
	
	
	
	/**
	 * Gets pubchem candidate compounds by molecular weight.
	 * 
	 * @param mass the mass
	 * @param error the error
	 * @param limit the limit
	 * 
	 * @return the pubchem mimw
	 * 
	 * @throws Exception the exception
	 */
	public Vector<String> getHitsByMW(double mass, double error, Integer limit) throws Exception
	{
		Vector<String> pubchemCIDs = new Vector<String>();
		
		PUGSoap pug_soap = this.pug_locator.getPUGSoap();
		
        EUtilsServiceLocator eutils_locator = new EUtilsServiceLocator();
        EUtilsServiceSoap eutils_soap = eutils_locator.geteUtilsServiceSoap();


		
		//search "aspirin" in PubChem Compound
		ESearchRequest request = new ESearchRequest();
		String db = new String("pccompound");
		request.setDb(db);
		
		double min = mass - error;
		double max = mass + error;
		
		System.out.println("Min: " + min + " Max: " + max);
		
		request.setTerm(min + ":" + max + "[MW]");
		// create a history item, and don't return any actual ids in the
		// SOAP response
		request.setUsehistory("y");
		request.setRetMax(limit.toString());

		ESearchResult result = eutils_soap.run_eSearch(request);
		
		//String[] idList = result.getIdList();
		if (result.getQueryKey() == null || result.getQueryKey().length() == 0
				|| result.getWebEnv() == null
				|| result.getWebEnv().length() == 0) {
			throw new Exception("ESearch failed to return query_key and WebEnv");
		}
		System.out.println("ESearch returned " + result.getCount() + " hits");
		
		
		// give this Entrez History info to PUG SOAP
		EntrezKey entrezKey = new EntrezKey(db, result.getQueryKey(), result.getWebEnv());	
		String listKey = pug_soap.inputEntrez(entrezKey);
		
		System.out.println("ListKey = " + listKey);
		
		//int[] ids = pug_soap.getIDList(entrezKey.getKey());
		
		// Initialize the download; request SDF with gzip compression
		String downloadKey = pug_soap.download(listKey, FormatType.eFormat_SDF,
				CompressType.eCompress_None, false);
		System.out.println("DownloadKey = " + downloadKey);

		// Wait for the download to be prepared
		StatusType status;
		while ((status = pug_soap.getOperationStatus(downloadKey)) == StatusType.eStatus_Running
				|| status == StatusType.eStatus_Queued) {
			System.out.println("Waiting for download to finish...");
			Thread.sleep(10000);
		}

		// On success, get the download URL, save to local file
		if (status == StatusType.eStatus_Success) {
			
			// PROXY
		    System.getProperties().put( "ftp.proxySet", "true" );
		    System.getProperties().put( "ftp.proxyHost", "www.ipb-halle.de" );
		    System.getProperties().put( "ftp.proxyPort", "3128" );

			
			URL url = new URL(pug_soap.getDownloadUrl(downloadKey));
			System.out.println("Success! Download URL = " + url.toString());

			// get input stream from URL
			URLConnection fetch = url.openConnection();
			InputStream input = fetch.getInputStream();

			// open local file based on the URL file name
			String filename = "/tmp"
					+ url.getFile().substring(url.getFile().lastIndexOf('/'));
			FileOutputStream output = new FileOutputStream(filename);
			System.out.println("Writing data to " + filename);

			// buffered read/write
			byte[] buffer = new byte[10000];
			int n;
			while ((n = input.read(buffer)) > 0)
				output.write(buffer, 0, n);
			
			//now read in the file
			FileInputStream in = null;
	        in = new FileInputStream(filename);
	        
	        //IChemObjectReader cor = null;
	        //cor = new ReaderFactory().createReader(in);
	       
	        MDLV2000Reader reader = new MDLV2000Reader(in);
	        ChemFile fileContents = (ChemFile)reader.read(new ChemFile());
	        System.out.println("Got " + fileContents.getChemSequence(0).getChemModelCount() + " atom containers");
	        
	        SmilesGenerator generatorSmiles = new SmilesGenerator();
	        for (int i = 0; i < fileContents.getChemSequence(0).getChemModelCount(); i++) {
				this.containers.add(fileContents.getChemSequence(0).getChemModel(i).getMoleculeSet().getAtomContainer(0));
				Map<Object, Object> properties = fileContents.getChemSequence(0).getChemModel(i).getMoleculeSet().getAtomContainer(0).getProperties();
		        pubchemCIDs.add((String) properties.get("PUBCHEM_COMPOUND_CID"));
		        System.out.println((String) properties.get("PUBCHEM_COMPOUND_CID"));
		        this.retrievedHits.put(Integer.parseInt(properties.get("PUBCHEM_COMPOUND_CID").toString()), generatorSmiles.createSMILES(fileContents.getChemSequence(0).getChemModel(i).getMoleculeSet().getMolecule(0)));
			}

	        System.out.println("Read the file");
	        
	        new File("/tmp"	+ url.getFile().substring(url.getFile().lastIndexOf('/'))).delete();
	        
	        System.out.println("Temp file deleted!");
			
		} else {
			System.out.println("Error: "
					+ pug_soap.getStatusMessage(downloadKey));
		}
		
		return pubchemCIDs;
		
	}
	
	
	
	/**
	 * Gets the compound. You have to execute the find by mass or find by
	 * molecular formula first, otherwise it will be null.
	 * 
	 * TODO
	 * 
	 * @param pubchemCID the pubchem cid
	 * 
	 * @return the compound
	 * @throws InvalidSmilesException 
	 */
	public IAtomContainer getCompound(int pubchemCID) throws InvalidSmilesException
	{
		SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
		return sp.parseSmiles(this.retrievedHits.get(pubchemCID));		
	}
	
	
	/**
	 * Gets the mol. You have to execute the find by mass or find by
	 * molecular formula first, otherwise it will be null.
	 * 
	 * TODO
	 * 
	 * @param number the number
	 * 
	 * @return the mol
	 * @throws InvalidSmilesException 
	 * @throws NumberFormatException 
	 */
	public IAtomContainer getMol(String number) throws NumberFormatException, InvalidSmilesException
	{
		//got a new database hit...which is not stored in the database
		if(this.retrievedHits.get(Integer.parseInt(number)) == null)
			return null;
		
		SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
		return sp.parseSmiles(this.retrievedHits.get(Integer.parseInt(number)));
	}

}
