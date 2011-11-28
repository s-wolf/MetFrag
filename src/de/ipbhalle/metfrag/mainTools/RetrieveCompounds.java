package de.ipbhalle.metfrag.mainTools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.SDFWriter;
import org.openscience.cdk.io.iterator.IteratingMDLReader;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;

import de.ipbhalle.metfrag.fragmenter.Candidates;
import de.ipbhalle.metfrag.main.Config;
import de.ipbhalle.metfrag.pubchem.PubChemWebService;
import de.ipbhalle.metfrag.spectrum.WrapperSpectrum;


public class RetrieveCompounds {
	
	private static IAtomContainer getPubChemEntryFromSdfGZ(String pubChemCID, File[] pubchemFiles)
	{
		IAtomContainer molToReturn = null;
		String pathToFile = "";
		int pid = Integer.parseInt(pubChemCID);
		
		for (int i = 0; i < pubchemFiles.length; i++) {
			
			if(pubchemFiles[i].getName().startsWith("README"))
				continue;
			
			String[] zipRange = pubchemFiles[i].getName().split("\\.")[0].split("_");
			int start = Integer.parseInt(zipRange[1]);
			int end = Integer.parseInt(zipRange[2]);

			if(pid >= start && pid <= end)
			{
				pathToFile = pubchemFiles[i].getPath();
				break;
			}
		}
		
		File sdfFile = new File(pathToFile);
		IteratingMDLReader reader;
		try {
			reader = new IteratingMDLReader(new GZIPInputStream(new FileInputStream(sdfFile)), DefaultChemObjectBuilder.getInstance());
			while (reader.hasNext()) {
				IAtomContainer molecule = (IAtomContainer)reader.next();
				
				if(Integer.parseInt((String)molecule.getProperty("PUBCHEM_COMPOUND_CID")) > pid)
					return null;
				
				if(molecule.getProperty("PUBCHEM_COMPOUND_CID").equals(pubChemCID))
				{
					molToReturn = molecule;
					break;
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return molToReturn;
	}
	
	
	/**
	 * This method downloads every structure and saves it to the specified location
	 * @throws Exception 
	 *
	 */
	public static void main(String[] args) throws Exception {
		
		//parameters
		String database = "pubchem";
		double searchPPM = 10.0;
		boolean isOnline = false;
		
		
//		File f = new File("/home/swolf/MOPAC/Hill-Riken-MM48_POSITIVE_PubChem_Formula/");
//		File f = new File("/home/swolf/MOPAC/Hill-Riken-MM48_POSITIVE_PubChem_LocalMass2009_CHONPS_NEW/spectra/");
//		File files[] = f.listFiles();
		
		File[] files = new File[]{new File(args[0])};
		
		Config config = null;
		try {
			config = new Config("outside");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		File[] pubchemFiles = new File("/vol/mirrors/pubchem").listFiles();
		Arrays.sort(pubchemFiles);
		
		for(int i=0;i<files.length;i++)
		{
			if(files[i].isFile() && files[i].getName().split("\\.")[1].equals("txt"))
			{
				WrapperSpectrum spectrum = new WrapperSpectrum(files[i].toString());
				List<String> candidates = null;
				
				String filePath = files[i].getParent();
				String fileName = files[i].getName().split("\\.")[0];
				if(new File(filePath + "/" + database + "/" + fileName).isDirectory())
					continue;
				
				if(isOnline)
				{
					PubChemWebService pubchem = new PubChemWebService();
					candidates = Candidates.queryOnline(database, "", "", spectrum.getExactMass(), searchPPM, false, pubchem);
				}
				else
					candidates = Candidates.queryLocally(database, spectrum.getExactMass(), searchPPM, config.getJdbc(), config.getUsername(), config.getPassword());
				
				for (String candString : candidates) {
					IAtomContainer mol = Candidates.getCompoundLocally(database, candString, config.getJdbc(), config.getUsername(), config.getPassword(), false, config.getChemspiderToken());
					
					if(mol == null)
						continue;
					
					try
					{
						AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol);
				        CDKHydrogenAdder hAdder = CDKHydrogenAdder.getInstance(mol.getBuilder());
				        hAdder.addImplicitHydrogens(mol);
				        AtomContainerManipulator.convertImplicitToExplicitHydrogens(mol);
					}
					catch(CDKException e)
					{
						System.err.println(e.getMessage());
					}
					
											
					try {
						new File(filePath + "/" + database + "/" + fileName).mkdirs();
						
						IAtomContainer molwith2D = getPubChemEntryFromSdfGZ(candString, pubchemFiles);			
						if(molwith2D != null)
						{
							SDFWriter writer = new SDFWriter(new FileWriter(new File(filePath + "/" + database + "/" + fileName + "/" + candString + ".sdf")));
							writer.write(molwith2D);
							writer.close();
						}
						else
						{
							System.err.println("Mol not found! " + candString);

							SDFWriter writer = new SDFWriter(new FileWriter(new File(filePath + "/" + database + "/" + fileName + "/" + candString + ".sdf")));
							writer.write(mol);
							writer.close();
						}
						
					} catch (CDKException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
}
