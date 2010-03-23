package de.ipbhalle.metfrag.tools;

public class Number {
	
	public static String numberToFixedLength(int number, int desiredLength) 
	{
		String numberstring = Integer.toString(number);
		int length = numberstring.length();
		String zeros = "";
		for (int i=length; i<desiredLength; ++i) zeros += "0";
		return zeros + numberstring;
	}
}
