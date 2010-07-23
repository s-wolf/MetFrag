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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TanimotoClusterer {
	
	private float[][] distanceMatrix;
	private Map<String, Integer> candidateToPosition;
	
	/**
	 * Instantiates a new tanimoto clusterer.
	 * 
	 * This method is based on: "Butina D: Unsupervised Data Base Clustering Based on Daylight’s
	 * Fingerprint and Tanimoto Similarity: A Fast and Automated Way To
	 * Cluster Small and Large Data Sets. Journal of Chemical Information and
     * Computer Sciences 1999, 39(4):747-750"
	 * 
	 * @param distanceMatrix the distance matrix previously calculated using the Similarity class which returns 
	 * 			an upper triangular matrix with tanimoto distances
	 * @param candidateToPosition the candidate to position hashmap
	 */
	public TanimotoClusterer(float[][] distanceMatrix, Map<String, Integer> candidateToPosition)
	{
		this.distanceMatrix = distanceMatrix;
		this.candidateToPosition = candidateToPosition;
	}
	
	
	
	/**
	 * Gets the tanimoto distance from a list of candidates and groups them!.
	 * 
	 * @param candidateGroup the candidate group
	 * @param threshold the threshold
	 * 
	 * @return the sorted grouped candidates
	 */
	public List<SimilarityGroup> clusterCandididates(List<String> candidateGroup, double similarityThreshold)
	{
		List<SimilarityGroup> groupedCandidates = new ArrayList<SimilarityGroup>();
		for (String cand1 : candidateGroup) {
			SimilarityGroup simGroup = new SimilarityGroup(cand1);
			for (String cand2 : candidateGroup) {
				if(cand1.equals(cand2))
					continue;
				else if(cand1 == null || cand2 == null)
					continue;
				else
				{
					Float tanimoto = getTanimotoDistance(cand1, cand2);
					if(tanimoto > similarityThreshold)
						simGroup.addSimilarCompound(cand2, tanimoto);
				}
			}
			//now add similar compound to the group list
			//if(!isContainedInPreviousResults(groupedCandidates, simGroup))
			groupedCandidates.add(simGroup);
		}
		groupedCandidates = sortGroupedCandidates(groupedCandidates);
		return groupedCandidates;
	}
	
	
	/**
	 * Sort grouped candidates according to their cluster size.
	 * 
	 * @param groupedCandidates the grouped candidates
	 * 
	 * @return the list< similarity group>
	 */
	private List<SimilarityGroup> sortGroupedCandidates(List<SimilarityGroup> groupedCandidates)
	{
		Map<Integer, List<SimilarityGroup>> sortedGroups = new HashMap<Integer, List<SimilarityGroup>>();
		for (SimilarityGroup similarityGroup : groupedCandidates) {
			addToMap(sortedGroups, similarityGroup);
		}
		
		Integer[] sortedClusterSizesArray = new Integer[sortedGroups.keySet().size()];
		sortedClusterSizesArray = sortedGroups.keySet().toArray(sortedClusterSizesArray);
		Arrays.sort(sortedClusterSizesArray);
		
		List<SimilarityGroup> sortedCandidateCluster = new ArrayList<SimilarityGroup>();
		for (int i = 0; i < sortedClusterSizesArray.length; i++) {
			List<SimilarityGroup> tempList = sortedGroups.get(i);
			for (SimilarityGroup similarityGroup : tempList) {
				sortedCandidateCluster.add(similarityGroup);
			}
		}
		
		return sortedCandidateCluster;
	}
	
	
	/**
	 * Adds the similarity group to the map.
	 * 
	 * @param sortedGroups the sorted groups
	 * @param simGroup the sim group
	 */
	private void addToMap(Map<Integer, List<SimilarityGroup>> sortedGroups, SimilarityGroup simGroup)
	{
		int clusterSize = simGroup.getSimilarCandidatesWithBase().size();
		if(sortedGroups.containsKey(clusterSize))
		{
			List<SimilarityGroup> temp = sortedGroups.get(clusterSize);
			temp.add(simGroup);
		}
		else
		{
			List<SimilarityGroup> temp = new ArrayList<SimilarityGroup>();
			temp.add(simGroup);
		}
			
	}
	
	
	/**
	 * Gets the tanimoto distance between two candidate structures.
	 * 
	 * @param candidate1 the candidate1
	 * @param candidate2 the candidate2
	 * 
	 * @return the tanimoto distance
	 */
	public float getTanimotoDistance(String candidate1, String candidate2)
	{
		int pos1 = 0;
		int pos2 = 0;
		try
		{
			pos1 = candidateToPosition.get(candidate1);
			pos2 = candidateToPosition.get(candidate2);
		}
		catch(NullPointerException e)
		{
			return 0;
		}
		if(distanceMatrix[pos1][pos2] != Float.POSITIVE_INFINITY)
			return distanceMatrix[pos1][pos2];
		else
			return distanceMatrix[pos2][pos1];
	}
}
