package de.ipbhalle.metfrag.tools;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.openscience.cdk.Molecule;
import org.openscience.cdk.aromaticity.CDKHueckelAromaticityDetector;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.geometry.GeometryTools;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.renderer.Java2DRenderer;
import org.openscience.cdk.renderer.Renderer2DModel;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

// TODO: Auto-generated Javadoc
/**
 * The Class DisplayStructure.
 */
public class DisplayStructure extends JPanel {
    
    /** The with h. */
    protected boolean withH = false;
    
    /** The width. */
    protected int width = 300;
    
    /** The height. */
    protected int height = 300;
    
    /** The scale. */
    protected double scale = .9;
    
    /** The do color. */
    protected boolean doColor = false;
    
    /** The oformat. */
    protected String oformat = "PNG";
    
    /** The odir. */
    protected String odir = "./";
    
    /** The suffix. */
    protected String suffix = ".png";
    
    /** The frame. */
    protected JFrame frame = null;
    
    JPanel panel = null;
    
    protected String fileName = "";


    /** The mol. */
    IAtomContainer mol;

    /** The r2dm. */
    Renderer2DModel r2dm;
    
    /** The renderer. */
    Java2DRenderer renderer;

    /**
     * Instantiates a new display structure.
     */
    public DisplayStructure() {
    }    
    
    /**
     * Instantiates a new display structure.
     * 
     * @param withH the with h
     * @param width the width
     * @param height the height
     * @param scale the scale
     * @param doColor the do color
     * @param oformat the oformat
     * @param odir the odir
     */
    public DisplayStructure(boolean withH, int width, int height, double scale, boolean doColor, String oformat, String odir) {
        this.withH = withH;
        this.width = width;
        this.height = height;
        this.scale = scale;
        this.oformat = oformat;
        this.odir = odir;
        this.doColor = doColor;

        if (oformat.equalsIgnoreCase("png")) {
            suffix = ".png";
            oformat = "PNG";
        } else if (oformat.equalsIgnoreCase("jpeg")) {
            suffix = ".jpg";
            oformat = "JPEG";
        } else if (oformat.equalsIgnoreCase("pdf")) {
            suffix = ".pdf";
            oformat = "PDF";
        } else if (oformat.equalsIgnoreCase("svg")) {
            suffix = ".svg";
            oformat = "SVG";
        }
        
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        this.frame = new JFrame();
    }
    
    
    /**
     * Instantiates a new display structure.
     * 
     * @param withH the with h
     * @param width the width
     * @param height the height
     * @param scale the scale
     * @param doColor the do color
     * @param oformat the oformat
     * @param odir the odir
     * @param filename the filename
     */
    public DisplayStructure(boolean withH, int width, int height, double scale, boolean doColor, String oformat, String odir, String filename) {
        this.withH = withH;
        this.width = width;
        this.height = height;
        this.scale = scale;
        this.oformat = oformat;
        this.odir = odir;
        this.doColor = doColor;
        this.fileName = filename;

        if (oformat.equalsIgnoreCase("png")) {
            suffix = ".png";
            oformat = "PNG";
        } else if (oformat.equalsIgnoreCase("jpeg")) {
            suffix = ".jpg";
            oformat = "JPEG";
        } else if (oformat.equalsIgnoreCase("pdf")) {
            suffix = ".pdf";
            oformat = "PDF";
        } else if (oformat.equalsIgnoreCase("svg")) {
            suffix = ".svg";
            oformat = "SVG";
        }
        
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        System.out.println("Headless mode: " + ge.isHeadless());
        
        System.out.println("HEADLESS: " + System.getProperty("java.awt.headless"));
        //fix headless exception
        System.setProperty( "java.awt.headless" , "true" );
        //Toolkit tk = Toolkit.getDefaultToolkit( );
        //this.panel = new JPanel();
        
        System.out.println("HEADLESS: " + System.getProperty("java.awt.headless"));
        
        this.frame = new JFrame();
        
        System.out.println("Past JFRAME: " + System.getProperty("java.awt.headless"));
        
    }

