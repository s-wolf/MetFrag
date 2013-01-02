/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package de.ipbhalle.metfrag.keggWebservice;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.MDLReader;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;

import de.ipbhalle.metfrag.tools.MolecularFormulaTools;

import keggapi.*;


public class KeggWebservice {
	
	/**
	 * KEGG search by mass.
	 * TODO: Get candidates through webservice...bget
	 * 
	 * @param mass the mass
	 * @param error the error
	 * 
	 * @return the vector< string>
	 */
	public static Vector<String> KEGGbyMass(double mass, double error)
	{
		Vector<String> KEGGCandidates = new Vector<String>();
		
		try 
		{
	    	KEGGLocator  locator = new KEGGLocator();
            KEGGPortType serv    = locator.getKEGGPort();

            String[] results = serv.search_compounds_by_mass((float)mass, (float)error);
            
            for (int i = 0; i < results.length; i++) {
                System.out.println(results[i]);
                KEGGCandidates.add(results[i]);
            }
	    } 
		catch (Exception e) {
	        System.err.println(e.toString());
	    }
		
		return KEGGCandidates;
	}
	
	
	/**
	 * KEGG get names by cpd id.
	 * 
	 * @param identifier the identifier
	 * 
	 * @return the string
	 */
	public static String[] KEGGgetNameByCpd(String identifier)
	{
		String ret = "";
		
		try 
		{
	    	KEGGLocator  locator = new KEGGLocator();
            KEGGPortType serv    = locator.getKEGGPort();
            //get molecule by accession ID
            String str = "cpd:" + identifier;
            
            ret = serv.bget(str);
	    } 
		catch (Exception e) {
	        System.err.println(e.toString());
	    }
		
		//System.out.println(ret);
		String names = ret.substring(ret.indexOf("NAME") + 12, ret.indexOf("FORMULA") - 1);		
		//System.out.println(names);
		
		//find white spaces
		Pattern p = Pattern.compile("\\s+");
		Matcher m = p.matcher(names);
		String cleanedNames = m.replaceAll(" ");
		
		//System.out.println(cleanedNames);
		
		String[] namesArr = cleanedNames.split(";");
		
		for (int i = 0; i < namesArr.length; i++) {
			Pattern p1 = Pattern.compile("^\\s+");
			Matcher m1 = p1.matcher(namesArr[i]);
			namesArr[i] = m1.replaceAll("");
		}
		
		return namesArr;
	}
	
	
	
	/**
	 * KEGG get name by cpd locally.
	 * 
	 * @param identifier the identifier
	 * @param pathToFile to the compound file
	 * 
	 * @return the string[]
	 * @throws IOException 
	 */
	public static String[] KEGGgetNameByCpdLocally(String identifier, String pathToFile) throws IOException
	{
		String ret = "";
		
		// Open the file that is the first 
	    // command line parameter
	    FileInputStream fstream = new FileInputStream(pathToFile);
	    // Get the object of DataInputStream
	    DataInputStream in = new DataInputStream(fstream);
	    BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    String strLine;

	    boolean found = false;
	    String keggID = "";
	    //Read File Line By Line
	    while ((strLine = br.readLine()) != null)   {
	    	if(!strLine.startsWith("ENTRY") && found == false)
	    		continue;
	    	else
	    	{
	    		//found my entry
	    		if(strLine.startsWith("///") && found)
	    			break;
	    		
	    		//kegg id
	    		if(!found || strLine.startsWith("ENTRY"))
	    			keggID = strLine.substring(12,18);
	    		
	    		if(keggID.equals(identifier) && !found)
	    		{
	    			found = true;
	    		}
	    		
	    		if(found)
	    			ret += strLine + "\n";
	    	}
	    }
	    //Close the input stream
	    in.close();
		
				
	    //System.out.println(ret);
		String names = ret.substring(ret.indexOf("NAME") + 12, ret.indexOf("FORMULA") - 1);		
		//System.out.println(names);
		
		//find white spaces
		Pattern p = Pattern.compile("\\s+");
		Matcher m = p.matcher(names);
		String cleanedNames = m.replaceAll(" ");
		
		//System.out.println(cleanedNames);
		
		String[] namesArr = cleanedNames.split(";");
		
		for (int i = 0; i < namesArr.length; i++) {
			Pattern p1 = Pattern.compile("^\\s+");
			Matcher m1 = p1.matcher(namesArr[i]);
			namesArr[i] = m1.replaceAll("");
		}
		
		return namesArr;
	}
	
	
	/**
	 * KEGG by sum formula.
	 * 
	 * @param sumFormula the sum formula
	 * 
	 * @return the vector< string>
	 */
	public static Vector<String> KEGGbySumFormula(String sumFormula)
	{
		Vector<String> KEGGCandidates = new Vector<String>();
		
		try 
		{
	    	KEGGLocator  locator = new KEGGLocator();
            KEGGPortType serv    = locator.getKEGGPort();

            String[] results = serv.search_compounds_by_composition(sumFormula);
            
            for (int i = 0; i < results.length; i++) {
                System.out.println(results[i]);
                KEGGCandidates.add(results[i]);
            }
	    } 
		catch (Exception e) {
	        System.err.println(e.toString());
	    }
		
		return KEGGCandidates;
	}
	
	
	
