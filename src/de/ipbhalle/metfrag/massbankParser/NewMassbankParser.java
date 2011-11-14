/*
*
* Copyright (C) 2009-2010 IPB Halle, Franziska Taruttis
*
* Contact: ftarutti@ipb-halle.de
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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.xml.crypto.Data;

import de.ipbhalle.metfrag.spectrum.AssignFragmentPeak;


/**
 * The Class NewMassbankParser.
 */
public class NewMassbankParser {
	
	//Record specific information
 	
	/**
	 * The Enum recordInformation.
	 */
	private enum recordInformation { /** The ACCESSION. */
 ACCESSION , /** The RECOR d_ title. */
 RECORD_TITLE , /** The DATE. */
 DATE , /** The AUTHORS. */
 AUTHORS , /** The COPYRIGHT. */
 COPYRIGHT , /** The PUBLICATION. */
 PUBLICATION , /** The COMMENT. */
 COMMENT };
	
	//Compound information
	
	/** The Constant CH$. */
	private final static String CH$ = "CH$";
	
	/**
	 * The Enum CH.
	 */
	private enum CH { /** The NAME. */
 NAME , /** The COMPOUN d_ class. */
 COMPOUND_CLASS , /** The FORMULA. */
 FORMULA , /** The EXAC t_ mass. */
 EXACT_MASS , /** The SMILES. */
 SMILES , /** The IUPAC. */
 IUPAC , /** The LINK. */
 LINK , /** The COMMENT. */
 COMMENT };
	
	//Sample information
	
	/** The Constant SP$. */
	private final static String SP$ = "SP$";
	
	/**
	 * The Enum SP.
	 */
	private enum SP { /** The SCIENTIFI c_ name. */
 SCIENTIFIC_NAME , /** The NAME. */
 NAME , /** The LINEAGE. */
 LINEAGE , /** The LINK. */
 LINK , /** The SAMPLE. */
 SAMPLE ,  /** The COMMENT. */
  COMMENT };
	
	//Analytical information
	
	/** The Constant AC$. */
	private final static String AC$ = "AC$";
	
	/**
	 * The Enum AC.
	 */
	private enum AC { /** The INSTRUMENT. */
 INSTRUMENT , /** The INSTRUMEN t_ type. */
 INSTRUMENT_TYPE , /** The ANALYTICA l_ condition. */
 ANALYTICAL_CONDITION , /** The COMMENT. */
 COMMENT  };
	
	/** The Constant PRECURSOR_TYPE. */
	private final static String PRECURSOR_TYPE=" PRECURSOR_TYPE";
	
	/** The Constant COLLISION_ENERGY. */
	private final static String COLLISION_ENERGY=" COLLISION_ENERGY";
	
	/** The Constant MODE. */
	private final static String MODE=" MODE";
	
	// Spectral information
	
	/** The Constant MS$. */
	private final static String MS$ = "MS$";
	
	/**
	 * The Enum MS.
	 */
	private enum MS{ /** The RELATE d_ ms. */
 RELATED_MS , /** The FOCUSE d_ ion. */
 FOCUSED_ION , /** The DAT a_ processing. */
 DATA_PROCESSING , /** The COMMENT. */
 COMMENT };
	
	// Peak information
	
	/** The Constant PK$. */
	private final static String PK$ = "PK$";
	
