package de.ipbhalle.metfrag.similarity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.smsd.Isomorphism;
import org.openscience.cdk.smsd.interfaces.Algorithm;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

public class Subgraph {
	
	/**
	 * Gets the subgraphs using SMSD.
	 * [Rahman, S.A. and Bashton, M. and Holliday, G.L. and Schrader, R. and Thornton, J.M. ,
	 * Small Molecule Subgraph Detector (SMSD) Toolkit, Journal of Cheminformatics, 2009, 1:12]
	 * 
	 * This method returns the matching subgraphs. Those parts can be highlighted.
	 * 
	 * @param query the query
	 * @param target the target
	 * @param onlyBestMatch or return every mapping (false)
	 * @param removeHydrogens the remove hydrogens
	 * 
	 * @return the subgraphs
	 * 
	 * @throws CDKException the CDK exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws CloneNotSupportedException the clone not supported exception
	 */
	public static IAtomContainer getSubgraphsSMSD(IAtomContainer query, IAtomContainer target, boolean onlyBestMatch, boolean removeHydrogens) throws CDKException, IOException, CloneNotSupportedException
    {
    	IAtomContainer subgraph = DefaultChemObjectBuilder.getInstance().newInstance(AtomContainer.class);
		Isomorphism comparison = new Isomorphism(Algorithm.SubStructure, true);
		
		IMolecule targetTemp = new Molecule((IAtomContainer)target.clone());
		
		
		AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(query);
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(targetTemp);
        
        if(removeHydrogens)
        {
        	query = (IMolecule) AtomContainerManipulator.removeHydrogens(query);
            targetTemp = (IMolecule) AtomContainerManipulator.removeHydrogens(targetTemp);
        }
        

		// set molecules and remove hydrogens
		comparison.init(query, targetTemp, removeHydrogens, false);
		// set chemical filter true
		comparison.setChemFilters(false, false, false);

		// Print the mapping between molecules
//		System.out.println(" Mappings: ");
		List<IAtom> matchedAtoms = new ArrayList<IAtom>();
		if (comparison.isSubgraph() && onlyBestMatch) {
			for (Map.Entry<Integer, Integer>  mapping : comparison.getFirstMapping().entrySet()) {			
				
				//Get the mapped atom number in Query Molecule
                int queryMappingNumber = mapping.getKey();
                //Get the mapped atom number in Target Molecule
                int targetMappingNumber = mapping.getValue();
                //Get the mapped atom in Query Molecule
                IAtom queryAtom = query.getAtom(queryMappingNumber);
                //Get the mapped atom in Target Molecule
                IAtom targetAtom = targetTemp.getAtom(targetMappingNumber);

                //Print mapped atom numbers
//                System.out.println(queryMappingNumber + " "
//                        + (targetMappingNumber));
//                //Print mapped atoms
//                System.out.println(queryAtom.getSymbol() + " "
//                        + targetAtom.getSymbol());

				
				IAtom tAtom = target.getAtom(mapping.getValue());
				matchedAtoms.add(tAtom);
				subgraph.addAtom(tAtom);
			}
			
			for (IAtom atom1 : matchedAtoms) {
				for (IAtom atom2 : matchedAtoms) {
					if (!atom1.equals(atom2))
					{
						IBond bond = target.getBond(atom1, atom2);
						if(bond != null)
							subgraph.addBond(bond);
					}
				}
			}
		}
		//this gets all mapped atoms
		else if(comparison.isSubgraph())
		{
			for (Map<Integer, Integer>  map : comparison.getAllMapping()) {	
				for (Map.Entry<Integer, Integer>  mapping : map.entrySet()) {
					//Get the mapped atom number in Query Molecule
	                int queryMappingNumber = mapping.getKey();
	                //Get the mapped atom number in Target Molecule
	                int targetMappingNumber = mapping.getValue();
	                //Get the mapped atom in Query Molecule
	                IAtom queryAtom = query.getAtom(queryMappingNumber);
	                //Get the mapped atom in Target Molecule
	                IAtom targetAtom = targetTemp.getAtom(targetMappingNumber);

//	                //Print mapped atom numbers
//	                System.out.println(queryMappingNumber + " " + (targetMappingNumber));
//	                //Print mapped atoms
//	                System.out.println(queryAtom.getSymbol() + " " + targetAtom.getSymbol());
					
					IAtom tAtom = target.getAtom(mapping.getValue());
					matchedAtoms.add(tAtom);
					subgraph.addAtom(tAtom);
				}
			}
			
			for (IAtom atom1 : matchedAtoms) {
				for (IAtom atom2 : matchedAtoms) {
					if (!atom1.equals(atom2))
					{
						IBond bond = target.getBond(atom1, atom2);
						if(bond != null)
							subgraph.addBond(bond);
					}
				}
			}
		}
			

		return subgraph;
    }
	
	
	
