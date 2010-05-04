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

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

public class ListManipulator {
	
	/**
	 * Removes the hydrogens from all atom containers in the list.
	 * 
	 * @param mols the mols
	 * 
	 * @return the list< i atom container>
	 */
	public static List<IAtomContainer> removeHydrogensInList(List<IAtomContainer> mols)
	{
		List<IAtomContainer> ret = new ArrayList<IAtomContainer>();
		for (int i = 0; i < mols.size(); i++) {
			ret.add(AtomContainerManipulator.removeHydrogens(mols.get(i)));
		}
		return ret;
	}

}
