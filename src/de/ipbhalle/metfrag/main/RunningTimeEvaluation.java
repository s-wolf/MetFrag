package de.ipbhalle.metfrag.main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.formula.MolecularFormula;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

import de.ipbhalle.metfrag.fragmenter.Fragmenter;
import de.ipbhalle.metfrag.massbankParser.Peak;
import de.ipbhalle.metfrag.molDatabase.PubChemLocal;
import de.ipbhalle.metfrag.tools.MolecularFormulaTools;
import de.ipbhalle.metfrag.tools.PPMTool;

public class RunningTimeEvaluation {
	
	/**
	 * The method automatically retrieves 
	 * 
	 * @param args the arguments
	 */
	public static void main(String[] args) {

		double minMass = 100.0;
		double maxMass = 1000.0;
		int maxCandidates = 100;
		int candidateCount = 0;
		int massIncrease = 10;
		double randomIncrease = Math.random() * 10;
		Vector<Peak> peakList = new Vector<Peak>();
		Peak peak = new Peak(30.0, 999.0, 10);
		peakList.add(peak);
				
		// Create file 
	    FileWriter fstream;
		try {
			
			Config c = new Config();
	        String url = c.getJdbc();
	        String username = c.getUsername();
	        String password = c.getPassword();
			PubChemLocal pubchem = new PubChemLocal(url, username, password);
			double currentMass = minMass + randomIncrease;
			
			fstream = new FileWriter("performance_td3.log", true);
			
			BufferedWriter out = new BufferedWriter(fstream);
			while(currentMass < maxMass)
			{
				while(candidateCount < maxCandidates)
				{
					double lowerBound = currentMass - 0.5; 
					double upperBound = currentMass + 0.5; 
					Vector<String> candidates = new Vector<String>();
					
						
						try {
							candidates = pubchem.getHitsVector(lowerBound, upperBound);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (ClassNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						for (String candID : candidates) {
							IAtomContainer mol = null;
							try {
								mol = pubchem.getMol(candID, true);
								AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol);
						        CDKHydrogenAdder hAdder = CDKHydrogenAdder.getInstance(mol.getBuilder());
						        hAdder.addImplicitHydrogens(mol);
						        AtomContainerManipulator.convertImplicitToExplicitHydrogens(mol);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								continue;
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								continue;
							} catch (InvalidSmilesException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								continue;
							} catch (CDKException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								continue;
							}
							IMolecularFormula formula = new MolecularFormula();
							formula = MolecularFormulaManipulator.getMolecularFormula(mol, formula);
							double exactMass = MolecularFormulaTools.getMonoisotopicMass(formula);
							Fragmenter fragmenter = new Fragmenter(peakList, 0, 10, 1, true, false, true, true);
							List<IAtomContainer> results = new ArrayList<IAtomContainer>();
							long start = System.currentTimeMillis(); // start timing
					        try {
								results = fragmenter.generateFragmentsInMemory(mol, false, 3);
							} catch (CDKException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								continue;
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								continue;
							}
					        long stop = System.currentTimeMillis(); // stop timing
							
							out.write(candID + "\t");
							out.write(exactMass + "\t");
							out.write(results.size() + "\t");
							out.write((stop - start) + "\t");
							out.newLine();
							
							candidateCount++;
							if(candidateCount >= maxCandidates)
								break;
						}
						
						out.flush();
				}
				System.out.println("\n\n");
				candidateCount = 0;
				randomIncrease = Math.random() * 10;
				currentMass = currentMass + massIncrease + randomIncrease;
			}
			out.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	        
	    
		
		
		
		
	}

}
