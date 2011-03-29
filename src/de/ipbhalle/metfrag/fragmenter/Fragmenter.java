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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Vector;


import org.openscience.cdk.Atom;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.SingleElectron;
import org.openscience.cdk.aromaticity.AromaticityCalculator;
import org.openscience.cdk.aromaticity.CDKHueckelAromaticityDetector;
import org.openscience.cdk.config.IsotopeFactory;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.NoSuchAtomException;
import org.openscience.cdk.formula.MolecularFormula;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IElement;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.interfaces.IMolecularFormulaSet;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IRing;
import org.openscience.cdk.interfaces.IRingSet;
import org.openscience.cdk.interfaces.ISingleElectron;
import org.openscience.cdk.interfaces.IBond.Stereo;
import org.openscience.cdk.io.CMLWriter;
import org.openscience.cdk.io.SDFWriter;
import org.openscience.cdk.isomorphism.IsomorphismTester;
import org.openscience.cdk.isomorphism.UniversalIsomorphismTester;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;
import org.openscience.cdk.qsar.DescriptorValue;
import org.openscience.cdk.qsar.descriptors.molecular.ALOGPDescriptor;
import org.openscience.cdk.qsar.descriptors.molecular.MannholdLogPDescriptor;
import org.openscience.cdk.qsar.descriptors.molecular.XLogPDescriptor;
import org.openscience.cdk.ringsearch.AllRingsFinder;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

import de.ipbhalle.metfrag.bondPrediction.BondPrediction;
import de.ipbhalle.metfrag.bondPrediction.Charges;
import de.ipbhalle.metfrag.graphviz.GraphViz;
import de.ipbhalle.metfrag.massbankParser.Peak;
import de.ipbhalle.metfrag.spectrum.AssignFragmentPeak;
import de.ipbhalle.metfrag.tools.Constants;
import de.ipbhalle.metfrag.tools.MolecularFormulaTools;
import de.ipbhalle.metfrag.tools.MoleculeTools;
import de.ipbhalle.metfrag.tools.PPMTool;


public class Fragmenter {
	
	private int nround = 0;
	private List<BondPair> knownBonds = null;
	private IRingSet allRings;
	private List<IBond> aromaticBonds;
	private List<BondInRing> bondInRing = null;
    private double minWeight = Double.MAX_VALUE;
    private double mzabs;
    private double mzppm;
    private Vector<Peak> peakList;
    private IMolecularFormulaSet molecularFormulas;
    private Vector<HashMap<String, Integer>> peakSumFormulaTable;
    //store the sum formula with its atom container properties
    private HashMap<String, List<IAtomContainer>> molformulaToFragMap;
    //private String currentSumFormula;
    private int countIsomorph = 0;
    private IAtomContainer originalMolecule;
    private boolean breakAromaticRings = false;
    private boolean givenPeaks = true;
    private int mode = 1;
    private static int bondNumber = 0;
    private HashMap<String, Double> atomMasses = new HashMap<String, Double>();
    private Double currentFragWeight = 0.0;
    private List<IAtom> atomList = new ArrayList<IAtom>();
    //private boolean checked = false; //only 1 iteration for molecules that only consist of rings (if false)...probably WRONG
    private GraphViz gv;
    private boolean removePeak = false;
    private boolean molecularFormulaRedundancyCheck = false;
    private boolean smilesRedundancyCheck = false;
    private boolean realIsomorphism = false;
    private boolean radicalGeneration = true;

    private int atomsContained;
    private Map<String, Double> bondEnergies;
    private int treeDepth = 0;
    private PostProcess pp = null;
    private List<String> bondsToBreak = null;
    private boolean isOnlyBreakSelectedBonds = false;
    private BondPrediction bondPrediction = null;
    private boolean partialChargesPreferred = true;
    
    private Map<String, Object> moleculeDescriptors;
      
    
    /**
     * Instantiates a new fragmenter (heuristic) with no peaks. Test the fragmenter to split up the molecule.
     * This method is something like Brute Force with a minimum peak set
     * 
     * @param breakAromaticRings break aromatic rings?
     * @param peakList the peak list
     * @param minWeight the min weight
     * @param molecularFormulaRedundancyCheck
     * @param isOnlyBreakSelectedBonds the is only break selected bonds
     */
    public Fragmenter(Vector<Peak> peakList, Double minWeight, boolean breakAromaticRings, boolean molecularFormulaRedundancyCheck, boolean isOnlyBreakSelectedBonds)
    {
    	this.peakList = peakList;
    	this.mzabs = 0;
    	this.mzppm = 0;
    	this.breakAromaticRings = breakAromaticRings;
    	this.molecularFormulas = null;
    	//HashMap which stores the sum formulas corresponding to the Atomcontainer (fragment)
    	this.molformulaToFragMap = new HashMap<String, List<IAtomContainer>>();
    	this.minWeight = minWeight;
    	this.nround = 0;
    	//graphviz output
    	gv = new GraphViz();
        gv.addln(gv.start_graph());
        this.molecularFormulaRedundancyCheck = molecularFormulaRedundancyCheck;
    }
    
    
    /**
     * Instantiates a new fragmenter. Given the peak list it gets the smallest mass.
     * The fragmentation is only done if the resulting fragment is heavy enough and if there are enough elements if the
     * sum formula of the fragment.
     * 
     * @param molecularFormulas the sum formulas
     * @param mzabs the mzabs
     * @param mzppm the mzppm
     * @param positiveMode the positive mode
     * @param breakAromaticRings the break aromatic rings
     * @param peakList the peak list
     * @param molecularFormulaRedundancyCheck the molecular formula redundancy check
     * @param neutralLossCheck the neutral loss check
     * @param isOnlyBreakSelectedBonds the is only break selected bonds
     */
    public Fragmenter(Vector<Peak> peakList, IMolecularFormulaSet molecularFormulas, double mzabs, double mzppm, int positiveMode, boolean breakAromaticRings, boolean molecularFormulaRedundancyCheck, boolean isOnlyBreakSelectedBonds)
    {
    	this.peakList = peakList;
    	this.mzabs = mzabs;
    	this.mzppm = mzppm;
    	this.breakAromaticRings = breakAromaticRings;
    	this.mode = positiveMode;
    	this.molecularFormulas = molecularFormulas;
    	this.molformulaToFragMap = new HashMap<String, List<IAtomContainer>>();
    	this.nround = 0;
    	this.molecularFormulaRedundancyCheck = molecularFormulaRedundancyCheck;
    	
    	//process the peaks sum formulas
    	parseFormula();
    	//set the minimum weight...the "leightest" peak
    	setMinWeight();
    	//graphviz output
    	gv = new GraphViz();
        gv.addln(gv.start_graph());
    }
    
    
    /**
     * Instantiates a new fragmenter (heuristic). There are most likely no sum formulas from the peak given.
     * The smallest mass is the lower bound for breaking up bonds.
     * 
     * @param mzabs the mzabs
     * @param mzppm the mzppm
     * @param peakList the peak list
     * @param positiveMode the positive mode
     * @param breakAromaticRings the break aromatic rings
     * @param molecularFormulaRedundancyCheck the experimental isomorphism check
     */
    public Fragmenter(Vector<Peak> peakList, double mzabs, double mzppm, int positiveMode, boolean breakAromaticRings, boolean molecularFormulaRedundancyCheck, boolean isOnlyBreakSelectedBonds)
    {
    	this.peakList = peakList;
    	this.mzabs = mzabs;
    	this.mzppm = mzppm;
    	this.breakAromaticRings = breakAromaticRings;
    	this.mode = positiveMode;
    	this.molformulaToFragMap = new HashMap<String, List<IAtomContainer>>();
    	this.nround = 0;
    	this.molecularFormulaRedundancyCheck = molecularFormulaRedundancyCheck;
    	
    	//set the minimum weight...the "leightest" peak
    	setMinWeight();
    	//graphviz output
    	gv = new GraphViz();
        gv.addln(gv.start_graph());
    }
    
    
    /**
     * Instantiates a new fragmenter. This is only to be used with the Performance test. No smallest Peak gets deleted!
     * 
     * @param peakList the peak list
     * @param mzabs the mzabs
     * @param mzppm the mzppm
     * @param positiveMode the positive mode
     * @param breakAromaticRings the break aromatic rings
     * @param removePeaks the remove peaks
     * @param molecularFormulaRedundancyCheck
     */
    public Fragmenter(Vector<Peak> peakList, double mzabs, double mzppm, int positiveMode, boolean breakAromaticRings, boolean removePeaks, boolean molecularFormulaRedundancyCheck, boolean isOnlyBreakSelectedBonds)
    {
    	this.peakList = peakList;
    	this.mzabs = mzabs;
    	this.mzppm = mzppm;
    	this.breakAromaticRings = breakAromaticRings;
    	this.mode = positiveMode;
    	this.molformulaToFragMap = new HashMap<String, List<IAtomContainer>>();
    	this.nround = 0;
    	this.molecularFormulaRedundancyCheck = molecularFormulaRedundancyCheck;
    	
    	//set the minimum weight...the "leightest" peak
    	setMinWeight();
    	this.removePeak = removePeaks;
    	//graphviz output
    	gv = new GraphViz();
        gv.addln(gv.start_graph());
    }
    