	/**
	 * The Enum PK.
	 */
	private enum PK{ /** The NU m_ peak. */
 NUM_PEAK ,  /** The ANNOTATIO n_ method. */
  ANNOTATION_METHOD , /** The ANNOTATION. */
 ANNOTATION , /** The COMMENT. */
 COMMENT , /** The PEAK. */
 PEAK }
	
	
	
	
	/**
	 * Read the spectra.
	 *
	 * @param filename the filename
	 * @return the Spectrum
	 */
	public static Vector<Spectrum> Read(String filename)  {

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
		String name = "", instrument = "", precursorType = "";
		String nameTrivial = "";
//		int linkPubChem = 0;
//		int sid=0;
//		String linkCHEBI = "";
//		String linkKEGG = "none";
//		String linkMetlin="none";
//		String linkKnapSack ="none";
		String[] array;
		String IUPAC = "";
		int mode = 0, collisionEnergy = 0;
		//int minCollisionEnergy = 0 , maxCollisionEnergy = 0;
		double mass = 0.0, focusedMass = 0.0;
		
		double precursorMZ=0.0;
		
		String smiles;
		
		Vector<Peak> peaks = null;
		Vector<Spectrum> spectra = new Vector<Spectrum>();
		//Vector<Compound> compounds = new Vector<Compound>(); not used....
		TreeMap<String, Integer> map = new TreeMap<String, Integer>();
		boolean errorFlag = true; // starts with true, so no compound is added in the first loop.
		boolean isPositive = false;
		
		
		Map<DatabaseIDs, String> dbLinks= new HashMap<DatabaseIDs, String>();
		DatabaseIDs[] allIDs =DatabaseIDs.values();
		
		
		Map<String, ArrayList<String> > recordSpecificInformation = new HashMap<String, ArrayList<String> >();
		Map<String, ArrayList<String> > compoundInformation = new HashMap<String, ArrayList<String> >();
		Map<String, ArrayList<String> > sampleInformation = new HashMap<String, ArrayList<String> >();
		Map<String, ArrayList<String> > analyticalInformation = new HashMap<String, ArrayList<String> >();
		Map<String, ArrayList<String> > spectralInformation = new HashMap<String, ArrayList<String> >();
		Map<String, ArrayList<String> > peakInformation = new HashMap<String, ArrayList<String> >();
		
		
		Map< String , Map <String, ArrayList<String>>> record = new HashMap < String , Map <String, ArrayList<String>>>();
		
		peaks = new Vector<Peak>();
		
		String lineTest="";

		boolean readPeaks=false;
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(filename));
			

			String line = in.readLine();
			
			while(line != null)
			{
				
				
				//read Record specific information				
				while(!line.contains("$") && line.contains(":"))
				{
					recordSpecificInformation = addElementsToMap(line, recordSpecificInformation);
					
					line = in.readLine();
				
					
				}
								
				//Compound information
				while(line.contains(CH$))
				{
					compoundInformation = addElementsToMap(line,  compoundInformation);
					
					line = in.readLine();
			
				}
				//Sample information
				while(line.contains(SP$))
				{
					sampleInformation = addElementsToMap(line,  sampleInformation);
					
					line = in.readLine();
					
				}
				//Analytical information
				while(line.contains(AC$))
				{
					analyticalInformation = addElementsToMap(line, analyticalInformation);
					
					line = in.readLine();
					
				}
				
				
				//Spectral information
				while(line.contains(MS$))
				{
					spectralInformation = addElementsToMap(line, spectralInformation);
					
					line = in.readLine();
		
				}
				
				//Peak information
				while(line.contains(PK$))
				{
					peakInformation = addElementsToMap(line, peakInformation);

					if(line.contains("PK$PEAK:"))
					{

						readPeaks=true;
						line = in.readLine();
					}
					else
					{
						line=in.readLine();
					}
				}
							
				//read Peaks
				if(line.equals("")) break;
				
//
//				System.out.println(line+"\t"+readPeaks);

				
				while(!line.contains("//") && readPeaks)/* && line!=null*/    
				{		
					
					
					String splitString[] = new String[line.split("\\s+").length];
					splitString=line.split("\\s+");

					
					peaks.add(new Peak(Double.valueOf(splitString[1]), Double.valueOf(splitString[2]), Double.valueOf(splitString[3]), collisionEnergy));
					line = in.readLine();
			
				}
				
				
				
				line = in.readLine();
				
			}
			
			in.close();
			

			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		record.put("Info", recordSpecificInformation );
		record.put(CH$, compoundInformation);
		record.put(SP$, sampleInformation);
		record.put(AC$, analyticalInformation);
		record.put(MS$, spectralInformation);
		record.put(PK$, peakInformation);

	
		formula = record.get(CH$).get(CH$+CH.FORMULA.toString()).get(0);

		name = record.get(CH$).get(CH$+CH.NAME.toString()).get(0);
				
