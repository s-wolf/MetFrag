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
import java.util.List;
import java.util.Vector;

public class AssignFragmentPeak {
	
	private List<IAtomContainer> acs;
	private Vector<Peak> peakList;
	private Vector<PeakMolPair> hits;
	private Vector<PeakMolPair> hitsAll;
	private Vector<Double> hitsPeaks;
	//private Vector<PeakMolPair> noHits; TODO: for log file
	private double mzabs;
	private double mzppm;
	private static final double hydrogenMass = MolecularFormulaTools.getMonoisotopicMass("H1");
	private boolean hydrogenTest = true;
	private Double matchedMass = 0.0;
	private String molecularFormula = "";
	private boolean html = false;
	private int hydrogenPenalty = 0;
	private String partialChargeDiff;
	
	
	public AssignFragmentPeak()
	{
		
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
	 * @param isPositive is positive mode (is used for [M+]+ or [M-]-)
	 * @throws CDKException the CDK exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void assignFragmentPeak(List<IAtomContainer> acs, Vector<Peak> peakList, double mzabs, double mzppm, int mode, boolean html, boolean isPositive) throws CDKException, IOException
	{
		this.acs = acs;
		this.peakList = peakList;
		this.mzabs = mzabs;
		this.mzppm = mzppm;
		this.hits = new Vector<PeakMolPair>();
		this.hitsAll = new Vector<PeakMolPair>();
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
				if(matchByMass(this.acs.get(j), this.peakList.get(i).getMass(), mode, isPositive))
				{
					//add hits to list...only 1...check if this found hydrogen penalty is less than the previous found one
					if(test || hits.lastElement().getHydrogenPenalty() > this.hydrogenPenalty)
					{
						//exchange the last element...this is the one with the current peak
						if(!test && hits.size() > 0 && hits.lastElement().getHydrogenPenalty() > this.hydrogenPenalty)
							hits.remove(hits.size() - 1);
						hits.add(new PeakMolPair(acs.get(j),this.peakList.get(i), this.matchedMass, this.molecularFormula, this.hydrogenPenalty, this.partialChargeDiff));
						hitsPeaks.add(this.peakList.get(i).getMass());
						test = false;
					}
					hitsAll.add(new PeakMolPair(acs.get(j),this.peakList.get(i), this.matchedMass, this.molecularFormula, this.hydrogenPenalty, this.partialChargeDiff));
				}
			}
		}
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
	private boolean matchByMass(IAtomContainer ac, double peak, int mode, boolean isPositive) throws CDKException, IOException
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
        
        String modeString = "";
        double massForMode = 0.0;
        if(mode == 1)
        {
            modeString = " +";
            massForMode = Constants.HYDROGEN_MASS - Constants.ELECTRON_MASS;
        }
        else if(mode == -1)
        {
        	modeString = " -";
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
        
        
        double peakLow = peak - this.mzabs - PPMTool.getPPMDeviation(peak, this.mzppm);
        double peakHigh = peak + this.mzabs + PPMTool.getPPMDeviation(peak, this.mzppm);
        double massToCompare = mass + massForMode;
        
        String neutralLoss = "";
    	if(ac.getProperty("NlElementalComposition") != null && ac.getProperty("NlElementalComposition") != "")
    		neutralLoss = " -" + ac.getProperty("NlElementalComposition");
    
        
        if((massToCompare >= peakLow && massToCompare <= peakHigh))
        {
        	found = true;
        	matchedMass = Math.round(massToCompare*10000.0)/10000.0;
        	this.hydrogenPenalty = 0;
        	
        	if(this.html)
        		this.molecularFormula = MolecularFormulaManipulator.getHTML(molecularFormula) + modeString + "H" + neutralLoss;
        	else
        		this.molecularFormula = MolecularFormulaManipulator.getString(molecularFormula) + modeString + "H" + neutralLoss;
        	
        	//System.out.println("HIT!" + (double)Math.round(((mass+protonMass)-peak) * 10000)/10000 + " Mass: " + (double)Math.round((mass + protonMass)* 10000)/10000 + " " + MolecularFormulaManipulator.getString(molecularFormula) + " " + mode + "H Error: " + (double)Math.round((Math.abs((mass+protonMass)-peak)*10000))/10000);
        }
        //now try to decrease the hydrogens...at most the treedepth
        if(hydrogenTest && !found)
        {
        	int treeDepth = Integer.parseInt((String)ac.getProperty("TreeDepth"));
        	for(int i= 1; i <= treeDepth; i++)
        	{
    			double hMass = i * Constants.HYDROGEN_MASS;
    			
    			//found
    			if(((massToCompare - hMass) >= peakLow && (massToCompare - hMass) <= peakHigh))
    			{
    				found = true;
    				matchedMass = Math.round((massToCompare-hMass)*10000.0)/10000.0;
    				
    				//now add a bond energy equivalent to a H-C bond
    				this.hydrogenPenalty = i;
    				
    				String hydrogenCountString = Integer.toString(i);
    				//i+1 because the other hydrogen is from the mode!
    				if(mode == -1)
    					hydrogenCountString = Integer.toString(i + 1);
    				else if(mode == 1)
    					hydrogenCountString = Integer.toString(i - 1);
    				
    				//positive mode...no hydrogen added for i == 1
    				else if(mode == 1 && i == 1)
    				{
    					//reduce the hydrogen
        				if(this.html)
        	        		this.molecularFormula = MolecularFormulaManipulator.getHTML(molecularFormula) + neutralLoss;
        	        	else
        	        		this.molecularFormula = MolecularFormulaManipulator.getString(molecularFormula) + neutralLoss;
    				}
    				else
    				{
        				if(this.html)
        	        		this.molecularFormula = MolecularFormulaManipulator.getHTML(molecularFormula) + "-" + hydrogenCountString + "H" + neutralLoss;
        	        	else
        	        		this.molecularFormula = MolecularFormulaManipulator.getString(molecularFormula) + "-" + hydrogenCountString + "H" + neutralLoss;
    				}
    				
    				
    				
    				
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
    				else if(mode == -1 && i == 1)
    				{
    					//reduce the hydrogen
        				if(this.html)
        	        		this.molecularFormula = MolecularFormulaManipulator.getHTML(molecularFormula) + neutralLoss;
        	        	else
        	        		this.molecularFormula = MolecularFormulaManipulator.getString(molecularFormula) + neutralLoss;
    				}
    				else
    				{
        				if(this.html)
        	        		this.molecularFormula = MolecularFormulaManipulator.getHTML(molecularFormula) + "+" + hydrogenCountString + "H" + neutralLoss;
        	        	else
        	        		this.molecularFormula = MolecularFormulaManipulator.getString(molecularFormula) + "+" + hydrogenCountString + "H" + neutralLoss;
    				}
    				
    				break;
    			}
        	}
        }
        		
		return found;
	}
	

	/**
	 * Gets the hits.
	 * 
	 * @return the hits
	 */
	public Vector<PeakMolPair> getHits()
	{
		return this.hits;
	}
	
	/**
	 * Gets the all hits. (with all possibilities)
	 * 
	 * @return the all hits
	 */
	public Vector<PeakMolPair> getAllHits()
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
