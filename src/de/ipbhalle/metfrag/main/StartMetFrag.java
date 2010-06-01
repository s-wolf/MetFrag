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

package de.ipbhalle.metfrag.main;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.graph.ConnectivityChecker;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.io.MDLReader;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

import de.ipbhalle.metfrag.chemspiderClient.ChemSpider;
import de.ipbhalle.metfrag.fragmenter.Fragmenter;
import de.ipbhalle.metfrag.keggWebservice.KeggWebservice;
import de.ipbhalle.metfrag.massbankParser.Peak;
import de.ipbhalle.metfrag.pubchem.PubChemWebService;
import de.ipbhalle.metfrag.scoring.Scoring;
import de.ipbhalle.metfrag.spectrum.AssignFragmentPeak;
import de.ipbhalle.metfrag.spectrum.CleanUpPeakList;
import de.ipbhalle.metfrag.spectrum.PeakMolPair;
import de.ipbhalle.metfrag.spectrum.WrapperSpectrum;
import de.ipbhalle.metfrag.tools.MolecularFormulaTools;
import de.ipbhalle.metfrag.tools.PPMTool;

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
		
		//alternatively read peaklist from file
//		String peaks = "";
//		try
//		{
//			FileInputStream fstream = new FileInputStream("/path/to/file");
//		    DataInputStream in = new DataInputStream(fstream);
//		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
//		    String strLine;
//		    //Read File Line By Line
//		    while ((strLine = br.readLine()) != null)   {
//		      peaks += strLine;
//		    }
//		    in.close();
//		}
//		catch(IOException e)
//		{
//			System.err.println("File not found! " + e.getMessage());
//		}
		
		//create spectrum
		WrapperSpectrum spectrum = new WrapperSpectrum(peaks, 1, exactMass);
		
		
		String output = "";
		//start MetFrag
		try {
			boolean useIPBProxy = false;
			//possible databases: kegg, pubchem, chemspider
			output = MetFrag.start("pubchem", "932", "", exactMass, spectrum, useIPBProxy, "/home/swolf/test.sdf");
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
		
		System.out.println("\n\n\nMetFrag Results\n\n" + output);
		
		
	}

}
