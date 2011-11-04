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
package de.ipbhalle.metfrag.tools;

public class PPMTool {
	
	/**
	 * Gets the PPM for a specified peak (m/z).
	 * 
	 * @param peak the peak
	 * @param ppm the ppm
	 * 
	 * @return the allowed error with the given ppm
	 */
	public static double getPPMDeviation(double peak, double ppm)
	{
		double ret = 0.0;
		//calculate the allowed error for the given peak m/z in Th
		ret = (peak/1000000.0) * ppm;
		return ret;
	}
	
	
	/**
	 * Gets the PPM deviation.
	 * 
	 * @param peak the peak
	 * @param measuredPeak the measured peak (calculated exact mass)
	 * 
	 * @return the pPM
	 */
	public static double getPPMWeb(double peak, double measuredPeak)
	{
		double ret = 0.0;
		//calculate the allowed error for the given peak m/z in Th
		ret = Math.round((((peak-measuredPeak)/measuredPeak) * 1000000.0) * 10.0)/10.0;
		return ret;
	}

}
