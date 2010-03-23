package de.ipbhalle.metfrag.cdk12Testing;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import net.sf.jniinchi.INCHI_OPTION;
import net.sf.jniinchi.INCHI_RET;

import org.openscience.cdk.Atom;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.ChemSequence;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.aromaticity.CDKHueckelAromaticityDetector;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.inchi.InChIGenerator;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IChemModel;
import org.openscience.cdk.interfaces.IChemSequence;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.io.INChIPlainTextReader;
import org.openscience.cdk.io.INChIReader;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;
import org.xml.sax.InputSource;

public class InChITest {
	
	
	public static void main(String[] args) {
		try
		{
			
			InputStream ins = new BufferedInputStream(new FileInputStream("naringenin.mol"));
	        MDLV2000Reader reader = new MDLV2000Reader(ins);
	        ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());
	        IChemSequence sequence = chemFile.getChemSequence(0);
	        IChemModel model = sequence.getChemModel(0);
	        IAtomContainer molRead = model.getMoleculeSet().getAtomContainer(0);
	        
	        CDKHueckelAromaticityDetector.detectAromaticity(molRead);
	        CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(molRead.getBuilder());
	        for (IAtom atom : molRead.atoms()) {
	        	IAtomType type = matcher.findMatchingAtomType(molRead, atom);
		        AtomTypeManipulator.configure(atom, type);
	        }
	        CDKHydrogenAdder hAdder = CDKHydrogenAdder.getInstance(molRead.getBuilder());
	        hAdder.addImplicitHydrogens(molRead);
	        AtomContainerManipulator.convertImplicitToExplicitHydrogens(molRead);
	        
			InChIGeneratorFactory factory = new InChIGeneratorFactory();
	        InChIGenerator gen = factory.getInChIGenerator(molRead);
	        
	        String inchi = gen.getInchi();
	        String auxinfo = gen.getAuxInfo();	        
	        
	        INCHI_RET ret = gen.getReturnStatus();
	        if (ret == INCHI_RET.WARNING) {
		        // InChI generated, but with warning message
		        System.out.println("InChI warning: " + gen.getMessage());
	        } else if (ret != INCHI_RET.OKAY) {
	        	// InChI generation failed
	        	throw new CDKException("InChI failed: " + ret.toString() + " [" + gen.getMessage() + "]");
	        }
	        
	        System.out.println(inchi + "\n" + auxinfo);
	        
	        
	        String naringenin = "INChI=1/C15H12O5/c16-9-3-1-8(2-4-9)13-7-12(19)15-11(18)5-10(17)6-14(15)20-13/h1-6,13,16-18H,7H2";
	        String working = "INChI=1S/C2H6O/c1-2-3/h3H,2H2,1H3";
	        
	        //working test case
	        InputStream is1 = new ByteArrayInputStream(working.getBytes("UTF-8"));
	        INChIPlainTextReader reader2 = new INChIPlainTextReader(is1);
	        ChemFile chemFile2 = (ChemFile)reader2.read((ChemObject)new ChemFile());
	        System.out.println(chemFile2.getChemSequenceCount());
	        
	        //not working
	        InputStream is = new ByteArrayInputStream(naringenin.getBytes("UTF-8"));
	        INChIPlainTextReader reader1 = new INChIPlainTextReader(is);
	        ChemFile chemFile1 = (ChemFile)reader1.read((ChemObject)new ChemFile());
	        System.out.println(chemFile1.getChemSequenceCount());
	        
	        
	        
		}
		catch(CDKException e)
		{
			System.err.println("CDK Error: " + e.getMessage());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}