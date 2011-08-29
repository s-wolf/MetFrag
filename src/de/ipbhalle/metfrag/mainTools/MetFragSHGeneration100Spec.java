package de.ipbhalle.metfrag.mainTools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;

public class MetFragSHGeneration100Spec {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		int treeDepth = 2;
		
		try
		{
			//thats the current file 
			if(args[0] != null)
			{
				treeDepth = Integer.parseInt(args[0]);
			}
		}
		catch(Exception e)
		{
			System.out.println("No tree depth specified! Using standard (2)");
		}
		
		//java -Xms1500m -Xmx4048m -Dproperty.file.path=/vol/local/lib/MetFrag/MetFragPaper/ -jar /vol/local/lib/MetFrag/MetFragPaper/MetFragBatchScoringPaperSDF_Scoring2_HydrogenCheck.jar /vol/data_extern/emma.schymanski@ufz.de/ufzleipzig/SpecificExamples/C12H10O2_01.mf /home/swolf/MOPAC/EMMATest/MetFragTests/results/C12H10O2_01.mf 0.4 10 SDF /home/swolf/MOPAC/EMMATest/MetFragTests/SpecificExamples/C12H10O2_isomers_corr27.sdf.H.sdf

		
		//"/home/swolf/MOPAC/BATCH/jar/PreprocessMolecules.jar" "/home/swolf/MOPAC/ProofOfConcept/pubchem/" "/home/swolf/MOPAC/BATCH/sh/" 600 600
		
		String writePath = "/home/swolf/MOPAC/EMMATest/MetFragTests/sge100/";
		String pathToSpectra = "/home/swolf/MOPAC/EMMATest/MetFragTests/100spec/";
		String pathToJar = "/vol/local/lib/MetFrag/MetFragPaper/MetFragBatchScoringPaperSDF_Scoring2_HydrogenCheck.jar";
				
//		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
//	    java.util.Date date = new java.util.Date();
//		String dateString = dateFormat.format(date);
		
		if(!writePath.contains("sge100/"))
		{
			System.err.println("Please use a folder structure that uses /.../.../sh/ at the end");
			System.exit(1);
		}
		
		//loop over all files in folder
		FileUtils.deleteDirectory(new File(writePath));
		new File(writePath).mkdirs();
		
		Map<String, File> mapNumberToFileSpectra = new HashMap<String, File>();
		Map<String, File> mapNumberToFileStructures = new HashMap<String, File>();
		
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
			
			//System.out.println("Extension: " + extension);
			
			if(extension.equals(".mf"))
			{				
				System.out.println("MF: " + file.getName() + " Number: " + file.getName().substring(18, 21));
				mapNumberToFileSpectra.put(file.getName().substring(18, 21), file);
			}

			if(extension.equals(".sdf.H.sdf"))
			{		
				System.out.println("SDF: " + file.getName() + " Number: " + file.getName().substring(0, 3));
				mapNumberToFileStructures.put(file.getName().substring(0, 3), file);
			}
		}
		
		for (String number : mapNumberToFileSpectra.keySet()) {
			File fileMF = mapNumberToFileSpectra.get(number);
			File fileSDF = mapNumberToFileStructures.get(number);
			File f2 = new File(writePath + "sge_" + mapNumberToFileSpectra.get(number).getName().split("\\.")[0] + ".sh"); 
			System.out.println("Out filename: " + f2.toString());
			BufferedWriter out = new BufferedWriter(new FileWriter(f2));
			out.write("#!/bin/bash");
			out.newLine();
			///vol/data_extern/emma.schymanski@ufz.de/ufzleipzig/SpecificExamples/C12H10O2_01.mf /home/swolf/MOPAC/EMMATest/MetFragTests/results/C12H10O2_01.mf 0.4 10 SDF /home/swolf/MOPAC/EMMATest/MetFragTests/SpecificExamples/C12H10O2_isomers_corr27.sdf.H.sdf
			out.write("java -Dproperty.file.path=/vol/local/lib/MetFrag/MetFragPaper/" + " -Xms1500m -Xmx5500m -jar " + pathToJar + " \"" + fileMF.getPath().toString() + "\" " + "\"/home/swolf/MOPAC/EMMATest/MetFragTests/results100/" + fileMF.getName() + "\" 0.4 10 SDF " + fileSDF.getPath().toString() + " " + treeDepth);
		  	out.close();

		}
		
	}
}