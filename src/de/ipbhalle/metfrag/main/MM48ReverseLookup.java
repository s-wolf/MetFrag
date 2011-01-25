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
package de.ipbhalle.metfrag.main;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.formula.MolecularFormula;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

import de.ipbhalle.metfrag.fragmenter.Fragmenter;
import de.ipbhalle.metfrag.massbankParser.Peak;
import de.ipbhalle.metfrag.pubchem.PubChemWebService;
import de.ipbhalle.metfrag.spectrum.AssignFragmentPeak;
import de.ipbhalle.metfrag.spectrum.CleanUpPeakList;
import de.ipbhalle.metfrag.spectrum.MatchedFragment;
import de.ipbhalle.metfrag.spectrum.NeutralLoss;
import de.ipbhalle.metfrag.spectrum.WrapperSpectrum;

public class MM48ReverseLookup {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//results map
		Map<Integer, Map<String, Integer>> compoundToFile = new HashMap<Integer, Map<String,Integer>>();
		Map<Integer, Integer> compoundToPubchem = new HashMap<Integer, Integer>();
		
		double mzabs = 0.0;
		double mzppm = 0.0;
		
		if(args.length > 1)
		{
			mzabs = Double.parseDouble(args[0]);
			mzppm = Double.parseDouble(args[1]);
		}
		else
		{
			System.err.println("No mzabs and mzppm given!\ne.g. mzabs mzppm: 0.01 50 as parameter!");
			System.exit(1);
		}
		
		
		
		//read in file
		try 
        {	
			
			String file = "/vol/data/lcms/archive/MICROTOF-Q/cboettch/MM48/MM48.csv";
			String folderPeakList = "/vol/data/lcms/archive/MICROTOF-Q/cboettch/MM48/microTOFQ/050309/PeakLists";
			
			FileInputStream fstream = new FileInputStream(new File(file));
		    // Get the object of DataInputStream
		    DataInputStream in = new DataInputStream(fstream);
		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    String strLine;
		    boolean first = true;
		    
		    //Read File Line By Line
		    while ((strLine = br.readLine()) != null)   {
		    	//skip header
		    	if(first)
		    	{
		    		first = false;
		    		continue;
		    	}
		      
		      if(strLine.startsWith("#"))
		    	  continue;
		      
		      char[] charArray = strLine.toCharArray();
		      boolean start = false;
		      String strLineClean = "";
		      for (int i = 0; i < charArray.length; i++) {
		    	  if(charArray[i] == '\"' && !start)
		    		  start = true;
		    	  else if(charArray[i] == '\"')
		    	  {
		    		  start = false;
		    	  }
		    	  //in between a string
		    	  if(start && charArray[i] == ',')
		    		  strLineClean += "#";
		    	  else
		    		  strLineClean += charArray[i] + "";
		      }
		      String[] lineArray = strLineClean.split(",");
		      Integer number = Integer.parseInt(lineArray[0]);
		      String name = lineArray[1].replace("\"", "").replace("#", ",");
		      String formula = lineArray[2].replace("\"", "").replace("#", ",");
		      String pubchemID = lineArray[3];
		      Double parentMass = Double.parseDouble(lineArray[5]);
		    
		      System.out.println(number + " Name: " + name + " Formula: " + formula + " PubChem: " + pubchemID + " ParentMass: " + parentMass);
		      
		      if(pubchemID.isEmpty())
    	      {
    	    	  System.err.println("\nNo PubChem identifier given! " + number + " Name: " + name + "\n");
    	    	  continue;
    	      }
		      
		      
		      PubChemWebService pubchemService = new PubChemWebService();
			  IAtomContainer molecule = pubchemService.getSingleMol(pubchemID, true);
		      
		      System.out.println("\nNow match spectra\n");
		      File f = new File(folderPeakList);
		      File files[] = f.listFiles();
    	      
    	      
    	      compoundToPubchem.put(number, Integer.parseInt(pubchemID));
		      
		      //now fragment the molecule
	    	  try
		      {
			      //add hydrogens
			      CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(molecule.getBuilder());
	
			      for (IAtom atom : molecule.atoms()) {
			          IAtomType type = matcher.findMatchingAtomType(molecule, atom);
			          AtomTypeManipulator.configure(atom, type);
			      }
			      CDKHydrogenAdder hAdder = CDKHydrogenAdder.getInstance(molecule.getBuilder());
			      hAdder.addImplicitHydrogens(molecule);
			      AtomContainerManipulator.convertImplicitToExplicitHydrogens(molecule);
		      }
		      //there is a bug in cdk?? error happens when there is a S or Ti in the molecule
		      catch(IllegalArgumentException e)
	          {
		    	  System.out.println("Error: Something wrong with molecule");
		    	  continue;
	          }
		        
		      Vector<Peak> peakListTemp = new Vector<Peak>();
		      peakListTemp.add(new Peak(30, 200, 10));
		      Fragmenter fragmenter = new Fragmenter(peakListTemp, mzabs, mzppm, 1, true, true, false);     
		      List<IAtomContainer> l = null;
		      try
		      {
		    	  l = fragmenter.generateFragmentsInMemory(molecule, true, 2);
		      }
		      catch(OutOfMemoryError e)
		      {
		    	  System.out.println("OUT OF MEMORY ERROR! " + pubchemID);
		    	  continue;
		      }
	        
		      List<IAtomContainer> fragments = l; 
		      
		      Map<String, Integer> fileToHits = new HashMap<String, Integer>();
    	      for(int i=0;i<files.length;i++)
    	      {
    	    	  if(files[i].isFile())
    	    	  {
    	    		  
    	    		  FileInputStream fstream1 = new FileInputStream(files[i]);
    	    		  // Get the object of DataInputStream
    	    		  DataInputStream in1 = new DataInputStream(fstream1);
    	    		  BufferedReader br1 = new BufferedReader(new InputStreamReader(in1));
    	    		  String peakList = "";
    	    		  while ((strLine = br1.readLine()) != null)   {
    	    			  peakList += strLine + "\n";
    	    		  } 
    	    		  in1.close();
    	    		  fstream1.close();
	    		    	
    	    		  WrapperSpectrum spectrum = new WrapperSpectrum(peakList, 1, parentMass, true);
    	    		  //get the original peak list again
    	    		  Vector<Peak> peakListParsed = spectrum.getPeakList();
	    				
	    				
    	    		  //clean up peak list
    	    		  CleanUpPeakList cList = new CleanUpPeakList((Vector<Peak>) peakListParsed.clone());
    	    		  Vector<Peak> cleanedPeakList = cList.getCleanedPeakList(spectrum.getExactMass());
	    				
	    				
    	    		  //now find corresponding fragments to the mass
    	    		  AssignFragmentPeak afp = new AssignFragmentPeak(3);
    	    		  afp.assignFragmentPeak(fragments, cleanedPeakList, mzabs, mzppm, spectrum.getMode(), true, true);
    	    		  Vector<MatchedFragment> hits = afp.getHits();
    	    		  
    	    		  //add to result map
    	    		  fileToHits.put(files[i].getName(), hits.size());
	    			  
//    	    		  System.out.println("File name: " + files[i].getName() + " Pubchem: " + pubchemID + " Hits: " + hits.size());
    	    	  }
    	      }
    	      
    	      compoundToFile.put(number, fileToHits);

    	    }
    	    //Close the input stream
    	    in.close();
    	    fstream.close();
        }
		catch (Exception e) {
			e.printStackTrace();
		}
		
		
		Map<String, Integer> resultFileToHits = new HashMap<String, Integer>();
		Map<String, Vector<Integer>> resultFileToPubChem = new HashMap<String, Vector<Integer>>();
		
