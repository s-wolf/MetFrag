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
