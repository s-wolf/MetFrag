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

package de.ipbhalle.metfrag.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.MoleculeSet;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.geometry.GeometryTools;
import org.openscience.cdk.graph.ConnectivityChecker;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IMoleculeSet;
import org.openscience.cdk.interfaces.IBond.Stereo;
import org.openscience.cdk.io.MDLReader;
import org.openscience.cdk.io.SDFWriter;
import org.openscience.cdk.isomorphism.matchers.smarts.StereoBond;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.AtomContainerSetManipulator;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

import de.ipbhalle.metfrag.chemspiderClient.ChemSpider;
import de.ipbhalle.metfrag.fragmenter.Candidates;
import de.ipbhalle.metfrag.fragmenter.Fragmenter;
import de.ipbhalle.metfrag.fragmenter.FragmenterResult;
import de.ipbhalle.metfrag.fragmenter.FragmenterThread;
import de.ipbhalle.metfrag.keggWebservice.KeggWebservice;
import de.ipbhalle.metfrag.massbankParser.Peak;
import de.ipbhalle.metfrag.molDatabase.KEGGLocal;
import de.ipbhalle.metfrag.pubchem.PubChemWebService;
import de.ipbhalle.metfrag.scoring.Scoring;
import de.ipbhalle.metfrag.similarity.Similarity;
import de.ipbhalle.metfrag.similarity.SimilarityGroup;
import de.ipbhalle.metfrag.spectrum.AssignFragmentPeak;
import de.ipbhalle.metfrag.spectrum.CleanUpPeakList;
import de.ipbhalle.metfrag.spectrum.PeakMolPair;
import de.ipbhalle.metfrag.spectrum.WrapperSpectrum;
import de.ipbhalle.metfrag.tools.MolecularFormulaTools;
import de.ipbhalle.metfrag.tools.PPMTool;

public class MetFrag {
	
