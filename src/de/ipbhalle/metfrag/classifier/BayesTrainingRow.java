package de.ipbhalle.metfrag.classifier;

public class BayesTrainingRow {
	
	private String compound;
	private int eV;
	private double mass;
	private boolean correct;
	
	/**
	 * Instantiates a new bayes training row.
	 * 
	 * @param the collision energy in eV
	 * @param mass the m/z
	 * @param correct the correct
	 * @param compound the compound
	 */
	public BayesTrainingRow(String compound, int eV, double mass, boolean correct)
	{
		this.compound = compound;
		this.eV = eV;
		this.mass = mass;
		this.correct = correct;
	}
	
	/**
	 * Gets the compound identifier.
	 * 
	 * @return the peak number
	 */
	public String getCompund()
	{
		return this.compound;
	}
	
	/**
	 * Gets the collision energy in eV.
	 * 
	 * @return the eV
	 */
	public int geteV()
	{
		return this.eV;
	}
	
	/**
	 * Gets the mass.
	 * 
	 * @return the mass
	 */
	public double getMass()
	{
		return this.mass;
	}
	
	/**
	 * This peaks explains the correct molecule?
	 * 
	 * @return the correct
	 */
	public boolean getCorrect()
	{
		return this.correct;
	}

}
