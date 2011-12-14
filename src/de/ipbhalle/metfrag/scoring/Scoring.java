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

import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

import de.ipbhalle.metfrag.massbankParser.Peak;
import de.ipbhalle.metfrag.spectrum.MatchedFragment;
import de.ipbhalle.metfrag.spectrum.NeutralLoss;
import de.ipbhalle.metfrag.spectrum.WrapperSpectrum;
import de.ipbhalle.metfrag.tools.Constants;
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
	double scoreBondLengthChange = 0.0;
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
			
			scoreBondEnergy = hits.get(i).getBde();
			scoreBondLengthChange = hits.get(i).getBondLengthChange();
			
			penalty += (hits.get(i).getHydrogenPenalty() * 100);
			
			//get all neutral losses
			NeutralLoss[] nl = hits.get(i).getNeutralLosses();
			String neutralLossString = "";
			if(nl != null)
			{
				for (int j = 0; j < nl.length; j++) {
					if(j == (nl.length - 1))
						neutralLossString += MolecularFormulaManipulator.getString(nl[j].getElementalComposition());
					else
						neutralLossString += MolecularFormulaManipulator.getString(nl[j].getElementalComposition()) + ",";
				}
			}
						
			//add new entry to optimization matrix
			this.optimizationMatrixEntries.add(new OptimizationMatrixEntry(candidateID, hits.get(i).getPeak().getMass(), hits.get(i).getPeak().getIntensity(), (String)hits.get(i).getFragmentStructure().getProperty(Constants.BDE), hits.get(i).getHydrogenPenalty(), (String)hits.get(i).getFragmentStructure().getProperty(Constants.BONDLENGTHCHANGE), neutralLossString, (String)hits.get(i).getFragmentStructure().getProperty(Constants.BONDORDER), (String)hits.get(i).getFragmentStructure().getProperty(Constants.BONDORDERDIFF), (String)hits.get(i).getFragmentStructure().getProperty(Constants.BONDREMOVED)));
			
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
		double bondLengthChange = 0.0;
		
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
			bondLengthChange += hits.get(i).getBondLengthChange();
			
			//get all neutral losses
			NeutralLoss[] nl = hits.get(i).getNeutralLosses();
			String neutralLossString = "";
			if(nl != null)
			{
				for (int j = 0; j < nl.length; j++) {
					if(j == (nl.length - 1))
						neutralLossString += MolecularFormulaManipulator.getString(nl[j].getElementalComposition());
					else
						neutralLossString += MolecularFormulaManipulator.getString(nl[j].getElementalComposition()) + ",";
				}
			}
			
			//add new entry to optimization matrix
			this.optimizationMatrixEntries.add(new OptimizationMatrixEntry(candidateID, hits.get(i).getPeak().getMass(), hits.get(i).getPeak().getRelIntensity(), bondEnergies, hits.get(i).getHydrogenPenalty(), neutralLossString, (String)hits.get(i).getFragmentStructure().getProperty(Constants.BONDLENGTHCHANGE), (String)hits.get(i).getFragmentStructure().getProperty(Constants.BONDORDER), (String)hits.get(i).getFragmentStructure().getProperty(Constants.BONDORDERDIFF),  (String)hits.get(i).getFragmentStructure().getProperty(Constants.BONDREMOVED)));
			
		}
		
		
		this.scoreBondEnergy = BDE;
		this.scoreBondLengthChange = bondLengthChange;
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
			tempPartialCharges = bondLengthChange / hits.size();
		}
		score = (a * weightedPeaks) - (b * (tempPartialCharges * tempPartialCharges));

		return score;
	}
	
	/**
	 * Score candidates with the optimized new scoring using bond orders. Candidates need to be
	 * preprocessed!!
	 *
	 * @param mapToCandidateFragments the map to candidate fragments
	 * @return the map
	 */
	public static Map<Double, Vector<String>> scoreCandidates(Map<String, Vector<MatchedFragment>> mapToCandidateFragments)
	{
		Map<Double, Vector<String>> scoredCandidates = new HashMap<Double, Vector<String>>();

        double maxIntensity = 0.0;
        double maxPeakMass = 0.0;
        double maxBondLengthChange = 0.0;
        double maxBondOrder = 0.0;
        double maxBDE = 0.0;
        double maxHP = 0.0;
        
		for (String candidateID : mapToCandidateFragments.keySet()) {
			double tempBondLength = 0.0;
			double tempBondOrder = 0.0;
			double tempPeakMass = 0.0;
			double tempPeakInt = 0.0;
			double tempBDE = 0.0;
			double tempHP = 0.0;
			
			//iterate over this candidates' fragments and get the max values
			for (MatchedFragment fragment : mapToCandidateFragments.get(candidateID)) {
				tempBondLength = fragment.getBondLengthChange();
				tempBondOrder = fragment.getBondOrder();
				tempPeakMass = fragment.getPeak().getMass();
				tempPeakInt = fragment.getPeak().getIntensity();
				tempBDE = fragment.getBde();
				tempHP = fragment.getHydrogenPenalty();
				
				if(tempPeakInt > maxIntensity)
					maxIntensity = tempPeakInt;
				
				if(tempPeakMass > maxPeakMass)
					maxPeakMass = tempPeakMass;
								
				if(tempBondLength > maxBondLengthChange)
					maxBondLengthChange = tempBondLength;
				
				if(tempBondOrder > maxBondOrder)
					maxBondOrder = tempBondOrder;
				
				if(tempBDE > maxBDE)
					maxBDE = tempBDE;
				
				if(tempHP > maxHP)
					maxHP = tempHP;
				
				//now average the values so candidates which explain a lot peaks are not penalized!
//				int matchedFragmentCount = mapToCandidateFragments.get(candidateID).size();
//				tempBondLength = tempBondLength / Double.valueOf(matchedFragmentCount);
//				tempBondOrder = tempBondOrder / Double.valueOf(matchedFragmentCount);
//				tempBDE = tempBDE / Double.valueOf(matchedFragmentCount);
			}			
		}
		
		//optimized parameters 0.6353030459896155 1.0915238557782132 2.271690272229761
		double a = 0.85;
		double b = 0.58;
		double c = 0.52;
//		double d = 0.033436071416133584;
		
		for (String candidateID : mapToCandidateFragments.keySet()) {
			double individualScores = 0.0;
			double tempBondLength = 0.0;
			double tempBondOrder = 0.0;
			double tempPeakMass = 0.0;
			double tempPeakInt = 0.0;
			double tempBDE = 0.0;
			double tempHP = 0.0;
			//iterate over this candidates' fragments and get the max values
			for (MatchedFragment fragment : mapToCandidateFragments.get(candidateID)) {
				tempBondLength = fragment.getBondLengthChange();
				tempBondOrder = fragment.getBondOrderWeighted();
				tempPeakMass = fragment.getPeak().getMass();
				tempPeakInt = fragment.getPeak().getIntensity();
				tempBDE = fragment.getBde();
				tempHP = fragment.getHydrogenPenalty();
				
//				individualScores += ((a * (tempPeakMass / maxPeakMass) + b * (tempPeakInt / maxIntensity)) * (c * (1 - (tempBondOrder / maxBondOrder))));
//				individualScores.add(((this.parameters[0] * (r.getPeakMass() / maxIndividualPeakMass)) + 
//						(this.parameters[1] * (r.getPeakInt() / maxIndividualInt))) * 
//						(this.parameters[2] * (1 - (tempBOIndividual / maxIndividualBondOrder))));
				
//              best result: rrp: 10.0968: 0.7499 1.6752 0.4627 --> with rrp minimization
//              best result 1/3 median + 1/3 average: 2011-11-28 16:15:14.4| 0.8924398156145014 2.5829939404187297 2.702359459531139 Score: 45.24666666666666
//				individualScores += (((Math.pow((tempPeakMass / maxPeakMass), a)) * (Math.pow((tempPeakInt / maxIntensity), b))) * (c * (1 - (tempBondOrder / maxBondOrder))));
//				 0.905370372011248 0.7602936368046967 2.9802977932529973 0.07976331444216822 Score: 158.9570091735854
//				2011-11-30 10:36:29.915| 0.605519245363914 0.9044550966839193 1.8045546811729256 0.016615536120909935 Score: 184.26328386626636
//				2011-11-30 10:45:34.723| 0.8050470257810172 0.9004924210209632 0.9355410100697816 0.033436071416133584 Score: 177.53203623370868
				
				//bug in scoring function optimization....using same values for int and mass
				//fixed NL BUG and use NL BO and target function: sum(rrp)*median
//				2011-12-06 19:25:23.226| 0.38647870592117006 0.7711949058143358 2.873537217254535 0.1813990367861028 Score: 150.11550152848065
//				2011-12-06 18:55:55.241| 0.348977334605987 0.8750913898107362 2.4647011556436698 0.07126222195714549 Score: 152.94279236370213
				
				//bug fixed parameters median*rrp
//              2011-12-08 12:25:28.085| 0.9820687515274057 0.39231025184307844 3.5865043604129894 Score: 157.32399216852696
//              2011-12-08 12:35:11.636| 0.8559748053512829 0.5047291590635609 1.9431895490213669 Score: 158.95402179884636
				
				//CV average values 0,969420007 0,4636468136 1,7775420293

				
//				if(maxHP == 0)
//					individualScores += ((((Math.pow((tempPeakMass / maxPeakMass), a)) * (Math.pow((tempPeakInt / maxIntensity), b))) * (c * (1 - (tempBondOrder / maxBondOrder)))));
//				else
//					individualScores += ((((Math.pow((tempPeakMass / maxPeakMass), a)) * (Math.pow((tempPeakInt / maxIntensity), b))) * (c * (1 - (tempBondOrder / maxBondOrder)))) + (d * (1 - (tempHP / maxHP))));
				
				individualScores += Math.pow((tempPeakMass / maxPeakMass), a) * Math.pow((tempPeakInt / maxIntensity), b) * Math.pow(tempBondOrder, c);
			}
			
			//now add the score to the results list
			if(scoredCandidates.containsKey(individualScores))
			{
				scoredCandidates.get(individualScores).add(candidateID);
			}
			else
			{	
				Vector<String> temp = new Vector<String>();
				temp.add(candidateID);
				scoredCandidates.put(individualScores, temp);
			}
		}
		
		return scoredCandidates;
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
		return this.scoreBondLengthChange;
	}


	public void setPenalty(double penalty) {
		this.penalty = penalty;
	}



	public double getPenalty() {
		return penalty;
	}
	
	

}
