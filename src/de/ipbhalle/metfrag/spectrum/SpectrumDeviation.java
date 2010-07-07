package de.ipbhalle.metfrag.spectrum;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.ipbhalle.metfrag.fragmenter.Candidates;
import de.ipbhalle.metfrag.fragmenter.FragmenterThread;
import de.ipbhalle.metfrag.main.Config;
import de.ipbhalle.metfrag.main.MetFrag;
import de.ipbhalle.metfrag.main.MetFragResult;
import de.ipbhalle.metfrag.pubchem.PubChemWebService;
import de.ipbhalle.metfrag.tools.PPMTool;

public class SpectrumDeviation {
	
	
	private void analyseSpectra(String folder, String database, double mzabs, double mzppm) throws Exception
	{
		//loop over all files in folder
		File f = new File(folder);
		File files[] = f.listFiles();
		Arrays.sort(files);
		
		//create new folder
		String path = folder + "Proccessed";
		new File(path).mkdir();
		
		for(int i=0;i<files.length-1;i++)
		{
			if(files[i].isFile())
			{
				WrapperSpectrum spectrum = new WrapperSpectrum(files[i].toString());
				
				//only 1 result because only the correct compound is fragmented
				List<MetFragResult> result = MetFrag.startConvenience(database, Integer.toString(spectrum.getCID()), "", spectrum.getExactMass(), spectrum, false, mzabs, mzppm, 0.0, true, true, 2, true, false, true, false, Integer.MAX_VALUE, true);
				List<Double> deviations = new ArrayList<Double>();
				
				for (PeakMolPair explainedPeak : result.get(0).getFragments()) {
					deviations.add(Math.abs(PPMTool.getPPMWeb(explainedPeak.getMatchedMass(), explainedPeak.getPeak().getMass())));
				}
				
				double total = 0.0;
				for (Double dev : deviations) {
					total += dev;
				}
				
				double average = total / deviations.size();
				double median = 0.0;
				Double[] devArray = new Double[deviations.size()]; 
				devArray = deviations.toArray(devArray);
				Arrays.sort(devArray);
				for (int j = 0; j < devArray.length; j++) {
					//median von gerader anzahl
					if(j == (devArray.length/2) && (j%2) == 0)
					{
						median = devArray[j];
					}
					else if(j == (devArray.length/2))
					{
						median = devArray[j];
					}
				}
				
				System.out.println(files[i].getName() + ": " + average + " " + median);
				
			}
		}
	}
	
	
	public static void main(String[] args) {
		String folder = "/home/swolf/MassBankData/MetFragSunGrid/RikenDataMerged/CHONPS/useable/";
		SpectrumDeviation sd = new SpectrumDeviation();
		try {
			sd.analyseSpectra(folder, "pubchem", 0.01, 10.0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
