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
		
		String writePath = "/home/swolf/MOPAC/BATCH/sh/";
		
		String path = "/home/swolf/MOPAC/ProofOfConcept/pubchem/";
		//loop over all files in folder
		FileUtils.deleteDirectory(new File(writePath));
		new File(writePath).mkdirs();
		
		String[] ext = {"sdf"};
		Collection<File> filesRecursively = (Collection<File>)FileUtils.listFiles(new File(path), ext, true);
		for (File file : filesRecursively) {
			String fileName = file.getName();
			int dotPos = fileName.indexOf(".");
			String extension = "";
			if(dotPos >= 0)
				extension = fileName.substring(dotPos);

			File f2 = new File(writePath + "sge_" + file.getName() + ".sh"); 
			
			BufferedWriter out = new BufferedWriter(new FileWriter(f2));
			out.write("#!/bin/bash");
			out.newLine();
	  		out.write("java -jar /home/swolf/MOPAC/BATCH/jar/PreprocessMolecules.jar \"" + file.getPath() + "\"" + " \"" + file.getParent() + "/mopac_600/\"" + " 1200 600");
		  	out.close();

		}

	}


}
