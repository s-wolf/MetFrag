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
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;


import org.openscience.cdk.Molecule;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.io.MDLWriter;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;

import de.ipbhalle.metfrag.fragmenter.Fragmenter;
import de.ipbhalle.metfrag.keggWebservice.KeggWebservice;
import de.ipbhalle.metfrag.massbankParser.Peak;
import de.ipbhalle.metfrag.scoring.Scoring;
import de.ipbhalle.metfrag.spectrum.AssignFragmentPeak;
import de.ipbhalle.metfrag.spectrum.CleanUpPeakList;
import de.ipbhalle.metfrag.spectrum.PeakMolPair;
import de.ipbhalle.metfrag.spectrum.WrapperSpectrum;
import de.ipbhalle.metfrag.tools.GetKEGGIdentifier;
import de.ipbhalle.metfrag.tools.MassBankData;
import de.ipbhalle.metfrag.tools.PPMTool;
import de.ipbhalle.metfrag.tools.renderer.StructureRenderer;
import de.ipbhalle.metfrag.tools.renderer.StructureRendererTable;
import de.ipbhalle.metfrag.read.Molfile;




public class HierarchicalSearch {
	
	private Vector<WrapperSpectrum> spectra;
	private String completeLog = "";
	private boolean breakAromaticRings;
	private double mzabs = 0.0;
	private double mzppm = 0.0;
	private boolean quickRedundancy = false;
	private boolean pdf = false;
	private boolean showDiagrams = false;
	private Vector<IAtomContainer> foundHits;
	private Vector<String> blackList;
	private HashMap<Integer, ArrayList<String>> scoreMap = new HashMap<Integer, ArrayList<String>>();
	private int foundPeaks = 0;
    private int allPeaks = 0;
    private String histogram = "";
    private String histogramCompare = "";
    private String histogramReal = "";
    private String histogramPeaks = "";
    private String histogramPeaksReal = "";
    private String histogramPeaksAll = "";
    private long sumTime = 0;
    private String keggPath = "";
    private boolean hydrogenTest = false;
	
	/**
	 * Instantiates a new hierarchical search. Frag Search starts from the lowest collision energy
	 * to the highest collision energy one after one.
	 * 
	 * @param folder the folder
	 * @param file the file
	 */
	public HierarchicalSearch(String folder, String file, double mzabs, double mzppm, boolean breakAromaticRings, boolean sumFormulaRedundancy, boolean showDiagrams, boolean pdf, int treeDepth, String keggPath, boolean hydrogenTest)
	{
		MassBankData mbd = new MassBankData(folder, file);
		//get the spectra sorted by their collision energy
		this.spectra = mbd.getSpectra();
		this.mzabs = mzabs;
		this.mzppm = mzppm;
		this.keggPath = keggPath;
		this.breakAromaticRings = breakAromaticRings;
		this.quickRedundancy = sumFormulaRedundancy;
		this.showDiagrams = showDiagrams;
		this.pdf = pdf;
		this.foundHits = new Vector<IAtomContainer>();
		BlackList bl = new BlackList(sumFormulaRedundancy);
		this.blackList = bl.getBlackList();
		this.hydrogenTest = hydrogenTest;
		processSpectra(folder, file, treeDepth);
	}
	
	
	public String getCompleteLog()
	{
		return completeLog;
	}
	
	public int getFoundPeaks()
	{
		return foundPeaks;
	}
	
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
    
    public String getHistogramPeaksReal()
    {
    	return histogramPeaksReal;
    }
    
