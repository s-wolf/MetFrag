package de.ipbhalle.metfrag.bondPrediction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.io.FileUtils;

public class MopacGridEngineSH {
	
	public static void main(String[] args) throws IOException {
		
		//"/home/swolf/MOPAC/BATCH/jar/PreprocessMolecules.jar" "/home/swolf/MOPAC/ProofOfConcept/pubchem/" "/home/swolf/MOPAC/BATCH/sh/" 600 600
		
		
//		String writePath = "/home/swolf/MOPAC/EMMATest/MMFF94ValidationSet_JAVA/sh/";
//		String pathToSDF = "/home/swolf/MOPAC/EMMATest/MMFF94ValidationSet_JAVA/data/";
//		String pathToJar = "/home/swolf/MOPAC/EMMATest/MMFF94ValidationSet_JAVA/jar/MopacGridEngine.jar";
		
		String writePath = "/home/swolf/MOPAC/EMMATest/1000_molec_JAVA/sh/";
		String pathToSDF = "/home/swolf/MOPAC/EMMATest/1000_molec_JAVA/data/";
		String pathToJar = "/home/swolf/MOPAC/EMMATest/1000_molec_JAVA/jar/MopacGridEngine.jar";
		String mopacRuntime = "2400";
		String ffSteps = "2400";
//		String ffMethod = "UFF";
//		String ffMethod = "MMFF94";
		String ffMethod = "Ghemical";
		String mopacParameters = "AM1, GEO-OK, ECHO, MMOK, XYZ";
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
        Date date = new Date();
        String dateString = dateFormat.format(date);
		
		if(args.length < 5)
		{
			System.err.println("Not all arguments given");
//				System.exit(1);
		}
		else
		{
			pathToJar = args[0];
			pathToSDF = args[1];
			writePath = args[2];
			mopacRuntime = args[3];
			ffSteps = args[4];
				
		}
		
		if(!writePath.contains("sh/"))
		{
			System.err.println("Please use a folder structure that uses /.../.../sh/ at the end");
			System.exit(1);
		}
		
		//loop over all files in folder
		FileUtils.deleteDirectory(new File(writePath));
		new File(writePath).mkdirs();
		
		String[] ext = {"sdf"};
		Collection<File> filesRecursively = (Collection<File>)FileUtils.listFiles(new File(pathToSDF), ext, true);
		for (File file : filesRecursively) {
			String fileName = file.getName();
			int dotPos = fileName.indexOf(".");
			String extension = "";
			if(dotPos >= 0)
				extension = fileName.substring(dotPos);

			File f2 = new File(writePath + "sge_" + file.getName().split("\\.")[0] + ".sh"); 
			
			BufferedWriter out = new BufferedWriter(new FileWriter(f2));
			out.write("#!/bin/bash");
			out.newLine();
			
//			/home/swolf/MOPAC/EMMATest/1000_molec_JAVA/data/1000_molec_try8_wM_END1.sdf.H.sdf 2011-08-01 /home/swolf/MOPAC/EMMATest/1000_molec_JAVA/log/test.txt UFF 2400 2400 "AM1, GEO-OK, ECHO, MMOK, XYZ, BONDS"
			out.write("java -jar " + pathToJar + " \"" + file.getPath() + "\"" +  " \"" + dateString + "\"" + " \"" + new File(writePath).getParent()  + "/log/mopac_" + mopacRuntime + "/" + ffMethod + "\" " + ffMethod + " " + ffSteps + " " + mopacRuntime + " \"" + mopacParameters + "\"");
//				out.write("java -jar " + pathToJar + " \"" + file.getPath() + "\"" + " \"" + new File(new File(file.getParent()).getParent()).getParent() + "/pubchemClusteredMopac/mopac_" + mopacRuntime + "/\" " + mopacRuntime + " " + ffSteps);
//		  		out.write("java -jar /home/swolf/MOPAC/BATCH/jar/PreprocessMolecules.jar \"" + file.getPath() + "\"" + " \"" + file.getParent() + "/mopac_1200/\"" + " 1200 600");
		  	out.close();

		}
	}
}
