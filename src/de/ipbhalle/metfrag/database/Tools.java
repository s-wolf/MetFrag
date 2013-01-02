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
package de.ipbhalle.metfrag.database;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	
}
