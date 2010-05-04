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

import java.util.Vector;


/**
 * The Class BayesTraining.
 */
public class BayesTraining {

	private Vector<BayesTrainingRow> trainingTableMolecule;
	
	/**
	 * Instantiates a new bayes training data table.
	 */
	public BayesTraining()
	{
		trainingTableMolecule = new Vector<BayesTrainingRow>();
	}
	
	/**
	 * Adds the row.
	 * 
	 * @param compound the compound
	 * @param eV the e v
	 * @param mass the mass
	 * @param correct the correct
	 */
	public void addRow(String compound, int eV, double mass, boolean correct)
	{
		trainingTableMolecule.add(new BayesTrainingRow(compound, eV, mass, correct));
	}
	
	/**
	 * Gets the training data.
	 * 
	 * @param excludedMolecule the excluded molecule
	 * 
	 * @return the training data
	 */
	public Vector<BayesTrainingRow> getTrainingData(String excludedMolecule)
	{
		Vector<BayesTrainingRow> ret = new Vector<BayesTrainingRow>();
		for (BayesTrainingRow row : trainingTableMolecule) {
			if(!row.getCompund().equals(excludedMolecule))
				ret.add(row);
		}
		return ret;
	}
	
	/**
	 * Gets the complete data.
	 * 
	 * @return the complete data
	 */
	public Vector<BayesTrainingRow> getCompleteData()
	{
		return this.trainingTableMolecule;
	}
	
	/**
	 * Gets the data table string.
	 * 
	 * @return the data table string
	 */
	public String getDataTableString()
	{
		String dataTable = "";
		for (BayesTrainingRow row : trainingTableMolecule) {
			dataTable += row.getCompund() + "\t" + row.geteV() + "\t" + row.getMass() + "\t" + row.getCorrect() + "\n";
		}
		
		return dataTable;
	}
	
	

}
