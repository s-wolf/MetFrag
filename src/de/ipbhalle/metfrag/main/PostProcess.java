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

import java.util.Iterator;
import java.util.List;
import java.util.Vector;


import org.openscience.cdk.Atom;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.Bond;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

import de.ipbhalle.metfrag.massbankParser.Peak;
import de.ipbhalle.metfrag.tools.MolecularFormulaTools;



/**
 * The Class PostProcess. Do not USE ANYMORE!
 */
public class PostProcess {
	
	/** The test. */
	private boolean test = false;
	
	/** The candidate. */
	private boolean candidate = false;
	
	/**
	 * Post process the fragments to possibly find rearrangements. DO NOT USE ANYMORE! This is done in the fragmenter!
	 * 
	 * @param fragments the fragments
	 * @param peakList the peak list
	 * @param mzabs the mzabs
	 * @param mode the mode
	 * @param mzppm the mzppm
	 * 
	 * @return the i atom container set
	 */
	public IAtomContainerSet postProcess(IAtomContainerSet fragments, Vector<Peak> peakList, double mzabs, double mzppm, int mode)
	{
		IAtomContainer newFrag = new AtomContainer();
		IAtomContainerSet frags = fragments;
		
		for (int i = 0; i < peakList.size(); i++) {
			for (int j = 0; j < frags.getAtomContainerCount(); j++) {
				IAtomContainer frag = frags.getAtomContainer(j);
				newFrag = matchByMassWater(frag, peakList.get(i).getMass(), mzabs, mzppm, mode);
				if(test && candidate)
				{
					this.test = false;
					this.candidate = false;
					fragments.addAtomContainer(newFrag);
				}
			}
		}			
		
		return fragments;
	}
	
	/**
	 * Match by mass when water is split up.
	 * Rule: R---O-|-CH---RH   -->  H2O + C===R
	 * 
	 * @param frag the frag
	 * @param peak the peak
	 * @param mzabs the mzabs
	 * @param mzppm the mzppm
	 * @param mode the mode
	 * 
	 * @return true, if successful
	 */
	private IAtomContainer matchByMassWater(IAtomContainer frag, double peak, double mzabs, double mzppm, int mode)
	{
		IAtomContainer result = new AtomContainer();
		IMolecule m = new Molecule(frag);
        IMolecularFormula molecularFormula = MolecularFormulaManipulator.getMolecularFormula(m);  
        
        //water
        double massWater = MolecularFormulaTools.getMonoisotopicMass("H2O");
        

        double mass = MolecularFormulaTools.getMonoisotopicMass(molecularFormula);
        //remove water from molecule
        mass = mass - massWater;
        
        double peakLow = (peak) - mzabs - mzppm;
        double peakHigh = (peak) + mzabs + mzppm;
        double protonMass = 1.007276466 * (double)mode;
        
        if(((mass+protonMass) >= peakLow && (mass+protonMass) <= peakHigh)) //allowed error
        {
        	test = true;
        	//try to rearrange result
        	result = rearrangeWater(frag);
        }
		
		return result;
	}
	
	/**
	 * Split water. --> find a candidate oxygen atom!
	 * Rule: R---O-|-CH---RH   -->  H2O + C===R
	 * 
	 * @param frag the frag
	 * 
	 * @return the i atom container
	 */
	private IAtomContainer rearrangeWater(IAtomContainer frag)
	{
		IAtomContainer temp = new AtomContainer();
		for (IBond bond : frag.bonds()) {            
            // lets see if it is a terminal bond...check for oxygen
			for (IAtom atom : bond.atoms()) {
				//a possible hit....try to find 2 hydrogens...terminal oxygen
				if (frag.getConnectedAtomsCount(atom) == 1 && atom.getSymbol().startsWith("O")) {
					temp = checkForHydrogens(frag, atom);
					return temp;
				}
			}
        }
		return temp;
	}
        
