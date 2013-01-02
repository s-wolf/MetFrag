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
package de.ipbhalle.metfrag.read;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;

import de.ipbhalle.metfrag.main.MetFrag;

public class SDFFile {
	
	/**
	 * Read sdf file.
	 * 
	 * @param path the path
	 * 
	 * @return the list< i atom container>
	 * 
	 * @throws FileNotFoundException the file not found exception
	 * @throws CDKException the CDK exception
	 */
	public static List<IAtomContainer> ReadSDFFile(String path) throws FileNotFoundException, CDKException
	{
		MDLV2000Reader reader;
		List<IAtomContainer> containersList;
		List<IAtomContainer> ret = new ArrayList<IAtomContainer>();
		
		File f = new File(path);
		
		if(f.isFile())
		{
			reader = new MDLV2000Reader(new FileReader(f));
	        ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());
	        containersList = ChemFileManipulator.getAllAtomContainers(chemFile);
	        for (IAtomContainer container: containersList) {
	        	ret.add(container);
			}
	        
		}
		else
		{
			System.err.println("Did not find SDF file: " + path);
			//throw error
//			reader = new MDLV2000Reader(new FileReader(f));
		}
		
        return ret;
	}
	
	public static void main(String[] args) {
		try {
			List<IAtomContainer> list = ReadSDFFile("/vol/data_extern/emma.schymanski@ufz.de/ufzleipzig/100spec/060_4169_C3H3Cl3_struct_wM_END.sdf");
			for (IAtomContainer molecule : list) {
				try
		        {
			        //add hydrogens
			        AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
			        CDKHydrogenAdder hAdder = CDKHydrogenAdder.getInstance(molecule.getBuilder());
			        hAdder.addImplicitHydrogens(molecule);
			        AtomContainerManipulator.convertImplicitToExplicitHydrogens(molecule);
		        }
		        //there is a bug in cdk??
		        catch(IllegalArgumentException e)
	            {
		        	System.err.println(e.getMessage());
		        	e.printStackTrace();
	            }
			}
			
			System.out.println(list.size());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CDKException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
