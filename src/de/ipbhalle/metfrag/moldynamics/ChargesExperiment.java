package de.ipbhalle.metfrag.moldynamics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.vecmath.Point3d;

import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.charges.GasteigerMarsiliPartialCharges;
import org.openscience.cdk.charges.GasteigerPEPEPartialCharges;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemFile;
import org.openscience.cdk.io.IChemObjectReader;
import org.openscience.cdk.io.ISimpleChemObjectReader;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.io.Mol2Reader;
import org.openscience.cdk.io.ReaderFactory;
import org.openscience.cdk.io.formats.CMLFormat;
import org.openscience.cdk.io.formats.IChemFormatMatcher;
import org.openscience.cdk.io.formats.MDLFormat;
import org.openscience.cdk.io.formats.MDLV2000Format;
import org.openscience.cdk.io.formats.MDLV3000Format;
import org.openscience.cdk.io.formats.Mol2Format;
import org.openscience.cdk.io.formats.SDFFormat;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;

import com.lowagie.text.pdf.hyphenation.TernaryTree.Iterator;

import de.ipbhalle.metfrag.tools.renderer.StructureRenderer;

public class ChargesExperiment {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		List<String> files = new ArrayList<String>();
		//at most 2 cpd's for now
			
		//compound0 D1
//		files.add("/home/swolf/bioclipse-workspace-21/sybyl/molDFTPaper/compound0/Charges/CID_20097272.sdf");
//		files.add("/home/swolf/bioclipse-workspace-21/sybyl/molDFTPaper/compound0/Charges/CID_20097272_D1.sdf");
		files.add("/home/swolf/bioclipse-workspace-21/sybyl/molDFTPaper/compound0/mopac/20097272_mopac.mol2");
		files.add("/home/swolf/bioclipse-workspace-21/sybyl/molDFTPaper/compound0/mopac/20097272_D1_mopac.mol2");
		
		//compound1 F1
//		files.add("/home/swolf/bioclipse-workspace-21/sybyl/molDFTPaper/compound1/Charges/CID_3365.sdf");
//		files.add("/home/swolf/bioclipse-workspace-21/sybyl/molDFTPaper/compound1/Charges/CID_3365_F1.sdf");
		//compound1 F2
//		files.add("/home/swolf/bioclipse-workspace-21/sybyl/molDFTPaper/compound1/Charges/CID_3365.sdf");
//		files.add("/home/swolf/bioclipse-workspace-21/sybyl/molDFTPaper/compound1/Charges/CID_3365_F2.sdf");
		
		//compound2 V1
//		files.add("/home/swolf/bioclipse-workspace-21/sybyl/molDFTPaper/compound2/Charges/CID_5231054.sdf");
//		files.add("/home/swolf/bioclipse-workspace-21/sybyl/molDFTPaper/compound2/Charges/CID_5231054_V1.sdf");
		//compound2 V2
//		files.add("/home/swolf/bioclipse-workspace-21/sybyl/molDFTPaper/compound2/Charges/CID_5231054.sdf");
//		files.add("/home/swolf/bioclipse-workspace-21/sybyl/molDFTPaper/compound2/Charges/CID_5231054_V2.sdf");
		//compound2 V3
//		files.add("/home/swolf/bioclipse-workspace-21/sybyl/molDFTPaper/compound2/Charges/CID_5231054.sdf");
//		files.add("/home/swolf/bioclipse-workspace-21/sybyl/molDFTPaper/compound2/Charges/CID_5231054_V3.sdf");
		//compound2 V4
//		files.add("/home/swolf/bioclipse-workspace-21/sybyl/molDFTPaper/compound2/Charges/CID_5231054.sdf");
//		files.add("/home/swolf/bioclipse-workspace-21/sybyl/molDFTPaper/compound2/Charges/CID_5231054_V4.sdf");
		//compound2 V5
//		files.add("/home/swolf/bioclipse-workspace-21/sybyl/molDFTPaper/compound2/Charges/CID_5231054.sdf");
//		files.add("/home/swolf/bioclipse-workspace-21/sybyl/molDFTPaper/compound2/Charges/CID_5231054_V5.sdf");
		
		
		List<Distance> cpd1BondToDistance = new ArrayList<Distance>();
		List<Distance> cpd2BondToDistance = new ArrayList<Distance>();
		
	
		List<IAtomContainer> containersList;
		DefaultChemObjectBuilder builder = DefaultChemObjectBuilder.getInstance();	
		
