package de.ipbhalle.metfrag.massbankParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class parserTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try
		{
		// TODO Auto-generated method stub
		String file = "PB000122"; 
		//folder where the .txt file is in
		String folder = "/home/basti/WorkspaceJava/MassbankData/MassbankTestData/";
		
        BufferedReader reader = new BufferedReader(new FileReader(folder + file + "/2008-07-12 23:41:51/log"));
	  	String line;
	  	// Create file 
	  	FileWriter fstream = new FileWriter(folder + file + "/2008-07-12 23:41:51/logFile.txt");
  	    BufferedWriter out = new BufferedWriter(fstream);
	  	while ((line = reader.readLine()) != null)
	  	{
	  	  if (line.length() > 5)
	  	  {
		  	  if(line.startsWith("STDOUT") || line.startsWith("STDERR"))
		  	  {
		  	    out.write(line + "\n");
		  	  }
	  	  }
	  	}
	  	out.close();
		}
		catch (Exception e)
		{
			System.out.println("Error: " + e.getMessage());
		}
	}

}