		//now process results 
		for (Integer compound : compoundToPubchem.keySet()) {
			//get map storing for each file the number of hits
			Map<String, Integer> tempFileHits = compoundToFile.get(compound);
			//for each compound check every file and the maximum number of hits!
			for (String file : tempFileHits.keySet()) {
				Integer hits = tempFileHits.get(file);
				String fileBestFit = "";
				int currentHits = 0;
				
				if(resultFileToHits.get(file) == null)
				{
					resultFileToHits.put(file, hits);
					//add pubchem id
					Vector<Integer> pubChemIDs = new Vector<Integer>();
					pubChemIDs.add(compoundToPubchem.get(compound));
					resultFileToPubChem.put(file, pubChemIDs);
				}
				else
				{
					currentHits = resultFileToHits.get(file);
					if(hits > currentHits)
					{
						resultFileToHits.put(file, hits);
						
						Vector<Integer> pubChemIDs = new Vector<Integer>();
						pubChemIDs.add(compoundToPubchem.get(compound));
						resultFileToPubChem.put(file, pubChemIDs);

					}
					else if(hits == currentHits)
					{
						if(resultFileToPubChem.get(file).isEmpty())
						{
							Vector<Integer> pubChemIDs = new Vector<Integer>();
							pubChemIDs.add(compoundToPubchem.get(compound));
							resultFileToPubChem.put(file, pubChemIDs);
						}
						else
						{
							Vector<Integer> pubChemIDs = resultFileToPubChem.get(file);
							pubChemIDs.add(compoundToPubchem.get(compound));
							resultFileToPubChem.put(file, pubChemIDs);
						}
					}
				}
				
			}
		}
		
		//now print complete results
		for (String file : resultFileToHits.keySet()) {
			Vector<Integer> pubchemIds = resultFileToPubChem.get(file);
			System.out.print("File: " + file + " PubChem: ");
			for (Integer integer : pubchemIds) {
				System.out.print(integer + " ");
			}
			System.out.print(" Hits: " + resultFileToHits.get(file) + "\n");
		}
		
	}
}
