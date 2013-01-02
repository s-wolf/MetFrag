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
