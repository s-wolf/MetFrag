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
package de.ipbhalle.metfrag.scoring;

public class OptimizationMatrixEntry {
	
	private String candidateID;
	private Double peakMass;
	private Double peakInt;
	private String bondEnergyString;
	private int hydrogenPenalty;
	private String bondLengthChange;
	private String neutralLossRules;
	private String bondOrderString;
	private String bondOrderDiffString;
	private String bondRemoved;
	
	/**
	 * Instantiates a new optimization matrix entry.
	 *
	 * @param candidateID the candidate id
	 * @param peakMass the peak mass
	 * @param peakInt the peak int
	 * @param bondEnergyString the bond energy string
	 * @param hydrogenPenalty the hydrogen penalty
	 * @param neutralLossRules the neutral loss rules
	 * @param bondLengthChange the bond length change
	 * @param bondOrder the bond order
	 * @param bondRemoved the bond removed
	 */
	public OptimizationMatrixEntry(String candidateID, Double peakMass, Double peakInt, String bondEnergyString, int hydrogenPenalty, String neutralLossRules, String bondLengthChange, String bondOrder, String bondOrderDiff, String bondRemoved)
	{
		setBondEnergyString(bondEnergyString);
		setCandidateID(candidateID);
		setbondLengthChange(bondLengthChange);
		setHydrogenPenalty(hydrogenPenalty);
		setPeakInt(peakInt);
		setPeakMass(peakMass);
		setNeutralLossRules(neutralLossRules);
		setBondOrderString(bondOrder);
		setBondRemoved(bondRemoved);
		setBondOrderDiffString(bondOrderDiff);
	}

	/**
	 * Sets the candidate id.
	 * 
	 * @param candidateID the new candidate id
	 */
	public void setCandidateID(String candidateID) {
		this.candidateID = candidateID;
	}

	/**
	 * Gets the candidate id.
	 * 
	 * @return the candidate id
	 */
	public String getCandidateID() {
		return candidateID;
	}

	/**
	 * Sets the peak mass.
	 * 
	 * @param peakMass the new peak mass
	 */
	public void setPeakMass(Double peakMass) {
		this.peakMass = peakMass;
	}

	/**
	 * Gets the peak mass.
	 * 
	 * @return the peak mass
	 */
	public Double getPeakMass() {
		return peakMass;
	}

	/**
	 * Sets the peak int.
	 * 
	 * @param peakInt the new peak int
	 */
	public void setPeakInt(Double peakInt) {
		this.peakInt = peakInt;
	}

	/**
	 * Gets the peak int.
	 * 
	 * @return the peak int
	 */
	public Double getPeakInt() {
		return peakInt;
	}

	/**
	 * Sets the bond energy string.
	 * 
	 * @param bondEnergyString the new bond energy string
	 */
	public void setBondEnergyString(String bondEnergyString) {
		this.bondEnergyString = bondEnergyString;
	}

	/**
	 * Gets the bond energy string.
	 * 
	 * @return the bond energy string
	 */
	public String getBondEnergyString() {
		return bondEnergyString;
	}

	/**
	 * Sets the hydrogen penalty.
	 * 
	 * @param hydrogenPenalty the new hydrogen penalty
	 */
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

	/**
	 * Sets the charges diff string.
	 * 
	 * @param chargesDiffString the new charges diff string
	 */
	public void setbondLengthChange(String bondLengthChange) {
		this.bondLengthChange = bondLengthChange;
	}

	/**
	 * Gets the charges diff string.
	 * 
	 * @return the charges diff string
	 */
	public String getBondLengthChange() {
		return bondLengthChange;
	}

	/**
	 * Sets the neutral loss rules.
	 *
	 * @param neutralLossRules the new neutral loss rules
	 */
	public void setNeutralLossRules(String neutralLossRules) {
		this.neutralLossRules = neutralLossRules;
	}

	/**
	 * Gets the neutral loss rules.
	 *
	 * @return the neutral loss rules
	 */
	public String getNeutralLossRules() {
		return neutralLossRules;
	}

	/**
	 * Sets the bond order string.
	 *
	 * @param bondOrderString the new bond order string
	 */
	public void setBondOrderString(String bondOrderString) {
		this.bondOrderString = bondOrderString;
	}

	/**
	 * Gets the bond order string.
	 *
	 * @return the bond order string
	 */
	public String getBondOrderString() {
		return bondOrderString;
	}

	/**
	 * Sets the bond length change.
	 *
	 * @param bondLengthChange the new bond length change
	 */
	public void setBondLengthChange(String bondLengthChange) {
		this.bondLengthChange = bondLengthChange;
	}

	/**
	 * Sets the bond removed.
	 *
	 * @param bondRemoved the new bond removed
	 */
	public void setBondRemoved(String bondRemoved) {
		this.bondRemoved = bondRemoved;
	}

	/**
	 * Gets the bond removed.
	 *
	 * @return the bond removed
	 */
	public String getBondRemoved() {
		return bondRemoved;
	}

	/**
	 * Gets the bond order diff string.
	 *
	 * @return the bond order diff string
	 */
	public String getBondOrderDiffString() {
		return bondOrderDiffString;
	}

	/**
	 * Sets the bond order diff string.
	 *
	 * @param bondOrderDiffString the new bond order diff string
	 */
	public void setBondOrderDiffString(String bondOrderDiffString) {
		this.bondOrderDiffString = bondOrderDiffString;
	}

}
