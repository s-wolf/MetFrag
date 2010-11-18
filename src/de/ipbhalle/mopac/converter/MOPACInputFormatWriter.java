package de.ipbhalle.mopac.converter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.Mol2Reader;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;

public class MOPACInputFormatWriter {
	
	private String keywords;
	
	/**
	 * Instantiates a new MOPAC input format writer.
	 *
	 * @param keywords for MOPAC
	 */
	public MOPACInputFormatWriter(String keywords)
	{
		this.keywords = keywords;
	}
	
	/**
	 * Write the output to file.
	 *
	 * @param mol the mol
	 * @param outputFile the output file
	 */
	public void write(IAtomContainer mol, File outputFile)
	{
		StringBuilder output = new StringBuilder();
		
		//first 3 lines
		output.append(this.keywords + "\n");
		output.append(outputFile.getPath() + "\n");
		output.append("\n");
		
		//now get the coordinates
		for (IAtom atom : mol.atoms())
		{
			String line = atom.getSymbol() + "   " + atom.getPoint3d().x + " 0 " + atom.getPoint3d().y + " 0 " + atom.getPoint3d().z + " 0    0 0 0 0";
			output.append(line + "\n");
		}
		
		try{
			// Create file 
			FileWriter fstream = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(output.toString() + "\n");
			//Close the output stream
			out.close();
	    }catch (Exception e){//Catch exception if any
	    	System.err.println("Error: " + e.getMessage());
	    }
		
	}
	
	
	/**
	 * Write the output to file.
	 *
	 * @param mol2File the mol2 file
	 * @param outputFile the output file
	 * @throws FileNotFoundException the file not found exception
	 * @throws CDKException the cDK exception
	 */
	public void write(File mol2File, File outputFile) throws FileNotFoundException, CDKException
	{
		Mol2Reader mol2 = new Mol2Reader(new FileReader(mol2File));
		ChemFile chemFile = (ChemFile)mol2.read((ChemObject)new ChemFile());
        List<IAtomContainer> containersList = ChemFileManipulator.getAllAtomContainers(chemFile);
        IAtomContainer mol = containersList.get(0);
		
		
		StringBuilder output = new StringBuilder();
		
		//first 3 lines
		output.append(this.keywords + "\n");
		output.append(outputFile.getPath() + "\n");
		output.append("\n");
		
		//now get the coordinates
		for (IAtom atom : mol.atoms())
		{
			String line = atom.getSymbol() + "   " + atom.getPoint3d().x + " 0 " + atom.getPoint3d().y + " 0 " + atom.getPoint3d().z + " 0    0 0 0 0";
			output.append(line + "\n");
		}
		
		try{
			// Create file 
			FileWriter fstream = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(output.toString() + "\n");
			//Close the output stream
			out.close();
	    }catch (Exception e){//Catch exception if any
	    	System.err.println("Error: " + e.getMessage());
	    }
		
	}
	
	public static void main(String[] args) {
		try {
			Mol2Reader mol2 = new Mol2Reader(new FileReader(new File("/home/swolf/bioclipse-workspace/MOPACStepByStep/FFUFF.mol2")));
			ChemFile chemFile = (ChemFile)mol2.read((ChemObject)new ChemFile());
	        List<IAtomContainer> containersList = ChemFileManipulator.getAllAtomContainers(chemFile);
	        IAtomContainer mol = containersList.get(0);
	        MOPACInputFormatWriter mopIn = new MOPACInputFormatWriter("PREC AM1 T=3600 GEO-OK, GNORM=0.1, MMOK, SCFCRT=1D-9");
	        mopIn.write(mol, new File("/home/swolf/bioclipse-workspace/MOPACStepByStep/FFUFF.dat"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CDKException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
