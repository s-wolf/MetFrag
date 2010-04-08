package de.ipbhalle.metfrag.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.openscience.cdk.Molecule;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.graph.ConnectivityChecker;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.io.MDLWriter;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;

import de.ipbhalle.metfrag.fragmenter.Fragmenter;
import de.ipbhalle.metfrag.massbankParser.Peak;
import de.ipbhalle.metfrag.massbankParser.Spectrum;
import de.ipbhalle.metfrag.read.Molfile;
import de.ipbhalle.metfrag.scoring.Scoring;
import de.ipbhalle.metfrag.spectrum.AssignFragmentPeak;
import de.ipbhalle.metfrag.spectrum.CleanUpPeakList;
import de.ipbhalle.metfrag.spectrum.PeakMolPair;
import de.ipbhalle.metfrag.spectrum.WrapperSpectrum;

public class PubChemFragmentationParallelThread implements Runnable{
	
	private IAtomContainer molecule = null;
	private String candidate = null;
	private String folder = "";
	private String file = "";
	private boolean recreateFrags = true;
	private Vector<Peak> peakList = null;
	private double mzabs;
	private double mzppm;
	private boolean sumFormulaRedundancyCheck = true;
	private int mode = 1;
	private boolean breakAromaticRings = true;
	private int treeDepth = 1;
	private boolean showDiagrams = false;
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
	public PubChemFragmentationParallelThread(IAtomContainer molecule, String candidate, String folder, String file,
			boolean recreateFrags, Vector<Peak> peakList, double mzabs, double mzppm, boolean sumFormulaRedundancyCheck, int mode,
			boolean breakAromaticRings, int treeDepth, boolean showDiagrams, WrapperSpectrum spectrum, boolean hydrogenTest,
			boolean neutralLossAdd, boolean bondEnergyScoring, boolean isOnlyBreakSelectedBonds)
	{
		this.molecule = molecule;
		this.candidate = candidate;
		this.file = file;
		this.folder = folder;
		this.recreateFrags = recreateFrags;
		this.peakList = peakList;
		this.mzabs = mzabs;
		this.mzppm = mzppm;
		this.sumFormulaRedundancyCheck = sumFormulaRedundancyCheck;
		this.mode = mode;
		this.breakAromaticRings = breakAromaticRings;
		this.treeDepth = treeDepth;
		this.showDiagrams = showDiagrams;
		this.spectrum = spectrum;
		this.hydrogenTest = hydrogenTest;
		this.neutralLossAdd = neutralLossAdd;
		this.bondEnergyScoring = bondEnergyScoring;
		this.isOnlyBreakSelectedBonds = isOnlyBreakSelectedBonds;
	}
	
	
	@Override public void run()
	{		
		try
		{	        
	        //now create a new folder to write the .mol files into
	        new File(folder + file).mkdir();
	        //boolean status = new File(folder + file + "/" + candidate).mkdir();
	        
	        //if the folder was created...if the folder exists --> skip....there are already fragments
	        if (recreateFrags)
	        {
		        //System.out.println("Folder created: " + folder + file);
		        
		        try
		        {
			        //add hydrogens
			        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
			        CDKHydrogenAdder hAdder = CDKHydrogenAdder.getInstance(molecule.getBuilder());
			        hAdder.addImplicitHydrogens(molecule);
			        AtomContainerManipulator.convertImplicitToExplicitHydrogens(molecule);
		        }
		        //there is a bug in cdk?? error happens when there is a S or Ti in the molecule
		        catch(IllegalArgumentException e)
	            {
		        	PubChemSearchParallel.addToCompleteLog("Error: " + candidate + " Message: " + e.getMessage());
	            	//skip it
	            	return;
	            }
		        
		        Fragmenter fragmenter = new Fragmenter((Vector<Peak>)peakList.clone(), mzabs, mzppm, mode, breakAromaticRings, sumFormulaRedundancyCheck, neutralLossAdd, isOnlyBreakSelectedBonds);
		        long start = System.currentTimeMillis();
		        List<File> generatedFrags = null;
		        try
		        {
		        	generatedFrags = fragmenter.generateFragmentsEfficient(molecule, true, treeDepth, candidate);
		        }
		        catch(OutOfMemoryError e)
		        {
		        	System.out.println("OUT OF MEMORY ERROR! " + treeDepth);
		        	PubChemSearchParallel.addToCompleteLog("Error: " + candidate + " Message: " + e.getMessage());
		        	return;
		        }
		        long time = System.currentTimeMillis() - start;
		        System.out.println("Benötigte Zeit: " + time + " Got " + generatedFrags.size() + " fragments");

		        //read temp files in again
		        List<IAtomContainer> l = Molfile.ReadfolderTemp(generatedFrags);
		        //delete those files because they use up a lot space...in another thread TODO
		        //Molfile.DeleteFilesTemp(generatedFrags);
		        
		        //Create a new thread to delete all the temp files!!!
		        int threads = 1;
			    ExecutorService threadExecutor = null;
			    System.out.println("Used Threads to Delete: " + threads);
			    threadExecutor = Executors.newFixedThreadPool(threads);
			    threadExecutor.execute(new DeleteTempFiles(generatedFrags));
			    threadExecutor.shutdown();
			    
		        
		        try
				{					
					//get the original peak list again
					peakList = spectrum.getPeakList();
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
					Map<String, Double> mapCandidateToEnergy = PubChemSearchParallel.getCandidateToEnergy();
					mapCandidateToEnergy.put(candidate, currentBondEnergy);
					PubChemSearchParallel.setCandidateToEnergy(mapCandidateToEnergy);
					
					Map<String, Double> mapCandidateToHydrogenPenalty = PubChemSearchParallel.getCandidateToHydrogenPenalty();
					mapCandidateToHydrogenPenalty.put(candidate, score.getPenalty());
					PubChemSearchParallel.setCandidateToHydrogenPenalty(mapCandidateToHydrogenPenalty);
					
					Map<String, Double> mapCandidateToPartialChargesDiff = PubChemSearchParallel.getCandidateToPartialChargesDiff();
					mapCandidateToPartialChargesDiff.put(candidate, score.getPartialChargesDiff());
					PubChemSearchParallel.setCandidateToPartialChargesDiff(mapCandidateToPartialChargesDiff);
					
					//now add all the parameters to the map
					PubChemSearchParallel.addOptimizationMatrixEntry(candidate, score.getOptimizationMatrixEntries());					
					
					
					Map<Double, Vector<String>> realScoreMap = PubChemSearchParallel.getRealScoreMap();
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
					PubChemSearchParallel.setRealScoreMap(realScoreMap);
					
					//save score in hashmap...if there are several hits with the same
					//amount of identified peaks --> ArrayList
					HashMap<Integer, ArrayList<String>> scoreMap = PubChemSearchParallel.getScoreMap();
					if(scoreMap.containsKey(hits.size()))
			        {
			        	ArrayList<String> tempList = scoreMap.get(hits.size());
			        	tempList.add(candidate);
			        	scoreMap.put(hits.size(), tempList);
			        }
			        else
			        {
			        	ArrayList<String> temp = new ArrayList<String>();
			        	temp.add(candidate);
			        	scoreMap.put(hits.size(), temp);
			        }
					PubChemSearchParallel.setScoreMap(scoreMap);
					
				
					//get all the identified peaks
					String peaks = "";
					Double bondEnergy = 0.0;
					
					for (int i = 0; i < hits.size(); i++) {
						
						bondEnergy += Double.parseDouble((String)hits.get(i).getFragment().getProperty("BondEnergy"));
						peaks += hits.get(i).getPeak().getMass() + "[" + hits.get(i).getFragment().getProperty("BondEnergy") + "]" +  " ";
						//add to vector
						Vector<Peak> temp = PubChemSearchParallel.getVectorOfPeaks();
						temp.add(hits.get(i).getPeak());
						PubChemSearchParallel.setVectorOfPeaks(temp);
						
						if(spectrum.getCID() == Integer.parseInt(candidate))
						{
							Vector<Peak> tempCorrect = PubChemSearchParallel.getVectorOfCorrectPeaks();
							tempCorrect.add(hits.get(i).getPeak());
							PubChemSearchParallel.setVectorOfCorrectPeaks(tempCorrect);
						}
					}
					
					
					
//					//write things to log file
					PubChemSearchParallel.addToCompleteLog("\nFile: " + candidate + "\t #Peaks: " + spectrum.getPeakList().size() + "\t #Found: " + hits.size());
					PubChemSearchParallel.addToCompleteLog("\tPeaks: " + peaks);
					
					List<IAtomContainer> hitsListTest = new ArrayList<IAtomContainer>();
					for (int i = 0; i < hits.size(); i++) {
						List<IAtomContainer> hitsList = new ArrayList<IAtomContainer>();
						hitsList.add(AtomContainerManipulator.removeHydrogens(hits.get(i).getFragment()));
						hitsListTest.add(hits.get(i).getFragment());
						//Render.Highlight(AtomContainerManipulator.removeHydrogens(molecule), hitsList , Double.toString(hits.get(i).getPeak()));
					}
	//					if (showDiagrams)
	//						Render.Draw(molecule, hitsListTest , "Fragmente von: " + candidates.get(c));
				}
				catch(CDKException e)
				{
					System.out.println("CDK error!" + e.getMessage());
					PubChemSearchParallel.addToCompleteLog("CDK Error! " + e.getMessage() + " File: " + candidate);
				}
				catch(Exception e)
				{
					System.out.println("Error: " + e.getMessage());
					e.printStackTrace();
					PubChemSearchParallel.addToCompleteLog("Error! "+ e.getMessage() + " File: " + candidate);
				}
				catch(OutOfMemoryError e)
				{
					System.out.println("Out of memory: " + e.getMessage() + "\n" + e.getStackTrace());
					System.gc();
					PubChemSearchParallel.addToCompleteLog("Out of memory! "+ e.getMessage() + " File: " + candidate);
				}
	        }
	        else
	        	System.out.println("Could not create folder...frags already computed!!!!");
	        
		}
		catch(CDKException e)
		{
			System.out.println("CDK error!" + e.getMessage());
			PubChemSearchParallel.addToCompleteLog("CDK Error! " + e.getMessage() + "File: " + candidate);
		}
		catch(FileNotFoundException e)
		{
			System.out.println("File not found" + e.getMessage());
			PubChemSearchParallel.addToCompleteLog("File not found error! "+ e.getMessage() + "File: " + candidate);
		}
		catch(IOException e)
		{
			System.out.println("IO error: " + e.getMessage());
			PubChemSearchParallel.addToCompleteLog("IO Error! "+ e.getMessage() + "File: " + candidate);
		}
		catch(Exception e)
		{
			System.out.println("Error: " + e.getMessage());
			e.printStackTrace();
			PubChemSearchParallel.addToCompleteLog("Error! "+ e.getMessage() + "File: " + candidate);
		}
		catch(OutOfMemoryError e)
		{
			System.out.println("Out of memory: " + e.getMessage() + "\n" + e.getStackTrace());
			System.gc();
			PubChemSearchParallel.addToCompleteLog("Out of memory! "+ e.getMessage() + "File: " + candidate);
		}
	}
}

