package de.ipbhalle.metfrag.cdk12Testing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.StringReader;
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
import de.ipbhalle.metfrag.read.Molfile;
import de.ipbhalle.metfrag.spectrum.WrapperSpectrum;
import de.ipbhalle.metfrag.tools.MolecularFormulaTools;
import de.ipbhalle.metfrag.tools.Render;

public class NaringeninTest {
	
	private Vector<File> l1 = null;
	private List<IAtomContainer> l2 = null;
	WrapperSpectrum spectrum = null;
	double mzabs = 0.01;
	double mzppm = 50.0;
	
	public void startTest () throws FileNotFoundException, CDKException
	{
		String candidate = "C00509";
		double exactMass = 272.06847;
		String peaks = "119.051 467.616 45\n" +
		   "123.044 370.662 36\n" +
		   "147.044 6078.145 606\n" +
		   "153.019 10000.0 999\n" +
		   "179.036 141.192 13\n" +
		   "189.058 176.358 16\n";
		int mode = 1;

		spectrum = new WrapperSpectrum(peaks, mode, exactMass);		
			
	    //get mol file from kegg....remove "cpd:"
		String candidateMol = "";
		IAtomContainer molecule = null;
				
		candidateMol = KeggWebservice.KEGGgetMol("C00509", "/vol/data/pathways/kegg/mol/");
		MDLReader reader;
		List<IAtomContainer> containersList;
		
	    reader = new MDLReader(new StringReader(candidateMol));
	    ChemFile chemFile = null;
		try {
			chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());
		} catch (CDKException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	    containersList = ChemFileManipulator.getAllAtomContainers(chemFile);
	    molecule = containersList.get(0);
				
		
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
//			l1 = fragmenter.generateFragmentsEfficient(molecule, true, 2, "C00509");
//			l2 = Molfile.ReadfolderTemp(l1);
	    	l2 = fragmenter.generateFragmentsInMemory(molecule, true, 2);
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
		NaringeninTest t = new NaringeninTest();
		try {
			t.startTest();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CDKException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
