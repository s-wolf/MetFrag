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

import junit.framework.Assert;

import org.junit.Test;

public class WrapperSpectrumTest {
	
	WrapperSpectrum spectrum = null;
	
	public WrapperSpectrumTest() {
		double exactMass = 272.06847;
		String peaks = "153.019    10000.0    999\n" +
				"273.076  10000.000   999\n" +
				"274.083   318.003   30\n" +
				"119.051 467.616 45\n" +
				"123.044 370.662 36\n" +
				"147.044 6078.145 606\n" +
				"179.036 141.192 13\n" +
				"189.058 176.358 16\n";
		int mode = 1;

		spectrum = new WrapperSpectrum(peaks, mode, exactMass, true);	
	}
	
	
	@Test
	public void peaksParserTest()
	{
		Assert.assertEquals(8, spectrum.getPeakList().size());
	}
	
	
	@Test
	public void peaksParserMassTest()
	{
		Assert.assertEquals(272.06847, spectrum.getExactMass());
	}
	
	
	@Test
	public void peaksParserCommaSeperatorTest()
	{
		double exactMass = 272.06847;
		String peaks = "153,019    10000,0    999\n" +
				"143,019 10000,0    999\n";
		int mode = 1;

		spectrum = new WrapperSpectrum(peaks, mode, exactMass, true);	
		Assert.assertEquals(2, spectrum.getPeakList().size());
	}
	
	@Test
	public void peaksParserCommaSeperatorTest1()
	{
		double exactMass = 272.06847;
		String peaks = "153,019    10000,0    999\n";
		int mode = 1;

		spectrum = new WrapperSpectrum(peaks, mode, exactMass, true);
		Assert.assertEquals(153.019, spectrum.getPeakList().firstElement().getMass());
	}
	
	@Test
	public void peaksParserCommaSeperatorTest2()
	{
		double exactMass = 272.06847;
		String peaks = "153,019    10000,0    999\n";
		int mode = 1;

		spectrum = new WrapperSpectrum(peaks, mode, exactMass, true);
		Assert.assertEquals(10000.0, spectrum.getPeakList().firstElement().getIntensity());
	}
	
	
	@Test
	public void peaksParserCommaSeperatorTest3()
	{
		double exactMass = 272.06847;
		String peaks = "153,019    10000,0    999\n";
		int mode = 1;

		spectrum = new WrapperSpectrum(peaks, mode, exactMass, true);
		Assert.assertEquals(999.0, spectrum.getPeakList().firstElement().getRelIntensity());
	}
	
	

}
