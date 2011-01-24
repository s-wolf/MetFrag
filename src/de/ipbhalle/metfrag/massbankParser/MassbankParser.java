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
import java.util.Vector;
import java.util.TreeMap;


public class MassbankParser{
	
	public static Vector<Spectrum> Read(String filename) {
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
		String linkKEGG = "none";
		String[] array;
		String IUPAC = "";
		int mode = 0, collisionEnergy;
		double mass = 0.0, focusedMass = 0.0;
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
			  	mass = Double.parseDouble(line.substring(line.indexOf("CH$EXACT_MASS")+15));
			  	
			  	
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
			  			linkPubChem = Integer.parseInt(line.substring(line.indexOf("CH$LINK: PUBCHEM CID:")+21).split("\\ ")[0]);
			  		else if(line.contains("KEGG"))
			  			linkKEGG = line.substring(line.indexOf("CH$LINK: KEGG")+14);
			  		
			  		line = reader.readLine();
	  			}

			  	
			  	while (line != null && !line.contains("AC$INSTRUMENT")){
			  	  line = reader.readLine();
			  	}
			  	
			  	//Experimental condition
			  	//Equipment
				instrument = line.substring(line.indexOf("AC$INSTRUMENT")+15);
			  	while (line != null){
			  		if(line.contains("AC$ANALYTICAL_CONDITION: PRECURSOR_TYPE") || line.contains("AC$ANALYTICAL_CONDITION: MODE"))
			  			break;
			  		
			  		line = reader.readLine();
			  	}	
		  		// PRECURSOR_TYPE: POSITIVE (1) or NEGATIVE (-1)
				if (line.contains("AC$ANALYTICAL_CONDITION: PRECURSOR_TYPE") && line.substring(line.indexOf("AC$ANALYTICAL_CONDITION: PRECURSOR_TYPE")+40).contains("[M+H]+"))
				{
					mode = 1;
					isPositive = true;
				}
				else
				{
					mode = -1;
					isPositive = false;
				}
				//RIKEN Spektren
				if (line.contains("AC$ANALYTICAL_CONDITION: MODE") && line.substring(line.indexOf("AC$ANALYTICAL_CONDITION: MODE")+30).contains("POSITIVE"))
				{
					mode = 1;
					isPositive = true;
				}
				else
				{
					mode = -1;
					isPositive = false;
				}
			}
		  	
	  		//skipped PRECURSER SELECTION, FRAGMENTATION_EQUIPMENT, SPECTRUM_TYPE.....
		  	while (line != null && !line.contains("AC$ANALYTICAL_CONDITION: COLLISION_ENERGY")){
		  	  line = reader.readLine();
		  	}
			try
			{
				collisionEnergy = Integer.parseInt(line.substring(line.indexOf("AC$ANALYTICAL_CONDITION: COLLISION_ENERGY")+42, line.length()-3));
			}
			catch(NumberFormatException e)
			{
				//error in source file
				collisionEnergy = 0;
			}
			
			
			while (!line.contains("PK$PEAK") && line != null && !line.contains("MS$FOCUSED_ION: PRECURSOR_TYPE")){
			  	  line = reader.readLine();
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
				peaks.add(new Peak(Double.parseDouble(array[2]), Double.parseDouble(array[3]), Double.parseDouble(array[4]), collisionEnergy));
				line = reader.readLine();
			}
			spectra.add(new Spectrum(collisionEnergy, peaks, mass, mode, IUPAC, linkPubChem, linkKEGG, nameTrivial, formula, precursorType, isPositive));
			
				
			}
			catch (IOException e) {
				System.out.println("File not found!!! Error: " +e.getMessage());
				errorFlag = true;
			}
		}
		return spectra;
	}
	
	public static void main(String[] args) {
		Vector<Spectrum> spectra = Read("/home/swolf/MassBankData/MetFragSunGrid/HillPaperDataMerged/4_Aminoantipyrine_104_Aminoantipyrine_204_Aminoantipyrine_304_Aminoantipyrine_404_Aminoantipyrine_50.txt");
		
	}
}
