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
package de.ipbhalle.metfrag.tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class GetKEGGIdentifier {
	private HashMap<Integer, String> pubchemToKEGG;
	
	/**
	 * Instantiates a new kegg identifier. The file CID-KEGG.txt is read in.
	 * 
	 * @param path to the file CID-KEGG.txt
	 * 
	 * @throws FileNotFoundException the file not found exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public GetKEGGIdentifier(String path) throws FileNotFoundException, IOException
	{
		//now find out the KEGG identifier
		FileReader fr = new FileReader(path);
        BufferedReader br = new BufferedReader(fr);
        String line = "";
        
        pubchemToKEGG = new HashMap<Integer, String>();
        while ((line = br.readLine()) != null)
        {
        	String[] tempArray = line.split("\\\t");
			pubchemToKEGG.put(Integer.parseInt(tempArray[0]), tempArray[1]);
        }
        br.close();
	}
	
	/**
	 * Gets the kEGGID with a given Pubchem CID.
	 * 
	 * @param pubchemCID the Pubchem CID
	 * 
	 * @return the kEGGID
	 */
	public String getKEGGID(int pubchemCID)
	{
		String KEGG = "";
		KEGG = pubchemToKEGG.get(pubchemCID);
		if(KEGG == null)
			KEGG = "";
		return KEGG;
	}
	
	/**
	 * Test if the compound has an entry in KEGG
	 * 
	 * @param pubchemCID the pubchem cid
	 * 
	 * @return true, if successful
	 */
	public boolean existInKEGG(int pubchemCID)
	{
		boolean check = false;
		String kegg = getKEGGID(pubchemCID);
		if(!kegg.isEmpty())
			check = true;
		return check;
	}
}
