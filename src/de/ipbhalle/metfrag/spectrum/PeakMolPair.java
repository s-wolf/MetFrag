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
package de.ipbhalle.metfrag.spectrum;


import org.openscience.cdk.interfaces.IAtomContainer;

import de.ipbhalle.metfrag.massbankParser.Peak;

// TODO: Auto-generated Javadoc
/**
 * The Class PeakMolPair.
 */
public class PeakMolPair {
	
	private IAtomContainer ac;
	private Peak peak;
	private double matchedMass;
	private String molecularFormula;
	private int hydrogenPenalty = 0;
	private String partialChargeDiff;
	private String adduct;
	private double adductMass;
	
	
	/**
	 * Instantiates a new peak mol pair.
	 * 
	 * @param ac the ac
	 * @param peak the peak
	 */
	public PeakMolPair(IAtomContainer ac, Peak peak, double matchedMass, String molecularFormula, int score, String partialChargeDiff, String adduct, double adductMass)
	{
		this.ac = ac;
		this.peak = peak;
		this.matchedMass = matchedMass;
		this.setMolecularFormula(molecularFormula);
		this.hydrogenPenalty = score;
		this.setPartialChargeDiff(partialChargeDiff);
		this.adductMass = adductMass;
		this.adduct = adduct;
	}
	
	/**
	 * Gets the fragment.
	 * 
	 * @return the fragment
	 */
	public IAtomContainer getFragment()
	{
		return this.ac;
	}
	
	/**
	 * Gets the peak.
	 * 
	 * @return the peak
	 */
	public Peak getPeak()
	{
		return this.peak;
	}

	/**
	 * Sets the matched mass.
	 * 
	 * @param matchedMass the new matched mass
	 */
	public void setMatchedMass(double matchedMass) {
		this.matchedMass = matchedMass;
	}

	/**
	 * Gets the matched mass.
	 * 
	 * @return the matched mass
	 */
	public double getMatchedMass() {
		return matchedMass;
	}

	/**
	 * Sets the molecular formula.
	 * 
	 * @param sumFormula the new molecular formula
	 */
	public void setMolecularFormula(String sumFormula) {
		this.molecularFormula = sumFormula;
	}

	/**
	 * Gets the molecular formula.
	 * 
	 * @return the molecular formula
	 */
	public String getMolecularFormula() {
		return molecularFormula;
	}

	/**
	 * Sets the score.
	 * 
	 * @param score the new score
	 */
	public void setHydrogenPenalty(int penalty) {
		this.hydrogenPenalty = penalty;
	}

	/**
	 * Gets the score.
	 * 
	 * @return the score
	 */
	public int getHydrogenPenalty() {
		return hydrogenPenalty;
	}

	/**
	 * Sets the partial charge diff.
	 * 
	 * @param partialChargeDiff the new partial charge diff
	 */
	public void setPartialChargeDiff(String partialChargeDiff) {
		this.partialChargeDiff = partialChargeDiff;
	}

	/**
	 * Gets the partial charge diff.
	 * 
	 * @return the partial charge diff
	 */
	public String getPartialChargeDiff() {
		return partialChargeDiff;
	}

	/**
	 * Sets the adduct.
	 *
	 * @param adduct the new adduct
	 */
	public void setAdduct(String adduct) {
		this.adduct = adduct;
	}

	/**
	 * Gets the adduct.
	 *
	 * @return the adduct
	 */
	public String getAdduct() {
		return adduct;
	}

	/**
	 * Sets the adduct mass.
	 *
	 * @param adductMass the new adduct mass
	 */
	public void setAdductMass(double adductMass) {
		this.adductMass = adductMass;
	}

	/**
	 * Gets the adduct mass.
	 *
	 * @return the adduct mass
	 */
	public double getAdductMass() {
		return adductMass;
	}

}
