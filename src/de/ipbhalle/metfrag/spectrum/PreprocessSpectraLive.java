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
package de.ipbhalle.metfrag.spectrum;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;


import de.ipbhalle.metfrag.massbankParser.Peak;
import de.ipbhalle.metfrag.tools.PPMTool;


public class PreprocessSpectraLive {
	
	private HashMap<Peak, Vector<Double>> peaksIntensity = null;
	private HashMap<Peak, Vector<Double>> peaksRelIntensity = null;
	private HashMap<String, WrapperSpectrum> inchiModeToSpectra = null; 
	private Vector<Double> newIntensity = null;
	private Vector<Double> newRelIntensity = null;
	private String filename = "";
	
	/**
	 * Creates a new object and reads in all spectra from a given folder. Each spectra is merged
	 * and their peaks are merged, as well. This merging is based on the mzabs and mzppm values.
	 * If two or more peaks are in this given range they are merged and the average m/z value is used.
	 * The maximum intensity of all merged peaks is set as the intensity.
	 * 
	 * 
	 * @param folder the folder
	 * @param mzabs the mzabs
	 * @param mzppm the mzppm
	 */
	public PreprocessSpectraLive(String folder, double mzabs, double mzppm)
	{
		this.inchiModeToSpectra = new HashMap<String, WrapperSpectrum>();
		Preprocess(folder, mzabs, mzppm);
	}
	
	/**
	 * Creates a new object and reads in all spectra from a given folder (But only spectra with the 
	 * given file name are merged). Each spectra is merged
	 * and their peaks are merged, as well. This merging is based on the mzabs and mzppm values.
	 * If two or more peaks are in this given range they are merged and the average m/z value is used.
	 * The maximum intensity of all merged peaks is set as the intensity.
	 * 
	 * 
	 * @param folder the folder
	 * @param mzabs the mzabs
	 * @param mzppm the mzppm
	 */
	public PreprocessSpectraLive(String folder, double mzabs, double mzppm, String filename)
	{
		this.inchiModeToSpectra = new HashMap<String, WrapperSpectrum>();
		this.filename = filename;
		Preprocess(folder, mzabs, mzppm);
	}
	
	/**
	 * Gets all the merged spectra from the previous specified folder.
	 * 
	 * @return the merged spectra
	 */
	public Vector<WrapperSpectrum> getMergedSpectra()
	{
		Vector<WrapperSpectrum> ret = new Vector<WrapperSpectrum>();
		String[] keysArray = new String[this.inchiModeToSpectra.size()];
		keysArray = this.inchiModeToSpectra.keySet().toArray(keysArray);
		Arrays.sort(keysArray);
		for (String string : keysArray) {
			ret.add(this.inchiModeToSpectra.get(string));
		}
		return ret;
	}
	
