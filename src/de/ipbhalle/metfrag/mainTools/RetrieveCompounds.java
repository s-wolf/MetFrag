package de.ipbhalle.metfrag.mainTools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.SDFWriter;

import de.ipbhalle.metfrag.fragmenter.Candidates;
import de.ipbhalle.metfrag.main.Config;
import de.ipbhalle.metfrag.pubchem.PubChemWebService;
import de.ipbhalle.metfrag.spectrum.WrapperSpectrum;


public class RetrieveCompounds {
	
	/**
	 * This method downloads every structure and saves it to the specified location
	 * @throws Exception 
	 *
	 */
	public static void main(String[] args) throws Exception {
		
		//parameters
		String database = "pubchem";
		double searchPPM = 10.0;
		boolean isOnline = true;
		
		
		File f = new File("/home/swolf/MOPAC/ProofOfConcept/");
		File files[] = f.listFiles();
		Config config = null;
		try {
			config = new Config();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		for(int i=0;i<files.length;i++)
		{
			if(files[i].isFile() && files[i].getName().split("\\.")[1].equals("txt"))
			{
				WrapperSpectrum spectrum = new WrapperSpectrum(files[i].toString());
				List<String> candidates = null;
				if(isOnline)
				{
					PubChemWebService pubchem = new PubChemWebService();
					candidates = Candidates.queryOnline(database, "", spectrum.getFormula(), spectrum.getExactMass(), searchPPM, false, pubchem);
				}
				else
					candidates = Candidates.queryLocally(database, spectrum.getExactMass(), searchPPM, config.getJdbc(), config.getUsername(), config.getPassword());
				
				for (String candString : candidates) {
					IAtomContainer mol = Candidates.getCompoundLocally(database, candString, config.getJdbc(), config.getUsername(), config.getPassword(), true, config.getChemspiderToken());
					
					if(mol == null)
						continue;
						
					try {
						String filePath = files[i].getParent();
						String fileName = files[i].getName().split("\\.")[0];
						new File(filePath + "/" + database + "/" + fileName).mkdirs();
						
						SDFWriter writer = new SDFWriter(new FileWriter(new File(filePath + "/" + database + "/" + fileName + "/" + candString + ".sdf")));
						writer.write(mol);
						writer.close();
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