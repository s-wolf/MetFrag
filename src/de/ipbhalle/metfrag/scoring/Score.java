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
