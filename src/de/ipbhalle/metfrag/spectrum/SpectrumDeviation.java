package de.ipbhalle.metfrag.spectrum;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.openscience.cdk.interfaces.IAtomContainer;

import de.ipbhalle.metfrag.fragmenter.Candidates;
import de.ipbhalle.metfrag.fragmenter.FragmenterThread;
import de.ipbhalle.metfrag.main.Config;
import de.ipbhalle.metfrag.main.MetFrag;
import de.ipbhalle.metfrag.main.MetFragResult;
import de.ipbhalle.metfrag.massbankParser.Peak;
import de.ipbhalle.metfrag.pubchem.ESearchDownload;
import de.ipbhalle.metfrag.pubchem.PubChemWebService;
import de.ipbhalle.metfrag.tools.MolecularFormulaTools;
import de.ipbhalle.metfrag.tools.PPMTool;

public class SpectrumDeviation {
	
	
	private void analyseSpectra(String folder, String database, double mzabs, double mzppm) throws Exception
	{
		//loop over all files in folder
		File f = new File(folder);
		File files[] = f.listFiles();
		Arrays.sort(files);
		
		//create new folder
		String path = folder + "logs/deviation/";
		new File(path).mkdirs();
		
		FileWriter fstream = new FileWriter(path + "spectrumDeviation.txt", true);
        BufferedWriter out = new BufferedWriter(fstream);
        out.write("File\tAverage\tMedian\tDev. Molpeak\tPeaks Explained\n");
        out.close();
        
        List<String> idList = new ArrayList<String>();
        for(int i=0;i<files.length-1;i++)
		{
			if(files[i].isFile())
			{
				WrapperSpectrum spectrum = new WrapperSpectrum(files[i].toString());
				idList.add(Integer.toString(spectrum.getCID()));
			}
		}
        
        Map<String, IAtomContainer> idToStructure = ESearchDownload.ESearchDownloadPubChemIDs(idList);
		
		
		for(int i=0;i<files.length-1;i++)
		{
			if(files[i].isFile())
			{
				WrapperSpectrum spectrum = new WrapperSpectrum(files[i].toString());
				
				//only 1 result because only the correct compound is fragmented
//				Config c = new Config("outside");
//				List<MetFragResult> result = MetFrag.startConvenienceMetFusion(database, Integer.toString(spectrum.getCID()), "", 0.0, new WrapperSpectrum(files[i].toString()), false, mzabs, mzppm, 10.0, true, true, 2, true, false, true, false, 10, c.getJdbc(), c.getUsername(), c.getPassword()); 
				List<MetFragResult> result = MetFrag.startConvenienceWithStructure(database, idToStructure.get(Integer.toString(spectrum.getCID())), Integer.toString(spectrum.getCID()), "", spectrum.getExactMass(), new WrapperSpectrum(files[i].toString()), false, mzabs, mzppm, 0.0, true, true, 3, true, false, true, false, Integer.MAX_VALUE, true);
				List<Double> deviations = new ArrayList<Double>();
				
				if(result == null || result.size() == 0 || result.get(0) == null || result.get(0).getFragments().size() == 0)
				{
					fstream = new FileWriter(path + "spectrumDeviation.txt", true);
			        out = new BufferedWriter(fstream);
					out.write(files[i].getName() + "\tERROR\tERROR\tERROR\t0\n");
					out.close();
					continue;
				}
					
				
				for (PeakMolPair explainedPeak : result.get(0).getFragments()) {
					System.out.println("Measured Peak: " + explainedPeak.getMatchedMass() + "(Mass: " + explainedPeak.getPeak().getMass() + ")");
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
				
				
				double deviation = 1.5;
				Double deviationMolPeak = -1.0;
				if(closestPeak.getMass() > (calculatedPeak - deviation) && closestPeak.getMass() < (calculatedPeak + deviation))
				{
					deviationMolPeak = Math.abs(PPMTool.getPPMWeb(calculatedPeak, closestPeak.getMass()));
				}
				
				
				//write everything to log file
				System.out.println(files[i].getName() + "\t" + average + "\t" + median + "\t" + deviationMolPeak + "\t" + result.get(0).getFragments().size());
				
				fstream = new FileWriter(path + "spectrumDeviation.txt", true);
		        out = new BufferedWriter(fstream);
				out.write(files[i].getName() + "\t" + average + "\t" + median + "\t" + deviationMolPeak + "\t" + result.get(0).getPeaksExplained() + "\n");
				out.close();
			}
		}
		
		out.close();
	}
	
	
	public static void main(String[] args) {
//		String folder = "/home/swolf/MassBankData/MetFragSunGrid/RikenDataMerged/CHONPS/useable/";
		String folder = "/home/swolf/MassBankData/MetFragSunGrid/HillPaperDataMerged/";
//		String folder = "/home/swolf/MassBankData/MetFragSunGrid/BrukerRawData/Processed/Merged/";
		
		if(args != null && args.length > 0)
			folder = args[0];
		
		SpectrumDeviation sd = new SpectrumDeviation();
		try {
			sd.analyseSpectra(folder, "pubchem", 0.01, 10.0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
