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
