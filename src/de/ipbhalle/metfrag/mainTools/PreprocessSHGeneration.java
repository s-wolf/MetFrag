package de.ipbhalle.metfrag.mainTools;


/*
*
* Copyright (C) 2009-2010 IPB Halle, Sebastian Wolf
*
* Contact: swolf@ipb-halle.de
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*
*/
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;


public class PreprocessSHGeneration {
	

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		
		//"/home/swolf/MOPAC/BATCH/jar/PreprocessMolecules.jar" "/home/swolf/MOPAC/ProofOfConcept/pubchem/" "/home/swolf/MOPAC/BATCH/sh/" 600 600
		
//		String writePath = "/home/swolf/MOPAC/BondOrderTests/Hill_ProofOfConcept/sh/";
//		String pathToSDF = "/home/swolf/MOPAC/ProofOfConcept/Hill_OnlyCorrect/";
//		String pathToJar = "/home/swolf/MOPAC/BondOrderTests/Hill_ProofOfConcept/jar/PreprocessMolecules.jar";
		
		String writePath = "/home/swolf/MOPAC/Hill-Riken-MM48_POSITIVE_PubChem_LocalMass2009_CHONPS_NEW/sh/";
		String pathToSDF = "/home/swolf/MOPAC/Hill-Riken-MM48_POSITIVE_PubChem_LocalMass2009_CHONPS_NEW/pubchem/CO000021CO000022CO000023CO000024CO000025";
		String pathToJar = "/home/swolf/MOPAC/Hill-Riken-MM48_POSITIVE_PubChem_LocalMass2009_CHONPS_NEW/jar/PreprocessMolecules.jar";
			
		
		String mopacRuntime = "4800";
		String ffSteps = "4800";
		
		if(args.length < 5)
		{
			System.err.println("Not all arguments given");
//			System.exit(1);
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
			out.write("java -jar " + pathToJar + " \"" + file.getPath() + "\"" + " \"" + new File(writePath).getParent()  + "/mopac_" + mopacRuntime + "/\" " + mopacRuntime + " " + ffSteps);
//			out.write("java -jar " + pathToJar + " \"" + file.getPath() + "\"" + " \"" + new File(new File(file.getParent()).getParent()).getParent() + "/pubchemClusteredMopac/mopac_" + mopacRuntime + "/\" " + mopacRuntime + " " + ffSteps);
//	  		out.write("java -jar /home/swolf/MOPAC/BATCH/jar/PreprocessMolecules.jar \"" + file.getPath() + "\"" + " \"" + file.getParent() + "/mopac_1200/\"" + " 1200 600");
		  	out.close();

		}
	}
}
