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

import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.formula.MolecularFormula;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObject;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

import de.ipbhalle.metfrag.main.MetFragPreCalculated;
import de.ipbhalle.metfrag.massbankParser.Peak;
import de.ipbhalle.metfrag.tools.Constants;
import de.ipbhalle.metfrag.tools.MolecularFormulaTools;
import de.ipbhalle.metfrag.tools.MoleculeTools;
import de.ipbhalle.metfrag.tools.PPMTool;
import de.ipbhalle.metfrag.tools.SMARTSTools;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
	public void assignFragmentPeak(List<IAtomContainer> acs, Vector<Peak> peakList, double mzabs, double mzppm, int mode, boolean html, boolean isPositive) throws CDKException, IOException
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
		
		List<String> explainedPeak = new ArrayList<String>();
		explainedPeak.add("m/z\tbondOrder\tbondLength\tbondEnergy");
		
		List<String> notExplainedPeak = new ArrayList<String>();
		notExplainedPeak.add("m/z\tbondOrder\tbondLength\tbondEnergy");
		
		for (int i=0; i< peakList.size(); i++)
		{
			//System.out.println("Peak: " + this.peakList.get(i).getMass() + " ====================================");
			boolean test = true;
			List<MatchedFragment> fragmentsMatched = new ArrayList<MatchedFragment>();
			for (int j = 0; j < this.acs.size(); j++) {
				//match peaks
				List<MatchedFragment> list = matchPeak(this.acs.get(j), this.peakList.get(i), mode, neutralLossCombination, isPositive);
				fragmentsMatched.addAll(list);
			}
			
			if(fragmentsMatched.size() == 0)
				continue;
			
			//now find the best hit for the peak
			//loop over every matched fragment and find the smallest bondorder
			double minValue = 2;
			
			Map<Double, List<MatchedFragment>> valueToFragment = new HashMap<Double, List<MatchedFragment>>();
			for (MatchedFragment matchedFragment : fragmentsMatched) {
				double value = matchedFragment.getBondOrder();
				if(value < minValue)
					minValue = value;

				if(valueToFragment.containsKey(value))
				{
					List<MatchedFragment> tmp = valueToFragment.get(value);
					tmp.add(matchedFragment);
					valueToFragment.put(value, tmp);
				}
				else
				{
					List<MatchedFragment> list = new ArrayList<MatchedFragment>();
					list.add(matchedFragment);
					valueToFragment.put(value, list);
				}
			}
			
			Double[] array = new Double[valueToFragment.size()];
			array = valueToFragment.keySet().toArray(array);
			Arrays.sort(array);
			List<MatchedFragment> bestHits = valueToFragment.get(array[array.length - 1]); 
			
			//if there are several hits with the same bondorder use the one with the least hydrogen penalty
			int minHydrogenPenalty = 10;
			MatchedFragment bestHit = null;
			for (MatchedFragment matchedFragment : bestHits) {
				if(matchedFragment.getHydrogenPenalty() < minHydrogenPenalty)
				{
					minHydrogenPenalty = matchedFragment.getHydrogenPenalty();
					bestHit = matchedFragment;
				}
			}
			
			hits.add(bestHit);
			hitsPeaks.add(this.peakList.get(i).getMass());
			hitsAll.addAll(fragmentsMatched);
		}
		Map<Double, MatchedFragment> peakAnnotated = new HashMap<Double, MatchedFragment>();
		for (MatchedFragment mf : hits) {
			peakAnnotated.put(mf.getFragmentMass(), mf);
		}
		
