package de.ipbhalle.metfrag.similarity;

import java.util.ArrayList;
import java.util.List;

public class SimilarityGroup {
	
	private List<String> similarCandidates = new ArrayList<String>();
	private List<String> similarCandidatesWithBase = new ArrayList<String>();
	private List<Float> tanimotoCandidates = new ArrayList<Float>();
	private String candidateTocompare;
	
	public SimilarityGroup(String candidateToCompare)
	{
		this.candidateTocompare = candidateToCompare;
		this.similarCandidatesWithBase.add(candidateToCompare);
	}
	
	/**
	 * Adds the similar compound.
	 * 
	 * @param candidate the candidate
	 */
	public void addSimilarCompound(String candidate, float tanimotoDist)
	{
		similarCandidates.add(candidate);
		similarCandidatesWithBase.add(candidate);
		tanimotoCandidates.add(tanimotoDist);
	}
	
	/**
	 * Gets the similar compounds.
	 * 
	 * @return the similar compounds
	 */
	public List<String> getSimilarCompounds()
	{
		return similarCandidates;
	}
	
	
	
	/**
	 * Gets the similar compounds' tanimoto distances
	 * 
	 * @return the similar compounds
	 */
	public List<Float> getSimilarCompoundsTanimoto()
	{
		return tanimotoCandidates;
	}

	public String getCandidateTocompare() {
		return candidateTocompare;
	}


	/**
	 * Gets the similar candidates with base.
	 * 
	 * @return the similar candidates with base
	 */
	public List<String> getSimilarCandidatesWithBase() {
		return similarCandidatesWithBase;
	}
	

}
