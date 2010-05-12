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

package de.ipbhalle.metfrag.fragmenter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.graph.ConnectivityChecker;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.MDLReader;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;

import de.ipbhalle.metfrag.chemspiderClient.ChemSpider;
import de.ipbhalle.metfrag.fragmenter.Fragmenter;
import de.ipbhalle.metfrag.keggWebservice.KeggWebservice;
import de.ipbhalle.metfrag.main.MetFrag;
import de.ipbhalle.metfrag.massbankParser.Peak;
import de.ipbhalle.metfrag.pubchem.PubChemWebService;
import de.ipbhalle.metfrag.scoring.Scoring;
import de.ipbhalle.metfrag.spectrum.AssignFragmentPeak;
import de.ipbhalle.metfrag.spectrum.CleanUpPeakList;
import de.ipbhalle.metfrag.spectrum.PeakMolPair;
import de.ipbhalle.metfrag.spectrum.WrapperSpectrum;

public class FragmenterThread implements Runnable{
	
	private String database = null;
	private PubChemWebService pw = null;
	private String candidate = null;
	private double mzabs;
	private double mzppm;
	private boolean sumFormulaRedundancyCheck = true;
	private boolean breakAromaticRings = true;
	private int treeDepth = 2;
	private WrapperSpectrum spectrum = null;
	private boolean hydrogenTest = true;
	private boolean neutralLossAdd = false;
	private boolean bondEnergyScoring = false;
	private boolean isOnlyBreakSelectedBonds = false;
	
	/**
	 * Instantiates a new pubChem search thread.
	 * 
	 * @param molecule the molecule
	 * @param candidate the candidate
	 * @param folder the folder
	 * @param file the file
	 * @param recreateFrags the recreate frags
	 * @param peakList the peak list
	 * @param mzabs the mzabs
	 * @param mzppm the mzppm
	 * @param sumFormulaRedundancyCheck the sum formula redundancy check
	 * @param mode the mode
	 * @param breakAromaticRings the break aromatic rings
	 * @param treeDepth the tree depth
	 * @param showDiagrams the show diagrams
	 * @param spectrum the spectrum
	 * @param hydrogenTest the hydrogen test
	 */
	public FragmenterThread(String candidate, String database, PubChemWebService pw,
			WrapperSpectrum spectrum, double mzabs, double mzppm, boolean sumFormulaRedundancyCheck,
			boolean breakAromaticRings, int treeDepth, boolean showDiagrams, boolean hydrogenTest,
			boolean neutralLossAdd, boolean bondEnergyScoring, boolean isOnlyBreakSelectedBonds)
	{
		this.candidate = candidate;
		this.pw = pw;
		this.database = database;
		this.mzabs = mzabs;
		this.mzppm = mzppm;
		this.sumFormulaRedundancyCheck = sumFormulaRedundancyCheck;
		this.breakAromaticRings = breakAromaticRings;
		this.spectrum = spectrum;
		this.hydrogenTest = hydrogenTest;
		this.neutralLossAdd = neutralLossAdd;
		this.bondEnergyScoring = bondEnergyScoring;
		this.isOnlyBreakSelectedBonds = isOnlyBreakSelectedBonds;
		
	}
	
	
	@Override public void run()
	{		
		IAtomContainer molecule = null;
		
		try
		{	    

			if(database.equals("kegg"))
			{		
				candidate = candidate.substring(4);
				String candidateMol = KeggWebservice.KEGGgetMol(candidate, "");
				MDLReader reader;
				List<IAtomContainer> containersList;
				
		        reader = new MDLReader(new StringReader(candidateMol));
		        ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());
		        containersList = ChemFileManipulator.getAllAtomContainers(chemFile);
		        molecule = containersList.get(0);
				
			}
			else if(database.equals("chemspider"))
			{
				String candidateMol = ChemSpider.getMolByID(candidate);
				
				MDLReader reader;
				List<IAtomContainer> containersList;
				
		        reader = new MDLReader(new StringReader(candidateMol));
		        ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());
		        containersList = ChemFileManipulator.getAllAtomContainers(chemFile);
		        molecule = containersList.get(0);
		        
			}
			else if(database.equals("pubchem"))
			{
				molecule = pw.getMol(candidate);
			}
			else
			{
				System.err.println("No database selected or wrong database name?");
			}
			
			
			//molecule is not stored in the database or not chonsp!
			if(molecule == null)
				return;
			boolean isConnected = true;
			if (molecule != null)
				isConnected = ConnectivityChecker.isConnected(molecule);
			if(!isConnected)
				return;
	        
	         
	        try
	        {
		        //add hydrogens
		        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
		        CDKHydrogenAdder hAdder = CDKHydrogenAdder.getInstance(molecule.getBuilder());
		        hAdder.addImplicitHydrogens(molecule);
		        AtomContainerManipulator.convertImplicitToExplicitHydrogens(molecule);
	        }
	        //there is a bug in cdk??
	        catch(IllegalArgumentException e)
            {
	        	MetFrag.results.getCompleteLog().append("Error: " + candidate + " Message: " + e.getMessage());
            	//skip it
            	return;
            }
	        
	        
	        //get the original peak list again
			Vector<Peak> peakList = spectrum.getPeakList();
	        
