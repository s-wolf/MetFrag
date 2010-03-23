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
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;

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
		
        return ret;
	}

}
