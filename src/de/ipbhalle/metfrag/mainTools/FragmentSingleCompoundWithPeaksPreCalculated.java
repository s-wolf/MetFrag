package de.ipbhalle.metfrag.mainTools;

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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import junit.framework.Assert;

import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.MDLReader;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

import de.ipbhalle.metfrag.fragmenter.Fragmenter;
import de.ipbhalle.metfrag.keggWebservice.KeggWebservice;
import de.ipbhalle.metfrag.massbankParser.Peak;
import de.ipbhalle.metfrag.read.CMLTools;
import de.ipbhalle.metfrag.read.Molfile;
import de.ipbhalle.metfrag.spectrum.AssignFragmentPeak;
import de.ipbhalle.metfrag.spectrum.CleanUpPeakList;
import de.ipbhalle.metfrag.spectrum.MatchedFragment;
import de.ipbhalle.metfrag.spectrum.WrapperSpectrum;
import de.ipbhalle.metfrag.tools.MolecularFormulaTools;
import de.ipbhalle.metfrag.tools.renderer.StructureRendererTable;


public class FragmentSingleCompoundWithPeaksPreCalculated {
	
	private List<File> l1 = null;
	private List<IAtomContainer> l2 = null;
	WrapperSpectrum spectrum = null;
	double mzabs = 0.01;
	double mzppm = 10.0;
	IAtomContainer molecule;
	

	public FragmentSingleCompoundWithPeaksPreCalculated(IAtomContainer mol) throws CDKException {
		
		this.molecule = mol;
		
		String peaks = "220.07 300 30\n" +
				"238.08 500 50\n" +
				"289.10 200 20\n";
		
//		double exactMass = 504.252332;
//		String peaks = "279.1745 250\n" + 
//		   "301.1592 250\n" +
//		   "319.1698 250\n" +
//		   "337.1801 250\n" +
//		   "355.1908 200\n" +
//		   "393.2061 200\n" +
//		   "411.2172 999 13\n" +
//		   "485.2534 400 16\n";
		int mode = 1;

		AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
		Double massDoubleOrig = MolecularFormulaTools.getMonoisotopicMass(MolecularFormulaManipulator.getMolecularFormula(molecule));
		massDoubleOrig = (double)Math.round((massDoubleOrig)*10000)/10000;
		String massOrig = massDoubleOrig.toString();
		spectrum = new WrapperSpectrum(peaks, mode, massDoubleOrig, true);		

		        
        Fragmenter fragmenter = new Fragmenter((Vector<Peak>)spectrum.getPeakList().clone(), mzabs, mzppm, mode, true, true, true, false);
        try {
//        	l1 = fragmenter.generateFragmentsEfficient(molecule, true, 2, "C00509", true);
			l2 = fragmenter.generateFragmentsInMemory(molecule, true, 2, true);
		} catch (CDKException e1) {
			e1.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	
	public void testFragmenterWithoutHydrogen() throws FileNotFoundException, CDKException
	{
		//get the original peak list again
		Vector<Peak> peakListParsed = spectrum.getPeakList();
		
		
		//clean up peak list
		CleanUpPeakList cList = new CleanUpPeakList((Vector<Peak>) peakListParsed.clone());
		Vector<Peak> cleanedPeakList = cList.getCleanedPeakList(spectrum.getExactMass());
		
		
		List<IAtomContainer> l = new ArrayList<IAtomContainer>();
//		l = Molfile.ReadfolderTemp(l1);
		l = l2;
		
		StructureRendererTable.Draw(molecule, l, "");
		
		//now find corresponding fragments to the mass
		AssignFragmentPeak afp = new AssignFragmentPeak(3);
		try {
			afp.assignFragmentPeak(l, cleanedPeakList, mzabs, mzppm, spectrum.getMode(), false, spectrum.isPositive());
		} catch (CDKException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Vector<MatchedFragment> hits = afp.getHits();
		Vector<Double> peaksFound = new Vector<Double>();
		
		//get all the identified peaks
		for (int i = 0; i < hits.size(); i++) {
			peaksFound.add(hits.get(i).getPeak().getMass());
		}
		
		StructureRendererTable.DrawHits(molecule, hits, "");
	}
	
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		String file = "/home/swolf/MOPAC/ProofOfConcept/pubchem/CID_3365_spectrum/mopac/3365.sdf_Combined.cml";
		
		FragmentSingleCompoundWithPeaksPreCalculated test = null;
		try {
			IAtomContainer mol = CMLTools.read(new File(file));
			test = new FragmentSingleCompoundWithPeaksPreCalculated(mol);
		} catch (CDKException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		System.out.println("Fragmenter :"+ (endTime-startTime));
		try {
			startTime = System.currentTimeMillis();
			test.testFragmenterWithoutHydrogen();
			endTime = System.currentTimeMillis();
			System.out.println("Assignment :"+ (endTime-startTime));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CDKException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

