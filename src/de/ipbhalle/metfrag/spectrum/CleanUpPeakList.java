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
package de.ipbhalle.metfrag.spectrum;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import de.ipbhalle.metfrag.massbankParser.Peak;

public class CleanUpPeakList {
	
	private Vector<Peak> peakList;
	
	/**
	 * Instantiates a new clean up peak list.
	 * 
	 * @param peakList the peak list
	 */
	public CleanUpPeakList(Vector<Peak> peakList)
	{
		this.peakList = peakList;
	}
	
	/**
	 * Gets the cleaned peak list.
	 * 
	 * @return the cleaned peak list
	 */
	public Vector<Peak> getCleanedPeakList(double mass)
	{
		removeHeavyPeaks(mass);
		return this.peakList;
	}
	
	/**
	 * Removes the peaks which correspond to the original molecule. (molecule peak)
	 * 
	 * @param mass the mass
	 */
	private void removeHeavyPeaks(double mass)
	{
		List<Peak> toRemove = new ArrayList<Peak>();
		for (int i = 0; i < peakList.size(); i++) {
			if(peakList.get(i).getMass() >= mass)
			{
				toRemove.add(peakList.get(i));
			}
		}
		
		//now remove the peaks
		for (Peak peak : toRemove) {
			peakList.remove(peak);
		}
	}

}
