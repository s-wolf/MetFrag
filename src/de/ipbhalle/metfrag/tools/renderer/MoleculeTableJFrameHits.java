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

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.templates.MoleculeFactory;

import de.ipbhalle.metfrag.spectrum.MatchedFragment;

public class MoleculeTableJFrameHits extends JFrame {
	
	private MoleculeTableHits table;
	
	public MoleculeTableJFrameHits(List<MatchedFragment> matchedFragments) {
        this.table = new MoleculeTableHits(matchedFragments);
        //JScrollPane pane = new JScrollPane(this.table);
        

        this.setTitle("MetFrag Fragments");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        
        JPanel spane = new JPanel(new GridLayout(0, 4), true);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		
		for (Component comp : table.getComponents()) {
			spane.add(comp);
		}
		//this.add(spane);
		JScrollPane scrollpane = new JScrollPane(spane,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.getContentPane().add(scrollpane);
		
    }
}