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
package de.ipbhalle.metfrag.tools;

import java.util.ArrayList;
import java.util.List;

import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.smiles.smarts.SMARTSQueryTool;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

import de.ipbhalle.metfrag.tools.renderer.StructureRenderer;


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double mass = MolecularFormulaTools.getMonoisotopicMass("C17H4O14");
		System.out.println(mass);
		
		System.out.println(PPMTool.getPPMDeviation(200.0, 10));
		System.out.println(PPMTool.getPPMDeviation(200.0, 20));
		System.out.println(PPMTool.getPPMWeb(200.0, 200.004));
		
		try {
			SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
			IAtomContainer atomContainer = sp.parseSmiles("C1=CC=C(C=C1)C(=O)C2=C(C(=CC=C2)CC(=O)O)N");
			
			
			AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(atomContainer);
			CDKHydrogenAdder hAdder = CDKHydrogenAdder.getInstance(atomContainer.getBuilder());
	        hAdder.addImplicitHydrogens(atomContainer);
	        AtomContainerManipulator.convertImplicitToExplicitHydrogens(atomContainer);
	        new StructureRenderer(atomContainer, "test");
	        
	        SmilesGenerator sg = new SmilesGenerator();
	        System.out.println(sg.createSMILES(atomContainer));
			
//			//WATER //TODO check distance of 3
//	        SMARTSQueryTool querytool = new SMARTSQueryTool("[H][$([OH1][#6])][#6][#6][#6][H]");
			SMARTSQueryTool querytool = new SMARTSQueryTool("[H][$([OH1][#6])][#6][#6][H]");
//			SMARTSQueryTool querytool1 = new SMARTSQueryTool("[H][$([OH1][#6])][#6][H]");
	        
//	        //APOMORPHINE CH3NH2 loss ... Apomorphine
//			SMARTSQueryTool querytool = new SMARTSQueryTool("[#6H3][NR][#6H2]");
	        
	        //Alkaloids HCN neutral loss TODO
//			SMARTSQueryTool querytool = new SMARTSQueryTool("[N][#6][H]");
	        
	        //COH to CO +H
//	        SMARTSQueryTool querytool = new SMARTSQueryTool("[H][O][#6]");
	        
	        //COH to COH2O
//	        SMARTSQueryTool querytool = new SMARTSQueryTool("[H][O][#6][H]");
//	        SMARTSQueryTool querytool = new SMARTSQueryTool("[H][O][#6][#6][H]");
//	        SMARTSQueryTool querytool = new SMARTSQueryTool("[H][O][#6][#6][#6][H]");
	        
	        //COOH to CO2
//	        SMARTSQueryTool querytool = new SMARTSQueryTool("[H][O][#6]=[O]");
	        
	        //COOH to HCOOH
//	        SMARTSQueryTool querytool = new SMARTSQueryTool("[H][O][#6]([#6][H])=[O]");
//	        SMARTSQueryTool querytool = new SMARTSQueryTool("[H][O][#6]([#6][#6][H])=[O]");
//	        SMARTSQueryTool querytool = new SMARTSQueryTool("[H][O][#6]([#6][#6][#6][H])=[O]");
			
			//NH3 Neutral loss TODO (the N has 2 attached H!)
//	        SMARTSQueryTool querytool = new SMARTSQueryTool("[$([NH2][#6])]([H])([H])[#6][#6][#6][H]");
//	        SMARTSQueryTool querytool = new SMARTSQueryTool("[$([NH2][#6])]([H])([H])[#6][#6][H]");
//	        SMARTSQueryTool querytool = new SMARTSQueryTool("[$([NH2][#6])]([H])([H])[#6][H]");
	        
	        //TODO: SO3H
//	        SMARTSQueryTool querytool = new SMARTSQueryTool("[$([NH2][#6])]([H])([H])[#6][H]");
	        
			boolean status = querytool.matches(atomContainer);
			if (status) {
			   int nmatch = querytool.countMatches();
			   System.out.println(nmatch);
			   
			   List<List<Integer>> matchedAtoms = querytool.getMatchingAtoms();
			   List<Integer> matched = new ArrayList<Integer>();
			   for (List<Integer> list : matchedAtoms) {
				   for (Integer integer : list) {
					   System.out.println("Atom " + (integer + 1));
				   }
				   System.out.println("");
				   matched.addAll(list);			   
			   }
			   
			   new StructureRenderer(atomContainer, matched, "matched");
				
			}
			   
			
		} catch (CDKException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
