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

public class BondPair {
	
	private int count = 0;
	private IBond bond1 = null;
	private IBond bond2 = null;
	
	public BondPair(IBond bond1, IBond bond2)
	{
		count = 2;
		this.bond1 = bond1;
		this.bond2 = bond2;
	}
	
	public BondPair(IBond bond1)
	{
		count = 1;
		this.bond1 = bond1;
	}
	
	public int getCount()
	{
		return this.count;
	}
	
	public IBond getBond1()
	{
		return this.bond1;
	}
	
	public IBond getBond2()
	{
		return this.bond2;
	}
	
	public boolean checkBonds(IBond bond1, IBond bond2)
	{
		boolean found = false;
		if ((this.bond1 == bond1 || this.bond2 == bond1) && (this.bond1 == bond2 || this.bond2 == bond2))
			found = true;
			
		return found;
	}
	
	public boolean checkBond(IBond bond1)
	{
		boolean found = false;
		if (this.bond1 == bond1)
			found = true;
			
		return found;
	}

}