    public String getHistogramPeaksAll()
    {
    	return this.histogramPeaksAll;
    }
	
	
	/**
	 * Process spectra in the following way:
	 * Fragment each hit in KEGG starting with the lowest collision energy
	 * If hits were found they are used for the next measurement as input and the original
	 * molecule is not used. 
	 * 
	 * @param folder the folder
	 * @param file the file
	 */
	private void processSpectra(String folder, String file, int treeDepth)
	{
		
		this.scoreMap = new HashMap<Integer, ArrayList<String>>();
		
		if(blackList.contains(file))
		{
			completeLog += "Blacklisted Molecule: " + file; 
			histogramReal += "\n" + file + "\tBLACKLIST\t";
			histogram += "\n" + file + "\tBLACKLIST\t";
			histogramCompare += "\n" + file + "\tBLACKLIST\t";
			return;
		}
		
		double exactMass = spectra.get(0).getExactMass();
		
		//timing
		long timeStart = System.currentTimeMillis(); 

		HashMap<Double, Vector<String>> realScoreMap = new HashMap<Double, Vector<String>>();
		int mode = spectra.get(0).getMode();
		
		//instantiate and read in CID-KEGG.txt
		String keggIdentifier = spectra.get(0).getKEGG();
		
		
		completeLog += "\n\n============================================================================";
		completeLog += "\nFile: " + spectra.get(0).getTrivialName() + " (KEGG Entry: " + keggIdentifier + ")";
		
		//get candidates from kegg webservice...with with a given mzppm and mzabs
		Vector<String> candidates = KeggWebservice.KEGGbyMass(exactMass, (mzabs+PPMTool.getPPMDeviation(exactMass, mzppm)));
		
		try
		{
			GetKEGGIdentifier keggID = new GetKEGGIdentifier(folder + "CID-KEGG/CID-KEGG.txt");
			//now find the corresponding KEGG entry
			if(keggID.existInKEGG(spectra.get(0).getCID()))
				keggIdentifier = keggID.getKEGGID(spectra.get(0).getCID());
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
			completeLog += "Error! Message: " + e.getMessage();
		}
		
		//comparison histogram
		if(keggIdentifier.equals("none"))
			histogramCompare += "\n" + file + "\t" + keggIdentifier + "\t\t" + exactMass;
		else
			histogramCompare += "\n" + file + "\t" + keggIdentifier + "\t" + candidates.size() + "\t" + exactMass;

		
		//list of peaks which are contained in the real molecule
		Vector<Peak> listOfPeaksCorresponding = new Vector<Peak>();
		//list of peaks which are not contained in the real molecule
		Vector<Peak> listOfPeaks = new Vector<Peak>();
		
		
		//loop over all hits
		for (int c = 0; c < candidates.size(); c++) {				
			
			this.foundHits = new Vector<IAtomContainer>();
			
			//get mol file from kegg....remove "cpd:"
			String candidate = KeggWebservice.KEGGgetMol(candidates.get(c).substring(4), this.keggPath);
			IAtomContainer molecule = null;
			try
			{
				//write string to disk
				new File(folder + file + "_Mol").mkdir();
				File outFile = new File(folder + file + "_Mol/" + candidates.get(c).substring(4) + ".mol");
	            FileWriter out = new FileWriter(outFile);
	            out.write(candidate);
	            out.close();
	            //now fragment the retrieved molecule
		        molecule = Molfile.Read(folder + file + "_Mol/" + candidates.get(c).substring(4) + ".mol");
		        //now create a new folder to write the .mol files into
		        new File(folder + file).mkdir();
		        boolean status = new File(folder + file + "/" + candidates.get(c).substring(4)).mkdir();
			}
			catch(IOException e)
			{
				completeLog += "Error: " + candidates.get(c).substring(4) + " Message: " + e.getMessage();
			}
			catch(CDKException e)
			{
				completeLog += "Error: " + candidates.get(c).substring(4) + " Message: " + e.getMessage();
			}
	        
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
            	completeLog += "Error: " + candidates.get(c).substring(4) + " Message: " + e.getMessage();
            	//skip it
            	continue;
            }
	        catch(CDKException e)
            {
            	completeLog += "Error: " + candidates.get(c).substring(4) + " Message: " + e.getMessage();
            	//skip it
            	continue;
            }
	        
	        
	        //get peak list....it is reset if this is not the first run
			Vector<Peak> peakList = spectra.get(0).getPeakList();
			//create a new instance
			Fragmenter fragmenter = new Fragmenter((Vector<Peak>)peakList.clone(), mzabs, mzppm, mode, breakAromaticRings, quickRedundancy, false, false);

	        
	        double combinedScore = 0;
	        int combinedHits = 0;
	        int combinedPeakCount= 0;
	        String peaks = "";
	        combinedPeakCount = peakList.size();
	        int count = 0;
	        
			boolean first = true;
			//loop over the different collision energies
			for (WrapperSpectrum spectrum : spectra) {
				
				List<IAtomContainer> l = null;
				long start = System.currentTimeMillis();
				
				try
				{					
					if(first)
					{
				        try
				        {
				        	l = fragmenter.generateFragmentsInMemory(molecule, true, treeDepth);
				        	count++;
				        }
				        catch(OutOfMemoryError e)
				        {
				        	System.out.println("OUT OF MEMORY ERROR! " + candidates.get(c).substring(4));
				        	completeLog += "Error: " + candidates.get(c).substring(4) + " Message: " + e.getMessage();
				        	continue;
				        }
				        first = false;
					}
					else
					{
						peakList = spectrum.getPeakList();
						combinedPeakCount += peakList.size();
						//set the current peak list...
				        fragmenter.setPeakList((Vector<Peak>)peakList.clone());
				        try
				        {
//				        	l = fragmenter.generateFragmentsHierarchical(foundHits, true);
				        	System.err.println("REMOVED....no improvement!");
				        	System.exit(1);
				        	count++;
				        }
				        catch(OutOfMemoryError e)
				        {
				        	System.out.println("OUT OF MEMORY ERROR! " + candidates.get(c).substring(4));
				        	completeLog += "Error: " + candidates.get(c).substring(4) + " Message: " + e.getMessage();
				        	continue;
				        }
					}
					
					
			        
			        long time = System.currentTimeMillis() - start;
			        System.out.println("Benötigte Zeit: " + time);
			        System.out.println("Got " + l.size() + " fragments");
			        System.out.println("Needed " + fragmenter.getNround() + " calls to generateFragments()");
			        
			        new File(folder + file + "/" + candidates.get(c).substring(4) + "/" + spectrum.getCollisionEnergy()).mkdir();
			        
			        for (int i = 0; i < l.size(); i++) 
			        {		
						
				        try {
				           //write fragments to disk
				           FileWriter w = new FileWriter(new File(folder + file + "/" + candidates.get(c).substring(4) + "/" + spectrum.getCollisionEnergy() + "/frag_" + i + ".mol"));
				           MDLWriter mw = new MDLWriter(w);
				           mw.write(new Molecule(l.get(i)));
				           mw.close();
				        }catch (IOException e) {
				           System.out.println("IOException: " + e.toString());
				           completeLog += "Error: " + candidates.get(c).substring(4) + " Message: " + e.getMessage();
				        } 
				        catch (Exception e) {
				           System.out.println(e.toString());
				           completeLog += "Error: " + candidates.get(c).substring(4) + " Message: " + e.getMessage();
				        }
			        }
		        
			        //Draw molecule and its fragments
			        if (showDiagrams)
			        	StructureRendererTable.Draw(molecule,l, "Original Molecule");
			        
//			        if(pdf)
//			        {
//				        //Create PDF Output
//				        l.add(0,molecule);
//			        	DisplayStructure ds1 = null;
//			        	//create pdf subfolder
//			        	new File(folder + file + "/" + candidates.get(c) + "pdf/").mkdir();
//			        	ds1 = new WritePDFTable(true, 300, 300, 0.9, 2, false, false, folder + file + "/" + candidates.get(c) + "pdf/");
//			        	for (int i = 0; i < l.size(); i++) {
//			                //ds = new displayStructure(false, 300, 300, 0.9, false, "PDF", "/home/basti/WorkspaceJava/TandemMSLookup/fragmenter/Test");
//			                assert ds1 != null;
//			                ds1.drawStructure(l.get(i), i);
//			    		}
//				        
//				        if (ds1 != null) ds1.close();
//			        }
			     
			
			        
					//now read the saved mol files
					List<IAtomContainer> fragments = Molfile.Readfolder(folder + file + "/" + candidates.get(c).substring(4) + "/" + spectrum.getCollisionEnergy());
					
					List<IAtomContainer> fragmentsList = new ArrayList<IAtomContainer>();
					for (int i = 0; i < fragments.size(); i++) {
						fragmentsList.add(fragments.get(i));
					}
					
					//get the original peak list again 
					//spectrum = new SpectrumWrapper(folder + file + ".txt");
					peakList = (Vector<Peak>)spectrum.getPeakList().clone();
					//clean up peak list
					CleanUpPeakList cList = new CleanUpPeakList(peakList);
					Vector<Peak> cleanedPeakList = cList.getCleanedPeakList(spectrum.getExactMass());
					
					
					//now find corresponding fragments to the mass
					AssignFragmentPeak afp = new AssignFragmentPeak();
					afp.setHydrogenTest(hydrogenTest);
					afp.assignFragmentPeak(fragments, cleanedPeakList, mzabs, mzppm, spectrum.getMode(), false);					Vector<PeakMolPair> hits = afp.getHits();
					Vector<PeakMolPair> hitsAll = afp.getAllHits();
					
					//add them to the list of already found fragments....they were found in the previous run 
					//with a smaller collission energy
					for (PeakMolPair peakMolPair : hitsAll) {
						foundHits.add(peakMolPair.getFragment());
					}
					
					combinedHits += ((combinedHits + hits.size()) - combinedHits);
			        //Render.Draw(molecule, foundHits, "Found Hits"); 
					
					//now "real" scoring
					Scoring score = new Scoring(cleanedPeakList, candidate);
					double currentScore = score.computeScoringPeakMolPair(afp.getHits());
					combinedScore += currentScore; 
					
					//check for last run and create the data for the log file
					if(count == spectra.size())
					{
						//save score in hashmap...if there are several hits with the same score --> vector of strings
						if(realScoreMap.containsKey(combinedScore))
				        {
				        	Vector<String> tempList = realScoreMap.get(combinedScore);
				        	tempList.add(candidates.get(c).substring(4));
				        	realScoreMap.put(combinedScore, tempList);
				        }
				        else
				        {
				        	Vector<String> temp = new Vector<String>();
				        	temp.add(candidates.get(c).substring(4));
				        	realScoreMap.put(combinedScore, temp);
				        }
						
						
						//save score in hashmap...if there are several hits with the same
						//amount of identified peaks --> ArrayList
						if(scoreMap.containsKey(combinedHits))
				        {
				        	ArrayList<String> tempList = scoreMap.get(combinedHits);
				        	tempList.add(candidates.get(c).substring(4));
				        	scoreMap.put(combinedHits, tempList);
				        }
				        else
				        {
				        	ArrayList<String> temp = new ArrayList<String>();
				        	temp.add(candidates.get(c).substring(4));
				        	scoreMap.put(combinedHits, temp);
				        }
					}
				
					//get all the identified peaks
					for (int i = 0; i < hits.size(); i++) {
						peaks += hits.get(i).getPeak().getMass() + " ";
						listOfPeaks.add(hits.get(i).getPeak());
						if(keggIdentifier.equals(candidates.get(c).substring(4)))
							listOfPeaksCorresponding.add(hits.get(i).getPeak());
					}
					
					List<IAtomContainer> hitsListTest = new ArrayList<IAtomContainer>();
					for (int i = 0; i < hits.size(); i++) {
						List<IAtomContainer> hitsList = new ArrayList<IAtomContainer>();
						hitsList.add(AtomContainerManipulator.removeHydrogens(hits.get(i).getFragment()));
						hitsListTest.add(hits.get(i).getFragment());
						//Render.Highlight(AtomContainerManipulator.removeHydrogens(molecule), hitsList , Double.toString(hits.get(i).getPeak()));
					}
					if (showDiagrams)
						StructureRendererTable.Draw(molecule, hitsListTest , "Fragmente von: " + candidates.get(c));
				}
				catch(CDKException e)
				{
					System.out.println("CDK error!" + e.getMessage());
					completeLog += "CDK Error! " + e.getMessage() + "File: " + candidates.get(c).substring(4);
				}
				catch(FileNotFoundException e)
				{
					System.out.println("File not found" + e.getMessage());
					completeLog += "File not found error! "+ e.getMessage() + "File: " + candidates.get(c).substring(4);
				}
				catch(IOException e)
				{
					System.out.println("IO error: " + e.getMessage());
					completeLog += "IO Error! "+ e.getMessage() + "File: " + candidates.get(c).substring(4);
				}
				catch(Exception e)
				{
					System.out.println("Error" + e.getMessage());
					completeLog += "Error! "+ e.getMessage() + "File: " + candidates.get(c).substring(4);
				}
				catch(OutOfMemoryError e)
				{
					System.out.println("Out of memory: " + e.getMessage() + "\n" + e.getStackTrace());
					System.gc();
					completeLog += "Out of memory! "+ e.getMessage() + "File: " + candidates.get(c).substring(4);
				}
			}
			
			//write things to log file
			foundPeaks += combinedHits;
			allPeaks += combinedPeakCount;
			completeLog += "\nFile: " + candidates.get(c).substring(4) + "\t #Peaks: " + combinedPeakCount + "\t #Found: " + combinedHits;
			completeLog += "\tPeaks: " + peaks;
			
		}
		
		
		//easy scoring
		Integer[] keylist = new Integer[scoreMap.keySet().size()];
		Object[] keys = scoreMap.keySet().toArray();
		
