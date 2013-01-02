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
package de.ipbhalle.metfrag.tools;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

import de.ipbhalle.metfrag.spectrum.WrapperSpectrum;


public class MassBankData {
	
	private Vector<WrapperSpectrum> spectraSet;
	int currentNumber = 0;
	int spectraCount = 0;
	
	
	/**
	 * Stores a list of Spectra from the same compound and different collision energies. 
	 * The MassBank source files have to be the same folder as the specified one.
	 * 
	 * @param folder the folder
	 * @param file the file
	 */
	public MassBankData(String folder, String file)
	{
		spectraSet = new Vector<WrapperSpectrum>();
		WrapperSpectrum sw = new WrapperSpectrum(folder + file + ".txt");
		currentNumber++;
		spectraCount++;
		//get all spectra
		getAllCollisionEnergies(folder, sw);
	}
	
	/**
	 * Gets all the already read in spectra combined in a Vector
	 * 
	 * @return the spectra
	 */
	public Vector<WrapperSpectrum> getSpectra()
	{
		return spectraSet;
	}
	
	/**
	 * Preprocess a single file and get all measurements given an Accession number. 
	 * The other measurements are fetched by comparison of the InchI name
	 * 
	 * @param folder the folder
	 * @param sw a single spectra
	 */
	private void getAllCollisionEnergies(String folder, WrapperSpectrum sw)
	{
		//loop over all files in folder
		File f = new File(folder);
		File files[] = f.listFiles();
		Arrays.sort(files);
		
		//the already read in collision energy
		int currentCollisionEnergy = sw.getCollisionEnergy();
		//the current InchI
		String currentInchI = sw.getInchI();
		//Hashmap storing collision energy as key to spectrum
		HashMap<Integer, WrapperSpectrum> collisionEnergyToSpectrum = new HashMap<Integer, WrapperSpectrum>();
		collisionEnergyToSpectrum.put(currentCollisionEnergy, sw);
		
		for(int i=0; i<files.length; i++)
		{
			if(files[i].isFile())
			{
				WrapperSpectrum spectrum = new WrapperSpectrum(files[i].toString());
				
				//add spectra to the list
				if(spectrum.getInchI().equals(currentInchI) && currentCollisionEnergy != spectrum.getCollisionEnergy())
				{
					spectraCount++;
					collisionEnergyToSpectrum.put(spectrum.getCollisionEnergy(), spectrum);
				}
			}
		}
		Integer[] collisionEnergies = new Integer[collisionEnergyToSpectrum.size()];
		collisionEnergies = collisionEnergyToSpectrum.keySet().toArray(collisionEnergies);
		Arrays.sort(collisionEnergies);
		
		//now store the spectra sorted by their collision energies 
		for (Integer cEnergy : collisionEnergies) {
			spectraSet.add(collisionEnergyToSpectrum.get(cEnergy));
		}
		
	}
}
