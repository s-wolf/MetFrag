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

import java.util.ArrayList;
import java.util.List;

import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

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
		IAtom[] atomList = AtomContainerManipulator.getAtomArray(mol);
		for (int i = 0; i < atomList.length; i++) {
			atomList[i].setID((i) + "");
		}	
		
		IBond[] bondList = AtomContainerManipulator.getBondArray(mol);
		for (int i = 0; i < bondList.length; i++) {
			bondList[i].setID((i) + "");
		}
		
		return mol;
	}

}
