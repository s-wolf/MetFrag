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

package de.ipbhalle.metfrag.main;

import org.openscience.cdk.interfaces.IAtomContainer;

public class MetFragResult {
	
	private String candidateID = "";
	private IAtomContainer structure = null;
	private double score = 0.0;
	private int peaksExplained = 0;
	
	/**
	 * Instantiates a new MetFrag result.
	 * 
	 * @param candidateID the candidate id
	 * @param structure the structure
	 * @param score the score
	 * @param peaksExplained the peaks explained
	 */
	public MetFragResult( String candidateID, IAtomContainer structure, double score, int peaksExplained)
	{
		this.candidateID = candidateID;
		this.score = score;
		this.structure = structure;
		this.peaksExplained = peaksExplained;
	}

}
