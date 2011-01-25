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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.openscience.cdk.Molecule;
import org.openscience.cdk.MoleculeSet;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IMoleculeSet;
import org.openscience.cdk.io.SDFWriter;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;

import de.ipbhalle.metfrag.fragmenter.Fragmenter;
import de.ipbhalle.metfrag.massbankParser.Peak;
import de.ipbhalle.metfrag.pubchem.PubChemWebService;
import de.ipbhalle.metfrag.spectrum.AssignFragmentPeak;
import de.ipbhalle.metfrag.spectrum.CleanUpPeakList;
import de.ipbhalle.metfrag.spectrum.MatchedFragment;
import de.ipbhalle.metfrag.spectrum.WrapperSpectrum;

public class FragmentSinglePubChemCompound {
	
	private int treeDepth;
	private boolean sumFormulaRedundancyCheck;
	private boolean allFragments;
	private static String sample;
	
	/**
	 * Instantiates a new fragment single compound with default options:
	 * <ul>
	 * 	<li> tree depth = 2
	 * 	<li> sum formula redundancy check = true
	 *  <li> brak aromatic rings = true
	 * </ul>
	 */
	public FragmentSinglePubChemCompound()
	{
		setTreeDepth(2);
		setSumFormulaRedundancyCheck(true);
		setAllFragments(true);
	}
	
	/**
	 * Gets the fragments.
	 * 
	 * @param file the file
	 * @param mzabs the mzabs
	 * @param mzppm the mzppm
	 * 
	 * @return the fragments
	 * 
	 * @throws Exception the exception
	 */
	public List<IAtomContainer> getFragments(String file, Double mzabs, Double mzppm) throws Exception
	{
		List<IAtomContainer> ret = new ArrayList<IAtomContainer>();
		//read in file
		FileInputStream fstream = new FileInputStream(new File(file));
	    // Get the object of DataInputStream
	    DataInputStream in = new DataInputStream(fstream);
	    BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    String strLine;
	    String pubchemID = "";
	    String peaks = "";
	    Double parentMass = 0.0;
	    int mode = 1;
	    boolean isPositive = false;
	    
	    //Read File Line By Line
	    while ((strLine = br.readLine()) != null)   {
	    	
	    	//sample
	    	if(strLine.startsWith("# Sample:"))
	    		sample = strLine.substring(10).replace('/', '_');
	    	
	    	//pubchem id
	    	if(strLine.startsWith("# PubChem ID:"))
	    		pubchemID = strLine.substring(14);
	    	
	    	//parent mass
	    	if(strLine.startsWith("# Parent Mass:"))
	    		parentMass = Double.parseDouble(strLine.substring(15));
	    	
	    	//mode
	    	if(strLine.startsWith("# Mode:"))
	    	{
	    		mode = Integer.parseInt(strLine.substring(8));
	    		if(mode == 0)
	    		{
	    			if(strLine.contains("\\+"))
	    				isPositive = true;
	    		}
	    	}
	    	
	    	//peaks
	    	if(!strLine.startsWith("#"))
	    		peaks += strLine + "\n";
	    }
	      
	    PubChemWebService pubchemService = new PubChemWebService();
    	IAtomContainer molecule = pubchemService.getSingleMol(pubchemID, true);
	    //now fragment the molecule
    	try
	    {
		    //add hydrogens
		    CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(molecule.getBuilder());

		    for (IAtom atom : molecule.atoms()) {
		    	IAtomType type = matcher.findMatchingAtomType(molecule, atom);
		        AtomTypeManipulator.configure(atom, type);
		    }
		    CDKHydrogenAdder hAdder = CDKHydrogenAdder.getInstance(molecule.getBuilder());
		    hAdder.addImplicitHydrogens(molecule);
		    AtomContainerManipulator.convertImplicitToExplicitHydrogens(molecule);
	    }
	    //there is a bug in cdk?? error happens when there is a S or Ti in the molecule
	    catch(IllegalArgumentException e)
        {
	    	System.out.println("Error: Something wrong with molecule");
        }
	        
	    WrapperSpectrum spectrum = new WrapperSpectrum(peaks, 1, parentMass, isPositive);
	    //get the original peak list again
		Vector<Peak> peakListParsed = spectrum.getPeakList();
		//clean up peak list
		CleanUpPeakList cList = new CleanUpPeakList((Vector<Peak>) peakListParsed.clone());
		Vector<Peak> cleanedPeakList = cList.getCleanedPeakList(spectrum.getExactMass());
	    Fragmenter fragmenter = new Fragmenter(cleanedPeakList, mzabs, mzppm, 1, true, sumFormulaRedundancyCheck, false);     
	    List<IAtomContainer> l = null;
	    try
	    {
	    	l = fragmenter.generateFragmentsInMemory(molecule, true, 2);
	    }
	    catch(OutOfMemoryError e)
	    {
	    	System.out.println("OUT OF MEMORY ERROR! " + pubchemID);
	    }
      
	    List<IAtomContainer> fragments = l; 
	    
	    
		//now find corresponding fragments to the mass
		AssignFragmentPeak afp = new AssignFragmentPeak(3);
		afp.assignFragmentPeak(fragments, cleanedPeakList, mzabs, mzppm, spectrum.getMode(), true, isPositive);
		Vector<MatchedFragment> hits = afp.getHits();
		
		if(isAllFragments())
			ret = fragments;
		else
		{
			for (MatchedFragment peakMolPair : hits) {
				ret.add(peakMolPair.getFragmentStructure());
			}
		}
		
		return ret;
	}
	

