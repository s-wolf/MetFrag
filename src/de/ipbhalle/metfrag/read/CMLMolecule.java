/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package de.ipbhalle.metfrag.read;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.CMLReader;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;

public class CMLMolecule {
	
	private String fileName;
	private File molFile;
	
	/**
	 * Instantiates a new VML molecule.
	 *
	 * @param molFile the mol file
	 * @param fileName the file name
	 */
	public CMLMolecule(File molFile, String fileName)
	{
		setFileName(fileName);
		setMolFile(molFile);
	}

	/**
	 * Sets the file name.
	 *
	 * @param fileName the new file name
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Gets the file name.
	 *
	 * @return the file name
	 */
	public String getFileName() {
		return fileName;
	}
	
	/**
	 * Gets the mol.
	 *
	 * @return the mol
	 * @throws FileNotFoundException 
	 * @throws CDKException 
	 */
	public IAtomContainer getMolStructure() throws FileNotFoundException, CDKException {
		CMLReader reader = new CMLReader(new FileInputStream(this.molFile));
        ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());
        List<IAtomContainer> containersList;
		List<CMLMolecule> ret = new ArrayList<CMLMolecule>();
        containersList = ChemFileManipulator.getAllAtomContainers(chemFile);
        return containersList.get(0);
	}

	public void setMolFile(File molFile) {
		this.molFile = molFile;
	}

	public File getMolFile() {
		return molFile;
	}

}
