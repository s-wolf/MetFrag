package de.ipbhalle.metfrag.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.rpc.ServiceException;


import org.openscience.cdk.Molecule;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.formula.MolecularFormula;
import org.openscience.cdk.graph.ConnectivityChecker;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.io.MDLWriter;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

import de.ipbhalle.metfrag.database.PubChemToDatabaseParallel;
import de.ipbhalle.metfrag.fragmenter.Fragmenter;
import de.ipbhalle.metfrag.keggWebservice.KeggWebservice;
import de.ipbhalle.metfrag.massbankParser.Peak;
import de.ipbhalle.metfrag.molDatabase.PubChemLocal;
import de.ipbhalle.metfrag.pubchem.ESearchDownload;
import de.ipbhalle.metfrag.pubchem.PubChemWebService;
import de.ipbhalle.metfrag.read.Molfile;
import de.ipbhalle.metfrag.scoring.OptimizationMatrixEntry;
import de.ipbhalle.metfrag.scoring.Scoring;
import de.ipbhalle.metfrag.similarity.Similarity;
import de.ipbhalle.metfrag.similarity.SimilarityGroup;
import de.ipbhalle.metfrag.spectrum.WrapperSpectrum;
import de.ipbhalle.metfrag.tools.GetKEGGIdentifier;
import de.ipbhalle.metfrag.tools.MolecularFormulaTools;
import de.ipbhalle.metfrag.tools.PPMTool;

public class PubChemSearchParallel{
		
		private Vector<String> blackList;
	    private static String completeLog = "";
	    private int foundPeaks = 0;
	    private int allPeaks = 0;
	    private boolean showDiagrams = false;
	    private Vector<String> doneMols = new Vector<String>();
	    private static HashMap<Integer, ArrayList<String>> scoreMap = new HashMap<Integer, ArrayList<String>>();
	    private String jdbc = "";
	    private String username = "";
	    private String password = "";
	    private static String histogram = "";
	    private static String histogramCompare = "";
	    private static String histogramReal = "";
	    private static String histogramPeaks = "";
	    private static String histogramPeaksAll = "";
	    private static String histogramPeaksReal = "";
	    //list of peaks which are contained in the corresponding molecule
		private static Vector<Peak> listOfPeaksCorresponding = new Vector<Peak>();
		//list of peaks which are not contained in the real molecule
		private static Vector<Peak> listOfPeaks = new Vector<Peak>();
		private boolean hydrogenTest = false;
		private static Map<String, Double> candidateToEnergy = new HashMap<String, Double>(); 
		private static Map<String, Double> candidateToHydrogenPenalty = new HashMap<String, Double>();
		private static Map<String, Double> candidateToPartialChargesDiff = new HashMap<String, Double>();
		
		private static Map<String, List<OptimizationMatrixEntry>> candidateToOptimizationMatrixEntries = new HashMap<String, List<OptimizationMatrixEntry>>();
		
		private boolean neutralLossAdd = false;
		private static String similarityValues = "";
		private static String parameterOptimizationMatrix = "";


	    private long sumTime = 0;
	    private static Map<Double, Vector<String>> realScoreMap = new HashMap<Double, Vector<String>>();
		
	    
		