	public void setTreeDepth(int treeDepth) {
		this.treeDepth = treeDepth;
	}
	public int getTreeDepth() {
		return treeDepth;
	}
	
	
	public void setAllFragments(boolean allFragments) {
		this.allFragments = allFragments;
	}
	public boolean isAllFragments() {
		return allFragments;
	}
	

	public void setSumFormulaRedundancyCheck(boolean sumFormulaRedundancyCheck) {
		this.sumFormulaRedundancyCheck = sumFormulaRedundancyCheck;
	}
	public boolean isSumFormulaRedundancyCheck() {
		return sumFormulaRedundancyCheck;
	}
	
	public void setSample(String sample) {
		this.sample = sample;
	}
	public String getSample() {
		return sample;
	}
	
	
	public static void main(String[] args) {
		String file = "/home/swolf/MassBankData/TestSpectra/RikenMerged/PR100376.txt";
		Double mzabs = 0.0;
		Double mzppm = 10.0;
		Integer treeDepth = 2;
		Integer allFragments = 0;
		
		//get command line arguments
		if(args != null && args.length >= 3)
		{
			file = args[0];
			mzabs = Double.parseDouble(args[1]);
			mzppm = Double.parseDouble(args[2]);
			if(args.length > 3)
				treeDepth = Integer.parseInt(args[3]);
			if(args.length > 4)
				allFragments = Integer.parseInt(args[4]);
		}
		else
		{
			System.err.println("Please enter CL values!\n1. value: Complete Path to file\n2. value: mzabs\n3. value: mzppm\nExample: /home/frasche/MM48_MSMSpos_MH3_20_1-A,1_01_13435-15.mb 0.01 10\n");
			System.exit(1);
		}
		
		FragmentSinglePubChemCompound test = new FragmentSinglePubChemCompound();
		//those are the default values...no need to set them like this
		test.setSumFormulaRedundancyCheck(true);
		test.setTreeDepth(treeDepth);
		if(allFragments.equals(1))
			test.setAllFragments(true);
		else
			test.setAllFragments(false);
		
		List<IAtomContainer> resultingFragments = new ArrayList<IAtomContainer>();
		try {
			resultingFragments = test.getFragments(file, mzabs, mzppm);
		} catch (Exception e) {
			System.out.println("Error! TODO...");
			e.printStackTrace();
		}
		

		IMoleculeSet setOfMolecules = new MoleculeSet();
		for (IAtomContainer iAtomContainer : resultingFragments) {
			IMolecule mol = new Molecule(iAtomContainer);
			setOfMolecules.addAtomContainer(mol);
		}		

		try {
			SDFWriter writer = new SDFWriter(new FileWriter(new File(sample + ".sdf")));
			writer.write(setOfMolecules);
			writer.close();
		} catch (CDKException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
