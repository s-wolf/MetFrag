package de.ipbhalle.metfrag.mainTools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

import de.ipbhalle.metfrag.spectrum.WrapperSpectrum;

public class MetFragSHGeneration {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		//"/home/swolf/MOPAC/BATCH/jar/PreprocessMolecules.jar" "/home/swolf/MOPAC/ProofOfConcept/pubchem/" "/home/swolf/MOPAC/BATCH/sh/" 600 600
		
//		String writePath = "/home/swolf/MOPAC/BondOrderTests/Hill_ProofOfConcept/MFsh/";
//		String pathToSpectra = "/home/swolf/MOPAC/BondOrderTests/Hill_ProofOfConcept/testData/";
//		String pathToJar = "/home/swolf/MOPAC/BondOrderTests/Hill_ProofOfConcept/jar/MetFragPreCalculated.jar";
//		String pathToPreCalculatedCML = "/home/swolf/MOPAC/BondOrderTests/Hill_ProofOfConcept/mopac_2400/";
		
		String writePath = "/home/swolf/MOPAC/Hill-Riken-MM48_POSITIVE_PubChem_LocalMass2009_CHONPS_NEW/MFPubChem2006sh/";
		String pathToSpectra = "/home/swolf/MOPAC/Hill-Riken-MM48_POSITIVE_PubChem_LocalMass2009_CHONPS_NEW/spectra/";
		String pathToJar = "/home/swolf/MOPAC/Hill-Riken-MM48_POSITIVE_PubChem_LocalMass2009_CHONPS_NEW/jar/MetFragPreCalculated_2006PubChemFINAL.jar";
		String pathToPreCalculatedCML = "/home/swolf/MOPAC/Hill-Riken-MM48_POSITIVE_PubChem_LocalMass2009_CHONPS_NEW/mopac_4800/";
		String configPath = "/home/swolf/MOPAC/Hill-Riken-MM48_POSITIVE_PubChem_LocalMass2009_CHONPS_NEW/config/";
		
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
		
		File[] spectraFolder = new File(pathToSpectra).listFiles();
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
						
			List<double[]> params = new ArrayList<double[]>();
			
//			#Parameters
//			paramA = 
//			paramB =
//			paramC = 

			double[] set1 = new double[]{0.7933814634,0.6342220326,0.231636172};
			double[] set2 = new double[]{0.7672403027,0.4027641232,0.5131069369};
			double[] set3 = new double[]{0.6488030305,0.711347627,0.2763659724};
			double[] set4 = new double[]{0.759930967,0.5927900091,0.3299290679};
			double[] set5 = new double[]{0.8055460379,0.503569136,0.3110833622};
			double[] set6 = new double[]{0.9137227733,0.0534295769,0.4361120749};
			double[] set7 = new double[]{0.8761753887,0.8004489961,0.1582160573};
			double[] set8 = new double[]{0.9508089622,0.4686835434,0.2935260068};
			double[] set9 = new double[]{0.9581691938,0.4491647801,0.1920923441};
			double[] set10 = new double[]{0.9740953205,0.9173382645,0.1406057389};
			
			params.add(set1);
			params.add(set2);
			params.add(set3);
			params.add(set4);
			params.add(set5);
			params.add(set6);
			params.add(set7);
			params.add(set8);
			params.add(set9);
			params.add(set10);
			
			
			
			BufferedWriter out = new BufferedWriter(new FileWriter(f2));
			out.write("#!/bin/bash");
			out.newLine();
			//CID_3365_spectrum.txt 2011-02-02_16-00-00 /home/swolf/MOPAC/Hill_PubChem_Formula/pubchemClusteredMopac/mopac_600/C30H60N3O3/ 1
//			out.write("java -Dproperty.file.path=" + pathToSpectra + "config/" + " -Xms1500m -Xmx5500m -jar " + pathToJar + " \"" + file.getPath() + "\" \"" + dateString + "\" \"" + new File(pathToPreCalculatedCML) + "/" + spectrum.getFormula().trim() + "/\"");
			out.write("java -Dproperty.file.path=" + configPath + getSet(file) + "/ -Xms1500m -Xmx5500m -jar " + pathToJar + " \"" + file.getPath() + "\" \"" + dateString + "\" \"" + new File(pathToPreCalculatedCML) + "/\"");
		  	out.close();

		}
	}
	
	public static String getSet(File file)
	{
		String test = "/home/swolf/CrossValidation/data/TestSet";
		for (int k = 1; k < 11; k++) {
			
			File testFiles = new File(test + k);
			File[] testFolder = testFiles.listFiles();

			Arrays.sort(testFolder);
			for (int j = 0; j < testFolder.length; j++) {
				if(file.getName().split("\\.")[0].equals(testFolder[j].getName()))
					return (k) + "";
			}	
		}

		return "notFound";
	}
}
