package de.ipbhalle.metfrag.spectrum;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openscience.cdk.interfaces.IMolecularFormulaSet;
import org.openscience.cdk.formula.MassToFormulaTool;

import de.ipbhalle.metfrag.massbankParser.*;



/**
 * Wrapper for Massbank parser.
 */
public class WrapperSpectrum {
	
	/** The spectra. */
	private Vector<Spectrum> spectra;
	/** The peaks. */
	private Vector<Peak> peaks;
	/** The candidates. */
	private IMolecularFormulaSet candidates;
	private double exactMass;
	private int mode;
	private int collisionEnergy;
	private String InchI;
	private int CID;
	private String KEGG;
	private String nameTrivial;
	private String filename;
	private String formula;
	
	
	/**
	 * Reads in a MassBank flat file from a given location.
	 * 
	 * @param filename the filename
	 */
	public WrapperSpectrum(String filename){
		this.spectra = MassbankParser.Read(filename);
		this.peaks = spectra.get(0).getPeaks(); //just one spectra for now
		this.exactMass = spectra.get(0).getExactMass();
		this.mode = spectra.get(0).getMode();
		this.collisionEnergy = spectra.get(0).getCollisionEnergy();
		this.InchI = spectra.get(0).getInchi();
		this.CID = spectra.get(0).getCID();
		this.KEGG = spectra.get(0).getKEGG();
		this.nameTrivial = spectra.get(0).getTrivialName();
		String[] fileTemp = filename.split("\\/");
		this.filename = fileTemp[fileTemp.length - 1];
		this.setFormula(spectra.get(0).getFormula());
	}
	
	
	/**
	 * Creates a new Spectrum with a given peaklist...used for the web interface
	 * Ignores inchi, keggID, CID (-1), trivial name, collision energy (set to -1)
	 * 
	 * @param Peaks the peaks
	 * @param mode the mode
	 * @param exactMass the exact mass
	 */
	public WrapperSpectrum(String peakString, int mode, double exactMass){
		this.spectra = new Vector<Spectrum>();
		this.collisionEnergy = -1;
		spectra.add(new Spectrum(-1, parsePeaks(peakString), exactMass, mode, "none", -1, "none", "none",""));
		
		this.peaks = spectra.get(0).getPeaks(); //just one spectra for now
		this.exactMass = spectra.get(0).getExactMass();
		this.mode = spectra.get(0).getMode();
		this.InchI = spectra.get(0).getInchi();
		this.CID = spectra.get(0).getCID();
		this.KEGG = spectra.get(0).getKEGG();
		this.nameTrivial = spectra.get(0).getTrivialName();
		this.setFormula(spectra.get(0).getFormula());
	}
	
	
	/**
	 * Parses the peaks String from the web interface.
	 * 
	 * @param peakString the peak string
	 * 
	 * @return the vector<Peak>
	 */
	private Vector<Peak> parsePeaks(String peakString)
	{
		//replace all "," with "."
		peakString = peakString.replaceAll(",", ".");
		
		
		// Compile regular expression --> remove spaces in front and end of lines
        Pattern pattern = Pattern.compile("^[ \t]+|[ \t]+$");
        //replace all tabs whitespaces at end of the line
        Pattern patternTabs = Pattern.compile("[\t]+");
        
		Vector<Peak> parsedPeaks = new Vector<Peak>();
		String[] lines = peakString.split("\\\n");
		for (String line : lines) {
			//skip comment
			if(line.startsWith("#"))
				continue;
			
	        // Replace all occurrences of pattern in input
	        Matcher matcher = pattern.matcher(line);
	        String output = matcher.replaceAll("");
	        
	        output = output.replaceAll("  ", " ");
	    
	        Matcher matcherTabs = patternTabs.matcher(output);
	        output = matcherTabs.replaceAll(" ");
	        
			String[] array = output.split(" ");
			String[] arrayClean = new String[3];
			
			int count = 0;
			for (int i = 0; i < array.length; i++) {
				if(array[i] == null || array[i].equals(""))
					continue;
				else
				{
					arrayClean[count] = array[i];
					count++;
				}
			}
			
			//no absolute intensity is given
			if(count == 2)
			{
				parsedPeaks.add(new Peak(Double.parseDouble(arrayClean[0]), Double.parseDouble(arrayClean[1]), 0.0, collisionEnergy));
				calcRelIntensity(parsedPeaks);
			}
			//rel intensity is given
			else if(count == 3)
			{
				parsedPeaks.add(new Peak(Double.parseDouble(arrayClean[0]), Double.parseDouble(arrayClean[1]), Double.parseDouble(arrayClean[2]), collisionEnergy));
			}
		}
		return parsedPeaks;
	}
	
	
	