		for (int i = 0; i < keys.length; i++) {
			keylist[i] = Integer.parseInt(keys[i].toString());
		}
		
		Arrays.sort(keylist);
		String scoreList = "";
		int rank = 0;
		for (int i = keylist.length-1; i >= 0; i--) {
			boolean check = false;
			for (int j = 0; j < scoreMap.get(keylist[i]).size(); j++) {
				scoreList += "\n" + keylist[i] + " - " + scoreMap.get(keylist[i]).get(j);
				if(keggIdentifier.equals(scoreMap.get(keylist[i]).get(j)))
				{
					check = true;
				}
				//worst case: count all which are better or have a equal position
				rank++;
			}
			if(check)
			{
				histogram += "\n" + file + "\t" + keggIdentifier + "\t" + rank + "\t" + exactMass;
			}
		}
		
		if(keggIdentifier.equals("none"))
		{
			histogram += "\n" + file + "\t" + keggIdentifier + "\t\t" + exactMass;
		}
		
		completeLog += "\n\n*****************Scoring*****************************";
		completeLog += "Supposed to be: " + keggIdentifier;
		completeLog += scoreList;
		completeLog += "\n*****************************************************\n\n";
		//easy scoring end
		
		
		
		//real scoring
		Double[] keysScore = new Double[realScoreMap.keySet().size()];
		keysScore = realScoreMap.keySet().toArray(keysScore);
		