		/**
		 * Instantiates a new PubChem Search using parallel threads.
		 * 
		 * @param folder the folder
		 * @param mzabs the mzabs
		 * @param mzppm the mzppm
		 * @param pdf the pdf
		 * @param showDiagrams the show diagrams
		 * @param recreateFrags the recreate frags
		 * @param breakAromaticRings the break aromatic rings
		 * @param sumFormulaRedundancyCheck the experimental redundancy check
		 * @param spectrum the spectrum
		 * @param username the username
		 * @param password the password
		 * @param jdbc the jdbc
		 * @param treeDepth the tree depth
		 * @param keggPath the kegg path
		 * @throws SQLException 
		 * @throws ClassNotFoundException 
		 * @throws ServiceException 
		 * @throws RemoteException 
		 * @throws NumberFormatException 
		 * @throws CDKException 
		 * @throws FileNotFoundException 
		 */
		public PubChemSearchParallel(String folder, WrapperSpectrum spectrum, double mzabs, double mzppm, int searchPPM, boolean pdf, boolean showDiagrams, boolean recreateFrags, boolean breakAromaticRings, boolean sumFormulaRedundancyCheck, String username, String password, String jdbc, int treeDepth, boolean hydrogenTest, boolean neutralLossAdd, boolean bondEnergyScoring, boolean isOnlyBreakSelectedBonds) throws NumberFormatException, RemoteException, ServiceException, ClassNotFoundException, SQLException, FileNotFoundException, CDKException
		{
			this.username = username;
			this.password = password;
			this.jdbc = jdbc;
			BlackList bl = new BlackList(sumFormulaRedundancyCheck);
			this.blackList = bl.getBlackList();
			this.hydrogenTest = hydrogenTest;
			this.neutralLossAdd = neutralLossAdd;
			//delete previous entries
			completeLog = "";
			histogram = "";
		    histogramCompare = "";
		    histogramReal = "";
		    histogramPeaks = "";
		    histogramPeaksAll = "";
		    histogramPeaksReal = "";
		    realScoreMap = new HashMap<Double, Vector<String>>();
		    scoreMap = new HashMap<Integer, ArrayList<String>>();
			pubChemSearch(folder, spectrum, mzabs, mzppm, searchPPM, recreateFrags, breakAromaticRings, sumFormulaRedundancyCheck, pdf, treeDepth, bondEnergyScoring, isOnlyBreakSelectedBonds);
		}
		
		
		/**
		 * Adds the to complete log.
		 * 
		 * @param add the add
		 */
		public static void addToCompleteLog(String add)
		{
			completeLog += add;
		}
		
		/**
		 * Gets the real score map.
		 * 
		 * @return the real score map
		 */
		public static Map<Double, Vector<String>> getRealScoreMap()
		{
			return realScoreMap;
		}
		
		/**
		 * Sets the real score map.
		 * 
		 * @param realScoreMap the real score map
		 */
		public static void setRealScoreMap(Map<Double, Vector<String>> realScoreMap)
		{
			PubChemSearchParallel.realScoreMap = realScoreMap;
		}
		
		
		/**
		 * Gets the score map.
		 * 
		 * @return the score map
		 */
		public static HashMap<Integer, ArrayList<String>> getScoreMap()
		{
			return scoreMap;
		}
		
		
		/**
		 * Sets the score map.
		 * 
		 * @param scoreMap the score map
		 */
		public static void setScoreMap( HashMap<Integer, ArrayList<String>> scoreMap)
		{
			PubChemSearchParallel.scoreMap = scoreMap;
		}
		
		
		/**
		 * Gets the complete log.
		 * 
		 * @return the complete log
		 */
		public String getCompleteLog()
		{
			return completeLog;
		}
		
		/**
		 * Gets the found peaks.
		 * 
		 * @return the found peaks
		 */
		public int getFoundPeaks()
		{
			return foundPeaks;
		}
		
		/**
		 * Gets the vector of peaks.
		 * 
		 * @return the vector of peaks
		 */
		public static Vector<Peak> getVectorOfPeaks()
		{
			return listOfPeaks;
		}
		
		/**
		 * Sets the vector of peaks.
		 * 
		 * @return the vector of peaks
		 */
		public static void setVectorOfPeaks(Vector<Peak> peaks)
		{
			listOfPeaks = peaks;
		}
		
		/**
		 * Gets the vector of correct peaks.
		 * 
		 * @return the vector of correct peaks
		 */
		public static Vector<Peak> getVectorOfCorrectPeaks()
		{
			return listOfPeaksCorresponding;
		}
		
		/**
		 * Sets the vector of correct peaks.
		 * 
		 * @return the vector of correct peaks
		 */
		public static void setVectorOfCorrectPeaks(Vector<Peak> peaks)
		{
			listOfPeaksCorresponding = peaks;
		}
	    
		/**
		 * Gets the all peaks.
		 * 
		 * @return the all peaks
		 */
		public int getAllPeaks()
		{
			return allPeaks;
		}
		
		/**
		 * Gets the histogram.
		 * 
		 * @return the histogram
		 */
		public String getHistogram()
		{
			return histogram;
		}
		
	    /**
	     * Histogram compare.
	     * 
	     * @return the string
	     */
	    public String getHistogramCompare()
	    {
	    	return histogramCompare;
	    }
	    
	    /**
	     * Gets the histogram real.
	     * 
	     * @return the histogram real
	     */
	    public String getHistogramReal()
	    {
	    	return histogramReal;
	    }
	    
