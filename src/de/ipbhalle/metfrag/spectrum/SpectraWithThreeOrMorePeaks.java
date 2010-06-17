package de.ipbhalle.metfrag.spectrum;

import java.io.File;
import java.util.Arrays;

public class SpectraWithThreeOrMorePeaks {
	
	public static void main(String[] args) {
		
		String folder = "/home/swolf/MassBankData/MetFragSunGrid/RikenDataMerged/CHONPS/";
		//loop over all files in folder
		File f = new File(folder);
		File files[] = f.listFiles();
		Arrays.sort(files);
		
		//create new folder
		String path = folder + "fewPeaks";
		new File(path).mkdir();
		

		for(int i=0;i<files.length-1;i++)
		{
			if(files[i].isFile())
			{
				WrapperSpectrum spectrum = new WrapperSpectrum(files[i].toString());
				if(spectrum.getPeakList().size() <= 3)
				{
					files[i].renameTo(new File(folder + "fewPeaks", files[i].getName()));
				}
			}
		}
	}

}
