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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.CMLReader;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;

public class CMLTools {
	
	/**
	 * Read all cml files in given folder and return a List.
	 * file extension has to be .cml!
	 *
	 * @param folder the folder
	 * @return the list
	 * @throws FileNotFoundException 
	 * @throws CDKException 
	 */
	public static List<CMLMolecule> readFolder(File folder) throws FileNotFoundException, CDKException
	{
		CMLReader reader;
		List<IAtomContainer> containersList;
		List<CMLMolecule> ret = new ArrayList<CMLMolecule>();
		
		File files[] = folder.listFiles();

		for(int i=0;i<files.length;i++)
		{
			int dotPos = files[i].getName().lastIndexOf(".");
		    String extension = files[i].getName().substring(dotPos);
			if(files[i].isFile() && extension.equals(".cml") && files[i].getName().contains("Combined"))
			{
		        ret.add(new CMLMolecule(files[i], files[i].getName())); //one container per file
			}
		}
        return ret;  
	}
	
	/**
	 * Read single molecule from cml file.
	 *
	 * @param file the file
	 * @return the list
	 * @throws FileNotFoundException the file not found exception
	 * @throws CDKException the cDK exception
	 */
	public static IAtomContainer read(File file) throws FileNotFoundException, CDKException
	{
		CMLReader reader;	
		reader = new CMLReader(new FileInputStream(file));
		ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());
		List<IAtomContainer> containersList = ChemFileManipulator.getAllAtomContainers(chemFile);
		return containersList.get(0);
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
		CMLReader reader;
		List<IAtomContainer> containersList;
		List<IAtomContainer> ret = new ArrayList<IAtomContainer>();
		

		for(int i = 0; i < fragments.size(); i++)
		{
			reader = new CMLReader(new FileInputStream(fragments.get(i)));
	        ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());
	        containersList = ChemFileManipulator.getAllAtomContainers(chemFile);
	        ret.add(containersList.get(0)); //one container per file
		}
        return ret;    
	}

}
