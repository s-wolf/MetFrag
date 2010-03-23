package de.ipbhalle.metfrag.cdk12Testing;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.openscience.cdk.Atom;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.inchi.InChIGenerator;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.INChIPlainTextReader;


public class InChIBugTest {
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		try
		{
			InChIGeneratorFactory factory = new InChIGeneratorFactory();
			IAtomContainer ac = new AtomContainer();
	        ac.addAtom(new Atom("Cl"));
	        InChIGenerator gen = factory.getInChIGenerator(ac);
	        System.out.println(gen.getInchi());//, "InChI=1/Cl");
	        
	        
	        InputStream is = new ByteArrayInputStream((gen.getInchi() + "\n" + gen.getAuxInfo()).getBytes("UTF-8"));
	        INChIPlainTextReader reader1 = new INChIPlainTextReader(is);
	        ChemFile chemFile1 = (ChemFile)reader1.read((ChemObject)new ChemFile());
	        System.out.println(chemFile1.getChemSequenceCount());
		}
		catch(CDKException e)
		{
			System.err.println("CDK Error: " + e.getMessage());
		}

	}

}
