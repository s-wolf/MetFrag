package de.ipbhalle.metfrag.main;

import java.net.*;
import java.io.*;

public class GetMassbankID {
	
	public static String getMassbankFromPubchem(int linkID)
	{
		String massbankID = "";
		String webPage = "";
		
		try
		{
			URL pubChem = new URL("http://pubchem.ncbi.nlm.nih.gov/summary/summary.cgi?cid=" + linkID);
			BufferedReader in = new BufferedReader(
						new InputStreamReader(
						pubChem.openStream()));
	
			String inputLine;
	
			while ((inputLine = in.readLine()) != null)
			    webPage += inputLine;
	
			in.close();
			
			
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Error: " + e.getMessage());
		}
		catch(IOException e)
		{
			System.out.println("Error: " + e.getMessage());
		}
		
		massbankID = parsePubChem(webPage);
		
		return massbankID;
	}
	
	private static String parsePubChem(String webPage)
	{
		String keggID = "";
		
		//Example ID: C00389
		//SEARCH for this string: <A href="http://www.genome.jp/dbget-bin/www_bget?cpd+ Here goes the KEGG ID (has length 6)
		keggID = webPage.substring(webPage.indexOf("<A href=\"http://www.genome.jp/dbget-bin/www_bget?cpd+")+53, 53 + 6);
		return keggID;
	}

}
