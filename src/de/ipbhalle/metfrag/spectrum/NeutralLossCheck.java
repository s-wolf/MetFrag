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

package de.ipbhalle.metfrag.spectrum;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openscience.cdk.formula.MolecularFormula;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

import de.ipbhalle.metfrag.massbankParser.Peak;

public class NeutralLossCheck {
	
	private Map<Double, NeutralLoss> neutralLoss = new HashMap<Double, NeutralLoss>();
	private List<List<NeutralLoss[]>> neutralLossCombinations = new ArrayList<List<NeutralLoss[]>>();
	
	/**
	 * Instantiates a new neutral loss rules.
	 */
	public NeutralLossCheck(int maximumCombinations){
		//read in the neutral losses from file
		ReadInNeutralLosses();
		generateNeutralLossCombinations(maximumCombinations);
	}
	
	
	/**
	 * Gets the neutral losses which are stored in a file.
	 * 
	 * @return the neutral losses
	 */
	private void ReadInNeutralLosses()
	{
		neutralLoss = new HashMap<Double, NeutralLoss>();
		try 
        {	
			String file = "";
        	if(System.getProperty("property.file.path") != null)
        	{
        		file = System.getProperty("property.file.path");
        		file += "neutralLossSMARTS.csv";
        	}
        	else
        	{
        		URL url = AssignFragmentPeak.class.getClassLoader().getResource("neutralLossSMARTS.csv");
    			file = url.getFile();
        		//System.out.println("Pfad: " + url.getFile());
        	}
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
    	      if(strLine.equals("//"))
    	    	  break;
    	      
    	      if(strLine.startsWith("#"))
    	    	  continue;
    	      
    	      String[] lineArray = strLine.split("\t");
    	      IMolecularFormula formula = new MolecularFormula();
    	      int mode = 1;
    	      //positive and negative mode
    	      if(lineArray[0].equals("+ -"))
    	    	  mode = 0;
    	      //negative mode
    	      else if(lineArray[0].equals("-"))
    	    	  mode = -1;
    	      
    	      IMolecularFormula mfT = new MolecularFormula();
    	      IMolecularFormula mfE = new MolecularFormula();
    	      double deltaM = Double.parseDouble(lineArray[1]);
    	      NeutralLoss nl = new NeutralLoss(deltaM, MolecularFormulaManipulator.getMolecularFormula(lineArray[3], mfE), MolecularFormulaManipulator.getMolecularFormula(lineArray[2], mfT), mode, Integer.parseInt(lineArray[4]), Integer.parseInt(lineArray[5]), lineArray[6], Integer.parseInt(lineArray[7]), lineArray[8]);
    	      
    	      neutralLoss.put(deltaM, nl);
    	      //System.out.println("Bond: '" + bond + "' Energy: '" + energy + "'");
    	    }
    	    //Close the input stream
    	    in.close();
        } 
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
	}

	
