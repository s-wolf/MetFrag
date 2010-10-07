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
import de.ipbhalle.metfrag.tools.PPMTool;

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
	private boolean hydrogenTest = true;
	private boolean html;
	
	
	/**
	 * Match fragment with peak. Assigns peaks to the fragments.
	 * 
	 * @param acs the acs
	 * @param peakList the peak list
	 * @param mzabs the mzabs
	 * @param mzppm the mzppm
	 * @param mode the mode
	 * @param hydrogenTest the hydrogen test
	 * @param html the html
	 * 
	 * @throws CDKException the CDK exception
	 * @throws IOException 
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
				if(matchByMass(this.acs.get(j), this.peakList.get(i).getMass(), mode))
				{
					//add hits to list...only 1...check if this found hydrogen penalty is less than the previous found one
					if(test || hits.lastElement().getHydrogenPenalty() > this.hydrogenPenalty)
					{
						//exchange the last element...this is the one with the current peak
						if(!test && hits.size() > 0 && hits.lastElement().getHydrogenPenalty() > this.hydrogenPenalty)
							hits.remove(hits.size() - 1);
						hits.add(new MatchedFragment(acs.get(j),this.peakList.get(i), this.matchedMass, this.molecularFormula, this.hydrogenPenalty, this.partialChargeDiff));
						hitsPeaks.add(this.peakList.get(i).getMass());
						test = false;
					}
					hitsAll.add(new PeakMolPair(acs.get(j),this.peakList.get(i), this.matchedMass, this.molecularFormula, this.hydrogenPenalty, this.partialChargeDiff));
				}
			}
		}
	}
	
	
	private List<MatchedFragment> matchPeak(IAtomContainer fragmentStructure, Peak peak, int mode)
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
        
        
        String partialChargeDiff = (String)fragmentStructure.getProperty("PartialChargeDiff");
        
        
        double peakLow = peak.getMass() - this.mzabs - PPMTool.getPPMDeviation(peak.getMass(), this.mzppm);
        double peakHigh = peak.getMass() + this.mzabs + PPMTool.getPPMDeviation(peak.getMass(), this.mzppm);
        double protonMass = Constants.PROTON_MASS * (double)mode;
        double massToCompare = fragmentMass + protonMass;
        double matchedMass = 0.0;
        int hydrogenPenalty = 0;
        String molecularFormulaString;
        
        boolean explained = true;        
        
        String modeString = (mode > 0) ? " +" : " -";
        
        
        //first case: fragment matches directly peak without modifications
        if((massToCompare >= peakLow && massToCompare <= peakHigh))
        {
        	explained = true;
        	matchedMass = Math.round(massToCompare*10000.0)/10000.0;
        	hydrogenPenalty = 0;
        	

        	if(this.html)
        		molecularFormulaString = MolecularFormulaManipulator.getHTML(molecularFormula) + modeString + "H";
        	else
        		molecularFormulaString = MolecularFormulaManipulator.getString(molecularFormula) + modeString + "H";
     
        	matchedFragments.add(new MatchedFragment(peak, fragmentMass, matchedMass, fragmentStructure, null, hydrogenPenalty, partialChargeDiff, molecularFormulaString));
        }
        
        //second case: match the fragment with any combination of neutral losses
        
		
		
		
		return mf;
	}
	
	/**
	 * Match by mass.
	 * 
	 * @param ac the ac
	 * @param peak the peak
	 * 
	 * @return true, if successful
	 * 
	 * @throws CDKException the CDK exception
	 * @throws IOException 
	 */
	private boolean matchByMass(IAtomContainer ac, double peak, int mode) throws CDKException, IOException
	{
		boolean found = false;
		
		IMolecularFormula molecularFormula = new MolecularFormula();
        molecularFormula = MolecularFormulaManipulator.getMolecularFormula(ac, molecularFormula);         
        double mass = 0.0;
        //speed up and neutral loss matching!
        if(ac.getProperty("FragmentMass") != null && ac.getProperty("FragmentMass") != "")
        	mass = Double.parseDouble(ac.getProperty("FragmentMass").toString());
        else
        	mass = MolecularFormulaTools.getMonoisotopicMass(molecularFormula);
        
        
        this.partialChargeDiff = (String)ac.getProperty("PartialChargeDiff");
        
        
        double peakLow = peak - this.mzabs - PPMTool.getPPMDeviation(peak, this.mzppm);
        double peakHigh = peak + this.mzabs + PPMTool.getPPMDeviation(peak, this.mzppm);
        double protonMass = Constants.PROTON_MASS * (double)mode;
        double massToCompare = mass + protonMass;
        
        
        String modeString = (mode > 0) ? " +" : " -";
        
        
        
        
        if((massToCompare >= peakLow && massToCompare <= peakHigh))
        {
        	found = true;
        	matchedMass = Math.round(massToCompare*10000.0)/10000.0;
        	this.hydrogenPenalty = 0;
        	
        	if(this.html)
        		this.molecularFormula = MolecularFormulaManipulator.getHTML(molecularFormula) + modeString + "H";
        	else
        		this.molecularFormula = MolecularFormulaManipulator.getString(molecularFormula) + modeString + "H";
        	
        }
        
        //TODO: now do the neutral loss matching
        Map<Double, IAtomContainer> 
        
        
        		
		return found;
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
	
	/**
	 * Sets the hydrogen test.
	 * 
	 * @param set the new hydrogen test
	 */
	public void setHydrogenTest(boolean set)
	{
		this.hydrogenTest = set;
	}

}
