package de.ipbhalle.metfrag.cdk12Testing;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.aromaticity.AromaticityCalculator;
import org.openscience.cdk.aromaticity.CDKHueckelAromaticityDetector;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.IRing;
import org.openscience.cdk.interfaces.IRingSet;
import org.openscience.cdk.ringsearch.AllRingsFinder;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;

import de.ipbhalle.metfrag.fragmenter.Fragmenter;

public class SmilesTest {
	
	public static void main(String[] args) throws Exception {
		
		boolean pdf = true;
		
		String smiles1 = "C1=CC=C(C=C1)=O";
		String smiles2 = "C1C(OC2=CC(=CC(=C2C1=O)O)O)C3=CC=C(C=C3)O";
		SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
		try {
			IAtomContainer molecule = sp.parseSmiles(smiles1);
			
			IAtomContainer molecule1 = sp.parseSmiles(smiles2);
			
			
	        
//	        CDKAtomTypeMatcher matcher1 = CDKAtomTypeMatcher.getInstance(molecule1.getBuilder());
//			for (IAtom atom : molecule.atoms()) {
//	          IAtomType type = matcher1.findMatchingAtomType(molecule1,atom);
//	          AtomTypeManipulator.configure(atom, type);
//	        }
	        CDKHydrogenAdder adder1 = CDKHydrogenAdder.getInstance(molecule1.getBuilder());
	        adder1.addImplicitHydrogens(molecule1);
	        AtomContainerManipulator.convertImplicitToExplicitHydrogens(molecule1);        
	        
	        
	        //now retrieve the search results
	        String driver = "com.mysql.jdbc.Driver"; 
	        Connection con = null; 
			Class.forName(driver); 
			DriverManager.registerDriver (new com.mysql.jdbc.Driver()); 
	        // JDBC-driver
	        Class.forName(driver);
	        //databse data
	        String url = "jdbc:mysql://rdbms/FragSearch"; 
	        String username = "swolf"; 
	        String password = "populusromanus"; 
	        con = DriverManager.getConnection(url, username, password);
		    
	        Statement stmt = null;
		    stmt = con.createStatement();
		    ResultSet rs = stmt.executeQuery("SELECT id, smiles FROM PubChem WHERE chonsp = 1 and id = 12220");
		    SmilesParser sp1 = new SmilesParser(DefaultChemObjectBuilder.getInstance());
		    while(rs.next())
		    {
		    	//Name
		    	System.out.print(rs.getString("id") + "\t");
		    	molecule = sp1.parseSmiles(rs.getString("smiles"));
		    }
	        con.close();
	        
//	        CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(molecule.getBuilder());
//			Iterator<IAtom> atoms = molecule.atoms();
//	        while (atoms.hasNext()) {
//	          IAtom atom = atoms.next();
//	          IAtomType type = matcher.findMatchingAtomType(molecule,atom);
//	          AtomTypeManipulator.configure(atom, type);
//	        }
//	        CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(molecule.getBuilder());
//	        adder.addImplicitHydrogens(molecule);
//	        AtomContainerManipulator.convertImplicitToExplicitHydrogens(molecule);
	        
//	        CDKHueckelAromaticityDetector.detectAromaticity(molecule);
	        
//	        CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(molecule.getBuilder());
//	        for (IAtom atom : molecule.atoms()) {
//	          IAtomType type = matcher.findMatchingAtomType(molecule, atom);
//	          AtomTypeManipulator.configure(atom, type);
//	        }
//	        CDKHydrogenAdder hAdder = CDKHydrogenAdder.getInstance(molecule.getBuilder());
//	        hAdder.addImplicitHydrogens(molecule);
//	        AtomContainerManipulator.convertImplicitToExplicitHydrogens(molecule);
	        
	        
	        
	        de.ipbhalle.metfrag.tools.Render.Draw(molecule, "from db");
	        //Vector<Peak> peakList = new Vector<Peak>();
	        //peakList.add(new Peak(119.051, 467.616, 45));
	        Fragmenter fragmenter = new Fragmenter(true, true, false);
	        List<IAtomContainer> res = fragmenter.generateFragmentsInMemory(molecule, true, 1);
	        de.ipbhalle.metfrag.tools.Render.Draw(molecule, res, "Original");
	        
	        
		} catch (InvalidSmilesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CDKException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
