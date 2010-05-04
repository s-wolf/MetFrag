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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import javax.xml.rpc.ServiceException;

import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemModel;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.Reaction;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IChemFile;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.IChemObjectReader;
import org.openscience.cdk.io.ISimpleChemObjectReader;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.io.PCCompoundASNReader;
import org.openscience.cdk.io.ReaderFactory;
import org.openscience.cdk.io.formats.IChemFormat;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;

import gov.nih.nlm.ncbi.pubchem.AssayColumnsType;
import gov.nih.nlm.ncbi.pubchem.AssayFormatType;
import gov.nih.nlm.ncbi.pubchem.CompressType;
import gov.nih.nlm.ncbi.pubchem.EntrezKey;
import gov.nih.nlm.ncbi.pubchem.FormatType;
import gov.nih.nlm.ncbi.pubchem.LimitsType;
import gov.nih.nlm.ncbi.pubchem.MFSearchOptions;
import gov.nih.nlm.ncbi.pubchem.PCIDType;
import gov.nih.nlm.ncbi.pubchem.PUGLocator;
import gov.nih.nlm.ncbi.pubchem.PUGSoap;
import gov.nih.nlm.ncbi.pubchem.PUGSoapStub;
import gov.nih.nlm.ncbi.pubchem.StatusType;
import gov.nih.nlm.ncbi.www.soap.eutils.EUtilsServiceLocator;
import gov.nih.nlm.ncbi.www.soap.eutils.EUtilsServiceSoap;
import gov.nih.nlm.ncbi.www.soap.eutils.EUtilsServiceSoapStub;
import gov.nih.nlm.ncbi.www.soap.eutils.esearch.ESearchRequest;
import gov.nih.nlm.ncbi.www.soap.eutils.esearch.ESearchResult;

public class PUGTest {
	
