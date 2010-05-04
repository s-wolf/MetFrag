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
