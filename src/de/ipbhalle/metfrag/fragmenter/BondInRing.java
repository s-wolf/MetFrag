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
package de.ipbhalle.metfrag.fragmenter;

import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IRing;

public class BondInRing {
	
	private IBond bond = null;
	private IRing ring = null;
	
	/**
	 * Creates a new bond - ring pair.
	 * 
	 * @param bond the bond
	 * @param ring the ring
	 */
	public BondInRing(IBond bond, IRing ring)
	{
		this.bond = bond;
		this.ring = ring;
	}
	
	
	/**
	 * Gets the bond.
	 * 
	 * @return the bond
	 */
	public IBond getBond()
	{
		return this.bond;
	}
	
	/**
	 * Gets the ring.
	 * 
	 * @return the ring
	 */
	public IRing getRing()
	{
		return this.ring;
	}

}