		String firstFile = files.get(0);
		
		try {
			
			//now get the coordinated
			for (int i = 0; i < files.size(); i++) {
				
				File input = new File(files.get(i));
	            ReaderFactory readerFactory = new ReaderFactory();
	            ISimpleChemObjectReader reader = readerFactory.createReader(new FileReader(input));
	            //ISimpleChemObjectReader content = reader.read(builder.newChemFile());
	

				ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());
		        containersList = ChemFileManipulator.getAllAtomContainers(chemFile);
		        IAtomContainer cpd = containersList.get(0);
		        try {
		        	GasteigerMarsiliPartialCharges peoe = new GasteigerMarsiliPartialCharges();
		        	GasteigerPEPEPartialCharges pepe = new GasteigerPEPEPartialCharges();
		            AtomContainerManipulator.convertImplicitToExplicitHydrogens(cpd);
		            AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(cpd);
		    		
		    		peoe.calculateCharges(cpd);
//		    		pepe.calculateCharges(cpd);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        	    
				new StructureRenderer(cpd, "Compound");
		        
		        for (IBond bond : cpd.bonds()) {
		        	IAtom atom1 = null;
		        	IAtom atom2 = null;
		        	for (IAtom atom : bond.atoms()) {
		        		if(atom1 == null)
		        			atom1 = atom;
		        		else
		        			atom2 = atom;
		        		
					}
		        	//System.out.println("Distance between " + atom1.getSymbol() + "-"  +	atom2.getSymbol() + "\t" +
		        	//		atom1.getPoint3d().distance(atom2.getPoint3d()));
		        	
		        	Double atom1Charge = (Math.round(atom1.getCharge() * 1000.0)/1000.0);
		        	Double atom2Charge = (Math.round(atom2.getCharge() * 1000.0)/1000.0);
		        	
		        	Double diffCharge = Math.abs(atom1Charge - atom2Charge);
		        	
		        	if(firstFile.equals(files.get(i)))
		        		cpd1BondToDistance.add(new Distance(atom1.getSymbol() + "-" + atom2.getSymbol(), diffCharge));
//		        		cpd1BondToDistance.add(new Distance(atom1.getSymbol() + "-" + atom2.getSymbol(), atom1.getPoint2d().distance(atom2.getPoint2d())));
		        	else
		        		cpd2BondToDistance.add(new Distance(atom1.getSymbol() + "-" + atom2.getSymbol(), diffCharge));
//		        		cpd2BondToDistance.add(new Distance(atom1.getSymbol() + "-" + atom2.getSymbol(), atom1.getPoint2d().distance(atom2.getPoint2d())));
				}
			}	        
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CDKException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<String> notMatched = new ArrayList<String>();
		
		//now compare the results
		int offset = 0;
		for (int i = 0; i < cpd1BondToDistance.size(); i++) {
			Double dist = -1.0;
			if(cpd1BondToDistance.get(i).getBond().equals(cpd2BondToDistance.get(i + offset).getBond()))
				dist = (cpd2BondToDistance.get(i + offset).getBondLength() - cpd1BondToDistance.get(i).getBondLength());
			else
			{
				notMatched.add(cpd2BondToDistance.get(i + offset).getBond());
				offset++;
				dist = (cpd2BondToDistance.get(i + offset).getBondLength() - cpd1BondToDistance.get(i).getBondLength());
			}
			System.out.println(cpd1BondToDistance.get(i).getBond() + " " + cpd1BondToDistance.get(i).getBondLength() + " " + cpd2BondToDistance.get(i + offset).getBondLength() + ": " + Math.round(dist*1000.0)/1000.0);
		}
		
		System.out.println("Not matched: ");
		for (String string : notMatched) {
			System.out.println(string);
		}

	}

}
