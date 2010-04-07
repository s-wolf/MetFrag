package de.ipbhalle.metfrag.tools.renderer;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.templates.MoleculeFactory;

public class MoleculeTableJFrame extends JFrame {
	
	private MoleculeTable table;
    
    public MoleculeTableJFrame(List<IAtomContainer> atomContainers) {
        this.table = new MoleculeTable(atomContainers);
        //JScrollPane pane = new JScrollPane(this.table);
        

        
        this.setTitle("MetFrag Fragments");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        
        JPanel spane = new JPanel(new GridLayout(0, 4), true);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		
		for (Component comp : table.getComponents()) {
			spane.add(comp);
		}
		//this.add(spane);
		JScrollPane scrollpane = new JScrollPane(spane,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.getContentPane().add(scrollpane);
		
    }
    
    public static void main(String[] args) {
    	List<IAtomContainer> containers = new ArrayList<IAtomContainer>();
        containers.add(MoleculeFactory.make123Triazole());
        containers.add(MoleculeFactory.makeCyclobutadiene());
        containers.add(MoleculeFactory.makeTetrahydropyran());
        containers.add(MoleculeFactory.makeTetrahydropyran());
        containers.add(MoleculeFactory.makeTetrahydropyran());
        containers.add(MoleculeFactory.make123Triazole());
        containers.add(MoleculeFactory.makeCyclobutadiene());
        containers.add(MoleculeFactory.makeTetrahydropyran());
        containers.add(MoleculeFactory.makeTetrahydropyran());
        containers.add(MoleculeFactory.makeTetrahydropyran());
        containers.add(MoleculeFactory.make123Triazole());
        containers.add(MoleculeFactory.makeCyclobutadiene());
        containers.add(MoleculeFactory.makeTetrahydropyran());
        containers.add(MoleculeFactory.makeTetrahydropyran());
        containers.add(MoleculeFactory.makeTetrahydropyran());
        containers.add(MoleculeFactory.make123Triazole());
        containers.add(MoleculeFactory.makeCyclobutadiene());
        containers.add(MoleculeFactory.makeTetrahydropyran());
        containers.add(MoleculeFactory.makeTetrahydropyran());
        containers.add(MoleculeFactory.makeTetrahydropyran());
        containers.add(MoleculeFactory.make123Triazole());
        containers.add(MoleculeFactory.makeCyclobutadiene());
        containers.add(MoleculeFactory.makeTetrahydropyran());
        containers.add(MoleculeFactory.makeTetrahydropyran());
        containers.add(MoleculeFactory.makeTetrahydropyran());
        containers.add(MoleculeFactory.make123Triazole());
        containers.add(MoleculeFactory.makeCyclobutadiene());
        containers.add(MoleculeFactory.makeTetrahydropyran());
        containers.add(MoleculeFactory.makeTetrahydropyran());
        containers.add(MoleculeFactory.makeTetrahydropyran());
        
        MoleculeTableJFrame example = new MoleculeTableJFrame(containers);
        
        example.pack();
        example.setVisible(true);
	}

}
