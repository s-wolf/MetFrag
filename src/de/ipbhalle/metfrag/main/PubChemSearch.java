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
import java.util.List;
import java.util.Vector;

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

import de.ipbhalle.metfrag.fragmenter.Fragmenter;
import de.ipbhalle.metfrag.keggWebservice.KeggWebservice;
import de.ipbhalle.metfrag.massbankParser.Peak;
import de.ipbhalle.metfrag.pubchem.PubChemWebService;
import de.ipbhalle.metfrag.read.Molfile;
import de.ipbhalle.metfrag.scoring.Scoring;
import de.ipbhalle.metfrag.spectrum.CleanUpPeakList;
import de.ipbhalle.metfrag.spectrum.WrapperSpectrum;
import de.ipbhalle.metfrag.tools.DisplayStructure;
import de.ipbhalle.metfrag.tools.GetKEGGIdentifier;
import de.ipbhalle.metfrag.tools.MolecularFormulaTools;
import de.ipbhalle.metfrag.tools.PPMTool;
import de.ipbhalle.metfrag.tools.Render;
import de.ipbhalle.metfrag.tools.WritePDFTable;

public class PubChemSearch {
		
		private Vector<String> blackList;
	    private String completeLog = "";
	    private int foundPeaks = 0;
	    private int allPeaks = 0;
	    private boolean showDiagrams = false;
	    private Vector<String> doneMols = new Vector<String>();
	    private HashMap<Integer, ArrayList<String>> scoreMap = new HashMap<Integer, ArrayList<String>>();
	    private String jdbc = "";
	    private String username = "";
	    private String password = "";
	    private String histogram = "";
	    private String histogramCompare = "";
	    private String histogramReal = "";
	    private String histogramPeaks = "";
	    private String histogramPeaksAll = "";
	    private String histogramPeaksReal = "";
	    //list of peaks which are contained in the corresponding molecule
		private Vector<Peak> listOfPeaksCorresponding = new Vector<Peak>();
		//list of peaks which are not contained in the real molecule
		private Vector<Peak> listOfPeaks = new Vector<Peak>();
		private boolean hydrogenTest = false;
		
		
	    private long sumTime = 0;
		
	    
		
