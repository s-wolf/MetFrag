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
package de.ipbhalle.metfrag.scoring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import de.ipbhalle.metfrag.fragmenter.Fragmenter;
import de.ipbhalle.metfrag.massbankParser.Peak;
import de.ipbhalle.metfrag.spectrum.MatchedFragment;
import de.ipbhalle.metfrag.spectrum.NeutralLoss;
import de.ipbhalle.metfrag.spectrum.WrapperSpectrum;
import de.ipbhalle.metfrag.tools.MoleculeTools;



/**
 * The Class Scoring.
 * f(X) = sum i = 1 to n ( (I_n/ I_g) * p_N)
 * p_N = 0 peak not found --> else: 1
 * I_n ... nth intensity
 * I_g ... sum of all intensities
 */
public class Scoring {
	
	private Map<Double, Double> mzToIntensity = null;
	private Map<Double, NeutralLoss> neutralLoss= null;
	private double sumIntensities = 0;
	double scoreBondEnergy = 0.0;
	double scoreChargesDiff = 0.0;
	private double penalty = 0.0;
	private HashMap<Double, Integer> peakToRank;
	private List<OptimizationMatrixEntry> optimizationMatrixEntries;
	private String candidateID;
	
	/**
	 * Instantiates a new scoring.
	 * 
	 * @param peakList the intensities
	 */
	public Scoring(WrapperSpectrum spectrum, String candidateID)
	{
		this.mzToIntensity = new HashMap<Double, Double>();
		this.peakToRank = new HashMap<Double, Integer>();
		
		//sum up intensities ... TODO or just take the maximum?!
		Vector<Peak> peakList = spectrum.getPeakList();
		for (int i = peakList.size()-1; i >= 0; i--) {
			mzToIntensity.put(peakList.get(i).getMass(), peakList.get(i).getRelIntensity());
			//rank the peaks from small to large....sorted is used as input
			this.peakToRank.put(peakList.get(i).getMass(), i+1);
			this.sumIntensities += peakList.get(i).getRelIntensity();
		}
		
		this.optimizationMatrixEntries = new ArrayList<OptimizationMatrixEntry>();
		this.candidateID = candidateID;
	}	
	