	/**
	 * Gets the mol file from a specified KEGG ID.
	 * 
	 * @param accessionID the accession id (e.g. C00509)
	 * 
	 * @return the string
	 */
	public static String KEGGgetMol(String accessionID, String keggPath)
	{
		String ret = "";
		
		try 
		{
	    	KEGGLocator  locator = new KEGGLocator();
            KEGGPortType serv    = locator.getKEGGPort();
            //get molecule by accession ID
            String str = "-f m cpd:" + accessionID;
            
            boolean webservice = false;
            String mol = "";
            
            try
            {
            	// Open the file that is the first 
                // command line parameter
                FileInputStream fstream = new FileInputStream(keggPath + accessionID + ".mol");
                // Get the object of DataInputStream
                DataInputStream in = new DataInputStream(fstream);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));

                //Read File Line By Line
                String line;
                while ((line = br.readLine()) != null)   {
                  // Print the content on the console
                  mol += line + "\n";
                }
                //Close the input stream
                in.close();
            	
            }
            catch(FileNotFoundException e)
            {
            	webservice = true;
            }
            
            //if this file is not found use webservice...otherwise use local molfile
		  	if(webservice || keggPath.equals(""))
		  		ret = serv.bget(str);
		  	else
		  		ret = mol;
		  	
	    } 
		catch (Exception e) {
	        System.err.println(e.toString());
	    }
		
		return ret;
	}
	
	
	/**
	 * Gets the mol file from a specified KEGG ID.
	 * 
	 * @param accessionID the accession id (e.g. C00509)
	 * 
	 * @return the string
	 * @throws CDKException 
	 */
	public static IAtomContainer getMol(String accessionID, String keggPath, boolean getAll) throws CDKException
	{
		String ret = "";
		
		try 
		{
	    	KEGGLocator  locator = new KEGGLocator();
            KEGGPortType serv    = locator.getKEGGPort();
            //get molecule by accession ID
            String str = "-f m cpd:" + accessionID;
            
            boolean webservice = false;
            String mol = "";
            
            try
            {
            	// Open the file that is the first 
                // command line parameter
                FileInputStream fstream = new FileInputStream(keggPath + accessionID + ".mol");
                // Get the object of DataInputStream
                DataInputStream in = new DataInputStream(fstream);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));

                //Read File Line By Line
                String line;
                while ((line = br.readLine()) != null)   {
                  // Print the content on the console
                  mol += line + "\n";
                }
                //Close the input stream
                in.close();
            	
            }
            catch(FileNotFoundException e)
            {
            	webservice = true;
            }
            
            //if this file is not found use webservice...otherwise use local molfile
		  	if(webservice)
		  		ret = serv.bget(str);
		  	else
		  		ret = mol;
		  	
	    } 
		catch (Exception e) {
	        System.err.println(e.toString());
	    }
		
		if(ret == null || ret.equals(""))
			return null;
		
		MDLReader reader;
		List<IAtomContainer> containersList;
		
        reader = new MDLReader(new StringReader(ret));
        ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());
        containersList = ChemFileManipulator.getAllAtomContainers(chemFile);
        IAtomContainer molecule = containersList.get(0);
		
        if(getAll)
        	return molecule;
        if(!MolecularFormulaTools.isBiologicalCompound(molecule))
    		molecule = null;        
        
		return molecule;
		
	}
	
	/**
	 * The main method.
	 * 
	 * @param args the arguments
	 */
	public static void main(String[] args) {
	       try {
//	    	   KEGGLocator  locator = new KEGGLocator();
//               KEGGPortType serv    = locator.getKEGGPort();
//               
//               
//               LinkDBRelation[] results = serv.get_linkdb_by_entry("4202", "pubchem", 1, 5); //get_linkdb_between_databases("pubchem:236", "compound", 1, 10);
//               
//               for (int i = 0; i < results.length; i++) {
//                   System.out.println(results[i].getEntry_id1() + " - " + results[i].getEntry_id2());
//               }
               
               //mol = bget("-f m cpd:C00111")
               
               
               List<String> list = KeggWebservice.KEGGbySumFormula("C19H26N2");
               for (String string : list) {
				System.out.println(string);
               }
               

	      } catch (Exception e) {
	        System.err.println(e.toString());
	      }
	    }
	
}