	/**
	 * Calc rel intensity.
	 * 
	 * @param peakList the peak list
	 */
	private void calcRelIntensity(Vector<Peak> peakList)
	{
		double maxAbsInt = 0.0;
		for (Peak peak : peakList) {
			if(peak.getRelIntensity() > maxAbsInt)
				maxAbsInt = peak.getRelIntensity();
		}
		
		//calculate the rel. intensity
		for (Peak peak : peakList) {
			peak.setRelIntensity((peak.getIntensity() / maxAbsInt) * 999);
		}
	}
	
	
	/**
	 * Set a new peakList.
	 * 
	 * @param peakList the peak list
	 * 
	 */
	public void setPeakList(Vector<Peak> peakList)
	{
		spectra.get(0).setPeaks(peakList);
		this.peaks = peakList;
	}
	
	
	/**
	 * Returns the filename.
	 * 
	 * @return the string
	 */
	public String getFilename()
	{
		return this.filename.split("\\.")[0];
	}
	
	
	/**
	 * Sets the filename. (Removes the file extension)
	 * 
	 * @param filename the new filename
	 */
	public void setFilename(String filename)
	{
		this.filename = filename.split("\\.")[0];
	}
	
	
	/**
	 * Sets the exact mass. Fix for bug in records.
	 * 
	 * @param Mass the new exact mass
	 */
	public void setExactMass(double Mass)
	{
		this.exactMass = Mass;
	}
	
	/**
	 * Gets the InchI.
	 * 
	 * @return the InchI
	 */
	public String getInchI()
	{
		return this.InchI;
	}
	
	/**
	 * Gets the KEGG ID.
	 * 
	 * @return the KEGG ID
	 */
	public String getKEGG()
	{
		return this.KEGG;
	}
	
	/**
	 * Gets the Pubchem CID.
	 * 
	 * @return the Pubchem CID
	 */
	public int getCID()
	{
		return this.CID;
	}
	
	/**
	 * Gets the collision energy.
	 * 
	 * @return the int
	 */
	public int getCollisionEnergy()
	{
		return this.collisionEnergy;
	}
	
	/**
	 * Identify peak. not used
	 * TODO: Use peak identifier from Jena.
	 * 
	 * @param mass the mass
	 * 
	 * @return the i molecular formula set
	 */
	public IMolecularFormulaSet IdentifyPeak(double mass){
		
//		MassToFormulaTool mf = new MassToFormulaTool();
//		candidates = mf.generate(mass);
//		return this.candidates;
		return null;
	}	
	
	
	/**
	 * Gets the peakList.
	 * 
	 * @return the peaks
	 */
	public Vector<Peak> getPeakList()
	{
		return this.peaks;
	}
	
	/**
	 * Gets the parent peak.
	 * 
	 * @return the parent peak
	 */
	public double getExactMass()
	{
		return this.exactMass;
	}
	
	/**
	 * Gets the mode. +1 or -1.
	 * 
	 * @return the mode
	 */
	public int getMode()
	{
		return this.mode;
	}
	
	/**
	 * Gets the trivial name.
	 * 
	 * @return the trivial name
	 */
	public String getTrivialName()
	{
		return this.nameTrivial;
	}


	public void setFormula(String formula) {
		this.formula = formula;
	}


	public String getFormula() {
		return formula;
	}
	
}
