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

import java.util.List;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecularFormula;

import de.ipbhalle.metfrag.fragmenter.NeutralLoss;
import de.ipbhalle.metfrag.massbankParser.Peak;

public class MatchedFragment {
	
	private Peak peak;
	private double fragmentMass;
	private double matchedMass;
	private IAtomContainer fragmentStructure;
	private List<NeutralLoss> neutralLosses;
	private IMolecularFormula molecularFormula;
	private String molecularFormulaString;
	private int hydrogenPenalty;
	private String partialChargeDiff;
	
	/**
	 * Instantiates a new matched fragment.
	 *
	 * @param peakMass the peak mass
	 * @param fragmentMass the fragment mass
	 * @param fragmentStructure the fragment structure
	 * @param neutralLosses the neutral loss
	 * @param hydrogenPenalty the hydrogen penalty
	 * @param partialChargeDiff the partial charge diff
	 */
	public MatchedFragment(Peak peak, double fragmentMass, double matchedMass, IAtomContainer fragmentStructure, List<NeutralLoss> neutralLosses, int hydrogenPenalty, String partialChargeDiff, String molecularFormulaString)
	{
		setFragmentMass(fragmentMass);
		setPeakMass(peak);
		setFragmentStructure(fragmentStructure);
		setNeutralLosses(neutralLosses);
		setHydrogenPenalty(hydrogenPenalty);
		setPartialChargeDiff(partialChargeDiff);
		setMolecularFormulaString(molecularFormulaString);
	}

	public void setNeutralLosses(List<NeutralLoss> neutralLoss) {
		this.neutralLosses = neutralLoss;
	}

	/**
	 * Gets the neutral loss.
	 *
	 * @return the neutral loss
	 */
	public List<NeutralLoss> getNeutralLosses() {
		return neutralLosses;
	}

	public void setFragmentStructure(IAtomContainer fragmentStructure) {
		this.fragmentStructure = fragmentStructure;
	}

	/**
	 * Gets the fragment structure.
	 *
	 * @return the fragment structure
	 */
	public IAtomContainer getFragmentStructure() {
		return fragmentStructure;
	}

	public void setFragmentMass(double fragmentMass) {
		this.fragmentMass = fragmentMass;
	}

	/**
	 * Gets the fragment mass.
	 *
	 * @return the fragment mass
	 */
	public double getFragmentMass() {
		return fragmentMass;
	}

	public void setPeakMass(Peak peak) {
		this.peak = peak;
	}

	/**
	 * Gets the peak.
	 *
	 * @return the peak
	 */
	public Peak getPeak() {
		return peak;
	}

	public void setHydrogenPenalty(int hydrogenPenalty) {
		this.hydrogenPenalty = hydrogenPenalty;
	}

	/**
	 * Gets the hydrogen penalty.
	 *
	 * @return the hydrogen penalty
	 */
	public int getHydrogenPenalty() {
		return hydrogenPenalty;
	}

	public void setMolecularFormula(IMolecularFormula molecularFormula) {
		this.molecularFormula = molecularFormula;
	}

	/**
	 * Gets the molecular formula.
	 *
	 * @return the molecular formula
	 */
	public IMolecularFormula getMolecularFormula() {
		return molecularFormula;
	}

	public void setPartialChargeDiff(String partialChargeDiff) {
		this.partialChargeDiff = partialChargeDiff;
	}

	/**
	 * Gets the partial charge difference.
	 *
	 * @return the partial charge difference
	 */
	public String getPartialChargeDiff() {
		return partialChargeDiff;
	}

	public void setMolecularFormulaString(String molecularFormulaString) {
		this.molecularFormulaString = molecularFormulaString;
	}

	/**
	 * Gets the molecular formula as string.
	 *
	 * @return the molecular formula as string
	 */
	public String getMolecularFormulaString() {
		return molecularFormulaString;
	}

	public void setMatchedMass(double matchedMass) {
		this.matchedMass = matchedMass;
	}

	/**
	 * Gets the matched mass. This is the mass the fragment had after all modifications (mode, neutral losses, hydrogen penalty)
	 *
	 * @return the matched mass
	 */
	public double getMatchedMass() {
		return matchedMass;
	}

}
