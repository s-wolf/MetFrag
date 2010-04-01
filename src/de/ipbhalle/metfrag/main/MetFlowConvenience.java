package de.ipbhalle.metfrag.main;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.graph.ConnectivityChecker;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.io.MDLReader;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

import de.ipbhalle.metfrag.chemspiderClient.ChemSpider;
import de.ipbhalle.metfrag.fragmenter.Fragmenter;
import de.ipbhalle.metfrag.keggWebservice.KeggWebservice;
import de.ipbhalle.metfrag.massbankParser.Peak;
import de.ipbhalle.metfrag.molDatabase.PubChemLocal;
import de.ipbhalle.metfrag.pubchem.PubChemWebService;
import de.ipbhalle.metfrag.scoring.Scoring;
import de.ipbhalle.metfrag.spectrum.AssignFragmentPeak;
import de.ipbhalle.metfrag.spectrum.CleanUpPeakList;
import de.ipbhalle.metfrag.spectrum.PeakMolPair;
import de.ipbhalle.metfrag.spectrum.WrapperSpectrum;
import de.ipbhalle.metfrag.tools.MolecularFormulaTools;
import de.ipbhalle.metfrag.tools.PPMTool;


public class MetFlowConvenience {
	
	
	private boolean sumFormulaRedundancyCheck = false;
	private String database = "kegg";
	private String sumFormula = "";
	private int limit = 100;
	private int mode = 1;
	private double exactMass = 272.06847;


	private double mzabs = 0.01;
	private double mzppm = 50;
	private double searchPPM = 10;
	private int count = 0;
	private int hitsDatabase = 0;
	private String peaks = "119.051 467.616 45\n" +
	   "123.044 370.662 36\n" +
	   "147.044 6078.145 606\n" +
	   "153.019 10000.0 999\n" +
	   "179.036 141.192 13\n" +
	   "189.058 176.358 16\n" +
	   "273.076 10000.000 999\n" +
	   "274.083 318.003 30\n";	
	private String databaseID = "";
	
	
	
	
	/**
	 * Instantiates a new database retrieval just for testing with the default values.
	 */
	public MetFlowConvenience()
	{
		
	}
	
