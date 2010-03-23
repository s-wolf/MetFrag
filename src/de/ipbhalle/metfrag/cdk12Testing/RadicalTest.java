package de.ipbhalle.metfrag.cdk12Testing;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.aromaticity.CDKHueckelAromaticityDetector;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.inchi.InChIGenerator;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IChemModel;
import org.openscience.cdk.interfaces.IChemSequence;
import org.openscience.cdk.interfaces.IMoleculeSet;
import org.openscience.cdk.interfaces.IReactionSet;
import org.openscience.cdk.io.INChIPlainTextReader;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.io.MDLWriter;
import org.openscience.cdk.io.SDFWriter;
import org.openscience.cdk.io.setting.IOSetting;
import org.openscience.cdk.reaction.IReactionProcess;
import org.openscience.cdk.reaction.type.HomolyticCleavageReaction;
import org.openscience.cdk.reaction.type.parameters.IParameterReact;
import org.openscience.cdk.reaction.type.parameters.ParameterReact;
import org.openscience.cdk.reaction.type.parameters.SetReactionCenter;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;

import de.ipbhalle.metfrag.tools.Render;

public class RadicalTest {
	
	public static void main(String[] args) {
		String smiles = "C1C(OC2=CC(=CC(=C2C1=O)O)O)C3=CC=C(C=C3)O";
		SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
		IAtomContainer molRead = null;
		try {
			molRead = sp.parseSmiles(smiles);
			
			
	        CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(molRead.getBuilder());
	        for (IAtom atom : molRead.atoms()) {
	        	IAtomType type = matcher.findMatchingAtomType(molRead, atom);
		        AtomTypeManipulator.configure(atom, type);
	        }
	        CDKHydrogenAdder hAdder = CDKHydrogenAdder.getInstance(molRead.getBuilder());
	        hAdder.addImplicitHydrogens(molRead);
	        //AtomContainerManipulator.convertImplicitToExplicitHydrogens(molRead);
	        CDKHueckelAromaticityDetector.detectAromaticity(molRead);
	        
			IMoleculeSet setOfReactants = DefaultChemObjectBuilder.getInstance().newMoleculeSet();
			setOfReactants.addMolecule(new Molecule(molRead));
			IReactionProcess type = new HomolyticCleavageReaction();
			
			List<IParameterReact> paramList = new ArrayList<IParameterReact>();
	        IParameterReact param = new SetReactionCenter();
	        param.setParameter(Boolean.FALSE);
	        paramList.add(param);
	        type.setParameterList(paramList);
			
			IReactionSet setOfReactions = type.initiate(setOfReactants, null);
			IAtomContainer resultingFrag = setOfReactions.getReaction(0).getProducts().getAtomContainer(0);
			System.out.println(setOfReactions.getReaction(0).getProductCount());
			Render.Draw(resultingFrag, "test");
			
			CDKAtomTypeMatcher matcher1 = CDKAtomTypeMatcher.getInstance(resultingFrag.getBuilder());
	        for (IAtom atom : resultingFrag.atoms()) {
	        	IAtomType type1 = matcher1.findMatchingAtomType(resultingFrag, atom);
		        AtomTypeManipulator.configure(atom, type1);
	        }
	        CDKHydrogenAdder hAdder1 = CDKHydrogenAdder.getInstance(resultingFrag.getBuilder());
	        hAdder1.addImplicitHydrogens(resultingFrag);
	        AtomContainerManipulator.convertImplicitToExplicitHydrogens(resultingFrag);
	        CDKHueckelAromaticityDetector.detectAromaticity(resultingFrag);
			
	        Map<Object, Object> props = resultingFrag.getProperties();
	        props.put("BondEnergy", "1289");
	        
			FileWriter w = new FileWriter(new File("radicalTest.sdf"));
			SDFWriter sdfW = new SDFWriter(w);
			IOSetting[] settings = sdfW.getIOSettings();
			for (int i = 0; i < settings.length; i++) {
				System.out.println(settings[i].getName() + ": " + settings[i].getSetting());
			}
//	        MDLWriter mw = new MDLWriter(w);
			sdfW.write(resultingFrag);
			sdfW.close();
			
			InputStream ins = new BufferedInputStream(new FileInputStream("radicalTest.sdf"));
			MDLV2000Reader reader = new MDLV2000Reader(ins);
	        ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());
	        IChemSequence sequence = chemFile.getChemSequence(0);
	        IChemModel model = sequence.getChemModel(0);
	        IAtomContainer molReadIn = model.getMoleculeSet().getAtomContainer(0);
	        System.out.println("Read in Bond Energy: " + molReadIn.getProperties().get("BondEnergy"));

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		Render.Draw(molRead, "Read in mol!");
	}

}
