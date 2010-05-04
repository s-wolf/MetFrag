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
package de.ipbhalle.metfrag.massbankParser;


public class Peak implements Comparable<Peak>, java.io.Serializable {

	private double mass;
	private double intensity;
	private double relIntensityDB;
	private double relIntensity;
	private int lowestEnergy;
	private int highestEnergy;


	/**
	 * Instantiates a new peak.
	 * 
	 * @param mass the mass
	 * @param intensity the intensity
	 * @param relIntensityDB the rel intensity db
	 * @param energy the energy
	 */
	public Peak(double mass, double intensity, double relIntensityDB, int energy){
		this.mass = mass;
		this.intensity = intensity;
		this.relIntensityDB = relIntensityDB;
		relIntensity = 0.0;
		lowestEnergy = energy;
		highestEnergy = energy;
	}
	
	/**
	 * Instantiates a new peak.
	 * 
	 * @param mass the mass
	 * @param intensity the intensity
	 * @param energy the energy
	 */
	public Peak(double mass, double intensity, int energy){
		this.mass = mass;
		this.intensity = intensity;
		this.relIntensityDB = 0.0;
		relIntensity = 0.0;		
		lowestEnergy = energy;
		highestEnergy = energy;
	}

	public double getMass(){
		return mass;
	}
	
	public double getIntensity(){
		return intensity;
	}
	public double getRelIntensityDB(){
		return relIntensityDB;
	}
	
	public double getRelIntensity(){
		if (relIntensity == 0.0) return relIntensityDB;
		return relIntensity;
	}
		
	public void setMass(double mass){
		this.mass = mass;	
	}
	
	public void setIntensity(double intensity){
		this.intensity = intensity;
	}
	
	public void setRelIntensityDB(double relIntensityDB){
		this.relIntensityDB = relIntensityDB;
	}

	public void setRelIntensity(double relIntensity){
		this.relIntensity = relIntensity;
	}
	
	public int getHighestEnergy() {
		return highestEnergy;
	}

	public void setHighestEnergy(int highestEnergy) {
		this.highestEnergy = highestEnergy;
	}

	public int getLowestEnergy() {
		return lowestEnergy;
	}

	public void setLowestEnergy(int lowestEnergy) {
		this.lowestEnergy = lowestEnergy;
	}

	/**
	 * Sets the energy.
	 * 
	 * @param energy the new energy
	 */
	public void setEnergy(int energy){
		this.lowestEnergy = energy;
		this.highestEnergy = energy;		
	}
	
	public boolean isEnergyAdjacent(Peak another){
		return (highestEnergy+1 >= another.lowestEnergy && lowestEnergy-1 <= another.highestEnergy);
	}

	public int compareTo(Peak p){
		if (this.mass < p.mass) return -1;
		else if (this.mass == p.mass) return 0;
		return 1;
	}
	
	/**
	 * Gets the nominal mass.
	 * 
	 * @return the nominal mass
	 */
	public int getNominalMass(){
		return (int)Math.round(this.mass);
	}
}