	/**
	 * Instantiate and set all parameters needed for MetFrag.
	 * 
	 * @param mzabs the mzabs
	 * @param mzppm the mzppm
	 * @param searchPPM the search ppm
	 * @param peaks the peaks
	 * @param database the database
	 * @param sumFormulaRedundancyCheck the sum formula redundancy check
	 * @param sumFormula the sum formula
	 * @param limit the limit
	 * @param mode the mode
	 * @param exactMass the exact mass
	 */
	public MetFlowConvenience(double mzabs, double mzppm, double searchPPM, String peaks, String database, boolean sumFormulaRedundancyCheck, String sumFormula, int limit, int mode, double exactMass)
	{
		this.mzabs = mzabs;
		this.mzppm = mzppm;
		this.searchPPM = searchPPM;
		this.peaks = peaks;
		this.database = database;
		this.sumFormulaRedundancyCheck = sumFormulaRedundancyCheck;
		this.sumFormula = sumFormula;
		this.limit = limit;
		this.mode = mode;
		this.exactMass = exactMass;
	}
	
	
	/**
	 * MetFrag convenience method!!!! :)
	 * 
	 * Retrieve candidate molecules from the selected database and fragment them.
	 * A String is returned which contains the a list sorted by their score.
	 * 
	 * @throws Exception the exception
	 */
	public String metFrag() throws Exception
	{
		
		PubChemWebService pubchem = new PubChemWebService();
		Vector<String> candidates = null;
		
		System.out.println("Search PPM: " + this.searchPPM);
		
		if(this.database.equals("kegg") && databaseID.equals(""))
		{
			if(this.sumFormula != "")
				candidates = KeggWebservice.KEGGbySumFormula(this.sumFormula);
			else
				candidates = KeggWebservice.KEGGbyMass(exactMass, (PPMTool.getPPMDeviation(exactMass, this.searchPPM)));
		}
		else if(this.database.equals("chemspider") && databaseID.equals(""))
		{
			if(this.sumFormula != "")
				candidates = ChemSpider.getChemspiderBySumFormula(this.sumFormula);
			else
				candidates = ChemSpider.getChemspiderByMass(exactMass, (PPMTool.getPPMDeviation(exactMass, this.searchPPM)));
		}
		else if(this.database.equals("pubchem") && databaseID.equals(""))
		{
			if(this.sumFormula != "")
				candidates = pubchem.getHitsbySumFormula(this.sumFormula);
			else
				candidates = pubchem.getHitsByMass(exactMass, (PPMTool.getPPMDeviation(exactMass, this.searchPPM)), this.limit);
		}
		else if (!databaseID.equals(""))
		{
			candidates = new Vector<String>();
			candidates.add(databaseID);
		}
		
		if(this.limit < this.hitsDatabase)
			this.hitsDatabase = this.limit;
		else
			this.hitsDatabase = candidates.size();
		

		WrapperSpectrum spectrum = new WrapperSpectrum(this.peaks, mode, exactMass);
		HashMap<Integer, ArrayList<String>> scoreMap = new HashMap<Integer, ArrayList<String>>();
		HashMap<Double, Vector<String>> realScoreMap = new HashMap<Double, Vector<String>>();
		
		
		this.hitsDatabase = candidates.size();
		
		count = 0;
		
		for (int c = 0; c < candidates.size(); c++) {
			
			Vector<Peak> listOfPeaks = new Vector<Peak>();

	        //get mol file from kegg....remove "cpd:"
			String candidateMol = "";
			String candidateID = "";
			IAtomContainer molecule = null;
			
			try
			{
				if(this.database.equals("kegg"))
				{					
					candidateMol = KeggWebservice.KEGGgetMol(candidates.get(c).substring(4), "/vol/data/pathways/kegg/mol/");
					candidateID = candidates.get(c).substring(4);
					MDLReader reader;
					List<IAtomContainer> containersList;
					
			        reader = new MDLReader(new StringReader(candidateMol));
			        ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());
			        containersList = ChemFileManipulator.getAllAtomContainers(chemFile);
			        molecule = containersList.get(0);
					
				}
				else if(this.database.equals("chemspider"))
				{
					candidateMol = ChemSpider.getMolByID(candidates.get(c));
					candidateID = candidates.get(c);
					
					MDLReader reader;
					List<IAtomContainer> containersList;
					
			        reader = new MDLReader(new StringReader(candidateMol));
			        ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());
			        containersList = ChemFileManipulator.getAllAtomContainers(chemFile);
			        molecule = containersList.get(0);
			        
				}
				else if(this.database.equals("pubchem") && databaseID.equals(""))
				{
					candidateID = candidates.get(c);
					molecule = pubchem.getMol(candidates.get(c));
				}
				else if(this.database.equals("pubchem") && !databaseID.equals(""))
				{
					candidateID = candidates.get(c);
					molecule = pubchem.getMol(candidates.get(c));
				}
				else
				{
					System.err.println("ERROR: no database selected!!!!");
				}
			}
			//some error in the molecule!?
			catch(ArrayIndexOutOfBoundsException e)
			{
				System.out.println("Some error reading the molecule: " + candidateID + " (" + this.database + ")");
				System.out.println("Error: " + e.getMessage());
				e.printStackTrace();
			}
			
			
			//skip if molecule is not connected
			boolean isConnected = ConnectivityChecker.isConnected(molecule);
			if(!isConnected)
				continue;
	        
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
	        	//skip it
            	continue;
            }
	        
	        
	        //render original compound....thats the first picture in the list
			int countTemp = 0;

			
			Double massDoubleOrig = MolecularFormulaTools.getMonoisotopicMass(MolecularFormulaManipulator.getMolecularFormula(molecule));
			massDoubleOrig = (double)Math.round((massDoubleOrig)*10000)/10000;
			String massOrig = massDoubleOrig.toString();
			countTemp++;
			        
	        Fragmenter fragmenter = new Fragmenter((Vector<Peak>)spectrum.getPeakList().clone(), mzabs, mzppm, mode, true, this.sumFormulaRedundancyCheck, false, false);     
	        List<IAtomContainer> l = null;
	        try
	        {
	        	l = fragmenter.generateFragmentsInMemory(molecule, true, 2);
	        }
	        catch(OutOfMemoryError e)
	        {
	        	System.out.println("OUT OF MEMORY ERROR! " + candidateID);
	        	continue;
	        }
        
		        
		    List<IAtomContainer> fragments = l;  
			
			//get the original peak list again
			Vector<Peak> peakListParsed = spectrum.getPeakList();
			
			
			//clean up peak list
			CleanUpPeakList cList = new CleanUpPeakList((Vector<Peak>) peakListParsed.clone());
			Vector<Peak> cleanedPeakList = cList.getCleanedPeakList(spectrum.getExactMass());
			
			
			//now find corresponding fragments to the mass
			AssignFragmentPeak afp = new AssignFragmentPeak();
			afp.setHydrogenTest(true);
			afp.assignFragmentPeak(fragments, cleanedPeakList, mzabs, mzppm, spectrum.getMode(), false);
			
			Vector<PeakMolPair> hits = afp.getHits();			
			
			
			//now "real" scoring --> depends on intensities
			Scoring score = new Scoring(spectrum.getPeakList());
			double currentScore = score.computeScoringPeakMolPair(afp.getHits());
			
			//save score in hashmap...if there are several hits with the same score --> vector of strings
			if(realScoreMap.containsKey(currentScore))
	        {
	        	Vector<String> tempList = realScoreMap.get(currentScore);
	        	tempList.add(candidateID);
	        	realScoreMap.put(currentScore, tempList);
	        }
	        else
	        {
	        	Vector<String> temp = new Vector<String>();
	        	temp.add(candidateID);
	        	realScoreMap.put(currentScore, temp);
	        }
			
			
			//save score in hashmap...if there are several hits with the same
			//amount of identified peaks --> ArrayList
			if(scoreMap.containsKey(hits.size()))
	        {
	        	ArrayList<String> tempList = scoreMap.get(hits.size());
	        	tempList.add(candidateID);
	        	scoreMap.put(hits.size(), tempList);
	        }
	        else
	        {
	        	ArrayList<String> temp = new ArrayList<String>();
	        	temp.add(candidateID);
	        	scoreMap.put(hits.size(), temp);
	        }
			
			Vector<Double> peaks = new Vector<Double>();
			Vector<Double> intensities = new Vector<Double>();
			
			//get all the identified peaks
			for (int i = 0; i < hits.size(); i++) {
				listOfPeaks.add(hits.get(i).getPeak());
				peaks.add(hits.get(i).getPeak().getMass());
				intensities.add(hits.get(i).getPeak().getRelIntensity());
				//all found peaks are later on marked in the spectrum
				//xyFound.add(hits.get(i).getPeak().getMass(), hits.get(i).getPeak().getRelIntensity());
			}

			
			List<IAtomContainer> hitsList = new ArrayList<IAtomContainer>();
			for (int i = 0; i < hits.size(); i++) {
				hitsList.add(AtomContainerManipulator.removeHydrogens(hits.get(i).getFragment()));
				//Render.Highlight(AtomContainerManipulator.removeHydrogens(molecule), hitsList , Double.toString(hits.get(i).getPeak()));
			}
			
			System.out.println(" " + candidateID + " " + afp.getHits().size() + " " + currentScore);
			//done fragments
			count++;
			
			//break is enough compounds were processed
			if(count == this.limit)
				break;
		}
		
		String ret = "";
		
		
		HashMap<Double, Vector<String>> scoresNormalized = normalize(realScoreMap);
		Double[] scores = new Double[scoresNormalized.size()];
		scores = scoresNormalized.keySet().toArray(scores);
		Arrays.sort(scores);
		
		//now collect the result
		for (int i = scores.length -1; i >=0 ; i--) {
			Vector<String> list = scoresNormalized.get(scores[i]);
			for (String string : list) {
				
				if(this.database.equals("kegg"))
				{
					String[] names = KeggWebservice.KEGGgetNameByCpdLocally(string, "/vol/mirrors/kegg/compound");
					String name = "";
					for (int j = 0; j < names.length; j++) {
						if(j == names.length -1 )
							name += names[j];
						else
							name += names[j] + "#";

					}
					ret += string + "\t" + name + "\t" + scores[i] + "\n";
				}
				else
				{
					ret += string + "\t" + scores[i] + "\n";
				}
			}
		}
		
		return ret;
	}
	
	
	/**
	 * Normalize the score between 0 and 1.
	 */
	private HashMap<Double, Vector<String>> normalize(HashMap<Double, Vector<String>> realScoreMap)
	{
		HashMap<Double, Vector<String>> ret = new HashMap<Double, Vector<String>>();
		
		double maxScore = 0; 
		for (Double score : realScoreMap.keySet()) {
			if(score > maxScore)
			{
				maxScore = score;
			}
		}
		
		for (Double score : realScoreMap.keySet()) {
			Vector<String> list = realScoreMap.get(score);
			ret.put((Math.round((score / maxScore) * 1000) / 1000.0), list);
		}
		
		return ret;
	}
	
	
	public static void main(String[] args) {
//		MetFlowConvenience test = new MetFlowConvenience();
//		try {
//			System.out.println(test.metFrag());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		String peaks = "119.051 467.616 45\n123.044 370.662 36\n" +
		"147.044 6078.145 606\n153.019 10000.0 999\n179.036 141.192 13\n189.058 176.358 16\n" +
		"273.076 10000.000 999\n274.083 318.003 30\n";
		MetFlowConvenience mfc = new MetFlowConvenience(0.01, 50.0, 10.0, peaks, "kegg", true, "", 50, 1, 272.06847);
		
		try {
			System.out.println(mfc.metFrag());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
