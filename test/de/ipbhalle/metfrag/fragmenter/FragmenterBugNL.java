package de.ipbhalle.metfrag.fragmenter;

import java.io.FileNotFoundException;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.openscience.cdk.exception.CDKException;

import de.ipbhalle.metfrag.main.FragmentSingleCompound;

public class FragmenterBugNL {
	
	
	/**
	 * Test fragmenter with Sarcosin.
	 * There should be one water loss without breaking the structure
	 * 
	 * @throws CDKException 
	 * @throws FileNotFoundException 
	 */
	@Test
	public void testFragmenterNL() {
		//example values
		String smiles = "CNCC(=O)O";
		Double minMass = 40.0;
		Boolean render = true;
				
		FragmentSingleCompound test = new FragmentSingleCompound();
		//those are the default values...no need to set them like this
		test.setSumFormulaRedundancyCheck(true);
		test.setTreeDepth(2);
		
		List<String> resultingFragments = null;
		try {
			resultingFragments = test.getFragments(smiles, minMass, render);
			List<String> resultingEnergies = test.getEnergies();
			System.out.println("Fragment count: " + resultingFragments.size());
			for (int i = 0; i < resultingFragments.size(); i++) {
				System.out.print(resultingFragments.get(i));
				System.out.print(" " + resultingEnergies.get(i) + "\n");
			}
		} catch (Exception e) {
			System.out.println("Error! TODO...");
			e.printStackTrace();
		}
		
		Assert.assertEquals(11, resultingFragments.size());
		
	}

}
