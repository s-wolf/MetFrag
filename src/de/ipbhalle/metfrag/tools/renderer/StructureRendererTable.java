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
package de.ipbhalle.metfrag.tools.renderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.templates.MoleculeFactory;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

import de.ipbhalle.metfrag.massbankParser.Peak;
import de.ipbhalle.metfrag.spectrum.MatchedFragment;

public class StructureRendererTable {
	
	/**
	 * Draw Molecule and the fragments of it
	 * 
	 * @param original the original molecule
	 * @param List of Fragments
	 */
	public static void Draw(IAtomContainer original, List<IAtomContainer> l, String name) {
		
		List<IAtomContainer> containers = new ArrayList<IAtomContainer>();
		containers.addAll(l);
		containers.add(0,original);
        
        MoleculeTableJFrame example = new MoleculeTableJFrame(containers);
        
        example.pack();
        example.setVisible(true);
	}
	
	
	/**
	 * Draw Molecule and the explained fragments of it
	 * 
	 * @param original the original molecule
	 * @param List of Fragments
	 */
	public static void DrawHits(IAtomContainer original, Vector<MatchedFragment> l, String name) {
		
		List<MatchedFragment> containers = new ArrayList<MatchedFragment>();
		containers.add(new MatchedFragment(new Peak(0.0, 0.0, 0), 0.0, 0.0, original, null, 0, ""));
		boolean showHydrogen = true;
		//add fragments to list
		if(!showHydrogen)
		{
			for (MatchedFragment frag : l) {
				frag.setFragmentStructure(AtomContainerManipulator.removeHydrogens(frag.getFragmentStructure()));
				containers.add(frag);
			}
		}
		else
		{
			for (MatchedFragment frag : l) {
				containers.add(frag);
			}
		}
		
		
        
        MoleculeTableJFrameHits example = new MoleculeTableJFrameHits(containers);
        
        example.pack();
        example.setVisible(true);
	}


}
