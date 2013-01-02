/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package de.ipbhalle.metfrag.massbankParser;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.Collections;
import java.util.Iterator;

import org.eclipse.swt.internal.cde.DtActionArg;


public class Spectrum  implements java.io.Serializable, Comparable<Spectrum> {

	private int collisionEnergy;

	private Vector<Peak> peaks;
	private double tic;
	private double exactMass;
	private int mode;
	private String InchI;
	private int CID;
	private String KEGG;
	private String CHEBI;
	private String metlin;
	private String knapsack;
	private String nameTrivial;
	private String formula;
	private boolean isPositive;
	private double precursorMZ;
	private String precursorType;
	
	private String smiles;
	
	public Map<DatabaseIDs,String> dblinks;
	
	public Spectrum(int collisionEnergy, Vector<Peak> peaks, double exactMass, int mode, String InchI, Map<DatabaseIDs,String> dblinks ,String nameTrivial, String formula, double precursorMZ, String precursorType, boolean isPositive,String smiles){
		this( collisionEnergy, 0.0, peaks, exactMass, mode, InchI, dblinks, nameTrivial, formula, precursorMZ, precursorType, isPositive,smiles);
	}
	
	public Spectrum(int collisionEnergy, double tic, Vector<Peak> peaks, double exactMass, int mode, String InchI, Map<DatabaseIDs,String> dblinks, String nameTrivial, String formula, double precursorMZ, String precursorType, boolean isPositive,String smiles)
	{
		this.tic = tic;
		this.peaks = peaks;
		this.exactMass = exactMass;
		this.mode = mode;
		this.InchI = InchI;

		this.nameTrivial = nameTrivial;
		this.precursorMZ = precursorMZ;
		this.precursorType = precursorType;
		this.setFormula(formula);
		this.isPositive = isPositive;

		this.smiles = smiles;
		
		this.dblinks=dblinks;
		
		//TODO: perhaps delete the following things
		this.CID=0;
		this.KEGG="none";
		this.CHEBI="";
		this.metlin="";
		this.knapsack="";
		if(dblinks.containsKey(DatabaseIDs.PUBCHEM_CID))
		{
			this.CID = Integer.parseInt(dblinks.get(DatabaseIDs.PUBCHEM_CID));
		}
		if(dblinks.containsKey(DatabaseIDs.KEGG))
		{
			this.KEGG=dblinks.get(DatabaseIDs.KEGG);
		}
		if(dblinks.containsKey(DatabaseIDs.CHEBI))
		{
			this.CHEBI= dblinks.get(DatabaseIDs.CHEBI);
		}
		if(dblinks.containsKey(DatabaseIDs.METLIN))
		{
			this.metlin= dblinks.get(DatabaseIDs.METLIN);
		}
		if(dblinks.containsKey(DatabaseIDs.KNAPSACK))
		{
			this.knapsack=dblinks.get(DatabaseIDs.KNAPSACK);
		}
		
		
	}
	
	public Spectrum(int collisionEnergy, double tic, Vector<Peak> peaks, double exactMass, int mode, String InchI, int CID, String KEGG, String CHEBI,String metlin,String knapsack, String nameTrivial, String formula, double precursorMZ, String precursorType, boolean isPositive, String smiles){
		this.collisionEnergy =  collisionEnergy;
		
		this.tic = tic;
		this.peaks = peaks;
		this.exactMass = exactMass;
		this.mode = mode;
		this.InchI = InchI;
		this.CID = CID;
		this.KEGG = KEGG;
		this.metlin = metlin;
		this.knapsack = knapsack;
		this.nameTrivial = nameTrivial;
		this.precursorMZ = precursorMZ;
		this.precursorType = precursorType;
		this.setFormula(formula);
		this.isPositive = isPositive;
		this.CHEBI = CHEBI;
		
		this.smiles = smiles;
	}
	
	
	public Spectrum(int collisionEnergy, double tic, Vector<Peak> peaks, double exactMass, int mode, String InchI, int CID, String KEGG, String CHEBI,String metlin, String nameTrivial, String formula, double precursorMZ, String precursorType, boolean isPositive){
		this.collisionEnergy =  collisionEnergy;
		
		this.tic = tic;
		this.peaks = peaks;
		this.exactMass = exactMass;
		this.mode = mode;
		this.InchI = InchI;
		this.CID = CID;
		this.KEGG = KEGG;
		this.metlin=metlin;
		this.nameTrivial = nameTrivial;
		this.precursorMZ = precursorMZ;
		this.precursorType = precursorType;
		this.setFormula(formula);
		this.isPositive = isPositive;
		this.CHEBI = CHEBI;
	}
	
	
	
	public Spectrum(int collisionEnergy, Vector<Peak> peaks, double exactMass, int mode, String InchI, int CID, String KEGG, String linkCHEBI,String linkMetlin, String linkKnapsack ,String nameTrivial, String formula, double precursorMZ, String precursorType, boolean isPositive,String smiles){
		this( collisionEnergy, 0.0, peaks, exactMass, mode, InchI, CID, KEGG, linkCHEBI,linkMetlin,linkKnapsack, nameTrivial, formula, precursorMZ, precursorType, isPositive,smiles);
	}	
	
