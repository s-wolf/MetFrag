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
package de.ipbhalle.metfrag.tools;

import java.util.Arrays;
import java.util.List;

public class Number {
	
	/**
	 * Number to fixed length.
	 *
	 * @param number the number
	 * @param desiredLength the desired length
	 * @return the string
	 */
	public static String numberToFixedLength(int number, int desiredLength) 
	{
		String numberstring = Integer.toString(number);
		int length = numberstring.length();
		String zeros = "";
		for (int i=length; i<desiredLength; ++i) zeros += "0";
		return zeros + numberstring;
	}
	
	/**
	 * Calculates the median of list with double values.
	 *
	 * @param list the list
	 * @return the median
	 */
	public static Double median(List<Double> list)
	{
		double median = 0.0;
		Double[] devArray = new Double[list.size()]; 
		devArray = list.toArray(devArray);
		Arrays.sort(devArray);
		for (int j = 0; j < devArray.length; j++) {
			//median von gerader anzahl
			if(j == (devArray.length/2) && (j%2) == 0)
			{
				median = devArray[j];
			}
			else if(j == (devArray.length/2))
			{
				median = devArray[j];
			}
		}
		
		return median;
	}
}
