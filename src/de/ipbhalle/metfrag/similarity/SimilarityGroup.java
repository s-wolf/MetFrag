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
