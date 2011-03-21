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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;


import de.ipbhalle.metfrag.massbankParser.Peak;
import de.ipbhalle.metfrag.tools.PPMTool;




public class PreprocessSpectra {
	
	private HashMap<Double, Vector<Double>> peaksIntensity = null;
	private HashMap<Double, Vector<Double>> peaksRelIntensity = null;
	private Vector<Double> newIntensity = null;
	private Vector<Double> newRelIntensity = null;

	
	
	/**
	 * Preprocess the spectra in a given folder and merge those with a different
	 * collision energy (from the same compound). All results are saved!!!!
	 * 
	 * @param folder the folder
	 */
	private void preprocessUnsorted(String folder, double mzabs, double mzppm)
	{
		//loop over all files in folder
		File f = new File(folder);
		File files[] = f.listFiles();
		Arrays.sort(files);
		
		//create new folder
		String path = folder + "Merged";
		new File(path).mkdir();
		
		int temp = 0;
		
		
		Map<String, List<File>> pubchemToFiles = new HashMap<String, List<File>>();
		for(int i=0; i < files.length; i++)
		{
			if(files[i].isFile())
			{
				WrapperSpectrum spectrum = new WrapperSpectrum(files[i].toString());
				if(pubchemToFiles.containsKey(Integer.toString(spectrum.getCID())))
					pubchemToFiles.get(Integer.toString(spectrum.getCID())).add(files[i]);
				else
				{
					List<File> fileList = new ArrayList<File>();
					fileList.add(files[i]);
					pubchemToFiles.put(Integer.toString(spectrum.getCID()), fileList);
				}
				
				System.out.println(files[i].toString() + spectrum.getCID());
			}
		}
		
		
		for (String pubchemID : pubchemToFiles.keySet()) {
			String mergedNames = "";
			Vector<WrapperSpectrum> spectra = new Vector<WrapperSpectrum>();
			String lastFile = "";
			
			for (File file : pubchemToFiles.get(pubchemID)) {

				WrapperSpectrum spectrum = new WrapperSpectrum(file.toString());
				
				//no pubchem id given ... skip this spectrum!
				if(spectrum.getCID() == 0)
					continue;
				
				spectra.add(spectrum);
				mergedNames += file.getName().split("\\.")[0];
				
				lastFile = file.toString();
			}

			//now merge the peaks from the different collision energies
			Vector<Double> mergedPeaks = mergePeaks(spectra, mzabs, mzppm);
			//now write back those peaks into a new file
			String line = "";
			try 
			{
				BufferedReader reader = new BufferedReader(new FileReader(lastFile));
			  	line += reader.readLine() + "\n";
			  	while (line != null && !line.contains("PK$NUM_PEAK:")){
			  		String currentLine = reader.readLine();
			  		if(currentLine.contains("PK$NUM_PEAK:"))
			  			break;
			  		else
			  			line += currentLine + "\n";
			  	}
			  	line += "PK$NUM_PEAK: " + mergedPeaks.size() + "\n";
			  	line += "PK$PEAK: m/z int. rel.int.\n";
			  	for (int k = 0; k < mergedPeaks.size(); k++) {
					//those intensities should be in sync with the merged peaks....quick and dirty...
			  		line += "  " + mergedPeaks.get(k) + " " + this.newIntensity.get(k) + " " + this.newRelIntensity.get(k) + "\n";
				}
			  	line += "//";
			  	//write to file
		  	    File outFile = new File(path + "/" + mergedNames + ".txt");
		  	    System.out.println(mergedNames);
	            FileWriter out = new FileWriter(outFile);
	            out.write(line);
	            out.close();
			}
			catch(FileNotFoundException e)
			{
				System.err.println("File not found: " + e.getMessage());
			}
			catch(IOException e)  
			{
				System.err.println("Error: " + e.getMessage());
			}				
		}
	}
	
	
	
	
	/**
	 * Preprocess the spectra in a given folder and merge those with a different
	 * collision energy (from the same compound). All results are saved!!!!
	 * 
	 * @param folder the folder
	 */
	private void preprocess(String folder, double threshold)
	{
		//loop over all files in folder
		File f = new File(folder);
		File files[] = f.listFiles();
		Arrays.sort(files);
		
		//create new folder
		String path = folder + "Merged";
		new File(path).mkdir();
		
		int temp = 0;
		for(int i=0;i < (files.length - 1);i++)
		{
			if(files[i].isFile())
			{
				WrapperSpectrum spectrum = new WrapperSpectrum(files[i].toString());
				
				//no pubchem id given ... skip this spectrum!
				if(spectrum.getCID() == 0)
					continue;
				
				temp = i;
				
				Vector<WrapperSpectrum> spectra = new Vector<WrapperSpectrum>();
				spectra.add(spectrum);
				//next file
				WrapperSpectrum spectrumTemp = new WrapperSpectrum(files[i+1].toString());
				//same InchI
				int j = 2;
				String mergedNames = files[i].getName().split("\\.")[0];
				while(spectrum.getInchI().compareTo(spectrumTemp.getInchI()) == 0 && (i+j) <= files.length)
//				while(spectrum.getTrivialName().equals(spectrumTemp.getTrivialName()) && (i+j) <= files.length)
				{
					//same InchI --> add to list
					spectra.add(spectrumTemp);
					mergedNames += files[i+(j-1)].getName().split("\\.")[0];
					//fix for last file
					if((i+j) < files.length)
						spectrumTemp = new WrapperSpectrum(files[i+j].toString());
					j++;
				}
				i=i+(j-2);
				
				//now merge the peaks from the different collision energies
				Vector<Double> mergedPeaks = mergePeaks(spectra, threshold, 0.0);
				//now write back those peaks into a new file
				String line = "";
				try 
				{
					BufferedReader reader = new BufferedReader(new FileReader(files[temp].toString()));
				  	line += reader.readLine() + "\n";
				  	while (line != null && !line.contains("AC$ANALYTICAL_CONDITION: COLLISION_ENERGY")){
				  	  line += reader.readLine() + "\n";
				  	}
				  	line += "PK$NUM_PEAK: " + mergedPeaks.size() + "\n";
				  	line += "PK$PEAK: m/z int. rel.int.\n";
				  	for (int k = 0; k < mergedPeaks.size(); k++) {
						//those intensities should be in sync with the merged peaks....quick and dirty...
				  		line += "  " + mergedPeaks.get(k) + " " + this.newIntensity.get(k) + " " + this.newRelIntensity.get(k) + "\n";
					}
				  	//write to file
			  	    File outFile = new File(path + "/" + mergedNames + ".txt");
			  	    System.out.println(mergedNames);
		            FileWriter out = new FileWriter(outFile);
		            out.write(line);
		            out.close();
				}
				catch(FileNotFoundException e)
				{
					System.err.println("File not found: " + e.getMessage());
				}
				catch(IOException e)  
				{
					System.err.println("Error: " + e.getMessage());
				}
				
			}
		}
	}
	
	
	/**
	 * Merge peaks with a given mzabs and mzppm.
	 *
	 * @param spectra the spectra
	 * @param mzabs the mzabs
	 * @param mzppm the mzppm
	 * @return the vector
	 */
	private Vector<Double> mergePeaks(Vector<WrapperSpectrum> spectra, double mzabs, double mzppm)
	{
		Vector<Double> newPeakList = new Vector<Double>();
		newIntensity = new Vector<Double>();
		newRelIntensity = new Vector<Double>();
		
		this.peaksIntensity = new HashMap<Double, Vector<Double>>();
		this.peaksRelIntensity = new HashMap<Double, Vector<Double>>();
		for (int i = 0; i < spectra.size(); i++) {
			Vector<Peak> tempPeaks = spectra.get(i).getPeakList();
			for (int j = 0; j < tempPeaks.size(); j++) {
				//peaks.add(tempPeaks.get(j).getMass());
				peaksIntensity = addToMap(peaksIntensity, tempPeaks.get(j).getMass(), tempPeaks.get(j).getIntensity());
				peaksRelIntensity = addToMap(peaksRelIntensity, tempPeaks.get(j).getMass(),tempPeaks.get(j).getRelIntensity());
			}
		}

		Double[] peakArray = new Double[peaksIntensity.size()];
		peakArray = peaksIntensity.keySet().toArray(peakArray);
		//sort peak list
		Arrays.sort(peakArray);
		
		//now merge adjacent peaks within a given threshold
		double currentPeak = peakArray[0];
		double currentIntensity = 0.0;
		double currentRelIntensity = 0.0;
		Vector<Double> temp = new Vector<Double>();
		temp.add(currentPeak);

		for (int i = 0; i < peakArray.length; i++) {
			//found peak within a given threshold
			double peakdeviation = PPMTool.getPPMDeviation(peakArray[i], mzppm) + mzabs;
			if((currentPeak >= (peakArray[i] - peakdeviation)) && (currentPeak <= (peakArray[i] + peakdeviation)))
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
					for (int j = 0; j < temp.size(); j++) {
						newPeak += temp.get(j);
					}
					newPeak = Math.round(newPeak/temp.size() *1000)/1000.0;
					newPeakList.add(newPeak);					
					
					//add the corresponding intensities too
					newIntensity.add(currentIntensity);
					newRelIntensity.add(currentRelIntensity);
					
					temp = new Vector<Double>();
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
				for (int j = 0; j < temp.size(); j++) {
					newPeak += temp.get(j);
				}
				newPeak = Math.round(newPeak/temp.size() *1000)/1000.0;
				newPeakList.add(newPeak);
				
				//add the corresponding intensities too
				newIntensity.add(currentIntensity);
				newRelIntensity.add(currentRelIntensity);
				
				
				temp = new Vector<Double>();
				currentPeak = peakArray[i];
				currentIntensity = 0.0;
				currentRelIntensity = 0.0;
				temp.add(currentPeak);
				
				//get the last peak too
				if(i == peakArray.length - 1)
				{
					newPeakList.add(peakArray[i]);
					//add the corresponding intensities too
					//gets the max intensity from the same peaks
					int size = peaksIntensity.get(peakArray[i]).size();
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
				temp = new Vector<Double>();
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
	private HashMap<Double, Vector<Double>> addToMap(HashMap<Double, Vector<Double>> map, double currentPeak, double intensity)
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
//		String folder = "/home/swolf/MassBankData/MetFragSunGrid/BrukerRawData/Processed/";
		String folder = "/home/swolf/MassBankData/TestSpectra/MM48/";
//		String folder = "/home/swolf/test/";
		PreprocessSpectra pps = new PreprocessSpectra();
		pps.preprocessUnsorted(folder, 0.002, 10);
//		pps.Preprocess(folder, 0.01);
		System.out.println("Done!");
	}

}
