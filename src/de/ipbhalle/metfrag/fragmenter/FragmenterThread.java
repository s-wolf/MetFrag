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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.graph.ConnectivityChecker;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

import de.ipbhalle.metfrag.fragmenter.Fragmenter;
import de.ipbhalle.metfrag.main.Config;
import de.ipbhalle.metfrag.main.MetFrag;
import de.ipbhalle.metfrag.massbankParser.Peak;
import de.ipbhalle.metfrag.pubchem.PubChemWebService;
import de.ipbhalle.metfrag.read.Molfile;
import de.ipbhalle.metfrag.scoring.Scoring;
import de.ipbhalle.metfrag.spectrum.AssignFragmentPeak;
import de.ipbhalle.metfrag.spectrum.CleanUpPeakList;
import de.ipbhalle.metfrag.spectrum.MatchedFragment;
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
	private Config c = null;
	private boolean generateFragmentsInMemory = true;
	private String jdbc, username, password = "";
	private IAtomContainer candidateStructure = null;
	
	/**
	 * Instantiates a new pubChem search thread.
	 * 
	 * @param candidate the candidate
	 * @param mzabs the mzabs
	 * @param mzppm the mzppm
	 * @param sumFormulaRedundancyCheck the sum formula redundancy check
	 * @param breakAromaticRings the break aromatic rings
	 * @param treeDepth the tree depth
	 * @param showDiagrams the show diagrams
	 * @param spectrum the spectrum
	 * @param hydrogenTest the hydrogen test
	 * @param database the database
	 * @param pw the pw
	 * @param neutralLossAdd the neutral loss add
	 * @param bondEnergyScoring the bond energy scoring
	 * @param isOnlyBreakSelectedBonds the is only break selected bonds
	 * @param c the c
	 * @param generateFragmentsInMemory the generate fragments in memory
	 */
	public FragmenterThread(String candidate, String database, PubChemWebService pw,
			WrapperSpectrum spectrum, double mzabs, double mzppm, boolean sumFormulaRedundancyCheck,
			boolean breakAromaticRings, int treeDepth, boolean showDiagrams, boolean hydrogenTest,
			boolean neutralLossAdd, boolean bondEnergyScoring, boolean isOnlyBreakSelectedBonds, Config c,
			boolean generateFragmentsInMemory)
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
		this.treeDepth = treeDepth;
		this.c = c;
		this.generateFragmentsInMemory = generateFragmentsInMemory;
	}
	
	
	public FragmenterThread(IAtomContainer candidateStructure, String candidate, String database, PubChemWebService pw,
			WrapperSpectrum spectrum, double mzabs, double mzppm, boolean sumFormulaRedundancyCheck,
			boolean breakAromaticRings, int treeDepth, boolean showDiagrams, boolean hydrogenTest,
			boolean neutralLossAdd, boolean bondEnergyScoring, boolean isOnlyBreakSelectedBonds, Config c,
			boolean generateFragmentsInMemory)
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
		this.treeDepth = treeDepth;
		this.c = c;
		this.generateFragmentsInMemory = generateFragmentsInMemory;
		this.candidateStructure = candidateStructure;
	}
	
	
	/**
	 * Instantiates a new pubChem search thread.
	 * 
	 * @param candidate the candidate
	 * @param mzabs the mzabs
	 * @param mzppm the mzppm
	 * @param sumFormulaRedundancyCheck the sum formula redundancy check
	 * @param breakAromaticRings the break aromatic rings
	 * @param treeDepth the tree depth
	 * @param showDiagrams the show diagrams
	 * @param spectrum the spectrum
	 * @param hydrogenTest the hydrogen test
	 * @param database the database
	 * @param pw the pw
	 * @param neutralLossAdd the neutral loss add
	 * @param bondEnergyScoring the bond energy scoring
	 * @param isOnlyBreakSelectedBonds the is only break selected bonds
	 * @param c the c
	 * @param generateFragmentsInMemory the generate fragments in memory
	 */
	public FragmenterThread(String candidate, String database, PubChemWebService pw,
			WrapperSpectrum spectrum, double mzabs, double mzppm, boolean sumFormulaRedundancyCheck,
			boolean breakAromaticRings, int treeDepth, boolean showDiagrams, boolean hydrogenTest,
			boolean neutralLossAdd, boolean bondEnergyScoring, boolean isOnlyBreakSelectedBonds, Config c,
			boolean generateFragmentsInMemory, String jdbc, String username, String password)
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
		this.treeDepth = treeDepth;
		this.generateFragmentsInMemory = generateFragmentsInMemory;
		this.username = username;
		this.password = password;
		this.jdbc = jdbc;
	}
	
	
	@Override public void run()
	{		
		IAtomContainer molecule = null;
		
		try
		{	    
			
			//retrieve the candidate from the database
			if(this.candidateStructure != null)
				molecule = this.candidateStructure;
			else if(pw == null && c == null)
				molecule = Candidates.getCompoundLocally(this.database, candidate, jdbc, username, password, false);
			else if(pw == null)
				molecule = Candidates.getCompoundLocally(this.database, candidate, c.getJdbc(), c.getUsername(), c.getPassword(), false);
			else
			{
				molecule = Candidates.getCompound(database, candidate, pw);
				if(molecule == null && database.equals("pubchem"))
					molecule = pw.getSingleMol(candidate, false);
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
	        	MetFrag.results.addToCompleteLog("Error: " + candidate + " Message: " + e.getMessage());
            	//skip it
            	return;
            }
	        
	        
	        //get the original peak list again
			Vector<Peak> peakList = spectrum.getPeakList();
	        
	        Fragmenter fragmenter = new Fragmenter((Vector<Peak>)peakList.clone(), mzabs, mzppm, spectrum.getMode(), breakAromaticRings, sumFormulaRedundancyCheck, isOnlyBreakSelectedBonds);
	        long start = System.currentTimeMillis();
	        List<IAtomContainer> generatedFrags = null;
	        try
	        {
	        	if(generateFragmentsInMemory)
	        		generatedFrags = fragmenter.generateFragmentsInMemory(molecule, true, treeDepth);
	        	else
	        	{
	        		List<File> fragsFiles = fragmenter.generateFragmentsEfficient(molecule, false, treeDepth, candidate);
	        		generatedFrags = Molfile.ReadfolderTemp(fragsFiles);
	        	}
	        }
	        catch(OutOfMemoryError e)
	        {
	        	System.out.println("OUT OF MEMORY ERROR! " + treeDepth);
	        	MetFrag.results.addToCompleteLog("Error: " + candidate + " Message: " + e.getMessage());
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
				Vector<MatchedFragment> hits = afp.getHits();
				
				
				//now "real" scoring --> depends on intensities
				Scoring score = new Scoring(spectrum, candidate);
				double currentScore = 0.0;
				if(this.bondEnergyScoring)
//					currentScore = score.computeScoringWithBondEnergies(hits);
					currentScore = score.computeScoringOptimized(hits, spectrum.getExactMass());
				else
					currentScore = score.computeScoringPeakMolPair(hits);
				
				double currentBondEnergy = score.getBDE();
	
				if(currentBondEnergy > 0)
					currentBondEnergy = currentBondEnergy / afp.getHits().size();
				
				//set the added up energy of every fragment
				MetFrag.results.getMapCandidateToEnergy().put(candidate, currentBondEnergy);
				MetFrag.results.getMapCandidateToHydrogenPenalty().put(candidate, score.getPenalty());
				MetFrag.results.getMapCandidateToPartialChargesDiff().put(candidate, score.getPartialChargesDiff());
				
				//also output the optimization matrix if needed
				MetFrag.results.getCandidateToOptimizationMatrixEntries().put(candidate, score.getOptimizationMatrixEntries());	
				
				//also add the structure to results file
				MetFrag.results.getMapCandidateToStructure().put(candidate, molecule);
				MetFrag.results.getMapCandidateToFragments().put(candidate, afp.getHits());
				
				
				
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
					bondEnergy += Fragmenter.getCombinedEnergy((String)hits.get(i).getFragmentStructure().getProperty("BondEnergy"));
					peaks += hits.get(i).getPeak().getMass() + "[" + hits.get(i).getFragmentStructure().getProperty("BondEnergy") + "]" +  " ";
				}
				

				//write things to log file
				MetFrag.results.addToCompleteLog("\nCandidate: " + candidate + "\t #Peaks: " + spectrum.getPeakList().size() + "\t #Found: " + hits.size());
				MetFrag.results.addToCompleteLog("\tPeaks: " + peaks);
				
				List<IAtomContainer> hitsListTest = new ArrayList<IAtomContainer>();
				for (int i = 0; i < hits.size(); i++) {
					List<IAtomContainer> hitsList = new ArrayList<IAtomContainer>();
					hitsList.add(AtomContainerManipulator.removeHydrogens(hits.get(i).getFragmentStructure()));
					hitsListTest.add(hits.get(i).getFragmentStructure());
				}

			}
			catch(CDKException e)
			{
				System.out.println("CDK error!" + e.getMessage());
				MetFrag.results.addToCompleteLog("CDK Error! " + e.getMessage() + " File: " + candidate);
			}
			catch(Exception e)
			{
				System.out.println("Error: " + e.getMessage());
				e.printStackTrace();
				MetFrag.results.addToCompleteLog("Error! "+ e.getMessage() + " File: " + candidate);
			}
			catch(OutOfMemoryError e)
			{
				System.out.println("Out of memory: " + e.getMessage() + "\n" + e.getStackTrace());
				System.gc();
				MetFrag.results.addToCompleteLog("Out of memory! "+ e.getMessage() + " File: " + candidate);
			}

	        
		}
		catch(CDKException e)
		{
			System.out.println("CDK error!" + e.getMessage());
			MetFrag.results.addToCompleteLog("CDK Error! " + e.getMessage() + "File: " + candidate);
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File not found" + e.getMessage());
			MetFrag.results.addToCompleteLog("File not found error! "+ e.getMessage() + "File: " + candidate);
		}
		catch(IOException e)
		{
			System.out.println("IO error: " + e.getMessage());
			MetFrag.results.addToCompleteLog("IO Error! "+ e.getMessage() + "File: " + candidate);
		}
		catch(Exception e)
		{
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
			MetFrag.results.addToCompleteLog("Error! "+ e.getMessage() + "File: " + candidate);
		}
		catch(OutOfMemoryError e)
		{
			System.out.println("Out of memory: " + e.getMessage() + "\n" + e.getStackTrace());
			System.gc();
			MetFrag.results.addToCompleteLog("Out of memory! "+ e.getMessage() + "File: " + candidate);
		}
	}

}


