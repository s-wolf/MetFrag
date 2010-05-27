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
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.renderer.Renderer;
import org.openscience.cdk.renderer.RendererModel;
import org.openscience.cdk.renderer.font.AWTFontManager;
import org.openscience.cdk.renderer.font.IFontManager;
import org.openscience.cdk.renderer.generators.AtomNumberGenerator;
import org.openscience.cdk.renderer.generators.BasicAtomGenerator;
import org.openscience.cdk.renderer.generators.BasicBondGenerator;
import org.openscience.cdk.renderer.generators.BasicGenerator;
import org.openscience.cdk.renderer.generators.BasicSceneGenerator;
import org.openscience.cdk.renderer.generators.ExtendedAtomGenerator;
import org.openscience.cdk.renderer.generators.HighlightAtomGenerator;
import org.openscience.cdk.renderer.generators.HighlightBondGenerator;
import org.openscience.cdk.renderer.generators.IAtomContainerGenerator;
import org.openscience.cdk.renderer.generators.IGenerator;
import org.openscience.cdk.renderer.generators.IGeneratorParameter;
import org.openscience.cdk.renderer.generators.LonePairGenerator;
import org.openscience.cdk.renderer.generators.RingGenerator;
import org.openscience.cdk.renderer.visitor.AWTDrawVisitor;
import org.openscience.cdk.smiles.SmilesParser;
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
//            		System.out.println("parameter: " +
//            	      parameter.getClass().getName().substring(40) +
//            	      " -> " +
//            	      parameter.getValue());
            		if(parameter.getClass().getName().substring(40).equals("BasicAtomGenerator$KekuleStructure"))
            			parameter.setValue(true);
            		
            	}
            }
            
            renderer.getRenderer2DModel().setDrawNumbers(true);
//            System.out.println("Numbers: " + renderer.getRenderer2DModel().drawNumbers());
                        
            this.isNew = true;
        }
        
        
        public MoleculePanel(IAtomContainer atomContainer, IAtomContainer highlight) {

        	this.atomContainer = atomContainer;           
            this.initialWidth = 300;
            this.initialHeight = 300;
            
            this.setPreferredSize(
                    new Dimension(this.initialWidth + 10, this.initialHeight + 10));
            this.setBackground(Color.WHITE);
            this.setBorder(BorderFactory.createRaisedBevelBorder());
            
            List<IAtomContainerGenerator> generators = new ArrayList<IAtomContainerGenerator>();
            HighlightAtomGenerator ha = new HighlightAtomGenerator();
            HighlightBondGenerator hb = new HighlightBondGenerator();
            
            
            generators.add(new BasicAtomGenerator());
            generators.add(new BasicBondGenerator());
            generators.add(ha);
            generators.add(hb);
                                      
         
            IFontManager fm = new AWTFontManager();
            this.renderer = new Renderer(generators, fm); 

//            ha.generate(highlight, renderer.getRenderer2DModel());
//            hb.generate(highlight, renderer.getRenderer2DModel());
            
            for (IGenerator generator : renderer.getGenerators()) {
            	for (IGeneratorParameter parameter : generator.getParameters()) {
//            		System.out.println("parameter: " +
//            	      parameter.getClass().getName().substring(40) +
//            	      " -> " +
//            	      parameter.getValue());
            		if(parameter.getClass().getName().substring(40).equals("BasicAtomGenerator$KekuleStructure"))
            			parameter.setValue(true);
            		
            	}
            } 
            renderer.getRenderer2DModel().setDrawNumbers(true);
//            System.out.println("Numbers: " + renderer.getRenderer2DModel().drawNumbers());
                        
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
    
    /**
     * Instantiates a new structure renderer.
     * Displays the structure in a window and highlights the given structure
     * 
     * @param original the original
     * @param name the name
     * @param highlight the highlight
     */
    public StructureRenderer(IAtomContainer original, IAtomContainer highlight, String name) {
    	
    	IMolecule mol = new Molecule(original);
    	
    	StructureDiagramGenerator sdg = new StructureDiagramGenerator();
        sdg.setMolecule(mol);
        
        try {
            sdg.generateCoordinates();
            MoleculePanel molPanel = new MoleculePanel(sdg.getMolecule(), highlight);
            this.add(new JScrollPane(molPanel));
        } catch (Exception e) {}
        
        this.pack();
        this.setVisible(true);
    }
    
    
    public static void main(String[] args) {
    	SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
    	try {
			IAtomContainer ac = sp.parseSmiles("O=c1c2ccccc2[Se]n1c1ccccc1");
			IAtomContainer ac2 = sp.parseSmiles("O=c1c2ccccc2[Se]n1c1ccccc1");
			new StructureRenderer(ac, "test");
			new StructureRenderer(ac, ac2, "test");
			
		} catch (InvalidSmilesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//    	IMolecule chain = MoleculeFactory.makeAdenine();
//    	chain.setProperty(CDKConstants.TITLE, "Test");
//		new StructureRenderer(chain, "test");
	}


}