    /**
     * Check for 2 hydrogens in the next 2 Atoms.
     * 
     * @param candidateOxygen the candidate oxygen atom
     * @param frag the frag
     * 
     * @return true, if successful
     */
    private IAtomContainer checkForHydrogens(IAtomContainer frag, IAtom candidateOxygen)
    {
    	candidate = false;
    	IAtomContainer ret = new AtomContainer();
    	
    	//there should only be one connected atom
    	List<IBond> bondList = frag.getConnectedBondsList(candidateOxygen);
    	IAtom tempAtom = new Atom();
    	for (IAtom atom : bondList.get(0).atoms()) {
    		//thats the terminal oxygen
    		if(atom.getSymbol().startsWith("O"))
    			continue;
    		//save atom
    		if(atom.getSymbol().startsWith("C"))
    		{
    			tempAtom = atom;
    		}
    		else
    		{
    			//not a carbon....return
    			return ret;
    		}
    		
    	}
    	
    	boolean firstHydrogen = false;
    	Vector<IAtom> carbonList = new Vector<IAtom>();
    	
    	//atom and bonds to be removed
    	IAtom firstAtom = new Atom();
    	IBond firstBond = new Bond();
    	
    	//all bonds from carbon...check for hydrogen
    	bondList = frag.getConnectedBondsList(tempAtom);
    	for (int i = 0; i < bondList.size(); i++) {
			
    		if(bondList.get(i).getConnectedAtom(tempAtom).getSymbol().startsWith("H") && !firstHydrogen)
    		{
    			//one hydrogen found
    			firstHydrogen = true;
    			//the atom to be removed
    			firstAtom = bondList.get(i).getConnectedAtom(tempAtom);
    			//the bond to be removed
    			firstBond = frag.getBond(tempAtom, bondList.get(i).getConnectedAtom(tempAtom));	
    		}
    		
    		
    		
    		//else if(bondList.get(i).getConnectedAtom(tempAtom).getSymbol() == "H" && firstHydrogen)
    		//{
    			//second Hydrogen found
    		//	secondHydrogen = true;
    		//	return true;
    		//}
    			
    		
    		if(bondList.get(i).getConnectedAtom(tempAtom).getSymbol().startsWith("C"))
    		{
    			//add carbon to list to find more hydrogen
    			carbonList.add(bondList.get(i).getConnectedAtom(tempAtom));
    		}
		}
    	
    	
    	//now check those carbons in the list for another hydrogen atom
    	//atom and bonds to be removed
    	IAtom secondAtom = new Atom();
    	IBond secondBond = new Bond();
    	
    	for (int i = 0; i < carbonList.size(); i++) {
    		//get next carbon
    		bondList = frag.getConnectedBondsList(carbonList.get(i));
    		//now check carbon from list for another hydrogen
    		for (int j = 0; j < bondList.size(); j++) {
    			if(bondList.get(j).getConnectedAtom(carbonList.get(i)).getSymbol().startsWith("H"))
        		{
        			//the atom to be removed
        			secondAtom = bondList.get(j).getConnectedAtom(carbonList.get(i));
        			//the bond to be removed
        			secondBond = frag.getBond(carbonList.get(i), bondList.get(j).getConnectedAtom(carbonList.get(i)));
        			
        			//Render.Draw(frag, "Hit");
        			//remove the hydrogen and the bond to the hydrogen 
        			frag.removeAtomAndConnectedElectronContainers(secondAtom);
        			frag.removeBond(secondBond);
        			//Render.Draw(frag, "Rearranged!!!1");
        			//from the first hydrogen too
        			frag.removeAtomAndConnectedElectronContainers(firstAtom);
        			frag.removeBond(firstBond);
        			//Render.Draw(frag, "Rearranged!!!2");
        			
        			//now remove the oxygen too
        			frag.removeAtomAndConnectedElectronContainers(candidateOxygen);
        			frag.removeBond(tempAtom,candidateOxygen);
        			
        			
        			//create a double bond from the bond between the carbon
        			//IBond doubleBond = frag.getBond(tempAtom, carbonList.get(i));
        			frag.removeBond(tempAtom, carbonList.get(i));
        			IChemObjectBuilder builder = DefaultChemObjectBuilder.getInstance();
        			IBond b = builder.newBond(tempAtom, carbonList.get(i), IBond.Order.DOUBLE);
        	        frag.addBond(b);
        	        ret = frag;
        	        
        	        //Render.Draw(frag, "Rearranged");
        	        
        	        candidate = true;
        	        return ret;
        		}
    		}
    		
		}
    	
    	return ret;
    }
       
	
}
