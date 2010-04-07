package de.ipbhalle.metfrag.moldynamics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.io.ISimpleChemObjectReader;
import org.openscience.cdk.io.ReaderFactory;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;


public class BondDistance {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		List<String> files = new ArrayList<String>();
		
		//compound0 D1
//		files.add("/home/swolf/bioclipse-workspace-21/sybyl/molDFTPaper/compound0/mopac/20097272_mopac.mol2");
//		files.add("/home/swolf/bioclipse-workspace-21/sybyl/molDFTPaper/compound0/mopac/20097272_D1_mopac.mol2");
		
		//compound1 F1
//		files.add("/home/swolf/bioclipse-workspace-21/sybyl/molDFTPaper/compound1/mopac/3365_mopac.mol2");
//		files.add("/home/swolf/bioclipse-workspace-21/sybyl/molDFTPaper/compound1/mopac/3365_F1_mopac.mol2");
		//compound1 F2
//		files.add("/home/swolf/bioclipse-workspace-21/sybyl/molDFTPaper/compound1/mopac/3365_mopac.mol2");
//		files.add("/home/swolf/bioclipse-workspace-21/sybyl/molDFTPaper/compound1/mopac/3365_F2_mopac.mol2");
	
		//compound2 V1
//		files.add("/home/swolf/bioclipse-workspace-21/sybyl/molDFTPaper/compound2/mopac/5231054_mopac.mol2");
//		files.add("/home/swolf/bioclipse-workspace-21/sybyl/molDFTPaper/compound2/mopac/5231054_V1_mopac.mol2");
		//compound2 V2
//		files.add("/home/swolf/bioclipse-workspace-21/sybyl/molDFTPaper/compound2/mopac/5231054_mopac.mol2");
//		files.add("/home/swolf/bioclipse-workspace-21/sybyl/molDFTPaper/compound2/mopac/5231054_V2_mopac.mol2");
		//compound2 V3
//		files.add("/home/swolf/bioclipse-workspace-21/sybyl/molDFTPaper/compound2/mopac/5231054_mopac.mol2");
//		files.add("/home/swolf/bioclipse-workspace-21/sybyl/molDFTPaper/compound2/mopac/5231054_V3_mopac.mol2");
		//compound2 V4
//		files.add("/home/swolf/bioclipse-workspace-21/sybyl/molDFTPaper/compound2/mopac/5231054_mopac.mol2");
//		files.add("/home/swolf/bioclipse-workspace-21/sybyl/molDFTPaper/compound2/mopac/5231054_V4_mopac.mol2");
		//compound2 V5
		files.add("/home/swolf/bioclipse-workspace-21/sybyl/molDFTPaper/compound2/mopac/5231054_mopac.mol2");
		files.add("/home/swolf/bioclipse-workspace-21/sybyl/molDFTPaper/compound2/mopac/5231054_V5_mopac.mol2");
		
		List<Distance> cpd1BondToDistance = new ArrayList<Distance>();
		List<Distance> cpd2BondToDistance = new ArrayList<Distance>();
		
	
		List<IAtomContainer> containersList;

		Map<Integer, IAtom> idToAtom1 = new HashMap<Integer, IAtom>();
		Map<Integer, IAtom> idToAtom2 = new HashMap<Integer, IAtom>();
		
		IAtomContainer firstAtomContainer = null;
		
		
		try {
			
			//now get all atoms give every atom a id
			String firstFile = files.get(0);
			for (int i = 0; i < files.size(); i++) {
				File input = new File(files.get(i));
	            ReaderFactory readerFactory = new ReaderFactory();
	            ISimpleChemObjectReader reader = readerFactory.createReader(new FileReader(input));
	            //ISimpleChemObjectReader content = reader.read(builder.newChemFile());
	            ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());
		        containersList = ChemFileManipulator.getAllAtomContainers(chemFile);
		        IAtomContainer cpd = containersList.get(0);
		        
		        if(firstFile.equals(files.get(i)))
		        	firstAtomContainer = cpd;
		        	
		        
		        Integer count = 0;
		        int offset = 0;
		        for (IAtom atom : cpd.atoms()) {
		        	if(firstFile.equals(files.get(i)))
		        	{
		        		atom.setID(count.toString());
		        		idToAtom1.put(count, atom);
		        	}
		        	else if((idToAtom1.get((count + offset)) != null) && idToAtom1.get((count + offset)).getSymbol().equals(atom.getSymbol()))
		        	{
		        		idToAtom2.put(count + offset, atom);
		        	}
		        	else
		        	{
		        		offset++;
		        		idToAtom2.put(count + offset, atom);
		        	}
		        	
		        	count++;
		        }
		        
			}
			
	        
	        
	        for (IBond bond : firstAtomContainer.bonds()) {
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
	        	
	        	//now get the distance
	        	Integer id1 = Integer.parseInt(atom1.getID());
	        	Integer id2 = Integer.parseInt(atom2.getID());
        		cpd1BondToDistance.add(new Distance(atom1.getSymbol() + "-" + atom2.getSymbol(), atom1.getPoint3d().distance(atom2.getPoint3d())));
        		String symb1 = idToAtom2.get(id1).getSymbol();
        		String symb2 = idToAtom2.get(id2).getSymbol();
        		cpd2BondToDistance.add(new Distance(idToAtom2.get(id1).getSymbol() + "-" + idToAtom2.get(id2).getSymbol(), idToAtom2.get(id1).getPoint3d().distance(idToAtom2.get(id2).getPoint3d())));

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
				dist = cpd2BondToDistance.get(i).getBondLength() - cpd1BondToDistance.get(i + offset).getBondLength();
			else
			{
				notMatched.add(cpd2BondToDistance.get(i + offset).getBond());
				offset++;
				dist = cpd2BondToDistance.get(i).getBondLength() - cpd1BondToDistance.get(i + offset).getBondLength();
			}
			System.out.println(cpd1BondToDistance.get(i).getBond() + ": " + Math.round(dist*1000.0)/1000.0);
		}
		
		System.out.println("Not matched: ");
		for (String string : notMatched) {
			System.out.println(string);
		}

	}

}
