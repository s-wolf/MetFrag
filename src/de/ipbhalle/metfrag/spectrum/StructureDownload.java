package de.ipbhalle.metfrag.spectrum;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.MDLV2000Writer;

import de.ipbhalle.metfrag.pubchem.ESearchDownload;

public class StructureDownload {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String folder = "/home/swolf/MassBankData/TestSpectra/HillMerged/";
		
		//loop over all files in folder
		File f = new File(folder);
		File files[] = f.listFiles();
		Arrays.sort(files);
		
		//create new folder
		String path = folder + "logs/structures/";
		new File(path).mkdirs();
		
		
        
        List<String> idList = new ArrayList<String>();
        for(int i=0;i<files.length;i++)
		{
			if(files[i].isFile())
			{
				WrapperSpectrum spectrum = new WrapperSpectrum(files[i].toString());
				idList.add(Integer.toString(spectrum.getCID()));
			}
		}
        
        try {
			Map<String, IAtomContainer> idToStructure = ESearchDownload.ESearchDownloadPubChemIDs(idList);
			for (String string : idList) {
				;
				MDLV2000Writer writer = new MDLV2000Writer(new FileWriter(new File(path + string + ".mol")));
				writer.write(idToStructure.get(string));
				writer.close();
			}
	        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        
	}

}