		if(record.get(CH$).get(CH$+CH.NAME.toString()).size() > 0)
		{
			nameTrivial = record.get(CH$).get(CH$+CH.NAME.toString()).get(0);
		}

		if(record.get(AC$).containsKey(AC$+AC.INSTRUMENT.toString()) )	
			instrument = record.get(AC$).get(AC$+AC.INSTRUMENT.toString()).get(0); 
	    
		if(record.get(CH$).get(CH$+CH.IUPAC.toString()).get(0).contains("InChI=") )
		{
			IUPAC = record.get(CH$).get(CH$+CH.IUPAC.toString()).get(0);
		}
		else
		{
			IUPAC = "InChI="+record.get(CH$).get(CH$+CH.IUPAC.toString()).get(0).replace(" ", ""); 
		}
		
		mass = Double.valueOf(record.get(CH$).get(CH$+CH.EXACT_MASS.toString()).get(0)).doubleValue();
		

		smiles = record.get(CH$).get(CH$+ CH.SMILES.toString()).get(0);		
		//COMPOUND INFORMATION MAP
		
		Map<String, ArrayList<String>> chmap = record.get(CH$);
		if (record.get(CH$).containsKey(CH$ + CH.LINK.toString())) {
			String chkey = CH$ + CH.LINK.toString();

			ArrayList<String> chs = chmap.get(chkey);

			// System.out.println(chmap.toString());

			for (String ch : chs) {

//				String[] dblink = ch.split("\\s+");


//				if (dblink[1].equals("PUBCHEM")) {
//
//					String splitString[] = new String[dblink[2].split(":").length];
//
//					splitString = dblink[2].split(":");
//
//					if(splitString.length>=2 && dblink[1].equals("CID"))
//					{
//						linkPubChem = Integer.valueOf(splitString[1]).intValue();
//					}
//					else
//					{
//						if(splitString.length>=2 && dblink[1].equals("SID"))
//						{
//							sid= Integer.valueOf(splitString[1]).intValue();
//						}
//					}
//				} else if (dblink[1].equals("KEGG") && dblink.length >=3)
//					linkKEGG = dblink[2];
//				else if (dblink[1].equals("CHEBI") && dblink.length >=3)
//					linkCHEBI = dblink[2];
//				else if (dblink[1].contains("METLIN") && dblink.length>=3)
//					linkMetlin=dblink[2];
//				else if (dblink[1].contains("KNAPSACK") && dblink.length>=3)
//					linkKnapSack=dblink[2];

				for (int i = 0; i < allIDs.length; i++) {
					
					if(allIDs[i]!=null && ch.contains(allIDs[i].toString()))
					{
						String link = ch.replace(allIDs[i].toString(), "");
						link = link.replace(":", "");
						link=link.trim();
						
						dbLinks.put(allIDs[i], link);
						
					}
					
				}
				
				
			}
		}

		
		// ANALYTICAL CONDITIONS MAP
		Map<String, ArrayList<String>> acmap = record.get(AC$);
		String ackey = AC$+AC.ANALYTICAL_CONDITION.toString();
		
		ArrayList<String> acs = acmap.get(ackey);
		
		for (String ac : acs) {
			
			if(ac.startsWith(PRECURSOR_TYPE))
			{
				
			
				String splitString[] = new String [ ac.split("\\s+").length];
				splitString =ac.split("\\s+");
				
				precursorType = splitString[2];
			}
			
			if(ac.startsWith(COLLISION_ENERGY))
			{
				String splitString[] = new String[ ac.split("\\s+").length ];
				splitString= ac.split("\\s+");
	
				try{
					
					collisionEnergy = Integer.valueOf(splitString[2]).intValue();
					collisionEnergy = Integer.valueOf(splitString[2]).intValue();
				}
				catch(NumberFormatException e)
				{
	
					splitString[2].toUpperCase();
					if(splitString[2].equals("RAMP"))
					{
						String[] splitString2 = new String[splitString[3].split("-").length];
						splitString2= splitString[3].split("-");
						//TODO: divide between minimum and maximum energy
						collisionEnergy =  Integer.valueOf(splitString2[0]).intValue();
						collisionEnergy =  Integer.valueOf(splitString2[1]).intValue();
						
						
					}
				}
			}
			
			if(ac.startsWith(MODE))
			{
				String splitString[] = new String[ac.split("\\s+").length];
				
				splitString = ac.split("\\s+");
	
				if(splitString[2].equals("POSITIVE"))
				{
					mode=1;
					isPositive=true;
				}
				else
				{
					mode=-1;
				}
			}
			
		}

		
		// SPECTRAL INFORMATION MAP
		Map<String, ArrayList<String>> msmap = record.get(MS$);
		String mskey = MS$+MS.FOCUSED_ION.toString();
		
