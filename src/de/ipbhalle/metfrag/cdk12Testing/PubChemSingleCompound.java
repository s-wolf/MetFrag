package de.ipbhalle.metfrag.cdk12Testing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
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
import de.ipbhalle.metfrag.main.AssignFragmentPeak;
import de.ipbhalle.metfrag.main.PeakMolPair;
import de.ipbhalle.metfrag.massbankParser.Peak;
import de.ipbhalle.metfrag.molDatabase.KEGGLocal;
import de.ipbhalle.metfrag.molDatabase.PubChemLocal;
import de.ipbhalle.metfrag.read.Molfile;
import de.ipbhalle.metfrag.spectrum.WrapperSpectrum;
import de.ipbhalle.metfrag.tools.MolecularFormulaTools;
import de.ipbhalle.metfrag.tools.Render;


public class PubChemSingleCompound {
		
		private List<File> l1 = null;
		private List<IAtomContainer> l2 = null;
		WrapperSpectrum spectrum = null;
		double mzabs = 0;
		double mzppm = 15;
		
		public void startTest () throws CDKException, SQLException, ClassNotFoundException, IOException
		{
			String candidate = "8003";
			double exactMass = 146.06914;
			String peaks = "  130.086 298.781 30\n" +
					"134.0965 131.413 13\n" +
					"160.112 349.18 35\n" +
					"234.1449 10000 999\n" +
					"303.1676 2689.935 269";
			
			int mode = 1;
			spectrum = new WrapperSpectrum(peaks, mode, exactMass);		
				
		    //get mol file from kegg....remove "cpd:"
			String candidateMol = "";
			IAtomContainer molecule = null;
					
			PubChemLocal molPubChem = new PubChemLocal("jdbc:mysql://rdbms/MetFrag", "swolf", "populusromanus");
			molecule = molPubChem.getMol(candidate, false);				
			
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
				l1 = fragmenter.generateFragmentsEfficient(molecule, true, 2, candidate);
				l2 = Molfile.ReadfolderTemp(l1);
//		    	l2 = fragmenter.generateFragments(molecule, true, 2);
		    	System.out.println("Fragments: " + l2.size());
			} catch (CDKException e1) {
				e1.printStackTrace();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
//			Render.Draw(molecule, Molfile.ReadfolderTemp(l1), "Naringenin");
			Render.Draw(molecule, l2, "Naringenin");
			
			AssignFragmentPeak afp = new AssignFragmentPeak();
			afp.setHydrogenTest(true);
			afp.AssignFragmentPeak(l2, spectrum.getPeakList(), mzabs, mzppm, spectrum.getMode(), false);
			Vector<PeakMolPair> hits = afp.getHits();
			
			Render.DrawHits(molecule, hits, "");
			
		}
		
		public static void main(String[] args) {
			PubChemSingleCompound test = new PubChemSingleCompound();
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
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	
}
