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
package de.ipbhalle.metfrag.bondPrediction;

import org.openscience.cdk.interfaces.IAtom;

public class AtomProperty implements Comparable{
	
	private IAtom atom;
	private Double charge;
	
	/**
	 * Instantiates a new atom property.
	 * 
	 * @param atom the atom
	 * @param charge the charge
	 */
	public AtomProperty(IAtom atom, Double charge)
	{
		this.atom = atom;
		this.charge = charge;
	}
	
	
	public int compareTo(Object o1) 
	{
        if (this.charge == ((AtomProperty) o1).charge)
            return 0;
        else if ((this.charge) > ((AtomProperty) o1).charge)
            return 1;
        else
            return -1;
    }
	
	public IAtom getAtom()
	{
		return atom;
	}
	
	public Double getCharge()
	{
		return charge;
	}

}
