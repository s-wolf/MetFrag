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
package de.ipbhalle.metfrag.scoring;

public class OptimizationMatrixEntry {
	
	private String candidateID;
	private Double peakMass;
	private Double peakInt;
	private String bondEnergyString;
	private int hydrogenPenalty;
	private String chargesDiffString;
	private String neutralLossRules;
	
	/**
	 * Instantiates a new optimization matrix entry.
	 * 
	 * @param candidateID the candidate id
	 * @param peakMass the peak mass
	 * @param peakInt the peak int
	 * @param weightedPeak the weighted peak
	 * @param bondEnergyString the bond energy string
	 * @param hydrogenPenalty the hydrogen penalty
	 * @param chargesDiffString the charges diff string
	 */
	public OptimizationMatrixEntry(String candidateID, Double peakMass, Double peakInt, String bondEnergyString, int hydrogenPenalty, String chargesDiffString, String neutralLossRules)
	{
		setBondEnergyString(bondEnergyString);
		setCandidateID(candidateID);
		setChargesDiffString(chargesDiffString);
		setHydrogenPenalty(hydrogenPenalty);
		setPeakInt(peakInt);
		setPeakMass(peakMass);
		setNeutralLossRules(neutralLossRules);
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
	public void setChargesDiffString(String chargesDiffString) {
		this.chargesDiffString = chargesDiffString;
	}

	/**
	 * Gets the charges diff string.
	 * 
	 * @return the charges diff string
	 */
	public String getChargesDiffString() {
		return chargesDiffString;
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

}
