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
package de.ipbhalle.metfrag.spectrum;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecularFormulaSet;
import org.openscience.cdk.io.SDFWriter;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.formula.MassToFormulaTool;

import de.ipbhalle.metfrag.databaseMetChem.Query;
import de.ipbhalle.metfrag.main.Config;
import de.ipbhalle.metfrag.massbankParser.*;
import de.ipbhalle.metfrag.molDatabase.PubChemLocal;
import de.ipbhalle.metfrag.read.SDFFile;




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
	private String chebi;
	private String nameTrivial;
	private String filename;
	private String formula;
	private String precursorType;
	private double precursorMZ;
	private boolean isPositive;
	private String smiles;
	
	
	/**
	 * Reads in a MassBank flat file from a given location.
	 * 
	 * @param filename the filename
	 */
	public WrapperSpectrum(String filename){
		this.spectra = NewMassbankParser.Read(filename);
		this.peaks = spectra.get(0).getPeaks(); //just one spectra for now
		this.exactMass = spectra.get(0).getExactMass();
		this.mode = spectra.get(0).getMode();
		this.collisionEnergy = spectra.get(0).getCollisionEnergy();
		this.InchI = spectra.get(0).getInchi();
		this.CID = spectra.get(0).getCID();
		this.KEGG = spectra.get(0).getKEGG();
		this.nameTrivial = spectra.get(0).getTrivialName();
		this.chebi = spectra.get(0).getCHEBI();
		this.setPrecursorType(spectra.get(0).getPrecursorType());
		this.setPrecursorMZ(spectra.get(0).getPrecursorMZ());
		String[] fileTemp = filename.split("\\/");
		this.filename = fileTemp[fileTemp.length - 1];
		this.setFormula(spectra.get(0).getFormula());
		this.isPositive = spectra.get(0).isPositive();
		
		this.smiles=spectra.get(0).getSmiles();
	}
	
	
	/**
	 * Creates a new Spectrum with a given peaklist...used for the web interface
	 * Ignores inchi, keggID, CID (-1), trivial name, collision energy (set to -1)
	 * 
	 * @param Peaks the peaks
	 * @param mode the mode
	 * @param exactMass the exact mass
	 */
	public WrapperSpectrum(String peakString, int mode, double exactMass, boolean isPositive, String smiles){
		this.spectra = new Vector<Spectrum>();
		this.collisionEnergy = -1;
		spectra.add(new Spectrum(-1, parsePeaks(peakString), exactMass, mode, "none", -1, "none", "none", "none", "", 0.0, "", isPositive,smiles));

		this.peaks = spectra.get(0).getPeaks(); //just one spectra for now
		this.exactMass = spectra.get(0).getExactMass();
		this.mode = spectra.get(0).getMode();
		this.InchI = spectra.get(0).getInchi();
		this.CID = spectra.get(0).getCID();
		this.KEGG = spectra.get(0).getKEGG();
		this.chebi = spectra.get(0).getCHEBI();
		this.nameTrivial = spectra.get(0).getTrivialName();
		this.setFormula(spectra.get(0).getFormula());
		this.setPositive(isPositive);
		
		this.smiles =smiles;
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
			
			//no rel intensity is given
			if(count == 2)
			{
				parsedPeaks.add(new Peak(Double.parseDouble(arrayClean[0]), Double.parseDouble(arrayClean[1]), 0.0, collisionEnergy));
			}
			//rel intensity is given
			else if(count == 3)
			{
				parsedPeaks.add(new Peak(Double.parseDouble(arrayClean[0]), Double.parseDouble(arrayClean[1]), Double.parseDouble(arrayClean[2]), collisionEnergy));
			}
		}
		
		parsedPeaks = calcRelIntensity(parsedPeaks);
		
		return parsedPeaks;
	}
	
	
	
	/**
	 * Calc rel intensity.
	 * 
	 * @param peakList the peak list
	 */
	private Vector<Peak> calcRelIntensity(Vector<Peak> peakList)
	{
		double maxAbsInt = 0.0;
		for (Peak peak : peakList) {
			if(peak.getIntensity() > maxAbsInt)
				maxAbsInt = peak.getIntensity();
		}
		
		//calculate the rel. intensity
		for (Peak peak : peakList) {
			peak.setRelIntensity(Math.round(((peak.getIntensity() / maxAbsInt) * 999)));
		}
		
		return peakList;
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
	 * Gets the cleaned peaks.
	 * More precisely all peaks with an intensity smaller than 5% of intensity of the peak with the maximal intensity are removed.  
	 * 
	 * @return the cleaned peaks
	 */
	public Vector<Peak> getCleanedPeakList()
	{
		
		double intensities[]=new double[this.peaks.size()];
		
		Vector<Peak> cleanedPeaks = new Vector<Peak>();;
		
		int k=0;
		for (Iterator iterator = peaks.iterator(); iterator.hasNext();) {
			Peak peak = (Peak) iterator.next();
			intensities[k] = peak.getRelIntensity();
			k++;
			cleanedPeaks.add(peak);
		}
		
		double maxIntensity=intensities[0];
		for(int i=1;i<intensities.length;i++)
		{
			if(intensities[i]>maxIntensity)
			{
				maxIntensity=intensities[i];
			}
		}
		
		
		int anz=intensities.length;
		
		for(int iter=0;iter<anz;iter++)
		{		
			if(cleanedPeaks.elementAt(iter).getRelIntensity() < (0.05*maxIntensity))
			{
				
				cleanedPeaks.removeElementAt(iter);
				iter--;
				anz--;			
			}
		}
		
		return cleanedPeaks;
	}
	
	
	public String toString()
	{
		StringBuilder str = new StringBuilder();
		Spectrum currentSpectrum = spectra.get(0);
		str.append("Trvial Name: " + currentSpectrum.getTrivialName() + "\n");
		str.append("Exact Mass: " + currentSpectrum.getExactMass() + "\n");
		str.append("Formula: " + currentSpectrum.getFormula() + "\n");
		str.append("CHEBI: " + currentSpectrum.getCHEBI() + "\n");
		str.append("PubChem CID: " + currentSpectrum.getCID() + "\n");
		str.append("KEGG: " + currentSpectrum.getKEGG() + "\n");
		str.append("Precursor MZ: " + currentSpectrum.getPrecursorMZ() + "\n");
		str.append("Precursor Type: " + currentSpectrum.getPrecursorType() + "\n");
		str.append("Collision Energy: " + currentSpectrum.getCollisionEnergy() + "\n");
		str.append("Mode: " + currentSpectrum.getMode() + " Positive: " + currentSpectrum.isPositive() + "\n");
		str.append("Filename: " + this.filename + "\n");
		
		String peaksString = "";
		for (Peak peak : currentSpectrum.getPeaks()) {
			peaksString += peak.getMass() + " " + peak.getRelIntensity() + "\n"; 
		}
		str.append(peaksString + "\n");
		
		return str.toString();
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


	/**
	 * Sets the molecular formula.
	 *
	 * @param formula the new formula
	 */
	public void setFormula(String formula) {
		this.formula = formula;
	}


	/**
	 * Gets the molecular formula.
	 *
	 * @return the formula
	 */
	public String getFormula() {
		return formula;
	}


	/**
	 * Sets the precursor type as defined in the massbank record.
	 *
	 * @param precursorType the new precursor type
	 */
	public void setPrecursorType(String precursorType) {
		this.precursorType = precursorType;
	}


	/**
	 * Gets the precursor type as defined in the massbank record.
	 *
	 * @return the precursor type
	 */
	public String getPrecursorType() {
		return precursorType;
	}

	
	/**
	 * Sets the precursor MZ as defined in the massbank record.
	 *
	 * @param precursorMZ the new precursor MZ
	 */
	public void setPrecursorMZ(double precursorMZ) {
		this.precursorMZ = precursorMZ;
	}


	/**
	 * Gets the precursor MZ as defined in the massbank record.
	 *
	 * @return the precursor MZ
	 */
	public double getPrecursorMZ() {
		return precursorMZ;
	}
	
	public void setPositive(boolean isPositive) {
		this.isPositive = isPositive;
	}


	public boolean isPositive() {
		return isPositive;
	}


	public void setChebi(String chebi) {
		this.chebi = chebi;
	}


	public String getChebi() {
		return chebi;
	}
	
	
	public String getSmiles(){
		
		return smiles;
	}
	
	public static void main(String[] args) {
		WrapperSpectrum spectrum = new WrapperSpectrum("/home/swolf/MassBankData/TestSpectra/HillMerged/CO000056CO000057CO000058CO000059CO000060.txt");
		System.out.println(spectrum.toString());
		System.out.println("PubChemID: " + spectrum.getCID());
		Config conf;
		try {
			conf = new Config();
			Query query = new Query(conf.getUsernamePostgres(), conf.getPasswordPostgres(), conf.getJdbcPostgres());
			IAtomContainer mol = query.getCompoundUsingIdentifier(Integer.toString(spectrum.getCID()), "pubchem");
			System.out.println(mol.getAtomCount());
		}
		catch(IOException e)
		{
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CDKException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		File folder = new File("/home/swolf/MassBankData/TestSpectra/HillMerged/");
//		File[] fileArr = folder.listFiles();
//		for (int i = 0; i < fileArr.length; i++) {
//			
//			if(!fileArr[i].isFile())
//				continue;
//			
//			if(fileArr[i].getName().split("\\.")[1].equals("txt"))
//			{
//				WrapperSpectrum spectrum = new WrapperSpectrum(fileArr[i].toString());
//				Config conf;
//				try {
//					conf = new Config();
//					Query query = new Query(conf.getUsernamePostgres(), conf.getPasswordPostgres(), conf.getJdbcPostgres());
//					IAtomContainer mol = query.getCompoundUsingIdentifier(Integer.toString(spectrum.getCID()), "pubchem");
//					SDFWriter writer = new SDFWriter(new FileOutputStream(new File("/home/swolf/MOPAC/ProofOfConcept/Hill_OnlyCorrect/" + fileArr[i].getName().split("\\.")[0] + ".sdf")));
//					writer.write(mol);
//					writer.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (InvalidSmilesException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (CDKException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//			}
//		}
	}
}
