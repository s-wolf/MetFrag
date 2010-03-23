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
