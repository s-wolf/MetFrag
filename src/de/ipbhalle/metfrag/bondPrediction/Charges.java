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
package de.ipbhalle.metfrag.bondPrediction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openscience.cdk.Atom;
import org.openscience.cdk.Bond;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.charges.GasteigerMarsiliPartialCharges;
import org.openscience.cdk.charges.GasteigerPEPEPartialCharges;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.graph.invariant.CanonicalLabeler;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.smiles.InvPair;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

import de.ipbhalle.metfrag.moldynamics.Distance;
import de.ipbhalle.metfrag.tools.MoleculeTools;
import de.ipbhalle.metfrag.tools.renderer.StructureRenderer;

public class Charges {
	
	private IAtomContainer mol;
	private Map<String, Double> bondToBondLength;
	private boolean verbose = false;
	
	/**
	 * Instantiates a new charges class.
	 * 
	 * @param mol the mol
	 */
	public Charges()
	{
		this.bondToBondLength = new HashMap<String, Double>();
	}
	
	
	public void debug()
	{
		verbose = true;
	}
	
	
	/**
	 * Calculate bonds which will most likely break.
	 * It returns a list with bonds.
	 * 
	 * @return the list< i bond>
	 * @throws CloneNotSupportedException 
	 * @throws CDKException 
	 */
	public List<String> calculateBondsToBreak(IAtomContainer mol) throws CloneNotSupportedException, CDKException
	{		
		this.mol = MoleculeTools.moleculeNumbering(mol);
		List<String> bondsToBreak = new ArrayList<String>();
		
		try {
        	GasteigerMarsiliPartialCharges peoe = new GasteigerMarsiliPartialCharges();
//        	GasteigerPEPEPartialCharges pepe = new GasteigerPEPEPartialCharges();
            AtomContainerManipulator.convertImplicitToExplicitHydrogens(this.mol);
            AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(this.mol);
    		
    		peoe.calculateCharges(this.mol);
//	    	pepe.calculateCharges(cpd);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
        

		//now get the atoms with the most electronegativity
		List<AtomProperty> atomCharges = new ArrayList<AtomProperty>();
		boolean[] atomDone = new boolean[this.mol.getAtomCount()];

		for (IBond bond : this.mol.bonds()) {			
        	for (IAtom atom : bond.atoms()) {
        		if(!atomDone[Integer.parseInt(atom.getID())])
        		{
        			AtomProperty atomProp = new AtomProperty(atom, atom.getCharge());
        			atomCharges.add(atomProp);
        			atomDone[Integer.parseInt(atom.getID())] = true;
        		}
			}	        	
		}
		
		AtomProperty[] chargesArray = new AtomProperty[atomCharges.size()];
		chargesArray = atomCharges.toArray(chargesArray);
		Arrays.sort(chargesArray);
		
//		for (int i = 0; i < chargesArray.length; i++) {
//			System.out.println(chargesArray[i].getAtom().getSymbol() + " " + chargesArray[i].getCharge());
//		}
		
		IAtomContainer[] molArray = new IAtomContainer[2];
		molArray[0] = this.mol;
		List<Distance> cpd1BondToDistance = new ArrayList<Distance>();
		
		//now add to every candidate atom a hydrogen and calculate the charges again
		for (int i = 0; i < chargesArray.length; i++) {
			
			IAtomContainer protonatedMol = null;
					
			//only atoms with partial charge < 0
			if(chargesArray[i].getCharge() > 0)
				break;			
			else
			{
				//now add hydrogen atom
				protonatedMol = (IAtomContainer) mol.clone();
				IAtom hydrogenAtom = new Atom("H");
				if(verbose)
					System.out.println("Protonation of atom: " + chargesArray[i].getAtom().getSymbol()  + (Integer.parseInt(chargesArray[i].getAtom().getID()) + 1));
				IBond hydrogenBond = new Bond(AtomContainerManipulator.getAtomById(protonatedMol, chargesArray[i].getAtom().getID()), hydrogenAtom);
				protonatedMol.addAtom(hydrogenAtom);
				protonatedMol.addBond(hydrogenBond);
				
				AtomContainerManipulator.convertImplicitToExplicitHydrogens(protonatedMol);
	            AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(protonatedMol);
//	            Render.Draw(this.mol, "original");
	            if(verbose)
	            	new StructureRenderer(protonatedMol, "protonated");
	            GasteigerMarsiliPartialCharges peoe = new GasteigerMarsiliPartialCharges();
	            peoe.calculateCharges(protonatedMol);
	            protonatedMol = MoleculeTools.moleculeNumbering(protonatedMol);
				molArray[1] = protonatedMol;
			}
			
			cpd1BondToDistance = new ArrayList<Distance>();
			List<Distance> cpd2BondToDistance = new ArrayList<Distance>();
			
			for (int j = 0; j < 2; j++) {
				
				if(!cpd1BondToDistance.isEmpty() && j == 0)
					continue;					
				
				//now compare to the original one
				for (IBond bond : molArray[j].bonds()) {
		        	IAtom atom1 = null;
		        	IAtom atom2 = null;
		        	for (IAtom atom : bond.atoms()) {
		        		if(atom1 == null)
		        			atom1 = atom;
		        		else
		        			atom2 = atom;
		        		
					}
//		        	System.out.println("Distance between " + atom1.getSymbol() + "-"  +	atom2.getSymbol() + "\t" +
//		        			atom1.getPoint3d().distance(atom2.getPoint3d()));
		        	
		        	Double atom1Charge = (Math.round(atom1.getCharge() * 1000.0)/1000.0);
		        	Double atom2Charge = (Math.round(atom2.getCharge() * 1000.0)/1000.0);
		        	
		        	Double diffCharge = Math.abs(atom1Charge - atom2Charge);
		        	
		        	Distance dist = new Distance(atom1.getSymbol() + (Integer.parseInt(atom1.getID()) + 1) + "-" + atom2.getSymbol() + (Integer.parseInt(atom2.getID()) + 1), diffCharge, bond.getID());
		        	//original molecule
		        	if(j == 0 && !cpd1BondToDistance.contains(dist))
		        		cpd1BondToDistance.add(dist);
//			        		cpd1BondToDistance.add(new Distance(atom1.getSymbol() + "-" + atom2.getSymbol(), atom1.getPoint2d().distance(atom2.getPoint2d())));
		        	else if(!cpd2BondToDistance.contains(dist))
		        		cpd2BondToDistance.add(dist);
//			        		cpd2BondToDistance.add(new Distance(atom1.getSymbol() + "-" + atom2.getSymbol(), atom1.getPoint2d().distance(atom2.getPoint2d())));
				}
			}
			
			
			List<String> notMatched = new ArrayList<String>();
			
			//now compare the results
			int offset = 0;
			for (int i1 = 0; i1 < cpd1BondToDistance.size(); i1++) {
				Double dist = -999.9;
				if(cpd1BondToDistance.get(i1).getBond().equals(cpd2BondToDistance.get(i1 + offset).getBond()))
					dist = (cpd2BondToDistance.get(i1 + offset).getBondLength() - cpd1BondToDistance.get(i1).getBondLength());
				else
				{
					notMatched.add(cpd2BondToDistance.get(i1 + offset).getBond());
					offset++;
					dist = (cpd2BondToDistance.get(i1 + offset).getBondLength() - cpd1BondToDistance.get(i1).getBondLength());
				}
				double distRound = Math.round(dist*1000.0)/1000.0;
				
				//now save only the maximum bond length change...
				bondToBondLength = saveMaximum(bondToBondLength, cpd1BondToDistance.get(i1).getBondID(), distRound);
				
				if(verbose)
					System.out.println(cpd1BondToDistance.get(i1).getBond() + " " + cpd1BondToDistance.get(i1).getBondLength() + " " + cpd2BondToDistance.get(i1 + offset).getBondLength() + ": " + distRound);
			}
			
//			for (String string : notMatched) {
//				System.out.println(string);
//			}

		}
		
		for (IBond bond : molArray[0].bonds()) {
//			System.out.println(bond.getID() + " " + bond.getAtom(0).getSymbol() + "-" + bond.getAtom(1).getSymbol() + " " + bondToBondLength.get(bond.getID()));
			
			//if it is C-C check for double or triple bond order
			if(!isCandidateBond(bond))
				continue;
			
			if(bondToBondLength.get(bond.getID()) > 0.02)
				bondsToBreak.add(bond.getID());				
		}
		
//		for (String bondID : bondsToBreak) {
//			System.out.println(bondID);
//		}

		return bondsToBreak;
            
	}
	
	
	private Map<String, Double> saveMaximum(Map<String, Double> bondToBondLength, String bondID, Double dist)
	{
		if(bondToBondLength.get(bondID) == null)
			bondToBondLength.put(bondID, dist);
		else if(bondToBondLength.get(bondID) < dist)
			bondToBondLength.put(bondID, dist);
		
		return bondToBondLength;
	}
	
	
	/**
	 * Checks if it is candidate bond. In a C-C bond it must be a double or
	 * triple bond.
	 * 
	 * @return true, if is candidate bond
	 */
	private boolean isCandidateBond(IBond bond)
	{
		boolean check = false;
		//only protonate carbon atom where a double or triple bond is
		if(bond.getAtom(1).getSymbol().equals("C") && bond.getAtom(0).getSymbol().equals("C"))
		{
			if(bond.getOrder().equals(IBond.Order.DOUBLE) || bond.getOrder().equals(IBond.Order.TRIPLE))
				check = true;
		}
		//dont break bonds with hydrogen
		else if(bond.getAtom(1).getSymbol().equals("H") || bond.getAtom(0).getSymbol().equals("H"))
			check = false;
		else
			check = true;
		
		return check;
	}
	
	/**
	 * Gets the bond length for a specified bond ID.
	 * Molecule bonds are numbered using MoleculeTools.moleculeNumbering
	 * 
	 * @param bondID the bond id
	 * 
	 * @return the bond length
	 */
	public Double getBondLength(String bondID)
	{
		return this.bondToBondLength.get(bondID);
	}
	
	public static void main(String[] args) {
		
		try {
			String naringenin = "C1C(OC2=CC(=CC(=C2C1=O)O)O)C3=CC=C(C=C3)O";
			SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
			IMolecule m = sp.parseSmiles(naringenin);
			
			AtomContainerManipulator.convertImplicitToExplicitHydrogens(m);
            AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(m);
            
            MoleculeTools.moleculeNumbering(m);
            
//            IAtom s = new Atom("O");
//            IAtom test = m.getAtom(1); // getAtomById(m, "0");
//            test.setSymbol("S");
//            m.setAtom(1, s);
            
            AtomContainerManipulator.convertImplicitToExplicitHydrogens(m);
            AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(m);
            
			new StructureRenderer(m, "Naringenin"); 
			IAtom[] atomList = AtomContainerManipulator.getAtomArray(m);			
			for (int i = 0; i < atomList.length; i++) {
				System.out.println(i + " id:" + (Integer.parseInt(atomList[i].getID()) + 1) +  " symbol:" + atomList[i].getSymbol() + " atomicNumber:" + atomList[i].getAtomicNumber());
			}
//			for (IBond bond : m.bonds()) {
//				for (IAtom atom : bond.atoms()) {
//					if(!alreadyDone.contains(atom))
//					{
//						System.out.println(atom.getAtomicNumber());
//					}
//				}
//			}
			
			
			Charges charges = new Charges();
			charges.debug();
			charges.calculateBondsToBreak(m);
			
			
			
		} catch (InvalidSmilesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CDKException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
	

}