	        Fragmenter fragmenter = new Fragmenter((Vector<Peak>)peakList.clone(), mzabs, mzppm, spectrum.getMode(), breakAromaticRings, sumFormulaRedundancyCheck, neutralLossAdd, isOnlyBreakSelectedBonds);
	        long start = System.currentTimeMillis();
	        List<IAtomContainer> generatedFrags = null;
	        try
	        {
	        	generatedFrags = fragmenter.generateFragmentsInMemory(molecule, true, treeDepth);
	        }
	        catch(OutOfMemoryError e)
	        {
	        	System.out.println("OUT OF MEMORY ERROR! " + treeDepth);
	        	MetFrag.results.getCompleteLog().append("Error: " + candidate + " Message: " + e.getMessage());
	        	return;
	        }
	        long time = System.currentTimeMillis() - start;
//	        System.out.println("Benötigte Zeit: " + time + " Got " + generatedFrags.size() + " fragments");

	        //read temp files in again
	        List<IAtomContainer> l = generatedFrags;
	                

	        try
			{					
				//clean up peak list
				CleanUpPeakList cList = new CleanUpPeakList(peakList);
				Vector<Peak> cleanedPeakList = cList.getCleanedPeakList(spectrum.getExactMass());
				
				
				//now find corresponding fragments to the mass
				AssignFragmentPeak afp = new AssignFragmentPeak();
				afp.setHydrogenTest(hydrogenTest);
				afp.assignFragmentPeak(l, cleanedPeakList, mzabs, mzppm, spectrum.getMode(), false);
				Vector<PeakMolPair> hits = afp.getHits();
				
				
				//now "real" scoring --> depends on intensities
				Scoring score = new Scoring(spectrum.getPeakList(), candidate);
				double currentScore = 0.0;
				if(this.bondEnergyScoring)
					currentScore = score.computeScoringWithBondEnergies(hits);
				else
					currentScore = score.computeScoringPeakMolPair(hits);
				
				double currentBondEnergy = score.getFragmentBondEnergy();
	
				if(currentBondEnergy > 0)
					currentBondEnergy = currentBondEnergy / afp.getHits().size();
				
				//set the added up energy of every fragment
				MetFrag.results.getMapCandidateToEnergy().put(candidate, currentBondEnergy);
				MetFrag.results.getMapCandidateToHydrogenPenalty().put(candidate, score.getPenalty());
				MetFrag.results.getMapCandidateToPartialChargesDiff().put(candidate, score.getPartialChargesDiff());
				
				Map<Double, Vector<String>> realScoreMap = MetFrag.results.getRealScoreMap();
				//save score in hashmap...if there are several hits with the same score --> vector of strings
				if(realScoreMap.containsKey(currentScore))
		        {
		        	Vector<String> tempList = realScoreMap.get(currentScore);
		        	tempList.add(candidate);
		        	realScoreMap.put(currentScore, tempList);
		        }
		        else
		        {
		        	Vector<String> temp = new Vector<String>();
		        	temp.add(candidate);
		        	realScoreMap.put(currentScore, temp);
		        }
				
				Map<Integer, List<String>> scoreMap = MetFrag.results.getScoreMap();
				if(scoreMap.containsKey(hits.size()))
		        {
		        	List<String> tempList = scoreMap.get(hits.size());
		        	tempList.add(candidate);
		        	scoreMap.put(hits.size(), tempList);
		        }
		        else
		        {
		        	List<String> temp = new ArrayList<String>();
		        	temp.add(candidate);
		        	scoreMap.put(hits.size(), temp);
		        }

			
				//get all the identified peaks
				String peaks = "";
				Double bondEnergy = 0.0;
				for (int i = 0; i < hits.size(); i++) {
					bondEnergy += Fragmenter.getCombinedEnergy((String)hits.get(i).getFragment().getProperty("BondEnergy"));
					peaks += hits.get(i).getPeak().getMass() + "[" + hits.get(i).getFragment().getProperty("BondEnergy") + "]" +  " ";
				}
				

				//write things to log file
				MetFrag.results.getCompleteLog().append("\nFile: " + candidate + "\t #Peaks: " + spectrum.getPeakList().size() + "\t #Found: " + hits.size());
				MetFrag.results.getCompleteLog().append("\tPeaks: " + peaks);
				
				List<IAtomContainer> hitsListTest = new ArrayList<IAtomContainer>();
				for (int i = 0; i < hits.size(); i++) {
					List<IAtomContainer> hitsList = new ArrayList<IAtomContainer>();
					hitsList.add(AtomContainerManipulator.removeHydrogens(hits.get(i).getFragment()));
					hitsListTest.add(hits.get(i).getFragment());
				}

			}
			catch(CDKException e)
			{
				System.out.println("CDK error!" + e.getMessage());
				MetFrag.results.getCompleteLog().append("CDK Error! " + e.getMessage() + " File: " + candidate);
			}
			catch(Exception e)
			{
				System.out.println("Error: " + e.getMessage());
				e.printStackTrace();
				MetFrag.results.getCompleteLog().append("Error! "+ e.getMessage() + " File: " + candidate);
			}
			catch(OutOfMemoryError e)
			{
				System.out.println("Out of memory: " + e.getMessage() + "\n" + e.getStackTrace());
				System.gc();
				MetFrag.results.getCompleteLog().append("Out of memory! "+ e.getMessage() + " File: " + candidate);
			}

	        
		}
		catch(CDKException e)
		{
			System.out.println("CDK error!" + e.getMessage());
			MetFrag.results.getCompleteLog().append("CDK Error! " + e.getMessage() + "File: " + candidate);
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File not found" + e.getMessage());
			MetFrag.results.getCompleteLog().append("File not found error! "+ e.getMessage() + "File: " + candidate);
		}
		catch(IOException e)
		{
			System.out.println("IO error: " + e.getMessage());
			MetFrag.results.getCompleteLog().append("IO Error! "+ e.getMessage() + "File: " + candidate);
		}
		catch(Exception e)
		{
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
			MetFrag.results.getCompleteLog().append("Error! "+ e.getMessage() + "File: " + candidate);
		}
		catch(OutOfMemoryError e)
		{
			System.out.println("Out of memory: " + e.getMessage() + "\n" + e.getStackTrace());
			System.gc();
			MetFrag.results.getCompleteLog().append("Out of memory! "+ e.getMessage() + "File: " + candidate);
		}
	}

}


