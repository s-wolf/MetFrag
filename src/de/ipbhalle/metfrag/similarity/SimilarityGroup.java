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
package de.ipbhalle.metfrag.similarity;

import java.util.ArrayList;
import java.util.List;


public class SimilarityGroup {
	
	private List<SimilarityCompound> similarCandidates = new ArrayList<SimilarityCompound>();
	private List<SimilarityCompound> similarCandidatesWithBase = new ArrayList<SimilarityCompound>();
	private List<Float> tanimotoSimilarities = new ArrayList<Float>();
	
	private String candidateTocompare;
	
	public SimilarityGroup(String candidateToCompare)
	{
		this.candidateTocompare = candidateToCompare;
		this.similarCandidatesWithBase.add(new SimilarityCompound(candidateToCompare));
	}
	
	
	/**
	 * Adds the similar compound.
	 * 
	 * @param candidate the candidate
	 */
	public void addSimilarCompound(String candidate, float tanimotoDist)
	{
		similarCandidates.add(new SimilarityCompound(candidate));
		similarCandidatesWithBase.add(new SimilarityCompound(candidate));
		tanimotoSimilarities.add(tanimotoDist);
	}
	
	/**
	 * Adds the similar compound.
	 * 
	 * @param candidate the candidate
	 */
	public void addSimilarCompound(String candidate)
	{
		similarCandidates.add(new SimilarityCompound(candidate));
		similarCandidatesWithBase.add(new SimilarityCompound(candidate));
	}
	
	/**
	 * Gets the similar compounds.
	 * 
	 * @return the similar compounds
	 */
	public List<SimilarityCompound> getSimilarCompounds()
	{
		return similarCandidates;
	}
	
	/**
	 * Gets the similar compounds as an array of compound id's.
	 * 
	 * @return the similar compounds
	 */
	public List<String> getSimilarCompoundsAsArray()
	{
		List<String> similarArray = new ArrayList<String>();
		for (SimilarityCompound simCpd : this.similarCandidates) {
			similarArray.add(simCpd.getCompoundID());
		}
		return similarArray;
	}
	
	/**
	 * Gets the similar compounds with base compound as an array of compound id's.
	 * 
	 * @return the similar compounds
	 */
	public List<String> getSimilarCompoundsWithBaseAsArray()
	{
		List<String> similarArray = new ArrayList<String>();
		for (SimilarityCompound simCpd : this.similarCandidatesWithBase) {
			similarArray.add(simCpd.getCompoundID());
		}
		return similarArray;
	}
	
	/**
	 * Gets the similar compounds' tanimoto distances
	 * 
	 * @return the similar compounds
	 */
	public List<Float> getTanimotoSimilarities()
	{
		return tanimotoSimilarities;
	}

	/**
	 * Gets the candidate tocompare.
	 * 
	 * @return the candidate tocompare
	 */
	public String getCandidateTocompare() {
		return candidateTocompare;
	}

	/**
	 * Gets the similar candidates with base.
	 * 
	 * @return the similar candidates with base
	 */
	public List<SimilarityCompound> getSimilarCandidatesWithBase() {
		return similarCandidatesWithBase;
	}
	

}
