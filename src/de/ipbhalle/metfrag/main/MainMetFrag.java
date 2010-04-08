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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Vector;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.xml.rpc.ServiceException;


import org.openscience.cdk.Molecule;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.io.MDLWriter;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;

import de.ipbhalle.metfrag.fragmenter.*;
import de.ipbhalle.metfrag.graphviz.GraphViz;
import de.ipbhalle.metfrag.massbankParser.Peak;
import de.ipbhalle.metfrag.read.Molfile;
import de.ipbhalle.metfrag.spectrum.AssignFragmentPeak;
import de.ipbhalle.metfrag.spectrum.CleanUpPeakList;
import de.ipbhalle.metfrag.spectrum.PeakMolPair;
import de.ipbhalle.metfrag.spectrum.WrapperSpectrum;
import de.ipbhalle.metfrag.tools.PreprocessSpectraLive;
import de.ipbhalle.metfrag.tools.renderer.StructureRendererTable;
import de.ipbhalle.metfrag.tools.renderer.StructureToFile;



public class MainMetFrag {
	
	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    private static java.util.Date date = new java.util.Date();
    private static String completeLog = "";
    private static int foundPeaks = 0;
    private static int allPeaks = 0;
    private boolean showDiagrams = false;
    private Vector<String> doneMols = new Vector<String>();
    private HashMap<Integer, ArrayList<String>> scoreMap = new HashMap<Integer, ArrayList<String>>();
    private static String histogram = "";
    private static String histogramCompare = "";
    private static String histogramReal = "";
    private static String histogramPeaks = "";
    private static String histogramPeaksAll = "";
    private static String histogramPeaksCorresponding = "";
    private static long sumTime = 0;
    private static int fragsCount = 0;
    private static Config c;

	
	/**
	 * Add mol name without extension to the already done molecules
	 * 
	 * @param mol the mol
	 */
	private void addMol(String mol)
	{
		this.doneMols.add(mol);
	}
	
	/**
	 * Already done mol files.
	 * 
	 * @param candidate the candidate molecule
	 * 
	 * @return true, if successful
	 */
	private boolean alreadyDone(String candidate)
	{
		boolean test = false;
		
		if(doneMols.contains(candidate))
			test = true;
		
		return test;
	}	
		
	
	
	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		
		try {
			c = new Config();
		} catch (IOException e1) {
			System.err.println("COULD NOT CONFIG FILE! " + e1.getMessage());
			e1.printStackTrace();
			System.exit(0);
		}
				
		if(c.isCreateTree() && c.isFolderRead())
		{
			System.err.println("Do not use create tree with folder read! Image creation very time consuming!");
			System.exit(0);
		}
		
		HierarchicalSearch hs = null;
		KEGGSearch ks = null;
		PubChemSearchParallel psParallel = null;
		PubChemSearch ps = null;
		