	private void Preprocess(String folder, double mzabs, double mzppm)
	{
		//loop over all files in folder
		File f = new File(folder);
		File files[] = f.listFiles();
		Arrays.sort(files);
		//remove the folders feom the array
		Vector<File> onlyFiles = new Vector<File>();
		
		
		for (File file : files) {
			if(file.isFile())
				onlyFiles.add(file);
		}
		
		
		int temp = 0;
		
		if(onlyFiles.size() == 1)
		{
			WrapperSpectrum spectrum = new WrapperSpectrum(onlyFiles.get(0).toString());
			spectrum.setFilename(onlyFiles.get(0).getName());
			this.inchiModeToSpectra.put(onlyFiles.get(0).getName().split("\\.")[0], spectrum);
		}
		//TODO: bug in last file!!!!!!!!!!
		for(int i=0; i<onlyFiles.size()-1; i++)
		{
			//check if only one file is being processed..if so find the first occurence
			if(!this.filename.equals(onlyFiles.get(i).getName().split("\\.")[0]) && !this.filename.equals(""))
				continue;
			
			WrapperSpectrum spectrum = new WrapperSpectrum(onlyFiles.get(i).toString());
			temp = i;
			
			Vector<WrapperSpectrum> spectra = new Vector<WrapperSpectrum>();
			spectra.add(spectrum);
			//next file
			WrapperSpectrum spectrumTemp = new WrapperSpectrum(onlyFiles.get(i+1).toString());
			//same InchI
			int j = 2;
			String mergedNames = onlyFiles.get(i).getName().split("\\.")[0];
			while(spectrum.getInchI().compareTo(spectrumTemp.getInchI()) == 0 &&  spectrum.getMode() == spectrumTemp.getMode() && (i+j) <= files.length)
			{
				//same InchI and mode --> add to list
				spectra.add(spectrumTemp);
				if((i+(j-1)) < onlyFiles.size())
					mergedNames += onlyFiles.get(i+(j-1)).getName().split("\\.")[0];
				//fix for last file
				if((i+j) < onlyFiles.size())
					spectrumTemp = new WrapperSpectrum(onlyFiles.get(i+j).toString());
				j++;
			}
			i=i+(j-2);
			
			//now merge the peaks from the different collision energies
			Vector<Peak> mergedPeaks = mergePeaks(spectra, mzabs, mzppm);
			//set the new merged peak data
			WrapperSpectrum mergedSpectrum = spectra.get(0);
			mergedSpectrum.setPeakList(mergedPeaks);
			mergedSpectrum.setFilename(mergedNames);
			
			String tempfile = mergedNames + "_" + spectrum.getMode() + ".txt";
			inchiModeToSpectra.put(tempfile, mergedSpectrum);				
			
		}
	}
	
	
	private Vector<Peak> mergePeaks(Vector<WrapperSpectrum> spectra, double mzabs, double mzppm)
	{
		Vector<Peak> newPeakList = new Vector<Peak>();
		newIntensity = new Vector<Double>();
		newRelIntensity = new Vector<Double>();
		
		Vector<Peak> peaks = new Vector<Peak>();
		this.peaksIntensity = new HashMap<Peak, Vector<Double>>();
		this.peaksRelIntensity = new HashMap<Peak, Vector<Double>>();
		for (int i = 0; i < spectra.size(); i++) {
			Vector<Peak> tempPeaks = spectra.get(i).getPeakList();
			for (int j = 0; j < tempPeaks.size(); j++) {
				//peaks.add(tempPeaks.get(j).getMass());
				peaksIntensity = addToMap(peaksIntensity, tempPeaks.get(j), tempPeaks.get(j).getIntensity());
				peaksRelIntensity = addToMap(peaksRelIntensity, tempPeaks.get(j),tempPeaks.get(j).getRelIntensity());
			}
		}

		Peak[] peakArray = new Peak[peaksIntensity.size()];
		peakArray = peaksIntensity.keySet().toArray(peakArray);
		//sort peak list
		Arrays.sort(peakArray);
		
		//now merge adjacent peaks within a given threshold
		Peak currentPeak = peakArray[0];
		double currentIntensity = 0.0;
		double currentRelIntensity = 0.0;
		Vector<Peak> temp = new Vector<Peak>();
		temp.add(currentPeak);

		for (int i = 1; i < peakArray.length; i++) {
			//found peak within a given threshold
			if((currentPeak.getMass() >= (peakArray[i].getMass() - (mzabs + PPMTool.getPPMDeviation(currentPeak.getMass(), mzppm)))) && (currentPeak.getMass() <= (peakArray[i].getMass() + (mzabs + PPMTool.getPPMDeviation(currentPeak.getMass(), mzppm)))))
			{
				temp.add(peakArray[i]);
				//gets the max intensity from the same peaks
				for (int k = 0; k < temp.size(); k++) {
					int size = peaksIntensity.get(temp.get(k)).size();
					for (int j = 0; j < size; j++) {
						if(currentIntensity < peaksIntensity.get(temp.get(k)).get(j))
							currentIntensity = peaksIntensity.get(temp.get(k)).get(j);
					}
					for (int j = 0; j < size; j++) {
						if(currentRelIntensity < peaksRelIntensity.get(temp.get(k)).get(j))
							currentRelIntensity = peaksRelIntensity.get(temp.get(k)).get(j);
					}
				}
				//get the last peak too
				if(temp.size() > 1 && i == peakArray.length - 1)
				{
					double newPeak = 0;
					int lowestCollisionEnergy = Integer.MAX_VALUE;
					
					for (int j = 0; j < temp.size(); j++) {
						newPeak += temp.get(j).getMass();
						
						if(lowestCollisionEnergy > temp.get(j).getLowestEnergy())
							lowestCollisionEnergy = temp.get(j).getLowestEnergy();
					}
					newPeak = Math.round(newPeak/temp.size() *1000)/1000.0;
					newPeakList.add(new Peak(newPeak, currentIntensity, currentRelIntensity, lowestCollisionEnergy));					
					
					//add the corresponding intensities too
					newIntensity.add(currentIntensity);
					newRelIntensity.add(currentRelIntensity);
					
					temp = new Vector<Peak>();
					currentPeak = peakArray[i];
					currentIntensity = 0.0;
					currentRelIntensity = 0.0;
					temp.add(currentPeak);
				}
				
			}
			//merge found peaks
			else if(temp.size() > 1)
			{
				double newPeak = 0;
				int lowestCollisionEnergy = Integer.MAX_VALUE;
				for (int j = 0; j < temp.size(); j++) {
					newPeak += temp.get(j).getMass();
					if(lowestCollisionEnergy > temp.get(j).getLowestEnergy())
						lowestCollisionEnergy = temp.get(j).getLowestEnergy();
				}
				newPeak = Math.round(newPeak/temp.size() *1000)/1000.0;
				newPeakList.add(new Peak(newPeak, currentIntensity, currentRelIntensity, lowestCollisionEnergy));
				
				//add the corresponding intensities too
				newIntensity.add(currentIntensity);
				newRelIntensity.add(currentRelIntensity);
				
				
				temp = new Vector<Peak>();
				currentPeak = peakArray[i];
				currentIntensity = 0.0;
				currentRelIntensity = 0.0;
				temp.add(currentPeak);
			}
			//no peaks to merge
			else
			{
				newPeakList.add(peakArray[i-1]);
				
				//gets the max intensity from the same peaks
				int size = peaksIntensity.get(currentPeak).size();
				for (int j = 0; j < size; j++) {
					if(currentIntensity < peaksIntensity.get(currentPeak).get(j))
						currentIntensity = peaksIntensity.get(currentPeak).get(j);
				}
				for (int j = 0; j < size; j++) {
					if(currentRelIntensity < peaksRelIntensity.get(currentPeak).get(j))
						currentRelIntensity = peaksRelIntensity.get(currentPeak).get(j);
				}
				
				//add the corresponding intensities too
				newIntensity.add(currentIntensity);
				newRelIntensity.add(currentRelIntensity);
				
				//get the last peak too
				if(i == peakArray.length - 1)
				{
					newPeakList.add(peakArray[i]);
					//add the corresponding intensities too
					//gets the max intensity from the same peaks
					size = peaksIntensity.get(peakArray[i]).size();
					for (int j = 0; j < size; j++) {
						if(currentIntensity < peaksIntensity.get(peakArray[i]).get(j))
							currentIntensity = peaksIntensity.get(peakArray[i]).get(j);
					}
					for (int j = 0; j < size; j++) {
						if(currentRelIntensity < peaksRelIntensity.get(peakArray[i]).get(j))
							currentRelIntensity = peaksRelIntensity.get(peakArray[i]).get(j);
					}
					newIntensity.add(currentIntensity);
					newRelIntensity.add(currentRelIntensity);
				}
				currentPeak = peakArray[i];
				currentIntensity = 0.0;
				currentRelIntensity = 0.0;
				temp = new Vector<Peak>();
				temp.add(currentPeak);
			}	
		}
		
		return newPeakList;
		
	}
	
