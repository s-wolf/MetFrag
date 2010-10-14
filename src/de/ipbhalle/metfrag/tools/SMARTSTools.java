package de.ipbhalle.metfrag.tools;

import java.util.ArrayList;
import java.util.List;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.smiles.smarts.SMARTSQueryTool;

import de.ipbhalle.metfrag.tools.renderer.StructureRenderer;

public class SMARTSTools {
	
	
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

}
