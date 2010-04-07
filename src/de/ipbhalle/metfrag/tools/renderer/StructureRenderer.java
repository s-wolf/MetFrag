package de.ipbhalle.metfrag.tools.renderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.vecmath.Vector2d;

import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.renderer.Renderer;
import org.openscience.cdk.renderer.RendererModel;
import org.openscience.cdk.renderer.font.AWTFontManager;
import org.openscience.cdk.renderer.font.IFontManager;
import org.openscience.cdk.renderer.generators.AtomNumberGenerator;
import org.openscience.cdk.renderer.generators.BasicAtomGenerator;
import org.openscience.cdk.renderer.generators.BasicSceneGenerator;
import org.openscience.cdk.renderer.generators.ExtendedAtomGenerator;
import org.openscience.cdk.renderer.generators.IAtomContainerGenerator;
import org.openscience.cdk.renderer.generators.IGenerator;
import org.openscience.cdk.renderer.generators.IGeneratorParameter;
import org.openscience.cdk.renderer.generators.LonePairGenerator;
import org.openscience.cdk.renderer.generators.RingGenerator;
import org.openscience.cdk.renderer.visitor.AWTDrawVisitor;
import org.openscience.cdk.templates.MoleculeFactory;

/**
* Example code for implementing a scrolling panel.
*
* @author maclean
*
*/
public class StructureRenderer extends JFrame {
    
    public class MoleculePanel extends JPanel {
        
        private int initialWidth;
        private int initialHeight;
        private Renderer renderer;
        private IAtomContainer atomContainer; 
        private boolean isNew;
        
        public MoleculePanel(IAtomContainer atomContainer) {
            
        	
        	this.atomContainer = atomContainer;
            
            this.initialWidth = 300;
            this.initialHeight = 300;
            
            this.setPreferredSize(
                    new Dimension(this.initialWidth + 10, this.initialHeight + 10));
            this.setBackground(Color.WHITE);
            this.setBorder(BorderFactory.createRaisedBevelBorder());
            
            List<IAtomContainerGenerator> generators = new ArrayList<IAtomContainerGenerator>();
            generators.add(new BasicSceneGenerator());
            generators.add(new RingGenerator());
            generators.add(new ExtendedAtomGenerator());
                                      
         
            IFontManager fm = new AWTFontManager();
            this.renderer = new Renderer(generators, fm); 
            
            for (IGenerator generator : renderer.getGenerators()) {
            	for (IGeneratorParameter parameter : generator.getParameters()) {
            		System.out.println("parameter: " +
            	      parameter.getClass().getName().substring(40) +
            	      " -> " +
            	      parameter.getValue());
            		if(parameter.getClass().getName().substring(40).equals("BasicAtomGenerator$KekuleStructure"))
            			parameter.setValue(true);
            		
            	}
            }
            
            renderer.getRenderer2DModel().setDrawNumbers(true);
            System.out.println("Numbers: " + renderer.getRenderer2DModel().drawNumbers());
                        
            this.isNew = true;
        }
        
        public void paint(Graphics g) {
            super.paint(g);
            
            
            Rectangle drawArea =
                new Rectangle(0, 0, this.initialWidth, this.initialHeight);
            
            if (this.isNew) {
                this.renderer.setup(atomContainer, drawArea);
                this.isNew = false;
            }
            
            Rectangle diagramRectangle =
                this.renderer.calculateDiagramBounds(atomContainer);
            
            Rectangle result = renderer.shift(drawArea, diagramRectangle);
            this.setPreferredSize(new Dimension(result.width, result.height));
            this.revalidate();
            
            this.renderer.paintMolecule(this.atomContainer, new AWTDrawVisitor((Graphics2D) g), drawArea, true);
        }
    }
    
    /**
     * Instantiates a new structure renderer. Just an example.
     */
    public StructureRenderer() {
        IMolecule chain = MoleculeFactory.makeAdenine();
                
        StructureDiagramGenerator sdg = new StructureDiagramGenerator();
        sdg.setMolecule((IMolecule)chain);
        
        try {
            sdg.generateCoordinates();
            MoleculePanel molPanel = new MoleculePanel(sdg.getMolecule());
            this.add(new JScrollPane(molPanel));
        } catch (Exception e) {}
        
        this.pack();
        this.setVisible(true);
    }
    
    
    /**
     * Instantiates a new structure renderer.
     * Displays the structure in a window
     * 
     * @param original the original
     * @param name the name
     */
    public StructureRenderer(IAtomContainer original, String name) {
    	
    	IMolecule mol = new Molecule(original);
    	
    	StructureDiagramGenerator sdg = new StructureDiagramGenerator();
        sdg.setMolecule(mol);
        
        try {
            sdg.generateCoordinates();
            MoleculePanel molPanel = new MoleculePanel(sdg.getMolecule());
            this.add(new JScrollPane(molPanel));
        } catch (Exception e) {}
        
        this.pack();
        this.setVisible(true);
    }
    
    
    public static void main(String[] args) {
    	IMolecule chain = MoleculeFactory.makeAdenine();
    	chain.setProperty(CDKConstants.TITLE, "Test");
		new StructureRenderer(chain, "test");
	}


}
