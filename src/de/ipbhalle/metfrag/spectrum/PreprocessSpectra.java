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
	private void Preprocess(String folder, double threshold)
	{
		//loop over all files in folder
		File f = new File(folder);
		File files[] = f.listFiles();
		Arrays.sort(files);
		
		//create new folder
		String path = folder + "Proccessed";
		new File(path).mkdir();
		
		int temp = 0;
		for(int i=0;i<files.length-1;i++)
		{
			if(files[i].isFile())
			{
				WrapperSpectrum spectrum = new WrapperSpectrum(files[i].toString());
				temp = i;
				
				Vector<WrapperSpectrum> spectra = new Vector<WrapperSpectrum>();
				spectra.add(spectrum);
				//next file
				WrapperSpectrum spectrumTemp = new WrapperSpectrum(files[i+1].toString());
				//same InchI
				int j = 2;
				String mergedNames = files[i].getName().split("\\.")[0];
				while(spectrum.getInchI().compareTo(spectrumTemp.getInchI()) == 0 && (i+j) <= files.length)
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
				Vector<Double> mergedPeaks = mergePeaks(spectra, threshold);
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
	
	
	private Vector<Double> mergePeaks(Vector<WrapperSpectrum> spectra, double threshold)
	{
		Vector<Double> newPeakList = new Vector<Double>();
		newIntensity = new Vector<Double>();
		newRelIntensity = new Vector<Double>();
		
		Vector<Double> peaks = new Vector<Double>();
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

		for (int i = 1; i < peakArray.length; i++) {
			//found peak within a given threshold
			if((currentPeak >= (peakArray[i] - threshold)) && (currentPeak <= (peakArray[i] + threshold)))
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
		String folder = "/home/swolf/MassBankData/HillPaper/Paper_0.01/";
		PreprocessSpectra pps = new PreprocessSpectra();
		pps.Preprocess(folder, 0.01);
		System.out.println("Done!");
	}

}
