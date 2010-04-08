package de.ipbhalle.metfrag.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.xml.rpc.ServiceException;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;

import de.ipbhalle.metfrag.spectrum.WrapperSpectrum;


public class MainMetFragScriptable {
	
public static void main(String[] args) {
		
		Config c = null;
		String completeLog = "";
		String date = "";
		String currentFile = "";
		
		String histogramCompare = "";
		String histogram = "";
		String histogramReal = "";
		String histogramPeaks = "";
		String histogramPeaksCorresponding = "";
		String histogramPeaksAll = "";
		String similarityValues = "";
		
		try
		{
			//thats the current file
			if(args[0] != null)
			{
				currentFile = args[0];
			}
			else
			{
				System.err.println("Error! Parameter missing!");
				System.exit(1);
			}
			
			//thats the date for the log file
			if(args[1] != null)
			{
				date = args[1]; 
			}
			else
			{
				System.err.println("Error! Parameter missing!");
				System.exit(1);
			}
		}
		catch(Exception e)
		{
			System.err.println("Error! Parameter missing!");
			System.exit(1);
		}
			
		
		try {
			//just a hack to load the config file from outside
			c = new Config("test");
		} catch (IOException e1) {
			System.err.println("COULD NOT FIND CONFIG FILE! " + e1.getMessage());
			e1.printStackTrace();
			System.exit(0);
		}
				
		KEGGSearch ks = null;
		PubChemSearchParallel psParallel = null;


		//add comment to log file
		completeLog += c.getComment() + "\n\n\n"; 

		//timer
		long start = 0;
	    long end = 0;
	    long sum = 0;
	    
//	    PreprocessSpectraLive ppLive = new PreprocessSpectraLive(c.getFolder(), c.getMzabs(), c.getMzppm());
//	    Vector<WrapperSpectrum> mergedSpectra = ppLive.getMergedSpectra();
	    
	    WrapperSpectrum spectrum = new WrapperSpectrum(c.getFolder() + currentFile);
	    
    	try
		{		
			//create a buffered reader that connects to the console, we use it so we can read lines
            String path = c.getFolder() + spectrum.getFilename();
			new File(path).mkdir();
			//PrintWriter out = new PrintWriter(new FileWriter(folder + file + "/" + dateFormat.format(date) + "/ConsoleOutput.txt"));
			
			
			// initialize logging to go to rolling log file
	        LogManager logManager = LogManager.getLogManager();
	        logManager.reset();

	        // log file max size 10K, 3 rolling files, append-on-open
	        Handler fileHandler = new FileHandler(c.getFolder() + spectrum.getFilename() + "/log_" + date, 10000000, 1, true);
	        fileHandler.setFormatter(new SimpleFormatter());
	        Logger.getLogger("").addHandler(fileHandler);        

			// preserve old stdout/stderr streams in case they might be useful
			PrintStream stdout = System.out;
		
			// now rebind stdout/stderr to logger
			Logger logger;
			LoggingOutputStream los;

			logger = Logger.getLogger("stdout");
			los = new LoggingOutputStream(logger, StdOutErrLevel.STDOUT);
			System.setOut(new PrintStream(los, true));
		
			logger = Logger.getLogger("stderr");
			los= new LoggingOutputStream(logger, StdOutErrLevel.STDERR);
			System.setErr(new PrintStream(los, true));

	        
	      
	        //things to log BEGIN ================================================================
			MainMetFrag test = new MainMetFrag();
			
			//take time
			start = System.currentTimeMillis();
			//kegg or massbank proof of concept						
			if(c.isKEGG())
			{
				ks = new KEGGSearch(c.getFolder(), spectrum, c.getMzabs(), c.getMzppm(), c.isPdf(), c.isShowDiagrams(), c.isRecreateFrags(), c.isBreakAromaticRings(), c.isSumFormulaRedundancyCheck(), c.getUsername(), c.getPassword(), c.getJdbc(), c.getTreeDepth(), c.isHydrogenTest(), c.getKeggPath(), c.isNeutralLossAdd(), c.isBondEnergyScoring(), c.isOnlyBreakSelectedBonds());
			}
			else if(c.isPubChem())
			{
				try {
					//ps = new PubChemSearch(c.getFolder(), spectrum, c.getMzabs(), c.getMzppm(), c.getSearchPPM(), c.isPdf(), c.isShowDiagrams(), c.isRecreateFrags(), c.isBreakAromaticRings(), c.isSumFormulaRedundancyCheck(), c.getUsername(), c.getPassword(), c.getJdbc(), c.getTreeDepth(), c.isHydrogenTest());
					psParallel = new PubChemSearchParallel(c.getFolder(), spectrum, c.getMzabs(), c.getMzppm(), c.getSearchPPM(), c.isPdf(), c.isShowDiagrams(), c.isRecreateFrags(), c.isBreakAromaticRings(), c.isSumFormulaRedundancyCheck(), c.getUsername(), c.getPassword(), c.getJdbc(), c.getTreeDepth(), c.isHydrogenTest(), c.isNeutralLossAdd(), c.isBondEnergyScoring(), c.isOnlyBreakSelectedBonds());
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (RemoteException e) {
					e.printStackTrace();
				} catch (InvalidSmilesException e) {
					e.printStackTrace();
				} catch (ServiceException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (CDKException e) {
					e.printStackTrace();
				}					
			}
			else
			{
				if(c.isHierarchical())
				{
					System.err.println("Not implemented with hierarchical option");
					System.exit(0);
				}
			}
			end = System.currentTimeMillis() - start;
			
			if(c.isKEGG())
			{
				completeLog = ks.getCompleteLog();
				completeLog += "\t Time: " + end + "\n";
				histogramCompare += ks.getHistogramCompare() + "\t" + end;
				histogram += ks.getHistogram() + "\t" + end;
				histogramReal += ks.getHistogramReal() + "\t" + end;

			}
			else if(c.isPubChem())
			{
				completeLog = psParallel.getCompleteLog();
				completeLog += "\t Time: " + end + "\n ";
				histogramCompare += psParallel.getHistogramCompare() + "\t" + end;
				histogram += psParallel.getHistogram() + "\t" + end;
				histogramReal += psParallel.getHistogramReal() + "\t" + end;
				histogramPeaks += psParallel.getHistogramPeaks().toString();
				histogramPeaksCorresponding += psParallel.getHistogramPeaksReal().toString();
				histogramPeaksAll += psParallel.getHistogramPeaksAll().toString();
				similarityValues += psParallel.getSimilarityValues().toString();
			}
			
            sum += end;
			
            
            //things to log END ====================================================================      
	        // and output on the original stdout				        
	        stdout.println("Finished LOG!!! " + spectrum.getFilename());

	        BufferedReader reader = new BufferedReader(new FileReader(c.getFolder() + spectrum.getFilename() + "/log_" + date));
		  	String line;
		  	// Create file 
		  	FileWriter fstream = new FileWriter(c.getFolder() + spectrum.getFilename() + "/logFile_" + date + ".txt");
	  	    BufferedWriter out = new BufferedWriter(fstream);
	  	    
	  	    while ((line = reader.readLine()) != null)
		  	{
		  	  if (line.length() > 5)
		  	  {
			  	  if(line.startsWith("STDOUT") || line.startsWith("STDERR"))
			  	  {
			  	    out.write(line + "\n");
			  	  }
		  	  }
		  	}
		  	out.close();
			  	
		
			}
			catch(IOException e)
			{
				System.out.println("Error during reading/writing" + e.getMessage());
			}
			
			

			completeLog += "\n\n\n============================================================================";
//			completeLog += "\n#Peaks: " + allPeaks;
//			completeLog += " Found Peaks: " + foundPeaks + " (" + ((double)(foundPeaks*100)/(double)allPeaks) + "%) ";
			completeLog += " Complete Time: " + (sum) + "ms";
//			completeLog += " \n\n Histogram COMPARISON: \n" + histogramCompare;
//			completeLog += " \n\n Histogram (easy scoring): \n" + histogram;
//			completeLog += " \n\n Histogram (real scoring): \n" + histogramReal;

			//write string to disk
			try
			{
				new File(c.getFolder() + "logs/").mkdir();
				File outFile = new File(c.getFolder() + "logs/" + date + "_log.txt");
	            FileWriter out = new FileWriter(outFile, true);
	            out.write(completeLog);
	            out.close();
	            
	            //write peak data of the correct compounds to file
	            FileWriter out1 = new FileWriter(c.getFolder() + "logs/" + date + "_histCompare.txt", true);
	            out1.write(histogramCompare);
	            out1.close();
	            
	            //write peak data of all explained peaks to file
	            FileWriter out2 = new FileWriter(c.getFolder() + "logs/" + date + "_histEasy.txt", true);
	            out2.write(histogram);
	            out2.close();
	            
	            //write peak data of all explained peaks to file
	            FileWriter out3 = new FileWriter(c.getFolder() + "logs/" + date + "_histRealScoring.txt", true);
	            out3.write(histogramReal);
	            out3.close();
	            
	            
	            //write peak data
	            FileWriter allPeaksOut = new FileWriter(c.getFolder() + "logs/" + date + "_AllPeaksInclusive.txt", true);
				allPeaksOut.write(histogramPeaksAll);
				allPeaksOut.close();
	            FileWriter allPeaksExclusiveOut = new FileWriter(c.getFolder() + "logs/" + date + "_AllPeaksExclusive.txt", true);
	            allPeaksExclusiveOut.write(histogramPeaks);
	            allPeaksExclusiveOut.close();
				FileWriter allFoundPeaks = new FileWriter(c.getFolder() + "logs/" + date + "_FoundPeaks.txt", true);
				allFoundPeaks.write(histogramPeaksCorresponding);
	            allFoundPeaks.close();
	            
//	            FileWriter similarities = new FileWriter(c.getFolder() + "logs/" + date + "_Similarities.txt", true);
//	            similarities.write(similarityValues);
//	            similarities.close();
			}
			catch (FileNotFoundException ex)
			{
				System.out.print("File not found: " + ex.getMessage());
			}
			catch (IOException ex)
			{
				System.out.print("IO Error: " + ex.getMessage());
			}
		}

}