	/**
	 * Compute scoring.
	 * * f(X) = sum i = 1 to n ( (I_n/ I_g) * p_N)
	 * p_N = 0 peak not found --> else: 1
	 * I_n ... nth intensity
	 * I_g ... sum of all intensities --> precomputed
	 * 
	 * @param hits the hits
	 */
	public double computeScoringPeakMolPair(Vector<MatchedFragment> hits)
	{
		double score = 0.0;

		for (int i = 0; i < hits.size(); i++) {			
			//Scoring like in Massbank paper m=0.6, n=3
			//W = [Peak intensity]^m * [Mass]^n
			score += Math.pow(this.mzToIntensity.get(hits.get(i).getPeak().getMass()), 0.6) * Math.pow(hits.get(i).getPeak().getMass(),3);
		}
		
		return score;
	}
	
	
	/**
	 * Compute scoring with bond energies.
	 * 
	 * @param hits the hits
	 * 
	 * @return the double
	 */
	public double computeScoringWithBondEnergies(Vector<MatchedFragment> hits)
	{
		double score = 0.0;
		this.scoreBondEnergy = 0.0;
		
		for (int i = 0; i < hits.size(); i++) {			
			//Scoring like in Massbank paper m=0.6, n=3
			//W = [Peak intensity]^m * [Mass]^n
			score += Math.pow(this.mzToIntensity.get(hits.get(i).getPeak().getMass()), 0.6) * Math.pow(hits.get(i).getPeak().getMass(),3);
			
			String bondEnergies = (String)hits.get(i).getFragmentStructure().getProperty("BondEnergy");
			scoreBondEnergy = MoleculeTools.getCombinedEnergy(bondEnergies);

			scoreChargesDiff = hits.get(i).getPartialChargeDiff();
			
			penalty += (hits.get(i).getHydrogenPenalty() * 100);
			
			//get all neutral losses
			NeutralLoss[] nl = hits.get(i).getNeutralLosses();
			String neutralLossString = "";
			if(nl != null)
			{
				for (int j = 0; j < nl.length; j++) {
					if(i == (nl.length - 1))
						neutralLossString += nl[i];
					else
						neutralLossString += nl[i] + ",";
				}
			}
						
			//add new entry to optimization matrix
			this.optimizationMatrixEntries.add(new OptimizationMatrixEntry(candidateID, hits.get(i).getPeak().getMass(), hits.get(i).getPeak().getIntensity(), (String)hits.get(i).getFragmentStructure().getProperty("BondEnergy"), hits.get(i).getHydrogenPenalty(), (String)hits.get(i).getFragmentStructure().getProperty("PartialChargeDiff"), neutralLossString));
			
		}

		return score;
	}
	
	
	/**
	 * Compute scoring optimized.
	 * 
	 * @param hits the hits
	 * @param candidateExactMass the candidate exact mass
	 * 
	 * @return the double
	 */
	public double computeScoringOptimized(Vector<MatchedFragment> hits, double candidateExactMass)
	{
		double score = 0.0;
		double weightedPeaks = 0.0;
		double BDE = 0.0;
		double hydrogenPenalty = 0.0;
		double partialChargesDiff = 0.0;
		
		for (int i = 0; i < hits.size(); i++) {			
			//Scoring like in Massbank paper m=0.6, n=3
			//NEW trained data from hill
			//W = [Peak intensity]^m * [Mass]^n
			weightedPeaks += Math.pow(this.mzToIntensity.get(hits.get(i).getPeak().getMass()), 1.150429) * Math.pow(((hits.get(i).getPeak().getMass() / candidateExactMass) * 10), 1.843564);
			
			//bond energy
			String bondEnergies = (String)hits.get(i).getFragmentStructure().getProperty("BondEnergy");
			BDE += MoleculeTools.getCombinedEnergy(bondEnergies);
			
			//hydrogen penalty
			hydrogenPenalty += (hits.get(i).getHydrogenPenalty() * 100);
			
			//partial charges diff
			partialChargesDiff += hits.get(i).getPartialChargeDiff();
			
			//get all neutral losses
			NeutralLoss[] nl = hits.get(i).getNeutralLosses();
			String neutralLossString = "";
			if(nl != null)
			{
				for (int j = 0; j < nl.length; j++) {
					if(i == (nl.length - 1))
						neutralLossString += nl[i];
					else
						neutralLossString += nl[i] + ",";
				}
			}
			
			//add new entry to optimization matrix
			this.optimizationMatrixEntries.add(new OptimizationMatrixEntry(candidateID, hits.get(i).getPeak().getMass(), hits.get(i).getPeak().getRelIntensity(), bondEnergies, hits.get(i).getHydrogenPenalty(), (String)hits.get(i).getFragmentStructure().getProperty("PartialChargeDiff"), neutralLossString));
			
		}
		
		
		this.scoreBondEnergy = BDE;
		this.scoreChargesDiff = partialChargesDiff;
		this.penalty = hydrogenPenalty;
		
		//best result so far
//		double a = 0.1049;
//		double b = 7.3030;
		double a = 0.723703;
		double b = 0.276297;
		double tempBDE = 0.0;
		double tempPartialCharges = 0.0;
		if(hits.size() > 0)
		{
			tempBDE = BDE / hits.size();
			tempPartialCharges = partialChargesDiff / hits.size();
		}
		score = (a * weightedPeaks) - (b * (tempPartialCharges * tempPartialCharges));

		return score;
	}
	
	
	/**
	 * Gets the fragment bond energy.
	 * 
	 * @return the fragment bond energy
	 */
	public double getBDE()
	{
		return this.scoreBondEnergy;
	}
	
	
	/**
	 * Gets the combined score. This is used for normalizing the score values and weight the terms differently.
	 * 
	 * @param realScoreMap the real score map
	 * @param mapCandidateToEnergy the map candidate to energy
	 * @param mapCandidateToHydrogenPenalty the map candidate to hydrogen penalty
	 * 
	 * @return the combined score
	 */
	public static Map<Double, Vector<String>> getCombinedScore(Map<Double, Vector<String>> realScoreMap, Map<String, Double> mapCandidateToEnergy, Map<String, Double> mapCandidateToHydrogenPenalty)
	{
		Map<Double, Vector<String>> ret = new HashMap<Double, Vector<String>>();
		double maxScore = 0.0;
		double maxBondEnergy = 0.0;
		
		for (Double score : realScoreMap.keySet()) {
			if(score > maxScore)
				maxScore = score;
			
			Vector<String> cands = realScoreMap.get(score);
			for (String candidate : cands) {
				double bondEnergy = mapCandidateToEnergy.get(candidate);
				double hydrogenPenalty = 0;
				try
				{
					hydrogenPenalty = mapCandidateToHydrogenPenalty.get(candidate);
				}
				catch(NullPointerException e)
				{
					System.err.println("ERROR: Null Pointer exception in scoring! \n" + e.getMessage());
				}
				double combinedEnergy = bondEnergy + hydrogenPenalty;
				if(combinedEnergy > maxBondEnergy)
					maxBondEnergy = combinedEnergy;
			}
		}
		
		
		//now compute the final score....normalize the weighted peaks to 1 and the bond energy to 0.5 and finally add them up
		//and add them together
		for (Double score : realScoreMap.keySet()) {
			double normalizedScore = Math.round((score / maxScore) * 1000) / 1000.0;
			Vector<String> cands = realScoreMap.get(score);
			for (String candidate : cands) {
				double bondEnergy = mapCandidateToEnergy.get(candidate);
				double hydrogenPenalty = 0;
				try
				{
					hydrogenPenalty = mapCandidateToHydrogenPenalty.get(candidate);
				}
				catch(NullPointerException e)
				{
					System.err.println("ERROR: Null Pointer exception in scoring! \n" + e.getMessage());
				}
				double combinedEnergy = bondEnergy + hydrogenPenalty;
				
				double normalizedBondEnergy = 0;
				//more than 1 peak explained
				if(bondEnergy > 0)
				{
					//now normalize the bond energy and subtract it from 1
					normalizedBondEnergy = Math.round((combinedEnergy / maxBondEnergy) * 1000) / 1000.0;
				}
				
				
				//TODO: 4 delivers better results than 2
				double finalScore = normalizedScore - (normalizedBondEnergy / 4);
				
				if(finalScore < 0)
					finalScore = 0.0;
				
				if(ret.containsKey(finalScore))
				{
					Vector<String> temp = ret.get(finalScore);
					temp.add(candidate);
					ret.remove(finalScore);
					ret.put(finalScore, temp);
				}
				else
				{
					Vector<String> temp = new Vector<String>();
					temp.add(candidate);
					ret.put(finalScore, temp);
				}
			}
			
		}		
		
		return ret;
	}
	

	/**
	 * Gets the optimization matrix entries.
	 * 
	 * @return the optimization matrix entries
	 */
	public List<OptimizationMatrixEntry> getOptimizationMatrixEntries()
	{
		return this.optimizationMatrixEntries;
	}

	
	public double getPartialChargesDiff()
	{
		return this.scoreChargesDiff;
	}


	public void setPenalty(double penalty) {
		this.penalty = penalty;
	}



	public double getPenalty() {
		return penalty;
	}
	
	

}
