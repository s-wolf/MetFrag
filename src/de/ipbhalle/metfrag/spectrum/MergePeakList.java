package de.ipbhalle.metfrag.spectrum;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import de.ipbhalle.metfrag.massbankParser.Peak;

public class MergePeakList {
	
	private static HashMap<Double, Vector<Double>> peaksIntensity = null;
	private static HashMap<Double, Vector<Double>> peaksRelIntensity = null;
	private static Vector<Double> newIntensity = null;
	private static Vector<Double> newRelIntensity = null;
	
	/**
	 * Adds a new peaks with its intensity to the map.
	 * 
	 * @param map the map
	 * @param currentPeak the current peak
	 * @param intensity the intensity
	 */
	private static HashMap<Double, Vector<Double>> addToMap(HashMap<Double, Vector<Double>> map, double currentPeak, double intensity)
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
	
	public static Vector<Double> mergePeaks(Vector<WrapperSpectrum> spectra, double threshold)
	{
		Vector<Double> newPeakList = new Vector<Double>();
		newIntensity = new Vector<Double>();
		newRelIntensity = new Vector<Double>();
		
		Vector<Double> peaks = new Vector<Double>();
		peaksIntensity = new HashMap<Double, Vector<Double>>();
		peaksRelIntensity = new HashMap<Double, Vector<Double>>();
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
	
	
	public static void main(String[] args) {
		
		try
		{
			if(args == null)
				System.exit(1);
				
			String file = args[0];
			String folder = args[1];
			String mergePPM = args[2];
			
			//loop over all files in folder
			File f = new File(folder);
			File files[] = f.listFiles();
			Arrays.sort(files);
			
			//create new folder
			String path = folder + "Merged";
			new File(path).mkdir();

			FileInputStream fstream = new FileInputStream(file);
		    DataInputStream in = new DataInputStream(fstream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    String strLine;
		    Map<String, List<String>> nameToFile = new HashMap<String, List<String>>();
		    //Read File Line By Line
		    while ((strLine = br.readLine()) != null)   {
		    	String[] split = strLine.split(", ");
		    	if(nameToFile.containsKey(split[1].trim()))
		    		nameToFile.get(split[1].trim()).add(split[0].trim());
		    	else
		    	{
		    		List<String> temp = new ArrayList<String>();
		    		temp.add(split[0].trim());
		    		nameToFile.put(split[1].trim(), temp);
		    	}
		    }
		    in.close();
		    
		    for (String name : nameToFile.keySet()) {
		    	List<String> fileNames = nameToFile.get(name);
		    	
		    	Vector<WrapperSpectrum> spectra = new Vector<WrapperSpectrum>();
		    	String fileNameMerged = name;
		    	for (String string : fileNames) {
//		    		fileNameMerged += string;
		    		FileInputStream fstreamFile = new FileInputStream(folder + string + ".csv");
				    DataInputStream inFile = new DataInputStream(fstreamFile);
				    BufferedReader brFile = new BufferedReader(new InputStreamReader(inFile));
				    String strLineFile;
				    boolean header = true;
				    String peaks = "";
				    //Read File Line By Line
				    while ((strLineFile = brFile.readLine()) != null)   {
				    	if(header)
				    	{
				    		header = false;
				    		continue;
				    	}
				    	peaks += strLineFile.replace(',', '\t');
				    	peaks += "\n";
				    }
				    in.close();
				    WrapperSpectrum spectrum = new WrapperSpectrum(peaks, 1, 1000.0);
				    spectra.add(spectrum);
				}
		    	
		    	Vector<Double> mergedPeaks = mergePeaks(spectra, Double.parseDouble(mergePPM));
		    	
		    	FileWriter fw = new FileWriter(new File(folder + "Merged/" + fileNameMerged + ".csv"), true);
		    	fw.write("\"mz\",\"into\",\"intrel\"\n");
		    	for (int k = 0; k < mergedPeaks.size(); k++) {
					fw.write(mergedPeaks.get(k) + "," + newIntensity.get(k) + "," + newRelIntensity.get(k) + "\n");
				}
		    	fw.close();
			}

		}
		catch(IOException e)
		{
			System.err.println("File not found! " + e.getMessage());
		}
	}

}
