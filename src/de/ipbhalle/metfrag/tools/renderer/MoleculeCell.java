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
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;


import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.renderer.AtomContainerRenderer;
import org.openscience.cdk.renderer.RendererModel;
import org.openscience.cdk.renderer.font.AWTFontManager;
import org.openscience.cdk.renderer.generators.BasicAtomGenerator;
import org.openscience.cdk.renderer.generators.BasicBondGenerator;
import org.openscience.cdk.renderer.generators.BasicSceneGenerator;
import org.openscience.cdk.renderer.generators.IGenerator;
import org.openscience.cdk.renderer.generators.RadicalGenerator;
import org.openscience.cdk.renderer.generators.BasicAtomGenerator.AtomRadius;
import org.openscience.cdk.renderer.visitor.AWTDrawVisitor;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

import de.ipbhalle.metfrag.tools.MolecularFormulaTools;


public class MoleculeCell extends JPanel{
	
	private int preferredWidth;
    private int preferredHeight;
    private IAtomContainer atomContainer;
    private AtomContainerRenderer renderer;
    private String title;
    
    private boolean isNew;
    
    public MoleculeCell(){
    }
    
    public MoleculeCell(IAtomContainer atomContainer, int w, int h, String title, boolean first) {
        this.atomContainer = atomContainer;
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

        this.renderer.paint(this.atomContainer, new AWTDrawVisitor((Graphics2D) g), drawArea, true);
        
        //draw title
        int fontSize = 12;
        g.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
        g.setColor(Color.red);
        
        IMolecularFormula formula = MolecularFormulaManipulator.getMolecularFormula(this.atomContainer);
        String formulaString = MolecularFormulaManipulator.getString(formula);
        Double mass = Math.round(MolecularFormulaTools.getMonoisotopicMass(formula)*1000.0)/1000.0;
        
        g.drawString(formulaString, 5, 12);
        g.drawString(mass.toString(), (preferredWidth + 10), 12);
        if(this.atomContainer.getProperty("TreeDepth") != null)
        {
        	System.out.println(this.atomContainer.getProperty("TreeDepth").toString());
        	g.drawString(this.atomContainer.getProperty("TreeDepth").toString(), (preferredWidth + 10), 22);
        }
    }


}
