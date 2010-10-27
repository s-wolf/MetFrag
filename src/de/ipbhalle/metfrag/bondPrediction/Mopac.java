package de.ipbhalle.metfrag.bondPrediction;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;

import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.Mol2Reader;
import org.openscience.cdk.io.Mol2Writer;
import org.openscience.cdk.modeling.builder3d.ForceFieldConfigurator;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;

import de.ipbhalle.metfrag.tools.StreamGobbler;

public class Mopac {	
	
	/**
	 * Run MOPAC to optimize the geometry of the molecule.
	 *
	 * @param molToOptimize the mol to optimize
	 * @param ffSteps the ff steps
	 * @return the i atom container
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws InterruptedException the interrupted exception
	 * @throws CDKException the cDK exception
	 */
	public IAtomContainer runOptimization(IAtomContainer molToOptimize, int ffSteps) throws IOException, InterruptedException, CDKException
	{
		//write out the molecule
		File tempFile = File.createTempFile("mol",".mol2");
		FileWriter fw = new FileWriter(tempFile);
		Mol2Writer m2w = new Mol2Writer(fw);
		IMolecule molecule = new Molecule(molToOptimize);
		m2w.writeMolecule(molecule);
		m2w.close();
		
		//first of all do a force field optimization using open babel for a first optimization
		Runtime rt = Runtime.getRuntime();
		//thats the ff optimized file
		File tempFileFF = File.createTempFile("molFF",".pdb");
		String command = "obminimize -n " + ffSteps +" -sd -ff MMFF94 " + tempFile.getPath();
		String[] psCmd =
		{
		    "sh",
		    "-c", 
		    command
		};

        Process pr = rt.exec(psCmd, null);
        
        // any error message?
        StreamGobbler errorGobbler = new StreamGobbler(pr.getErrorStream(), "ERROR");            
        // any output?
        StreamGobbler outputGobbler = new StreamGobbler(pr.getInputStream(), "OUTPUT");
        // start
        errorGobbler.start();
        outputGobbler.start();  

        int exitVal = pr.waitFor();

        FileWriter fstream = new FileWriter(tempFileFF.getPath());
        BufferedWriter out = new BufferedWriter(fstream);
        out.write(outputGobbler.getOutput());
        //Close the output stream
        out.close();
        System.out.println("FF error code " + exitVal);

		//then optimize it using mopac
        //generate mopin from mol2
        File tempFileMOPIn = File.createTempFile("molMopIN",".dat");
        command = "babel " + tempFileFF.getPath() + " -o mopin " + tempFileMOPIn.getPath() + " -xk \"PREC AM1 T=3600 GEO-OK, GNORM=0.1, MMOK, SCFCRT=1D-9\"";
        String[] psCmdMOPIn =
		{
		    "sh",
		    "-c", 
		    command
		};
        Process prMopin = rt.exec(psCmdMOPIn);
        exitVal = prMopin.waitFor();
        System.out.println("MOP in error code " + exitVal);
        
        //now run mopac on mopin
        File tempFileMOPOut = File.createTempFile("molMopOUT",".out");
        command = "run_mopac7 " + tempFileMOPIn.getPath() + " > " + tempFileMOPOut.getPath();
        Process prMopOut = rt.exec(command);
        exitVal = prMopOut.waitFor();
        System.out.println("MOPAC error code " + exitVal);
       
        //now convert the result back to mol2
        File tempFileMOPACMol2 = File.createTempFile("molMopac",".out");
        Process prMopacMol2 = rt.exec("babel -i " + tempFileMOPOut.getPath() + " " + tempFileMOPACMol2);
        exitVal = prMopacMol2.waitFor();
        System.out.println("MOPAC Mol2 error code " + exitVal);
                
		//the read in the molecule again with the new coordinates
        Mol2Reader mr = new Mol2Reader(new FileReader(tempFileMOPACMol2));
        ChemFile chemFile = (ChemFile)mr.read((ChemObject)new ChemFile());
        List<IAtomContainer> containersList = ChemFileManipulator.getAllAtomContainers(chemFile);
        
        return containersList.get(0);
	}
}