		/**
		 * Instantiates a new KEGG search.
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
		 * @throws Exception 
		 */
		public PubChemSearch(String folder, WrapperSpectrum spectrum, double mzabs, double mzppm, int searchPPM, boolean pdf, boolean showDiagrams, boolean recreateFrags, boolean breakAromaticRings, boolean sumFormulaRedundancyCheck, String username, String password, String jdbc, int treeDepth, boolean hydrogenTest) throws Exception
		{
			this.username = username;
			this.password = password;
			this.jdbc = jdbc;
			BlackList bl = new BlackList(sumFormulaRedundancyCheck);
			this.blackList = bl.getBlackList();
			this.hydrogenTest = hydrogenTest;
			pubChemSearch(folder, spectrum, mzabs, mzppm, searchPPM, recreateFrags, breakAromaticRings, sumFormulaRedundancyCheck, pdf, treeDepth);
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
		public Vector<Peak> getVectorOfPeaks()
		{
			return this.listOfPeaks;
		}
		
		/**
		 * Gets the vector of correct peaks.
		 * 
		 * @return the vector of correct peaks
		 */
		public Vector<Peak> getVectorOfCorrectPeaks()
		{
			return this.listOfPeaksCorresponding;
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
	    	return this.histogramPeaksAll;
	    }
	    
		
		/**
		 * Add mol name without extension to the already done molecules
		 * 
		 * @param mol the mol
		 */
		private void addMol(String mol)
		{
			this.doneMols.add(mol);
		}
		
		/**
		 * Already done mol files.
		 * 
		 * @param candidate the candidate molecule
		 * 
		 * @return true, if successful
		 */
		private boolean alreadyDone(String candidate)
		{
			boolean test = false;
			
			if(doneMols.contains(candidate))
				test = true;
			
			return test;
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
		 * @param experimentalRedundancyCheck the experimental redundancy check
		 * @param pdf the pdf
		 * @param treeDepth the tree depth
		 * @throws Exception 
		 */
		private void pubChemSearch(String folder, WrapperSpectrum mergedSpectrum, Double mzabs, Double mzppm, int searchPPM, boolean recreateFrags, boolean breakAromaticRings, boolean experimentalRedundancyCheck, boolean pdf, int treeDepth) throws Exception
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
			HashMap<Double, Vector<String>> realScoreMap = new HashMap<Double, Vector<String>>();		
			
			int mode = spectrum.getMode();

			IMolecularFormula formula = MolecularFormulaManipulator.getMolecularFormula(spectrum.getFormula(),new MolecularFormula());
			double exactMass = MolecularFormulaTools.getMonoisotopicMass(formula);
			
			//instatiate and read in CID-KEGG.txt
			String pubChemIdentifier = spectrum.getCID() + "";
			
			
			completeLog += "\n\n============================================================================";
			completeLog += "\nFile: " + file + " (Pubchem Entry: " + pubChemIdentifier + ")";
			
			//get candidates from pubchem webservice...with with a given mzppm and mzabs
			PubChemWebService pubchem = new PubChemWebService();
			Vector<String> candidates = null;
			candidates = pubchem.getHitsByMass(exactMass, PPMTool.getPPMDeviation(exactMass, searchPPM), 3000);
			
			
			
			//comparison histogram
			if(pubChemIdentifier.equals("none"))
				histogramCompare += "\n" + file + "\t" + pubChemIdentifier + "\t\t" + exactMass;
			else
				histogramCompare += "\n" + file + "\t" + pubChemIdentifier + "\t" + candidates.size() + "\t" + exactMass;
			
			
			//loop over all hits
			for (int c = 0; c < candidates.size(); c++) {
				
				//skip already done files kegg entries
				if(alreadyDone(candidates.get(c)))
					continue;
				
				//add mol to finished
				addMol(candidates.get(c));
								
				try
				{
		            
					//now fragment the retrieved molecule
			        IAtomContainer molecule = pubchem.getMol(candidates.get(c));
			        
			        boolean isConnected = ConnectivityChecker.isConnected(molecule);
					if(!isConnected)
						continue;
			        
			        //now create a new folder to write the .mol files into
			        new File(folder + file).mkdir();
			        boolean status = new File(folder + file + "/" + candidates.get(c)).mkdir();
			        
			        //if the folder was created...if the folder exists --> skip....there are already fragments
			        if (status || recreateFrags)
			        {
				        //System.out.println("Folder created: " + folder + file);
				        
				        try
				        {
					        //add hydrogens
					        CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(molecule.getBuilder());
								
					        for (IAtom atom : molecule.atoms()) {
					          IAtomType type = matcher.findMatchingAtomType(molecule, atom);
					          AtomTypeManipulator.configure(atom, type);
					        }
					        CDKHydrogenAdder hAdder = CDKHydrogenAdder.getInstance(molecule.getBuilder());
					        hAdder.addImplicitHydrogens(molecule);
					        AtomContainerManipulator.convertImplicitToExplicitHydrogens(molecule);
				        }
				        //there is a bug in cdk?? error happens when there is a S or Ti in the molecule
				        catch(IllegalArgumentException e)
			            {
			            	completeLog += "Error: " + candidates.get(c) + " Message: " + e.getMessage();
			            	//skip it
			            	continue;
			            }
				        
				        Fragmenter fragmenter = new Fragmenter((Vector<Peak>)peakList.clone(), mzabs, mzppm, mode, breakAromaticRings, experimentalRedundancyCheck, true, false);
				        long start = System.currentTimeMillis();
				        List<IAtomContainer> l = null;
				        try
				        {
				        	l = fragmenter.generateFragmentsInMemory(molecule, true, treeDepth);
				        }
				        catch(OutOfMemoryError e)
				        {
				        	System.out.println("OUT OF MEMORY ERROR! " + candidates.get(c));
				        	completeLog += "Error: " + candidates.get(c) + " Message: " + e.getMessage();
				        	continue;
				        }
				        long time = System.currentTimeMillis() - start;
				        System.out.println("Benötigte Zeit: " + time + " Got " + l.size() + " fragments");
//				        System.out.println("Needed " + fragmenter.getNround() + " calls to generateFragments()");
				        
				        for (int i = 0; i < l.size(); i++) 
				        {		
							
					        try {
					           //write fragments to disk
					           new File(folder + file + "/" + candidates.get(c)).mkdir();
					           FileWriter w = new FileWriter(new File(folder + file + "/" + candidates.get(c) + "/frag_" + i + ".mol"));
					           MDLWriter mw = new MDLWriter(w);
					           mw.write(new Molecule(l.get(i)));
					           mw.close();
					        }catch (IOException e) {
					           System.out.println("IOException: " + e.toString());
					           completeLog += "Error: " + candidates.get(c) + " Message: " + e.getMessage();
					        } 
					        catch (Exception e) {
					           System.out.println(e.toString());
					           completeLog += "Error: " + candidates.get(c) + " Message: " + e.getMessage();
					        }
				        }
			        
				        //Draw molecule and its fragments
				        if (showDiagrams)
				        	Render.Draw(molecule,l, "Original Molecule"); 
				        
				        if(pdf)
				        {
					        //Create PDF Output
					        l.add(0,molecule);
				        	DisplayStructure ds1 = null;
				        	//create pdf subfolder
				        	new File(folder + file + "/" + candidates.get(c) + "pdf/").mkdir();
				        	ds1 = new WritePDFTable(true, 300, 300, 0.9, 2, false, false, folder + file + "/" + candidates.get(c) + "pdf/");
				        	for (int i = 0; i < l.size(); i++) {
				                //ds = new displayStructure(false, 300, 300, 0.9, false, "PDF", "/home/basti/WorkspaceJava/TandemMSLookup/fragmenter/Test");
				                assert ds1 != null;
				                ds1.drawStructure(l.get(i), i);
				    		}
					        
					        if (ds1 != null) ds1.close();
				        }
			        }
			        else
			        	System.out.println("Could not create folder...frags already computed!!!!");
			        
					//now read the saved mol files
					List<IAtomContainer> fragments = Molfile.Readfolder(folder + file + "/" + candidates.get(c));
					
					//get the original peak list again
					peakList = spectrum.getPeakList();
					//clean up peak list
					CleanUpPeakList cList = new CleanUpPeakList(peakList);
					Vector<Peak> cleanedPeakList = cList.getCleanedPeakList(spectrum.getExactMass());
					
					
					//now find corresponding fragments to the mass
					AssignFragmentPeak afp = new AssignFragmentPeak();
					afp.setHydrogenTest(hydrogenTest);
					afp.AssignFragmentPeak(fragments, cleanedPeakList, mzabs, mzppm, spectrum.getMode(), false);
					Vector<PeakMolPair> hits = afp.getHits();
					
					
					//now "real" scoring --> depends on intensities
					Scoring score = new Scoring(spectrum.getPeakList());
					double currentScore = score.computeScoring(afp.getHitsMZ());
					
					//save score in hashmap...if there are several hits with the same score --> vector of strings
					if(realScoreMap.containsKey(currentScore))
			        {
			        	Vector<String> tempList = realScoreMap.get(currentScore);
			        	tempList.add(candidates.get(c));
			        	realScoreMap.put(currentScore, tempList);
			        }
			        else
			        {
			        	Vector<String> temp = new Vector<String>();
			        	temp.add(candidates.get(c));
			        	realScoreMap.put(currentScore, temp);
			        }
					
					
					//save score in hashmap...if there are several hits with the same
					//amount of identified peaks --> ArrayList
					if(scoreMap.containsKey(hits.size()))
			        {
			        	ArrayList<String> tempList = scoreMap.get(hits.size());
			        	tempList.add(candidates.get(c));
			        	scoreMap.put(hits.size(), tempList);
			        }
			        else
			        {
			        	ArrayList<String> temp = new ArrayList<String>();
			        	temp.add(candidates.get(c));
			        	scoreMap.put(hits.size(), temp);
			        }
					
				
					//get all the identified peaks
					String peaks = "";
					for (int i = 0; i < hits.size(); i++) {
						peaks += hits.get(i).getPeak().getMass() + " ";
						listOfPeaks.add(hits.get(i).getPeak());
						if(pubChemIdentifier.equals(candidates.get(c)))
							listOfPeaksCorresponding.add(hits.get(i).getPeak());
					}
					//write things to log file
					foundPeaks += hits.size();
					allPeaks += spectrum.getPeakList().size();
					completeLog += "\nFile: " + candidates.get(c) + "\t #Peaks: " + spectrum.getPeakList().size() + "\t #Found: " + hits.size();
					completeLog += "\tPeaks: " + peaks;
					
					List<IAtomContainer> hitsListTest = new ArrayList<IAtomContainer>();
					for (int i = 0; i < hits.size(); i++) {
						List<IAtomContainer> hitsList = new ArrayList<IAtomContainer>();
						hitsList.add(AtomContainerManipulator.removeHydrogens(hits.get(i).getFragment()));
						hitsListTest.add(hits.get(i).getFragment());
						//Render.Highlight(AtomContainerManipulator.removeHydrogens(molecule), hitsList , Double.toString(hits.get(i).getPeak()));
					}
					if (showDiagrams)
						Render.Draw(molecule, hitsListTest , "Fragmente von: " + candidates.get(c));
				}
				catch(CDKException e)
				{
					System.out.println("CDK error!" + e.getMessage());
					completeLog += "CDK Error! " + e.getMessage() + "File: " + candidates.get(c);
				}
				catch(FileNotFoundException e)
				{
					System.out.println("File not found" + e.getMessage());
					completeLog += "File not found error! "+ e.getMessage() + "File: " + candidates.get(c);
				}
				catch(IOException e)
				{
					System.out.println("IO error: " + e.getMessage());
					completeLog += "IO Error! "+ e.getMessage() + "File: " + candidates.get(c);
				}
				catch(Exception e)
				{
					System.out.println("Error" + e.getMessage());
					completeLog += "Error! "+ e.getMessage() + "File: " + candidates.get(c);
				}
				catch(OutOfMemoryError e)
				{
					System.out.println("Out of memory: " + e.getMessage() + "\n" + e.getStackTrace());
					System.gc();
					completeLog += "Out of memory! "+ e.getMessage() + "File: " + candidates.get(c);
				}
			}
			
			
			
			//easy scoring
			Integer[] keylist = new Integer[scoreMap.keySet().size()];
			Object[] keys = scoreMap.keySet().toArray();
			
			for (int i = 0; i < keys.length; i++) {
				keylist[i] = Integer.parseInt(keys[i].toString());
			}
			
			Arrays.sort(keylist);
			String scoreList = "";
			int place = 0;
			for (int i = keylist.length-1; i >= 0; i--) {
				boolean check = false;
				for (int j = 0; j < scoreMap.get(keylist[i]).size(); j++) {
					scoreList += "\n" + keylist[i] + " - " + scoreMap.get(keylist[i]).get(j);
					if(pubChemIdentifier.equals(scoreMap.get(keylist[i]).get(j)))
					{
						check = true;
					}
					//worst case: count all which are better or have a equal position
					place++;
				}
				if(check)
				{
					histogram += "\n" + file + "\t" + pubChemIdentifier + "\t" + place + "\t" + exactMass;
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
			Double[] keysScore = new Double[realScoreMap.keySet().size()];
			keysScore = realScoreMap.keySet().toArray(keysScore);
			
			Arrays.sort(keysScore);
			String scoreListReal = "";
			place = 0;
			for (int i = keysScore.length-1; i >= 0; i--) {
				boolean check = false;
				for (int j = 0; j < realScoreMap.get(keysScore[i]).size(); j++) {
					scoreListReal += "\n" + keysScore[i] + " - " + realScoreMap.get(keysScore[i]).get(j);
					if(pubChemIdentifier.compareTo(realScoreMap.get(keysScore[i]).get(j)) == 0)
					{
						check = true;
					}
					//worst case: count all which are better or have a equal position
					place++;
				}
				if(check)
				{
					histogramReal += "\n" + file + "\t" + pubChemIdentifier + "\t" + place + "\t" + exactMass;
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
			completeLog += "\n*****************************************************\n\n";	
			

			//write the data for peak histogram to log file
			for (int i = 0; i < listOfPeaks.size(); i++) {
				histogramPeaksAll += listOfPeaks.get(i) + "\n";
			}	
			
			//filter the peaks which are contained in the all peaks list. (exclusive)
			for (int i = 0; i < listOfPeaksCorresponding.size(); i++) {
				for (int j = 0; j < listOfPeaks.size(); j++) {
					Double valueA = listOfPeaks.get(j).getMass();
					Double valueB = listOfPeaksCorresponding.get(i).getMass();
					if(valueA.compareTo(valueB) == 0)
					{
						listOfPeaks.remove(j);
						break;
					}
				}
			}
			
			for (int i = 0; i < listOfPeaks.size(); i++) {
				histogramPeaks += listOfPeaks.get(i) + "\n";
			}
			
			for (int i = 0; i < listOfPeaksCorresponding.size(); i++) {
				histogramPeaksReal += listOfPeaksCorresponding.get(i) + "\n";
			}
			
			
		}


}
