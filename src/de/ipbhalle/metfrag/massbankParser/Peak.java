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