    /**
     * Sets the peak list.
     * 
     * @param peakList the new peak list
     */
    public void setPeakList(Vector<Peak> peakList)
    {
    	this.peakList = peakList;
    }
    
        
    /**
     * Set the current bond number.
     * 
     * @return the current bond number
     */
    public static void setBondNumber(int num)
    {
    	bondNumber = num;
    }
    
    /**
     * Get the current bond number.
     * 
     * @return the current bond number
     */
    public static int getBondNumber()
    {
    	return bondNumber;
    }
    
    
    /**
     * Mark all bonds.
     * 
     * @param original the original molecule
     * 
     * @return modfied atom container
     */
    public IAtomContainer markAllBonds(IAtomContainer original)
    {
    	MoleculeTools.moleculeNumbering(original);
    	bondNumber = original.getBondCount();
    	atomsContained = original.getAtomCount();
    	
    	return original;
    }
    
    private void preprocessMolecule(IAtomContainer original, boolean isPreCalculated) throws Exception 
    {    	
    	//read in bondenergies.txt
    	bondEnergies = new HashMap<String, Double>();
        try 
        {
        	String file = "";
        	if(System.getProperty("property.file.path") != null)
        	{
        		file = System.getProperty("property.file.path");
        		file += "bondenergies.txt";
        	}
        	else
        	{
        		URL url = Fragmenter.class.getClassLoader().getResource("bondenergies.txt");
    			file = url.getFile();
//        		System.out.println("Pfad zu bondenergies.txt: " + url.getFile());
        	}
        	
        	FileInputStream fstream = new FileInputStream(new File(file));
    	    // Get the object of DataInputStream
    	    DataInputStream in = new DataInputStream(fstream);
    	    BufferedReader br = new BufferedReader(new InputStreamReader(in));
    	    String strLine;
    	    //Read File Line By Line
    	    while ((strLine = br.readLine()) != null)   {
    	      // Print the content on the console
    	      //System.out.println (strLine);
    	      if(strLine.equals("//"))
    	    	  break;
    	      String bond = strLine.substring(0,5).trim();
    	      String energy = strLine.substring(6).trim();
    	      bondEnergies.put(bond, Double.parseDouble(energy));
    	      //System.out.println("Bond: '" + bond + "' Energy: '" + energy + "'");
    	    }
    	    //Close the input stream
    	    in.close();
        } 
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    	
    	//prepare atom weights
        prepareAtomWeights(original);
        
        //mark all the bonds and atoms with numbers --> identify them later on        
        this.originalMolecule = markAllBonds(original);
        
        
        //do ring detection with the original molecule
        AllRingsFinder allRingsFinder = new AllRingsFinder();
        allRingsFinder.setTimeout(100000);
        allRings = allRingsFinder.findAllRings(this.originalMolecule);
        this.aromaticBonds = new ArrayList<IBond>();
        
        CDKHueckelAromaticityDetector.detectAromaticity(this.originalMolecule);
        
    	for (IBond bond : this.originalMolecule.bonds()) {
            boolean aromatic = false;
            //lets see if it is a ring and aromatic
            IRingSet rings = allRings.getRings(bond);
            //don't split up aromatic rings...see constructor for option
        	for (int i = 0; i < rings.getAtomContainerCount(); i++) {
        		aromatic =  AromaticityCalculator.isAromatic((IRing)rings.getAtomContainer(i), this.originalMolecule);
            	if(aromatic)
            	{
            		aromatic = true;
            		this.aromaticBonds.add(bond);
            		break;
            	}
			}
        }
    	
    	//calculate molecules descriptors: XlogP, AlogP and MannholdLogP
        this.moleculeDescriptors = new HashMap<String, Object>();
        XLogPDescriptor logP = new XLogPDescriptor();
        DescriptorValue logPValue = logP.calculate(this.originalMolecule);
		this.moleculeDescriptors.put("XLogP", logPValue);
		MannholdLogPDescriptor mLogP = new MannholdLogPDescriptor();
		DescriptorValue mlogPValue = mLogP.calculate(this.originalMolecule);
		this.moleculeDescriptors.put("MannholdLogP", mlogPValue);
		ALOGPDescriptor aLogP = new ALOGPDescriptor();
		DescriptorValue alogPValue = aLogP.calculate(this.originalMolecule);
		this.moleculeDescriptors.put("ALogP", alogPValue);


    	

    	if(isPreCalculated)
    	{
    		//now find all bonds which are worth splitting
    		bondPrediction = new BondPrediction(this.aromaticBonds);
    		Map<String, Double> bondToBondLength = new HashMap<String, Double>();
    		for(IBond bond : this.originalMolecule.bonds())
    		{
    			bondToBondLength.put(bond.getID(), Double.parseDouble((String)bond.getProperty("bondLengthChange")));
    		}
    		bondPrediction.setBondLength(bondToBondLength);
    	}
    	else
    	{
    		//now find all bonds which are worth splitting
    		bondPrediction = new BondPrediction(this.aromaticBonds);
    		this.bondsToBreak = bondPrediction.calculateBondsToBreak("/vol/local/bin/", this.originalMolecule, 600, "AM1", 600, true);
    	}
    } 
    
    
    /**
     * Split a molecule into fragments.
     * <p/>
     * The method considers bonds as splitable if they are
     * <ul>
     * <li><strike>non-terminal</strike>
     * <li><strike>Not in a ring</strike>
     * <li>Heavier than the given smallest weight
     * </ul>
     * <p/>
     * Hydrogens are added in this method to the original molecule! This method uses a queue (BFS).
     * 
     * Fragments are stored in the temporary folder. More memory efficient especially when using pubchem or chemspider
     * as compound database.
     *
     * @param atomContainer The molecule to split
     * @param verbose the verbose
     * @param treeDepthMax the tree depth max
     * @param identifier the identifier
     * @param isPrecalculated if the mol contains already the bond length change
     * @return a list of fragments
     * @throws CDKException the CDK exception
     * @throws Exception the exception
     */
    public List<File> generateFragmentsEfficient(IAtomContainer atomContainer, boolean verbose, int treeDepthMax, String identifier, boolean isPrecalculated) throws CDKException, Exception 
    {
    	List<File> fragmentsReturn = new ArrayList<File>();
    	int tempLevelCount = 0;
    	
    	//now set a new min weight
    	this.minWeight = this.minWeight - (double)treeDepthMax;
    	

    	//counting vars for the tree build
    	int parent = 0;
    	int globalCount = 0; //count all frags --> labeling of the pictures
    	
    	//fragments not yet split up enough...QUEUE --> BFS
        Queue<Node> fragmentQueue = new LinkedList<Node>();
       
		globalCount++;
        
		//do preprocess: find all rings and aromatic rings...mark all bonds
		preprocessMolecule(atomContainer, isPrecalculated);

		//add the original molecule
    	this.originalMolecule.setProperty("PartialChargeDiff", "0.0");
		this.originalMolecule.setProperty("TreeDepth", "0");
		this.originalMolecule.setProperty("BondEnergy", "0");
		fragmentsReturn.add(writeMoleculeToTemp(this.originalMolecule, identifier, globalCount, "0.0", 0, "0.0")); 
    	
		//add original molecule to it
        fragmentQueue.offer(new Node(0, 0, this.originalMolecule, 0));       
//        fragmentsReturn.add(writeMoleculeToTemp(this.originalMolecule, identifier, globalCount, "0", 0));
        globalCount++;
        int treeDepth = 1;
                
        
        //get the number of preprocessed spectra
        tempLevelCount = fragmentQueue.size();
        
        while(!fragmentQueue.isEmpty())
        {
        	nround++;
        	//remember already tried combinations of ring...so there are less combinations
        	knownBonds = new ArrayList<BondPair>();
        	
        	//get a fragment from the priority queue
        	Node currentNode = fragmentQueue.poll();
        	//reduce the number of fragments in this level
        	tempLevelCount--;
        	IAtomContainer currentFragment = currentNode.getMol();
        	
        	//add to result list...don't break fragments which only have 2 bonds left
        	if (currentFragment.getBondCount() < 2)
        		continue;
        	
        	List<IBond> splitableBonds = getSplitableBonds(currentFragment);
            
            //no splitable bonds are found
            if (splitableBonds.size() == 0)
                continue;
            
            parent = currentNode.getCurrent();

            for (IBond bond : splitableBonds) {
            	
            	List<IAtomContainer> parts = splitMolecule(currentFragment, bond);
                                
                for (IAtomContainer partContainer : parts) {
                	
                	//fix for sdf reader bug
                	partContainer.removeProperty("cdk:Remark");
                	partContainer.removeProperty("cdk:Title");
                	
                	//Render.Draw(partContainer, "Round: " + this.nround);
                    fragmentQueue.offer(new Node(globalCount, parent, partContainer, treeDepth));
                    fragmentsReturn.add(writeMoleculeToTemp(partContainer, identifier, globalCount, (String)partContainer.getProperty("BondEnergy"), treeDepth, (String)partContainer.getProperty("PartialChargeDiff")));                    
                    globalCount++;
                    int parentPP = globalCount - 1;
                }
            }
        	//set the number of fragments for this level
            if(tempLevelCount <= 0)
            {
            	//count for the current level
	            tempLevelCount = fragmentQueue.size();
	            treeDepth++;
        	}
            

            
//            System.out.println("Fragmente fertig #: " + fragments.size() + " Insgesamt Todo #: " + fragmentQueue.size() + " nround: " + this.nround);
//            System.out.println("Treedepth: " + treeDepth);
//            System.out.println("Benötigte Zeit Mass: " + this.sumMass);
//            System.out.println("Benötigte Zeit Traverse: " + this.sumTraverse);
//            System.out.println("Benötigte Zeit MakeAtomContainer: " + this.sumAtom);
//            System.out.println("Benötigte Zeit Isomorph check: " + this.sumIsomorph + "Anzahl Hits: " + this.countIsomorph);
//            System.out.println("Benötigte Zeit Fingerprint: " + this.endPartition + " Insgesamt: " + this.sumPartition);
//            System.out.println("Benötigte Zeit Split Molecule: " + this.endSplitable + " Insgesamt: " + this.sumSplitableBonds);
            
          //generate only fragments until a specified depth
	      if(treeDepth >= (treeDepthMax))
	    	  break;
    
        }
	    
        //return all fragments
	    return fragmentsReturn;        
    }
    
    
    /**
     * Split a molecule into fragments.
     * <p/>
     * The method considers bonds as splitable if they are
     * <ul>
     * <li><strike>non-terminal</strike>
     * <li><strike>Not in a ring</strike>
     * <li>Heavier than the given smallest weight
     * </ul>
     * <p/>
     * Hydrogens are added in this method to the original molecule! This method uses a queue (BFS).
     * 
     * Fragments are stored in the temporary folder. More memory efficient especially when using pubchem or chemspider
     * as compound database.
     *
     * @param atomContainer The molecule to split
     * @param verbose the verbose
     * @param treeDepthMax the tree depth max
     * @param isPrecalculated if the mol contains already the bond length change
     * @return a list of fragments
     * @throws CDKException the CDK exception
     * @throws Exception the exception
     */
    public List<IAtomContainer> generateFragmentsInMemory(IAtomContainer atomContainer, boolean verbose, int treeDepthMax, boolean isPrecalculated) throws CDKException, Exception 
    {
    	List<IAtomContainer> fragmentsReturn = new ArrayList<IAtomContainer>();
    	int tempLevelCount = 0;
    	
    	//now set a new min weight
    	this.minWeight = this.minWeight - (double)treeDepthMax;

    	//counting vars for the tree build
    	int parent = 0;
    	int globalCount = 0; //count all frags --> labeling of the pictures
    	
    	//fragments not yet split up enough...QUEUE --> BFS
        Queue<Node> fragmentQueue = new LinkedList<Node>();       
		globalCount++;
        
		//do preprocess: find all rings and aromatic rings...mark all bonds
		preprocessMolecule(atomContainer, isPrecalculated);
		
		this.originalMolecule.setProperty("PartialChargeDiff", "0.0");
		this.originalMolecule.setProperty("TreeDepth", "0");
		this.originalMolecule.setProperty("BondEnergy", "0");
		fragmentsReturn.add(this.originalMolecule);
    	
		//add original molecule to it
        fragmentQueue.offer(new Node(0, 0, this.originalMolecule, 0));       
        
        globalCount++;
        Integer treeDepth = 1;
                
        
        //get the number of preprocessed spectra
        tempLevelCount = fragmentQueue.size();
        
        while(!fragmentQueue.isEmpty())
        {
        	nround++;
        	//remember already tried combinations of ring...so there are less combinations
        	knownBonds = new ArrayList<BondPair>();
        	
        	//get a fragment from the priority queue
        	Node currentNode = fragmentQueue.poll();
        	//reduce the number of fragments in this level
        	tempLevelCount--;
        	IAtomContainer currentFragment = currentNode.getMol();
        	
        	//add to result list...don't break fragments which only have 2 bonds left
        	if (currentFragment.getBondCount() < 2)
        		continue;
        	
        	List<IBond> splitableBonds = getSplitableBonds(currentFragment);
            
            //no splitable bonds are found
            if (splitableBonds.size() == 0)
                continue;
            
            parent = currentNode.getCurrent();

            for (IBond bond : splitableBonds) {

            	List<IAtomContainer> parts = splitMolecule(currentFragment, bond);
                                
                for (IAtomContainer partContainer : parts) {
                	//Render.Draw(partContainer, "Round: " + this.nround);
                	partContainer.setProperty("TreeDepth", treeDepth.toString());
                    fragmentQueue.offer(new Node(globalCount, parent, partContainer, treeDepth));
                    fragmentsReturn.add(partContainer);                    
                    globalCount++;
                    int parentPP = globalCount - 1;
                }
            }
        	//set the number of fragments for this level
            if(tempLevelCount <= 0)
            {
            	//count for the current level
	            tempLevelCount = fragmentQueue.size();
	            treeDepth++;
        	}
            

            
//            System.out.println("Fragmente fertig #: " + fragments.size() + " Insgesamt Todo #: " + fragmentQueue.size() + " nround: " + this.nround);
//            System.out.println("Treedepth: " + treeDepth);
//            System.out.println("Benötigte Zeit Mass: " + this.sumMass);
//            System.out.println("Benötigte Zeit Traverse: " + this.sumTraverse);
//            System.out.println("Benötigte Zeit MakeAtomContainer: " + this.sumAtom);
//            System.out.println("Benötigte Zeit Isomorph check: " + this.sumIsomorph + "Anzahl Hits: " + this.countIsomorph);
//            System.out.println("Benötigte Zeit Fingerprint: " + this.endPartition + " Insgesamt: " + this.sumPartition);
//            System.out.println("Benötigte Zeit Split Molecule: " + this.endSplitable + " Insgesamt: " + this.sumSplitableBonds);
            
          //generate only fragments until a specified depth
	      if(treeDepth >= (treeDepthMax))
	    	  break;
    
        }
	    
        //return all fragments
	    return fragmentsReturn;        
    }
    
