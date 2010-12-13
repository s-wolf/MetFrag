package de.ipbhalle.metfrag.main;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.openscience.cdk.MoleculeSet;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IBond.Stereo;
import org.openscience.cdk.io.SDFWriter;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

import de.ipbhalle.metfrag.spectrum.PeakMolPair;
import de.ipbhalle.metfrag.spectrum.WrapperSpectrum;


/**
 * The Class BatchFileProcessing. This is able to process MetFrag batch files contained in 
 * a folder and writes out SDF files containing the ranked structures.
 */
public class BatchFileProcessingSDF {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String file = "";
		String outputFolder = "";
		Double mzabs = 0.0;
		Double mzppm = 0.0;
		Integer treeDepth = 2;
		Integer allFragments = 0;
		String database = "kegg";
		//TODO
		String pathToSDFDatabase = "";
		
		//get command line arguments
		if(args != null && args.length >= 3)
		{
			file = args[0];
			outputFolder = args[1];
			mzabs = Double.parseDouble(args[2]);
			mzppm = Double.parseDouble(args[3]);
			database = args[4];
			pathToSDFDatabase = args[5];
			if(args.length > 6)
				treeDepth = Integer.parseInt(args[6]);

		}
		else
		{
			System.err.println("Please enter command line arguments!\n1. Argument: Complete Path to input batch file \n2. Argument: Folder where to save the SDF output\n3. Argument: mzabs\n4. Argument: mzppm\n5. Argument: Database \n6. Argument: Local SDF file containing structures\nExample: /home/frasche/MM48_MSMSpos_MH3_20_1-A,1_01_13435-15.mb /home/swolf/ 0.01 10 SDF /home/swolf/SDFDatabase.sdf\n\n also the files neutralLossRules.csv and bondenergies.txt are needed: -Xms1500m -Xmx4048m -Dproperty.file.path=/home/swolf/src/MetFragCommandLine/MetFragPaper/");
			System.exit(1);
		}
		

	    String strLine;
	    String pubchemID = "";
	    String peaks = "";
	    Double exactMass = Double.MAX_VALUE;
	    String sample = "";
	    Integer mode = 1;
	    
	    try
	    {
	    	//read in file
			FileInputStream fstream = new FileInputStream(new File(file));
		    // Get the object of DataInputStream
		    DataInputStream in = new DataInputStream(fstream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    
		    //Read File Line By Line
		    while ((strLine = br.readLine()) != null)   {
		    	
		    	//sample
		    	if(strLine.startsWith("# Sample:"))
		    		sample = strLine.substring(10).replace('/', '_').trim();
		    	
		    	//pubchem id
		    	if(strLine.startsWith("# PubChem ID:"))
		    		pubchemID = strLine.substring(14);
		    	
		    	//parent mass
		    	if(strLine.startsWith("# Parent Mass:"))
		    		exactMass = Double.parseDouble(strLine.substring(15));
		    	
		    	//mode
		    	if(strLine.startsWith("# Mode:"))
		    		mode = Integer.parseInt(strLine.substring(8));
		    	
		    	//peaks
		    	if(!strLine.startsWith("#"))
		    		peaks += strLine + "\n";
		    }
		    
		    //now calculate the correct mass
		    exactMass = exactMass - ((double)mode * 1.0072765);
		    
		    //Fragment the structures!
		    List<MetFragResult> results = MetFrag.startConvenienceSDF(new WrapperSpectrum(peaks, mode, exactMass), false, mzabs, mzppm, 10, true, true, treeDepth, true, false, true, false, Integer.MAX_VALUE, true, pathToSDFDatabase);
		    													  
		    MoleculeSet setOfMolecules = new MoleculeSet();
			for (MetFragResult result : results) {
				//get corresponding structure
				IAtomContainer tmp = result.getStructure();
				tmp = AtomContainerManipulator.removeHydrogens(tmp);
				tmp.setProperty("DatabaseID", result.getCandidateID());
				tmp.setProperty("Score", result.getScore());
				tmp.setProperty("NoPeaksExplained", result.getPeaksExplained());
				
				//fix for bug in mdl reader setting where it happens that bond.stereo is null when the bond was read in as UP/DOWN (4)
				for (IBond bond : tmp.bonds()) {
					if(bond.getStereo() == null)
						bond.setStereo(Stereo.UP_OR_DOWN);		
				} 
				
				String matchedPeaksString = "";
				int count = 0;
				for (PeakMolPair fragment : result.getFragments()) {
					count++;
					if(count == result.getFragments().size())
						matchedPeaksString += fragment.getPeak().getMass() + " " + fragment.getPeak().getRelIntensity();
					else
						matchedPeaksString += fragment.getPeak().getMass() + " " + fragment.getPeak().getRelIntensity() + " ";
				}
				
				if(!matchedPeaksString.equals(""))
					tmp.setProperty("PeaksExplained", matchedPeaksString);
				
				setOfMolecules.addAtomContainer(tmp);
			}	

			try {
				SDFWriter writer = new SDFWriter(new FileWriter(new File(outputFolder + sample + ".sdf")));
				writer.write(setOfMolecules);
				writer.close();
			} catch (CDKException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    
	    }
	    catch(FileNotFoundException e){
	    	System.err.println("File not found!");
	    } catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