	public Spectrum(int collisionEnergy, Vector<Peak> peaks, double exactMass, int mode, String InchI, int CID, String KEGG, String linkCHEBI,String linkMetlin, String nameTrivial, String formula, double precursorMZ, String precursorType, boolean isPositive){
		this( collisionEnergy, 0.0, peaks, exactMass, mode, InchI, CID, KEGG, linkCHEBI,linkMetlin, nameTrivial, formula, precursorMZ, precursorType, isPositive);
	}	
	
	public int getCollisionEnergy(){
		return collisionEnergy;
	}
	

	
	
	public double getTic(){
		return tic;
	}
	
	public int getMode(){
		return mode;
	}
	
	public double getExactMass(){
		return exactMass;
	}

	public Vector<Peak> getPeaks(){
	 return peaks;
	}
	
	public String getInchi(){
		return InchI;
	}
	
	public int getCID(){
		return CID;
	}
	
	public String getTrivialName(){
		return nameTrivial;
	}
	
	public String getKEGG(){
		//fix space after C-Number
		String temp = KEGG.split("\\ ")[0];
		return temp;
	}
	
	public double getPrecursorMZ()
	{
		return precursorMZ;
	}
	
	public void setCollisionEnergy(int collisionEnergy){
		this.collisionEnergy = collisionEnergy;
	}
	

	
	public void setTic(double tic){
		this.tic = tic;
	}
	
	public void setPeaks(Vector<Peak> peaks){
		this.peaks = peaks;
	}
	
	public int compareTo(Spectrum another){
		if(this.collisionEnergy==0.0 || another.collisionEnergy==0.0)
		{
			throw new NumberFormatException("For at least one spectra there is no collision energy stored or at least one spectra is a ramp spectra."); 
		}
		else
		{
			return (collisionEnergy - another.collisionEnergy);
			
		}
	}
	
	/* TODO: Fix method ... IntensityComparator??? */
	public void calculateSNR(){
		//Collections.sort(peaks, new IntensityComparator());
		Collections.sort(peaks);
		double number = peaks.size(), i = 1.0;
		double prob = 1.0;
		
		for (Peak peak : peaks){
			//prob = lambda*Math.exp(-1*lambda*i/number);
			peak.setRelIntensity(i/number);
			++i;
		}
	}
	
	public void strictFilter(double threshold){
		for (Iterator<Peak> pIter = peaks.iterator(); pIter.hasNext();){
			if (pIter.next().getRelIntensityDB() < threshold){
				pIter.remove();
			}
		}
	}
	
	public void calculateRaw(){
		if (tic != 0.0) {
			double sum = 0.0;
			for (Peak peak : peaks){
				sum += peak.getIntensity();
			}		
			for (Peak peak : peaks){
				peak.setRelIntensity(tic*peak.getIntensity()/sum);
			}
		}
	}
	
	public void calcRelIntensities(){
		double totalIntensity = 0.0;
		for (int i=0; i<peaks.size(); ++i){
			totalIntensity += peaks.get(i).getIntensity();
		}
		for (int i=0; i<peaks.size(); ++i){
			peaks.get(i).setRelIntensity(peaks.get(i).getIntensity()*100.0/totalIntensity);
		}
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public String getFormula() {
		return formula;
	}

	public void setPrecursorType(String precursorType) {
		this.precursorType = precursorType;
	}

	public String getPrecursorType() {
		return precursorType;
	}

	public void setPositive(boolean isPositive) {
		this.isPositive = isPositive;
	}

	/**
	 * Checks if the mode is positive. (Needed if mode = 0)
	 *
	 * @return true, if is positive
	 */
	public boolean isPositive() {
		return isPositive;
	}

	public void setCHEBI(String cHEBI) {
		CHEBI = cHEBI;
	}

	public String getCHEBI() {
		return CHEBI;
	}
	
	public void setMetlin(String metlin) {
		this.metlin = metlin;
	}

	public String getMetlin() {
		return metlin;
	}
	
	public String getKnapsack() {
		return knapsack;
	}
	
	public String getSmiles(){
		return smiles;
	}
	
	public String getDatabaseID(DatabaseIDs id)
	{
		if(dblinks.containsKey(id))
		{
			return dblinks.get(id);
		}
		else
		{
			return null;
		}
	}
	
	public void show()
	{
		System.out.println("coll energy "+collisionEnergy);
	
		System.out.println("exactMass " + exactMass);
		System.out.println("mode " + mode);
		System.out.println("InchI " +InchI);
		System.out.println("CID " + CID);
		System.out.println("KEGG " + KEGG);
		System.out.println("linkCHEBI " + CHEBI);
		System.out.println("nameTrivial " + nameTrivial);
		System.out.println("formula " + formula);
		System.out.println("precursorMZ " + precursorMZ);
		System.out.println("precursorType " + precursorType);
		System.out.println("isPositive " + isPositive);
	}
}