    /**
     * Gets the graph.
     * 
     * @return the graph
     */
    public GraphViz getGraph()
    {
    	return gv;
    }
    
    
    /**
     * Gets the number of rounds the algorithm needed to get all the fragments.
     * 
     * @return the nround
     */
    public int getNround()
    {
    	return this.nround;
    }

    

    /**
     * Gets the splitable bonds.
     * 
     * @param atomContainer the atom container
     * 
     * @return the splitable bonds
     * 
     * @throws CDKException the CDK exception
     */
    private List<IBond> getSplitableBonds(IAtomContainer atomContainer) throws CDKException {
        
        // find the splitable bonds
        ArrayList<IBond> splitableBonds = new ArrayList<IBond>();
        
        for (IBond bond : atomContainer.bonds()) {
            boolean isTerminal = false;
            boolean aromatic = false;

            
            //lets see if it is a ring and aromatic
            //don't split up aromatic rings...see constructor for option
            if(!this.breakAromaticRings)
            {
            	if(aromaticBonds.contains(bond))
            		continue;
            }
            
            // lets see if it is a terminal bond...we dont want to split up the hydrogen
			for (IAtom atom : bond.atoms()) {
				//dont split up "terminal" H atoms
				if (atomContainer.getConnectedAtomsCount(atom) == 1 && atom.getSymbol().startsWith("H")) {
					//terminal hydrogen...ignore it
					isTerminal = true;
					break;
				}
			}

            //if (!(isInRing || isTerminal)) and bond is in the list to break
			if(isOnlyBreakSelectedBonds)
			{
				if (!(isTerminal || aromatic) && bondsToBreak.contains(bond.getID()))
	                splitableBonds.add(bond);
			}
			else
			{
				if (!(isTerminal || aromatic))
	                splitableBonds.add(bond);
			}
			
        }
        return splitableBonds;
    }
    

