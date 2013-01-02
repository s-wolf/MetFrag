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

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;


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