	    /**
	     * Gets the histogram peaks.
	     * 
	     * @return the histogram peaks
	     */
	    public String getHistogramPeaks()
	    {
	    	return histogramPeaks;
	    }
	    
	    /**
	     * Gets the histogram peaks real.
	     * 
	     * @return the histogram peaks real
	     */
	    public String getHistogramPeaksReal()
	    {
	    	return histogramPeaksReal;
	    }
	    
	    public String getHistogramPeaksAll()
	    {
	    	return histogramPeaksAll;
	    }
	    
		
	
			
		/**
		 * Test one spectrum in Pubchem. Download all the hits by mass and fragment them.
		 * 
		 * @param folder the folder
		 * @param mzabs the mzabs
		 * @param mzppm the mzppm
		 * @param mergedSpectrum the merged spectrum
		 * @param recreateFrags the recreate frags
		 * @param breakAromaticRings the break aromatic rings
		 * @param molecularFormulaRedundancyCheck the experimental redundancy check
		 * @param pdf the pdf
		 * @param treeDepth the tree depth
		 * @throws SQLException 
		 * @throws ClassNotFoundException 
		 * @throws ServiceException 
		 * @throws RemoteException 
		 * @throws NumberFormatException 
		 * @throws CDKException 
		 * @throws FileNotFoundException 
		 */
		private void pubChemSearch(String folder, WrapperSpectrum mergedSpectrum, Double mzabs, Double mzppm, int searchPPM, boolean recreateFrags, boolean breakAromaticRings, boolean molecularFormulaRedundancyCheck, boolean pdf, int treeDepth, boolean bondEnergyScoring, boolean isOnlyBreakSelectedBonds)
		{
			String file = mergedSpectrum.getFilename();
			
			if(blackList.contains(file))
			{
				completeLog += "Blacklisted Molecule: " + file; 
				histogramReal += "\n" + file + "\tBLACKLIST\t";
				histogram += "\n" + file + "\tBLACKLIST\t";
				histogramCompare += "\n" + file + "\tBLACKLIST\t";
				return;
			}
			//timing
			long timeStart = System.currentTimeMillis();  
			
			//Test Data
			WrapperSpectrum spectrum = mergedSpectrum;
			Vector<Peak> peakList = spectrum.getPeakList();
			
			int mode = spectrum.getMode();

			IMolecularFormula formula = MolecularFormulaManipulator.getMolecularFormula(spectrum.getFormula(),new MolecularFormula());
			double exactMass = MolecularFormulaTools.getMonoisotopicMass(formula);
			
			//instatiate and read in CID-KEGG.txt
			String pubChemIdentifier = spectrum.getCID() + "";
			
			
			completeLog += "\n\n============================================================================";
			completeLog += "\nFile: " + file + " (Pubchem Entry: " + pubChemIdentifier + ")";
			
			//get candidates from pubchem webservice...with with a given mzppm and mzabs
			PubChemLocal pubchem = null;
			List<String> candidates = null;
			Map<String, String> candidateToSmiles = new HashMap<String, String>();
			
			try {
				
				try {
					pubchem = new PubChemLocal(jdbc, username, password);
					double lowerBound = exactMass -(mzabs + PPMTool.getPPMDeviation(exactMass, mzppm)); 
					double upperBound = exactMass +(mzabs + PPMTool.getPPMDeviation(exactMass, mzppm)); 
					candidates = pubchem.getHits(lowerBound, upperBound);
					//test with data which is not newer than of the 6th february 2006
//					candidates = ESearchDownload.ESearchDownloadExactMassFebruary2006(lowerBound, upperBound);
//					//date candidate hack
//					if(pubChemIdentifier.equals("5701990"))
//						candidates.add("5701990");
//					if(pubChemIdentifier.equals("6420073"))
//						candidates.add("6420073");

				} catch (NumberFormatException e1) {
					completeLog += "Error: " + e1.getMessage();
					e1.printStackTrace();
				} catch (ClassNotFoundException e1) {
					completeLog += "Error: " + e1.getMessage();
					e1.printStackTrace();
				} catch (SQLException e1) {
					completeLog += "Error: " + e1.getMessage();
					e1.printStackTrace();
				} catch (Exception e) {
					completeLog += "Error: " + e.getMessage();
					e.printStackTrace();
				}
							
				
				//now fill executor!!!
				//number of threads depending on the available processors
			    int threads = Runtime.getRuntime().availableProcessors();
			    
			    //thread executor
			    ExecutorService threadExecutor = null;
			    System.out.println("Used Threads: " + threads);
			    threadExecutor = Executors.newFixedThreadPool(threads);
			    //threadExecutor = Executors.newCachedThreadPool();
				Vector<String> realCandidates = new Vector<String>();
				
				
				
				int count = 0;
			    //add them into queue
				for (String candidate : candidates) {
					
					IAtomContainer molecule = null;
					try {
						molecule = pubchem.getMol(candidate, false);
					} catch (NumberFormatException e) {
						completeLog += "Error!!! " + e.getMessage() + " Candidate: " + candidate + "\n" + e.getStackTrace();
						System.out.println("Error!!! " + e.getMessage() + " Candidate: " + candidate);
						e.printStackTrace();
					} catch (InvalidSmilesException e) {
						completeLog += "Error parsing smiles!!! " + e.getMessage() + " Candidate: " + candidate + "\n" + e.getStackTrace();
						System.out.println("Error parsing smiles!!! " + e.getMessage() + " Candidate: " + candidate);
						e.printStackTrace();
					} catch (SQLException e) {
						completeLog += "Error SQL!!! " + e.getMessage() + " Candidate: " + candidate + "\n" + e.getStackTrace();
						System.out.println("Error SQL!!! " + e.getMessage() + " Candidate: " + candidate);
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						completeLog += "Error!!! " + e.getMessage() + " Candidate: " + candidate + "\n" + e.getStackTrace();
						System.out.println("Error!!! " + e.getMessage() + " Candidate: " + candidate);
						e.printStackTrace();
					}
					
					//molecule is not stored in the database or not chonsp!
					if(molecule == null)
						continue;
					
					
					boolean isConnected = true;
					if (molecule != null)
						isConnected = ConnectivityChecker.isConnected(molecule);
					
					if(!isConnected)
						continue;
					
					candidateToSmiles.put(candidate, pubchem.getLastSmiles());
					
					realCandidates.add(candidate);
					threadExecutor.execute(new PubChemFragmentationParallelThread(molecule,candidate,folder,file, recreateFrags, peakList, mzabs, mzppm, molecularFormulaRedundancyCheck,mode, breakAromaticRings, treeDepth, showDiagrams, spectrum, hydrogenTest, neutralLossAdd, bondEnergyScoring, isOnlyBreakSelectedBonds));
					
					count++;
//					if(count >= 2000)
//						break;
				}
				
				//comparison histogram
				if(pubChemIdentifier.equals("none"))
					histogramCompare += "\n" + file + "\t" + pubChemIdentifier + "\t\t" + exactMass;
				else
					histogramCompare += "\n" + file + "\t" + pubChemIdentifier + "\t" + count + "\t" + exactMass;
				
				threadExecutor.shutdown();
				
				//wait until all threads are finished
				while(!threadExecutor.isTerminated())
				{
					try {
						Thread.currentThread().sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}//sleep for 1000 ms
				}
				
			}
			catch(OutOfMemoryError e)
			{
				System.out.println("Out of memory: " + e.getMessage() + "\n" + e.getStackTrace());
				System.gc();
				completeLog += "Out of memory! "+ e.getMessage() + "File: ";
			}			
			
			
			
			//easy scoring
			Integer[] keylist = new Integer[scoreMap.keySet().size()];
			Object[] keys = scoreMap.keySet().toArray();
			
			for (int i = 0; i < keys.length; i++) {
				keylist[i] = Integer.parseInt(keys[i].toString());
			}
			
			Arrays.sort(keylist);
			String scoreList = "";
			int rankWorstCase = 0;
			
			for (int i = keylist.length-1; i >= 0; i--) {
				boolean check = false;
				for (int j = 0; j < scoreMap.get(keylist[i]).size(); j++) {
					scoreList += "\n" + keylist[i] + " - " + scoreMap.get(keylist[i]).get(j) + " [" + Math.round(candidateToEnergy.get(scoreMap.get(keylist[i]).get(j))) + "]";
					if(pubChemIdentifier.equals(scoreMap.get(keylist[i]).get(j)))
					{
						check = true;
					}
					//worst case: count all which are better or have a equal position
					rankWorstCase++;
				}
				if(check)
				{
					histogram += "\n" + file + "\t" + pubChemIdentifier + "\t" + rankWorstCase + "\t" + exactMass;
				}
			}
			
			if(pubChemIdentifier.equals("none"))
			{
				histogram += "\n" + file + "\t" + pubChemIdentifier + "\t\t" + exactMass;
			}
			
			completeLog += "\n\n*****************Scoring*****************************";
			completeLog += "Supposed to be: " + pubChemIdentifier;
			completeLog += scoreList;
			completeLog += "\n*****************************************************\n\n";
			//easy scoring end
						
			
			//real scoring
			if(bondEnergyScoring)
				realScoreMap = Scoring.getCombinedScore(realScoreMap, candidateToEnergy, candidateToHydrogenPenalty);
			
			//generate the parameter optimization matrix
			parameterOptimizationMatrix = prepareParameterOptimizationMatrix(pubChemIdentifier);
			generateOptimizationMatrix(candidateToOptimizationMatrixEntries);
						
			
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
			
		}
		
		
		/**
		 * Prepare parameter optimization matrix.
		 * 
		 * @param realScoreMap the real score map
		 * 
		 * @return the string
		 */
		private String prepareParameterOptimizationMatrix(String pubChemIdentifier)
		{
			String ret = "";
			
			ret += pubChemIdentifier + "\n\n";
			ret += "candidate\tpeakMass\tpeakInt\tbondEnergy\thydrogenPenalty\tpCharges\n";
			
			return ret;
		}
				
		
		/**
		 * Generate optimization matrix.
		 * 
		 * @param candidateToOptimizationMatrixEntries the candidate to optimization matrix entries
		 */
		private void generateOptimizationMatrix(Map<String, List<OptimizationMatrixEntry>> candidateToOptimizationMatrixEntries)
		{
			for (String candidate : candidateToOptimizationMatrixEntries.keySet()) {
				for (OptimizationMatrixEntry entry : candidateToOptimizationMatrixEntries.get(candidate)) {
					parameterOptimizationMatrix += candidate + "\t" + entry.getPeakMass() + "\t" + entry.getPeakInt() + "\t" + entry.getBondEnergyString() + "\t" + entry.getHydrogenPenalty() + "\t" + entry.getChargesDiffString() + "\n";
				}
			}
		}
		
