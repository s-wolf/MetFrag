package de.ipbhalle.metfrag.bondPrediction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;

import de.ipbhalle.metfrag.tools.MoleculeTools;
import de.ipbhalle.metfrag.tools.Writer;

public class MopacGridEngine {
	
	public static void main(String[] args) {
		
//		Example: /home/swolf/MOPAC/EMMATest/1000_molec_JAVA/data/1000_molec_try8_wM_END1.sdf.H.sdf 2011-08-01 /home/swolf/MOPAC/EMMATest/1000_molec_JAVA/log/test.txt UFF 2400 2400 "AM1, GEO-OK, ECHO, MMOK, XYZ, BONDS"
		
		String currentFile = "";
		String date = "";
		String outputFile = "";
		String method = "";
		int ffSteps = 2400;
		int mopacTime = 2400;
		String mopacParameters = "AM1, GEO-OK, ECHO, MMOK, XYZ, BONDS";
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
			
			if(args[2] != null)
			{
				outputFile = args[2];
			}
			else
			{
				System.err.println("Error! Parameter missing!");
				System.exit(1);
			}
			if(args[3] != null)
			{
				method = args[3];
			}
			else
			{
				System.err.println("Error! Parameter missing!");
				System.exit(1);
			}
			
			if(args.length > 4)
			{
				ffSteps = Integer.parseInt(args[4]);
			}
			
			if(args.length > 5)
			{
				mopacTime = Integer.parseInt(args[5]);
			}
			
			if(args.length > 6)
			{
				mopacParameters = args[6];
			}
		}
		catch(Exception e)
		{
			System.err.println("Error! Parameter(s) missing!");
			System.exit(1);
		}
		
		try
		{
			File file = new File(currentFile);
			String[] temp = file.getName().split("\\.");
			String extension = temp[(temp.length -1)];
			if(file.isFile() && extension.toLowerCase().equals("sdf"))
			{
				String error = "";
				
				Mopac mopac = new Mopac();
				MDLV2000Reader reader;
				List<IAtomContainer> containersList;
				
				reader = new MDLV2000Reader(new FileReader(file));
		        ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());
		        containersList = ChemFileManipulator.getAllAtomContainers(chemFile);
		        IAtomContainer mol = containersList.get(0);
			    reader.close();
			    
		        try
		        {
			        //add hydrogens
			        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol);
			        CDKHydrogenAdder hAdder = CDKHydrogenAdder.getInstance(mol.getBuilder());
			        hAdder.addImplicitHydrogens(mol);
			        AtomContainerManipulator.convertImplicitToExplicitHydrogens(mol);
		        }
		        //there is a bug in cdk??
		        catch(IllegalArgumentException e)
	            {
		        	System.err.println("Error CDK! " + e.getMessage());
		        	error = e.getMessage();
		        	e.getStackTrace();
	            }
		        catch(CDKException e)
		        {
		        	System.err.println("Error CDK! " + e.getMessage());
		        	error = e.getMessage();
		        	e.getStackTrace();
		        }
		        
		        mol = MoleculeTools.moleculeNumbering(mol);
		        
		        String output = "";
				try
				{
					mopac.runOptimization(file.getName(), "/vol/local/bin/", "run_mopac7", mol, ffSteps, true, method, mopacParameters, mopacTime, true, "none", false, 0, false);
					output = file.getName() + "\tHeat of Formation: " + mopac.getHeatOfFormation() + "\tTime: " + mopac.getTime() + "\tWarning: " + mopac.getWarningMessage() + "\tError: " + mopac.getErrorMessage() + "\tCDKError: " + error  + "\n";
				}
				catch(Exception e)
				{
					output = file.getName() + "\tError in Optimization\n";
				}
				
				
				try{
				    // Create file 
					Writer.writeToFile(outputFile + "_" + date, output);
				    
			    }catch (Exception e){//Catch exception if any
			      System.err.println("Error: " + e.getMessage());
			    }
				
			}
		}
		catch(FileNotFoundException e)
		{
			System.err.println("Error! " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
