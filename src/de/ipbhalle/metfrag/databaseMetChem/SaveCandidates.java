package de.ipbhalle.metfrag.databaseMetChem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.SDFWriter;

import de.ipbhalle.metfrag.main.Config;
import de.ipbhalle.metfrag.spectrum.WrapperSpectrum;
import de.ipbhalle.metfrag.tools.PPMTool;

public class SaveCandidates {
	public static void main(String[] args) {
		Config config = null;
		try {
			config = new Config();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Query query = new Query(config.getUsernamePostgres(), config.getPasswordPostgres(), config.getJdbcPostgres());
		
		
		File folder = new File("/home/swolf/MOPAC/RIKEN/spectra/");
		File[] files = folder.listFiles();
		for (int i = 0; i < files.length; i++) {
			WrapperSpectrum spectrum = new WrapperSpectrum(files[i].toString());
			Double exactMass = spectrum.getExactMass();
			double deviation = PPMTool.getPPMDeviation(exactMass, config.getSearchPPM());
			List<CandidateMetChem> res = query.queryByMass((exactMass - deviation), (exactMass + deviation), "pubchem");
			
			
			String folderToWrite = folder.getParent() + "/candidates/" + files[i].getName().split("\\.")[0] + "/";
			new File(folderToWrite).mkdir();
			
			query.openConnection();
			
			for (CandidateMetChem candidate : res) {
				try {
					IAtomContainer mol = query.getCompoundConnectionOpened(candidate.getCompoundID());
					SDFWriter sdfwriter = new SDFWriter(new FileOutputStream(new File(folderToWrite + candidate.getAccession() + ".sdf")));
					sdfwriter.write(mol);
					sdfwriter.close();
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (CDKException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			query.closeConnection();
			
			
		}
		
	}
}
