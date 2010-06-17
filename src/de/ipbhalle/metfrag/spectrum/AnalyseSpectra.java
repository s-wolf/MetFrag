package de.ipbhalle.metfrag.spectrum;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

import de.ipbhalle.metfrag.massbankParser.Peak;
import de.ipbhalle.metfrag.tools.MolecularFormulaTools;
import de.ipbhalle.metfrag.tools.PPMTool;

public class AnalyseSpectra {
	
	private static Map<Double, List<String>> addToMap(Double deviation, String file, Map<Double, List<String>> deviationToFile)
    {
    	//add sum formula molecule comb. to map
        if(deviationToFile.containsKey(deviation))
        {
        	List<String> tempList = deviationToFile.get(deviation);
        	tempList.add(file);
        	deviationToFile.put(deviation, tempList);
        }
        else
        {
        	List<String> temp = new ArrayList<String>();
        	temp.add(file);
        	deviationToFile.put(deviation, temp);
        }
        
        return deviationToFile;
    }
	

	public static void main(String[] args) {
		
		String folder = "/home/swolf/MassBankData/MetFragSunGrid/RikenDataMerged/CHONPS/";
		//loop over all files in folder
		File f = new File(folder);
		File files[] = f.listFiles();
		Arrays.sort(files);
		
		//create new folder
		String path = folder + "useable";
		new File(path).mkdir();
		
		Map<Double, List<String>> deviationToFile = new HashMap<Double, List<String>>();
	
		
		for(int i=0;i<files.length-1;i++)
		{
			if(files[i].isFile())
			{
				WrapperSpectrum spectrum = new WrapperSpectrum(files[i].toString());
				double calculatedPeak = spectrum.getExactMass() + MolecularFormulaTools.getMonoisotopicMass("H1"); 
				Peak closestPeak = new Peak(0.0, 1.0, 1);
				double minDist = Double.MAX_VALUE;
				for (Peak peak : spectrum.getPeakList()) {
					if(Math.abs((peak.getMass() - calculatedPeak)) < minDist)
					{
						minDist = Math.abs(peak.getMass() - calculatedPeak);
						closestPeak = peak;
					}
				}
				
				
				double deviation = 2.5;
				if(closestPeak.getMass() > (calculatedPeak - deviation) && closestPeak.getMass() < (calculatedPeak + deviation))
				{
					deviationToFile = addToMap(Math.abs(PPMTool.getPPMWeb(calculatedPeak, closestPeak.getMass())), files[i].getName(), deviationToFile);
//					deviationToFile.put(Math.abs(PPMTool.getPPMWeb(calculatedPeak, closestPeak.getMass())), files[i].getName());
//					System.out.println(files[i].getName() + ": " + PPMTool.getPPMWeb(calculatedPeak, closestPeak.getMass()));
				}
				else
				{
					deviationToFile = addToMap(-1.0, files[i].getName(), deviationToFile);
//					deviationToFile.put(-1.0, files[i].getName());
//					System.err.println(files[i].getName() + ": " + "No mol peak found!");
				}
				
				
			}
		}
		
		Double[] keysScore = new Double[deviationToFile.keySet().size()];
		keysScore = deviationToFile.keySet().toArray(keysScore);
		Arrays.sort(keysScore);
		
		int count = 0;
		for (int i = 0; i < keysScore.length; i++) {
			for (String file : deviationToFile.get(keysScore[i])) {
				
				if(keysScore[i] >= 0 && keysScore[i] < 100)
				{
					File temp = new File(folder + file);
					temp.renameTo(new File(path + "/" + file));
				}
				
				System.out.println(keysScore[i] + " - " + file);
				count++;
			}
		}
		
		System.out.println("# of spectra: " + count);
	}
	
}
