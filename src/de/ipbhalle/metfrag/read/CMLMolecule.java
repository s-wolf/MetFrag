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

import org.openscience.cdk.interfaces.IAtomContainer;

public class CMLMolecule {
	
	private IAtomContainer mol;
	private String fileName;
	
	/**
	 * Instantiates a new VML molecule.
	 *
	 * @param mol the mol
	 * @param fileName the file name
	 */
	public CMLMolecule(IAtomContainer mol, String fileName)
	{
		setFileName(fileName);
		setMol(mol);
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
	 */
	public IAtomContainer getMol() {
		return mol;
	}

	/**
	 * Sets the mol.
	 *
	 * @param mol the new mol
	 */
	public void setMol(IAtomContainer mol) {
		this.mol = mol;
	}

}
