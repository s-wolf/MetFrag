package de.ipbhalle.metfrag.tools.renderer;

import java.util.List;

import javax.swing.JPanel;

import org.openscience.cdk.Molecule;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.layout.StructureDiagramGenerator;

import de.ipbhalle.metfrag.spectrum.MatchedFragment;


	
public class MoleculeTableHits extends JPanel {
    
    public MoleculeTableHits(List<MatchedFragment> matchedFragments) {
        int w = 200;
        int h = 200;
        
        boolean first = true;
        StructureDiagramGenerator sdg = new StructureDiagramGenerator();
        for (MatchedFragment frag : matchedFragments) {
            
        	IMolecule mol = new Molecule(frag.getFragmentStructure());
        	sdg.setMolecule(mol);
            try {
                sdg.generateCoordinates();
                mol = sdg.getMolecule();
                
                if(first)
                {
                	this.add(new MoleculeCellHits(frag, mol, w, h, "test", true));
                	first = false;
                }
                else
                	this.add(new MoleculeCellHits(frag, mol, w, h, "test", false));
                
            } catch (Exception e) {}
        }
    }

}

