package de.ipbhalle.metfrag.tools.renderer;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import de.ipbhalle.metfrag.bondPrediction.ChargeResult;
import de.ipbhalle.metfrag.bondPrediction.Charges;


import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.renderer.Renderer;
import org.openscience.cdk.renderer.font.AWTFontManager;
import org.openscience.cdk.renderer.font.IFontManager;
import org.openscience.cdk.renderer.generators.BasicAtomGenerator;
import org.openscience.cdk.renderer.generators.BasicBondGenerator;
import org.openscience.cdk.renderer.generators.BasicSceneGenerator;
import org.openscience.cdk.renderer.generators.ExtendedAtomGenerator;
import org.openscience.cdk.renderer.generators.IGenerator;
import org.openscience.cdk.renderer.generators.IGeneratorParameter;
import org.openscience.cdk.renderer.generators.RingGenerator;
import org.openscience.cdk.renderer.visitor.AWTDrawVisitor;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class WritePDFTable extends MoleculeCell {
    private PdfPTable table;
    private Document document;
    private int width;
    private int height;
    int ncol = 2;
    private int imageCount = 0;
    
    
    
    /**
     * Instantiates a new write pdf table. This is mainly for debugging the gasteiger marsili charges
     * 
     * @param odir the odir
     * @param width the width
     * @param height the height
     * @param chargeResults the charge results
     */
    public WritePDFTable(String odir, int width, int height, List<ChargeResult> chargeResults) {
        
    	this.width = width;
    	this.height = height;
    	
        try {
            File file = new File(odir);
            document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));

            float[] widths = new float[ncol];
            for (int i = 0; i < ncol; i += 2) {
                    widths[i] = 2f;
                    widths[i + 1] = 2f;
            }
            table = new PdfPTable(widths);
            document.open();
            
            for (ChargeResult result : chargeResults) {
            	PdfPCell cell = new PdfPCell();
                Phrase phrase = new Phrase();

                com.lowagie.text.Image image = com.lowagie.text.Image.getInstance(writeMOL2PNGFile(result.getMol()).getAbsolutePath());
                image.setAbsolutePosition(0, 0);
                table.addCell(image);
                addProperty(phrase, result.getChargeString());
//              cell.addElement(image);
                cell.addElement(phrase);
                table.addCell(cell);
			}
            
            document.add(table);
            document.close();
            
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    
    private void addProperty(Phrase phrase, String text) {
        phrase.add(new Chunk(
                text + "\n",
                FontFactory.getFont(FontFactory.HELVETICA)
        ));
    }
    
    private File writeMOL2PNGFile(IAtomContainer mol) {

        RenderedImage rImage = null;
        File tempFile = null;
        
        try {
        	tempFile = File.createTempFile(imageCount + "_Temp", "png");
            rImage = (BufferedImage) getImage4MOL(mol);
            ImageIO.write(rImage, "png", tempFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return tempFile;
    }
    
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
    	
    	
		List<IGenerator<IAtomContainer>> generators = new ArrayList<IGenerator<IAtomContainer>>();
        generators.add(new BasicSceneGenerator());
        generators.add(new RingGenerator());
        generators.add(new ExtendedAtomGenerator());

        IFontManager fm = new AWTFontManager();

        // the renderer needs to have a toolkit-specific font manager 
		Renderer renderer = new Renderer(generators, new AWTFontManager());
//        for (IGenerator generator : renderer.getGenerators()) {
//        	for (IGeneratorParameter parameter : generator.getParameters()) {
//        		if(parameter.getClass().getName().substring(40).equals("BasicAtomGenerator$KekuleStructure"))
//        			parameter.setValue(true);
//        	}
//        }
//
//		renderer.getRenderer2DModel().setDrawNumbers(true);

		
		// the call to 'setup' only needs to be done on the first paint
		renderer.setup(molSource, drawArea);
		   
		// paint the background
		Graphics2D g2 = (Graphics2D)image.getGraphics();
	   	g2.setColor(Color.WHITE);
	   	g2.fillRect(0, 0, this.width, this.height);
	   	
	   	// the paint method also needs a toolkit-specific renderer
	   	renderer.paintMolecule(molSource, new AWTDrawVisitor(g2), drawArea, true);

	   	return (RenderedImage) image;       

    }
    
    
    public static void main(String[] args) {
    	SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
		try {
			IAtomContainer naringenin = sp.parseSmiles("C1C(OC2=CC(=CC(=C2C1=O)O)O)C3=CC=C(C=C3)O");
			IAtomContainer ac = sp.parseSmiles("O=c1c2ccccc2[Se]n1c1ccccc1");
			IAtomContainer ac2 = sp.parseSmiles("O=c1c2ccccc2[Se]n1c1ccccc1");
			
			Charges charge = new Charges();
			charge.calculateBondsToBreak(naringenin);
			
			WritePDFTable wpdf = new WritePDFTable("/home/swolf/test.pdf", 600, 600, charge.getResults());
		} catch (InvalidSmilesException e) {
			e.printStackTrace();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		} catch (CDKException e) {
			e.printStackTrace();
		}
		
	}
}
