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

package de.ipbhalle.metfrag.main;

import de.ipbhalle.metfrag.spectrum.WrapperSpectrum;

public class StartMetFrag {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Double exactMass = 272.06847;
		String peaks = "119.051 467.616\n" +
		   "123.044 370.662\n" +
		   "147.044 6078.145\n" +
		   "153.019 10000.0\n" +
		   "179.036 141.192\n" +
		   "189.058 176.358\n" +
		   "273.076 10000.000\n" +
		   "274.083 318.003\n";
		
		//create spectrum
		WrapperSpectrum spectrum = new WrapperSpectrum(peaks, 1, exactMass, true);
		
		
		String output = "";
		//start MetFrag
		try {
			boolean useIPBProxy = false;
			//possible databases: kegg, pubchem, chemspider
			output = MetFrag.start("pubchem", "932", "", exactMass, spectrum, useIPBProxy, "/home/swolf/metFrag/");
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
		
		System.out.println("\n\n\nMetFrag Results\n\n" + output);
		
		
	}

}
