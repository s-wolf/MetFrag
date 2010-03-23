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
