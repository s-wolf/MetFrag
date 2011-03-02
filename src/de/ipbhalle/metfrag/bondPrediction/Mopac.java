package de.ipbhalle.metfrag.bondPrediction;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.List;

import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.MDLV2000Writer;
import org.openscience.cdk.io.Mol2Reader;
import org.openscience.cdk.io.Mol2Writer;
import org.openscience.cdk.io.formats.MOPAC2002Format;
import org.openscience.cdk.modeling.builder3d.ForceFieldConfigurator;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;

import de.ipbhalle.metfrag.tools.StreamGobbler;
import de.ipbhalle.mopac.converter.CoordinatesTransfer;
import de.ipbhalle.mopac.converter.MOPACInputFormatWriter;

public class Mopac {	
	
	/**
	 * Run MOPAC to optimize the geometry of the molecule.
	 *
	 * @param pathToBabel if a different openbael is to be used! e.g. "/vol/local/bin/"
	 * @param molToOptimize the mol to optimize
	 * @param ffSteps the ff steps
	 * @param verbose the verbose
	 * @param mopacMethod the mopac method
	 * @param mopacRuntime the mopac runtime
	 * @param firstRun the first run
	 * @param atomProtonized the atom protonized
	 * @return the i atom container
	 * @throws Exception the exception
	 */
	public IAtomContainer runOptimization(String pathToBabel, IAtomContainer molToOptimize, int ffSteps, boolean verbose, String mopacMethod, Integer mopacRuntime, boolean firstRun, String atomProtonized) throws Exception
	{
		//write out the molecule
//		File tempFile = File.createTempFile("mol",".mol");
//		FileWriter fw = new FileWriter(tempFile);
//		MDLV2000Writer m2w = new MDLV2000Writer(fw);
//		IMolecule molecule = new Molecule(molToOptimize);
//		m2w.writeMolecule(molecule);
//		m2w.close();
		
		File tempFile = File.createTempFile("mol",".mol2");
		FileWriter fw = new FileWriter(tempFile);
		Mol2Writer m2w = new Mol2Writer(fw);
		IMolecule molecule = new Molecule(molToOptimize);
		m2w.writeMolecule(molecule);
		m2w.close();
		
		Runtime rt = Runtime.getRuntime();
		File tempFileFFInput3D = null;
		
		if(firstRun)
		{
			//convert it back to mol2
	        tempFileFFInput3D = File.createTempFile("molFFInput",".mol2");
//	        String command = "babel --gen3d -i mol2 " + tempFile.getPath() + " -o mol2 " + tempFileFFInput3D.getPath();
	        String command = pathToBabel + "babel --gen2D -i mol2 " + tempFile.getPath() + " -o mol2 " + tempFileFFInput3D.getPath();
//	        String command = "babel -i mol2 " + tempFile.getPath() + " -o mol2 " + tempFileFFInput3D.getPath();
	        String[] psCmdFFInput =
			{
			    "sh",
			    "-c", 
			    command
			};
	        if(verbose)
	        	System.out.println("mol2 to mol2 (3D coordinates generation) command: " + command);
	        
	        Process prFFInput = rt.exec(psCmdFFInput, null);
	        int exitValFFInput = prFFInput.waitFor();
	        System.out.println("mol2 to mol2 (3D coordinates generation) error code " + exitValFFInput);
		}
		else
			tempFileFFInput3D = tempFile;
		
		
		//first of all do a force field optimization using open babel for a first optimization
		//thats the ff optimized file
		File tempFileFF = File.createTempFile("molFF",".pdb");
//		String command = "obminimize -n " + ffSteps + " -sd -ff MMFF94 " + tempFile.getPath();
//		String command = "obminimize -c 1e-3 -sd -ff UFF " + tempFile.getPath();
		String command = pathToBabel + "obminimize -n " + ffSteps + " -sd -ff UFF " + tempFileFFInput3D.getPath() + " > " + tempFileFF.getPath(); 
		String[] psCmd =
		{
		    "sh",
		    "-c", 
		    command
		};
		
		
		if(verbose)
			System.out.println("FF command: " + command);
		
        Process pr = rt.exec(psCmd, null);
        int exitVal = pr.waitFor();
        
//        // any error message?
////      StreamGobbler errorGobbler = new StreamGobbler(pr.getErrorStream(), "ERROR", false);            
//      // any output?
//      StreamGobbler outputGobbler = new StreamGobbler(pr.getInputStream(), "OUTPUT", false);
//      // start
////      errorGobbler.start();
//      outputGobbler.start();  
//
//        FileWriter fstream = new FileWriter(tempFileFF.getPath());
//        BufferedWriter out = new BufferedWriter(fstream);
//        out.write(outputGobbler.getOutput());
//        //Close the output stream
//        out.close();
        System.out.println("FF error code " + exitVal);
		
		
        //convert it back to mol2
        File tempFileFFOptimized = File.createTempFile("molFF",".mol2");
        command = pathToBabel + "babel " + tempFileFF.getPath() + " " + tempFileFFOptimized.getPath();
        String[] psCmdFFMol2 =
		{
		    "sh",
		    "-c", 
		    command
		};
        if(verbose)
        	System.out.println("PDB to mol2 command: " + command);
        
        Process prPDBToMol2 = rt.exec(psCmdFFMol2);
        exitVal = prPDBToMol2.waitFor();
        System.out.println("PDB to mol2 error code " + exitVal);
                
		//then optimize it using mopac
        //generate mopin from mol2
        
        //replace babel mopin generation with own mopin writer
        MOPACInputFormatWriter mopIn = new MOPACInputFormatWriter(mopacMethod + " T=" + mopacRuntime + " GEO-OK, ECHO, MMOK, SCFCRT=1.D-4, GNORM=0.1, XYZ");
        File tempFileMOPIn = File.createTempFile("molMopIN",".dat");
        mopIn.write(tempFileFFOptimized, tempFileMOPIn);
        System.out.println("MOL2 to MOPAC INPUT: " + tempFileFFOptimized.getPath() + " --> " + tempFileMOPIn.getPath());
        
//        File tempFileMOPIn = File.createTempFile("molMopIN",".dat");
//        command = "babel " + tempFileFFOptimized.getPath() + " -o mopin " + tempFileMOPIn.getPath() + " -xk \"AM1 T=3600 GEO-OK, GNORM=0.1, MMOK, SCFCRT=1D-9\"";
////        command = "babel " + tempFileFFOptimized.getPath() + " -o mopin " + tempFileMOPIn.getPath() + " -xk \"PREC AM1 T=3600\"";
//        String[] psCmdMOPIn =
//		{
//		    "sh",
//		    "-c", 
//		    command
//		};
//        
//        if(verbose)
//        	System.out.println("MOPIN command: " + command);
//        
//        Process prMopin = rt.exec(psCmdMOPIn);
//        exitVal = prMopin.waitFor();
//        System.out.println("MOP in error code " + exitVal);
        
        
        
        //now run mopac on mopin
        String tempStringMopacOut = tempFileMOPIn.getParent() + System.getProperty("file.separator") + tempFileMOPIn.getName().split("\\.")[0];
        command = "run_mopac7 " + tempStringMopacOut;
//        String[] psCmdMOPAC =
//		{
//        	"sh",
//        	"-c",
//        	command      	
//		};
        
        if(verbose)
        	System.out.println("MOPAC command: " + command);
        
        Process prMopOut = rt.exec(command);
//        exitVal = prMopOut.waitFor();
        int count = 0;
        //let the process calculate for 10 minutes
        boolean done = false;
        while(count < ((mopacRuntime + 10)/6))
        {
//        	Thread.sleep(12000);
//        	//test START
//        	done = false;
//        	break;
//        	//test END
        	
        	Thread.sleep(6000);    
        	
        	//now check if mopac calculation is finished
        	try
        	{
        		int exitMopac = -1;
        		exitMopac = prMopOut.exitValue();
	    		if(exitMopac >= 0)
	    		{
	    			done = true;
	    			break;
	    		}
        	}
        	catch(IllegalThreadStateException e)
        	{
        		System.out.print(".");
        	}
        	count++;
        }
        //now destroy the process if not finished yet
        if(done)
        	exitVal = prMopOut.waitFor();
        else
        {
        	System.out.println("MOPAC optimization canceled! Atom protonized: " + atomProtonized);
        	
        	//now try to kill the MOPAC process        	
        	Field f = prMopOut.getClass().getDeclaredField("pid");
        	f.setAccessible(true);
        	Integer processID = (Integer) f.get(prMopOut);
        	System.out.println("Process id of parent MOPAC process: " + processID );
        	
        	//now find out the pid of child process
        	String[] psCmdMOPACSubprocess =
    		{
        		"sh",
        		"-c",
            	"ps -ef | grep mopac"
    		};
        	
        	String mopacProcessID = "0";
        	
        	Process prMOPACSubprocess = rt.exec(psCmdMOPACSubprocess);
            
            StreamGobbler sgMOPACSubprocess = new StreamGobbler(prMOPACSubprocess.getInputStream(), "OUTPUT", false);
            sgMOPACSubprocess.start();
//            StreamGobbler sgMOPACSubprocessError = new StreamGobbler(prMOPACSubprocess.getErrorStream(), "ERROR", true);
//            sgMOPACSubprocessError.start();
            
            prMOPACSubprocess.waitFor();
            
            String psOutput = sgMOPACSubprocess.getOutput();
//            System.out.println("Output of ps to parse: \n\n\n" + psOutput + "\n\n\n");
            String cleaned = psOutput.replaceAll("[\\t\\v\\f\\r ]+", ";");
            String[] cleanedLine = cleaned.split("\\n");
            for (int i = 0; i < cleanedLine.length; i++) {
				String[] col = cleanedLine[i].split(";");
				if(col[2].equals(Integer.toString(processID)))
				{
					System.out.println("Found process!! Process id: " + col[1]);
					mopacProcessID = col[1];
				}
			}
            //now find out the process id !!! compare the parent id
        	
        	prMopOut.destroy();


        	
            command = "kill -9 " + mopacProcessID;
            String[] psMOPACKill =
    		{
    		    "sh",
    		    "-c", 
    		    command
    		};
            
            if(verbose)
            	System.out.println("MOPAC kill command: " + command);
            Process prMOPACKill = rt.exec(psMOPACKill);
            exitVal = prMOPACKill.waitFor();
            
            // any error message?
	        StreamGobbler errorGobblerKill = new StreamGobbler(prMOPACKill.getErrorStream(), "ERROR", true);            
	        // any output?
	        StreamGobbler outputGobblerKill = new StreamGobbler(prMOPACKill.getInputStream(), "OUTPUT", true);
	        // start
	        errorGobblerKill.start();
	        outputGobblerKill.start();  
	            
	        return null;
        }
        
        
        
        System.out.println("MOPAC error code " + exitVal);
       
        //now convert the result back to mol2
        File tempFileMOPACMol2 = File.createTempFile("molMopac",".mol2");
        command = pathToBabel + "babel -i mopout " + tempStringMopacOut + ".OUT" + " -o mol2 " + tempFileMOPACMol2;
        String[] psCmdMOPACMol2 =
		{
		    "sh",
		    "-c", 
		    command
		};
        Process prMopacMol2 = rt.exec(psCmdMOPACMol2);
        exitVal = prMopacMol2.waitFor();
        
        if(verbose)
        	System.out.println("MOPAC Mol2: " + command);
        
        System.out.println("MOPAC Mol2 error code " + exitVal);
                
		//read in the molecule again with the new coordinates
        Mol2Reader mr = new Mol2Reader(new FileReader(tempFileMOPACMol2));
        ChemFile chemFile = (ChemFile)mr.read((ChemObject)new ChemFile());
        List<IAtomContainer> containersList = ChemFileManipulator.getAllAtomContainers(chemFile);
        IAtomContainer molOptimized = containersList.get(0);
//        return containersList.get(0);
        return CoordinatesTransfer.transferCoordinates(molOptimized, molToOptimize);
	}
}