	public static void testDownload() throws Exception {
		
		PUGLocator pug_locator = new PUGLocator();
		PUGSoap pug_soap = pug_locator.getPUGSoap();
		
        EUtilsServiceLocator eutils_locator = new EUtilsServiceLocator();
        EUtilsServiceSoap eutils_soap = eutils_locator.geteUtilsServiceSoap();
        
		ESearchRequest request = new ESearchRequest();
		String db = new String("pccompound");
		request.setDb(db);
		
		
		request.setTerm("C15H12O5");
		// create a history item, and don't return any actual ids in the
		// SOAP response
		request.setUsehistory("n");
		request.setRetMax("10");

		ESearchResult result = eutils_soap.run_eSearch(request);
        String[] test1 = result.getIdList();
        
		
		
		MFSearchOptions mf_options = new MFSearchOptions();
		mf_options.setAllowOtherElements(false);
		String listKey = pug_soap.MFSearch("C19H26O14", mf_options, null);
		System.out.println("MFSearch C19H26O14 " + listKey);
		
		
		StatusType status;
		while ((status = pug_soap.getOperationStatus(listKey)) == StatusType.eStatus_Running
				|| status == StatusType.eStatus_Queued) {
			System.out.println("Waiting for query to finish...");
			Thread.sleep(1000);
		}
		
		// Retrieve CIDs
		int[] cids = pug_soap.getIDList(listKey);
		Vector<String> candidatesString = new Vector<String>(); 
		String test = "";
		for (int i = 0; i < cids.length; i++) {
			candidatesString.add(cids[i] + "");
			System.out.println(cids[i]);
			test += cids[i] + ",";
		}
		
		String listkey = pug_soap.inputList(cids, PCIDType.eID_CID);
		
		String testKey = pug_soap.inputListText(test, PCIDType.eID_CID);
		
		String downloadKey = pug_soap.download(listkey, FormatType.eFormat_SDF,
				CompressType.eCompress_GZip, false);
		System.out.println("DownloadKey = " + downloadKey);
		status = null;
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
	        
//	        IChemObjectReader cor = null;
//	        cor = new ReaderFactory().createReader(in);
//	       
//	        IChemFile content = (IChemFile)cor.read(DefaultChemObjectBuilder.getInstance().newChemFile());
	        
	        ReaderFactory factory = new ReaderFactory();
	        ISimpleChemObjectReader reader = factory.createReader(in);
	        IChemFile content = (IChemFile)reader.read(new ChemFile());
	        	        
	        System.out.println("Read the file");
	        List<IAtomContainer> containers = ChemFileManipulator.getAllAtomContainers(content);
	        System.out.println("Got " + containers.size() + " atom containers");
	        
	        
	        
		}
		else
			System.out.println("ERRRRRROOOOOOOOOOOORRRRRRRRRRR!!!");
		
		
//		//search "aspirin" in PubChem Compound
//		ESearchRequest request = new ESearchRequest();
//		String db = new String("pccompound");
//		request.setDb(db);
//		request.setTerm("272.02:272.021[EMAS]");
//		// create a history item, and don't return any actual ids in the
//		// SOAP response
//		request.setUsehistory("y");
//		request.setRetMax("0");
//
//		ESearchResult result = eutils_soap.run_eSearch(request);
//		
//		//String[] idList = result.getIdList();
//		if (result.getQueryKey() == null || result.getQueryKey().length() == 0
//				|| result.getWebEnv() == null
//				|| result.getWebEnv().length() == 0) {
//			throw new Exception("ESearch failed to return query_key and WebEnv");
//		}
//		System.out.println("ESearch returned " + result.getCount() + " hits");
//		
//		
//		
//		
//		// give this Entrez History info to PUG SOAP
//		EntrezKey entrezKey = new EntrezKey(db, result.getQueryKey(), result.getWebEnv());	
//		String listKey = pug_soap.inputEntrez(entrezKey);
//		
//		System.out.println("ListKey = " + listKey);
//		
//		//int[] ids = pug_soap.getIDList(entrezKey.getKey());
//		
//		// Initialize the download; request SDF with gzip compression
//		String downloadKey = pug_soap.download(listKey, FormatType.eFormat_SDF,
//				CompressType.eCompress_None, false);
//		System.out.println("DownloadKey = " + downloadKey);
//
//		// Wait for the download to be prepared
//		StatusType status;
//		while ((status = pug_soap.getOperationStatus(downloadKey)) == StatusType.eStatus_Running
//				|| status == StatusType.eStatus_Queued) {
//			System.out.println("Waiting for download to finish...");
//			Thread.sleep(10000);
//		}
//
//		// On success, get the download URL, save to local file
//		if (status == StatusType.eStatus_Success) {
//			
//			// PROXY
//		    System.getProperties().put( "ftp.proxySet", "true" );
//		    System.getProperties().put( "ftp.proxyHost", "www.ipb-halle.de" );
//		    System.getProperties().put( "ftp.proxyPort", "3128" );
//
//			
//			URL url = new URL(pug_soap.getDownloadUrl(downloadKey));
//			System.out.println("Success! Download URL = " + url.toString());
//
//			// get input stream from URL
//			URLConnection fetch = url.openConnection();
//			InputStream input = fetch.getInputStream();
//
//			// open local file based on the URL file name
//			String filename = "/tmp"
//					+ url.getFile().substring(url.getFile().lastIndexOf('/'));
//			FileOutputStream output = new FileOutputStream(filename);
//			System.out.println("Writing data to " + filename);
//
//			// buffered read/write
//			byte[] buffer = new byte[10000];
//			int n;
//			while ((n = input.read(buffer)) > 0)
//				output.write(buffer, 0, n);
//			
//			//now read in the file
//			FileInputStream in = null;
//	        in = new FileInputStream(filename);
//	        
//	        //IChemObjectReader cor = null;
//	        //cor = new ReaderFactory().createReader(in);
//	       
//	        MDLV2000Reader reader = new MDLV2000Reader(in);
//	        ChemFile fileContents = (ChemFile)reader.read(new ChemFile());
//	        System.out.println("Got " + fileContents.getChemSequence(0).getChemModelCount() + " atom containers");
//	        Map<Object, Object> properties = fileContents.getChemSequence(0).getChemModel(0).getMoleculeSet().getAtomContainer(0).getProperties();
//	        String cid = (String) properties.get("PUBCHEM_COMPOUND_CID");
//	        System.out.println("CID: " + cid);
//
//	        //List<IAtomContainer> containers = ChemFileManipulator.getAllAtomContainers(cFile);
//	        //String title = (String)containers.get(0).getProperty(CDKConstants.TITLE);
//	        
//	        System.out.println("Read the file");
//	        //List<IAtomContainer> containers = ChemFileManipulator.getAllAtomContainers(content);
//	        //System.out.println("Got " + containers.size() + " atom containers");
//	        
//	        
//	        Vector<String> candidatesString = new Vector<String>();
//	        // Retrieve CIDs
//			//string[] s_cids = Array.ConvertAll<int, string>(cids, delegate(int i) { return i.ToString(); });	        
//	    
//			
//		} else {
//			System.out.println("Error: "
//					+ pug_soap.getStatusMessage(downloadKey));
		

	}

