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
package de.ipbhalle.metfrag.read;

import org.openscience.cdk.io.MDLReader;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;
import java.io.*;
import java.util.*;

public class Molfile {	
	
	/**
	 * Read Molecule file (.mol) from specified Path.
	 * 
	 * @param file the file
	 * 
	 * @return the molecule
	 * 
	 * @throws CDKException the CDK exception
	 * @throws FileNotFoundException the file not found exception
	 */
	public static IAtomContainer Read (String file) throws CDKException, FileNotFoundException
	{
		MDLReader reader;
		List<IAtomContainer> containersList;
		
        reader = new MDLReader(new FileReader(new File(file)));
        ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());
        containersList = ChemFileManipulator.getAllAtomContainers(chemFile);
        
        IAtomContainer mol = containersList.get(0);
        return mol;
	        
	}
	
	
	/**
	 * Read a mol file from a given string.
	 * 
	 * @param molString the mol string
	 * 
	 * @return the i atom container
	 * 
	 * @throws CDKException the CDK exception
	 * @throws FileNotFoundException the file not found exception
	 * @throws UnsupportedEncodingException the unsupported encoding exception
	 */
	public static IAtomContainer ReadFromString(String molString) throws CDKException, FileNotFoundException, UnsupportedEncodingException
	{
		MDLV2000Reader reader;
		List<IAtomContainer> containersList;
		
        reader = new MDLV2000Reader(new ByteArrayInputStream(molString.getBytes("UTF-8")));
        ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());
        containersList = ChemFileManipulator.getAllAtomContainers(chemFile);
        
        IAtomContainer mol = containersList.get(0);
        return mol;
	        
	}
	
	
	

	
	/**
	 * Read files from specified folder.
	 * 
	 * @param the folder to read from
	 * 
	 * @return atom container set
	 * 
	 * @throws CDKException the CDK exception
	 * @throws FileNotFoundException the file not found exception
	 */
	public static List<IAtomContainer> Readfolder (String folder) throws CDKException, FileNotFoundException
	{
		MDLV2000Reader reader;
		List<IAtomContainer> containersList;
		List<IAtomContainer> ret = new ArrayList<IAtomContainer>();
		
		File f = new File(folder);
		File files[] = f.listFiles();

		for(int i=0;i<files.length;i++)
		{
			if(files[i].isFile())
			{
				reader = new MDLV2000Reader(new FileReader(files[i]));
		        ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());
		        containersList = ChemFileManipulator.getAllAtomContainers(chemFile);
		        ret.add(containersList.get(0)); //one container per file
			}
		}
        return ret;    
	}
	
	
	/**
	 * Read files from temp folder.
	 * 
	 * @param fragments the fragments
	 * 
	 * @return the i atom container set
	 * 
	 * @throws CDKException the CDK exception
	 * @throws FileNotFoundException the file not found exception
	 */
	public static List<IAtomContainer> ReadfolderTemp (Vector<File> fragments) throws CDKException, FileNotFoundException
	{
		MDLV2000Reader reader;
		List<IAtomContainer> containersList;
		List<IAtomContainer> ret = new ArrayList<IAtomContainer>();
		

		for(int i = 0; i < fragments.size(); i++)
		{
			reader = new MDLV2000Reader(new FileReader(fragments.get(i)));
	        ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());
	        containersList = ChemFileManipulator.getAllAtomContainers(chemFile);
	        ret.add(containersList.get(0)); //one container per file
		}
        return ret;    
	}
	
	
	/**
	 * Read files from temp folder.
	 * 
	 * @param fragments the fragments
	 * 
	 * @return the i atom container set
	 * 
	 * @throws CDKException the CDK exception
	 * @throws FileNotFoundException the file not found exception
	 */
	public static List<IAtomContainer> ReadfolderTemp (List<File> fragments) throws CDKException, FileNotFoundException
	{
		MDLV2000Reader reader;
		List<IAtomContainer> containersList;
		List<IAtomContainer> ret = new ArrayList<IAtomContainer>();
		

		for(int i = 0; i < fragments.size(); i++)
		{
			reader = new MDLV2000Reader(new FileReader(fragments.get(i)));
	        ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());
	        containersList = ChemFileManipulator.getAllAtomContainers(chemFile);
	        ret.add(containersList.get(0)); //one container per file
		}
        return ret;    
	}
	
	/**
	 * Deletes the temp files.
	 * 
	 * @param fragments the fragments
	 * 
	 */
	public static void DeleteFilesTemp (Vector<File> fragments)
	{
		for(int i = 0; i < fragments.size(); i++)
		{
			fragments.get(i).delete();
		}  
	}
}