    /**
     * the simplest approach would be to delete the specified bond and then
     * extract the two disconnected fragments. This implies that we have to
     * create an IAtomContainer object each time this method is called. To avoid
     * this we traverse the graph rather than doing anything destructive
     * --> Create to molecules out of the bond list
     * 
     * @param atomContainer
     * @param the bond to split on
     * 
     * @return the list< i atom container>
     */
    private List<IAtomContainer> splitMolecule(IAtomContainer atomContainer, IBond bond) throws CDKException, Exception
    {
    	
    	//if this bond is in a ring we have to split another bond in this ring where at least one 
        //bond is in between. Otherwise we wont have two fragments. Else normal split.
        
        List<IAtomContainer> ret = new ArrayList<IAtomContainer>();        
        
        //get bond energy for splitting this bond
        double currentBondEnergy = getBondEnergy(bond);
        
            
        //bond is in a ring....so we have to split up another bond to break it
        IRingSet rings = allRings.getRings(bond);
        if (rings.getAtomContainerCount() != 0 )
        { 
        	//only traverse one ring!!!!
        	for (int i = 0; i < 1; i++) {
        		//get bonds that are connected to the atom....they should not be removed....disabled
        		//List<IBond> atomBondsExclude = rings.get(i).getConnectedBondsList(atom);
            	
        		
        		for (IBond bondInRing : rings.getAtomContainer(i).bonds()) {
        			//if the bonds are the same...this wont split up the ring
        			if(bondInRing == bond)
        				continue;
        			

        			//check for already tried bonds
        			BondPair check = new BondPair(bond, bondInRing);
        			if (knownBonds.contains(check))
        				continue;
        			knownBonds.add(new BondPair(bond,bondInRing));
        			     			
			
        			List<IAtomContainer> set = new ArrayList<IAtomContainer>();
        			List<List<IBond>> bondListList = new ArrayList<List<IBond>>();
                	List<Double> fragWeightList = new ArrayList<Double>();

                	for (IAtom currentAtom : bond.atoms()) {

                		//List with bonds in Ring
            			List<IBond> partRing = new ArrayList<IBond>();
            			//reset the weight because it is computed inside the traverse
            			this.currentFragWeight = 0.0;
            			//initialize new atom list
            	        atomList = new ArrayList<IAtom>();
            	        
            	        //clone current atom because a single electron is being added...homolytic cleavage
                		partRing = traverse(atomContainer, currentAtom, partRing, bond, bondInRing);
                		
                		bondListList.add(partRing);
                		fragWeightList.add(this.currentFragWeight);
                        
                        IAtomContainer temp = makeAtomContainer(currentAtom, partRing);
                        //set the properties again!
                        Map<Object, Object> properties = atomContainer.getProperties();
                        temp.setProperties(properties);
                        
                        
                        //*********************************************************
                        //BOND ENERGY CALCULATION
                        //calculate bond energy
                        double currentBondEnergyRing = getBondEnergy(bondInRing);
                        
                        //*********************************************************
                        
                        //now set property from both bonds
                        temp = setBondEnergy(temp, currentBondEnergyRing, currentBondEnergy);
                        //set the partial charge diff 
                        temp = setCharge(temp, bondPrediction.getBondLength(bondInRing.getID()), bondPrediction.getBondLength(bond.getID()));
                        
                        
                        if(radicalGeneration)
                        {
                        	ISingleElectron singleElectron = new SingleElectron(currentAtom);
                    		temp.addSingleElectron(singleElectron);
                        }
                        
                        set.add(temp);
                        
                	}
        			
                	//now maybe add the fragments to the list
                    for (int j = 0; j < set.size(); j++) 
                    {	    	            
	                    //Render.Draw(set.getAtomContainer(j), "");
	    	            if (set.get(j).getAtomCount() > 0 && set.get(j).getBondCount() > 0 && 
	    	            		set.get(j).getAtomCount() != atomContainer.getAtomCount())
	    	            { 
	                		
	    	            	//now check the current mass
	    	            	double fragMass = getFragmentMass(set.get(j), fragWeightList.get(j));
	                		//check the weight of the current fragment
	    	            	if(!isHeavyEnough(fragMass))
	                			continue;

	        	            //returns true if isomorph
	    	            	//set the current sum formula
	    	            	IMolecularFormula fragmentFormula = MolecularFormulaManipulator.getMolecularFormula(set.get(j));
	    	            	String currentSumFormula = MolecularFormulaManipulator.getString(fragmentFormula);
	    	            	if(isIdentical(set.get(j), currentSumFormula))
	        	            	continue;
	                        
	                        //add the fragment to the return list
	    	                ret.add(set.get(j));
	    	            }     
                    }
        		}         		
			}
        }
        else
        {
        	List<IAtomContainer> set = new ArrayList<IAtomContainer>();
        	List<List<IBond>> bondListList = new ArrayList<List<IBond>>();
        	List<Double> fragWeightList = new ArrayList<Double>();

        	//get the atoms from the splitting bond --> create 2 fragments
        	for (IAtom currentAtom : bond.atoms()) {
        		List<IBond> part = new ArrayList<IBond>();
        		//reset the weight because it is computed inside the traverse
    			this.currentFragWeight = 0.0;
    			//initialize new atom list
    	        atomList = new ArrayList<IAtom>();
        		part = traverse(atomContainer, currentAtom, part, bond);
        		bondListList.add(part);        		

        		IAtomContainer temp = makeAtomContainer(currentAtom, part);
        		//set the properties again!
                Map<Object, Object> properties = atomContainer.getProperties();
                temp.setProperties(properties);
                //now calculate the correct weight subtrating the possible neutral loss mass
                
                fragWeightList.add(this.currentFragWeight);
                
                
        		//now set property: BondEnergy!
                temp = setBondEnergy(temp, currentBondEnergy);
                temp = setCharge(temp, bondPrediction.getBondLength(bond.getID()));
                
                if(radicalGeneration)
                {
                	ISingleElectron singleElectron = new SingleElectron(currentAtom);
            		temp.addSingleElectron(singleElectron);
                }
                set.add(temp);
               
        	}
            
            
            
            //at most 2 new molecules
            for (int i = 0; i < set.size(); i++) 
            {
	            
	            if (set.get(i).getAtomCount() > 0 && set.get(i).getBondCount() > 0 &&
	            		set.get(i).getAtomCount() != atomContainer.getAtomCount())
	            {
	            	//now check the current mass
	            	double fragMass = getFragmentMass(set.get(i), fragWeightList.get(i));
            		//check the weight of the current fragment
	            	if(!isHeavyEnough(fragMass))
            			continue;                  

		            //set the current sum formula
	            	IMolecularFormula fragmentFormula = MolecularFormulaManipulator.getMolecularFormula(set.get(i));
	            	String currentSumFormula = MolecularFormulaManipulator.getString(fragmentFormula);
	            	//returns true if isomorph (fast isomorph check)
	            	if(isIdentical(set.get(i), currentSumFormula))
		            	continue;
	                
	                ret.add(set.get(i));
	            }
            }
            
         }

        return ret;
    }
    
    
    
