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
package de.ipbhalle.metfrag.spectrum;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.formula.MolecularFormula;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

import de.ipbhalle.metfrag.massbankParser.Peak;
import de.ipbhalle.metfrag.tools.Constants;
import de.ipbhalle.metfrag.tools.MolecularFormulaTools;
import de.ipbhalle.metfrag.tools.MoleculeTools;
import de.ipbhalle.metfrag.tools.PPMTool;
import de.ipbhalle.metfrag.tools.SMARTSTools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class AssignFragmentPeak {
	
	private List<IAtomContainer> acs;
	private Vector<Peak> peakList;
	private Vector<MatchedFragment> hits;
	private Vector<MatchedFragment> hitsAll;
	private Vector<Double> hitsPeaks;
	//private Vector<PeakMolPair> noHits; TODO: for log file
	private double mzabs;
	private double mzppm;
	private boolean html;
	private NeutralLossCheck nlc;
	private int neutralLossCombination;
	private int hydrogenPenalty;
	
	/**
	 * Instantiates a new assign fragment peak.
	 *
	 * @param neutralLossCombination the neutral loss combination
	 */
	public AssignFragmentPeak(int neutralLossCombination)
	{
        this.nlc = new NeutralLossCheck(neutralLossCombination);
        this.neutralLossCombination = neutralLossCombination;
	}
	
	
	/**
	 * Match fragment with peak. Assigns peaks to the fragments.
	 *
	 * @param acs the acs
	 * @param peakList the peak list
	 * @param mzabs the mzabs
	 * @param mzppm the mzppm
	 * @param mode the mode
	 * @param html the html
	 * @param neutralLossCombination the maximum number of neutral loss combinations
	 * @throws CDKException the CDK exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void assignFragmentPeak(List<IAtomContainer> acs, Vector<Peak> peakList, double mzabs, double mzppm, int mode, boolean html) throws CDKException, IOException
	{
		this.acs = acs;
		this.peakList = peakList;
		this.mzabs = mzabs;
		this.mzppm = mzppm;
		this.hits = new Vector<MatchedFragment>();
		this.hitsAll = new Vector<MatchedFragment>();
		this.hitsPeaks = new Vector<Double>();
		this.html = html;
//		//initialize neutral losses
//		getNeutralLosses();
		
		
		for (int i=0; i< peakList.size(); i++)
		{
			//System.out.println("Peak: " + this.peakList.get(i).getMass() + " ====================================");
			boolean test = true;
			for (int j = 0; j < this.acs.size(); j++) {
				//matched peak
				List<MatchedFragment> fragmentsMatched = matchPeak(this.acs.get(j), this.peakList.get(i), mode, neutralLossCombination);
				
				//now find the best hit for the peak
				//loop over every matched fragment and find the largest partial charge difference
				double maxPartialChargeDiff = -1.0;
				int minHydrogenPenalty = 10;
				for (MatchedFragment matchedFragment : fragmentsMatched) {
					if(matchedFragment.getPartialChargeDiff() > maxPartialChargeDiff)
					{
						maxPartialChargeDiff = matchedFragment.getPartialChargeDiff();
						if(matchedFragment.getHydrogenPenalty() < minHydrogenPenalty)
						{
							minHydrogenPenalty = matchedFragment.getHydrogenPenalty();
						}
					}
				}
				
				//now save this fragment in the list with the best hits
				for (MatchedFragment matchedFragment : fragmentsMatched) {
					if(matchedFragment.getPartialChargeDiff() == maxPartialChargeDiff && minHydrogenPenalty == matchedFragment.getHydrogenPenalty())
					{
						hits.add(matchedFragment);
						hitsPeaks.add(this.peakList.get(i).getMass());
					}
				}
				
				hitsAll.addAll(fragmentsMatched);

			}
		}
	}
	
	
	/**
	 * Match the peak to a fragment using 3 different methods:
	 * 
	 *<ul><li>By mass</li>
	 *<li>With neutral loss</li>
	 *<li>With variable hydrogen count</li>
	 *
	 * @param fragmentStructure the fragment structure
	 * @param peak the peak
	 * @param mode the mode
	 * @param neutralLossCombination the neutral loss combination
	 * @return the list
	 * @throws CDKException 
	 * @throws IOException 
	 */
	private List<MatchedFragment> matchPeak(IAtomContainer fragmentStructure, Peak peak, int mode, int neutralLossCombination) throws IOException, CDKException
	{
		List<MatchedFragment> matchedFragments = new ArrayList<MatchedFragment>();
		
		IMolecularFormula molecularFormula = new MolecularFormula();
        molecularFormula = MolecularFormulaManipulator.getMolecularFormula(fragmentStructure, molecularFormula);         
        double fragmentMass = 0.0;
        //speed up and neutral loss matching!
        if(fragmentStructure.getProperty("FragmentMass") != null && fragmentStructure.getProperty("FragmentMass") != "")
        	fragmentMass = Double.parseDouble(fragmentStructure.getProperty("FragmentMass").toString());
        else
        	fragmentMass = MolecularFormulaTools.getMonoisotopicMass(molecularFormula);
        
        
        String partialChargeDiffString = (String)fragmentStructure.getProperty("PartialChargeDiff");
        int treeDepth = Integer.parseInt((String)fragmentStructure.getProperty("TreeDepth"));
        
        double peakLow = peak.getMass() - this.mzabs - PPMTool.getPPMDeviation(peak.getMass(), this.mzppm);
        double peakHigh = peak.getMass() + this.mzabs + PPMTool.getPPMDeviation(peak.getMass(), this.mzppm);
        double protonMass = Constants.PROTON_MASS * (double)mode;
        double massToCompare = fragmentMass + protonMass;
        double matchedMass = 0.0;
        hydrogenPenalty = 0;
        String molecularFormulaString = "";      
        
        String modeString = (mode > 0) ? " +" : " -";
        
        
        //first case: fragment matches directly peak without modifications
        if((massToCompare >= peakLow && massToCompare <= peakHigh))
        {
        	matchedMass = Math.round(massToCompare*10000.0)/10000.0;
        	hydrogenPenalty = 0;
        	

        	if(this.html)
        		molecularFormulaString = MolecularFormulaManipulator.getHTML(molecularFormula) + modeString + "H";
        	else
        		molecularFormulaString = MolecularFormulaManipulator.getString(molecularFormula) + modeString + "H";
     
        	matchedFragments.add(new MatchedFragment(peak, fragmentMass, matchedMass, fragmentStructure, null, hydrogenPenalty, MoleculeTools.getCombinedEnergy(partialChargeDiffString), molecularFormulaString));
        }
        //try to match the fragment to the peak with varying number of hydrogens
        else
        {
        	matchWithVariableHydrogen(matchedFragments, treeDepth, partialChargeDiffString, mode, molecularFormula, fragmentMass, massToCompare, peak, fragmentStructure, peakLow, peakHigh, null, true);
        }
        
        
        //second case: match the fragment with any combination of neutral losses
        List<List<NeutralLoss[]>> neutralLossCombinations = nlc.getNeutralLossCombinations();
        //iterate over layers
        for (List<NeutralLoss[]> layers : neutralLossCombinations) {
        	//each layer has 1 to many neutral loss rule combinations 
			for (NeutralLoss[] neutralLossRulesToApply : layers) {
				int countNL = 0;
				//save the mass of the fragment with all modifications
				double massToCompareTemp = massToCompare;
				hydrogenPenalty = 0;
				List<List<Integer>> matchedAtomsSMARTS = new ArrayList<List<Integer>>();
				for (int i = 0; i < neutralLossRulesToApply.length; i++) {
					//check for mode when the neutral loss is applied
					if(neutralLossRulesToApply[i].getMode() != mode && neutralLossRulesToApply[i].getMode() != 0)
						break;
					
					//now check for mass, molecular formula and smarts if the neutral loss is possible
					massToCompareTemp = calculateFragmentMassNeutralLoss(neutralLossRulesToApply[i], massToCompareTemp);
					matchedMass = massToCompareTemp;
					if(massToCompareTemp >= peakLow && massToCompareTemp <= peakHigh)
					{						
						//check for molecular formula
						if(MolecularFormulaTools.isPossibleNeutralLoss(MolecularFormulaTools.parseFormula(molecularFormula), neutralLossRulesToApply[i].getElementalComposition()))
						{
							String[] SMARTSStrings = neutralLossRulesToApply[i].getSMARTS();
							//now check smarts...may e different SMARTS given for 1 neutral loss rule
							for (int j = 0; j < SMARTSStrings.length; j++) {
								//check if one of the supplied SMARTS matches
								SMARTSTools st = new SMARTSTools(SMARTSStrings[j], fragmentStructure);
								if(st.isMatched())
								{
									matchedAtomsSMARTS.addAll(st.getMatchedAtoms());
									countNL++;
									break;
								}
							}							
						}
					}
					//try to match the neutral loss with variable hydrogen count
					else
					{
						double fragmentMassTemp = calculateFragmentMassNeutralLoss(neutralLossRulesToApply[i], fragmentMass);
						if(matchWithVariableHydrogen(matchedFragments, treeDepth, partialChargeDiffString, mode, molecularFormula, fragmentMassTemp, massToCompareTemp, peak, fragmentStructure, peakLow, peakHigh, null, false))
						{
							//check for molecular formula
							if(MolecularFormulaTools.isPossibleNeutralLoss(MolecularFormulaTools.parseFormula(molecularFormula), neutralLossRulesToApply[i].getElementalComposition()))
							{
								String[] SMARTSStrings = neutralLossRulesToApply[i].getSMARTS();
								//now check smarts...may e different SMARTS given for 1 neutral loss rule
								for (int j = 0; j < SMARTSStrings.length; j++) {
									//check if one of the supplied SMARTS matches
									SMARTSTools st = new SMARTSTools(SMARTSStrings[j], fragmentStructure);
									if(st.isMatched())
									{
										matchedAtomsSMARTS.addAll(st.getMatchedAtoms());
										countNL++;
										break;
									}
								}							
							}
						}
					}
				}
				
				if(countNL == neutralLossRulesToApply.length)
					matchedFragments.add(new MatchedFragment(peak, fragmentMass, (matchedMass + (this.hydrogenPenalty * Constants.HYDROGEN_MASS)), fragmentStructure, neutralLossRulesToApply , hydrogenPenalty, MoleculeTools.getCombinedEnergy(partialChargeDiffString), molecularFormulaString, matchedAtomsSMARTS));
			}
		}
		
		return matchedFragments;
	}
	
	
	/**
	 * Match with variable hydrogen count the given peak.
	 *
	 * @param matchedFragments the matched fragments
	 * @param treeDepth the tree depth
	 * @param partialChargeDiffString the partial charge diff string
	 * @param mode the mode
	 * @param molecularFormula the molecular formula
	 * @param fragmentMass the fragment mass
	 * @param massToCompare the mass to compare
	 * @param peak the peak
	 * @param fragmentStructure the fragment structure
	 * @param peakLow the peak low
	 * @param peakHigh the peak high
	 * @param neutralLoss the neutral loss
	 * @return if a fragment was matched
	 */
	private boolean matchWithVariableHydrogen(List<MatchedFragment> matchedFragments, int treeDepth, String partialChargeDiffString, int mode, IMolecularFormula molecularFormula, double fragmentMass, double massToCompare, Peak peak, IAtomContainer fragmentStructure, double peakLow, double peakHigh, NeutralLoss[] neutralLoss, boolean addToResultsList)
	{
		double matchedMass;
		String molecularFormulaString;
		boolean found = false;
		
		for(int i= 0; i <= treeDepth; i++)
    	{
    		if(i==0)
    		{
    			//found
    			if(((fragmentMass) >= peakLow && (fragmentMass) <= peakHigh))
    			{
    				hydrogenPenalty = 1;
    				matchedMass = Math.round((fragmentMass)*10000.0)/10000.0;
    				
    				if(this.html)
    	        		molecularFormulaString = MolecularFormulaManipulator.getHTML(molecularFormula);
    	        	else
    	        		molecularFormulaString = MolecularFormulaManipulator.getString(molecularFormula);
    				
    				if(addToResultsList)
    					matchedFragments.add(new MatchedFragment(peak, fragmentMass, matchedMass, fragmentStructure, neutralLoss, hydrogenPenalty, MoleculeTools.getCombinedEnergy(partialChargeDiffString), molecularFormulaString));
    				found = true;
    				
    				break;
    			}
    		}
    		else
    		{
    			double hMass = i * Constants.HYDROGEN_MASS;
    			
    			//found
    			if(((massToCompare - hMass) >= peakLow && (massToCompare - hMass) <= peakHigh))
    			{
    				matchedMass = Math.round((massToCompare-hMass)*10000.0)/10000.0;
    				
    				if(mode == -1)
    					hydrogenPenalty = (i);
    				else
    					hydrogenPenalty = (i + 1);
    				
    				if(this.html)
    	        		molecularFormulaString = MolecularFormulaManipulator.getHTML(molecularFormula) + "-" + (i + 1) + "H";
    	        	else
    	        		molecularFormulaString = MolecularFormulaManipulator.getString(molecularFormula) + "-" + (i + 1) + "H";
    				
    				if(addToResultsList)
    					matchedFragments.add(new MatchedFragment(peak, fragmentMass, matchedMass, fragmentStructure, neutralLoss, hydrogenPenalty, MoleculeTools.getCombinedEnergy(partialChargeDiffString), molecularFormulaString));
    				found = true;
    				
    				break;
    			}
    			else if(((massToCompare + hMass) >= peakLow && (massToCompare + hMass) <= peakHigh))
    			{
    				matchedMass = Math.round((massToCompare+hMass)*10000.0)/10000.0;
    				//now add a bond energy equivalent to a H-C bond
    				if(mode == 1)
    					hydrogenPenalty = (i);
    				else
    					hydrogenPenalty = (i + 1);
    				
    				
    				if(this.html)
    	        		molecularFormulaString = MolecularFormulaManipulator.getHTML(molecularFormula) + "+" + (i + 1) + "H" + neutralLoss;
    	        	else
    	        		molecularFormulaString = MolecularFormulaManipulator.getString(molecularFormula) + "+" + (i + 1) + "H" + neutralLoss;
    				
    				if(addToResultsList)
    					matchedFragments.add(new MatchedFragment(peak, fragmentMass, matchedMass, fragmentStructure, neutralLoss, hydrogenPenalty, MoleculeTools.getCombinedEnergy(partialChargeDiffString), molecularFormulaString));
    				found = true;
    				
    				break;
    			}
    		}	
    	}
		
		return found;
	}
	
	
	
	/**
	 * Calculate the fragment mass after the neutral loss is applied.
	 *
	 * @param nl the nl
	 * @param fragmentMass the fragment mass
	 * @return the double
	 */
	private double calculateFragmentMassNeutralLoss(NeutralLoss nl, Double fragmentMass)
	{
		double ret = fragmentMass + (nl.getHydrogenDifference() * Constants.HYDROGEN_MASS) - nl.getMass();
		return ret;
	}
	
	
	/**
	 * Gets the hits.
	 * 
	 * @return the hits
	 */
	public Vector<MatchedFragment> getHits()
	{
		return this.hits;
	}
	
	/**
	 * Gets the all hits. (with all possibilities)
	 * 
	 * @return the all hits
	 */
	public Vector<MatchedFragment> getAllHits()
	{
		return this.hitsAll;
	}
	
	/**
	 * Gets the hits (mz).
	 * 
	 * @return the hits (mz)
	 */
	public Vector<Double> getHitsMZ()
	{
		return this.hitsPeaks;
	}
}
