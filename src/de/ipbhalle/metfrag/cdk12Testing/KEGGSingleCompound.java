package de.ipbhalle.metfrag.cdk12Testing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.MDLReader;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

import de.ipbhalle.metfrag.fragmenter.Fragmenter;
import de.ipbhalle.metfrag.keggWebservice.KeggWebservice;
import de.ipbhalle.metfrag.massbankParser.Peak;
import de.ipbhalle.metfrag.molDatabase.KEGGLocal;
import de.ipbhalle.metfrag.read.Molfile;
import de.ipbhalle.metfrag.spectrum.WrapperSpectrum;
import de.ipbhalle.metfrag.tools.MolecularFormulaTools;
import de.ipbhalle.metfrag.tools.Render;

public class KEGGSingleCompound {
	
	private List<File> l1 = null;
	private List<IAtomContainer> l2 = null;
	WrapperSpectrum spectrum = null;
	double mzabs = 0.01;
	double mzppm = 50.0;
	
	public void startTest () throws FileNotFoundException, CDKException, SQLException, ClassNotFoundException
	{
		String candidate = "C00509";
		double exactMass = 146.06914;
		String peaks = "41.04 258.777 24.0\n" +
				"56.051 1550.778 154.0\n" +
				"74.024 58.909 4.0\n" +
				"84.045 10000.0 999.0\n" +
				"85.029 200.809 19.0\n" +
				"101.074 852.547 84.0\n" +
				"102.057 423.27 41.0\n" +
				"130.051 10000.0 999.0\n" +
				"147.077 10000.0 999.0\n";
		
		int mode = 1;
		spectrum = new WrapperSpectrum(peaks, mode, exactMass);		
			
	    //get mol file from kegg....remove "cpd:"
		String candidateMol = "";
		IAtomContainer molecule = null;
				
		KEGGLocal molKEGG = new KEGGLocal("jdbc:mysql://rdbms/MetFrag", "swolf", "populusromanus");
		molecule = molKEGG.getMol("C00064");				
		
	    //add hydrogens
	    try {
			AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
		} catch (CDKException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    CDKHydrogenAdder hAdder = CDKHydrogenAdder.getInstance(molecule.getBuilder());
	    try {
			hAdder.addImplicitHydrogens(molecule);
		} catch (CDKException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    AtomContainerManipulator.convertImplicitToExplicitHydrogens(molecule);

		Double massDoubleOrig = MolecularFormulaTools.getMonoisotopicMass(MolecularFormulaManipulator.getMolecularFormula(molecule));
		massDoubleOrig = (double)Math.round((massDoubleOrig)*10000)/10000;
		String massOrig = massDoubleOrig.toString();
		
		        
	    Fragmenter fragmenter = new Fragmenter((Vector<Peak>)spectrum.getPeakList().clone(), mzabs, mzppm, mode, true, true, false, false);
	    try {
			l1 = fragmenter.generateFragmentsEfficient(molecule, true, 2, "C00509");
			l2 = Molfile.ReadfolderTemp(l1);
//	    	l2 = fragmenter.generateFragments(molecule, true, 2);
	    	System.out.println("Fragments: " + l2.size());
		} catch (CDKException e1) {
			e1.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
//		Render.Draw(molecule, Molfile.ReadfolderTemp(l1), "Naringenin");
		Render.Draw(molecule, l2, "Naringenin");
	}
	
	public static void main(String[] args) {
		KEGGSingleCompound test = new KEGGSingleCompound();
		try {
			test.startTest();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CDKException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