    /**
     * Sets the bond energy for bonds from a ring
     * 
     * @param mol the mol
     * @param bondEnergy1 the bond energy1
     * @param bondEnergy2 the bond energy2
     * 
     * @return the i atom container
     */
    private IAtomContainer setBondEnergy(IAtomContainer mol, Double bondEnergy1, Double bondEnergy2)
    {
    	return setBondEnergy(mol, bondEnergy1 + "," + bondEnergy2);
    }
    
    /**
     * Sets the bond energy.
     * 
     * @param mol the mol
     * @param bondEnergy the bond energy
     * 
     * @return the i atom container
     */
    private IAtomContainer setBondEnergy(IAtomContainer mol, Double bondEnergy)
    {
    	return setBondEnergy(mol, bondEnergy.toString());
    }
    
    
    /**
     * Sets the bond energy.
     * 
     * @param mol the mol
     * @param bondEnergy the bond energy
     * 
     * @return the i atom container
     */
    private IAtomContainer setBondEnergy(IAtomContainer origMol, IAtomContainer mol, Double bondEnergy)
    {
    	
    	Map<Object, Object> props = mol.getProperties();
    	String bondEnergyOrig = (String)origMol.getProperty("BondEnergy");
    	if(bondEnergyOrig != null)
    	{
    		String sumEnergy = bondEnergyOrig + ";" + bondEnergy;
    		props.put("BondEnergy", sumEnergy.toString());	
    	}
    	else
    	{
    		props.put("BondEnergy", bondEnergy.toString());
    	}
    	
    	mol.setProperties(props);
    	return mol;
    }
    
    
    /**
     * Sets the bond energy.
     * 
     * @param mol the mol
     * @param bondEnergy the bond energy
     * 
     * @return the i atom container
     */
    private IAtomContainer setBondEnergy(IAtomContainer mol, String bondEnergy)
    {
    	
    	Map<Object, Object> props = mol.getProperties();
    	if(props.get("BondEnergy") != null)
    	{
    		String sumEnergy = (String)props.get("BondEnergy") + ";" + bondEnergy;
    		props.put("BondEnergy", sumEnergy);	
    	}
    	else
    	{
    		props.put("BondEnergy", bondEnergy.toString());
    	}
    	
    	mol.setProperties(props);
    	return mol;
    }
    
    
    /**
     * Sets the charge for bonds in a ring.
     * 
     * @param mol the mol
     * @param partialChargeDiff1 the partial charge diff1
     * @param partialChargeDiff2 the partial charge diff2
     * 
     * @return the i atom container
     */
    private IAtomContainer setCharge(IAtomContainer mol, Double partialChargeDiff1, Double partialChargeDiff2)
    {
    	return setCharge(mol, partialChargeDiff1.toString() + "," + partialChargeDiff2.toString());
    }
    
    /**
     * Sets the charge.
     * 
     * @param mol the mol
     * @param partialChargeDiff the partial charge diff
     * 
     * @return the i atom container
     */
    private IAtomContainer setCharge(IAtomContainer mol, Double partialChargeDiff)
    {
    	return setCharge(mol, partialChargeDiff.toString());
    }
    
    /**
     * Sets the partial charge difference previously calculated using Gasteiger-Marsili.
     * 
     * @param mol the mol
     * @param bondEnergy the bond energy
     * 
     * @return the i atom container
     */
    private IAtomContainer setCharge(IAtomContainer mol, String partialChargeDiff)
    {
    	
    	Map<Object, Object> props = mol.getProperties();
    	if(props.get("PartialChargeDiff") != null)
    	{
    		String sumPartialChargeDiff = (String)props.get("PartialChargeDiff") + ";" + partialChargeDiff;
    		props.put("PartialChargeDiff", sumPartialChargeDiff);	
    	}
    	else
    	{
    		props.put("PartialChargeDiff", partialChargeDiff.toString());
    	}
    	
    	mol.setProperties(props);
    	return mol;
    }
        
    
    /**
     * Gets the fragment mass subtracting the neutral loss from it.
     * It also sets the new FragmentWeight property
     * 
     * @param fragment the fragment
     * @param mass the mass
     * 
     * @return the fragment mass
     */
    private double getFragmentMass(IAtomContainer fragment, double mass)
    {
    	Double massFinal = mass;
    	double nlMass = 0.0;
    	if(fragment.getProperty("FragmentMass") != null && fragment.getProperty("FragmentMass") != "")
    	{
    		if(fragment.getProperty("NlMass") != null && fragment.getProperty("NlMass") != "")
    		{
    			String[] tempNLMass = fragment.getProperty("NlMass").toString().split(",");
    			for (int i = 0; i < tempNLMass.length; i++) {
					nlMass += Double.parseDouble(tempNLMass[i]);
				}
    		}
    	}
    	massFinal = massFinal -nlMass;
    	fragment.setProperty("FragmentMass", massFinal.toString());
    	return massFinal;
    }
    
