/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package de.ipbhalle.metfrag.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Vector;

import javax.xml.rpc.ServiceException;

import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.inchi.InChIGenerator;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;

import de.ipbhalle.metfrag.pubchem.PubChemWebService;

public class ModifyHillMassBankFiles {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws ServiceException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws InterruptedException 
	 * @throws CDKException 
	 */
	public static void main(String[] args) throws IOException, ServiceException, SQLException, ClassNotFoundException, CDKException, InterruptedException {
		String folder = "/home/swolf/MassBankData/HillPaper/";
		//loop over all files in folder
		File f = new File(folder);
		File files[] = f.listFiles();
		Arrays.sort(files);
		//remove the folders from the array
		Vector<File> onlyFiles = new Vector<File>();
		PubChemWebService pcw = new PubChemWebService();
		
		for (File file : files) {
			if(file.isFile())
				onlyFiles.add(file);
		}
		
		new File(folder + "Modified/").mkdir();
		int count = 0;
		
		for (File file : onlyFiles) {
			count++;
			
			BufferedReader reader;
			reader = new BufferedReader(new FileReader(file));

			File f2 = new File(folder + "Modified_MassBank/HI" + Number.numberToFixedLength(count, 6) + ".txt"); 
			BufferedWriter out = new BufferedWriter(new FileWriter(f2));
			
//		  	String line = reader.readLine();
//		  	if(line != null)
//		  		  out.write(line);
//		  	out.newLine();
		  	
		  	
		  	String line = "";
		  	while (line != null){
		  		
		  		
		  	  line = reader.readLine();
		  	  
		  	  if( line != null && line.contains("ACCESSION:"))
		  	  {
		  		out.write("ACCESSION: " + "HI" + Number.numberToFixedLength(count, 6));
			  	out.newLine();
			  	continue;
		  	  }
		  	  
		  	if( line != null && line.contains("RECORD_TITLE:"))
		  	  {
		  		String[] array = line.split(" ");
		  		String name = array[1];
		  		String ce = array[2];
		  		out.write("RECORD_TITLE: " + name + "; LC/MS/MS; QTOF; CE:" + ce + " eV; [M+H]+");
			  	out.newLine();
			  	continue;
		  	  }
		  	  
		  	  
		  	  if( line != null && line.contains("CH$LINK: PUBCHEM CID:"))
		  	  {
		  		Integer linkPubChem = Integer.parseInt(line.substring(line.indexOf("CH$LINK: PUBCHEM CID:")+21).split("\\ ")[0]);
			  	IAtomContainer ac = pcw.getSingleMol(linkPubChem.toString(), true);
			  	String smiles = (String)ac.getProperty("PUBCHEM_OPENEYE_CAN_SMILES");
			  	out.write("CH$SMILES: " + smiles);
			  	out.newLine();
			  	
			  	
			  	SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
				try {
					IAtomContainer molecule = sp.parseSmiles(smiles);
					
			        CDKAtomTypeMatcher matcher1 = CDKAtomTypeMatcher.getInstance(molecule.getBuilder());
					for (IAtom atom : molecule.atoms()) {
			          IAtomType type = matcher1.findMatchingAtomType(molecule,atom);
			          AtomTypeManipulator.configure(atom, type);
			        }
			        CDKHydrogenAdder adder1 = CDKHydrogenAdder.getInstance(molecule.getBuilder());
			        adder1.addImplicitHydrogens(molecule);
			        AtomContainerManipulator.convertImplicitToExplicitHydrogens(molecule);
			        
			      	//generate inchi
			        InChIGenerator gen = InChIGeneratorFactory.getInstance().getInChIGenerator(molecule);
			        String inchi = gen.getInchi();
			        
				  	out.write("CH$IUPAC: " + inchi);
				  	out.newLine();
				}
				catch(CDKException e)
				{
					e.printStackTrace();
				}
			  	
		  	  }
		  	  if(line != null)
		  		  out.write(line);
		  	  out.newLine();
		  	  
		  	}
		  	
		  	out.close();
		}
	}
}
