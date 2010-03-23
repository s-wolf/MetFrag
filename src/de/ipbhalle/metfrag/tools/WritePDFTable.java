package de.ipbhalle.metfrag.tools;

import com.lowagie.text.*;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;


import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.aromaticity.CDKHueckelAromaticityDetector;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.geometry.GeometryTools;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.renderer.Java2DRenderer;
import org.openscience.cdk.renderer.Renderer2DModel;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

import javax.swing.*;
import java.awt.*;
import java.awt.Image;
import java.io.*;
import java.text.DecimalFormat;
import java.util.Enumeration;


public class WritePDFTable extends DisplayStructure {
    private PdfPTable table;
    private Document document;
    int ncol = 1;
    boolean props = false;
    boolean doColor = false;
    DecimalFormat decimalFormat;

    public WritePDFTable(boolean withH, int width, int height, double scale, int ncol, boolean props, boolean doColor, String odir) {
        this.withH = withH;
        this.width = width;
        this.height = height;
        this.scale = scale;
        this.odir = odir;
        this.props = props;
        this.ncol = ncol;
        this.doColor = doColor;

        suffix = ".pdf";
        oformat = "PDF";

        decimalFormat = new DecimalFormat("0.000E00");
        this.frame = new JFrame();

        try {
            File file = new File(odir + "/output" + this.suffix);
            document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));

            float[] widths = new float[ncol];
            for (int i = 0; i < ncol; i += 2) {
                if (props) {
                    widths[i] = 3f;
                    widths[i + 1] = 2f;
                } else {
                    widths[i] = 2f;
                    widths[i + 1] = 2f;
                }
            }
            table = new PdfPTable(widths);
            document.open();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    private void addTitle(IAtomContainer mol, Phrase phrase, int cnt) {
       // if (mol.getProperty(CDKConstants.TITLE) == null ||
       //         mol.getProperty(CDKConstants.TITLE).toString().trim().length() == 0 || cnt < 0) return;
        
        if(cnt >= 0)
        {
        	double protonMass = MolecularFormulaTools.getMonoisotopicMass("H1");
        	IMolecule m = new Molecule(mol);
            IMolecularFormula molecularFormula = MolecularFormulaManipulator.getMolecularFormula(m); 
            double mass = MolecularFormulaTools.getMonoisotopicMass(MolecularFormulaManipulator.getString(molecularFormula));
            mass += protonMass;
        	phrase.add(new Chunk(
        			de.ipbhalle.metfrag.tools.Number.numberToFixedLength(cnt,3) + ".mol\n" +
        					"Weight: " + Math.round(mass*1000.0)/1000.0,
	                FontFactory.getFont(FontFactory.HELVETICA_BOLD)
	        ));
        }
        else
        {
	        phrase.add(new Chunk(
	                CDKConstants.TITLE + ": ",
	                FontFactory.getFont(FontFactory.HELVETICA_BOLD)
	        ));
	        phrase.add(new Chunk(
	                mol.getProperty(CDKConstants.TITLE) + "\n",
	                FontFactory.getFont(FontFactory.HELVETICA)
	        ));
        }
    }


    private void addProperty(Object propertyLabel, IAtomContainer mol, Phrase phrase) {
        //if (mol.getProperty(propertyLabel) == null ||
        //        mol.getProperty(propertyLabel).toString().trim().length() == 0) return;

        phrase.add(new Chunk(
                propertyLabel + ": ",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD)
        ));
        String value;
        try {
            double tmp = Double.parseDouble(mol.getProperty(propertyLabel).toString());
            value = decimalFormat.format(tmp);
        } catch (NumberFormatException nfe) {
            value = mol.getProperty(propertyLabel).toString();
        }
        phrase.add(new Chunk(
                value + "\n",
                FontFactory.getFont(FontFactory.HELVETICA)
        ));
    }

    public void drawStructure(IAtomContainer mol1, int cnt) {
        
    	if (!this.withH) {
            mol1 = AtomContainerManipulator.removeHydrogens(mol1);
        }
    	
    	IMolecule mol = new Molecule(mol1);
    	
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
            sdg.setMolecule((IMolecule) mol);
            sdg.generateCoordinates();
            this.mol = sdg.getMolecule();

            r2dm.setDrawNumbers(false);
            r2dm.setUseAntiAliasing(true);
            r2dm.setColorAtomsByType(doColor);
            r2dm.setShowImplicitHydrogens(true);
            r2dm.setShowAromaticity(false);
            r2dm.setShowReactionBoxes(false);
            r2dm.setKekuleStructure(false);

            GeometryTools.translateAllPositive(this.mol);
            GeometryTools.scaleMolecule(this.mol, getPreferredSize(), 0.9);
            GeometryTools.center(this.mol, getPreferredSize());

        }
        catch (Exception exc) {
            exc.printStackTrace();
        }
        this.frame.getContentPane().add(this);
        this.frame.pack();

        Rectangle pageSize = new Rectangle(this.getSize().width, this.getSize().height);
        try {
            StringBuffer comment = new StringBuffer();

            PdfPCell cell = new PdfPCell();
            Phrase phrase = new Phrase();

            addTitle(mol, phrase, cnt);

            if (props) {
                Enumeration props = (Enumeration)mol.getProperties().keySet();
                while (props.hasMoreElements()) {
                    Object key = props.nextElement();
                    if (key.equals("AllRings")) continue;
                    if (key.equals("SmallestRings")) continue;
                    if (key.equals(CDKConstants.TITLE)) continue;
                    addProperty(key, mol, phrase);
                }
            }
            cell.addElement(phrase);
            table.addCell(cell);

            Image awtImage = createImage(this.getSize().width, this.getSize().height);
            Graphics snapGraphics = awtImage.getGraphics();
            paint(snapGraphics);

            com.lowagie.text.Image pdfImage = com.lowagie.text.Image.getInstance(awtImage, null);
            pdfImage.setAbsolutePosition(0, 0);
            table.addCell(pdfImage);

        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        renderer.paintMolecule(this.mol, (Graphics2D) g, new java.awt.Rectangle(200, 200));
    }

    public void close() throws Exception {
        document.add(table);
        document.close();
    }

}