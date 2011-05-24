/*
*
* Copyright (C) 2009-2010 IPB Halle, Sebastian Wolf
*
* Contact: swolf@ipb-halle.de
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*
*/
package de.ipbhalle.metfrag.massbankParser;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.TreeMap;

import de.ipbhalle.metfrag.spectrum.AssignFragmentPeak;
import de.ipbhalle.metfrag.spectrum.NeutralLoss;


public class MassbankParser{
	
	/*public static Vector<Spectrum> Read(String filename) {
		ElementTable et = new ElementTable();
		et.add(new Element("C", 12.0, 4));
		et.add(new Element("H", 1.007825, 1));
		et.add(new Element("N", 14.003074, 3));
		et.add(new Element("O", 15.994915, 2));
		et.add(new Element("P", 30.973762, 3));
		et.add(new Element("S", 31.972071, 2));
		et.add(new Element("Cl", 35.4527, 1));
	
		int compoundsDone = 0;
		BufferedReader reader;
		ObjectOutputStream oostream;
		String formula = "";
		String line, name = "", instrument = "", precursorType = "";
		String nameTrivial = "";
		int linkPubChem = 0;
		String linkCHEBI = "";
		String linkKEGG = "none";
		String[] array;
		String IUPAC = "";
		int mode = 0, collisionEnergy = 0;
		double mass = 0.0, focusedMass = 0.0;
		
		double precursorMZ=0.0;
		
		Vector<Peak> peaks;
		Vector<Spectrum> spectra = new Vector<Spectrum>();
		//Vector<Compound> compounds = new Vector<Compound>(); not used....
		TreeMap<String, Integer> map = new TreeMap<String, Integer>();
		boolean errorFlag = true; // starts with true, so no compound is added in the first loop.
		boolean isPositive = true;
		
		for (int i= 1; i < 2; ++i){ //disabled loop ... read only 1 spectra
			try {
			//Skip first entries (Accession, Record_Title, Date, Authors, Copyright)
			//	reader = new BufferedReader(new FileReader("/home/basti/WorkspaceJava/TandemMSLookup/massbank_parser/test.TXT"));
			reader = new BufferedReader(new FileReader(filename));
		  	line = reader.readLine();
		  	while (line != null && !line.contains("CH$NAME")){
		  	  line = reader.readLine();
		  	}
		  	if (!line.substring(line.indexOf("CH$NAME")+9).equals(name)){
		  		
	  			spectra = new Vector<Spectrum>();
	  			errorFlag = false;
	  			
	  			//Use only the first name....skip synonymes
			  	name = line.substring(line.indexOf("CH$NAME")+9);
			  	line = reader.readLine();
			  	
			  	if(line.contains("CH$NAME"))
			  	{
				  	nameTrivial = line.substring(line.indexOf("CH$NAME")+9);
				  	while (line != null && !line.contains("CH$FORMULA")){
				  	  line = reader.readLine();
				  	}
			  	}
			  	else
			  	{
			  		nameTrivial = name;
			  		while (line != null && !line.contains("CH$FORMULA")){
			  			line = reader.readLine();
					}
			  	}
			  	
				formula = line.substring(line.indexOf("CH$FORMULA")+12);
			  	while (line != null && !line.contains("CH$EXACT_MASS")){
			  	  line = reader.readLine();
			  	}
			  	//exact mass
			  	mass = Double.valueOf(line.substring(line.indexOf("CH$EXACT_MASS")+15));
			  	
			  	
			  	//skipped CH$SMILES - SMILES code
			  	//IUPAC INCHI
			  	while (line != null && !line.contains("CH$IUPAC")){
				  	  line = reader.readLine();
				}
				//Inchi
				IUPAC = line.substring(line.indexOf("CH$IUPAC")+10);
				 
			    //CH$LINK - ID of other database with link
			  	while (line != null && !line.contains("CH$LINK:") && !line.startsWith("AC")){
				  	  line = reader.readLine();
				}
			  	
			  	//pubchem and kegg id
			  	while (line != null && line.contains("CH$LINK:") && !line.startsWith("AC"))
	  			{
			  		if(line.contains("PUBCHEM"))
			  		{
			  			try
			  			{
			  				linkPubChem = Integer.parseInt(line.substring(line.indexOf("CH$LINK: PUBCHEM CID:")+21).split("\\ ")[0]);
			  			}
			  			catch(NumberFormatException e)
			  			{
			  				linkPubChem = Integer.parseInt(line.substring(line.indexOf("CID:")+4).trim());
			  			}
			  			
			  		}
			  		else if(line.contains("KEGG"))
			  			linkKEGG = line.substring(line.indexOf("CH$LINK: KEGG")+14);
			  		else if(line.contains("CHEBI"))
			  			linkCHEBI = "CHEBI:" + line.substring(line.indexOf("CH$LINK: CHEBI")+15).split("\\s")[0];
			  		
			  		line = reader.readLine();
	  			}

			  	
			  	while (line != null && !line.contains("AC$INSTRUMENT")){
			  	  line = reader.readLine();
			  	}
			  	
			  	//Experimental condition
			  	//Equipment
				instrument = line.substring(line.indexOf("AC$INSTRUMENT")+15);
			  	while (line != null && !line.startsWith("PK")){
			  		if(line.contains("AC$ANALYTICAL_CONDITION: PRECURSOR_TYPE") || line.contains("AC$ANALYTICAL_CONDITION: MODE"))
			  		{
			  			// PRECURSOR_TYPE: POSITIVE (1) or NEGATIVE (-1)
						if (line.contains("AC$ANALYTICAL_CONDITION: PRECURSOR_TYPE") && line.substring(line.indexOf("AC$ANALYTICAL_CONDITION: PRECURSOR_TYPE")+40).contains("[M+H]+"))
						{
							mode = 1;
							isPositive = true;
						}
						else if(line.contains("AC$ANALYTICAL_CONDITION: PRECURSOR_TYPE"))
						{
							mode = -1;
							isPositive = false;
						}
						
						if(line.contains("AC$ANALYTICAL_CONDITION: PRECURSOR_TYPE"))
						{
							precursorType = (line.substring(line.indexOf("AC$ANALYTICAL_CONDITION: PRECURSOR_TYPE")+40, line.length()));
						}
						//RIKEN Spektren
						if (line.contains("AC$ANALYTICAL_CONDITION: MODE") && line.substring(line.indexOf("AC$ANALYTICAL_CONDITION: MODE")+30).contains("POSITIVE"))
						{
							mode = 1;
							isPositive = true;
						}
						else if(line.contains("AC$ANALYTICAL_CONDITION: MODE"))
						{
							mode = -1;
							isPositive = false;
						}
			  		}
			  		
			  		//skipped PRECURSER SELECTION, FRAGMENTATION_EQUIPMENT, SPECTRUM_TYPE.....
				  	if(line.contains("AC$ANALYTICAL_CONDITION: COLLISION_ENERGY"))
				  	{
				  		try
						{
							collisionEnergy = Integer.parseInt(line.substring(line.indexOf("AC$ANALYTICAL_CONDITION: COLLISION_ENERGY")+42, line.length()-3));
						}
						catch(NumberFormatException e)
						{
							//error in source file
							collisionEnergy = 0;
						}
				  	}
					
			  		line = reader.readLine();
			  	}	
			}
		  	
	
			
			if(line.contains("MS$FOCUSED_ION: PRECURSOR_TYPE"))
			{
				precursorType = (line.substring(line.indexOf("MS$FOCUSED_ION: PRECURSOR_TYPE")+31, line.length()));
			} 
			
			while (!line.contains("PK$PEAK") && line != null && !line.contains("MS$FOCUSED_ION: PRECURSOR_TYPE")){
			  	
				if(line.contains("MS$FOCUSED_ION: PRECURSOR_M/Z"))
				{
					precursorMZ = Double.valueOf(line.substring(line.indexOf("MS$FOCUSED_ION: PRECURSOR_M/Z")+30, line.length())).doubleValue();		
				}
				

				line = reader.readLine();
			}
			
			if(line.contains("MS$FOCUSED_ION: PRECURSOR_TYPE"))
			{
				precursorType = (line.substring(line.indexOf("MS$FOCUSED_ION: PRECURSOR_TYPE")+31, line.length()));
			} 
			
			if(!line.contains("PK$PEAK"))
				precursorType = line.substring(31);
	  	
	  	
			while (line != null && !line.contains("PK$PEAK")){
		  	  line = reader.readLine();
		  	}
			line = reader.readLine();
			peaks = new Vector<Peak>();
			while (line != null && !line.contains("//")){
				array = line.split(" ");
				// array[2] is mass, array[3] abs. intensity, array[4] rel. intensity.
				// spectra.size shows how many spectra had a lower energy than the spectrum this peak belongs to.
				peaks.add(new Peak(Double.valueOf(array[2]), Double.valueOf(array[3]), Double.valueOf(array[4]), collisionEnergy));
				line = reader.readLine();
			}
			
			
			if(precursorMZ==0.0) //TODO: 
			{
				
				Map<String ,Double> preType = readPrecursorTypes();
			
				if(preType.containsKey(precursorType))
				{
					precursorMZ=preType.get(precursorType)+mass;
				}
				
			}
			
			spectra.add(new Spectrum(collisionEnergy,collisionEnergy, peaks, mass, mode, IUPAC, linkPubChem, linkKEGG, linkCHEBI, nameTrivial, formula, precursorMZ, precursorType, isPositive));	
			}
			catch (IOException e) {
				System.out.println("File not found!!! Error: " +e.getMessage());
				errorFlag = true;
			}
		}
		return spectra;
	}
	
	public static Map<String,Double> readPrecursorTypes()
	{
		Map<String, Double> preType = new HashMap<String, Double>();

		String file = "";
		
		if(System.getProperty("property.file.path") != null)
    	{
    		file = System.getProperty("property.file.path");
    		file += "precursorType.csv";
    	}
		else
    	{
    		URL url = AssignFragmentPeak.class.getClassLoader().getResource("precursorType.csv");
			file = url.getFile();
    		//System.out.println("Pfad: " + url.getFile());
    	}
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			
			
			String line =in.readLine();
			 line=in.readLine();
			
			while(line!=null)
			{
		
				if(!line.substring(0,1).equals("#"))
				{
					
					String [] dummi=new String[line.split("\\s+").length];
					dummi=line.split("\\s+");
					preType.put(dummi[0], Double.valueOf(dummi[1]).doubleValue());

					
				}
				
				
				line =in.readLine();
			}
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return preType;
	}
	
	public static void main(String[] args) {
		
		Vector<Spectrum> spectra = Read("/home/ftarutti/records/CO000001.txt");
		System.out.println(spectra.get(0).getPrecursorType() +"   "+ spectra.get(0).getPrecursorMZ()+"  "+spectra.get(0).getExactMass());
		spectra = Read("/home/swolf/MassBankData/MetFragSunGrid/HillPaperDataMerged/4_Aminoantipyrine_104_Aminoantipyrine_204_Aminoantipyrine_304_Aminoantipyrine_404_Aminoantipyrine_50.txt");
		//System.out.println(spectra.get(0).getCID());
		
		int j=0;
		
		for (Iterator iterator = spectra.iterator(); iterator.hasNext();) {
			Spectrum spectrum = (Spectrum) iterator.next();
		}
		
		readPrecursorTypes();
	}*/
}
