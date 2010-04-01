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
	private double hydrogenPenalty = 0.0;
	private double partialChargeDiff = 0.0;
	
	
	/**
	 * Instantiates a new peak mol pair.
	 * 
	 * @param ac the ac
	 * @param peak the peak
	 */
	public PeakMolPair(IAtomContainer ac, Peak peak, double matchedMass, String molecularFormula, double score, double partialChargeDiff)
	{
		this.ac = ac;
		this.peak = peak;
		this.matchedMass = matchedMass;
		this.setMolecularFormula(molecularFormula);
		this.hydrogenPenalty = score;
		this.setPartialChargeDiff(partialChargeDiff);
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
	public void setHydrogenPenalty(double penalty) {
		this.hydrogenPenalty = penalty;
	}

	/**
	 * Gets the score.
	 * 
	 * @return the score
	 */
	public double getHydrogenPenalty() {
		return hydrogenPenalty;
	}

	/**
	 * Sets the partial charge diff.
	 * 
	 * @param partialChargeDiff the new partial charge diff
	 */
	public void setPartialChargeDiff(double partialChargeDiff) {
		this.partialChargeDiff = partialChargeDiff;
	}

	/**
	 * Gets the partial charge diff.
	 * 
	 * @return the partial charge diff
	 */
	public double getPartialChargeDiff() {
		return partialChargeDiff;
	}

}
