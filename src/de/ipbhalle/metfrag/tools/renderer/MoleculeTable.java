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
