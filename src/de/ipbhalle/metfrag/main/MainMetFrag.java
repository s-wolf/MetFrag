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
	 * Test 1! specified Massbank file with the corresponding mol file.
	 * 
	 * @param folder the folder
	 * @param file the file
	 * @param mzabs the mzabs
	 * @param mzppm the mzppm
	 * @param accessionID the accession id
	 * @param pdf the pdf
	 */
	public void MassbankProofOfConcept(String folder, WrapperSpectrum spectrum, String file, double mzabs, double mzppm, boolean pdf, boolean showDiagrams, boolean breakAromaticRings, boolean createTree, boolean sumFormulaRedundancyCheck)
	{
		BlackList bl = new BlackList(sumFormulaRedundancyCheck);
		Vector<String> blackList = bl.getBlackList();
		
		if(blackList.contains(file))
		{
			completeLog += "Blacklisted Molecule: " + file; 
			return;
		}
		
		this.showDiagrams = showDiagrams;
		//Test Data
		PreprocessSpectraLive ppLive = new PreprocessSpectraLive(c.getFolder(), c.getMzabs(), c.getMzppm(), file);
	    Vector<WrapperSpectrum> mergedSpectra = ppLive.getMergedSpectra();
	    //only get the first spectrum
		//WrapperSpectrum spectrum = mergedSpectra.get(0);
		Vector<Peak> peakList = spectrum.getPeakList();
		int mode = spectrum.getMode();
		
		//open mysql connection to local mysql database
		MysqlMassbank mb = new MysqlMassbank(c.getJdbc(), c.getUsername(), c.getPassword());
		//get mol file for corresponding massbank record
		String molFile = mb.getMol(file.substring(0, 8));
		mb.MysqlMassbankClose();
		
		IAtomContainer molecule = null;
		
		try
		{
			//check if molfile is already fragmented....return for now... save some memory
			if(alreadyDone(molFile))
				return;
			//now fragment the retrieved molecule
	        molecule = Molfile.Read(folder + "Molfiles/" + molFile + ".mol");
	        
	        
	        //now create a new folder to write the .mol files into
	        boolean status = new File(folder + spectrum.getFilename()).mkdir();
	        	        
	        if (status)
	        	System.out.println("Folder created: " + folder + molFile);
	        else
	        	System.out.println("Could not create folder...maybe already exists!!!!");
	        
	        //add hydrogens
	        CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(molecule.getBuilder());

	        for (IAtom atom : molecule.atoms()) {
	          IAtomType type = matcher.findMatchingAtomType(molecule, atom);
	          AtomTypeManipulator.configure(atom, type);
	        }
	        CDKHydrogenAdder hAdder = CDKHydrogenAdder.getInstance(molecule.getBuilder());
	        hAdder.addImplicitHydrogens(molecule);
	        AtomContainerManipulator.convertImplicitToExplicitHydrogens(molecule);
	        
	        Fragmenter fragmenter = new Fragmenter((Vector<Peak>)peakList.clone(), mzabs, mzppm, mode, breakAromaticRings, sumFormulaRedundancyCheck, false, false);
	        long start = System.currentTimeMillis();
	        List<IAtomContainer> l = fragmenter.generateFragmentsInMemory(molecule, true, c.getTreeDepth());
	        
	        //count the number of fragments
	        fragsCount += l.size();
	        long time = System.currentTimeMillis() - start;
	        System.out.println("Benötigte Zeit: " + time);
	        System.out.println("Got " + l.size() + " fragments");
	        System.out.println("Needed " + fragmenter.getNround() + " calls to generateFragments()");
	        
	        //create new folder
	        new File(folder + spectrum.getFilename() + "/pdf_" + dateFormat.format(date)).mkdir();
	        
	        if(createTree)
			{
				StructureToFile ds = new StructureToFile(200, 200, folder + spectrum.getFilename() + "/pdf_" + dateFormat.format(date), true, true);
				ds.writeMOL2PNGFile(molecule, 0 + "");
		        for (int i = 0; i < l.size(); i++) {
		        	ds.writeMOL2PNGFile(l.get(i), (i+1) + "");
				}
		        //create graph from fragments
		        File out = new File(folder + spectrum.getFilename() + "/pdf_" + dateFormat.format(date) + "/" + file + ".ps");
		        GraphViz gv = fragmenter.getGraph();
		        gv.writeGraphToFile(gv.getGraph(gv.getDotSource()), out);
			}
	        	        
	        //create new folder
	        new File(folder + spectrum.getFilename() + "/frags_" + dateFormat.format(date) + "/").mkdir();
	        
	        //write original molecule to folder
	        FileWriter wOrig = new FileWriter(new File(folder + spectrum.getFilename() + "/frags_" + dateFormat.format(date) + "/" + de.ipbhalle.metfrag.tools.Number.numberToFixedLength(0,3) + ".mol"));
            MDLWriter mwOrig = new MDLWriter(wOrig);
            mwOrig.write(new Molecule(molecule));
            mwOrig.close();
	        
	        for (int i = 0; i < l.size(); i++) 
	        {		
				
		        try {
		           FileWriter w = new FileWriter(new File(folder + spectrum.getFilename() + "/frags_" + dateFormat.format(date) + "/" + de.ipbhalle.metfrag.tools.Number.numberToFixedLength(i+1,3) + ".mol"));
		           MDLWriter mw = new MDLWriter(w);
		           mw.write(new Molecule(l.get(i)));
		           mw.close();
		        }catch (IOException e) {
		           System.out.println("IOException: " + e.toString());
		        } 
		        catch (Exception e) {
		           System.out.println(e.toString());
		        }
	        }
	        //Draw molecule and its fragments
	        if (showDiagrams)
	        	//Render.Draw(AtomContainerManipulator.removeHydrogens(molecule), ListManipulator.removeHydrogensInList(l) , "Original Molecule"); 
	        	StructureRendererTable.Draw(molecule, l , "Original Molecule"); 
	        
//	        if(pdf)
//	        {
//		        //Create PDF Output
//		        l.add(0,molecule);
//	        	DisplayStructure ds1 = null;
//	        	//create pdf subfolder
//	        	new File(folder + spectrum.getFilename() + "/pdf_" + dateFormat.format(date)).mkdir();
//	        	ds1 = new WritePDFTable(true, 200, 200, 0.9, 4, false, true, folder + spectrum.getFilename() + "/pdf_" + dateFormat.format(date));
//	        	for (int i = 0; i < l.size(); i++) {
//	                //DisplayStructure(false, 200, 200, 0.9, false, "PDF", "/home/swolf/workspaceFsNEW/FragSearch/");
//	                assert ds1 != null;
//	                ds1.drawStructure(l.get(i), i);
//	    		}
//		        
//		        if (ds1 != null) ds1.close();
//	        }
			
	        //now try to assign the peaks to the fragments
	        peakToStructure(folder, spectrum, spectrum.getFilename(), spectrum.getExactMass(), mzabs, mzppm, molecule, molFile);
			
		}
		catch(CDKException e)
		{
			System.out.println("!!!CDK Error!!!!!Error! " + file + " Message: " + e.getMessage());
			completeLog += "!!!CDK Error!!!!!Error! " + file + " Message: " + e.getMessage();
		}
		catch(FileNotFoundException e)
		{
			System.out.println("!!!!File not found!!!!!!Error! " + file + " Message: " + e.getMessage());
			completeLog += "!!!!File not found!!!!!!Error! " + file + " Message: " + e.getMessage();
		}
		catch(Exception e)
		{
			System.out.println("!!!!Error! " + file + " Message: " + e.getMessage());
			completeLog += "!!!!Error! " + file + " Message: " + e.getMessage();
		}
		catch(OutOfMemoryError e)
		{
			System.out.println("Out of memory: " + e.getMessage() + "\n" + e.getStackTrace());
			System.gc();
			//try to find peaks with all the frags got so far
			completeLog += "!!!!Out of memory in fragmenter! " + "File: " + file;
			peakToStructure(folder, spectrum, file, spectrum.getExactMass(), mzabs, mzppm, molecule, molFile);
		}
		
	}
	
	
	
	/**
	 * Peak to structure.
	 * 
	 * @param folder the folder
	 * @param file the file
	 * @param exactMass the exact mass
	 * @param mzabs the mzabs
	 * @param mzppm the mzppm
	 * @param molecule the molecule
	 * @param molFile the mol file
	 */
	private void peakToStructure(String folder, WrapperSpectrum spectrum, String file, double exactMass, double mzabs, double mzppm, IAtomContainer molecule, String molFile)
	{
		try
		{
			//now read the saved mol files
			List<IAtomContainer> fragments = Molfile.Readfolder(folder + file + "/frags_" + dateFormat.format(date) + "/");
			
			
			Vector<Peak> peakList = spectrum.getPeakList();
			//clean up peak list
			CleanUpPeakList cList = new CleanUpPeakList(peakList);
			Vector<Peak> cleanedPeakList = cList.getCleanedPeakList(spectrum.getExactMass());
			
			//Post process
			//PostProcess pp = new PostProcess();
			//IAtomContainerSet processedFragments = pp.postProcess(fragments, cleanedPeakList, mzabs, mzppm, spectrum.getMode());

			
			//now find corresponding fragments to the mass
			AssignFragmentPeak afp = new AssignFragmentPeak();
			afp.setHydrogenTest(true);
			afp.assignFragmentPeak(fragments, cleanedPeakList, mzabs, mzppm, spectrum.getMode(), false);
			Vector<PeakMolPair> hits = afp.getHits();
			Vector<PeakMolPair> hitsAll = afp.getAllHits();
			
			foundPeaks += hits.size();
			allPeaks += spectrum.getPeakList().size();
			String[] mbEntries = file.split("PB");
			for (int i = 1; i < mbEntries.length; i++) {
				completeLog += "\nURL:http://msbi.ipb-halle.de/MassBank/jsp/Dispatcher.jsp?type=disp&id=PB" + mbEntries[i];
			}
			completeLog += "\n#Peaks: " + spectrum.getPeakList().size() + "\t #Found: " + hits.size();
			System.out.println("\n#Peaks: " + spectrum.getPeakList().size() + "\t #Found: " + hits.size() + "\t AllHits: " + hitsAll.size());
			
			for (int i = 0; i < hits.size(); i++) {
				List<IAtomContainer> hitsList = new ArrayList<IAtomContainer>();
				hitsList.add(AtomContainerManipulator.removeHydrogens(hits.get(i).getFragment()));
				//if (showDiagrams)
				//Render.Highlight(AtomContainerManipulator.removeHydrogens(molecule), hitsList , Double.toString(hits.get(i).getPeak()));
			}
			System.out.println(hitsAll.size());
			if (showDiagrams)
				StructureRendererTable.DrawHits(molecule, hitsAll , "Fragmente von:");
			
			
			//add to done mols
			addMol(molFile);
		}
		catch(FileNotFoundException e)
		{
			System.err.println("File not found! " + e.getMessage());
			completeLog += "!!!File not found!!!!!Error! " + file + " Message: " + e.getMessage();
		}
		catch(CDKException e)
		{
			System.err.println("CDK Error! " + e.getMessage());
			completeLog += "!!!CDK Error!!!!!!Error! " + file + " Message: " + e.getMessage();
		}
		catch(OutOfMemoryError e)
		{
			System.out.println("Out of memory: " + e.getMessage() + "\n" + e.getStackTrace());
			System.gc();
			completeLog += "!!!Out of memory in peak to structure! " + "File: " + file;
			return;
		} catch (IOException e) {
			System.out.println("Out of memory: " + e.getMessage() + "\n" + e.getStackTrace());
			System.gc();
			completeLog += "!!!Error! " + "File: " + file;
		}
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
						else
						{
							test.MassbankProofOfConcept(c.getFolder(), spectrum, spectrum.getFilename(), c.getMzabs(), c.getMzppm(), c.isPdf(), c.isShowDiagrams(), c.isBreakAromaticRings(), c.isCreateTree(), c.isSumFormulaRedundancyCheck());
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
				else
				{
					start = System.currentTimeMillis();
					WrapperSpectrum spectrum = new WrapperSpectrum(c.getFolder() + c.getFile() + ".txt");
					test.MassbankProofOfConcept(c.getFolder(), spectrum, c.getFile(), c.getMzabs(), c.getMzppm(), c.isPdf(), c.isShowDiagrams(), c.isBreakAromaticRings(), c.isCreateTree(), c.isSumFormulaRedundancyCheck());
					end = System.currentTimeMillis() - start;
					completeLog += "Time: " + end + "\nFragments: " +  "";
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