    /**
     * Draw structure.
     * 
     * @param mol the mol
     * @param the number of mols to be drawn
     */
    public void drawStructure(IAtomContainer mol, int cnt) {
    	IMolecule molecule;
    	
        if (!this.withH) {
            molecule = new Molecule(AtomContainerManipulator.removeHydrogens(mol));
        }
        else
        	molecule = new Molecule(mol);

        // do aromaticity detection
        try {
            CDKHueckelAromaticityDetector.detectAromaticity(mol);
        } catch (CDKException cdke) {
            cdke.printStackTrace();
        }

      
        
        r2dm = new Renderer2DModel();
        renderer = new Java2DRenderer(r2dm);
        Dimension screenSize = new Dimension(this.width, this.height);
        setPreferredSize(screenSize);
        r2dm.setBackgroundDimension(screenSize); // make sure it is synched with the JPanel size
        setBackground(r2dm.getBackColor());


        try {
            StructureDiagramGenerator sdg = new StructureDiagramGenerator();
            sdg.setMolecule(molecule);
            sdg.generateCoordinates();
            this.mol = sdg.getMolecule();

            r2dm.setDrawNumbers(false);
            r2dm.setUseAntiAliasing(true);
            r2dm.setColorAtomsByType(doColor);
            r2dm.setShowImplicitHydrogens(true);
            r2dm.setShowExplicitHydrogens(this.withH);
            r2dm.setShowAromaticity(false);
            r2dm.setShowReactionBoxes(false);
            r2dm.setKekuleStructure(false);

            GeometryTools.translateAllPositive(this.mol);
            GeometryTools.scaleMolecule(this.mol, getPreferredSize(), 0.9);
            GeometryTools.center(this.mol, getPreferredSize());

        } catch (Exception exc) {
            exc.printStackTrace();
        }
        
        //fix headless exception
        //this.panel.add(this);
        //this.panel.addNotify();
        
        //original code
        this.frame.getContentPane().add(this);
        this.frame.pack();
        
        
        String filename;
        if(this.fileName != "")
        	filename = this.odir + "/" + this.fileName + this.suffix;
        else
        {
	        if (cnt < 10) filename = this.odir + "/img00" + cnt + this.suffix;
	        else if (cnt < 100) filename = this.odir + "/img0" + cnt + this.suffix;
	        else filename = this.odir + "/img" + cnt + this.suffix;
        }

        if (oformat.equalsIgnoreCase("png") || oformat.equalsIgnoreCase("jpeg")) {
            Image img;
            try {
                img = createImage(this.getSize().width, this.getSize().height);
                Graphics2D snapGraphics = (Graphics2D) img.getGraphics();
                this.paint(snapGraphics);
                RenderedOp image = JAI.create("AWTImage", img);
                JAI.create("filestore", image, filename, this.oformat);
            } catch (NullPointerException e) {
                System.out.println(e.toString());
            }
        } else if (oformat.equalsIgnoreCase("pdf")) {
            File file = new File(filename);
            Rectangle pageSize = new Rectangle(this.getSize().width, this.getSize().height);
            Document document = new Document(pageSize);
            try {
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();
                PdfContentByte cb = writer.getDirectContent();

                Image awtImage = createImage(this.getSize().width, this.getSize().height);
                Graphics snapGraphics = awtImage.getGraphics();
                paint(snapGraphics);

                com.lowagie.text.Image pdfImage = com.lowagie.text.Image.getInstance(awtImage, null);
                pdfImage.setAbsolutePosition(0, 0);
                cb.addImage(pdfImage);

            } catch (DocumentException de) {
                System.err.println(de.getMessage());
            } catch (IOException ioe) {
                System.err.println(ioe.getMessage());
            }
            document.close();
        } 
    }

    /* (non-Javadoc)
     * @see javax.swing.JComponent#paint(java.awt.Graphics)
     */
    public void paint(Graphics g) {
        super.paint(g);
        renderer.paintMolecule(this.mol, (Graphics2D) g, new java.awt.Rectangle(width, height));
    }

    /**
     * Close.
     * 
     * @throws Exception the exception
     */
    public void close() throws Exception {
    }
}