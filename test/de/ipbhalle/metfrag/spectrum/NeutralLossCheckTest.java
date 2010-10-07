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

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import de.ipbhalle.metfrag.fragmenter.NeutralLoss;

public class NeutralLossCheckTest {
	
	NeutralLossCheck nlc;
	
	public NeutralLossCheckTest()
	{
		nlc = new NeutralLossCheck(3);
	}
	
	/**
	 * Neutral loss list combination test. Just test for the number of combinations generated
	 */
	@Test
	public void neutralLossListCombinationTest()
	{
		Assert.assertEquals(6, nlc.getNeutralLossCombinations().get(0).size());
		Assert.assertEquals(21, nlc.getNeutralLossCombinations().get(1).size());
		Assert.assertEquals(56, nlc.getNeutralLossCombinations().get(2).size());
	}
}
