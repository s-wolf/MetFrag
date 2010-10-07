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

import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

import de.ipbhalle.metfrag.tools.Constants;

public class HydrogenCheck {
	
	public HydrogenCheck()
	{
		
	}
	
	public void matchFragments(double mass, double peakLow, double peakHigh, int treeDepth, IMolecularFormula molecularFormula, IAtomContainer fragment)
	{
        //now try to decrease or increase the hydrogens...at most the treedepth

    	for(int i= 0; i <= treeDepth; i++)
    	{
    		if(i==0)
    		{
    			//found
    			if(((mass) >= peakLow && (mass) <= peakHigh))
    			{
    				
    				//now add a bond energy equivalent to a H-C bond
    				this.hydrogenPenalty = 1;
    				
    				
    				found = true;
    				matchedMass = Math.round((mass)*10000.0)/10000.0;
    				
    				if(this.html)
    	        		this.molecularFormula = MolecularFormulaManipulator.getHTML(molecularFormula) + neutralLoss;
    	        	else
    	        		this.molecularFormula = MolecularFormulaManipulator.getString(molecularFormula) + neutralLoss;
    				
    				break;
    			}
    		}
    		else
    		{
    			double hMass = i * Constants.HYDROGEN_MASS;
    			
    			//found
    			if(((massToCompare - hMass) >= peakLow && (massToCompare - hMass) <= peakHigh))
    			{
    				found = true;
    				matchedMass = Math.round((massToCompare-hMass)*10000.0)/10000.0;
    				
    				//now add a bond energy equivalent to a H-C bond
    				if(mode == -1)
    					this.hydrogenPenalty = (i);
    				else
    					this.hydrogenPenalty = (i + 1);
    				
    				if(this.html)
    	        		this.molecularFormula = MolecularFormulaManipulator.getHTML(molecularFormula) + "-" + (i + 1) + "H" + neutralLoss;
    	        	else
    	        		this.molecularFormula = MolecularFormulaManipulator.getString(molecularFormula) + "-" + (i + 1) + "H" + neutralLoss;
    				
    				
    				break;
    			}
    			else if(((massToCompare + hMass) >= peakLow && (massToCompare + hMass) <= peakHigh))
    			{
    				found = true;
    				matchedMass = Math.round((massToCompare+hMass)*10000.0)/10000.0;
    				//now add a bond energy equivalent to a H-C bond
    				if(mode == 1)
    					this.hydrogenPenalty = (i);
    				else
    					this.hydrogenPenalty = (i + 1);
    				
    				
    				if(this.html)
    	        		this.molecularFormula = MolecularFormulaManipulator.getHTML(molecularFormula) + "+" + (i + 1) + "H" + neutralLoss;
    	        	else
    	        		this.molecularFormula = MolecularFormulaManipulator.getString(molecularFormula) + "+" + (i + 1) + "H" + neutralLoss;
    				
    				break;
    			}
    		}	
    	}
    }

}