//		String header = "fragmentMass\tbondOrder\tbondLengthChange\tBDE\tbondsRemoved";
//		MetFragPreCalculated.results.setPositivePeaks(MetFragPreCalculated.results.getPositivePeaks().append(header + "\tpeak\n"));
//		MetFragPreCalculated.results.setNegativePeaks(MetFragPreCalculated.results.getNegativePeaks().append(header + "\n"));
		
		for (int j = 0; j < this.acs.size(); j++) {
			
			//skip original molecule
			if(j == 0)
				continue;
			
			double fragmentMass = 0.0;
			if(this.acs.get(j).getProperty("FragmentMass") != null && this.acs.get(j).getProperty("FragmentMass") != "")
	        	fragmentMass = Double.parseDouble(this.acs.get(j).getProperty("FragmentMass").toString());
	        else
	        {
	        	fragmentMass = MolecularFormulaTools.getMonoisotopicMass(MolecularFormulaManipulator.getMolecularFormula(this.acs.get(j)));
	        }
			
//			if(peakAnnotated.containsKey(fragmentMass))
//			{				
//				MetFragPreCalculated.results.setPositivePeaks(MetFragPreCalculated.results.getPositivePeaks().append(fragmentMass + "\t" + this.acs.get(j).getProperty(Constants.BONDORDER) + "\t" + this.acs.get(j).getProperty(Constants.BONDLENGTHCHANGE) + "\t" + this.acs.get(j).getProperty(Constants.BDE) + "\t" + this.acs.get(j).getProperty(Constants.BONDREMOVED) + "\t" + peakAnnotated.get(fragmentMass).getPeak().getMass() + "\n"));
//			}
//			else
//			{
//				MetFragPreCalculated.results.setNegativePeaks(MetFragPreCalculated.results.getNegativePeaks().append(fragmentMass + "\t" + this.acs.get(j).getProperty(Constants.BONDORDER) + "\t" + this.acs.get(j).getProperty(Constants.BONDLENGTHCHANGE) + "\t" + this.acs.get(j).getProperty(Constants.BDE) + "\t" + this.acs.get(j).getProperty(Constants.BONDREMOVED) + "\n"));
//			}
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
	private List<MatchedFragment> matchPeak(IAtomContainer fragmentStructure, Peak peak, int mode, int neutralLossCombination, boolean isPositive) throws IOException, CDKException
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
        
        
        int treeDepth = Integer.parseInt((String)fragmentStructure.getProperty(Constants.TREEDEPTH));
        
        String modeString = "";
        double massForMode = 0.0;
        if(mode == 1)
        {
            modeString = " +H";
            massForMode = Constants.HYDROGEN_MASS - Constants.ELECTRON_MASS;
        }
        else if(mode == -1)
        {
        	modeString = " -H";
        	massForMode = (-1.0 * (Constants.HYDROGEN_MASS)) + Constants.ELECTRON_MASS;
        }
        else if(mode == 0)
        {
        	modeString = " ";
        	if(isPositive)
        		massForMode = Constants.ELECTRON_MASS;
        	else
        		massForMode = -1.0 * (Constants.ELECTRON_MASS);
        }

        double peakLow = peak.getMass() - this.mzabs - PPMTool.getPPMDeviation(peak.getMass(), this.mzppm);
        double peakHigh = peak.getMass() + this.mzabs + PPMTool.getPPMDeviation(peak.getMass(), this.mzppm);
        double massToCompare = fragmentMass + massForMode;
        double matchedMass = 0.0;
        hydrogenPenalty = 0;
        String molecularFormulaString = ""; 
        SMARTSTools st = new SMARTSTools(fragmentStructure);
        
        
        //first case: fragment matches directly peak without modifications
        if((massToCompare >= peakLow && massToCompare <= peakHigh))
        {
        	matchedMass = Math.round(massToCompare*10000.0)/10000.0;
        	hydrogenPenalty = 0;
        	

        	if(this.html)
        		molecularFormulaString = MolecularFormulaManipulator.getHTML(molecularFormula) + modeString;
        	else
        		molecularFormulaString = MolecularFormulaManipulator.getString(molecularFormula) + modeString;
     
        	matchedFragments.add(new MatchedFragment(peak, fragmentMass, matchedMass, fragmentStructure, null, hydrogenPenalty, molecularFormulaString));
        }
        //try to match the fragment to the peak with varying number of hydrogens
        else
        {
        	matchWithVariableHydrogen(matchedFragments, treeDepth, mode, molecularFormula, fragmentMass, massToCompare, peak, fragmentStructure, peakLow, peakHigh, null, true, isPositive);
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
								st.matchSMARTS(SMARTSStrings[j]);
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
						if(matchWithVariableHydrogen(matchedFragments, treeDepth, mode, molecularFormula, fragmentMassTemp, massToCompareTemp, peak, fragmentStructure, peakLow, peakHigh, null, false, isPositive))
						{
							//check for molecular formula
							if(MolecularFormulaTools.isPossibleNeutralLoss(MolecularFormulaTools.parseFormula(molecularFormula), neutralLossRulesToApply[i].getElementalComposition()))
							{
								String[] SMARTSStrings = neutralLossRulesToApply[i].getSMARTS();
								//now check smarts...may e different SMARTS given for 1 neutral loss rule
								for (int j = 0; j < SMARTSStrings.length; j++) {
									//check if one of the supplied SMARTS matches
									st.matchSMARTS(SMARTSStrings[j]);
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
				{
					Double maxBondLengthChangeNL = 0.0;
					Double minBondOrderNL = 2.0;
					//now get the bond length changes
					for (List<Integer> list : matchedAtomsSMARTS) {
						List<Integer> matched = list;
						List<IAtom> atomsMatched = new ArrayList<IAtom>();
			    		for (Integer integer : matched) {
			    			try
			    			{
			    				//acs position 0 is the original candidate molecule
			    				atomsMatched.add(fragmentStructure.getAtom(integer));
//			    				System.out.println(fragmentStructure.getAtom(integer).getSymbol());
			    			}
			    			catch(IndexOutOfBoundsException e)
			    			{
			    				System.err.println("Cannot highlight removed hydrogen!");
			    			}
			    						   
			    		}
			            
			    		
			    		List<IBond> bondsToBreak = new ArrayList<IBond>();
			            for (IAtom atom : atomsMatched) {
			            	for (IAtom atom2 : atomsMatched) {
			            		
			                	IBond bond = fragmentStructure.getBond(atom, atom2);
			                	if(bond!= null)
			                		bondsToBreak.add(bond);
			                }            	
			            }
			            Double bondLengthChangeNL = 0.0;
			            Double bondOrderNL = 2.0;
			            List<IAtom> foundAtoms = new ArrayList<IAtom>();
			            for (int i = 0; i < neutralLossRulesToApply.length; i++) {
			            	List<String> mainAtoms = neutralLossRulesToApply[i].getMainAtoms();
			            	for (String mainAtom : mainAtoms) {
								for (IBond bond : bondsToBreak) {
									
									IAtom atom1 = AtomContainerManipulator.getAtomById(this.acs.get(0), bond.getAtom(0).getID());
									IAtom atom2 = AtomContainerManipulator.getAtomById(this.acs.get(0), bond.getAtom(1).getID());
									IBond bondOrig = this.acs.get(0).getBond(atom1, atom2);
									
									if(bond.getAtom(0).getSymbol().equals(mainAtom) && !foundAtoms.contains(bond.getAtom(0)))
									{
										foundAtoms.add(bond.getAtom(0));
										bondLengthChangeNL = Double.parseDouble((String)bondOrig.getProperty(Constants.BONDLENGTHCHANGE));
										bondOrderNL = Double.parseDouble((String)bondOrig.getProperty(Constants.BONDORDER));
										break;
									}
									else if(bond.getAtom(1).getSymbol().equals(mainAtom) && !foundAtoms.contains(bond.getAtom(1)))
									{
										foundAtoms.add(bond.getAtom(1));
										bondLengthChangeNL = Double.parseDouble((String)bondOrig.getProperty(Constants.BONDLENGTHCHANGE));
										bondOrderNL = Double.parseDouble((String)bondOrig.getProperty(Constants.BONDORDER));
										break;
									}
									
								}
							}
						}
			            if(maxBondLengthChangeNL < bondLengthChangeNL)
			            	maxBondLengthChangeNL = bondLengthChangeNL;
			            
			            if(minBondOrderNL > bondOrderNL)
			            	minBondOrderNL = bondOrderNL;
		        	}
		        	
					if(this.html)
		        		molecularFormulaString = MolecularFormulaManipulator.getHTML(molecularFormula) + modeString;
		        	else
		        		molecularFormulaString = MolecularFormulaManipulator.getString(molecularFormula) + modeString;
					
					
					IAtomContainer fragmentStructureTemp = new AtomContainer(fragmentStructure);
					fragmentStructureTemp.setProperties(fragmentStructure.getProperties());
					fragmentStructureTemp.setProperty(Constants.BONDLENGTHCHANGE, fragmentStructure.getProperty(Constants.BONDLENGTHCHANGE) + ";" + maxBondLengthChangeNL);
					fragmentStructureTemp.setProperty(Constants.BONDORDER, fragmentStructure.getProperty(Constants.BONDORDER) + ";" + minBondOrderNL);
					
					MatchedFragment mf = new MatchedFragment(peak, fragmentMass, (matchedMass + (this.hydrogenPenalty * Constants.HYDROGEN_MASS)), fragmentStructureTemp, neutralLossRulesToApply , hydrogenPenalty, molecularFormulaString, matchedAtomsSMARTS);
					
					
					matchedFragments.add(mf);
				}
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
	private boolean matchWithVariableHydrogen(List<MatchedFragment> matchedFragments, int treeDepth, int mode, IMolecularFormula molecularFormula, double fragmentMass, double massToCompare, Peak peak, IAtomContainer fragmentStructure, double peakLow, double peakHigh, NeutralLoss[] neutralLoss, boolean addToResultsList, boolean isPositive)
	{
		double matchedMass;
		String molecularFormulaString = "";
		boolean found = false;
		hydrogenPenalty = 0;
		
		for(int i= 1; i <= treeDepth; i++)
    	{
			double hMass = i * Constants.HYDROGEN_MASS;
			//now add a bond energy equivalent to a H-C bond
			hydrogenPenalty = i;
			
			//found
			if(((massToCompare - hMass) >= peakLow && (massToCompare - hMass) <= peakHigh))
			{
				found = true;
				matchedMass = Math.round((massToCompare-hMass)*10000.0)/10000.0;
								
				String hydrogenCountString = Integer.toString(i);
				//i+1 because the other hydrogen is from the mode!
				if(mode == -1)
					hydrogenCountString = Integer.toString(i + 1);
				else if(mode == 1)
					hydrogenCountString = Integer.toString(i - 1);
				
				//positive mode...no hydrogen added for i == 1
				if(mode == 1 && i == 1)
				{
					//reduce the hydrogen
    				if(this.html)
    	        		molecularFormulaString = MolecularFormulaManipulator.getHTML(molecularFormula);
    	        	else
    	        		molecularFormulaString = MolecularFormulaManipulator.getString(molecularFormula);
				}
				else
				{
    				if(this.html)
    					molecularFormulaString = MolecularFormulaManipulator.getHTML(molecularFormula) + "-" + hydrogenCountString + "H";
    	        	else
    	        		molecularFormulaString = MolecularFormulaManipulator.getString(molecularFormula) + "-" + hydrogenCountString + "H";
				}
				
				
				if(addToResultsList)
					matchedFragments.add(new MatchedFragment(peak, fragmentMass, matchedMass, fragmentStructure, neutralLoss, hydrogenPenalty, molecularFormulaString));
				found = true;
				
				break;
    		}
			else if(((massToCompare + hMass) >= peakLow && (massToCompare + hMass) <= peakHigh))
			{
				found = true;
				matchedMass = Math.round((massToCompare+hMass)*10000.0)/10000.0;
				
				String hydrogenCountString = Integer.toString(i);
				//i+1 because the other hydrogen is from the mode!
				if(mode == 1)
					hydrogenCountString = Integer.toString(i + 1);
				else if(mode == -1)
					hydrogenCountString = Integer.toString(i - 1);
					
				//negative mode...no hydrogen added for i == 1
				if(mode == -1 && i == 1)
				{
					//reduce the hydrogen
    				if(this.html)
    	        		molecularFormulaString = MolecularFormulaManipulator.getHTML(molecularFormula);
    	        	else
    	        		molecularFormulaString = MolecularFormulaManipulator.getString(molecularFormula);
				}
				else
				{
    				if(this.html)
    					molecularFormulaString = MolecularFormulaManipulator.getHTML(molecularFormula) + "+" + hydrogenCountString + "H";
    	        	else
    	        		molecularFormulaString = MolecularFormulaManipulator.getString(molecularFormula) + "+" + hydrogenCountString + "H";
				}
				
				
    				if(addToResultsList)
    					matchedFragments.add(new MatchedFragment(peak, fragmentMass, matchedMass, fragmentStructure, neutralLoss, hydrogenPenalty, molecularFormulaString));
    				found = true;
    				
    				break;
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
//		double ret = fragmentMass + (nl.getHydrogenDifference() * Constants.HYDROGEN_MASS) - nl.getMass();
		double ret = fragmentMass - nl.getMass();
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

