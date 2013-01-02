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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObject;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.renderer.AtomContainerRenderer;
import org.openscience.cdk.renderer.RendererModel;
import org.openscience.cdk.renderer.RendererModel.ColorHash;
import org.openscience.cdk.renderer.font.AWTFontManager;
import org.openscience.cdk.renderer.font.IFontManager;
import org.openscience.cdk.renderer.generators.AtomNumberGenerator;
import org.openscience.cdk.renderer.generators.BasicAtomGenerator;
import org.openscience.cdk.renderer.generators.BasicAtomGenerator.AtomRadius;
import org.openscience.cdk.renderer.generators.BasicAtomGenerator.ColorByType;
import org.openscience.cdk.renderer.generators.BasicBondGenerator;
import org.openscience.cdk.renderer.generators.BasicSceneGenerator;
import org.openscience.cdk.renderer.generators.IGenerator;
import org.openscience.cdk.renderer.generators.IGeneratorParameter;
import org.openscience.cdk.renderer.generators.RadicalGenerator;
import org.openscience.cdk.renderer.generators.RingGenerator;
import org.openscience.cdk.renderer.generators.BasicAtomGenerator.KekuleStructure;
import org.openscience.cdk.renderer.visitor.AWTDrawVisitor;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.templates.MoleculeFactory;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

import de.ipbhalle.metfrag.similarity.Subgraph;

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
        private AtomContainerRenderer renderer;
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
            
            List<IGenerator<IAtomContainer>> generators = new ArrayList<IGenerator<IAtomContainer>>();
            generators.add(new BasicSceneGenerator());
//            generators.add(new BasicBondGenerator());
            generators.add(new BasicAtomGenerator());
            generators.add(new AtomNumberGenerator());
            generators.add(new RingGenerator());
            generators.add(new RadicalGenerator());
            
                                      
            
            IFontManager fm = new AWTFontManager();
            this.renderer = new AtomContainerRenderer(generators, fm); 
            RendererModel rm = renderer.getRenderer2DModel();
//            List<IGeneratorParameter<?>> parameterList = rm.getRenderingParameters();
//            for (IGeneratorParameter<?> parameter : parameterList) {
//				System.out.println(parameter.getClass().getName() + ": " +  parameter.getValue());
//			}
            
            
//            rm.set(ShowAromaticity.class, true);
            rm.set(KekuleStructure.class, true); 
            rm.set(AtomNumberGenerator.Offset.class, new javax.vecmath.Vector2d(15,0));
                        
            this.isNew = true;
        }
        
        
        public MoleculePanel(IAtomContainer atomContainer, IAtomContainer highlight) throws CDKException, IOException, CloneNotSupportedException {

        	this.atomContainer = atomContainer;           
            this.initialWidth = 300;
            this.initialHeight = 300;
            
            this.setPreferredSize(new Dimension(this.initialWidth + 10, this.initialHeight + 10));
            this.setBackground(Color.WHITE);
            this.setBorder(BorderFactory.createRaisedBevelBorder());
            
            List<IGenerator<IAtomContainer>> generators = new ArrayList<IGenerator<IAtomContainer>>();
            
            generators.add(new BasicSceneGenerator());
//            generators.add(new BasicBondGenerator());
            generators.add(new RingGenerator());
            generators.add(new BasicAtomGenerator());
            
            
            IFontManager fm = new AWTFontManager();
            this.renderer = new AtomContainerRenderer(generators, fm); 
            RendererModel rm = renderer.getRenderer2DModel();

            IAtomContainer subgraph = Subgraph.getSubgraphsSMSD(highlight, atomContainer, false, true);
			Map<IChemObject, Color> colorMap = new HashMap<IChemObject, Color>();
			
            for (IBond bond : subgraph.bonds()) {
            	colorMap.put(bond, new Color(0, 255, 0));
            }
            
            for (IAtom atom : subgraph.atoms()) {
                colorMap.put(atom, new Color(0, 255, 0));
            }
            
            rm.getParameter(ColorHash.class).setValue(colorMap);
                        
            this.isNew = true;
        }
        
        
        public MoleculePanel(IAtomContainer atomContainer, List<Integer> atomsToHighlight) throws CDKException, IOException, CloneNotSupportedException {

        	this.atomContainer = atomContainer;           
            this.initialWidth = 300;
            this.initialHeight = 300;
            
            this.setPreferredSize(new Dimension(this.initialWidth + 10, this.initialHeight + 10));
            this.setBackground(Color.WHITE);
            this.setBorder(BorderFactory.createRaisedBevelBorder());
            
            List<IGenerator<IAtomContainer>> generators = new ArrayList<IGenerator<IAtomContainer>>();
            
            generators.add(new BasicSceneGenerator());
//            generators.add(new BasicBondGenerator());
            generators.add(new RingGenerator());
            generators.add(new BasicAtomGenerator());
            
             
            IFontManager fm = new AWTFontManager();
            this.renderer = new AtomContainerRenderer(generators, fm); 
            RendererModel rm = renderer.getRenderer2DModel();
   
            List<IBond> bondsToHighlight = new ArrayList<IBond>();
            Map<IChemObject, Color> colorMap = new HashMap<IChemObject, Color>();
            
            List<IAtom> atomsMatched = new ArrayList<IAtom>();
			for (Integer integer : atomsToHighlight) {
				atomsMatched.add(atomContainer.getAtom(integer));			   
			}
            
            for (IAtom atom : atomsMatched) {
            	for (IAtom atom2 : atomsMatched) {
            		
                	IBond bond = atomContainer.getBond(atom, atom2);
                	if(bond!= null)
                		bondsToHighlight.add(bond);
                }            	
            }
            for (IBond bond : bondsToHighlight) {
                colorMap.put(bond, new Color(0, 255, 0));
            }
            rm.getParameter(ColorHash.class).setValue(colorMap);
            
//            List<IGeneratorParameter<?>> parameterList = rm.getRenderingParameters();
//	        for (IGeneratorParameter<?> parameter : parameterList) {
//	        	System.out.println(parameter.getClass().getName() + ": " +  parameter.getValue());
//			}
                        
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
            
            this.renderer.paint(this.atomContainer, new AWTDrawVisitor((Graphics2D) g), drawArea, true);
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
        } catch (Exception e) {
        	System.err.println("Error: " + e.getMessage() + "\n\n");
        	e.printStackTrace();
        }
        
        this.pack();
        this.setVisible(true);
    }
    
    
    public StructureRenderer(IAtomContainer original, List<Integer> atomsTohighlight, String name) {
    	
    	IMolecule mol = new Molecule(original);
    	
    	StructureDiagramGenerator sdg = new StructureDiagramGenerator();
        sdg.setMolecule(mol);
        
        try {
            sdg.generateCoordinates();
            MoleculePanel molPanel = new MoleculePanel(sdg.getMolecule(), atomsTohighlight);
            this.add(new JScrollPane(molPanel));
        } catch (Exception e) {
        	System.err.println("Error: " + e.getMessage() + "\n\n");
        	e.printStackTrace();
        }
        
        this.pack();
        this.setVisible(true);
    }
    
    
    public static void main(String[] args) {
    	SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
    	try {
			IAtomContainer ac = sp.parseSmiles("CC1=NN=C(N1C2CC3CCC(C2)N3CCC(C4=CC=CC=C4)NC(=O)C5CCC(CC5)(F)F)C(C)C");
			IAtomContainer ac2 = sp.parseSmiles("C1=CC=CC=C1");
			new StructureRenderer(ac, "test");
			new StructureRenderer(ac2, "test");
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
