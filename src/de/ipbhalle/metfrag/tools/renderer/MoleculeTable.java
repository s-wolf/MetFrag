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
package de.ipbhalle.metfrag.tools.renderer;

import java.awt.GridLayout;
import java.util.Iterator;
import java.util.List;

import javax.swing.JPanel;

import org.openscience.cdk.Molecule;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.layout.StructureDiagramGenerator;

	
public class MoleculeTable extends JPanel {
        
    public MoleculeTable(List<IAtomContainer> atomContainers) {
        int w = 200;
        int h = 200;
        
        boolean first = true;
        StructureDiagramGenerator sdg = new StructureDiagramGenerator();
        for (IAtomContainer atomContainer : atomContainers) {
            
        	IMolecule mol = new Molecule(atomContainer);
        	sdg.setMolecule(mol);
            try {
                sdg.generateCoordinates();
                mol = sdg.getMolecule();
                
                if(first)
                {
                	this.add(new MoleculeCell(mol, w, h, "test", true));
                	first = false;
                }
                else
                	this.add(new MoleculeCell(mol, w, h, "test", false));
                
            } catch (Exception e) {}
        }
    }

}
