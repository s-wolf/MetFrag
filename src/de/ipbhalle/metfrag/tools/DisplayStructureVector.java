package de.ipbhalle.metfrag.tools;

import java.io.File;

import javax.imageio.ImageIO;

import java.awt.Image;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import java.util.Properties;

import org.freehep.graphics2d.VectorGraphics;
import org.freehep.graphicsio.pdf.PDFGraphics2D;
import org.freehep.graphicsio.ps.PSGraphics2D;
import org.freehep.graphicsio.svg.SVGGraphics2D;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.aromaticity.*;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.geometry.GeometryTools;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.renderer.Java2DRenderer;
import org.openscience.cdk.renderer.Renderer2DModel;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;

import de.ipbhalle.metfrag.pubchem.PubChemWebService;



/*
 *   @author Ekkehard Goerlach
 *   modified by Sebastian Wolf
 */

public class DisplayStructureVector {


        private int vRenderSizeX = 200;
        private int vRenderSizeY = 200;
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
        public DisplayStructureVector(int width, int height, String folder, boolean showHydrogen, boolean isCompact) throws Exception
        {
        	this.vRenderSizeX = width;
        	this.vRenderSizeY = height;
        	this.folder = folder;
        	this.showHydrogen = showHydrogen;
        	this.isCompact = isCompact;
        }
        
        
        /*
         * Creates a structure image based on a MOL string. The image rendering is
         * done with CDK.
         */
        public final Image getImage4MOL(IAtomContainer molAC) throws Exception {

            // creates CDK Molecule object and get the renderer
            //Molecule   mol      = prepareMolecule(molfile);
        	IMolecule molSource = new Molecule(molAC);
        	StructureDiagramGenerator sdg = new	StructureDiagramGenerator();
    		sdg.setMolecule(molSource);
    		sdg.generateCoordinates();
    		IMolecule mol = sdg.getMolecule();
        	
        	
            Java2DRenderer renderer = prepareRenderer(mol);

            BufferedImage bimage = null;
            try {

                // paint molecule to java.awt.BufferedImage
                bimage = new BufferedImage(vRenderSizeX, vRenderSizeY, BufferedImage.TYPE_BYTE_INDEXED);
                Graphics2D g = bimage.createGraphics();
                g.setBackground(Color.WHITE);
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, vRenderSizeX, vRenderSizeY);
                renderer.paintMolecule(mol, g, new Rectangle(0, 0, vRenderSizeX, vRenderSizeY));

            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("Rendering of structure(s) failed");
            }

