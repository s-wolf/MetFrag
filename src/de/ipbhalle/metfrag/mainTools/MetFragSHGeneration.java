package de.ipbhalle.metfrag.mainTools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;

import de.ipbhalle.metfrag.spectrum.WrapperSpectrum;

public class MetFragSHGeneration {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		//"/home/swolf/MOPAC/BATCH/jar/PreprocessMolecules.jar" "/home/swolf/MOPAC/ProofOfConcept/pubchem/" "/home/swolf/MOPAC/BATCH/sh/" 600 600
		
		String writePath = "/home/swolf/MOPAC/BATCH/MFsh/";
		String pathToSDF = "/home/swolf/MOPAC/Hill_PubChem_Formula/";
		String pathToJar = "/home/swolf/MOPAC/BATCH/jar/MetFragPreCalculated.jar";
		String pathToPreCalculatedCML = "/home/swolf/MOPAC/Hill_PubChem_Formula/pubchemClusteredMopac/mopac_600/";
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	    java.util.Date date = new java.util.Date();
		String dateString = dateFormat.format(date);
		
		if(!writePath.contains("sh/"))
		{
			System.err.println("Please use a folder structure that uses /.../.../sh/ at the end");
			System.exit(1);
		}
		
		//loop over all files in folder
		FileUtils.deleteDirectory(new File(writePath));
		new File(writePath).mkdirs();
		
		File[] spectraFolder = new File(pathToSDF).listFiles();
		Arrays.sort(spectraFolder);
		for (File file : spectraFolder) {
			
			if(!file.isFile())
				continue;
			
			String fileName = file.getName();
			int dotPos = fileName.indexOf(".");
			String extension = "";
			if(dotPos >= 0)
				extension = fileName.substring(dotPos);
			
			WrapperSpectrum spectrum = new WrapperSpectrum(file.toString());
			
			File f2 = new File(writePath + "sge_" + file.getName().split("\\.")[0] + ".sh"); 
			
			BufferedWriter out = new BufferedWriter(new FileWriter(f2));
			out.write("#!/bin/bash");
			out.newLine();
			//CID_3365_spectrum.txt 2011-02-02_16-00-00 /home/swolf/MOPAC/Hill_PubChem_Formula/pubchemClusteredMopac/mopac_600/C30H60N3O3/ 1
			out.write("java -Dproperty.file.path=" + pathToSDF + "config/" + " -Xms1500m -Xmx5500m -jar " + pathToJar + " \"" + file.getName() + "\" \"" + dateString + "\" \"" + new File(pathToPreCalculatedCML) + "/" + spectrum.getFormula() + "/\"");
		  	out.close();

		}
	}
}