		if(c.isFolderRead())
		{
			//add comment to log file
			completeLog += c.getComment() + "\n\n\n"; 

			//timer
			long start = 0;
		    long end = 0;
		    long sum = 0;
		    
		    PreprocessSpectraLive ppLive = new PreprocessSpectraLive(c.getFolder(), c.getMzabs(), c.getMzppm());
		    Vector<WrapperSpectrum> mergedSpectra = ppLive.getMergedSpectra();
		    
		    
		    for (WrapperSpectrum spectrum : mergedSpectra) {
		    	try
				{		
					//create a buffered reader that connects to the console, we use it so we can read lines
		            System.out.println("Current Date Time : " + dateFormat.format(date));
		            String path = c.getFolder() + spectrum.getFilename();
					new File(path).mkdir();
					//PrintWriter out = new PrintWriter(new FileWriter(folder + file + "/" + dateFormat.format(date) + "/ConsoleOutput.txt"));
					
					
					// initialize logging to go to rolling log file
			        LogManager logManager = LogManager.getLogManager();
			        logManager.reset();

			        // log file max size 10K, 3 rolling files, append-on-open
			        Handler fileHandler = new FileHandler(c.getFolder() + spectrum.getFilename() + "/log_" + dateFormat.format(date), 10000000, 1, true);
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
						if(c.isHierarchical())
						{
							hs = new HierarchicalSearch(c.getFolder(), spectrum.getFilename().substring(0, 8), c.getMzabs(), c.getMzppm(), c.isBreakAromaticRings(), c.isSumFormulaRedundancyCheck(), c.isShowDiagrams(), c.isPdf(), c.getTreeDepth(), c.getKeggPath(), c.isHydrogenTest());
						}
						else
						{
							ks = new KEGGSearch(c.getFolder(), spectrum, c.getMzabs(), c.getMzppm(), c.isPdf(), c.isShowDiagrams(), c.isRecreateFrags(), c.isBreakAromaticRings(), c.isSumFormulaRedundancyCheck(), c.getUsername(), c.getPassword(), c.getJdbc(), c.getTreeDepth(), c.isHydrogenTest(), c.getKeggPath(), c.isNeutralLossAdd(), c.isBondEnergyScoring(), c.isOnlyBreakSelectedBonds());
						}
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
					
					if(c.isHierarchical())
					{
						completeLog = hs.getCompleteLog();
						completeLog += "\t Time: " + end + "\n Frags Generated: " + fragsCount;
						histogramCompare += hs.getHistogramCompare() + "\t" + end;
						histogram += hs.getHistogram() + "\t" + end;
						histogramReal += hs.getHistogramReal() + "\t" + end;
//						histogramPeaks += hs.getHistogramPeaks();
//						histogramPeaksCorresponding += hs.getHistogramPeaksReal();
//						histogramPeaksAll += hs.getHistogramPeaksAll();
					}
					else if(c.isKEGG())
					{
						completeLog = ks.getCompleteLog();
						completeLog += "\t Time: " + end + "\n Frags Generated: " + fragsCount;
						histogramCompare = ks.getHistogramCompare() + "\t" + end;
						histogram = ks.getHistogram() + "\t" + end;
						histogramReal = ks.getHistogramReal() + "\t" + end;
//						histogramPeaks += ks.getHistogramPeaks();
//						histogramPeaksCorresponding += ks.getHistogramPeaksReal();
//						histogramPeaksAll += ks.getHistogramPeaksAll();
					}
					else if(c.isPubChem())
					{
						completeLog = psParallel.getCompleteLog();
						completeLog += "\t Time: " + end + "\n Frags Generated: " + fragsCount;
						histogramCompare = psParallel.getHistogramCompare() + "\t" + end;
						histogram = psParallel.getHistogram() + "\t" + end;
						histogramReal = psParallel.getHistogramReal() + "\t" + end;
//						histogramPeaks += ps.getHistogramPeaks();
//						histogramPeaksCorresponding += ps.getHistogramPeaksReal();
//						histogramPeaksAll += ps.getHistogramPeaksAll();
					}
					
		            sum += end;
					
		            
		            //things to log END ====================================================================      
			        // and output on the original stdout				        
			        stdout.println("Finished LOG!!! " + spectrum.getFilename());

			        BufferedReader reader = new BufferedReader(new FileReader(c.getFolder() + spectrum.getFilename() + "/log_" + dateFormat.format(date)));
				  	String line;
				  	// Create file 
				  	FileWriter fstream = new FileWriter(c.getFolder() + spectrum.getFilename() + "/logFile_" + dateFormat.format(date) + ".txt");
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
				
				
				try
				{
					new File(c.getFolder() + "logs/").mkdir();
					File outFile = new File(c.getFolder() + "logs/" + dateFormat.format(date) + "_log.txt");
//					File histogramAllPeaks = new File(c.getFolder() + "logs/" + dateFormat.format(date) + "_AllPeaksInclusive.txt");
//					File histogramAllPeaksExclusive = new File(c.getFolder() + "logs/" + dateFormat.format(date) + "_AllPeaksExclusive.txt");
//					File histogramFoundPeaks = new File(c.getFolder() + "logs/" + dateFormat.format(date) + "_FoundPeaks.txt");
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
		            
//		            //write peak data of the correct compounds to file
//		            FileWriter out1 = new FileWriter(histogramAllPeaksExclusive);
//		            out1.write(histogramPeaks);
//		            out1.close();
//		            
//		            //write peak data of all explained peaks to file
//		            FileWriter out2 = new FileWriter(histogramFoundPeaks);
//		            out2.write(histogramPeaksCorresponding);
//		            out2.close();
//		            
//		          //write peak data of all explained peaks to file
//		            FileWriter out3 = new FileWriter(histogramAllPeaks);
//		            out3.write(histogramPeaksAll);
//		            out3.close();
		            
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
			
					
			
			if(c.isHierarchical())
			{
				completeLog += "\n\n\n============================================================================";
				completeLog += "\n#Peaks: " + hs.getAllPeaks();
				completeLog += " Found Peaks: " + hs.getFoundPeaks() + " (" + ((double)(hs.getFoundPeaks()*100)/(double)hs.getAllPeaks()) + "%) ";
				completeLog += " Complete Time: " + (sum) + "ms";
				completeLog += " \n\n Histogram COMPARISON: \n" + histogramCompare;
				completeLog += " \n\n Histogram (easy scoring): \n" + histogram;
				completeLog += " \n\n Histogram (real scoring): \n" + histogramReal;
			}
			else
			{
				completeLog += "\n\n\n============================================================================";
				completeLog += "\n#Peaks: " + allPeaks;
				completeLog += " Found Peaks: " + foundPeaks + " (" + ((double)(foundPeaks*100)/(double)allPeaks) + "%) ";
				completeLog += " Complete Time: " + (sum) + "ms";
				completeLog += " \n\n Histogram COMPARISON: \n" + histogramCompare;
				completeLog += " \n\n Histogram (easy scoring): \n" + histogram;
				completeLog += " \n\n Histogram (real scoring): \n" + histogramReal;
			}
			//write string to disk
			try
			{
				new File(c.getFolder() + "logs/").mkdir();
				File outFile = new File(c.getFolder() + "logs/" + dateFormat.format(date) + "_log.txt");
//				File histogramAllPeaks = new File(c.getFolder() + "logs/" + dateFormat.format(date) + "_AllPeaksInclusive.txt");
//				File histogramAllPeaksExclusive = new File(c.getFolder() + "logs/" + dateFormat.format(date) + "_AllPeaksExclusive.txt");
//				File histogramFoundPeaks = new File(c.getFolder() + "logs/" + dateFormat.format(date) + "_FoundPeaks.txt");
	            FileWriter out = new FileWriter(outFile, true);
	            out.write(completeLog);
	            out.close();
	            
	            //write peak data of the correct compounds to file
//	            FileWriter out1 = new FileWriter(histogramAllPeaksExclusive);
//	            out1.write(histogramPeaks);
//	            out1.close();
//	            
//	            //write peak data of all explained peaks to file
//	            FileWriter out2 = new FileWriter(histogramFoundPeaks);
//	            out2.write(histogramPeaksCorresponding);
//	            out2.close();
//	            
//	          //write peak data of all explained peaks to file
//	            FileWriter out3 = new FileWriter(histogramAllPeaks);
//	            out3.write(histogramPeaksAll);
//	            out3.close();
	            
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
		//only one file is being processed....
		else
		{
			//timer
			long start = 0;
		    long end = 0;

			MainMetFrag test = new MainMetFrag();			
			
			if(c.isKEGG())
			{
				if(c.isHierarchical())
				{
					hs = new HierarchicalSearch(c.getFolder(), c.getFile(), c.getMzabs(), c.getMzppm(), c.isBreakAromaticRings(), c.isSumFormulaRedundancyCheck(), c.isShowDiagrams(), c.isPdf(), c.getTreeDepth(), c.getKeggPath(), c.isHydrogenTest());
					completeLog += " \n\n Histogram: \n" + hs.getHistogram();
					completeLog += " \n\n Histogram (real scoring): \n" + hs.getHistogramReal();
				}
				else
				{
					WrapperSpectrum spectrum = new WrapperSpectrum(c.getFolder() + c.getFile() + ".txt");
					ks = new KEGGSearch(c.getFolder(), spectrum, c.getMzabs(), c.getMzppm(), c.isPdf(), c.isShowDiagrams(), c.isRecreateFrags(), c.isBreakAromaticRings(), c.isSumFormulaRedundancyCheck(), c.getUsername(), c.getPassword(), c.getJdbc(), c.getTreeDepth(), c.isHydrogenTest(), c.getKeggPath(), c.isNeutralLossAdd(), c.isBondEnergyScoring(), c.isOnlyBreakSelectedBonds());
					completeLog += " \n\n Histogram: \n" + ks.getHistogram();
					completeLog += " \n\n Histogram (real scoring): \n" + ks.getHistogramReal();
				}
			}
			else if(c.isPubChem())
			{
				try
				{
					WrapperSpectrum spectrum = new WrapperSpectrum(c.getFolder() + c.getFile() + ".txt");
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
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				completeLog += "\n\n\n============================================================================";
				completeLog += "\n#Peaks: " + ps.getAllPeaks();
				completeLog += " Found Peaks: " + ps.getFoundPeaks() + " (" + ((double)(ps.getFoundPeaks()*100)/(double)ps.getAllPeaks()) + "%) ";
//				completeLog += " Complete Time: " + (sum) + "ms";
				
				
				completeLog += ps.getCompleteLog();
				completeLog += "\t Time: " + end + "\n Frags Generated: " + fragsCount;
				histogramCompare += ps.getHistogramCompare() + "\t" + end;
				histogram += ps.getHistogram() + "\t" + end;
				histogramReal += ps.getHistogramReal() + "\t" + end;
				histogramPeaks += ps.getHistogramPeaks();
				histogramPeaksCorresponding += ps.getHistogramPeaksReal();
				histogramPeaksAll += ps.getHistogramPeaksAll();
				
				
				
				completeLog += " \n\n Histogram COMPARISON: \n" + histogramCompare;
				completeLog += " \n\n Histogram (easy scoring): \n" + histogram;
				completeLog += " \n\n Histogram (real scoring): \n" + histogramReal;
			}
			else
			{
				if(c.isHierarchical())
				{
					System.err.println("Not implemented with hierarchical search!");
					System.exit(0);
				}
			}
			
			
			//write string to disk
			try
			{
				new File(c.getFolder() + "logs/").mkdir();
				File outFile = new File(c.getFolder() + "logs/" + dateFormat.format(date) + "_log.txt");
	            FileWriter out = new FileWriter(outFile);
	            out.write(completeLog);
	            out.close();
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
}
