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
