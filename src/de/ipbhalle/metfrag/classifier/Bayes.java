package de.ipbhalle.metfrag.classifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;

import de.ipbhalle.metfrag.main.Config;
import de.ipbhalle.metfrag.main.KEGGSearch;
import de.ipbhalle.metfrag.main.MainMetFrag;
import de.ipbhalle.metfrag.massbankParser.Peak;
import de.ipbhalle.metfrag.spectrum.WrapperSpectrum;
import de.ipbhalle.metfrag.tools.PreprocessSpectraLive;



public class Bayes {
	
	public void generateTrainingTable() throws IOException
	{
		BayesTraining bayes = new BayesTraining();
		
		//************************************************************************************************
		//CONFIGURATION
		Config conf = new Config();
		conf.setComment(conf.getComment() + "\n mzabs: " + conf.getMzabs() + " mzppm: " + conf.getMzppm());

		if(conf.isFolderRead())
		{

			//timer
			long start = 0;
		    long end = 0;
		    long sum = 0;
		    
		    PreprocessSpectraLive ppLive = new PreprocessSpectraLive(conf.getFolder(), conf.getMzabs(), conf.getMzppm());
		    Vector<WrapperSpectrum> mergedSpectra = ppLive.getMergedSpectra();
		    KEGGSearch ks = null;
		    
		    
		    for (WrapperSpectrum spectrum : mergedSpectra) {
		
				//take time
				start = System.currentTimeMillis();
				//kegg or massbank proof of concept						
				if(conf.isKEGG())
				{
					ks = new KEGGSearch(conf.getFolder(), spectrum, conf.getMzabs(), conf.getMzppm(), conf.isPdf(), conf.isShowDiagrams(), conf.isRecreateFrags(), conf.isBreakAromaticRings(), conf.isSumFormulaRedundancyCheck(), conf.getUsername(), conf.getPassword(), conf.getJdbc(), conf.getTreeDepth(), conf.isHydrogenTest(), conf.getKeggPath(), conf.isNeutralLossAdd(), conf.isBondEnergyScoring(), conf.isOnlyBreakSelectedBonds());
					
					//now generate the data table for the classifier
					Vector<Peak> correctPeaks = ks.getVectorOfCorrectPeaks();
					Vector<Peak> allOtherPeaks = ks.getVectorOfPeaks();
					
					for (Peak peak : correctPeaks) {
						bayes.addRow(spectrum.getFilename(), peak.getLowestEnergy(), peak.getMass(), true);
					}
					
					//add all found peaks which do not correspond
					for (Peak peak : allOtherPeaks) {
						bayes.addRow(spectrum.getFilename(), peak.getLowestEnergy(), peak.getMass(), false);
					}
					
				}
				end = System.currentTimeMillis() - start;
				sum += end;
				
				
				//things to log END ====================================================================      
				// and output on the original stdout				        
				System.out.println("Finished LOG!!! " + spectrum.getFilename());
				break;
			}
			
					
			//write string to disk
			try
			{
				new File(conf.getFolder() + "logs/").mkdir();
				File outFile = new File(conf.getFolder() + "logs/training_table.txt");
	            FileWriter out = new FileWriter(outFile);
	            out.write(bayes.getDataTableString());
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
	
	public static void main(String args[])
	{
		Bayes test = new Bayes();
		try
		{
			test.generateTrainingTable();
		}
		catch(IOException e)
		{
			System.err.println("IO Error: " + e.getMessage());
		}
	}
}
