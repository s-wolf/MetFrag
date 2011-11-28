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

import de.ipbhalle.metfrag.massbankParser.Peak;
import de.ipbhalle.metfrag.tools.Constants;
import de.ipbhalle.metfrag.tools.MoleculeTools;

public class MatchedFragment {
	
	private Peak peak;
	private double fragmentMass;
	private double matchedMass;
	private IAtomContainer fragmentStructure;
	private NeutralLoss[] neutralLosses;
	private IMolecularFormula molecularFormula;
	private String molecularFormulaString;
	private int hydrogenPenalty;
	private List<List<Integer>> matchedAtoms;
	private Double bondLengthChange;
	private Double bondOrder;
	private Double bde;
	
	/**
	 * Instantiates a new matched fragment.
	 *
	 * @param peak the peak
	 * @param fragmentMass the fragment mass
	 * @param matchedMass the matched mass
	 * @param fragmentStructure the fragment structure
	 * @param neutralLosses the neutral loss
	 * @param hydrogenPenalty the hydrogen penalty
	 * @param bondLengthChange the partial charge diff
	 * @param molecularFormulaString the molecular formula string
	 */
	public MatchedFragment(Peak peak, double fragmentMass, double matchedMass, IAtomContainer fragmentStructure, NeutralLoss[] neutralLosses, int hydrogenPenalty, String molecularFormulaString)
	{
		setFragmentMass(fragmentMass);
		setMatchedMass(matchedMass);
		setPeakMass(peak);
		setFragmentStructure(fragmentStructure);
		setNeutralLosses(neutralLosses);
		setHydrogenPenalty(hydrogenPenalty);
		setMolecularFormulaString(molecularFormulaString);
	}
	
	/**
	 * Instantiates a new matched fragment with matched atoms from the neutral loss check.
	 *
	 * @param peak the peak
	 * @param fragmentMass the fragment mass
	 * @param matchedMass the matched mass
	 * @param fragmentStructure the fragment structure
	 * @param neutralLosses the neutral losses
	 * @param hydrogenPenalty the hydrogen penalty
	 * @param molecularFormulaString the molecular formula string
	 * @param matchedAtoms the matched atoms
	 */
	public MatchedFragment(Peak peak, double fragmentMass, double matchedMass, IAtomContainer fragmentStructure, NeutralLoss[] neutralLosses, int hydrogenPenalty, String molecularFormulaString, List<List<Integer>> matchedAtoms)
	{
		setFragmentMass(fragmentMass);
		setMatchedMass(matchedMass);
		setPeakMass(peak);
		setFragmentStructure(fragmentStructure);
		setNeutralLosses(neutralLosses);
		setHydrogenPenalty(hydrogenPenalty);
		setMolecularFormulaString(molecularFormulaString);
		setMatchedAtoms(matchedAtoms);
	}

	public void setNeutralLosses(NeutralLoss[] neutralLoss) {
		this.neutralLosses = neutralLoss;
	}

	/**
	 * Gets the neutral loss.
	 *
	 * @return the neutral loss
	 */
	public NeutralLoss[] getNeutralLosses() {
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

	public void setMatchedAtoms(List<List<Integer>> matchedAtoms) {
		this.matchedAtoms = matchedAtoms;
	}

	/**
	 * Gets the matched atoms from the neutral loss check.
	 * Null if the neutral loss did not match!
	 *
	 * @return the matched atoms
	 */
	public List<List<Integer>> getMatchedAtoms() {
		return matchedAtoms;
	}

	public void setBondOrder(double bondOrder) {
		this.bondOrder = bondOrder;
	}

	/**
	 * Gets the bond order.
	 *
	 * @return the bond order
	 */
	public double getBondOrder() {
		if(bondOrder == null)
//			return MoleculeTools.getCombinedEnergy((String)this.fragmentStructure.getProperty(Constants.BONDORDER));
			return MoleculeTools.getCombinedEnergyLeveled((String)this.fragmentStructure.getProperty(Constants.BONDORDER));
		else
			return bondOrder;
	}

	public void setBondLengthChange(double bondLengthChange) {
		this.bondLengthChange = bondLengthChange;
	}

	/**
	 * Gets the bond length change.
	 *
	 * @return the bond length change
	 */
	public Double getBondLengthChange() {
		if(bondLengthChange == null)
//			return MoleculeTools.getCombinedEnergy((String)this.fragmentStructure.getProperty(Constants.BONDLENGTHCHANGE));
			return MoleculeTools.getCombinedEnergyLeveled((String)this.fragmentStructure.getProperty(Constants.BONDLENGTHCHANGE));
		else
			return bondLengthChange;
	}

	public void setBde(Double bde) {
		this.bde = bde;
	}

	public Double getBde() {
//		return MoleculeTools.getCombinedEnergy((String)this.fragmentStructure.getProperty(Constants.BDE));
		return MoleculeTools.getCombinedEnergyLeveled((String)this.fragmentStructure.getProperty(Constants.BDE));
	}

}