    /**
     * Checks if the fragment is heavy enough.
     * 
     * @param mass the mass
     * 
     * @return true, if is heavy enough
     * 
     * @throws CDKException the CDK exception
     * @throws Exception the exception
     */
    private boolean isHeavyEnough(Double mass) throws CDKException, Exception
    {
    	boolean candidate = false;
    	
    	//only if peaks are supplied
    	if (this.givenPeaks)
    	{
    		//positive or negative mode!?
    		double protonMass = Constants.PROTON_MASS * (double)mode;
    		double min = (this.minWeight - (mzabs + PPMTool.getPPMDeviation(this.minWeight, this.mzppm)));
	    	if((mass + protonMass) > min)
	    	{
	    		//check if fragment is a possible hit
	    		if((mass + protonMass) > (this.minWeight - (mzabs + PPMTool.getPPMDeviation(this.minWeight, this.mzppm))) && (mass + protonMass) < (this.minWeight + (mzabs + PPMTool.getPPMDeviation(this.minWeight, this.mzppm))))
	    		{
	    			//remove peak from list so the fragments are not split up anymore...set new minWeight
	    			if(this.removePeak)
	    			{
	    				deletePeak(this.minWeight);
	    			}
	    		}
	    		
	    		candidate = true;
	    		
	    	}
    	}
    	else
    	{
    		//no peaks given
    		return true;
    	}
    
    	return candidate;
    	
    }
    
    
    /**
     * Quick isomorphism check:
     * ...if the fragment is isomorph to the ones already in the map
     * ...only the atoms are compared and a list of bonds is stored in a
     * 		map with sum formula to fragment
     * 
     * 
     * @param fragment the fragment to be checked
     * 
     * @return true, if successful
     * 
     * @throws CDKException the CDK exception
     */
    private boolean isIdentical(IAtomContainer fragment, String currentSumFormula) throws CDKException, Exception
    {
    	boolean isomorph = false;
    	
    	//if there is already a sum formula from the checks before or isHeavyEnough
    	//get lists of bond list  with the same sum formula
    	List<IAtomContainer> fragsToCompare = molformulaToFragMap.get(currentSumFormula);
    	
    	if(smilesRedundancyCheck)
    	{
    		//now generate the smiles for the fragment
    		SmilesGenerator sg = new SmilesGenerator();
    		IMolecule fragMol = new Molecule(fragment);
    		String smilesFrag = sg.createSMILES(fragMol);
    		Map<Object, Object> map = fragment.getProperties();
    		map.put("smiles", smilesFrag);
    	}
    	
    	//iterate over list to check for isomorphism
    	if (fragsToCompare != null)
    	{
    		//addFragmentToListMap(fragment, currentSumFormula);
	    	//isomorph = true;
    		if(realIsomorphism)
    			isomorph = isIsomorph(fragment, fragsToCompare);
    		else
    			isomorph = identicalAtoms(fragment, fragsToCompare);
    		//isomorphism quick check cdk
	    	//isomorph = quickCheck(fragment, fragsToCompare);
	    	if(isomorph)
	    	{
	    		this.countIsomorph++;
	    		//now check if this fragment energy to create the fragment is lower 
	    		if(molecularFormulaRedundancyCheck || smilesRedundancyCheck)
	    		{
	    			if(partialChargesPreferred)
	    			{
	    				//now replace fragment if its "bond energy is less"
			    		double partialChargeDiff = MoleculeTools.getCombinedEnergy((String)fragment.getProperty("PartialChargeDiff"));
			    		for (IAtomContainer atomContainer : fragsToCompare) {
							if(MoleculeTools.getCombinedEnergy((String)atomContainer.getProperty("PartialChargeDiff")) < partialChargeDiff )
							{
								addFragmentToListMapReplace(fragment, currentSumFormula);
							}
						}
	    			}
	    			else
	    			{
	    				//now replace fragment if its "bond energy is less"
			    		double bondEnergy = MoleculeTools.getCombinedEnergy((String)fragment.getProperty("BondEnergy"));
			    		for (IAtomContainer atomContainer : fragsToCompare) {
							if(MoleculeTools.getCombinedEnergy((String)atomContainer.getProperty("BondEnergy")) > bondEnergy )
							{
								addFragmentToListMapReplace(fragment, currentSumFormula);
							}
						}
	    			}
	    			
	    		}	    		
	    	}
	    	//if not in map (with this formula) add it
	    	else
	    		addFragmentToListMap(fragment, currentSumFormula);
    	}
    	else
		{
    		//sum formula has no entry in map yet
			addFragmentToListMap(fragment, currentSumFormula);
		}
    	return isomorph;
    }
    

    /**
     * Checks if two molecules are isomorph.
     * 
     * @param molecule1 the molecule1
     * @param fragsToCompare the frags to compare
     * 
     * @return true, if is isomorph
     * 
     * @throws CDKException the CDK exception
     */
    private boolean isIsomorph(IAtomContainer molecule1, List<IAtomContainer> fragsToCompare) throws CDKException 
    {
    	boolean isomorph = false;

    	for (int i = 0; i < fragsToCompare.size(); i++) {
    		//no match
    		if (molecule1.getBondCount() != fragsToCompare.get(i).getBondCount() && molecule1.getAtomCount() != fragsToCompare.get(i).getAtomCount()) 
    		{
    			continue;
    		}
    		if(UniversalIsomorphismTester.isIsomorph(molecule1, fragsToCompare.get(i)))
    			return true; 
		}
    	return isomorph;
    }
        
    
    /**
     * Very quick and easy isomorphism check.
     * 
     * @param molecule1 the molecule1
     * @param fragsToCompare the frags to compare
     * 
     * @return true, if successful
     */
    private boolean identicalAtoms(IAtomContainer molecule1, List<IAtomContainer> fragsToCompare) {
	
    	IMolecularFormula molFormula = null;
    	String molFormulaString = null;
    	if(molecularFormulaRedundancyCheck || smilesRedundancyCheck)
    	{
    		molFormula = MolecularFormulaManipulator.getMolecularFormula(molecule1);
    		molFormulaString = MolecularFormulaManipulator.getString(molFormula);
    	}
    	
    	
    	String smiles = null;
    	if(smilesRedundancyCheck)
    	{
    		smiles = (String)molecule1.getProperties().get("smiles");
    	}
    	
    	boolean[] idComparison = null;
    	if(!smilesRedundancyCheck && !molecularFormulaRedundancyCheck)
    	{
    		idComparison= new boolean[this.originalMolecule.getAtomCount()];
    		//now initialize array to compare the other candidates with it
    		for (IAtom atom : molecule1.atoms()) {
    			idComparison[Integer.parseInt(atom.getID())] = true; 
			}
    		
    	}
    	

    	for (int i = 0; i < fragsToCompare.size(); i++) 
    	{
    		//no match
    		if (molecule1.getBondCount() != fragsToCompare.get(i).getBondCount() && molecule1.getAtomCount() != fragsToCompare.get(i).getAtomCount()) 
    		{
    			continue;
    		}
    		
    		//smiles based redundancy check
    		if(smilesRedundancyCheck)
    		{   
    			if(smiles.equals((String)fragsToCompare.get(i).getProperty("smiles")))
    			{
    				return true;
    			}	
    		}
    		//Molecular Formula redundancy check
    		else if(molecularFormulaRedundancyCheck)
    		{
    			IMolecularFormula molFormulaFrag = MolecularFormulaManipulator.getMolecularFormula(fragsToCompare.get(i));
    			String molFormulaFragString = MolecularFormulaManipulator.getString(molFormulaFrag);
    			if(molFormulaString.equals(molFormulaFragString))
    				return true;
    		}
    		//atom based redundancy check
    		else
    		{
    			int count = 0;
	    		for (IAtom atom : fragsToCompare.get(i).atoms()) 
    			{				
	    			if(idComparison[Integer.parseInt(atom.getID())])
	    				count ++;
	    			else
	    				System.out.println("no match");
    			}
	    		//"isomorph" structure
	    		if(count == molecule1.getAtomCount())
	    			return true;
    		}
		}  	
    	
	    //no match found
		return false;
	}
    
    
    /**
     * Quick isomorphism check from CDK.
     * 
     * @param molecule1 the molecule1
     * @param fragsToCompare the frags to compare
     * 
     * @return true, if successful
     * 
     * @throws NoSuchAtomException the no such atom exception
     */
    private boolean quickCheck(IAtomContainer molecule1, List<IAtomContainer> fragsToCompare) throws NoSuchAtomException
    {
    	IMolecule mol = new Molecule(molecule1);
    	IsomorphismTester it = new IsomorphismTester(mol);
    	for (int i = 0; i < fragsToCompare.size(); i++) {
    		//no match
    		if (molecule1.getBondCount() != fragsToCompare.get(i).getBondCount() && molecule1.getAtomCount() != fragsToCompare.get(i).getAtomCount()) 
    		{
    			continue;
    		}

			//compare to molecule1
    		IMolecule mol2 = new Molecule(fragsToCompare.get(i));
			if (it.isIsomorphic((mol2))) {
				return true;
			}

		}
	    //no match found
		return false;
	}

    
    
