package de.ipbhalle.metfrag.similarity;

public class SimilarityCompound {
	
	private boolean isAlreadyInCluster;
	private String compoundID;
	
	/**
	 * Instantiates a new similarity compound.
	 * 
	 * @param compoundID the compound id
	 */
	public SimilarityCompound(String compoundID)
	{
		this.setCompoundID(compoundID);
	}

	/**
	 * Sets the already in cluster.
	 * 
	 * @param isAlreadyInCluster the new already in cluster
	 */
	public void setAlreadyInCluster(boolean isAlreadyInCluster) {
		this.isAlreadyInCluster = isAlreadyInCluster;
	}

	/**
	 * Checks if is already in cluster.
	 * 
	 * @return true, if is already in cluster
	 */
	public boolean isAlreadyInCluster() {
		return isAlreadyInCluster;
	}

	/**
	 * Sets the compound id.
	 * 
	 * @param compoundID the new compound id
	 */
	public void setCompoundID(String compoundID) {
		this.compoundID = compoundID;
	}

	/**
	 * Gets the compound id.
	 * 
	 * @return the compound id
	 */
	public String getCompoundID() {
		return compoundID;
	}

}
