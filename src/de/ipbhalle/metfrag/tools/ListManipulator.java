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
