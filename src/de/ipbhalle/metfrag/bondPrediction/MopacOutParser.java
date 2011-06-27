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

package de.ipbhalle.metfrag.bondPrediction;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MopacOutParser {
	
	private String filename;
	private double heatOfFormation = -1;
	private String time = "-1";
	private String error = "";
	private String warning = "";
	
	private int numberOfAtoms=0;
	
	
	Map<String, ArrayList<String> > bondOrder = new HashMap<String, ArrayList<String> >();
	

	
	/**
	 * Instantiates a new mopac out parser. This parses the *.OUT file from MOPAC
	 * and scans it for errors and the HEAT OF FORMATION.
	 *
	 * @param fileName the file name
	 */
	public MopacOutParser(String fileName,int numberOfAtoms)
	{
		this.numberOfAtoms=numberOfAtoms;
		
		this.filename = fileName;
		readData();
		
		
	}
	
	/**
	 * Read data from the file.
	 */
	private void readData()
	{
		
		try 
		{
			FileReader fr = new FileReader(this.filename);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();

			while (line != null)
			{
				if(line.contains("FINAL HEAT OF FORMATION"))
				{
					String[] tmpArray = line.split("=");
					String[] hofArray = tmpArray[1].split("\\sK");
					heatOfFormation = Double.parseDouble(hofArray[0].trim());
				}
				
				if(line.contains("COMPUTATION TIME"))
				{
					String[] tmpArray = line.split("=");
					time = tmpArray[1].trim();
				}
				
				if(line.contains("DUE TO PROGRAM BUG, THE FIRST THREE ATOMS MUST NOT LIE IN A STRAIGHT LINE"))
				{
					error = "DUE TO PROGRAM BUG, THE FIRST THREE ATOMS MUST NOT LIE IN A STRAIGHT LINE";
					break;
				}
				
				if(line.contains("THERE IS NOT ENOUGH TIME FOR ANOTHER CYCLE"))
				{
					error += "THERE IS NOT ENOUGH TIME FOR ANOTHER CYCLE; "; 
				}
				
				if(line.contains("EXCESS NUMBER OF OPTIMIZATION CYCLES"))
				{
					error += "EXCESS NUMBER OF OPTIMIZATION CYCLES; "; 
				}
				
				if(line.contains("SOME ELEMENTS HAVE BEEN SPECIFIED FOR WHICH"))
				{
					error += "SOME ELEMENTS HAVE BEEN SPECIFIED FOR WHICH; ";
				}
				
				if(line.contains("REGARDING MOLECULAR MECHANICS CORRECTION"))
				{
					error += "REGARDING MOLECULAR MECHANICS CORRECTION; ";
				}
				
				if(line.contains("NOT ENOUGH TIME FOR ANOTHER CYCLE"))
				{
					error += "NOT ENOUGH TIME FOR ANOTHER CYCLE; ";
				}
				
				if(line.contains("WARNING"))
				{
					warning += line + "; ";
				}
				
				
				if(line.contains("BOND ORDERS AND VALENCIES"))
				{

			
					do
					{
						line = br.readLine();
					}
					while(!line.contains("--"));
					
					line = br.readLine();
					
					while(line.split("\\s+").length>1)
					{
						String foo[] = new String [ line.split("\\s+").length];
						foo = line.split("\\s+");
						
						ArrayList <String> pos= new ArrayList<String>();
						for(int j=3;j<foo.length;j++)
						{
							pos.add(foo[j]);
							
						}
						
						bondOrder.put(foo[2] , pos);
						
						line = br.readLine();
					}
					

					do
					{
						//Check, if there are more than one tables; the tables are separated with lines containing either 0 or 1					
						while(line.split("\\s+").length==1)
						{
							line = br.readLine();
							
						}
						
						
						
						do
						{
							line = br.readLine();
						}
						while(!line.contains("--"));
						
						line=br.readLine();
						
						while(line.split("\\s+").length>1)
						{
					
							String foo[] = new String [ line.split("\\s+").length];
							foo = line.split("\\s+");
							
							
							if(bondOrder.containsKey(foo[2]))
							{
								for(int j=3;j<foo.length;j++)
								{
									bondOrder.get(foo[2]).add(foo[j]);								
								}
							}
							else{
								ArrayList <String> pos= new ArrayList<String>();
								for(int j=3;j<foo.length;j++)
								{
									pos.add(foo[j]);
								
								}
							
								bondOrder.put(foo[2] , pos);
							}
							line = br.readLine();
							
						}
				
		
						
					}
					while(  !(bondOrder.size()==numberOfAtoms && bondOrder.get(""+numberOfAtoms).size() == numberOfAtoms ));
					
				
					
				}
				
				line = br.readLine();
		    }
			br.close();
		}
		catch (IOException e) {
			System.err.println(e);
		}		  
	}

	public void setWarning(String warning) {
		this.warning = warning;
	}

	public String getWarning() {
		return warning;
	}

	public double getHeatOfFormation() {
		return heatOfFormation;
	}

	public void setHeatOfFormation(double heatOfFormation) {
		this.heatOfFormation = heatOfFormation;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	public Map<String, ArrayList<String> > getBondOrder()
	{
		return bondOrder;
	}
	
	public static void main(String[] args) {
		//MopacOutParser parser = new MopacOutParser("/tmp/molMopIN970033810921000308.OUT");
		//System.out.println("HoF: " + parser.getHeatOfFormation() + "\nTime: " + parser.getTime() + "\nWarning: " + parser.getWarning() + "\nError: " + parser.getError());
		
		//parser = new MopacOutParser("/tmp/molMopIN992547100097121308.OUT");
		//System.out.println("HoF: " + parser.getHeatOfFormation() + "\nTime: " + parser.getTime() + "\nWarning: " + parser.getWarning() + "\nError: " + parser.getError());

		
		MopacOutParser parser = new MopacOutParser("/home/ftarutti/Desktop/CheckParser/test35Atome.txt",35);
		
		//System.out.println(parser.bondOrder.get("1").toString());
		for(int i=1;i<parser.bondOrder.size()+1;i++)
		{
			String key = ""+i;
			System.out.println(parser.bondOrder.get(key).toString());
		}
	}

}