    /**
     * Check in lists of bond list for an identical list of bonds.
     * If there exists one identical list: The fragments are the same.
     * MAYBE implement a better isomorphism check!?
     * 
     * @param bondListList the bond list list
     * @param bondList the bond list
     * 
     * @return true, if successful
     */
    private boolean identicalBonds(List<List<IBond>> bondListList, List<IBond> bondList) {
        
    	boolean identical = false;
    	int count = 0;
    	int size = 0;
    	
    	for (int i = 0; i < bondListList.size(); i++) {
			//different amount of bonds
    		if(bondListList.get(i).size() != bondList.size())
				continue;
    		
    		size = bondListList.get(i).size();
    		for(IBond currentBond : bondListList.get(i))
    		{
    			if(bondList.contains(currentBond))
    				count++;
    			else
    				break;
    			
    			if(count == size)
    				identical = true;
    		}
		}
    	return identical;
    }
    
    
    /**
     * Adds a fragment to the list of fragments with the current sum formula as key.
     * 
     * @param fragment the fragment
     */
    private void addFragmentToListMap(IAtomContainer frag, String currentSumFormula)
    {
    	//add sum formula molecule comb. to map
        if(molformulaToFragMap.containsKey(currentSumFormula))
        {
        	List<IAtomContainer> tempList = molformulaToFragMap.get(currentSumFormula);
        	tempList.add(frag);
        	molformulaToFragMap.put(currentSumFormula, tempList);
        }
        else
        {
        	List<IAtomContainer> temp = new ArrayList<IAtomContainer>();
        	temp.add(frag);
        	molformulaToFragMap.put(currentSumFormula, temp);
        }
    }
    
    
    /**
     * Adds a fragment to the list of fragments with the current sum formula as key and replaces 
     * the current entry.
     * 
     * @param fragment the fragment
     */
    private void addFragmentToListMapReplace(IAtomContainer frag, String currentSumFormula)
    {
    	//add sum formula molecule comb. to map
        if(molformulaToFragMap.containsKey(currentSumFormula))
        {
        	List<IAtomContainer> tempList = molformulaToFragMap.get(currentSumFormula);
        	tempList.clear();
        	tempList.add(frag);
        	molformulaToFragMap.put(currentSumFormula, tempList);
        }
        else
        {
        	List<IAtomContainer> temp = new ArrayList<IAtomContainer>();
        	temp.add(frag);
        	molformulaToFragMap.put(currentSumFormula, temp);
        }
    }
    
    /**
     * Set new minweight.
     * 
     * @param fragment the fragment
     */
    private void setMinWeight()
    {
    	//set the minimum weight
    	for (int i = 0; i< peakList.size(); i++) {
			if(peakList.get(i).getMass() < this.minWeight)
				this.minWeight = peakList.get(i).getMass();
    	}
    }
    
    /**
     * Delete peak from spectrum.
     * 
     * @param peak the peak
     */
    private void deletePeak(double peak)
    {
    	for (int i = 0; i < peakList.size(); i++) {
			if(peakList.get(i).getMass() == peak)
			{
				peakList.remove(i);
				//set new minimum weight
				this.minWeight = Double.MAX_VALUE;
				setMinWeight();
				break;
			}
		}
    }
    
    /**
     * Parses the formula for later use. Only used when the sum formulas for the peaks are supplied.
     */
    private void parseFormula()
    {
    	peakSumFormulaTable = new Vector<HashMap<String,Integer>>();
    	
    	for (int i = 0; i < this.molecularFormulas.size(); i++) {
    		
    		String formula = MolecularFormulaManipulator.getString(this.molecularFormulas.getMolecularFormula(i));
            String[] elementArray = formula.split("[0-9]+");
            String[] intArray1 = formula.split("[A-Z]+");
            String[] intArray = new String[intArray1.length -1];
            
            //TODO: delete the first entry properly
            for (int j = 1; j <= intArray.length; j++) {
				intArray[j-1] = intArray1[j];
			}
             
            
            //use map to assign letter an integer
            HashMap<String,Integer> hm = new HashMap<String, Integer>();
            for (int j = 0; j < elementArray.length; j++) {
            	int temp = Integer.parseInt(intArray[j]);
            	hm.put(elementArray[j], temp);
			}         
            
            //Add Hashmap to Vector....ordered parsed sum formulas from small to large
            peakSumFormulaTable.add(hm);
            
		}
    		
    }
    
    
    /**
     * Prepare atom weights. Get all atom weights from the sum formula
     * 
     * @param mol the mol
     */
    private void prepareAtomWeights(IAtomContainer mol) throws IOException
    {
    	IMolecularFormula molecularFormula = MolecularFormulaManipulator.getMolecularFormula(mol);
        
        List<IElement> elements = MolecularFormulaManipulator.elements(molecularFormula);
        for (IElement element : elements) {
        	IAtom a = new Atom(element);
			IsotopeFactory.getInstance(a.getBuilder()).configure(a);
			//get mass and store in map
			atomMasses.put(element.getSymbol(), a.getExactMass());
		}
    }
    