	/**
	 * Gets the inverted subgraph. Thus it returns an atomcontainer containing only bonds and atoms which were
	 * not contained in the subgraph. 
	 * 
	 * @param mol the mol 
	 * @param subgraph the subgraph of the mol which was previously calculated in getSubgraphsSMSD
	 * 
	 * @return the inverted subgraph
	 */
	public static IAtomContainer getInvertedSubgraph(IAtomContainer mol, IAtomContainer subgraph)
	{
		IAtomContainer invertedSubgraph = DefaultChemObjectBuilder.getInstance().newInstance(AtomContainer.class);
		
		//now add every atom which is not contained in the subgraph
		for (IAtom atom : mol.atoms()) {
			if(!subgraph.contains(atom))
			{
				invertedSubgraph.addAtom(atom);
			}
		}
		
		for (IBond bond : mol.bonds()) {
			if(!subgraph.contains(bond))
			{
				invertedSubgraph.addBond(bond);
			}
		}
		
		return invertedSubgraph;
	}
	
	
	/**
	 * Gets the MCS using SMSD. TODO: not done yet
	 * http://www.ebi.ac.uk/thornton-srv/software/SMSD/MCSSearch.java
	 * [Rahman, S.A. and Bashton, M. and Holliday, G.L. and Schrader, R. and Thornton, J.M. , 
	 * Small Molecule Subgraph Detector (SMSD) Toolkit, Journal of Cheminformatics, 2009, 1:12]
	 * 
	 * This method returns the matching subgraphs. Those parts can be highlighted.
	 * 
	 * @param mol1
	 * @param mol2
	 * 
	 * @return the subgraphs
	 * 
	 * @throws CDKException the CDK exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws CloneNotSupportedException 
	 */
	public static IAtomContainer getMCSSMSD(IAtomContainer mol1, IAtomContainer mol2) throws CDKException, IOException, CloneNotSupportedException
    {
    	IAtomContainer subgraph = DefaultChemObjectBuilder.getInstance().newInstance(AtomContainer.class);
		Isomorphism comparison = new Isomorphism(Algorithm.SubStructure, true);
		
		IMolecule A1 = new Molecule((IAtomContainer)mol1.clone());
		IMolecule A2 = new Molecule((IAtomContainer)mol2.clone());
		
		AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(A1);
        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(A2);

        A1 = (IMolecule) AtomContainerManipulator.removeHydrogens(A1);
        A2 = (IMolecule) AtomContainerManipulator.removeHydrogens(A2);

		// set molecules and remove hydrogens
		comparison.init(A1, A2, true, false);
		// set chemical filter true
		comparison.setChemFilters(false, false, false);
		if (comparison.getAllMapping() != null) {
			//Get similarity score
			System.out.println("A1 is a subgraph of A2:  " + comparison.isSubgraph());
			// Print the mapping between molecules
//			System.out.println(" Mappings: ");
			List<IAtom> matchedAtoms = new ArrayList<IAtom>();
			for (Map.Entry<Integer, Integer>  mapping : comparison.getFirstMapping().entrySet()) {			
				IAtom eAtom = mol1.getAtom(mapping.getKey());
//				IAtom pAtom = Mol2.getAtom(mapping.getValue());
				matchedAtoms.add(eAtom);
				subgraph.addAtom(eAtom);
			}
			
			for (IAtom atom1 : matchedAtoms) {
				for (IAtom atom2 : matchedAtoms) {
					if (!atom1.equals(atom2))
					{
						IBond bond = mol1.getBond(atom1, atom2);
						if(bond != null)
							subgraph.addBond(bond);
					}
				}
			}
			
		}
		
		return subgraph;
    }

}
