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
package de.ipbhalle.metfrag.database;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.ipbhalle.metfrag.spectrum.WrapperSpectrum;

public class Tools {
	
	public static int checkCHONSP(String molecularFormula)
	{
		
		//only C,H,O,N,S and P in formula, otherwise 0 
		int chonsp = 1;
		List<Character> allowedElements = new ArrayList<Character>(6);
		allowedElements.add('C');
		allowedElements.add('H');
		allowedElements.add('O');
		allowedElements.add('N');
		allowedElements.add('S');
		allowedElements.add('P');
        
        //get only the characters -> atoms
        Pattern pattern = Pattern.compile("\\W");
        
        // Replace all occurrences of pattern in input
        Matcher matcher = pattern.matcher(molecularFormula);
        String output = matcher.replaceAll("");
        
        //remove numbers
        Pattern patternD = Pattern.compile("\\d");
		matcher = patternD.matcher(output);
		output = matcher.replaceAll("");
		
		output = output.replace("-", "");
		output = output.replace("+", "");
		
        
        char[] smilesChar = output.toCharArray();
		//check for allowed atoms
        for (int i = 0; i < smilesChar.length; i++) {
			if(!allowedElements.contains(smilesChar[i]))
			{
				chonsp = 0;
				break;
			}
		}
		
		
		return chonsp;
	}
	
	public static void main(String[] args) {
		WrapperSpectrum spectrum = new WrapperSpectrum("/home/swolf/MassBankData/MetFragSunGrid/RikenDataMergedCorrect/NoCHONPS/PR101029.txt");
		checkCHONSP(spectrum.getFormula());
	}
	
}
