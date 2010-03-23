package de.ipbhalle.metfrag.tools;

import java.util.ArrayList;
import java.util.List;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;

import de.ipbhalle.metfrag.bondPrediction.AtomProperty;

public class MoleculeTools {
	
	/**
	 * Sets a unique identifier for every bond and atom
	 * in the molecule.
	 * 
	 * @param mol the mol
	 * 
	 * @return the i atom container
	 */
	public static IAtomContainer moleculeNumbering(IAtomContainer mol)
	{
		Integer count = 0;
		Integer countBond = 0;
		List<IAtom> alreadyDone = new ArrayList<IAtom>();

		for (IBond bond : mol.bonds()) {
			
			bond.setID(countBond.toString());
			countBond++;
			
        	for (IAtom atom : bond.atoms()) {
        		if(!alreadyDone.contains(atom))
        		{
        			atom.setID(count.toString());
            		count++;
            		alreadyDone.add(atom);        			
        		}
			}	        	
		}
		
		return mol;
	}

}