//	public List<List<NeutralLoss>> checkForNeutralLoss(IAtomContainer fragmentStructure, Peak peak, double fragmentMass, double matchMass)
//	{
//		List<List<NeutralLoss>> neutralLossesMatched = new ArrayList<List<NeutralLoss>>();
//		
//		for (Double neutralLossMass : neutralLoss.keySet()) {
//			
//		}
//		
//		return neutralLossesMatched;		
//	}
	
	
	/**
	 * Generate neutral loss combinations. Each line contains the rules to be applied.
	 *
	 * @param maximumCombinations the maximum combinations
	 */
	private void generateNeutralLossCombinations(int maximumCombinations)
	{
		//sort the neutral loss masses ascending
		Double[] sortedNeutralLosses = new Double[this.neutralLoss.size()];
		sortedNeutralLosses = this.neutralLoss.keySet().toArray(sortedNeutralLosses);
		Arrays.sort(sortedNeutralLosses);
		
		//outer list stores the layers
		//next list stores the neutral losses combinations
		//this list contains the single neutral losses
		List<List<NeutralLoss[]>> neutralLossCombinations = new ArrayList<List<NeutralLoss[]>>(maximumCombinations);
		
		for (int i = 1; i <= maximumCombinations; i++) {

			//thats the first layer....no neutral loss combinations ... just single rules
			if(i == 1)
			{
				List<NeutralLoss[]> neutralLossLayer = new ArrayList<NeutralLoss[]>(sortedNeutralLosses.length);
				for (int j = 0; j < sortedNeutralLosses.length; j++) {
					NeutralLoss[] currentNeutraLossCombination = new NeutralLoss[1];
					currentNeutraLossCombination[0] = this.neutralLoss.get(sortedNeutralLosses[j]);
					neutralLossLayer.add(currentNeutraLossCombination);
				}
				neutralLossCombinations.add(neutralLossLayer);
			}
			else
			{
				List<NeutralLoss[]> previousLayer = neutralLossCombinations.get(i - 2);
				//calculate the size of the next layer (Kombination mit Wiederholung)
				int currentLayerSize = binomial((this.neutralLoss.size() + maximumCombinations - 1), maximumCombinations);
				List<NeutralLoss[]> neutralLossLayer = new ArrayList<NeutralLoss[]>(currentLayerSize);
				
				
				int count = 0;
				//prepare the current layer with the previous rules
				for (NeutralLoss[] previousNeutralLossCombination : previousLayer) {									
					
					//now add to every previously generated rule a single rule from the list
					for (int j = 0 + count; j < sortedNeutralLosses.length; j++) {
						//extend the array by 1
						NeutralLoss[] currentNeutralLossCombination = new NeutralLoss[previousNeutralLossCombination.length + 1];
						//copy the previous data
						for (int k = 0; k < previousNeutralLossCombination.length; k++) {
							currentNeutralLossCombination[k] = previousNeutralLossCombination[k];
						}
						currentNeutralLossCombination[currentNeutralLossCombination.length - 1] = this.neutralLoss.get(sortedNeutralLosses[j]);
						if(i > 2 && !isAlreadyContained(neutralLossLayer, currentNeutralLossCombination, currentNeutralLossCombination.length))
							neutralLossLayer.add(currentNeutralLossCombination);
						else if(i == 2)
							neutralLossLayer.add(currentNeutralLossCombination);
					}
					//prevent identical combinations
					if(i == 2)
						count++;
				}
				
				neutralLossCombinations.add(neutralLossLayer);
			}
		}
		
		this.setNeutralLossCombinations(neutralLossCombinations);
	}
	
	
	
	/**
	 * Checks if is already contained.
	 *
	 * @param neutralLossLayer the neutral loss layer
	 * @param currentNeutralLossCombination the current neutral loss combination
	 * @param count the count
	 * @return true, if is already contained
	 */
	private boolean isAlreadyContained(List<NeutralLoss[]> neutralLossLayer, NeutralLoss[] currentNeutralLossCombination, int count)
	{
		boolean ret = false;
		
		boolean[] testArray = new boolean[count];
		
		int temp = 0;
		for (NeutralLoss[] neutralLossCombination : neutralLossLayer) {
			for (NeutralLoss neutralLoss : neutralLossCombination) {
				for (int i = 0; i < currentNeutralLossCombination.length; i++) {
					if(!testArray[i] && neutralLoss.getMass() == currentNeutralLossCombination[i].getMass())
					{
						testArray[i] = true;
						temp++;
						break;
					}
				}
			}
			if(temp == count)
				return true;
			
			temp = 0;
			testArray = new boolean[count];
		}		
		
		return ret;
	}
	
	/**
	 * Binomial coefficient.
	 *
	 * @param n the n
	 * @param k the k
	 * @return the int
	 */
	private int binomial(int n, int k)
	{
		return factorial(n)/(factorial(n-k)*factorial(k));
	}
	
    /**
	 * Factorial.
	 *
	 * @param n the n
	 * @return the int
	 */
	private int factorial( int n )
    {
        if( n <= 1 )     // base case
            return 1;
        else
            return n * factorial( n - 1 );
    }
	
