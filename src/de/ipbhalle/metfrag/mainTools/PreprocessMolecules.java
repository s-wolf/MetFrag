package de.ipbhalle.metfrag.mainTools;

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
	
	// /home/swolf/MOPAC/ProofOfConcept/pubchem/CID_20097272_spectrum/20097272.sdf /home/swolf/MOPAC/ProofOfConcept/pubchem/CID_20097272_spectrum/mopac/ 600 600
	// LARGE BUG: /home/swolf/MOPAC/ProofOfConcept/pubchem/CID_3002977_spectrum/3002977.sdf /home/swolf/MOPAC/ProofOfConcept/pubchem/CID_3002977_spectrum/mopac/ 600 600
	public static void main(String[] args) {
		
		File file = null;
		String outputFolder = "";
		int mopacRuntime = 0;
		int ffSteps = 600;
		
		if(args.length < 3)
		{
			System.err.println("Not all parameters given!");
			System.exit(1);
		}
		else
		{
			file = new File(args[0]);
			outputFolder = args[1];
			mopacRuntime = Integer.parseInt(args[2]);
			if(args.length > 3)
				ffSteps = Integer.parseInt(args[3]);
		}
		
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
				bp.calculateBondsToBreak(molecule, ffSteps, "AM1", mopacRuntime);
				
				List<ChargeResult> results = bp.getResults();
				for (int i1 = 0; i1 < results.size(); i1++) {
					try {
						new File(outputFolder).mkdirs();
						CMLWriter writerCML = new CMLWriter(new FileOutputStream(new File(outputFolder + file.getName() + "_" + results.get(i1).getProtonatedAtom() + ".cml")));
						//thats the molecule containing the all the bond length changes from all protonation sites
						if(i1 == 0)
							writerCML.write(results.get(i1).getMolWithProton());
						//thats the mol containing the individual changes from one protonation site
						else
							writerCML.write(results.get(i1).getMolWithProton());
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
