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
package de.ipbhalle.metfrag.fragmenter;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Vector;

import org.openscience.cdk.Atom;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.Bond;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Isotope;
import org.openscience.cdk.RingSet;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.formula.MolecularFormula;
import org.openscience.cdk.graph.ConnectivityChecker;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IElement;
import org.openscience.cdk.interfaces.IIsotope;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IMoleculeSet;
import org.openscience.cdk.interfaces.IRingSet;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

import de.ipbhalle.metfrag.spectrum.AssignFragmentPeak;

public class PostProcess {
	
	/** The all rings. */
	private List<IBond> aromaticBonds;
	private IRingSet allRingsOrig;
	private IRingSet allRings;
    private Map<Double, NeutralLoss> neutralLoss;
	
	/**
	 * Instantiates a new post processing step. 
	 * It reads in the neutral loss table and needs all the aromatic and
	 * ring bonds.
	 * 
	 * @param original the original
	 * @param aromaticBonds the aromatic bonds
	 * @param allRings the all rings
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public PostProcess(List<IBond> aromaticBonds, IRingSet allRings, Map<Double, NeutralLoss> neutralLossTable) throws NumberFormatException, IOException
	{
		this.aromaticBonds = aromaticBonds;
		this.allRingsOrig = allRings;
		this.neutralLoss = neutralLossTable;
	}
	
	
	/**
	 * Post process a fragment. --> find neutral possible neutral losses
	 * read in from the file
	 * 
	 * @param original the original
	 * 
	 * @return the i atom container set
	 * 
	 * @throws CDKException the CDK exception
	 * @throws CloneNotSupportedException the clone not supported exception
	 */
	public List<IAtomContainer> postProcess(IAtomContainer original, double neutralLossMass) throws CDKException, CloneNotSupportedException
	{
		//Render.Draw(original, "Original Main");
		
		List<IAtomContainer> ret = new ArrayList<IAtomContainer>();
		allRings = new RingSet();
		
		if(allRingsOrig.getAtomContainerCount() > 0)
		{
			//get the rings which are not broken up yet
			HashMap<IBond, Integer> bondMap = new HashMap<IBond, Integer>();
			int count = 0;
			
			for (IBond bondOrig : original.bonds()) {
				bondMap.put(bondOrig, count);
				count++;
			}
			
			
			//check for rings which are not broken up!
			IRingSet validRings = new RingSet();
			for (int i = 0; i < allRingsOrig.getAtomContainerCount(); i++) {
				int bondcount = 0;
				
				for (IBond bondRing : allRingsOrig.getAtomContainer(i).bonds()) {
					if(bondMap.containsKey(bondRing))
						bondcount++;
				}
				if(bondcount == allRingsOrig.getAtomContainer(i).getBondCount())
					validRings.addAtomContainer(allRingsOrig.getAtomContainer(i));
			}
			//rings which are not split up
			allRings = validRings;
		}
		
		IChemObjectBuilder builder = NoNotificationChemObjectBuilder.getInstance();
        IAtomContainer temp = builder.newInstance(IAtomContainer.class);
		List<IAtom> doneAtoms = new ArrayList<IAtom>();
		List<IBond> doneBonds = new ArrayList<IBond>();	
			
		//now find out the important atoms of the neutral loss
		String atomToStart = neutralLoss.get(neutralLossMass).getAtomToStart();		
		
		for (IBond bond : original.bonds()) {
	         
			if(doneBonds.contains(bond)){
				continue;
			}
			else{
				doneBonds.add(bond);
			}
			
			
            // check if this was checked b4
			for (IAtom atom : bond.atoms()) {
				
				if(doneAtoms.contains(atom)){
					continue;
				}
				else{
					doneAtoms.add(atom);
				}
				
				
				//a possible hit
				if (atom.getSymbol().equals(atomToStart) && !allRings.contains(atom))
				{					
//					Render.Draw(original, "BEFORE");
					//check if it is a terminal bond...and not in between!
					List<IAtom> atomList = original.getConnectedAtomsList(atom);
					int atomCount = 0;
					for (IAtom iAtom : atomList) {
						//dont check 
						if(iAtom.getSymbol().equals("H"))
							continue;
						else
							atomCount++;
					}
					//not a terminal atom...so skip it!
					if(atomCount > 1)
						continue;
					
					temp = checkForCompleteNeutralLoss(original, atom, neutralLossMass);
					if(temp.getAtomCount() > 0)
					{
						if(ConnectivityChecker.isConnected(temp))
						{
							ret.add(temp);
						}
						else
						{
							IMoleculeSet set = ConnectivityChecker.partitionIntoMolecules(temp);
							for (IAtomContainer molecule : set.molecules()) {
								ret.add(molecule);
							}
						}
						//create a atom container
						temp = builder.newInstance(IAtomContainer.class);
					}
				}
			}
        }	
    	return ret;
	}
	
	
    /**
     * Check for the other atoms nearby in the fragment.
     * 
     * @param candidateOxygen the candidate oxygen atom
     * @param frag the frag
     * @param proton the proton
     * 
     * @return true, if successful
     * @throws CloneNotSupportedException 
     * @throws CDKException 
     */
    private IAtomContainer checkForCompleteNeutralLoss(IAtomContainer frag, IAtom candidateAtom, double neutralLossMass) throws CloneNotSupportedException, CDKException
    {
    	IChemObjectBuilder builder = NoNotificationChemObjectBuilder.getInstance();
        IAtomContainer ret = builder.newInstance(IAtomContainer.class);

    	//create a copy from the original fragment
    	List<IBond> part = new ArrayList<IBond>();
    	part = traverse(frag, candidateAtom, part);
    	IAtomContainer fragCopy = makeAtomContainer(candidateAtom, part);
    	
    	
    	//set properties again
    	Map<Object, Object> properties = frag.getProperties();
        fragCopy.setProperties(properties);
        
    	//now get the other atoms from the neutral loss
    	List<String> atomsToFind = new ArrayList<String>();
    	boolean addHydrogen = false;
    	//one hydrogen is lost with the neutral loss
    	if(neutralLoss.get(neutralLossMass).getHydrogenDifference() == -1)
    	{
	    	for (IIsotope isotope : neutralLoss.get(neutralLossMass).getElementalComposition().isotopes()) {
	    		int count = neutralLoss.get(neutralLossMass).getElementalComposition().getIsotopeCount(isotope);
	    		for (int i = 0; i < count; i++)
	    		{
	    			atomsToFind.add(isotope.getSymbol());
	    		}
			}
    	}
    	else
    	{
    		for (IIsotope isotope : neutralLoss.get(neutralLossMass).getTopoFragment().isotopes()) {
    			int count = neutralLoss.get(neutralLossMass).getElementalComposition().getIsotopeCount(isotope);
	    		for (int i = 0; i < count; i++)
	    		{
	    			atomsToFind.add(isotope.getSymbol());
	    		}
    			addHydrogen = true;
			}
    	}
    	
    	
    	//at most 2 bonds between the oxygen and other atoms (at most 1 H and 2 C)
    	int count = neutralLoss.get(neutralLossMass).getDistance();
    	//list storing all atoms to be removed later on if complete neutral loss was found
    	List<IAtom> foundAtoms = new ArrayList<IAtom>();
    	//list storing all bonds to remove
    	List<IBond> foundBonds = new ArrayList<IBond>();
    	//list storing all bonds already checked
    	List<IBond> checkedBonds = new ArrayList<IBond>();
    	//list storing all checked atoms
    	List<IAtom> checkedAtoms = new ArrayList<IAtom>();
    	//queue storing all bonds to check for a particular distance
    	List<IBond> bondQueue = new ArrayList<IBond>();
    	//List storing all bonds to be checked for the next distance
    	List<IBond> bondsFurther = new ArrayList<IBond>();
    	//get all bonds from this atom distance = 1

    	List<IBond> bondList = fragCopy.getConnectedBondsList(candidateAtom);
    	for (IBond bond : bondList) {
			if(bond != null)
				bondQueue.add(bond);
		}
    	
    	int hydrogenStartAtom = neutralLoss.get(neutralLossMass).getHydrogenOnStartAtom();
    	boolean firstBonds = true;
    	
    	while(count > 0)
    	{   
    		IBond currentBond = null;
    		if(bondQueue.size() > 0)
    			currentBond = bondQueue.remove(bondQueue.size() - 1);
    		
    		
    		//check for already tried bonds
    		if(checkedBonds.contains(currentBond) && currentBond != null)
    			continue;
    		else if(currentBond != null)
    			checkedBonds.add(currentBond);
    		
    		if(currentBond != null)
    		{
    			
	    		for (IAtom atom : currentBond.atoms()) {
	        		//check for already tried atoms
	    			if(checkedAtoms.contains(atom))
	        			continue;
	        		else
	        			checkedAtoms.add(atom);	    			
	    			
	    			if(firstBonds && atom.getSymbol().equals("H"))
	    				hydrogenStartAtom--;
	    			
	    			//thats the starting atom
	        		if(atom.getSymbol().equals(candidateAtom.getSymbol()))
	        		{
	        			boolean removed = atomsToFind.remove(candidateAtom.getSymbol());
	        			if(removed)
	        			{
	        				foundAtoms.add(atom);
	        				//remove bond
	        				if(!foundBonds.contains(currentBond))
		        				foundBonds.add(currentBond);
	        			}
	        			//this bond has to be removed 
	        			else if(!foundBonds.contains(currentBond) && atomsToFind.contains(atom.getSymbol()))
	        				foundBonds.add(currentBond);
	        			
	        			continue;
	        		}
	        		//found atom...remove it from the atoms to find list
	        		//do not remove atoms from ring
	        		if(atomsToFind.contains(atom.getSymbol()) && !allRings.contains(atom) && !atomsToFind.isEmpty())
	        		{
	        			boolean removed = atomsToFind.remove(atom.getSymbol());
	        			if(removed)
	        				foundAtoms.add(atom);
	        			else
	        				continue;
	        			
	        			if(!foundBonds.contains(currentBond))
	        				foundBonds.add(currentBond);
	        			
	        			continue;
	        		}
	        		
	        		//only walk along C-Atoms!
	    			if(!atom.getSymbol().equals("C"))
	    				continue;
	    			
	    			//get new bonds 
	    			List<IBond> bondsToAddTemp = fragCopy.getConnectedBondsList(atom);
	    			for (IBond bond : bondsToAddTemp) {
						if(bond != null)
							bondsFurther.add(bond);
					}
	        	}
    		}
    		
    		//break condition
    		if(currentBond == null && bondQueue.isEmpty() && bondsFurther.isEmpty())
    			break;
    		
    		//now check if the queue is empty...checked all bonds in this distance
    		if(bondQueue.isEmpty())
    		{
    			count--;
    			//set new queue data
    			for (IBond bond : bondsFurther) {
					if(bond != null)
						bondQueue.add(bond);
				}
    			//reinitialize
    			bondsFurther = new ArrayList<IBond>();
    			//the initially connected bonds are all checked!
    			firstBonds = false;
    		}	
    	}
    	
    	//found complete neutral loss
    	if(atomsToFind.isEmpty())
    	{
    		for (IAtom atom : foundAtoms) {
    			fragCopy.removeAtomAndConnectedElectronContainers(atom);
			}
    		//no need to remove bonds
//    		for (IBond bond : foundBonds) {
//    			fragCopy.removeBond(bond);
//			}
    		
    		
    		
    		//AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(fragCopy);
    		
    		//TODO add hydrogen somewhere
    		if(addHydrogen)
    		{
    			Map<Object, Object> props = fragCopy.getProperties();
    			props.put("hydrogenAddFromNL", "1");
    			fragCopy.setProperties(props);
    		}
    			
    		//add fragment to return if enough H were connected and fragment is still connected
    		if(hydrogenStartAtom <= 0)
    		{
    			ret.add(fragCopy);
//		    	Render.Draw(frag, "BEFORE");
//		    	Render.Draw(fragCopy, "AFTER");
//		    	IMoleculeSet set = ConnectivityChecker.partitionIntoMolecules(fragCopy);
//		    	for (IAtomContainer mol : set.molecules()) {
//					Render.Draw(mol, "partitions");
//				}
    		}
    	}
    	
    	return ret;
    }
    
    
    /**
     * Traverse like in a the real fragmenter. The main purpose is to create a copy of
     * of the old fragment
     * 
     * @param atomContainer the atom container
     * @param atom the atom
     * @param bondList the bond list
     * 
     * @return the list< i bond>
     */
    private List<IBond> traverse(IAtomContainer atomContainer, IAtom atom, List<IBond> bondList) {
        List<IBond> connectedBonds = atomContainer.getConnectedBondsList(atom);
        for (IBond aBond : connectedBonds) {
            if (bondList.contains(aBond))
                continue;
            bondList.add(aBond);
            IAtom nextAtom = aBond.getConnectedAtom(atom);
            if (atomContainer.getConnectedAtomsCount(nextAtom) == 1)
                continue;
            traverse(atomContainer, nextAtom, bondList);
        }
        return bondList;
    }
    
    private IAtomContainer makeAtomContainer(IAtom atom, List<IBond> parts) {
        
    	IChemObjectBuilder builder = NoNotificationChemObjectBuilder.getInstance();
        IAtomContainer partContainer = builder.newInstance(IAtomContainer.class);

        partContainer.addAtom(atom);
        for (IBond aBond : parts) {
            for (IAtom bondedAtom : aBond.atoms()) {
                if (!partContainer.contains(bondedAtom))
                    partContainer.addAtom(bondedAtom);
            }
            partContainer.addBond(aBond);
        }
        return partContainer;
    }
}