//	/**
//     * Adds the neutral losses but only where it possibly explaines a peak.
//     * Properties to be set seperated by ",", each column is one "entry":
//     * <ul>
//     * <li>Neutral loss list: elemental composition (-H2O,-HCOOH,CO2)
//     * <li>Neutral loss masses (18.01056, 46.00548, 43.98983)
//     * <li>Hydrogen difference (-H,-H,+H)
//     * <li>Current fragment mass...a single value (147.0232)
//     * </ul>
//     * 
//     * @param fragment the fragment
//     * @param fragmentFormula the fragment formula
//     * @param initialMolecule the initial molecule only important for the start
//     * 
//     * @return the list< i atom container>
//     * 
//     * @throws IOException Signals that an I/O exception has occurred.
//     * @throws CloneNotSupportedException the clone not supported exception
//     * @throws CDKException the CDK exception
//     */
//    private List<IAtomContainer> AddNeutralLosses(IAtomContainer fragment, IMolecularFormula fragmentFormula, boolean initialMolecule) throws IOException, CloneNotSupportedException, CDKException
//    {
//    	List<IAtomContainer> ret = new ArrayList<IAtomContainer>();
//    	double mass = MolecularFormulaTools.getMonoisotopicMass(fragmentFormula);
//    	Map<String, Double> originalFormulaMap = MolecularFormulaTools.parseFormula(fragmentFormula);
//    	boolean checked = false;
//    	
//    	//in the first layer add all neutral losses!!! afterwards only if it matches a peak!
//    	for (Peak peak : peakList) {
//    		
//    		if(initialMolecule && checked)
//    			break;
//    		
//    		double peakLow = peak.getMass() - this.mzabs - PPMTool.getPPMDeviation(peak.getMass(), this.mzppm);
//            double peakHigh = peak.getMass() + this.mzabs + PPMTool.getPPMDeviation(peak.getMass(), this.mzppm);
//    		checked = true;
//    		
//    		for (Double neutralLossMass : this.neutralLoss.keySet()) {
//        		//filter appropriate neutral losses by mode...0 means this occurs in both modes
//        		if(this.neutralLoss.get(neutralLossMass).getMode() == mode || this.neutralLoss.get(neutralLossMass).getMode() == 0)
//        		{
//        			IMolecularFormula neutralLossFormula = this.neutralLoss.get(neutralLossMass).getElementalComposition();
//        			boolean isPossibleNeutralLoss = MolecularFormulaTools.isPossibleNeutralLoss(originalFormulaMap, neutralLossFormula);
//        			double protonMass = Constants.PROTON_MASS * mode;
//        				
//    				if((isPossibleNeutralLoss && ((mass+protonMass)-neutralLossMass) >= peakLow && (((mass+protonMass)-neutralLossMass) <= peakHigh)) || initialMolecule)
//    				{
//    					List<IAtomContainer> fragmentsNL = pp.postProcess(fragment, neutralLossMass);
//    					for (IAtomContainer fragmentNL : fragmentsNL) {
//    						
//    						IMolecularFormula fragmentMolFormula = MolecularFormulaManipulator.getMolecularFormula(fragmentNL);
//    						Double fragmentMass = MolecularFormulaTools.getMonoisotopicMass(fragmentMolFormula);
//    						    						
//    						//skip this fragment which is lighter than the smallest peak
//    						if(fragmentMass < minWeight)
//    							continue;
//    						
//	    					//add neutral loss elemental composition to atomcontainer
////	    					fragmentNL.setProperty("NlElementalComposition", AddToProperty((String)fragmentNL.getProperty("NlElementalComposition"), MolecularFormulaManipulator.getString(neutralLossFormula)));
////	    					//add neutral loss mass
////	    					fragmentNL.setProperty("NlMass", AddToProperty((String)fragmentNL.getProperty("NlMass"), neutralLossMass.toString()));
////	    					//set H difference
////	    					fragmentNL.setProperty("NlHydrogenDifference", AddToProperty((String)fragmentNL.getProperty("NlHydrogenDifference"), "" + this.neutralLoss.get(neutralLossMass).getHydrogenDifference()));
////	    					//set current Fragment mass
////	    					//fragmentNL.setProperty("FragmentMass", ReplaceMassProperty((String)fragmentNL.getProperty("FragmentMass"), fragmentFormula, neutralLossFormula));
////	    					IMolecularFormula formulaFragment = MolecularFormulaManipulator.getMolecularFormula(fragmentNL);
////	    					fragmentNL.setProperty("FragmentMass", MolecularFormulaTools.getMonoisotopicMass(formulaFragment));
//	    					//set bond energy
//	    					fragmentNL = setBondEnergy(fragment, fragmentNL, 500.0);
//	    					//set partial charge diff to 0
//	    					fragmentNL = setCharge(fragmentNL, 0.0);
//	    					
//	    					
//	    					Map<Object, Object> props = fragmentNL.getProperties();
//	    					props.put("NeutralLossRule", MolecularFormulaManipulator.getString(neutralLossFormula));
//	    					
//	    					if(smilesRedundancyCheck)
//	    					{
//	    						SmilesGenerator sg = new SmilesGenerator();
//		    					IMolecule fragNLMol = new Molecule(fragmentNL);
//		    					String smiles = sg.createSMILES(fragNLMol);
//		    					props.put("smiles", smiles);
//	    					}
//	    					
//	    					addFragmentToListMap(fragmentNL, MolecularFormulaManipulator.getString(fragmentMolFormula));
//	    					
//	    					//add to result list
//	    					ret.add(fragmentNL);
//						}
//    					//peak found....test another one
//    					continue;
//    				}
//        		}
//        	}
//		}
//    	return ret;
//    }
	
    
    public static void main(String[] args) {
		System.out.println("Maximum combinations: 3");
		NeutralLossCheck nc3 = new NeutralLossCheck(3);
	}


	public void setNeutralLossCombinations(List<List<NeutralLoss[]>> neutralLossCombinations) {
		this.neutralLossCombinations = neutralLossCombinations;
	}


	/**
	 * Gets the neutral loss combinations.
	 * 
	 * The outer list contains the "layers" (e.g. the first layer are all neutral loss rules alone, 
	 * the second are combinations of 2 neutral loss rules)
	 * 
	 * The containing list contains the array of neutral losses.
	 * 
	 *
	 * @return the neutral loss combinations
	 */
	public List<List<NeutralLoss[]>> getNeutralLossCombinations() {
		return neutralLossCombinations;
	}
}
