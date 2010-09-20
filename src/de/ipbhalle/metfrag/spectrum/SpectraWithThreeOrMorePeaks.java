package de.ipbhalle.metfrag.spectrum;

import java.io.File;
import java.util.Arrays;

import de.ipbhalle.metfrag.database.Tools;

public class SpectraWithThreeOrMorePeaks {
	
	public static void main(String[] args) {
		
		String folder = "/home/swolf/MassBankData/MetFragSunGrid/RikenDataMergedCorrect/";
		//loop over all files in folder
		File f = new File(folder);
		File files[] = f.listFiles();
		Arrays.sort(files);
		
		//create new folder
		String path = folder + "fewPeaks";
		String path1 = folder + "negativeMode";
		String path2 = folder + "NoCHONPS";
		new File(path).mkdir();
		new File(path1).mkdir();
		new File(path2).mkdir();
		

		for(int i=0;i<files.length-1;i++)
		{
			if(files[i].isFile())
			{
				WrapperSpectrum spectrum = new WrapperSpectrum(files[i].toString());
				spectrum.setPeakList(new CleanUpPeakList(spectrum.getPeakList()).getCleanedPeakList(spectrum.getExactMass()));
				
				if(spectrum.getPeakList().size() <= 3)
				{
					files[i].renameTo(new File(folder + "fewPeaks", files[i].getName()));
					continue;
				}
				
				
				if(spectrum.getPeakList().size() <= 3)
				{
					files[i].renameTo(new File(folder + "fewPeaks", files[i].getName()));
					continue;
				}
				
				if(spectrum.getMode() != 1)
				{
					files[i].renameTo(new File(folder + "negativeMode", files[i].getName()));
				}
				
				if(Tools.checkCHONSP(spectrum.getFormula()) == 0)
				{
					files[i].renameTo(new File(folder + "NoCHONPS", files[i].getName()));
				}
				
			}
		}
	}

}
