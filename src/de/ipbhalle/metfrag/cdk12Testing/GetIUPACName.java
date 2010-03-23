package de.ipbhalle.metfrag.cdk12Testing;

import java.sql.SQLException;
import java.util.Vector;

import org.openscience.cdk.exception.InvalidSmilesException;

import de.ipbhalle.metfrag.molDatabase.PubChemLocal;
import de.ipbhalle.metfrag.spectrum.WrapperSpectrum;
import de.ipbhalle.metfrag.tools.PreprocessSpectraLive;

public class GetIUPACName {
	
	public static void main(String[] args) {
		PreprocessSpectraLive ppLive = new PreprocessSpectraLive("/home/swolf/MassBankData/MetFragSunGrid/HillPaperDataMerged/", 0.01, 10);
	    Vector<WrapperSpectrum> mergedSpectra = ppLive.getMergedSpectra();
	    for (WrapperSpectrum spectrum : mergedSpectra) {
	    	Integer pubchemID = spectrum.getCID();
	    	PubChemLocal molPubChem = new PubChemLocal("jdbc:mysql://rdbms/MetFrag", "swolf", "populusromanus");
			try {
				if(molPubChem.getNames(pubchemID.toString()).size() > 0)
					System.out.println(pubchemID + "\t" + spectrum.getTrivialName() + "\t" + molPubChem.getNames(pubchemID.toString()).get(0));
				else
					System.out.println(pubchemID + "\t" + spectrum.getTrivialName() + "\t" + "FEHLER");
			} catch (InvalidSmilesException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			
	    }
	}

}
