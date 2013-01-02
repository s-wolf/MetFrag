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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.smiles.smarts.SMARTSQueryTool;

import de.ipbhalle.metfrag.tools.renderer.StructureRenderer;

public class SMARTSTools {
	
	private List<List<Integer>> matchedAtoms;
	private IAtomContainer target;
	private boolean matched;
	SMARTSQueryTool querytool;
	
	/**
	 * Instantiates a new SMARTS tools which checks the target atomcontainer
	 * for substructures using the SMARTS string.
	 *
	 * @param target the target
	 * @throws CDKException the cDK exception
	 */
	public SMARTSTools(IAtomContainer target) throws CDKException
	{
		this.querytool = null;	
		this.target = target;
	}
	
	
	/**
	 * Match smarts with the given target atomcontainer.
	 *
	 * @param SMARTS the SMARTS String
	 * @throws CDKException the cDK exception
	 */
	public void matchSMARTS(String SMARTS) throws CDKException
	{
		if(querytool == null)
			querytool = new SMARTSQueryTool(SMARTS);
		else
			this.querytool.setSmarts(SMARTS);
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
