package de.ipbhalle.metfrag.resultsparser;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultsParser {
	
	private static Map<String, Map<Double, Integer>> fileToMapFound = new HashMap<String, Map<Double,Integer>>();
	private static Map<String, Map<Double, Integer>> fileToMapAll = new HashMap<String, Map<Double,Integer>>();
	private static Map<String, Integer> fileToCandidateCount = new HashMap<String, Integer>();
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		try{

			//candidate count
//		    FileInputStream fstream = new FileInputStream("/home/swolf/MassBankData/MetFragSunGrid/HillPaperDataMerged/logs/2010-02-01_18-54-02_histCompare.txt");
		    FileInputStream fstream = new FileInputStream("/home/swolf/MassBankData/MetFragSunGrid/HillPaperDataMerged/logs/2010-02-01_09-31-41_histCompare.txt");
		    // Get the object of DataInputStream
		    DataInputStream in = new DataInputStream(fstream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    String strLine;

		    //Read File Line By Line
		    while ((strLine = br.readLine()) != null)   {
		    	// Print the content on the console
		    	String[] lineArr = strLine.split("\t");
		    	if(lineArr.length > 1)
		    		fileToCandidateCount.put(lineArr[0], Integer.parseInt(lineArr[2]));
		    }

		    //Close the input stream
		    in.close();
		    }catch (Exception e){//Catch exception if any
		      System.err.println("Error: " + e.getMessage() + "\n");
		      e.printStackTrace();
		    }
		
		try{

			//FOUND PEAKS
//		    FileInputStream fstream = new FileInputStream("/home/swolf/MassBankData/MetFragSunGrid/HillPaperDataMerged/logs/2010-02-01_18-54-02_FoundPeaks.txt");
		    FileInputStream fstream = new FileInputStream("/home/swolf/MassBankData/MetFragSunGrid/HillPaperDataMerged/logs/2010-02-03_18-25-28_FoundPeaks_modified.txt");
		    // Get the object of DataInputStream
		    DataInputStream in = new DataInputStream(fstream);
		        BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    String strLine;
		    boolean nextFile = false;
		    boolean readName = false;
		    
		    String file = "";
		    Map<Double, Integer> peakToCountFound = new HashMap<Double, Integer>();
		    

		    //Read File Line By Line
		    while ((strLine = br.readLine()) != null)   {
		    	// Print the content on the console
		    	if(strLine.equals("//"))
		    		nextFile = true;
		    	
		    	if(readName)
		    	{
		    		file += strLine;
		    		readName = false;
		    	}
		    	else if(!nextFile)
		    	{
		    		Double peak = Double.parseDouble(strLine);
		    		Integer count = peakToCountFound.get(peak);
		    		if(count != null)
		    		{
		    			count++;
		    			peakToCountFound.put(peak, count);
		    		}
		    		else
		    		{
		    			peakToCountFound.put(peak, 1);
		    		}
		    	}
		    	
		    	
		    	
		    	if(nextFile)
		    	{
		    		
		    		addFound(peakToCountFound, file);
		    		peakToCountFound = new HashMap<Double, Integer>();
		    		file = "";
		    		nextFile = false;
		    		readName = true;
		    	}
		    }
		    //also add the data from the last file
		    addFound(peakToCountFound, file);
//		    System.out.println(addFound(peakToCountFound, file));
		    
		    //Close the input stream
		    in.close();
		    }catch (Exception e){//Catch exception if any
		      System.err.println("Error: " + e.getMessage() + "\n");
		      e.printStackTrace();
		    }
		    
		    
		    
		    
		    try{
			    
				//all peaks excluded
//		    	FileInputStream fstream = new FileInputStream("/home/swolf/MassBankData/MetFragSunGrid/HillPaperDataMerged/logs/2010-02-01_18-54-02_AllPeaksInclusive.txt");
			    FileInputStream fstream = new FileInputStream("/home/swolf/MassBankData/MetFragSunGrid/HillPaperDataMerged/logs/2010-02-03_18-25-28_AllPeaksInclusive_modified.txt");
			    // Get the object of DataInputStream
			    DataInputStream in = new DataInputStream(fstream);
			    BufferedReader br = new BufferedReader(new InputStreamReader(in));
			    String strLine;
			    boolean nextFile = false;
			    boolean readName = false;
			    
			    String file = "";
			    Map<Double, Integer> peakToCountAll = new HashMap<Double, Integer>();
			    

			    //Read File Line By Line
			    while ((strLine = br.readLine()) != null)   {
			    	// Print the content on the console
			    	if(strLine.equals("//"))
			    		nextFile = true;
			    	
			    	if(readName)
			    	{
			    		file += strLine;
			    		readName = false;
			    	}
			    	else if(!nextFile)
			    	{
			    		Double peak = Double.parseDouble(strLine);
			    		Integer count = peakToCountAll.get(peak);
			    		if(count != null)
			    		{
			    			count++;
			    			peakToCountAll.put(peak, count);
			    		}
			    		else
			    		{
			    			peakToCountAll.put(peak, 1);
			    		}
			    	}
			    	
			    	
			    	
			    	if(nextFile)
			    	{
			    		
			    		addAll(peakToCountAll, file);
			    		peakToCountAll = new HashMap<Double, Integer>();
			    		file = "";
			    		nextFile = false;
			    		readName = true;
			    	}
			    }
			    //also add the data from the last file
			    addAll(peakToCountAll, file);
			    //System.out.println(addAll(peakToCountAll, file));
			    
			    //Close the input stream
			    in.close();
			    }catch (Exception e){//Catch exception if any
			      System.err.println("Error: " + e.getMessage() + "\n");
			      e.printStackTrace();
			    }
			    
			    
			    
			    //now match found peaks and exclusive peaks
			    for (String file : fileToMapAll.keySet()) {
			    	if(file == "")
			    		continue;
			    	Map<Double, Integer> mapPeakToCountAll = fileToMapAll.get(file);
			    	Map<Double, Integer> mapPeakToCountFound = fileToMapFound.get(file);
			    	System.out.print(file + "\t" + fileToCandidateCount.get(file));
			    	int peakExplained = 0;
			    	int peakExplainedCount = 0;
			    	for (Double peak : mapPeakToCountAll.keySet()) {
//						System.out.print("\n" + peak + ": " + mapPeakToCountAll.get(peak) + "\t");
						if(mapPeakToCountFound.get(peak) != null)
						{
							peakExplained += (mapPeakToCountAll.get(peak) - 1);
							peakExplainedCount++;
						}						
					}
			    	double PPV = ((double)peakExplainedCount / ((double)peakExplainedCount + (double)peakExplained));
			    	//double candidateCount = (double)fileToCandidateCount.get(file);
			    	System.out.print("\t" + Math.round(PPV * 10000.0) / 10000.0 + "\n");
				}
	}
	
	
	private static String addFound(Map<Double, Integer> peakToCount, String file)
	{
		String out = file + "\n";
		for (Double peak : peakToCount.keySet()) {
			out += peak + " " + peakToCount.get(peak) + "\n";
		}
		
		fileToMapFound.put(file, peakToCount);
		
		return out + "\n\n\n";
	}
	
	private static String addAll(Map<Double, Integer> peakToCount, String file)
	{
		String out = file + "\n";
		for (Double peak : peakToCount.keySet()) {
			out += peak + " " + peakToCount.get(peak) + "\n";
		}
		
		fileToMapAll.put(file, peakToCount);
		
		return out + "\n\n\n";
	}

}