		/**
		 * Sets the candidate to energy.
		 * 
		 * @param map the map
		 */
		public static void setCandidateToEnergy(Map<String, Double> map) {
			candidateToEnergy = map;
		}
		
		/**
		 * Adds the optimization matrix entries.
		 * 
		 * @param candidateID the candidate id
		 * @param optimizationMatrixEntries the optimization matrix entries
		 */
		public static void addOptimizationMatrixEntry(String candidateID, List<OptimizationMatrixEntry> optimizationMatrixEntries)
		{
			candidateToOptimizationMatrixEntries.put(candidateID, optimizationMatrixEntries);
		}


		/**
		 * Gets the candidate to energy.
		 * 
		 * @return the candidate to energy
		 */
		public static Map<String, Double> getCandidateToEnergy() {
			return candidateToEnergy;
		}


		public static void setCandidateToHydrogenPenalty(
				Map<String, Double> candidateToHydrogenPenalty) {
			PubChemSearchParallel.candidateToHydrogenPenalty = candidateToHydrogenPenalty;
		}


		public static Map<String, Double> getCandidateToHydrogenPenalty() {
			return candidateToHydrogenPenalty;
		}
		
		public static String getSimilarityValues()
		{
			return similarityValues;
		}


		public static void setParameterOptimizationMatrix(
				String parameterOptimizationMatrix) {
			PubChemSearchParallel.parameterOptimizationMatrix = parameterOptimizationMatrix;
		}


		public static String getParameterOptimizationMatrix() {
			return parameterOptimizationMatrix;
		}


		public static void setCandidateToPartialChargesDiff(
				Map<String, Double> candidateToPartialChargesDiff) {
			PubChemSearchParallel.candidateToPartialChargesDiff = candidateToPartialChargesDiff;
		}


		public static Map<String, Double> getCandidateToPartialChargesDiff() {
			return candidateToPartialChargesDiff;
		}
}