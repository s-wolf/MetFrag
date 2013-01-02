package de.ipbhalle.metfrag.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.openscience.cdk.MoleculeSet;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IBond.Stereo;
import org.openscience.cdk.io.SDFWriter;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

import de.ipbhalle.metfrag.fragmenter.Candidates;
import de.ipbhalle.metfrag.fragmenter.FragmenterResult;
import de.ipbhalle.metfrag.fragmenter.FragmenterThread;
import de.ipbhalle.metfrag.pubchem.ESearchDownload;
import de.ipbhalle.metfrag.pubchem.PubChemWebService;
import de.ipbhalle.metfrag.read.CMLMolecule;
import de.ipbhalle.metfrag.read.CMLTools;
import de.ipbhalle.metfrag.scoring.OptimizationMatrixEntry;
import de.ipbhalle.metfrag.scoring.Scoring;
import de.ipbhalle.metfrag.similarity.Similarity;
import de.ipbhalle.metfrag.similarity.SimilarityGroup;
import de.ipbhalle.metfrag.similarity.TanimotoClusterer;
import de.ipbhalle.metfrag.spectrum.MatchedFragment;
import de.ipbhalle.metfrag.spectrum.WrapperSpectrum;
import de.ipbhalle.metfrag.tools.MoleculeTools;
import de.ipbhalle.metfrag.tools.PPMTool;
import de.ipbhalle.metfrag.tools.Writer;

public class MetFragPreCalculatedPubChem2006 {
	
	public static FragmenterResult results = new FragmenterResult();
	private String file = "";
	private String date = "";
	private long timeStart;
	private int candidateCount = 0;
		
