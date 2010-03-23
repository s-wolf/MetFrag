package de.ipbhalle.metfrag.cdk12Testing;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import de.ipbhalle.metfrag.keggWebservice.KeggWebservice;
import de.ipbhalle.metfrag.molDatabase.PubChemLocal;
import de.ipbhalle.metfrag.pubchem.PubChemWebService;
import de.ipbhalle.metfrag.spectrum.WrapperSpectrum;


public class HitsInPubChem {

	public static void main(String[] args) {
		
		//file with assignment CID to KEGG
		String defaultFile = "CID-KEGG.txt";
		if (args.length > 0) 
		{
			if(args[0] == null)
			{
				System.err.println("No folder given! Arguments: \"Source Folder with Massbank records\" \"Filename optional: CID-KEGG.txt (default)\"");
				System.exit(1);
			}
			if(args.length == 1)
			{
				System.out.println("Using default CID-KEGG.txt! Place this file in folder with the Massbank records!");
			}
			else
			{
				defaultFile = args[1];
			}
		}
		else
		{
			System.err.println("No folder given! Options: \"Source Folder with Massbank records\" \"Filename: CID-KEGG.txt (default)\"");
			System.exit(1);
		}
		
		BufferedReader reader;
		String line = "";
		Vector<Integer> linkPubChem = new Vector<Integer>();
		Vector<String> accessionID = new Vector<String>();
		
		Map<String, Integer> pubChemToHits = new HashMap<String, Integer>(); 
		
		try
		{
			//loop over all files in folder
			File f = new File(args[0]);
			File files[] = f.listFiles();
			//sort alphabetically
			Arrays.sort(files);
	
			for(int i=0;i<files.length;i++)
			{
				if(files[i].isFile() && files[i].getName().compareTo(defaultFile) != 0)
				{
					WrapperSpectrum ws = new WrapperSpectrum(files[i].getPath()); 
//				  	System.out.println(ws.getFormula());
				  	Vector<String> hits = KeggWebservice.KEGGbySumFormula(ws.getFormula());
				  	
					System.out.println(files[i].getName() + ":" + hits.size());
					pubChemToHits.put(files[i].getName(), hits.size());
				  	
				  	
//					reader = new BufferedReader(new FileReader(files[i]));
//				  	line = reader.readLine();
//				  	accessionID.add(line.substring(11));
//				  	while (line != null && !line.contains("CH$LINK: PUBCHEM CID:")){
//					  	  line = reader.readLine();
//					}
//				  	linkPubChem.add(Integer.parseInt(line.substring(line.indexOf("CH$LINK: PUBCHEM CID:")+22).split("\\ ")[0]));
//				  	
//				  	while (line != null && !line.contains("CH$FORMULA:")){
//					  	  line = reader.readLine();
//					}

				}
			}
			
			
			//now find out the KEGG identifier
//			FileReader fr = null;
//			if(args.length > 1)
//				fr = new FileReader(args[1]);
//			else
//				fr = new FileReader(args[0].concat(defaultFile));
//			
//	        BufferedReader br = new BufferedReader(fr);
//
//	        HashMap<Integer, String> pubchemToKEGG = new HashMap<Integer, String>();
//	        while ((line = br.readLine()) != null)
//	        {
//	        	String[] tempArray = line.split("\\\t");
//				pubchemToKEGG.put(Integer.parseInt(tempArray[0]), tempArray[1]);
//	        }
//	        br.close();

			
			//now check
//	        for (int i = 0; i < linkPubChem.size(); i++) {
//				if(pubchemToKEGG.containsKey(linkPubChem.get(i)))
//					System.out.println(accessionID.get(i) + ": " + linkPubChem.get(i) + "-" + pubchemToKEGG.get(linkPubChem.get(i)));
//				else
//					System.out.println(accessionID.get(i) + ": " + linkPubChem.get(i) + "-" + "None");
//			}
	        
	        for (String pcid : pubChemToHits.keySet()) {
				System.out.println(pcid + ":" + pubChemToHits.get(pcid));
			}
	        
	        
	        
			
		}
		catch (Exception e) {
			e.printStackTrace();
			System.err.println("Error: " + e.getMessage());
		}
		
	}
}