//	public static void main(String[] args) {
//		try {
//			testDownload();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		try {
//			PUGLocator locator = new PUGLocator();
//			PUGSoap soap = locator.getPUGSoap();
//
//			// Initialize MF search
//			String listKey = "";
//
//			listKey = soap.MFSearch("C6H11N", // formula
//					new MFSearchOptions(false, null), // don't allow other
//					null);
//			// no limits
//			System.out.println("ListKey = " + listKey);
//
//			// Wait for the search to finish
//			StatusType status = null;
//
//			while ((status = soap.getOperationStatus(listKey)) == StatusType.eStatus_Running
//					|| status == StatusType.eStatus_Queued) {
//				System.out.println("Waiting for search to finish...");
//				Thread.sleep(10000);
//			}
//
//			// On success, get the results as an Entrez URL
//			if (status == StatusType.eStatus_Success
//					|| status == StatusType.eStatus_TimeLimit
//					|| status == StatusType.eStatus_HitLimit) {
//				if (status == StatusType.eStatus_TimeLimit) {
//					System.out
//							.println("Warning: time limit reached before entire db searched");
//				} else if (status == StatusType.eStatus_HitLimit) {
//					System.out
//							.println("Warning: hit limit reached before entire db searched");
//				}
//				EntrezKey entrezKey = soap.getEntrezKey(listKey);
//				String URL = soap.getEntrezUrl(entrezKey);
//				System.out.println("Success! Entrez URL = " + URL);
//			} else {
//				System.out.println("Error: " + soap.getStatusMessage(listKey));
//			}
//
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		} catch (ServiceException e) {
//			e.printStackTrace();
//		}
//	}
	
	public static void main (String[] argv) throws Exception {
        
		PUGLocator locator = new PUGLocator();
		PUGSoap soap = locator.getPUGSoap();

	        // Initialize MF search
		String listKey = soap.MFSearch(
	                "C19H26O14",                         // formula
	                new MFSearchOptions(false, null), // don't allow other elements
	                null);                            // no limits
		System.out.println("ListKey = " + listKey);
	        
	        // Wait for the search to finish
		StatusType status;
		while ((status = soap.getOperationStatus(listKey)) 
	                    == StatusType.eStatus_Running || 
	               status == StatusType.eStatus_Queued) 
	        {
	            System.out.println("Waiting for search to finish...");
		    Thread.sleep(10000);
		}
	        
	        // On success, get the results as an Entrez URL
	        if (status == StatusType.eStatus_Success ||
	            status == StatusType.eStatus_TimeLimit ||
	            status == StatusType.eStatus_HitLimit) 
	        {
	            if (status == StatusType.eStatus_TimeLimit) {
	                System.out.println(
	                    "Warning: time limit reached before entire db searched");
	            } else if (status == StatusType.eStatus_HitLimit) {
	                System.out.println(
	                    "Warning: hit limit reached before entire db searched");
	            }
	            EntrezKey entrezKey = soap.getEntrezKey(listKey);
	            String URL = soap.getEntrezUrl(entrezKey);
	            System.out.println("Success! Entrez URL = " + URL);
	        } else {
	            System.out.println("Error: " 
	                + soap.getStatusMessage(listKey));            
	        }
	    }


}
