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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;


import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObject;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.renderer.AtomContainerRenderer;
import org.openscience.cdk.renderer.IRenderer;
import org.openscience.cdk.renderer.RendererModel;
import org.openscience.cdk.renderer.RendererModel.ColorHash;
import org.openscience.cdk.renderer.font.AWTFontManager;
import org.openscience.cdk.renderer.generators.BasicAtomGenerator;
import org.openscience.cdk.renderer.generators.BasicBondGenerator;
import org.openscience.cdk.renderer.generators.BasicSceneGenerator;
import org.openscience.cdk.renderer.generators.ExtendedAtomGenerator;
import org.openscience.cdk.renderer.generators.IGenerator;
import org.openscience.cdk.renderer.generators.LonePairGenerator;
import org.openscience.cdk.renderer.generators.RadicalGenerator;
import org.openscience.cdk.renderer.generators.RingGenerator;
import org.openscience.cdk.renderer.generators.BasicAtomGenerator.AtomRadius;
import org.openscience.cdk.renderer.visitor.AWTDrawVisitor;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

import de.ipbhalle.metfrag.spectrum.MatchedFragment;
import de.ipbhalle.metfrag.tools.MolecularFormulaTools;


public class MoleculeCellHits extends JPanel{
	
	private int preferredWidth;
    private int preferredHeight;
    private IAtomContainer atomContainer;
    private MatchedFragment matchedFragment;
    private AtomContainerRenderer renderer;
    private String title;
    
    private boolean isNew;
    
    public MoleculeCellHits(){
    }
    
    public MoleculeCellHits(MatchedFragment matchedFragment, IAtomContainer mol, int w, int h, String title, boolean first) {
        this.matchedFragment = matchedFragment;
        this.atomContainer = mol;
        this.preferredWidth = w;
        this.preferredHeight = h;
        this.title = title;
        
        this.setPreferredSize(new Dimension(w + 10, h + 10));
        this.setBackground(Color.WHITE);
        if(first)
        	this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, Color.red, Color.gray));
        else
        	this.setBorder(BorderFactory.createEtchedBorder());
        
        List<IGenerator<IAtomContainer>> generators = new ArrayList<IGenerator<IAtomContainer>>();
        generators.add(new BasicSceneGenerator());
        generators.add(new BasicBondGenerator());
        generators.add(new BasicAtomGenerator());
        generators.add(new RadicalGenerator());
               
        
        this.renderer = new AtomContainerRenderer(generators, new AWTFontManager());
        RendererModel rm = renderer.getRenderer2DModel();
        rm.set(AtomRadius.class, 0.4);
        
        this.isNew = true;
    }
    
    public void paint(Graphics g) {
        super.paint(g);
        Rectangle drawArea = null;
        
        if (this.isNew) {
            drawArea = new Rectangle(0, 0, this.preferredWidth, this.preferredHeight);
            this.renderer.setup(atomContainer, drawArea);
            this.isNew = false;
        }

        this.renderer.paint(atomContainer, new AWTDrawVisitor((Graphics2D) g), drawArea, true);
        
        //draw title
        int fontSize = 12;
        g.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
        g.setColor(Color.red);
        
        IMolecularFormula formula = MolecularFormulaManipulator.getMolecularFormula(atomContainer);
        String formulaString = MolecularFormulaManipulator.getString(formula);
        
        String neutralLossString = "";
        if(matchedFragment.getNeutralLosses() != null)
        {
        	for (int i = 0; i < matchedFragment.getNeutralLosses().length; i++) {
        		neutralLossString += MolecularFormulaManipulator.getString(matchedFragment.getNeutralLosses()[i].getElementalComposition()) + "(" + matchedFragment.getNeutralLosses()[i].getMass() + ") ";
			}
        }
        
        
        g.drawString(formulaString, 5, 12);
        if(matchedFragment.getPeak().getMass() != 0.0)
        {
	        g.drawString("P:" + matchedFragment.getPeak().getMass() + " MM:" + matchedFragment.getMatchedMass(), (preferredWidth + 10), 12);
	        g.drawString(" FM:" + matchedFragment.getFragmentMass(), (preferredWidth + 10), 22);
	        g.drawString(" PC:" + matchedFragment.getPartialChargeDiff() + " HP:" + matchedFragment.getHydrogenPenalty() + " TD: " + matchedFragment.getFragmentStructure().getProperty("TreeDepth").toString(), (preferredWidth + 10), 32);
	        if(!neutralLossString.equals(""))
	        {
	        	g.drawString(" NL:" + neutralLossString, (preferredWidth + 10), 42);
	        	
	        	//highlight the matched neutral losses
	        	List<Integer> matched = new ArrayList<Integer>();
	        	for (List<Integer> list : matchedFragment.getMatchedAtoms()) {
	        		for (Integer integer : list) {
	        			System.out.println("Atom " + (integer + 1));
	        		}
	        		System.out.println("");
	        		matched.addAll(list);			   
	        	}
	        	
	            List<IAtom> atomsMatched = new ArrayList<IAtom>();
	    		for (Integer integer : matched) {
	    			try
	    			{
	    				atomsMatched.add(atomContainer.getAtom(integer));
	    			}
	    			catch(IndexOutOfBoundsException e)
	    			{
	    				System.err.println("Cannot highlight removed hydrogen!");
	    			}
	    						   
	    		}
	            
	    		
	    		List<IBond> bondsToHighlight = new ArrayList<IBond>();
	            Map<IChemObject, Color> colorMap = new HashMap<IChemObject, Color>();
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
	            RendererModel rm = renderer.getRenderer2DModel();
	            rm.getParameter(ColorHash.class).setValue(colorMap);
	        }
        }
    }
}
