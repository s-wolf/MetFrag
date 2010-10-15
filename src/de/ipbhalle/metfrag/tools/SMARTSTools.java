package de.ipbhalle.metfrag.tools;

import java.util.ArrayList;
import java.util.List;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.smiles.smarts.SMARTSQueryTool;

import de.ipbhalle.metfrag.tools.renderer.StructureRenderer;

public class SMARTSTools {
	
	private List<List<Integer>> matchedAtoms;
	private boolean matched;
	
	/**
	 * Instantiates a new SMARTS tools which checks the target atomcontainer 
	 * for substructures using the SMARTS string.
	 *
	 * @param SMARTS the sMARTS
	 * @param target the target
	 * @throws CDKException the cDK exception
	 */
	public SMARTSTools(String SMARTS, IAtomContainer target) throws CDKException
	{
		SMARTSQueryTool querytool = new SMARTSQueryTool(SMARTS);
		matched = querytool.matches(target);
		if(matched)
			matchedAtoms = querytool.getMatchingAtoms();
	}
	
	
	/**
	 * Check for SMARTS pattern in the given target molecule.
	 *
	 * @param SMARTS the sMARTS
	 * @param target the target
	 * @return true, if successful
	 * @throws CDKException the cDK exception
	 */
	public static boolean checkForSMARTS(String SMARTS, IAtomContainer target) throws CDKException
	{
		SMARTSQueryTool querytool = new SMARTSQueryTool(SMARTS);
        
		boolean status = querytool.matches(target);
		return status;
	}

	public void setMatchedAtoms(List<List<Integer>> matchedAtoms) {
		this.matchedAtoms = matchedAtoms;
	}


	/**
	 * Gets the matched atoms.
	 *
	 * @return the matched atoms
	 */
	public List<List<Integer>> getMatchedAtoms() {
		return matchedAtoms;
	}


	public void setStatus(boolean matched) {
		this.matched = matched;
	}


	public boolean isMatched() {
		return matched;
	}

}
