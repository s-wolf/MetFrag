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