		Arrays.sort(keysScore);
		String scoreListReal = "";
		rank = 0;
		for (int i = keysScore.length-1; i >= 0; i--) {
			boolean check = false;
			for (int j = 0; j < realScoreMap.get(keysScore[i]).size(); j++) {
				scoreListReal += "\n" + keysScore[i] + " - " + realScoreMap.get(keysScore[i]).get(j);
				if(keggIdentifier.compareTo(realScoreMap.get(keysScore[i]).get(j)) == 0)
				{
					check = true;
				}
				//worst case: count all which are better or have a equal position
				rank++;
			}
			if(check)
			{
				histogramReal += "\n" + file + "\t" + keggIdentifier + "\t" + rank + "\t" + exactMass;
			}
		}
		
		if(keggIdentifier.equals("none"))
		{
			histogramReal += "\n" + file + "\t" + keggIdentifier + "\t\t" + exactMass;
		}
		
		//timing
		long timeEnd = System.currentTimeMillis() - timeStart;
        sumTime += timeEnd;
		
		completeLog += "\n\n*****************Scoring(Real)*****************************";
		completeLog += "Supposed to be: " + keggIdentifier;
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
				}
			}
		}
		
		for (int i = 0; i < listOfPeaks.size(); i++) {
			histogramPeaks += listOfPeaks.get(i) + " ";
		}
		
		for (int i = 0; i < listOfPeaksCorresponding.size(); i++) {
			histogramPeaksReal += listOfPeaksCorresponding.get(i) + " ";
		}
		
		
	}

}
