package de.ipbhalle.metfrag.tools.renderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.templates.MoleculeFactory;

import de.ipbhalle.metfrag.spectrum.PeakMolPair;

public class StructureRendererTable {
	
	/**
	 * Draw Molecule and the fragments of it
	 * 
	 * @param original the original molecule
	 * @param List of Fragments
	 */
	public static void Draw(IAtomContainer original, List<IAtomContainer> l, String name) {
		
		List<IAtomContainer> containers = new ArrayList<IAtomContainer>();
		containers.add(original);
		containers.addAll(l);
        
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
	public static void DrawHits(IAtomContainer original, Vector<PeakMolPair> l, String name) {
		
		List<IAtomContainer> containers = new ArrayList<IAtomContainer>();
		containers.add(original);
		
		//add fragments to list
		for (PeakMolPair frag : l) {
			containers.add(frag.getFragment());
		}
		
        
        MoleculeTableJFrame example = new MoleculeTableJFrame(containers);
        
        example.pack();
        example.setVisible(true);
	}


}
