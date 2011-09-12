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

import de.ipbhalle.metfrag.spectrum.WrapperSpectrum;
import de.ipbhalle.metfrag.tools.Constants;


/**
 * The Class BatchFileProcessing. This is able to process MetFrag batch files contained in 
 * a folder and writes out SDF files containing the ranked structures.
 */
public class BatchFileProcessingLocal {

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
			if(args.length > 5)
				treeDepth = Integer.parseInt(args[5]);
		}
		else
		{
			System.err.println("Please enter CL values!\n1. value: Complete Path to input batch file \n2. Folder where to store the SDF output\n3. value: mzabs\n4. value: mzppm\n5. value: Database\nExample: /home/frasche/MM48_MSMSpos_MH3_20_1-A,1_01_13435-15.mb /home/swolf/ 0.01 10 kegg\n\n also the files neutralLossRules.csv and bondenergies.txt are needed: -Xms1500m -Xmx4048m -Dproperty.file.path=/home/swolf/src/MetFragCommandLine/MetFragPaper/");
			System.exit(1);
		}
		
		Config config = null;
		try {
			config = new Config("outside");
		} catch (IOException e1) {
			System.err.println("Config not found! Please set: e.g.: -Dproperty.file.path=/vol/local/lib/MetFrag/MetFragPaper/");
			e1.printStackTrace();
		}
		
	    String strLine;
	    String peaks = "";
	    Double exactMass = 0.0;
	    String sample = "";
	    Integer mode = 1;
	    String jdbc =  config.getJdbcPostgres();
	    String username = config.getUsernamePostgres();
	    String password = config.getPasswordPostgres();
	    Integer searchPPM = 10;
	    boolean isPositive = false;
	    
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
		    	
		    	
		    	//parent mass
		    	if(strLine.startsWith("# Parent Mass:"))
		    		exactMass = Double.parseDouble(strLine.substring(15));
		    	
		    	//mode
		    	if(strLine.startsWith("# Mode:"))
		    		mode = Integer.parseInt(strLine.substring(8));
		    	
		    	//peaks
		    	if(!strLine.startsWith("#"))
		    		peaks += strLine + "\n";
		    	
		    	//charge
		    	if(strLine.startsWith("# Charge:"))
		    	{
		    		if(strLine.contains("+"))
		    			isPositive = true;
		    	}
		    	
		    	//search ppm
		    	if(strLine.startsWith("# Search PPM:"))
		    	{
		    		searchPPM = Integer.parseInt(strLine.substring(14));
		    	}
		    }
		    
		    //now calculate the correct mass
		    exactMass = exactMass - ((double)mode * Constants.PROTON_MASS);
		    
		    //Fragment the structures!
//		    List<MetFragResult> results = MetFrag.startConvenienceLocal(database, "", "", exactMass, new WrapperSpectrum(peaks, mode, exactMass, isPositive), false, mzabs, mzppm, searchPPM, true, true, treeDepth, true, false, true, false, Integer.MAX_VALUE, jdbc, username, password, 3);
		    List<MetFragResult> results = MetFrag.startConvenienceLocalMetChem(database, "", "", exactMass, new WrapperSpectrum(peaks, mode, exactMass, isPositive), false, mzabs, mzppm, searchPPM, true, true, treeDepth, true, false, true, false, Integer.MAX_VALUE, config, 3);
		    MoleculeSet setOfMolecules = new MoleculeSet();
			for (MetFragResult result : results) {
				//get corresponding structure
				IAtomContainer tmp = result.getStructure();
				tmp = AtomContainerManipulator.removeHydrogens(tmp);
				tmp.setProperty("DatabaseID", result.getCandidateID());
				tmp.setProperty("Score", result.getScore());
				tmp.setProperty("PeaksExplained", result.getPeaksExplained());
				
				//fix for bug in mdl reader setting where it happens that bond.stereo is null when the bond was read in as UP/DOWN (4)
				for (IBond bond : tmp.bonds()) {
					if(bond.getStereo() == null)
						bond.setStereo(Stereo.UP_OR_DOWN);		
				} 
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
