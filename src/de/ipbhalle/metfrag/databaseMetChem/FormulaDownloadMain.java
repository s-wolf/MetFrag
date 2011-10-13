package de.ipbhalle.metfrag.databaseMetChem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.xml.rpc.ServiceException;

import org.openscience.cdk.AtomContainerSet;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.formula.MolecularFormula;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomContainerSet;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.io.SDFWriter;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

import de.ipbhalle.metfrag.main.Config;
import de.ipbhalle.metfrag.pubchem.PubChemWebService;

public class FormulaDownloadMain {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {
		String pathToStructures = "/home/swolf/MOPAC/EMMATest/FormulaSDFs/molgen/";
		Config c = new Config();
		
		File[] spectraFolder = new File(pathToStructures).listFiles();
		Arrays.sort(spectraFolder);
		Query query = new Query(c.getUsernamePostgres(),c.getPasswordPostgres(),c.getJdbcPostgres());
		for (File file : spectraFolder) {
			
			if(file.getName().contains("pubchem"))
				continue;
			
			String formula = file.getName().split("_")[1].split("\\.")[0];
			IMolecularFormula molFormula = MolecularFormulaManipulator.getMolecularFormula(formula, NoNotificationChemObjectBuilder.getInstance());
			formula = MolecularFormulaManipulator.getString(molFormula, false);
			
			try {
				List<CandidateMetChem> candidates = query.queryByFormula(formula, "pubchem");
				System.out.println("Candidates for " + file.getName() + " : " + candidates.size());
				IAtomContainerSet res = new AtomContainerSet();
				query.openConnection();
				for (CandidateMetChem candidateMetChem : candidates) {
					res.addAtomContainer(query.getCompoundConnectionOpened(candidateMetChem.getCompoundID()));
				}
				query.closeConnection();
				SDFWriter writer = new SDFWriter(new FileOutputStream(new File(pathToStructures + file.getName().split("\\.")[0] + "_pubchem.sdf")));
				writer.write(res);
				writer.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CDKException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			try {
				
				PubChemWebService pw = new PubChemWebService();
				Vector<String> cands = pw.getHitsbySumFormula(formula, true);
				System.out.println("Candidates for " + file.getName() + " : " + cands.size());
				IAtomContainerSet res = new AtomContainerSet();
				for (String	cand : cands) {
					res.addAtomContainer(pw.getCompound(Integer.parseInt(cand)));
				}
				SDFWriter writer = new SDFWriter(new FileOutputStream(new File(pathToStructures + file.getName().split("\\.")[0] + "_pubchem_online.sdf")));
				writer.write(res);
				writer.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CDKException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

}
