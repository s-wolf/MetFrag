package de.ipbhalle.metfrag.tools.renderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.*;

import org.freehep.graphics2d.VectorGraphics;
import org.freehep.graphicsio.svg.SVGGraphics2D;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.aromaticity.CDKHueckelAromaticityDetector;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.*;
import org.openscience.cdk.layout.*;
import org.openscience.cdk.renderer.*;
import org.openscience.cdk.renderer.font.*;
import org.openscience.cdk.renderer.generators.*;
import org.openscience.cdk.renderer.visitor.*;
import org.openscience.cdk.templates.*;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

import de.ipbhalle.metfrag.pubchem.PubChemWebService;


public class StructureToFile {
	
	
	private int width = 200;
    private int height = 200;
    private String folder;
    private boolean showHydrogen;
    private boolean isCompact = false;
    
    
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
    public StructureToFile(int width, int height, String folder, boolean showHydrogen, boolean isCompact) throws Exception
    {
    	this.width = width;
    	this.height = height;
    	this.folder = folder;
    	this.showHydrogen = showHydrogen;
    	this.isCompact = isCompact;
    }
    
    /*
     * Creates a structure image based on a MOL string. The image rendering is
     * done with CDK.
     */
    private RenderedImage getImage4MOL(IAtomContainer molAC) throws Exception {

        // creates CDK Molecule object and get the renderer
    	IMolecule molSource = new Molecule(molAC);
    	
    	Rectangle drawArea = new Rectangle(this.width, this.height);
		Image image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
    	
    	
		StructureDiagramGenerator sdg = new StructureDiagramGenerator();
		sdg.setMolecule(molSource);
		try {
	       sdg.generateCoordinates();
		} catch (Exception e) { }
		molSource = sdg.getMolecule();
    	
    	
		// generators make the image elements
		List<IAtomContainerGenerator> generators = new ArrayList<IAtomContainerGenerator>();
		generators.add(new BasicBondGenerator());
		generators.add(new BasicAtomGenerator());
		   
		// the renderer needs to have a toolkit-specific font manager 
		Renderer renderer = new Renderer(generators, new AWTFontManager());
		renderer.getRenderer2DModel().setDrawNumbers(true);

		
		// the call to 'setup' only needs to be done on the first paint
		renderer.setup(molSource, drawArea);
		   
		// paint the background
		Graphics2D g2 = (Graphics2D)image.getGraphics();
	   	g2.setColor(Color.WHITE);
	   	g2.fillRect(0, 0, this.width, this.height);
	   	
	   	// the paint method also needs a toolkit-specific renderer
	   	renderer.paintMolecule(molSource, new AWTDrawVisitor(g2), drawArea, true);

	   	return (RenderedImage)image;
    }
    
    
    /**
     * Creates a structure SVG based on a MOL string
     * 
     * @param mol the mol
     * @param svgFile the eps file
     * 
     * @throws Exception the exception
     */
    public void writeMOL2SVGFile(IMolecule mol, String svgFile) throws Exception {

        // creates CDK Molecule object and get the renderer
        //Molecule   mol      = prepareMolecule(MOLString);
    	// generators make the image elements
		List<IAtomContainerGenerator> generators = new ArrayList<IAtomContainerGenerator>();
		generators.add(new BasicBondGenerator());
		generators.add(new BasicAtomGenerator());
		   
		// the renderer needs to have a toolkit-specific font manager 
		Renderer renderer = new Renderer(generators, new AWTFontManager());
		
        try {

            // paint molecule to java.awt.BufferedImage
        	VectorGraphics vg = new SVGGraphics2D(new File(this.folder + svgFile), new Dimension(width, height));     	       	
    	   	
    	   	StructureDiagramGenerator sdg = new StructureDiagramGenerator();
    		sdg.setMolecule(mol);
    		try {
    	       sdg.generateCoordinates();
    		} catch (Exception e) { }
    		mol = sdg.getMolecule();
    	   	
        	
            Properties p = new Properties();
            p.setProperty("PageSize","A4");
            vg.setProperties(p);
            vg.startExport();
            renderer.paintMolecule(mol, new AWTDrawVisitor(vg), new Rectangle(width, height), true);
            vg.endExport();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("SVG rendering of structure(s) failed");
        }

    }
    
    
    
    /**
     * Creates a structure EPS based on a MOL string
     * 
     * @param mol the mol
     * @param pngFile the png file
     * 
     * @throws Exception the exception
     */
    public final void writeMOL2PNGFile(IAtomContainer mol, String pngFile) throws Exception {

        RenderedImage rImage = null;

        try {
            rImage = (BufferedImage) getImage4MOL(mol);
            ImageIO.write(rImage, "png", new File(this.folder + pngFile));

        } catch (Exception e) {
            e.printStackTrace();
            //throw new Exception("EPS rendering of structure(s) failed");
        }

    }

	public static void main(String[] args) {
		
		StructureToFile sr = null;
		try {
			//IMolecule triazole = MoleculeFactory.make123Triazole();
			//IMolecule chain = MoleculeFactory.makeIndole();
			
        	PubChemWebService pw = new PubChemWebService();
        	String PCID = "1148";
        	IAtomContainer container = pw.getSingleMol(PCID);
        	container = AtomContainerManipulator.removeHydrogens(container);
        	IMolecule mol = new Molecule(container);
			
			sr = new StructureToFile(200,200, "/home/swolf/", false, false);
			sr.writeMOL2SVGFile(mol, PCID + ".svg");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}

}
