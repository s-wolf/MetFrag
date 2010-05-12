package de.ipbhalle.metfrag.main;

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
		//create spectrum
		WrapperSpectrum spectrum = new WrapperSpectrum(peaks, 1, exactMass);
		
		
		String output = "";
		//start MetFrag
		try {
			boolean useIPBProxy = false;
			//possible databases: kegg, pubchem, chemspider
			output = MetFrag.start("pubchem", "", "", exactMass, spectrum, useIPBProxy);
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
			e.printStackTrace();
		}
		
		System.out.println("\n\n\nMetFrag Results\n\n" + output);
		
		
	}

}