	/**
	 * Adds a new peaks with its intensity to the map.
	 * 
	 * @param map the map
	 * @param currentPeak the current peak
	 * @param intensity the intensity
	 */
	private HashMap<Peak, Vector<Double>> addToMap(HashMap<Peak, Vector<Double>> map, Peak currentPeak, double intensity)
	{
    	//add sum formula molecule comb. to map
        if(map.containsKey(currentPeak))
        {
        	Vector<Double> tempList = map.get(currentPeak);
        	tempList.add(intensity);
        	map.put(currentPeak, tempList);
        }
        else
        {
        	Vector<Double> temp = new Vector<Double>();
        	temp.add(intensity);
        	map.put(currentPeak, temp);
        }
        
        return map;
	}
	
	
	public static void main(String[] args) {
		String folder = "/home/swolf/MassBankData/testDataSingle/";
		PreprocessSpectraLive pps = new PreprocessSpectraLive(folder, 0.01, 50);
		Vector<WrapperSpectrum> ms = pps.getMergedSpectra();
		
		String peaksString = "";
		for (WrapperSpectrum spectrumWrapper : ms) {
			CleanUpPeakList cList = new CleanUpPeakList(spectrumWrapper.getPeakList());
			Vector<Peak> cleanedPeakList = cList.getCleanedPeakList(spectrumWrapper.getExactMass());
			for (Peak peak : cleanedPeakList) {
				peaksString += peak.getMass() + "\n";
			}
		}
		System.out.println("Done!");
		
		try
		{
			File outFile = new File(folder + "logs/" + "AllPeaks_Cleaned.txt");
	        FileWriter out = new FileWriter(outFile);
	        out.write(peaksString);
	        out.close();
		}
		catch(IOException e)
		{
			System.err.println("IO ERROR");
		}
	}
}