	public static FragmenterResult results = new FragmenterResult();
		
	
	/**
	 * MetFrag. Start the fragmenter thread. Afterwards score the results.
	 * 
	 * @param database the database
	 * @param searchPPM the search ppm
	 * @param databaseID the database id
	 * @param molecularFormula the molecular formula
	 * @param exactMass the exact mass
	 * @param spectrum the spectrum
	 * 
	 * @return the string
	 * 
	 * @throws Exception the exception
	 */
	public static String start(String database, String databaseID, String molecularFormula, Double exactMass, WrapperSpectrum spectrum, boolean useProxy, String outputFile) throws Exception
	{
		//get configuration
		Config config = new Config();
		PubChemWebService pubchem = new PubChemWebService();
		Vector<String> candidates = Candidates.getOnline(database, databaseID, molecularFormula, exactMass, config.getSearchPPM(), useProxy, pubchem);

		//now fill executor!!!
		//number of threads depending on the available processors
	    int threads = Runtime.getRuntime().availableProcessors();
	    //thread executor
	    ExecutorService threadExecutor = null;
	    System.out.println("Used Threads: " + threads);
	    threadExecutor = Executors.newFixedThreadPool(threads);
			
		for (int c = 0; c < candidates.size(); c++) {				
			threadExecutor.execute(new FragmenterThread(candidates.get(c), database, pubchem, spectrum, config.getMzabs(), config.getMzppm(), 
					config.isSumFormulaRedundancyCheck(), config.isBreakAromaticRings(), config.getTreeDepth(), false, config.isHydrogenTest(), config.isNeutralLossAdd(), 
					config.isBondEnergyScoring(), config.isOnlyBreakSelectedBonds()));		
		}
		
		threadExecutor.shutdown();
		
		//wait until all threads are finished
		while(!threadExecutor.isTerminated())
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}//sleep for 1000 ms
		}
		
		String ret = "";

		Map<Double, Vector<String>> scoresNormalized = Scoring.getCombinedScore(results.getRealScoreMap(), results.getMapCandidateToEnergy(), results.getMapCandidateToHydrogenPenalty());
		Double[] scores = new Double[scoresNormalized.size()];
		scores = scoresNormalized.keySet().toArray(scores);
		Arrays.sort(scores);
		
		
		
		//now collect the result
		Map<String, IAtomContainer> candidateToStructure = results.getMapCandidateToStructure();
		Map<String, Vector<PeakMolPair>> candidateToFragments = results.getMapCandidateToFragments();
		MoleculeSet setOfMolecules = new MoleculeSet();
		for (int i = scores.length -1; i >=0 ; i--) {
			Vector<String> list = scoresNormalized.get(scores[i]);
			for (String string : list) {
				ret += string + "\t" + scores[i] + "\n";
				//get corresponding structure
				IAtomContainer tmp = candidateToStructure.get(string);
				tmp = AtomContainerManipulator.removeHydrogens(tmp);
				tmp.setProperty("Score", scores[i]);
				tmp.setProperty("PeaksExplained", candidateToFragments.get(string).size());
				
				//fix for bug in mdl reader setting where it happens that bond.stereo is null when the bond was read in as UP/DOWN (4)
				for (IBond bond : tmp.bonds()) {
					if(bond.getStereo() == null)
						bond.setStereo(Stereo.UP_OR_DOWN);		
				} 
				setOfMolecules.addAtomContainer(tmp);
			}
		}
	
		
		//write results file
		try {
			SDFWriter writer = new SDFWriter(new FileWriter(new File(outputFile)));
			writer.write(setOfMolecules);
			writer.close();
		} catch (CDKException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return ret;
	}
	
	
	
	/**
	 * MetFrag. Start the fragmenter thread. Afterwards score the results.
	 * 
	 * @param database the database
	 * @param searchPPM the search ppm
	 * @param databaseID the database id
	 * @param molecularFormula the molecular formula
	 * @param exactMass the exact mass
	 * @param spectrum the spectrum
	 * @param useProxy the use proxy
	 * @param mzabs the mzabs
	 * @param mzppm the mzppm
	 * @param molecularFormulaRedundancyCheck the molecular formula redundancy check
	 * @param breakAromaticRings the break aromatic rings
	 * @param treeDepth the tree depth
	 * @param hydrogenTest the hydrogen test
	 * @param neutralLossInEveryLayer the neutral loss in every layer
	 * @param bondEnergyScoring the bond energy scoring
	 * @param breakOnlySelectedBonds the break only selected bonds
	 * 
	 * @return the string
	 * 
	 * @throws Exception the exception
	 */
	public static List<MetFragResult> startConvenience(String database, String databaseID, String molecularFormula, Double exactMass, WrapperSpectrum spectrum, boolean useProxy, 
			double mzabs, double mzppm, double searchPPM, boolean molecularFormulaRedundancyCheck, boolean breakAromaticRings, int treeDepth,
			boolean hydrogenTest, boolean neutralLossInEveryLayer, boolean bondEnergyScoring, boolean breakOnlySelectedBonds) throws Exception
	{
		
		PubChemWebService pubchem = new PubChemWebService();
		Vector<String> candidates = Candidates.getOnline(database, databaseID, molecularFormula, exactMass, searchPPM, useProxy, pubchem);


		//now fill executor!!!
		//number of threads depending on the available processors
	    int threads = Runtime.getRuntime().availableProcessors();
	    //thread executor
	    ExecutorService threadExecutor = null;
	    System.out.println("Used Threads: " + threads);
	    threadExecutor = Executors.newFixedThreadPool(threads);
			
		for (int c = 0; c < candidates.size(); c++) {				
			threadExecutor.execute(new FragmenterThread(candidates.get(c), database, pubchem, spectrum, mzabs, mzppm, 
					molecularFormulaRedundancyCheck, breakAromaticRings, treeDepth, false, hydrogenTest, neutralLossInEveryLayer, 
					bondEnergyScoring, breakOnlySelectedBonds));		
		}
		
		threadExecutor.shutdown();
		
		//wait until all threads are finished
		while(!threadExecutor.isTerminated())
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}//sleep for 1000 ms
		}

		Map<Double, Vector<String>> scoresNormalized = Scoring.getCombinedScore(results.getRealScoreMap(), results.getMapCandidateToEnergy(), results.getMapCandidateToHydrogenPenalty());
		Double[] scores = new Double[scoresNormalized.size()];
		scores = scoresNormalized.keySet().toArray(scores);
		Arrays.sort(scores);

		//now collect the result
		Map<String, IAtomContainer> candidateToStructure = results.getMapCandidateToStructure();
		Map<String, Vector<PeakMolPair>> candidateToFragments = results.getMapCandidateToFragments();

		List<MetFragResult> results = new ArrayList<MetFragResult>();
		for (int i = scores.length -1; i >=0 ; i--) {
			Vector<String> list = scoresNormalized.get(scores[i]);
			for (String string : list) {
				//get corresponding structure
				IAtomContainer tmp = candidateToStructure.get(string);
				tmp = AtomContainerManipulator.removeHydrogens(tmp);
				
				results.add(new MetFragResult(string, tmp, scores[i], candidateToFragments.get(string).size()));
			}
		}		
		
		return results;
	}
	
	
	
	/**
	 * MetFrag. Start the fragmenter thread. Afterwards score the results.
	 * This method is mainly used to evaluate MetFrag against the test data set.
	 * 
	 * @param database the database
	 * @param searchPPM the search ppm
	 * @param databaseID the database id
	 * @param molecularFormula the molecular formula
	 * @param exactMass the exact mass
	 * @param spectrum the spectrum
	 * 
	 * @return the string
	 * 
	 * @throws Exception the exception
	 */
	public static String startScriptable(String database, String databaseID, String molecularFormula, Double exactMass, WrapperSpectrum spectrum, boolean useProxy, String outputFile, String correctCandidateID, boolean generateOptimizationMatrix) throws Exception
	{
		//get configuration
		Config config = new Config();
		
		PubChemWebService pubchem = new PubChemWebService();
		List<String> candidates = Candidates.getLocally(database, exactMass, config.getSearchPPM(), config.getJdbc(), config.getUsername(), config.getPassword());

		//now fill executor!!!
		//number of threads depending on the available processors
	    int threads = Runtime.getRuntime().availableProcessors();
	    //thread executor
	    ExecutorService threadExecutor = null;
	    System.out.println("Used Threads: " + threads);
	    threadExecutor = Executors.newFixedThreadPool(threads);

		for (int c = 0; c < candidates.size(); c++) {				
			threadExecutor.execute(new FragmenterThread(candidates.get(c), database, pubchem, spectrum, config.getMzabs(), config.getMzppm(), 
					config.isSumFormulaRedundancyCheck(), config.isBreakAromaticRings(), config.getTreeDepth(), false, config.isHydrogenTest(), config.isNeutralLossAdd(), 
					config.isBondEnergyScoring(), config.isOnlyBreakSelectedBonds()));		
		}
		
		threadExecutor.shutdown();
		
		//wait until all threads are finished
		while(!threadExecutor.isTerminated())
		{
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}//sleep for 1000 ms
		}
		
		String ret = "";

		Map<Double, Vector<String>> scoresNormalized = Scoring.getCombinedScore(results.getRealScoreMap(), results.getMapCandidateToEnergy(), results.getMapCandidateToHydrogenPenalty());
		Double[] scores = new Double[scoresNormalized.size()];
		scores = scoresNormalized.keySet().toArray(scores);
		Arrays.sort(scores);

		//now collect the result
		Map<String, IAtomContainer> candidateToStructure = results.getMapCandidateToStructure();
		Map<String, Vector<PeakMolPair>> candidateToFragments = results.getMapCandidateToFragments();
		Map<String, Double> candidateToEnergy = results.getMapCandidateToEnergy();
		Map<Integer, List<String>> scoreMap = results.getScoreMap();
		Map<Double, Vector<String>> realScoreMap = results.getRealScoreMap();
		StringBuilder completeLog = results.getCompleteLog();
		
		
		//TODO
		
		
		//easy scoring
		Integer[] keylist = new Integer[scoreMap.keySet().size()];
		Object[] keys = scoreMap.keySet().toArray();
		
		for (int i = 0; i < keys.length; i++) {
			keylist[i] = Integer.parseInt(keys[i].toString());
		}
		
		Arrays.sort(keylist);
		StringBuilder scoreList = new StringBuilder();
		int rankWorstCase = 0;
		
		for (int i = keylist.length-1; i >= 0; i--) {
			boolean check = false;
			for (int j = 0; j < scoreMap.get(keylist[i]).size(); j++) {
				scoreList.append("\n" + keylist[i] + " - " + scoreMap.get(keylist[i]).get(j) + " [" + Math.round(candidateToEnergy.get(scoreMap.get(keylist[i]).get(j))) + "]");
				if(correctCandidateID.equals(scoreMap.get(keylist[i]).get(j)))
				{
					check = true;
				}
				//worst case: count all which are better or have a equal position
				rankWorstCase++;
			}
			if(check)
			{
				histogram += "\n" + file + "\t" + correctCandidateID + "\t" + rankWorstCase + "\t" + exactMass;
			}
		}
		
		if(correctCandidateID.equals("none"))
		{
			histogram += "\n" + file + "\t" + correctCandidateID + "\t\t" + exactMass;
		}
		
		completeLog.append("\n\n*****************Scoring*****************************");
		completeLog.append("Supposed to be: " + correctCandidateID);
		completeLog.append(scoreList);
		completeLog.append("\n*****************************************************\n\n");
		//easy scoring end
					
		
		//real scoring
		if(config.isBondEnergyScoring())
			realScoreMap = Scoring.getCombinedScore(realScoreMap, candidateToEnergy, candidateToHydrogenPenalty);
		
		//generate the parameter optimization matrix
		if(generateOptimizationMatrix)
		{
			parameterOptimizationMatrix = prepareParameterOptimizationMatrix(pubChemIdentifier, exactMass);
			generateOptimizationMatrix(candidateToOptimizationMatrixEntries);
		}
		
					
		
		Double[] keysScore = new Double[realScoreMap.keySet().size()];
		keysScore = realScoreMap.keySet().toArray(keysScore);
		
		Arrays.sort(keysScore);
		String scoreListReal = "";
		rankWorstCase = 0;
		int rankBestCase = 0;
		int rankBestCaseGrouped = 0;
		
		
		
		//now create the tanimoto distance matrix
		//to be able to group results with the same score
		//search molecules with the same connectivity
		String similarity = "";
		int rankTanimotoGroup = 0;
		int rankIsomorphism = 0;
		boolean stop = false;
		try {
			Similarity sim = new Similarity(candidateToSmiles, (float)0.95);
			for (int i = keysScore.length-1; i >= 0; i--) {
				similarity += "\nScore: " + keysScore[i] + "\n";
				List<String> candidateGroup = new ArrayList<String>();
				for (int j = 0; j < realScoreMap.get(keysScore[i]).size(); j++) {
					candidateGroup.add(realScoreMap.get(keysScore[i]).get(j));
				}

				List<SimilarityGroup> groupedCandidates = sim.getTanimotoDistanceList(candidateGroup);
				for (SimilarityGroup similarityGroup : groupedCandidates) {				
					List<String> tempSimilar = similarityGroup.getSimilarCompounds();
					List<Float> tempSimilarTanimoto = similarityGroup.getSimilarCompoundsTanimoto();
					similarity += similarityGroup.getCandidateTocompare() + ": ";
					
					if(pubChemIdentifier.equals(similarityGroup.getCandidateTocompare()))
						stop = true;					
					
					for (int k = 0; k < tempSimilar.size(); k++) {

						if(pubChemIdentifier.equals(tempSimilar.get(k)))
							stop = true;
						
						similarity += tempSimilar.get(k) + "(" +  tempSimilarTanimoto.get(k);
					
						boolean isIsomorph = sim.isIsomorph(tempSimilar.get(k), similarityGroup.getCandidateTocompare());
						if(!isIsomorph)
							rankIsomorphism++;
						
						similarity += " -" + isIsomorph + ") ";
					}
					similarity += "\n";						
					rankTanimotoGroup++;
					rankIsomorphism++;
				}
				if(stop)
					break;
			}
		} catch (CDKException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		for (int i = keysScore.length-1; i >= 0; i--) {
			boolean check = false;
			int temp = 0;
			for (int j = 0; j < realScoreMap.get(keysScore[i]).size(); j++) {
				scoreListReal += "\n" + keysScore[i] + " - " + realScoreMap.get(keysScore[i]).get(j) + "[" + candidateToEnergy.get(realScoreMap.get(keysScore[i]).get(j)) + "]";
				if(pubChemIdentifier.compareTo(realScoreMap.get(keysScore[i]).get(j)) == 0)
				{
					check = true;
				}
				//worst case: count all which are better or have a equal position
				rankWorstCase++;
				temp++;
			}
			rankBestCaseGrouped++;
			if(!check)
			{
				rankBestCase += temp;
			}
			//add it to rank best case
			else
			{
				histogramReal += "\n" + file + "\t" + pubChemIdentifier + "\t" + rankWorstCase + "\t" + rankTanimotoGroup + "\t" + rankIsomorphism + "\t" + exactMass;
			}
		}
		
		if(pubChemIdentifier.equals("none"))
		{
			histogramReal += "\n" + file + "\t" + pubChemIdentifier + "\t\t" + exactMass;
		}
		
		//timing
		long timeEnd = System.currentTimeMillis() - timeStart;
        sumTime += timeEnd;
		
		completeLog += "\n\n*****************Scoring(Real)*****************************";
		completeLog += "Supposed to be: " + pubChemIdentifier;
		completeLog += "\nTime: " + timeEnd;
		completeLog += scoreListReal;
		
		//write all tanimoto distances in one file
		//similarityValues += sim.getAllSimilarityValues();
		completeLog += similarity;			
		
		completeLog += "\n*****************************************************\n\n";	
		
		
		histogramPeaksAll += "//\n" + file + "\n";
		//write the data for peak histogram to log file
		for (int i = 0; i < listOfPeaks.size(); i++) {
			histogramPeaksAll += listOfPeaks.get(i).getMass() + "\n";
		}	
		
		//filter the peaks which are contained in the all peaks list. (exclusive)
		for (int i = 0; i < listOfPeaksCorresponding.size(); i++) {
			for (int j = 0; j < listOfPeaks.size(); j++) {
				Double valueA = listOfPeaks.get(j).getMass();
				Double valueB = listOfPeaksCorresponding.get(i).getMass();
				if(valueA.equals(valueB))
				{
					listOfPeaks.remove(j);
				}
			}
		}
		
		histogramPeaks += "//\n" + file + "\n";
		for (int i = 0; i < listOfPeaks.size(); i++) {
			histogramPeaks += listOfPeaks.get(i).getMass() + "\n";
		}
		
		histogramPeaksReal += "//\n" + file + "\n";
		for (int i = 0; i < listOfPeaksCorresponding.size(); i++) {
			histogramPeaksReal += listOfPeaksCorresponding.get(i).getMass() + "\n";
		}
		
		
		//TODO
		
		
		
		
		
		
		
		
		
		
	}
	
	
	
	public static void main(String[] args) {
		
	}

}