            return bimage;
        }

        /*
         * Creates a structure EPS based on a MOL string
         */
        public final void writeMOL2PNGFile(IAtomContainer mol, String pngFile) throws Exception {

            BufferedImage bimage = null;
 
            try {
                bimage = (BufferedImage) getImage4MOL(mol);
                ImageIO.write(bimage, "png", new File(this.folder + pngFile));
 
            } catch (Exception e) {
                e.printStackTrace();
                //throw new Exception("EPS rendering of structure(s) failed");
            }

        }
        
        /*
         * Creates a structure gif on a MOL string
         */
        public final void writeMOL2GIFFile(IAtomContainer mol, String pngFile) throws Exception {

            BufferedImage bimage = null;
 
            try {
                bimage = (BufferedImage) getImage4MOL(mol);
                ImageIO.write(bimage, "gif", new File(this.folder + pngFile));
 
            } catch (Exception e) {
                e.printStackTrace();
                //throw new Exception("EPS rendering of structure(s) failed");
            }

        }

        /*
         * Creates a structure EPS based on a MOL string
         */
        public final void writeMOL2EPSFile(IMolecule molSource, File epsFile) throws Exception {

            // creates CDK Molecule object and get the renderer
            //Molecule   mol      = prepareMolecule(MOLString);

    		StructureDiagramGenerator sdg = new	StructureDiagramGenerator();
    		sdg.setMolecule(molSource);
    		sdg.generateCoordinates();
    		IMolecule mol = sdg.getMolecule();
        	
            Java2DRenderer renderer = prepareRenderer(mol);

            try {

                // paint molecule to java.awt.BufferedImage
                VectorGraphics vg = new PSGraphics2D(epsFile, new Dimension(vRenderSizeX, vRenderSizeY)); 

                Properties p = new Properties();
                p.setProperty("PageSize","A5");
                vg.setProperties(p);
                vg.startExport();
                renderer.paintMolecule(mol, vg, new Rectangle(vRenderSizeX, vRenderSizeY));
                vg.endExport();
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("EPS rendering of structure(s) failed");
            }

        }


        /*
         * Creates a structure PDF based on a MOL string
         */
        public final void writeMOL2PDFFile(IMolecule molSource, File epsFile) throws Exception {

            // creates CDK Molecule object and get the renderer
            //Molecule   mol      = prepareMolecule(MOLString);
            
        	StructureDiagramGenerator sdg = new	StructureDiagramGenerator();
    		sdg.setMolecule(molSource);
    		sdg.generateCoordinates();
    		IMolecule mol = sdg.getMolecule();
        	
        	Java2DRenderer renderer = prepareRenderer(mol);

            try {

                // paint molecule to java.awt.BufferedImage
                VectorGraphics vg = new PDFGraphics2D(epsFile, new Dimension(vRenderSizeX, vRenderSizeY)); 

                Properties p = new Properties();
                p.setProperty("PageSize","A5");
                vg.setProperties(p);
                vg.startExport();
                renderer.paintMolecule(mol, vg, new Rectangle(vRenderSizeX, vRenderSizeY));
                vg.endExport();
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("PDF rendering of structure(s) failed");
            }

        }



        /*
         * Creates a structure SVG based on a MOL string
         */
        public final void writeMOL2SVGFile(IMolecule mol, File epsFile) throws Exception {

            // creates CDK Molecule object and get the renderer
            //Molecule   mol      = prepareMolecule(MOLString);
            Java2DRenderer renderer = prepareRenderer(mol);

            try {

                // paint molecule to java.awt.BufferedImage
            	VectorGraphics vg = new SVGGraphics2D(epsFile, new Dimension(vRenderSizeX, vRenderSizeY)); 

                Properties p = new Properties();
                p.setProperty("PageSize","A5");
                vg.setProperties(p);
                vg.startExport();
                renderer.paintMolecule(mol, vg, new Rectangle(vRenderSizeX, vRenderSizeY));
                vg.endExport();
            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("SVG rendering of structure(s) failed");
            }

        }


        private Java2DRenderer prepareRenderer(IMolecule mol) throws Exception {

            Renderer2DModel r2dm;
            Java2DRenderer renderer;
            Dimension imgSize;
            
            try {
	            CDKHueckelAromaticityDetector.detectAromaticity(mol);
	        } catch (CDKException cdke) {
	            cdke.printStackTrace();
	        }
            
            
            try {
            	
            	imgSize  = new Dimension(vRenderSizeX, vRenderSizeY); 
            	
                r2dm = new Renderer2DModel();
                r2dm.setDrawNumbers(false);
                r2dm.showAtomTypeNames();
                r2dm.setBackgroundDimension(imgSize);
                r2dm.setBackColor(Color.WHITE);
                r2dm.setDrawNumbers(false);
                r2dm.setUseAntiAliasing(true);
                r2dm.setColorAtomsByType(true);
                r2dm.setShowImplicitHydrogens(true);
                r2dm.setShowExplicitHydrogens(this.showHydrogen);
                r2dm.setIsCompact(this.isCompact);
                r2dm.setShowReactionBoxes(false);
                r2dm.setKekuleStructure(false);
                r2dm.setBondWidth(3);
                
                
                //r2dm.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 14));
                
                renderer = new Java2DRenderer(r2dm);
                
                
                // 1200,900
                // create renderer to render MOLString into an java.awt.Image
                // renders 2D structures



            } catch (Exception e) {
                e.printStackTrace();
                throw new Exception("Creation of renderer failed");
            }

            return renderer;
        }


    public static void main(final String[] args) {


//        String mf;
//        String molfile;

        try {
//            BufferedReader reader = new BufferedReader(new FileReader("renderTest.mol"));
//            StringBuffer   sbuff  = new StringBuffer();
//            String         line;
//
//            while ( (line = reader.readLine()) != null ) {
//                sbuff.append(line + "\n");
//            }
//            reader.close();
//            molfile = sbuff.toString();
//            System.out.println(molfile);
        	
        	//the graph
        	SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        	PubChemWebService pw = new PubChemWebService();
        	String PCID = "1148";
        	IAtomContainer container = pw.getSingleMol(PCID);
        	container = AtomContainerManipulator.removeHydrogens(container);
//            IAtomContainer container = sp.parseSmiles("OC1C(O)=COC(CO)C1(O)");

            //Render.Draw(container, "test");
    		
    		IMolecule test = new Molecule(container);
    		StructureDiagramGenerator sdg = new	StructureDiagramGenerator();
    		sdg.setMolecule(test);
    		sdg.generateCoordinates();
    		IMolecule mol = sdg.getMolecule();
    		
    		// adds implicit H atoms to molecule
//    		CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(mol.getBuilder());   		
//    		for (IAtom atom : mol.atoms()) {
//    			IAtomType type = matcher.findMatchingAtomType(mol, atom);
//	    		AtomTypeManipulator.configure(atom, type);
//			}
//    		
//    		CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(mol.getBuilder());
//    		adder.addImplicitHydrogens(mol);
//    		AtomContainerManipulator.convertImplicitToExplicitHydrogens(mol);
    		
	
	        // detects aromaticity; used to display aromatic ring systems correctly
	        CDKHueckelAromaticityDetector.detectAromaticity(mol);
    		
	        Render.Draw(mol, "test");
	        
    		DisplayStructureVector test12 = new DisplayStructureVector(200, 200, "/home/swolf/", true, true);
    		//test12.writeMOL2PNGFile(mol, "test_cdk12.png");
//    		test12.writeMOL2PDFFile(mol, new File("/home/swolf/test_cdk12.pdf"));
    		test12.writeMOL2SVGFile(mol, new File("/home/swolf/" + PCID + ".svg"));

            //ChemRenderer crender = new ChemRenderer();
            //crender.writeMOL2PNGFile(mol, new File("/home/swolf/test_new.png"));
            //crender.writeMOL2EPSFile(mol, new File("/home/swolf/test_new.eps"));
            //crender.writeMOL2PDFFile(mol, new File("/home/swolf/test_new.pdf"));
            
            //crender.writeMOL2SVGFile(molfile,new File("Output.svg"));

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage() + ".");
            e.printStackTrace();
        }

    }


}

