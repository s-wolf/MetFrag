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