		if(msmap.containsKey(mskey))
		{
			ArrayList<String> mss = msmap.get(mskey);
			
			for (String ms : mss) {
			
				if(ms.startsWith(PRECURSOR_TYPE))
				{
					String splitString[] = new String [ ms.split("\\s+").length];
					splitString =ms.split("\\s+");
				
					precursorType = splitString[2];
				}
			
			}
		}
		

		if(precursorType.equals("[M+H]+"))
		{
			mode=1;
			isPositive=true;
		}
		else{
			if( mode!=1)
			{
				mode=-1;
			}
		}

		
		if(precursorMZ==0.0) //TODO: 
		{
			
			Map<String ,Double> preType = readPrecursorTypes();
		
			if(preType.containsKey(precursorType))
			{
				precursorMZ=preType.get(precursorType)+mass;
			}
			
		}
		
		//TODO: add sid to spectra, or better create a Map <CompoundDatabases, (ID)String>
		
		spectra.add(new Spectrum(collisionEnergy, peaks, mass, mode, IUPAC, dbLinks,nameTrivial, formula, precursorMZ, precursorType, isPositive, smiles));	
		return spectra;
	}
	
	
	/**
	 * Adds the elements to map.
	 *
	 * @param line the line of massbank file
	 * @param map the map
	 * @return the map
	 */
	public static Map<String,ArrayList<String>> addElementsToMap(String line, Map<String,ArrayList<String>> map)
	{
		String[] splitString = new String[line.split(":").length];
		splitString = line.split(":");

		if(splitString.length>2)
		{
			for (int i = 2; i < splitString.length; i++) {
				splitString[1] += ":"+splitString[i]; 
			}
		}
		
		if(map.containsKey(splitString[0]))
		{
			
			ArrayList<String> elements = map.get(splitString[0]);
			elements.add(splitString[1]);
			map.put(splitString[0], elements );	
		}
		else{
			
			ArrayList<String> elements = new ArrayList<String>(); 
			elements.add(splitString[1]);
			map.put(splitString[0], elements );	
		}
		
		return map;
	}
	
	/**
	 * Read precursor types.
	 *
	 * @return the map
	 */
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
    	}
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			
			
			String line =in.readLine();
			 line=in.readLine();
			
			while(line!=null)
			{
		
				if(!line.substring(0,1).equals("#"))
				{
					
					String [] splitString=new String[line.split("\\s+").length];
					splitString=line.split("\\s+");
					preType.put(splitString[0], Double.valueOf(splitString[1]).doubleValue());

					
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


	
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		
		//Vector<Spectrum> spectra = Read("/home/ftarutti/records/CO000001.txt");
		//Vector<Spectrum> spectra = Read("/home/ftarutti/records/PB000122.txt");
		//Vector<Spectrum> spectra = Read("/home/ftarutti/records/PR100124.txt");
		//Vector<Spectrum> spectra = Read("/vol/massbank/data/records/PR100975.txt");
		//Vector<Spectrum> spectra = Read("/home/ftarutti/records/PR100040.txt");
		//Vector<Spectrum> spectra = Read("/home/ftarutti/records/PB006007.txt");
		
	

		
		Vector<Spectrum> spectra = Read("/home/ftarutti/testspectra/toMerge/XX006704.txt");
		for (Spectrum spectrum : spectra) {
			
			spectrum.show();
			Vector<Peak> peaks = spectrum.getPeaks();
			
			System.out.println(spectrum.dblinks.toString());
			
			
			}
		
	}
}
