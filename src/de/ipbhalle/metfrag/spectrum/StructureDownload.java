package de.ipbhalle.metfrag.spectrum;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.fingerprint.Fingerprinter;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.io.MDLV2000Writer;
import org.openscience.cdk.similarity.Tanimoto;

import de.ipbhalle.metfrag.pubchem.ESearchDownload;
import de.ipbhalle.metfrag.tools.renderer.StructureToFile;

public class StructureDownload {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String folder = "/home/swolf/MassBankData/MetFragSunGrid/HillPaperMetFusion/";
		
		//loop over all files in folder
		File f = new File(folder);
		File files[] = f.listFiles();
		Arrays.sort(files);
		
		//create new folder
		String path = folder + "logs/structures/";
		new File(path).mkdirs();
			
		
		List<File> comparison = new ArrayList<File>();
		//loop over all files in folder
		File f1 = new File(folder + "logs/structures/comparison/");
		File files1[] = f1.listFiles();
		Arrays.sort(files1);
        for(int i=0;i<files1.length;i++)
		{
			if(files1[i].isFile())
			{
				comparison.add(files1[i]);				
			}
		}
        
        
        List<String> idList = new ArrayList<String>();
        Map<String, String> idToFilename = new HashMap<String, String>();
        for(int i=0;i<files.length;i++)
		{
			if(files[i].isFile())
			{
				WrapperSpectrum spectrum = new WrapperSpectrum(files[i].toString());
				idList.add(Integer.toString(spectrum.getCID()));
				idToFilename.put(Integer.toString(spectrum.getCID()), files[i].getName());
			}
		}
        
        // compare the tanimotos
        
        
        try {
			Map<String, IAtomContainer> idToStructure = ESearchDownload.ESearchDownloadPubChemIDs(idList);
			for (String string : idList) {
				MDLV2000Writer writer = new MDLV2000Writer(new FileWriter(new File(path + idToFilename.get(string).split("\\.")[0] + ".mol")));
				writer.write(idToStructure.get(string));
				writer.close();
				
//				StructureToFile stf = new StructureToFile(400, 400, folder + "logs/structures/", false, false);
//				stf.writeMOL2PNGFile(idToStructure.get(string), idToFilename.get(string).split("\\.")[0] + ".png");
				
			}
			
			
			
			//compare the fingerprints
			for(int i=0;i<files.length / 5;i++)
			{
				if(files[i].isFile())
				{
					int index = i * 5;
					WrapperSpectrum spectrum = new WrapperSpectrum(files[index].toString());
					IAtomContainer tempOrig = idToStructure.get(Integer.toString(spectrum.getCID()));
					
					Fingerprinter finger = new Fingerprinter();
					MDLV2000Reader reader = new MDLV2000Reader(new FileReader(comparison.get(i)));
					IAtomContainer temp = reader.read(new Molecule());
					BitSet bs1 = finger.getFingerprint(temp);
					BitSet bs2 = finger.getFingerprint(tempOrig);
					System.out.println(files[index].getName() + "-" + comparison.get(i).getName() + ": " + Tanimoto.calculate(bs1, bs2));
				}
			}
			
	        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
        
	}

}
