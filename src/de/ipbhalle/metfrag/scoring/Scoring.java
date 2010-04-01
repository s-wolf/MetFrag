package de.ipbhalle.metfrag.scoring;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.openscience.cdk.formula.MolecularFormula;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

import de.ipbhalle.metfrag.fragmenter.NeutralLoss;
import de.ipbhalle.metfrag.massbankParser.Peak;
import de.ipbhalle.metfrag.spectrum.PeakMolPair;



// TODO: Auto-generated Javadoc
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
	private double penalty = 0.0;
	private double partialChargeDiff = 0.0;
	private HashMap<Double, Integer> peakToRank;
	
	/**
	 * Instantiates a new scoring.
	 * 
	 * @param peakList the intensities
	 */
	public Scoring(Vector<Peak> peakList)
	{
		this.mzToIntensity = new HashMap<Double, Double>();
		this.peakToRank = new HashMap<Double, Integer>();
		
		//sum up intensities ... TODO or just take the maximum?!
		for (int i = peakList.size()-1; i >= 0; i--) {
			mzToIntensity.put(peakList.get(i).getMass(), peakList.get(i).getRelIntensity());
			//rank the peaks from small to large....sorted is used as input
			this.peakToRank.put(peakList.get(i).getMass(), i+1);
			this.sumIntensities += peakList.get(i).getRelIntensity();
		}
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
	public double computeScoringPeakMolPair(Vector<PeakMolPair> hits)
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
	public double computeScoringWithBondEnergies(Vector<PeakMolPair> hits)
	{
		double score = 0.0;
		this.scoreBondEnergy = 0.0;
		
		for (int i = 0; i < hits.size(); i++) {			
			//Scoring like in Massbank paper m=0.6, n=3
			//W = [Peak intensity]^m * [Mass]^n
			score += Math.pow(this.mzToIntensity.get(hits.get(i).getPeak().getMass()), 0.6) * Math.pow(hits.get(i).getPeak().getMass(),3);
			scoreBondEnergy += Double.parseDouble((String)hits.get(i).getFragment().getProperty("BondEnergy"));
			penalty += hits.get(i).getHydrogenPenalty(); 
			partialChargeDiff += hits.get(i).getPartialChargeDiff();
		}
		//todo optimize parameter
		penalty = penalty / 10;
		return score;
	}
	
	/**
	 * Gets the fragment bond energy.
	 * 
	 * @return the fragment bond energy
	 */
	public double getFragmentBondEnergy()
	{
		return this.scoreBondEnergy;
	}
	
	/**
	 * Gets the partial charge Diff.
	 * 
	 * @return the partial charge diff
	 */
	public double getPartialChargeDiff()
	{
		return this.partialChargeDiff;
	}
	
	
	/**
	 * Gets the combined score.
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



	public void setPenalty(double penalty) {
		this.penalty = penalty;
	}



	public double getPenalty() {
		return penalty;
	}
	
	

}