	/**
	 * Instantiates a new metFrag object.
	 * 
	 * @param file the file
	 * @param date the date
	 * @param folder the folder
	 */
	public MetFragPreCalculatedPubChem2006(String file, String date)
	{
		this.file = file;
		this.date = date;
		this.timeStart = System.currentTimeMillis();
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
	public void startScriptable(boolean useProxy, boolean writeSDF, File folderToMopac, boolean getLowestHeatofFormation) throws Exception
	{
		results = new FragmenterResult();
		//get configuration
		Config config = new Config("outside");
		WrapperSpectrum spectrum = new WrapperSpectrum(file);
		
		
		String database = config.getDatabase();
		List<CMLMolecule> candidates = new ArrayList<CMLMolecule>();
		
		if(getLowestHeatofFormation)
		{
			System.out.println("Lowest HOF");
			double deviation = PPMTool.getPPMDeviation(spectrum.getExactMass(), 10.0);
			Map<String, String> candidateIDToFormula = ESearchDownload.ESearchDownloadExactMassFebruary2006((spectrum.getExactMass() - deviation) , (spectrum.getExactMass() + deviation), "/home/swolf/MOPAC/Hill-Riken-MM48_POSITIVE_PubChem_LocalMass2009_CHONPS_NEW/pubchem2006/" + new File(file).getName());
			
			System.out.println("Anzahl heruntergeladener Kandidaten: " + candidateIDToFormula.keySet().size());
			
			for (String id : candidateIDToFormula.keySet()) {
				List<CMLMolecule> candTemp = CMLTools.readIDLowestHoF(folderToMopac, candidateIDToFormula.get(id), id);
				if(candTemp != null)
					candidates.addAll(candTemp);
			}

			System.out.println("Anzahl der eingelesen Kandidaten: " + candidates.size());
			for (CMLMolecule cmlmol : candidates) {
				System.out.println(cmlmol.getFileName());
			}
		}
		else		
			candidates = CMLTools.readFolder(folderToMopac);			
		
		
		
//		this.candidateCount = candidates.size();
		results.addToCompleteLog("\n*****************************************************\n\n");
		results.addToCompleteLog("\nFile: " + file + " ====> " + getCorrectCandidateID(spectrum, database));
		
		
		//now fill executor!!!
		//number of threads depending on the available processors
	    int threads = Runtime.getRuntime().availableProcessors();
	    //thread executor
	    ExecutorService threadExecutor = null;
	    System.out.println("Used Threads: " + threads);
	    threadExecutor = Executors.newFixedThreadPool(threads);

		for (int c = 0; c < candidates.size(); c++) {
			FragmenterThread ft = new FragmenterThread(candidates.get(c).getMolStructure(), candidates.get(c).getFileName(), database, new PubChemWebService(), spectrum, config.getMzabs(), config.getMzppm(), 
					config.isSumFormulaRedundancyCheck(), config.isBreakAromaticRings(), config.getTreeDepth(), false, config.isHydrogenTest(), config.isNeutralLossAdd(), 
					config.isBondEnergyScoring(), config.isOnlyBreakSelectedBonds(), config, true, config.getMaximumNeutralLossCombination(), true);
			ft.setPubChem2006(true);
			threadExecutor.execute(ft);		
		}
		
		threadExecutor.shutdown();
		
		//wait until all threads are finished
		int count = 0;
		while(!threadExecutor.isTerminated())
		{
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}//sleep for 1000 ms
			if(count == 1000)
				System.err.println("ThreadExecutor is not terminated!");
			else
				Thread.sleep(5000);
			
			count++;
		}
		

		evaluateResults(getCorrectCandidateID(spectrum, database), spectrum, true, config.getFolder(), writeSDF);		
	}
	
	
	private String getCorrectCandidateID(WrapperSpectrum spectrum, String database)
	{
		String candidate = "";
		if(database.equals("pubchem"))
			candidate = Integer.toString(spectrum.getCID());
		else if(database.equals("kegg"))
			candidate = spectrum.getKEGG();
		else if(database.equals("chebi"))
			candidate = spectrum.getChebi();
		return candidate;
	}
	
	
	/**
	 * Write sdf file with all processed structures.
	 * 
	 * @param keysScore the keys score
	 * @param folder the folder
	 */
	private void writeSDF(Double[] keysScore, String folder, String correctCandidateID)
	{
		Map<String, Vector<MatchedFragment>> candidateToFragments = results.getMapCandidateToFragments();
		Map<Double, Vector<String>> realScoreMap = results.getRealScoreMap();
		Map<String, IAtomContainer> candidateToStructure = results.getMapCandidateToStructure();
		
		MoleculeSet setOfMolecules = new MoleculeSet();
		for (int i = keysScore.length -1; i >=0 ; i--) {
			Vector<String> list = realScoreMap.get(keysScore[i]);
			for (String string : list) {
				//get corresponding structure
				IAtomContainer tmp = candidateToStructure.get(string);
				tmp = AtomContainerManipulator.removeHydrogens(tmp);
				tmp.setProperty("DatabaseID", string);
				tmp.setProperty("Score", keysScore[i]);
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
			SDFWriter writer = new SDFWriter(new FileWriter(new File(folder + "logs/" + correctCandidateID + "_" + date + "_Structures.sdf")));
			writer.write(setOfMolecules);
			writer.close();
		} catch (CDKException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Evaluate results and write them to the log files
	 * @throws InterruptedException 
	 */
	private void evaluateResults(String correctCandidateID, WrapperSpectrum spectrum, boolean generateOptimizationMatrix, String folder, boolean writeSDF) throws InterruptedException
	{
		//TODO: hack for local file
//		correctCandidateID = correctCandidateID + "_Combined.cml";
		
		
		//now collect the result
		Map<String, IAtomContainer> candidateToStructure = results.getMapCandidateToStructure();
		Map<String, Double> candidateToEnergy = results.getMapCandidateToEnergy();
		//CHANGED TO NEW SCORING!!!
//		Map<Double, Vector<String>> realScoreMap = results.getRealScoreMap();
		Map<Double, Vector<String>> realScoreMap = Scoring.scoreCandidates(results.getMapCandidateToFragments());
		StringBuilder completeLog = results.getCompleteLog();
		
		//this is the real candidate count after filtering not connected compounds
		this.candidateCount = results.getMapCandidateToStructure().size();
		
		
		
		//generate the parameter optimization matrix
		String parameterOptimization = "";
		String parameterOptimizationCombined = "";
		if(generateOptimizationMatrix)
		{
			String header = prepareParameterOptimizationMatrix(correctCandidateID, spectrum.getExactMass());
			parameterOptimization = generateOptimizationMatrix(results.getCandidateToOptimizationMatrixEntries(), header);
			
			String headerCombined = prepareParameterOptimizationMatrixCombined(correctCandidateID, spectrum.getExactMass());
			parameterOptimizationCombined = generateOptimizationMatrixCombined(results.getCandidateToOptimizationMatrixEntries(), headerCombined);
		}
		
					
//		realScoreMap = Scoring.getCombinedScore(results.getRealScoreMap(), results.getMapCandidateToEnergy(), results.getMapCandidateToHydrogenPenalty());
//		Double[] keysScore = new Double[realScoreMap.keySet().size()];
//		keysScore = realScoreMap.keySet().toArray(keysScore);
//		Arrays.sort(keysScore);
//		TODO: new scoring function
		Double[] keysScore = new Double[realScoreMap.keySet().size()];
		keysScore = realScoreMap.keySet().toArray(keysScore);
		Arrays.sort(keysScore);
		
		//write out SDF with all the structures
		if(writeSDF)
		{
			writeSDF(keysScore, folder, correctCandidateID);
		}
		
		
		
		StringBuilder scoreListReal = new StringBuilder();
		int rankWorstCase = 0;
		int rankBestCase = 0;
		int rankBestCaseGrouped = 0;		
		
		//now create the tanimoto distance matrix
		//to be able to group results with the same score
		//search molecules with the same connectivity
		StringBuilder similarity = new StringBuilder();
		int rankTanimotoGroup = 0;
		int rankIsomorphism = 0;
		boolean stop = false;
		try {
			for (int i = keysScore.length-1; i >= 0; i--) {
				similarity.append("\nScore: " + keysScore[i] + "\n");
				List<String> candidateGroup = new ArrayList<String>();
				
				Map<String, IAtomContainer> candidateToStructureTemp = new HashMap<String, IAtomContainer>();
				for (int j = 0; j < realScoreMap.get(keysScore[i]).size(); j++) {
					candidateGroup.add(realScoreMap.get(keysScore[i]).get(j));
					candidateToStructureTemp.put(realScoreMap.get(keysScore[i]).get(j), candidateToStructure.get(realScoreMap.get(keysScore[i]).get(j)));
				}
				
				Similarity sim = null;
				try {
					sim = new Similarity(candidateToStructureTemp, true, false);
				} catch (CDKException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				//now cluster 
				TanimotoClusterer tanimoto = new TanimotoClusterer(sim.getSimilarityMatrix(), sim.getCandidateToPosition());
				List<SimilarityGroup> clusteredCpds = tanimoto.clusterCandididates(candidateGroup, 0.95f);
				List<SimilarityGroup> groupedCandidates = tanimoto.getCleanedClusters(clusteredCpds);
				
				for (SimilarityGroup similarityGroup : groupedCandidates) {			
										
					List<String> tempSimilar = similarityGroup.getSimilarCompoundsWithBaseAsArray();				
					
					for (int k = 0; k < tempSimilar.size(); k++) {

						if(correctCandidateID.equals(tempSimilar.get(k).split("_")[0]))
							stop = true;
						
						similarity.append(tempSimilar.get(k));
					
						boolean isIsomorph = sim.isIsomorph(tempSimilar.get(k), similarityGroup.getCandidateTocompare());
						if(!isIsomorph)
							rankIsomorphism++;
						
						similarity.append(" (" + isIsomorph + ") ");
					}
					similarity.append("\n");						
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
		
		String resultsTable = "";
		//timing
		long timeEnd = System.currentTimeMillis() - timeStart;
		
		if(correctCandidateID.equals("none"))
		{
			resultsTable += "\n" + file + "\t" + correctCandidateID + "\t\t\t" + spectrum.getExactMass();
		}
		else
		{
			for (int i = keysScore.length-1; i >= 0; i--) {
				boolean check = false;
				int temp = 0;
				for (int j = 0; j < realScoreMap.get(keysScore[i]).size(); j++) {
					scoreListReal.append("\n" + keysScore[i] + " - " + realScoreMap.get(keysScore[i]).get(j) + "\t[" + candidateToEnergy.get(realScoreMap.get(keysScore[i]).get(j)) + "]" + "\t");
					if(correctCandidateID.equals(realScoreMap.get(keysScore[i]).get(j).split("_")[0]))
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
					resultsTable = "\n" + file + "\t" + correctCandidateID + "\t" + this.candidateCount + "\t" + rankWorstCase + "\t" + rankTanimotoGroup + "\t" + rankIsomorphism + "\t" + spectrum.getExactMass() + "\t" + timeEnd;
				}
			}
		}
		
		//the correct candidate was not in the pubchem results
		if(resultsTable.equals(""))
			resultsTable = "\n" + file + "\t" + correctCandidateID + "\t" + this.candidateCount + "\tERROR\tCORRECT\tNOT FOUND\t" + spectrum.getExactMass() + "\t" + timeEnd;
		
		
		completeLog.append("\n\n*****************Scoring(Real)*****************************");
		completeLog.append("\nCorrect candidate ID: " + correctCandidateID);
		completeLog.append("\nTime: " + timeEnd);
		completeLog.append(scoreListReal);
		
		
		
		
		//write all tanimoto distances in one file
		//similarityValues += sim.getAllSimilarityValues();
		completeLog.append("\n********************Similarity***********************\n\n");	
		completeLog.append(similarity);			
		completeLog.append("\n*****************************************************\n\n");	

		File tmp = new File(this.file);
		String fileTmp = tmp.getName().substring(0, tmp.getName().lastIndexOf("."));
		System.out.println("Finished LOG!!! " + this.file);
		
		//write string to disk
		try
		{
			new File(folder + "logs/").mkdir();

			//complete log
			Writer.writeToFile(folder + "logs/" + date + "_log.txt", completeLog.toString());
			//positive and negative hits in peaks hist
			Writer.writeToFile(folder + "logs/" + date + "_hist_Positive.txt", results.getPositivePeaks().toString());
			Writer.writeToFile(folder + "logs/" + date + "_hist_Negative.txt", results.getNegativePeaks().toString());
			//write peak data of the correct compounds to file
			Writer.writeToFile(folder + "logs/" + date + "_results.txt", resultsTable);
			new File(folder + "logs/" + date + "/").mkdirs();
			Writer.writeToFile(folder + "logs/" + date + "/" + fileTmp, parameterOptimization);
			Writer.writeToFile(folder + "logs/" + date + "/" + fileTmp + "_Combined", parameterOptimizationCombined);
		}
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Prepare parameter optimization matrix.
	 * 
	 * @param realScoreMap the real score map
	 * 
	 * @return the string
	 */
	private String prepareParameterOptimizationMatrix(String pubChemIdentifier, Double exactMass)
	{
		String ret = "";
		
		ret += pubChemIdentifier + "\n";
		ret += exactMass.toString() + "\n\n";
		ret += "candidate\tpeakMass\tpeakInt\tbondEnergy\thydrogenPenalty\tpbondLengthChange\tBondOrder\tBondOrderDiff\tBondRemoved\tNeutralLossRules\n";
		
		return ret;
	}
			
	
	/**
	 * Generate optimization matrix.
	 * 
	 * @param candidateToOptimizationMatrixEntries the candidate to optimization matrix entries
	 */
	private String generateOptimizationMatrix(Map<String, List<OptimizationMatrixEntry>> candidateToOptimizationMatrixEntries, String header)
	{
		StringBuilder parameterOptimizationMatrix = new StringBuilder();
		parameterOptimizationMatrix.append(header);
		for (String candidate : candidateToOptimizationMatrixEntries.keySet()) {
			for (OptimizationMatrixEntry entry : candidateToOptimizationMatrixEntries.get(candidate)) {
				parameterOptimizationMatrix.append(candidate + "\t" + entry.getPeakMass() + "\t" + entry.getPeakInt() + "\t" + entry.getBondEnergyString() + "\t" + entry.getHydrogenPenalty() + "\t" + entry.getBondLengthChange() + "\t" + entry.getBondOrderString() + "\t" + entry.getBondOrderDiffString()  + "\t" + entry.getBondRemoved()  + "\t" + entry.getNeutralLossRules() + "\n");
			}
		}
		
		return parameterOptimizationMatrix.toString();
	}
	
	/**
	 * Prepare parameter optimization matrix ... one entry foreach candidate
	 * 
	 * @param realScoreMap the real score map
	 * 
	 * @return the string
	 */
	private String prepareParameterOptimizationMatrixCombined(String pubChemIdentifier, Double exactMass)
	{
		String ret = "";
		
		ret += pubChemIdentifier + "\n";
		ret += exactMass.toString() + "\n\n";
		ret += "Candidate\t#Peaks\tBondEnergy\tHydrogenPenalty\tBondLengthChange\tNormBondLengthChange\tBondOrder\tNeutralLossRules\n";
		
		return ret;
	}
	
	
	/**
	 * Generate optimization matrix...entries combined
	 * 
	 * @param candidateToOptimizationMatrixEntries the candidate to optimization matrix entries
	 */
	private String generateOptimizationMatrixCombined(Map<String, List<OptimizationMatrixEntry>> candidateToOptimizationMatrixEntries, String header)
	{
		StringBuilder parameterOptimizationMatrix = new StringBuilder();
		parameterOptimizationMatrix.append(header);
		
		Map<String, Double> candToMaxBondLEngthChange = new HashMap<String, Double>(); 
		//find maximum
		for (String candidate : candidateToOptimizationMatrixEntries.keySet()) {
			double maxBondLengthChange = 0.0;
			for (OptimizationMatrixEntry entry : candidateToOptimizationMatrixEntries.get(candidate)) {
				double temp = MoleculeTools.getCombinedEnergy(entry.getBondLengthChange());
				if(temp > maxBondLengthChange)
					maxBondLengthChange = temp;
			}
			candToMaxBondLEngthChange.put(candidate, maxBondLengthChange);
		}
		
		for (String candidate : candidateToOptimizationMatrixEntries.keySet()) {
			double bondEnergy = 0.0;
			int hydrogenPenalty = 0;
			double bondLengthChange = 0.0;
			double bondLengthChangeNorm = 0.0;
			double bondOrder = 0.0;
			String neutralLossRules = "";
			for (OptimizationMatrixEntry entry : candidateToOptimizationMatrixEntries.get(candidate)) {
				bondEnergy += MoleculeTools.getCombinedEnergy(entry.getBondEnergyString());
				hydrogenPenalty += entry.getHydrogenPenalty();
				bondLengthChange += MoleculeTools.getCombinedEnergy(entry.getBondLengthChange());
				bondLengthChangeNorm += MoleculeTools.getCombinedEnergy(entry.getBondLengthChange()) / candToMaxBondLEngthChange.get(candidate);
				bondOrder += MoleculeTools.getCombinedEnergy(entry.getBondOrderString()); 
				neutralLossRules += entry.getNeutralLossRules() + " ";
			}
			parameterOptimizationMatrix.append(candidate + "\t" + candidateToOptimizationMatrixEntries.get(candidate).size() + "\t" + bondEnergy + "\t" + hydrogenPenalty + "\t" + bondLengthChange + "\t" + bondLengthChangeNorm + "\t"+ bondOrder + "\t" + neutralLossRules + "\n");
		}
		
		return parameterOptimizationMatrix.toString();
	}
	
	
	
	
	
	/**
	 * The main method to start metfrag using command line parameters and start evaluation
	 * This is used for the gridengine.
	 * 
	 * @param args the arguments
	 */
	public static void main(String[] args) {		
		
		String currentFile = "";
		String date = "";
		boolean writeSDF = false;
		String CMLFiles = "";
		
		//test parameters: 
		//"Naltrexone_10Naltrexone_20Naltrexone_30Naltrexone_40Naltrexone_50.txt" "2011-04-20_13-33-33" "/home/swolf/MOPAC/Hill_PubChem_Formula/pubchemClusteredMopac/mopac_600/C20H23NO4/"
		//-Dproperty.file.path=/home/swolf/MOPAC/Hill_PubChem_Formula/config/ -Xms1500m -Xmx5500m
		
		//NaringeninTest
//		"/home/swolf/MOPAC/BondOrderTests/Naringenin/spectrum/PB000122PB000123PB000124PB000125.txt" "2011-04-20_13-33-33" "/home/swolf/MOPAC/BondOrderTests/Naringenin/"
//		-Dproperty.file.path=/home/swolf/MOPAC/BondOrderTests/Naringenin/config/ -Xms1500m -Xmx5500m
		try
		{
			//thats the current file 
			if(args[0] != null)
			{
				currentFile = args[0];
			}
			else
			{
				System.err.println("Error! Parameter missing!");
				System.exit(1);
			}
			
			//thats the date for the log file
			if(args[1] != null)
			{
				date = args[1]; 
			}
			else
			{
				System.err.println("Error! Parameter missing!");
				System.exit(1);
			}
			
			//thats the date for the log file
			if(args[2] != null)
			{
				CMLFiles = args[2]; 
			}
			else
			{
				System.err.println("Error! Parameter missing!");
				System.exit(1);
			}
			
			
			if(args.length > 3 && args[3] != null)
			{
				writeSDF = true;
			}
		}
		catch(Exception e)
		{
			System.err.println("Error! Parameter missing!");
			System.exit(1);
		}
			
		
		MetFragPreCalculatedPubChem2006 metFrag = new MetFragPreCalculatedPubChem2006(currentFile, date);
		try {
//			String resultsTable = "Spectrum\tCorrectCpdID\tHits\trankWorstCase\trankTanimoto\trankIsomorph\texactMass\tRuntime";
			metFrag.startScriptable(true, writeSDF, new File(CMLFiles), true);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
	}
}
