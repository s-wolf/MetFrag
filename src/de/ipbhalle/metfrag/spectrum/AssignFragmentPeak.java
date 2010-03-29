package de.ipbhalle.metfrag.spectrum;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.formula.MolecularFormula;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

import de.ipbhalle.metfrag.classifier.BayesTraining;
import de.ipbhalle.metfrag.fragmenter.NeutralLoss;
import de.ipbhalle.metfrag.main.PeakMolPair;
import de.ipbhalle.metfrag.massbankParser.Peak;
import de.ipbhalle.metfrag.tools.MolecularFormulaTools;
import de.ipbhalle.metfrag.tools.PPMTool;
import de.ipbhalle.metfrag.tools.Render;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	private BayesTraining trainingTable = new BayesTraining();
	private boolean hydrogenTest = true;
	private Double matchedMass = 0.0;
	private String molecularFormula = "";
	private boolean html = false;
	private double hydrogenPenalty = 0.0;
	
	
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
				if(matchByMass(this.acs.get(j), this.peakList.get(i).getMass(), mode))
				{
					//add hits to list...only 1...check if this found hydrogen penalty is less than the previous found one
					if(test || hits.lastElement().getHydrogenPenalty() > this.hydrogenPenalty)
					{
						//exchange the last element...this is the one with the current peak
						if(!test && hits.size() > 0 && hits.lastElement().getHydrogenPenalty() > this.hydrogenPenalty)
							hits.remove(hits.size() - 1);
						hits.add(new PeakMolPair(acs.get(j),this.peakList.get(i), this.matchedMass, this.molecularFormula, this.hydrogenPenalty));
						hitsPeaks.add(this.peakList.get(i).getMass());
						test = false;
					}
					hitsAll.add(new PeakMolPair(acs.get(j),this.peakList.get(i), this.matchedMass, this.molecularFormula, this.hydrogenPenalty));
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
        
        double peakLow = peak - this.mzabs - PPMTool.getPPMDeviation(peak, this.mzppm);
        double peakHigh = peak + this.mzabs + PPMTool.getPPMDeviation(peak, this.mzppm);
        double protonMass = hydrogenMass * (double)mode;
        double massToCompare = mass+protonMass;
        
        String neutralLoss = "";
    	if(ac.getProperty("NlElementalComposition") != null && ac.getProperty("NlElementalComposition") != "")
    		neutralLoss = " -" + ac.getProperty("NlElementalComposition");
    
        
        String modeString = (mode > 0) ? " +" : " -";
        
        if((massToCompare >= peakLow && massToCompare <= peakHigh))
        {
        	found = true;
        	matchedMass = Math.round(massToCompare*10000.0)/10000.0;
        	this.hydrogenPenalty = 0;
        	
        	if(this.html)
        		this.molecularFormula = MolecularFormulaManipulator.getHTML(molecularFormula) + modeString + "H" + neutralLoss + "BDE:" + ac.getProperty("BondEnergy");
        	else
        		this.molecularFormula = MolecularFormulaManipulator.getString(molecularFormula) + modeString + "H" + neutralLoss+ "BDE:" + ac.getProperty("BondEnergy");
        	
        	//System.out.println("HIT!" + (double)Math.round(((mass+protonMass)-peak) * 10000)/10000 + " Mass: " + (double)Math.round((mass + protonMass)* 10000)/10000 + " " + MolecularFormulaManipulator.getString(molecularFormula) + " " + mode + "H Error: " + (double)Math.round((Math.abs((mass+protonMass)-peak)*10000))/10000);
        }
        //now try to decrease the hydrogens...at most the treedepth
        if(hydrogenTest && !found)
        {
        	int treeDepth = Integer.parseInt((String)ac.getProperty("TreeDepth"));
        	for(int i= 0; i <= treeDepth; i++)
        	{
        		if(i==0)
        		{
        			//found
        			if(((mass) >= peakLow && (mass) <= peakHigh))
        			{
        				
        				//now add a bond energy equivalent to a H-C bond
        				this.hydrogenPenalty = 1000;
        				
        				
        				found = true;
        				matchedMass = Math.round((mass)*10000.0)/10000.0;
        				
        				if(this.html)
        	        		this.molecularFormula = MolecularFormulaManipulator.getHTML(molecularFormula) + neutralLoss;
        	        	else
        	        		this.molecularFormula = MolecularFormulaManipulator.getString(molecularFormula) + neutralLoss;
        				
        				break;
        			}
        		}
        		else
        		{
        			double hMass = i * protonMass;
        			
        			//found
        			if(((massToCompare - hMass) >= peakLow && (massToCompare - hMass) <= peakHigh))
        			{
        				found = true;
        				matchedMass = Math.round((massToCompare-hMass)*10000.0)/10000.0;
        				
        				//now add a bond energy equivalent to a H-C bond
        				this.hydrogenPenalty = (i * 1000) + 1000;
        				
        				if(this.html)
        	        		this.molecularFormula = MolecularFormulaManipulator.getHTML(molecularFormula) + "-" + (i + 1) + "H";
        	        	else
        	        		this.molecularFormula = MolecularFormulaManipulator.getString(molecularFormula) + "-" + (i + 1) + "H";
        				
        				
        				break;
        			}
        			else if(((massToCompare + hMass) >= peakLow && (massToCompare + hMass) <= peakHigh))
        			{
        				found = true;
        				matchedMass = Math.round((massToCompare+hMass)*10000.0)/10000.0;
        				//now add a bond energy equivalent to a H-C bond
        				this.hydrogenPenalty = (i * 1000) + 1000;
        				
        				
        				if(this.html)
        	        		this.molecularFormula = MolecularFormulaManipulator.getHTML(molecularFormula) + "+" + (i + 1) + "H" + neutralLoss;
        	        	else
        	        		this.molecularFormula = MolecularFormulaManipulator.getString(molecularFormula) + "+" + (i + 1) + "H" + neutralLoss;
        				
        				break;
        			}
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
