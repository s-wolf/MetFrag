package de.ipbhalle.metfrag.scoring;

/**
 * The Class Score.
 */
public class Score {
	
	/** The weighted peak. */
	private double weightedPeak = 0.0;
	
	/** The bond energy. */
	private double bondEnergy = 0.0;
	
	/**
	 * Instantiates a new score.
	 * 
	 * @param weightedPeak the weighted peak
	 * @param bondEnergy the bond energy
	 */
	public Score(double weightedPeak, double bondEnergy)
	{
		this.weightedPeak = weightedPeak;
		this.bondEnergy = bondEnergy;
	}

	/**
	 * Sets the bond energy.
	 * 
	 * @param bondEnergy the new bond energy
	 */
	public void setBondEnergy(double bondEnergy) {
		this.bondEnergy = bondEnergy;
	}

	/**
	 * Gets the bond energy.
	 * 
	 * @return the bond energy
	 */
	public double getBondEnergy() {
		return bondEnergy;
	}

	/**
	 * Sets the weighted peak.
	 * 
	 * @param weightedPeak the new weighted peak
	 */
	public void setWeightedPeak(double weightedPeak) {
		this.weightedPeak = weightedPeak;
	}

	/**
	 * Gets the weighted peak.
	 * 
	 * @return the weighted peak
	 */
	public double getWeightedPeak() {
		return weightedPeak;
	}
	
	

}
