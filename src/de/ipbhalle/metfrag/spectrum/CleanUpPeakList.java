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
