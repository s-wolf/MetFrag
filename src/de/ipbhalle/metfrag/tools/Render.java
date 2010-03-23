package de.ipbhalle.metfrag.tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.Hashtable;
import java.util.HashSet;


import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.*;
import java.awt.*;

import org.openscience.cdk.Molecule;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.formula.MolecularFormula;
import org.openscience.cdk.geometry.GeometryTools;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.renderer.Java2DRenderer;
import org.openscience.cdk.renderer.Renderer2DModel;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;
import org.openscience.cdk.isomorphism.UniversalIsomorphismTester;
import org.openscience.cdk.isomorphism.mcss.RMap;
import org.openscience.cdk.aromaticity.CDKHueckelAromaticityDetector;

import de.ipbhalle.metfrag.main.PeakMolPair;




public class Render {

	/**
	 * Draw Molecule and the fragments of it
	 * 
	 * @param original the original molecule
	 * @param List of Fragments
	 */
	public static void Draw(IAtomContainer original, List<IAtomContainer> l, String name) {

		// make some nice pictures
		if (l.size() > 0) {
			int x = 300;
			int y = 300;
			Render.Renderer2DPanel[] panels = new Render.Renderer2DPanel[l.size() + 1];
			System.out.println("Creating image for specified molecule");
			//Original Molecule .... highlighted
			panels[0] = new Render.Renderer2DPanel(original, x, y, false, name);
			panels[0].setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, Color.red, Color.gray));

			int counter = 0;
			for (IAtomContainer atomContainer : l) {
				IMolecularFormula formula = MolecularFormulaManipulator.getMolecularFormula(atomContainer);
				String formulaString = MolecularFormulaManipulator.getString(formula);
				double protonMass = MolecularFormulaTools.getMonoisotopicMass("H1");
				//TODO: take care of M-H in legend
				String legend =  formulaString + "+H  " + ((double)Math.round(((MolecularFormulaTools.getMonoisotopicMass(formula) + protonMass) * (double)10000))/(double)10000);
				panels[counter + 1] = new Render.Renderer2DPanel(atomContainer, x,y, true, legend);
				panels[counter + 1].setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
				counter++;
			}

