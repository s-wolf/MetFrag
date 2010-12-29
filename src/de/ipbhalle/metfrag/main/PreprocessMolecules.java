package de.ipbhalle.metfrag.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.aromaticity.AromaticityCalculator;
import org.openscience.cdk.aromaticity.CDKHueckelAromaticityDetector;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IRing;
import org.openscience.cdk.interfaces.IRingSet;
import org.openscience.cdk.io.CMLWriter;
import org.openscience.cdk.io.MDLReader;
import org.openscience.cdk.ringsearch.AllRingsFinder;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;

import de.ipbhalle.metfrag.bondPrediction.BondPrediction;
import de.ipbhalle.metfrag.bondPrediction.ChargeResult;
import de.ipbhalle.metfrag.tools.MoleculeTools;

public class PreprocessMolecules {
	
	public static void main(String[] args) {
		
		File file = null;
		
		if(args.length < 1)
		{
			System.err.println("No parameter given!");
			System.exit(1);
		}
		else
			file = new File(args[0]);
		
		if(file.isFile())
		{
			try {
				MDLReader reader = new MDLReader(new FileReader(file));
//				MDLReader reader = new MDLReader(new FileReader(new File("/vol/mirrors/kegg/mol/C00509.mol")));
				ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());
		        List<IAtomContainer> molList = ChemFileManipulator.getAllAtomContainers(chemFile);
		        IAtomContainer molecule = molList.get(0);
		        
				//add hydrogens
		        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
		        CDKHydrogenAdder hAdder = CDKHydrogenAdder.getInstance(molecule.getBuilder());
		        hAdder.addImplicitHydrogens(molecule);
		        AtomContainerManipulator.convertImplicitToExplicitHydrogens(molecule);
		        
		        //mark all the bonds and atoms with numbers --> identify them later on        
		        molecule = MoleculeTools.moleculeNumbering(molecule);
		        
		        //do ring detection with the original molecule
		        AllRingsFinder allRingsFinder = new AllRingsFinder();
		        allRingsFinder.setTimeout(100000);
		        IRingSet allRings = allRingsFinder.findAllRings(molecule);
		        List<IBond> aromaticBonds = new ArrayList<IBond>();
		        
		        CDKHueckelAromaticityDetector.detectAromaticity(molecule);
		        
		    	for (IBond bond : molecule.bonds()) {
		            boolean aromatic = false;
		            //lets see if it is a ring and aromatic
		            IRingSet rings = allRings.getRings(bond);
		            //don't split up aromatic rings...see constructor for option
		        	for (int i1 = 0; i1 < rings.getAtomContainerCount(); i1++) {
		        		aromatic =  AromaticityCalculator.isAromatic((IRing)rings.getAtomContainer(i1), molecule);
		            	if(aromatic)
		            	{
		            		aromatic = true;
		            		aromaticBonds.add(bond);
		            		break;
		            	}
					}
		        }
		    	
		    	BondPrediction bp = new BondPrediction(aromaticBonds);
			    bp.debug(false);
				bp.calculateBondsToBreak(molecule, 600, "AM1");
				
				List<ChargeResult> results = bp.getResults();
				for (int i1 = 0; i1 < results.size(); i1++) {
					try {
						CMLWriter writerCML = new CMLWriter(new FileOutputStream(new File("/home/swolf/MOPAC/KEGG/" + file.getName() + "_" + results.get(i1).getProtonatedAtom() + ".cml")));
						writerCML.write(results.get(i1).getOriginalMol());
						writerCML.close();
						
					} catch (CDKException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (FileNotFoundException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			} catch (CDKException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			
		}
	}
}