    /**
     * Make atom container from a given bond list. For each bond iterate over atoms and add them to the partContainer 
     * 
     * @param the atom
     * @param List of parts
     * 
     * @return partContainer
     */
    private IAtomContainer makeAtomContainer(IAtom atom, List<IBond> parts) {
    	
    	boolean[] atomsDone = new boolean[this.atomsContained];
    	
    	IChemObjectBuilder builder = NoNotificationChemObjectBuilder.getInstance();
        IAtomContainer partContainer = builder.newInstance(IAtomContainer.class);
        partContainer.addAtom(atom);
        
        
        atomsDone[Integer.parseInt(atom.getID())] = true;
                  
        for (IBond aBond : parts) {
            for (IAtom bondedAtom : aBond.atoms()) {
            	//check if the atom is already contained
            	if(atomsDone[Integer.parseInt(bondedAtom.getID())])
            		continue;
            	
            	partContainer.addAtom(bondedAtom);
            	atomsDone[Integer.parseInt(bondedAtom.getID())] = true;
            }
            partContainer.addBond(aBond);
        }

        return partContainer;
    }
    
    
    /**
     * Resursively traverse the molecule to get all the bonds in a list and return them. Start
     * at the given atom
     * 
     * @param atomContainer the atom container
     * @param atom the atom
     * @param bondList the bond list
     * 
     * @return the list< i bond>
     */
    private List<IBond> traverse(IAtomContainer atomContainer, IAtom atom, List<IBond> bondList, IBond bondToRemove)
    {
        List<IBond> connectedBonds = atomContainer.getConnectedBondsList(atom);
        
        
        for (IBond aBond : connectedBonds) {
            if (bondList.contains(aBond) || aBond.equals(bondToRemove))
                continue;
            bondList.add(aBond);
            
            //get the weight of the bonded atoms
            for (IAtom atomWeight : aBond.atoms()) {
            	//get the prepared mass of the atom if it is not already counted
            	if(!atomList.contains(atomWeight))
            	{
            		this.currentFragWeight += atomMasses.get(atomWeight.getSymbol());
            		atomList.add(atomWeight);
            	}
            }
            
            IAtom nextAtom = aBond.getConnectedAtom(atom);
            if (atomContainer.getConnectedAtomsCount(nextAtom) == 1)
                continue;
            traverse(atomContainer, nextAtom, bondList, bondToRemove);
        }
        return bondList;
    }
    
    /**
     * Resursively traverse the molecule to get all the bonds in a list and return them. Start at the given Atom.
     * Ignore the 2 given bonds --> split up a ring!
     * 
     * @param atomContainer the atom container
     * @param atom the atom
     * @param bondList the bond list
     * 
     * @return the list< i bond>
     */
    private List<IBond> traverse(IAtomContainer atomContainer, IAtom atom, List<IBond> bondList, IBond bondToRemove, IBond bondToRemove2) {
    	
    	List<IBond> connectedBonds = atomContainer.getConnectedBondsList(atom);
        for (IBond aBond : connectedBonds) {
            if (bondList.contains(aBond) || aBond.equals(bondToRemove) || aBond.equals(bondToRemove2))
                continue;
            bondList.add(aBond);
            //get the weight of the bonded atoms
            for (IAtom atomWeight : aBond.atoms()) {
            	//get the prepared mass of the atom if it is not already counted
            	if(!atomList.contains(atomWeight))
            	{
            		this.currentFragWeight += atomMasses.get(atomWeight.getSymbol());
            		atomList.add(atomWeight);
            	}
            }
            IAtom nextAtom = aBond.getConnectedAtom(atom);
            if (atomContainer.getConnectedAtomsCount(nextAtom) == 1)
                continue;
            traverse(atomContainer, nextAtom, bondList, bondToRemove, bondToRemove2);
        }
        return bondList;
    }
    
    /**
     * Gets the bond energy.
     * 
     * @param bond the bond
     * 
     * @return the bond energy
     */
    private double getBondEnergy(IBond bond)
    {
    	//calculate bond energy
        String bondEnergy = "";
        String bondEnergyReverse = "";
        String bondEnergyString1 = "";
        String bondEnergyString2 = "";
        boolean first = true;
        for (IAtom bondAtom : bond.atoms()) {
        	if(first)
        	{
        		bondEnergyString1 = bondAtom.getSymbol().toString();
        		first = false;
        	}
        	else
        		bondEnergyString2 = bondAtom.getSymbol().toString();
    	}
        //now check the bond order up to 3
        // - --> single bond
        // = --> double bond
        // ~ --> triple bond
        if(bond.getOrder().toString().equals("SINGLE"))
        {
        	bondEnergy = bondEnergyString1 + "-" + bondEnergyString2;
        	bondEnergyReverse = bondEnergyString2 + "-" + bondEnergyString1;
        }
        if(bond.getOrder().toString().equals("DOUBLE"))
        {
        	bondEnergy = bondEnergyString1 + "=" + bondEnergyString2;
        	bondEnergyReverse = bondEnergyString2 + "=" + bondEnergyString1;
        }
        if(bond.getOrder().toString().equals("TRIPLE"))
        {
        	bondEnergy = bondEnergyString1 + "~" + bondEnergyString2;
        	bondEnergyReverse = bondEnergyString2 + "~" + bondEnergyString1;
        }
        
        Double currentBondEnergy = 0.0;
        if(this.bondEnergies.get(bondEnergy) != null)
        	currentBondEnergy = this.bondEnergies.get(bondEnergy);
        else if (this.bondEnergies.get(bondEnergyReverse) != null)
        	currentBondEnergy = this.bondEnergies.get(bondEnergyReverse);
        else
        {
        	//not a covalent bond? just assume a C-C bond TODO!
        	currentBondEnergy = 348.0;
        	//System.out.println("COULD NOT FIND BOND ENERGY!!! " + bondEnergy + " " + bondEnergyReverse);
        }
        
        return currentBondEnergy;
    }
    
    /**
     * Write molecule to temp folder.
     * 
     * @param mol the mol
     * @param identifier the identifier
     * @param globalCount the global count
     * @param bondEnergy the bond energy
     * @param treeDepth the tree depth
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws CDKException the CDK exception
     */
    private File writeMoleculeToTemp(IAtomContainer mol, String identifier, int globalCount, String bondEnergy, Integer treeDepth, String partialChargeDiff) throws IOException, CDKException
    {
    	File temp = File.createTempFile(identifier + "_" + globalCount, ".cml");
        // Delete temp file when program exits.
        temp.deleteOnExit();
        
        mol = noStereochemistry(mol);
        
        FileWriter out = new FileWriter(temp);
        FileOutputStream fout = new FileOutputStream(temp);

//        SDFWriter mw = new SDFWriter(out);
        CMLWriter cw = new CMLWriter(fout);
        IAtomContainer tmp = mol;
        Map<Object, Object> props = mol.getProperties();
        IMolecule test = new Molecule(tmp);
        test.setProperties(props);
        test.setProperty("BondEnergy", bondEnergy);
        test.setProperty("TreeDepth", treeDepth.toString());
        test.setProperty("PartialChargeDiff", partialChargeDiff);
        cw.write(test);
        cw.close();
        
        return temp;
    }
    
    
    /**
     * No stereochemistry. Fix for BUG in SDF writer with CDK 1.3.4
     * 
     * @param mol the mol
     * 
     * @return the i atom container
     */
    private IAtomContainer noStereochemistry(IAtomContainer mol)
    {
    	for (IBond bond : mol.bonds()) {
			Stereo stereo = bond.getStereo();
			bond.setStereo(stereo.NONE);
		}    	
    	return mol;
    }  
    
    public void setMoleculeDescriptors(Map<String, Object> moleculeDescriptors) {
    	this.moleculeDescriptors = moleculeDescriptors;
    }

    /**
    * Gets the molecule descriptors.
    * Implemented are 3 different logP values: XlogP, AlogP and MannholdLogP
    *
    * @return the molecule descriptors
    */
    public Map<String, Object> getMoleculeDescriptors() {
    	return moleculeDescriptors;
    }
    
    /**
     * 
     * Returns the original molecule
     * @return
     */
    public IAtomContainer getOriginalMolecule()
    {
    	return this.originalMolecule;
    }

}