			JFrame frame = Render.multiStructurePanel(panels, 3, x, y);
			frame.pack();
			frame.setVisible(true);
		}
	}
	
	
	/**
	 * Draw Molecule and the fragments of it
	 * 
	 * @param original the original molecule
	 * @param List of Fragments
	 */
	public static void Draw(IAtomContainer original, IAtomContainerSet l, String name) {

		// make some nice pictures
		if (l.getAtomContainerCount() > 0) {
			int x = 300;
			int y = 300;
			Render.Renderer2DPanel[] panels = new Render.Renderer2DPanel[l.getAtomContainerCount() + 1];
			System.out.println("Creating image for specified molecule");
			//Original Molecule .... highlighted
			panels[0] = new Render.Renderer2DPanel(original, x, y, false, name);
			panels[0].setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, Color.red, Color.gray));

			int counter = 0;
			for (IAtomContainer atomContainer : l.atomContainers()) {
				IMolecularFormula formula = MolecularFormulaManipulator.getMolecularFormula(atomContainer);
				String formulaString = MolecularFormulaManipulator.getString(formula);
				double protonMass = MolecularFormulaTools.getMonoisotopicMass("H1");
				//TODO: take care of M-H in legend
				String legend =  formulaString + "+H  " + ((double)Math.round(((MolecularFormulaTools.getMonoisotopicMass(formula) + protonMass) * (double)10000))/(double)10000);
				panels[counter + 1] = new Render.Renderer2DPanel(atomContainer, x,y, true, legend);
				panels[counter + 1].setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
				counter++;
			}

			JFrame frame = Render.multiStructurePanel(panels, 3, x, y);
			frame.pack();
			frame.setVisible(true);
		}
	}
	
	/**
	 * Draw hits.
	 * 
	 * @param original the original
	 * @param hits the hits
	 * @param name the name
	 */
	public static void DrawHits(IAtomContainer original, Vector<PeakMolPair> hits, String name) {

		// make some nice pictures
		if (hits.size() > 0) {
			int x = 300;
			int y = 300;
			Render.Renderer2DPanel[] panels = new Render.Renderer2DPanel[hits.size() + 1];
			System.out.println("Creating image for specified molecule");
			//Original Molecule .... highlighted
			panels[0] = new Render.Renderer2DPanel(original, x, y, false, name);
			panels[0].setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, Color.red, Color.gray));

			int counter = 0;
			for (PeakMolPair pair : hits) {
				panels[counter + 1] = new Render.Renderer2DPanel(pair.getFragment(), x,y, true, "Peak: " + pair.getPeak());
				panels[counter + 1].setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
				counter++;
			}

			JFrame frame = Render.multiStructurePanel(panels, 3, x, y);
			frame.pack();
			frame.setVisible(true);
		}
	}
	
	/**
	 * Draw a picture of a single molecule.
	 * 
	 * @param original the original
	 * @param name the name
	 */
	public static void Draw(IAtomContainer original, String name) {

		// make some nice pictures
		
		int x = 300;
		int y = 300;
		Render.Renderer2DPanel[] panels = new Render.Renderer2DPanel[1];
		System.out.println("Creating image for specified molecule");
		//Original Molecule .... highlighted
		panels[0] = new Render.Renderer2DPanel(original, x, y, false, name);
		panels[0].setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, Color.red, Color.gray));

		JFrame frame = Render.multiStructurePanel(panels, 1, x, y);
		frame.pack();
		frame.setVisible(true);

	}
	
	/**
	 * Highlight a substructure in a molecule
	 * 
	 * @param the original molecule
	 * @param the substructure to highlight
	 * @param the name of the molecule
	 * 
	 * @throws Exception the exception
	 */
	public static void Highlight(IAtomContainer original, IAtomContainer l, String name) throws Exception {
		
		StructureDiagramGenerator sdg = new StructureDiagramGenerator();
        sdg.setMolecule((IMolecule) original);
        sdg.generateCoordinates();
        IMolecule mol = sdg.getMolecule();
        
		// find all subgraphs of the original molecule matching the fragment
	    List lHighlight = UniversalIsomorphismTester.getSubgraphMaps(original, l);
	    System.out.println("Number of matched subgraphs = " + lHighlight.size());
	
	    AtomContainer needle = new AtomContainer();
	    Vector<Integer> idlist = new Vector<Integer>();
	
	    // get the ID's (corresponding to the serial number of the Bond object in
	    // the AtomContainer for the supplied molecule) of the matching bonds
	    // (there will be repeats)
	    for (Object aL : lHighlight) {
	        List maplist = (List) aL;
	        for (Object i : maplist) {
	            idlist.add(((RMap) i).getId1());
	        }
	    }
	
	    // get a unique list of bond ID's and add them to an AtomContainer
	    HashSet<Integer> hs = new HashSet<Integer>(idlist);
	    for (Integer h : hs) needle.addBond(mol.getBond(h));
	
	    // show the substructure. Enjoy :)
	    new displaySubstructure(mol, needle, name);
	}
	
	
	/**
	 * Highlight a given bond in a molecule
	 * 
	 * @param the original molecule
	 * @param the bond to highlight
	 * @param the name of the molecule
	 * 
	 * @throws Exception the exception
	 */
	public static void HighlightBond(IAtomContainer original, IBond bond, String name) throws Exception {
		
		StructureDiagramGenerator sdg = new StructureDiagramGenerator();
        sdg.setMolecule((IMolecule) original);
        sdg.generateCoordinates();
        IMolecule mol = sdg.getMolecule();
        
	
	    AtomContainer needle = new AtomContainer();	
	    for (IAtom atom : bond.atoms()) {
            needle.addAtom(atom);
	    }
	    needle.addBond(bond);
	
	    // show the substructure. Enjoy :)
	    new displaySubstructure(mol, needle, name);
	}
    

	public static JFrame multiStructurePanel(Render.Renderer2DPanel[] data, int ncol, int cellx, int celly) {

		int pad = 5;

		int extra = data.length % ncol;
		int block = data.length - extra;
		int nrow = block / ncol;

		int i;
		int j;

		JFrame frame = new JFrame("Molecule Fragmenter");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel spane = new JPanel(new GridBagLayout(), true);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		//gbc.insets = new Insets(2, 2, 2, 2);

		int cnt = 0;
		for (i = 0; i < nrow; i++) {
			for (j = 0; j < ncol; j++) {
				gbc.gridx = j;
				gbc.gridy = i;
				spane.add(data[cnt], gbc);
				cnt += 1;
			}
		}
		j = 0;
		while (cnt < data.length) {
			gbc.gridy = nrow;
			gbc.gridx = j;
			spane.add(data[cnt], gbc);
			cnt += 1;
			j += 1;
		}

		if (extra != 0)
			nrow += 1;

		JScrollPane scrollpane = new JScrollPane(spane,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		frame.getContentPane().add(scrollpane);

		// start the show!
		frame.pack();
		if (nrow > 3) {
			frame.setSize(ncol * cellx + pad, 3 * celly + pad);
		} else {
			frame.setSize(ncol * cellx + pad, nrow * celly + pad);
		}

		return frame;
	}

	static class Renderer2DPanel extends JPanel {

		private static final long serialVersionUID = 1L;

		IAtomContainer mol;
		boolean withHydrogen = false;
		Renderer2DModel r2dm;
		Java2DRenderer renderer;
		String title;

		public Renderer2DPanel() {
		}

		public Renderer2DPanel(IAtomContainer mol, int x, int y, boolean withHydrogen, String name) {
			this.title = name;
			
			// do aromaticity detection
	        try {
	            CDKHueckelAromaticityDetector.detectAromaticity(mol);
	        } catch (CDKException cdke) {
	            cdke.printStackTrace();
	        }
			
			r2dm = new Renderer2DModel();
			renderer = new Java2DRenderer(r2dm);
			Dimension screenSize = new Dimension(x, y);
			setPreferredSize(screenSize);
			r2dm.setBackgroundDimension(screenSize); // make sure it is synched with the JPanel size
			setBackground(r2dm.getBackColor());

			this.mol = mol;
			this.withHydrogen = withHydrogen;

			try {
				StructureDiagramGenerator sdg = new StructureDiagramGenerator();
				sdg.setMolecule(new Molecule(mol));
				sdg.generateCoordinates();
				this.mol = sdg.getMolecule();

				r2dm.setDrawNumbers(true);
				r2dm.setShowMoleculeTitle(true);
				r2dm.setShowAtomTypeNames(false);
	            r2dm.setUseAntiAliasing(true);
	            r2dm.setColorAtomsByType(true);
	            r2dm.setShowImplicitHydrogens(true);
	            r2dm.setShowExplicitHydrogens(true);
	            r2dm.setShowAromaticity(false);
	            r2dm.setShowReactionBoxes(false);
	            r2dm.setKekuleStructure(false);
	            r2dm.setBondWidth(2);
	            r2dm.setShowTooltip(true);
	            
	            //r2dm.setBondDistance(60.0);
                r2dm.setFont(new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 14));
                //r2dm.setMargin(0.1);
				
//				GeometryTools.translateAllPositive(this.mol);
//	            GeometryTools.scaleMolecule(this.mol, getPreferredSize(), 0.9);
//	            GeometryTools.center(this.mol, getPreferredSize());

			} catch (Exception exc) {
				exc.printStackTrace();
			}
		}

		public void paint(Graphics g) {
			super.paint(g);

	        renderer.paintMolecule(this.mol, (Graphics2D) g, new Rectangle(300, 300));

	        // draw the name
	        g.drawString(this.title, 5, 15);
		}
	}
	
	
	static class displaySubstructure extends JPanel {
	    IAtomContainer localhaystack;
	    
	    Renderer2DModel r2dm;
	    Java2DRenderer renderer;
	    String title;

	    public displaySubstructure(IAtomContainer haystack, IAtomContainer needle, String name) {
	    	this.title = name;
	    	
	    	Hashtable ht = null;
	        
	        // do aromaticity detection
	        try {
	            CDKHueckelAromaticityDetector.detectAromaticity(haystack);
	        } catch (CDKException cdke) {
	            cdke.printStackTrace();
	        }
	        
	        r2dm = new Renderer2DModel();
	        renderer = new Java2DRenderer(r2dm);
	        Dimension screenSize = new Dimension(250, 250);
	        setPreferredSize(screenSize);
	        r2dm.setBackgroundDimension(screenSize);
	        setBackground(r2dm.getBackColor());

	        localhaystack = haystack;
	        

	        try {
	            r2dm.setDrawNumbers(true);
	            r2dm.setUseAntiAliasing(true);
	            r2dm.setShowImplicitHydrogens(false);
	            r2dm.setShowAromaticity(false);
	            r2dm.setColorAtomsByType(false);
	            r2dm.setSelectedPartColor(Color.green);
	            r2dm.setSelectedPart(needle);

//	            GeometryTools.translateAllPositive(haystack);
//	            GeometryTools.scaleMolecule(haystack, getPreferredSize(), 0.8);
//	            GeometryTools.center(haystack, getPreferredSize());
	        } catch (Exception exc) {
	            exc.printStackTrace();
	        }
	        JFrame frame = new JFrame();
	        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	        frame.getContentPane().add(this);
	        frame.pack();
	        frame.setVisible(true);

	    }

	    public void paint(Graphics g) {
	        super.paint(g);
	        renderer.paintMolecule(localhaystack, (Graphics2D) g, new Rectangle(200, 200));
	        // draw the name
	        g.drawString(this.title, 5, 15);
	    }

	}
}
