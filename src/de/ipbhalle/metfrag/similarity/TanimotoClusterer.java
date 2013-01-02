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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.smiles.SmilesParser;

public class TanimotoClusterer {
	
	private float[][] distanceMatrix;
	private Map<String, Integer> candidateToPosition;
	private Map<String, Boolean> candidatesDone;
	
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
		//set the flags for the compounds already contained in larger clusters
		filterClusters(groupedCandidates);
		return groupedCandidates;
	}
	
	
	/**
	 * Gets the cleaned clusters. Only candidates which do not have
	 * a flag are returned.
	 * 
	 * @param groupedCandidates the grouped candidates
	 * 
	 * @return the cleaned clusters
	 */
	public List<SimilarityGroup> getCleanedClusters(List<SimilarityGroup> groupedCandidates)
	{
		List<SimilarityGroup> cleanedClusters = new ArrayList<SimilarityGroup>();
		for (SimilarityGroup similarityGroup : groupedCandidates) {
			
			int count = 0;
			SimilarityGroup simGroup = new SimilarityGroup(similarityGroup.getCandidateTocompare());
			for (int i = 0; i < similarityGroup.getSimilarCandidatesWithBase().size(); i++) {
				SimilarityCompound simCpd = similarityGroup.getSimilarCandidatesWithBase().get(i);
				if(!simCpd.isAlreadyInCluster())
				{
					count++;
					simGroup.addSimilarCompound(simCpd.getCompoundID());
				}
			}
			
			if(count > 0)
				cleanedClusters.add(simGroup);
		}
		
		return cleanedClusters;
	}
	
	
	/**
	 * Gets the filtered clusters. This methods sets flags on compounds which are
	 * already contained in larger clusters
	 * 
	 * @param groupedCandidatesSorted the grouped candidates sorted
	 * 
	 * @return the filtered clusters
	 */
	private void filterClusters(List<SimilarityGroup> groupedCandidatesSorted)
	{
		this.candidatesDone = new HashMap<String, Boolean>();
		
		//iterate over the descending sorted list of grouped candidates
		for (int i = 0; i < groupedCandidatesSorted.size(); i++) {
			for (SimilarityCompound similarCompound : groupedCandidatesSorted.get(i).getSimilarCandidatesWithBase()) {
				if(this.candidatesDone.containsKey(similarCompound.getCompoundID()))
				{
					similarCompound.setAlreadyInCluster(true);
					continue;
				}
				else
				{
					this.candidatesDone.put(similarCompound.getCompoundID(), true);
				}
					
			}
		}
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
		for (int i = sortedClusterSizesArray.length - 1; i >= 0; i--) {
			List<SimilarityGroup> tempList = sortedGroups.get(sortedClusterSizesArray[i]);
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
			sortedGroups.put(clusterSize, temp);
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
	
	
	
	public static void main(String[] args) {
		
		
		SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
		try {
			String mol = "C1C(OC2=CC(=CC(=C2C1=O)O)O)C3=CC=CC=C3O";
			String mol1 = "C1C(OC2=CC(=CC(=C2C1=O)O)O)C3=CC=C(C=C3)O";
			String mol2 = "C1C(OC(=O)C2=C(C=C(C=C21)O)O)C3=CC=C(C=C3)O";
			String mol3 = "C1C(OC2=CC(=CC(=C2C1=O)O)O)C3=CC=C(C=C3)O";
			String mol4 = "C1C(OC2=CC(=CC(=C2C1=O)O)O)C3=CC=C(C=C3)O";
			String mol5 = "C1C(OC2=CC(=CC(=C2C1=O)O)O)C3=CC(=CC=C3)O";
			String mol6 = "C1C(OC2=CC(=C(C=C2C1=O)O)O)C3=CC=C(C=C3)O";
			String mol7 = "C1C(OC2=CC(=CC(=C2C1=O)O)O)C3=CC=CC=C3O";
			String mol8 = "C1C(C(=O)C2=CC(=C(C=C2O1)O)O)C3=CC=C(C=C3)O";
			Map<String, String> testMap = new HashMap<String, String>();
			testMap.put("179999", mol);
			testMap.put("932", mol1);
			testMap.put("10333412", mol2);
			testMap.put("439246", mol3);
			testMap.put("667495", mol4);
			testMap.put("113638", mol5);
			testMap.put("23724670", mol6);
			testMap.put("13889010", mol7);
			testMap.put("125100", mol8);
			
			List<String> cands = new ArrayList<String>();
			cands.add("179999");
			cands.add("932");
			cands.add("10333412");
			cands.add("439246");
			cands.add("667495");
			cands.add("113638");
			cands.add("23724670");
			cands.add("13889010");
			cands.add("125100");
			
			Similarity sim = new Similarity(testMap, false);
			TanimotoClusterer tanimoto = new TanimotoClusterer(sim.getSimilarityMatrix(), sim.getCandidateToPosition());
			List<SimilarityGroup> clusteredCpds = tanimoto.clusterCandididates(cands, 0.95f);
			List<SimilarityGroup> clusteredCpdsCleaned = tanimoto.getCleanedClusters(clusteredCpds);
			
			for (SimilarityGroup similarityGroup : clusteredCpdsCleaned) {
				//cluster
				if(similarityGroup.getSimilarCompounds().size() > 1)
				{
					for (int i = 0; i < similarityGroup.getSimilarCompounds().size(); i++) {
						System.out.print(similarityGroup.getSimilarCompounds().get(i).getCompoundID() + " ");
					}
				}
				//single
				else
				{
					System.out.print("Single: " + similarityGroup.getSimilarCompounds().get(0).getCompoundID());
				}
				
				System.out.println("");

			}
			
			
		} catch (InvalidSmilesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CDKException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
