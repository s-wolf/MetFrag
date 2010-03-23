package de.ipbhalle.metfrag.pubchem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.xml.rpc.ServiceException;

import org.openscience.cdk.ChemFile;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.smiles.SmilesGenerator;

import gov.nih.nlm.ncbi.pubchem.CompressType;
import gov.nih.nlm.ncbi.pubchem.EntrezKey;
import gov.nih.nlm.ncbi.pubchem.FormatType;
import gov.nih.nlm.ncbi.pubchem.PUGLocator;
import gov.nih.nlm.ncbi.pubchem.PUGSoap;
import gov.nih.nlm.ncbi.pubchem.StatusType;
import gov.nih.nlm.ncbi.www.soap.eutils.EUtilsServiceLocator;
import gov.nih.nlm.ncbi.www.soap.eutils.EUtilsServiceSoap;
import gov.nih.nlm.ncbi.www.soap.eutils.esearch.ESearchRequest;
import gov.nih.nlm.ncbi.www.soap.eutils.esearch.ESearchResult;

public class ESearchDownload {    
	
	/**
	 * Esearch download with exact mass for compounds only until 2006/02/06.
	 * 
	 * @param exactMass the exact mass
	 * @param error the error
	 * 
	 * @return the vector< string>
	 * 
	 * @throws Exception the exception
	 */
	public static Vector<String> ESearchDownloadExactMassFebruary2006(double lowerBound, double upperBound) throws Exception
	{
		Vector<String> foundIds = new Vector<String>();
	
        EUtilsServiceLocator eutils_locator = new EUtilsServiceLocator();
        EUtilsServiceSoap eutils_soap = eutils_locator.geteUtilsServiceSoap();
		PUGLocator pug_locator = new PUGLocator();
		PUGSoap pug_soap = pug_locator.getPUGSoap();
        
        // search "aspirin" in PubChem Compound
        ESearchRequest request = new ESearchRequest();
        String db = new String("pccompound");
        request.setDb(db);
		//932[uid] AND ((-2147483648 : 2008/02/28[CreateDate]))
		request.setTerm(lowerBound + ":" + upperBound + "[EMAS]" + " AND ((-2147483648 : 2006/02/06[CreateDate])");
        // create a history item, and don't return any actual ids in the
        // SOAP response
        request.setUsehistory("y");
        request.setRetMax("0");
        ESearchResult result = eutils_soap.run_eSearch(request);
        if (result.getQueryKey() == null || result.getQueryKey().length() == 0 ||
            result.getWebEnv() == null || result.getWebEnv().length() == 0)
        {
            throw new Exception("ESearch failed to return query_key and WebEnv");
        }
        System.out.println("ESearch returned " + result.getCount() + " hits");
        
        // give this Entrez History info to PUG SOAP
        EntrezKey entrezKey = new EntrezKey(db, result.getQueryKey(), result.getWebEnv());
        String listKey = pug_soap.inputEntrez(entrezKey);
        System.out.println("ListKey = " + listKey);

        // Initialize the download; request SDF with gzip compression
		String downloadKey = pug_soap.download(listKey, FormatType.eFormat_SDF, CompressType.eCompress_None, false);
		System.out.println("DownloadKey = " + downloadKey);
	        
	        // Wait for the download to be prepared
		StatusType status;
		while ((status = pug_soap.getOperationStatus(downloadKey)) 
	                    == StatusType.eStatus_Running || 
	               status == StatusType.eStatus_Queued) 
	        {
	            System.out.println("Waiting for download to finish...");
		    Thread.sleep(10000);
		}
        
        // On success, get the download URL, save to local file
        if (status == StatusType.eStatus_Success) {
        	
        	// PROXY
		    System.getProperties().put( "ftp.proxySet", "true" );
		    System.getProperties().put( "ftp.proxyHost", "www.ipb-halle.de" );
		    System.getProperties().put( "ftp.proxyPort", "3128" );
        	
        	
		    URL url = null;
		    InputStream input = null;
		    
            try
            {
            	url = new URL(pug_soap.getDownloadUrl(downloadKey));
                System.out.println("Success! Download URL = " + url.toString());
                
                // get input stream from URL
                URLConnection fetch = url.openConnection();
            	input = fetch.getInputStream();
            }
            catch(IOException e)
            {
            	System.out.println("Error downloading! " + e.getMessage());
            	//try again!
            	ESearchDownloadExactMassFebruary2006(lowerBound, upperBound);
            }
            
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
            
            MDLV2000Reader reader = new MDLV2000Reader(in);
	        ChemFile fileContents = (ChemFile)reader.read(new ChemFile());
	        System.out.println("Got " + fileContents.getChemSequence(0).getChemModelCount() + " atom containers");
	        
	        for (int i = 0; i < fileContents.getChemSequence(0).getChemModelCount(); i++) {
				Map<Object, Object> properties = fileContents.getChemSequence(0).getChemModel(i).getMoleculeSet().getAtomContainer(0).getProperties();
		        System.out.println((String) properties.get("PUBCHEM_COMPOUND_CID"));
		        foundIds.add(properties.get("PUBCHEM_COMPOUND_CID").toString());
			}

	        System.out.println("Read the file");
	        
	        new File("/tmp"	+ url.getFile().substring(url.getFile().lastIndexOf('/'))).delete();
	        
	        System.out.println("Temp file deleted!");
            
        } else {
            System.out.println("Error: " 
                + pug_soap.getStatusMessage(downloadKey));            
        }
        
        
        return foundIds;
    }


}
