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
package de.ipbhalle.metfrag.main;

import java.util.Vector;

public class BlackList {
	
	private Vector<String> blackList;
	
	
	/**
	 * Instantiates a new black list.
	 * 
	 * @param sumFormulaRedundancy the sum formula redundancy
	 */
	public BlackList(boolean sumFormulaRedundancy)
	{
		this.blackList = new Vector<String>();	
		
		if(sumFormulaRedundancy)
		{	
			//blackList.add("PB000745PB000746PB000781PB000782"); //too much time for non experimental
//			blackList.add("PB000783"); //too many fragments and takes too long
//			blackList.add("PB000803");
//			blackList.add("PB001341"); //too many fragments and takes too long
			//blackList.add("PB001329PB001330PB001331PB001332"); //too long for non experimental
			//blackList.add("PB001337PB001338PB001339PB001340"); //too long for non experimental
			blackList.add("PB000902"); //no peak to identify
			
			//new live merged spectra
			//blackList.add("PB000745PB000746PB000781PB000782_1"); //too much time for non experimental
//			blackList.add("PB000783PB000784PB000785PB000801PB000802"); //too many fragments and takes too long
//			blackList.add("PB000803PB000804"); //same as above
//			blackList.add("PB001341PB001342PB001343PB001344"); //too many fragments and takes too long
			//blackList.add("PB001329PB001330PB001331PB001332_1"); //too long for non experimental
			//blackList.add("PB001337PB001338PB001339PB001340_1"); //too long for non experimental
			blackList.add("PB000902"); //no peak to identify
			
			//hill paper
			//blackList.add("Hematoporphyrin_I_10Hematoporphyrin_I_20Hematoporphyrin_I_30Hematoporphyrin_I_40Hematoporphyrin_I_50");
		}
		else
		{			
			//new live merged spectra
//			blackList.add("PB000745PB000746PB000781PB000782"); //too much time for non experimental
//			blackList.add("PB000783PB000784PB000785PB000801PB000802"); //too many fragments and takes too long
//			blackList.add("PB000803PB000804"); //same as above
//			blackList.add("PB001341PB001342PB001343PB001344"); //too many fragments and takes too long
//			blackList.add("PB001329PB001330PB001331PB001332"); //too long for non experimental
//			blackList.add("PB001337PB001338PB001339PB001340"); //too long for non experimental
			blackList.add("PB000902"); //no peak to identify
			
		}

	}
	
	public Vector<String> getBlackList()
	{
		return this.blackList;
	}
	
}
