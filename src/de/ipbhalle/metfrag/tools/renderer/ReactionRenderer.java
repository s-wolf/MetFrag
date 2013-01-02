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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.*;
import javax.vecmath.Point2d;

import org.freehep.graphics2d.VectorGraphics;
import org.freehep.graphicsio.svg.SVGGraphics2D;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.Reaction;
import org.openscience.cdk.aromaticity.CDKHueckelAromaticityDetector;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.geometry.GeometryTools;
import org.openscience.cdk.interfaces.*;
import org.openscience.cdk.layout.*;
import org.openscience.cdk.renderer.*;
import org.openscience.cdk.renderer.RendererModel.ColorHash;
import org.openscience.cdk.renderer.font.*;
import org.openscience.cdk.renderer.generators.*;
import org.openscience.cdk.renderer.generators.BasicAtomGenerator.AtomRadius;
import org.openscience.cdk.renderer.generators.ReactionSceneGenerator.ShowReactionBoxes;
import org.openscience.cdk.renderer.visitor.*;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.templates.*;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

import de.ipbhalle.metfrag.pubchem.PubChemWebService;
import de.ipbhalle.metfrag.similarity.Subgraph;


public class ReactionRenderer {
	
	
	private int width = 200;
    private int height = 200;
    private String folder;
    
    
    /**
     * Instantiates a new display structure (vector or png output).
     * It does not use JFrame. It will work on headerless servers.
     * 
     * @param width the width
     * @param height the height
     * @param folder the folder
     * @param showHydrogen the show hydrogen
     * 
     * @throws Exception the exception
     */
    public ReactionRenderer(int width, int height, String folder, boolean showHydrogen, boolean isCompact) throws Exception
    {
    	this.width = width;
    	this.height = height;
    	this.folder = folder;
    }
    
    /*
     * Creates a structure image based on a MOL string. The image rendering is
     * done with CDK.
     */
    private RenderedImage getImage4MOL(IAtomContainer molAC, IAtomContainer molHighlight) throws Exception {

    	molAC = AtomContainerManipulator.removeHydrogens(molAC);
    	molHighlight = AtomContainerManipulator.removeHydrogens(molHighlight);
    	    	
        // creates CDK Molecule object and get the renderer
    	IMolecule molSource = new Molecule(molAC);
    	IMolecule molHigh = new Molecule(molHighlight);
    	
    	Rectangle drawArea = new Rectangle(this.width, this.height);
		Image image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
    	
    	
		IReaction reaction = new Reaction();

		StructureDiagramGenerator sdg = new StructureDiagramGenerator();
		
		sdg.setMolecule(molHigh);
		sdg.generateCoordinates();
		molHigh = sdg.getMolecule();
		
		sdg.setMolecule(molSource);
		sdg.generateCoordinates();
		molSource = sdg.getMolecule();
		try {
		GeometryTools.translate2DCenterTo(molSource, new Point2d(-10,0));
		GeometryTools.translate2DCenterTo(molHigh, new Point2d(1,0));
		} catch (Exception e) {
		 e.printStackTrace();
		}

		reaction.addReactant(molSource);
		reaction.addProduct(molHigh);
		
		// generators make the image elements
		List<IGenerator<IAtomContainer>> generators = new ArrayList<IGenerator<IAtomContainer>>();
		generators.add(new BasicSceneGenerator());
        generators.add(new BasicBondGenerator());
        generators.add(new BasicAtomGenerator());
        
//        generators.add(new RingGenerator());
//        generators.add(new RadicalGenerator());
        
        List<IGenerator<IReaction>> reactiongenerators =  new ArrayList<IGenerator<IReaction>>();
        reactiongenerators.add(new ReactionSceneGenerator());
//        reactiongenerators.add(new ReactionArrowGenerator());
//        reactiongenerators.add(new ReactionPlusGenerator());
		   
		// the renderer needs to have a toolkit-specific font manager 
        org.openscience.cdk.renderer.ReactionRenderer renderer = new org.openscience.cdk.renderer.ReactionRenderer(generators, reactiongenerators, new AWTFontManager());
//		ReactionRenderer renderer = new ReactionRenderer(generators, reactiongenerators, new AWTFontManager());
		RendererModel rm = renderer.getRenderer2DModel();
//        rm.set(AtomRadius.class, 0.4);
//        rm.set(ShowReactionBoxes.class, true);
		
		IAtomContainer subgraph = Subgraph.getSubgraphsSMSD(molHigh, molSource, false, true);
		Map<IChemObject, Color> colorMap = new HashMap<IChemObject, Color>();
        for (IBond bond : subgraph.bonds()) {
        	colorMap.put(bond, new Color(0, 255, 0));
        }
        
        for (IAtom atom : subgraph.atoms()) {
            colorMap.put(atom, new Color(0, 255, 0));
        }
        
        rm.getParameter(ColorHash.class).setValue(colorMap);
		
		// the call to 'setup' only needs to be done on the first paint
		renderer.setup(reaction, drawArea);
		   
		// paint the background
		Graphics2D g2 = (Graphics2D)image.getGraphics();
	   	g2.setColor(Color.WHITE);
	   	g2.fillRect(0, 0, this.width, this.height);
	   	
	   	// the paint method also needs a toolkit-specific renderer
	   	renderer.paint(reaction, new AWTDrawVisitor(g2));


	   	return (RenderedImage)image;
    }
    

    /**
     * REnder a reaction and write to png file
     * 
     * @param mol the mol
     * @param pngFile the png file
     * 
     * @throws Exception the exception
     */
    public final void writeMOL2PNGFile(IAtomContainer mol, IAtomContainer molHighlight, String pngFile) throws Exception {

        RenderedImage rImage = null;

        try {
            rImage = (BufferedImage) getImage4MOL(mol, molHighlight);
            ImageIO.write(rImage, "png", new File(this.folder + pngFile));

        } catch (Exception e) {
            e.printStackTrace();
            //throw new Exception("EPS rendering of structure(s) failed");
        }

    }

	public static void main(String[] args) {
		
		try {
			//IMolecule triazole = MoleculeFactory.make123Triazole();
			//IMolecule chain = MoleculeFactory.makeIndole();
			SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
			IAtomContainer ac = sp.parseSmiles("C1C(OC2=CC(=CC(=C2C1=O)O)O)C3=CC=C(C=C3)O");
			IAtomContainer ac2 = sp.parseSmiles("C1=CC=CC=C1");
			ReactionRenderer rr = new ReactionRenderer(600, 400, "/home/swolf/", false, false);
			rr.writeMOL2PNGFile(ac, ac2, "test.png");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

}
