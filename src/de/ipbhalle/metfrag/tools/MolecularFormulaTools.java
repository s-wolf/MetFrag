package de.ipbhalle.metfrag.tools;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openscience.cdk.Atom;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.config.IsotopeFactory;
import org.openscience.cdk.config.Symbols;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IElement;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;

import de.ipbhalle.metfrag.database.Tools;


/**
 * The Class MolecularFormulaTools.
 */
public class MolecularFormulaTools {
	
	/**
	 * Gets the monoisotopic mass from a Molecular formula (e.g C1H4).
	 * Not fully tested. The other method with the IMolecularFormula 
	 * Object should be used!
	 * 
	 * @param formula the formula
	 * 
	 * @return the monoisotopic mass
	 */
	public static double getMonoisotopicMass(String formula)
	{
		double mass = 0;
		HashMap<String, Double> parsedFormula = new HashMap<String, Double>();
		try
		{
			parsedFormula = parseFormula(formula);
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
		
		for (String s : parsedFormula.keySet()) {
			mass += parsedFormula.get(s);
		} 
		
		
		return mass;
	}
	
	
	/**
	 * Gets the monoisotopic mass from a Molecular formula.
	 * 
	 * @param formula the formula
	 * 
	 * @return the monoisotopic mass
	 */
	public static double getMonoisotopicMass(IMolecularFormula formula)
	{
		double mass = 0;
		
		
//		HashMap<String, Double> parsedFormula = new HashMap<String, Double>();
//		try
//		{
//			parsedFormula = parseFormula(formula);
//		}
//		catch (IOException e)
//		{
//			System.out.println(e.getMessage());
//		}
//		
//		for (String s : parsedFormula.keySet()) {
//			mass += parsedFormula.get(s);
//		} 
		
		mass = MolecularFormulaManipulator.getTotalExactMass(formula);

		return mass;
	}
	
	
	/**
	 * Gets the monoisotopic mass as string.
	 * 
	 * @param formula the formula
	 * 
	 * @return the monoisotopic mass string
	 */
	public static String getMonoisotopicMassString(String formula)
	{
		Double mass = 0.0;
		HashMap<String, Double> parsedFormula = new HashMap<String, Double>();
		try
		{
			parsedFormula = parseFormula(formula);
		}
		catch (IOException e)
		{
			System.out.println(e.getMessage());
		}
		
		for (String s : parsedFormula.keySet()) {
			mass += parsedFormula.get(s);
		} 
		
		
		return mass.toString();
	}
	
	/**
	 * Parses the formula. Each entry contains the summed up mass.
	 * 
	 * @param formula the formula
	 * 
	 * @return the hash map< string, double>
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static HashMap<String, Double> parseFormula(String formula) throws IOException
	{
		
		HashMap<String, Double> parsedFormula = new HashMap<String, Double>();
		String regexSymbols = "(\\d+)"; //digit at least once
		String regexNumbers = "[A-Z]{1}"; //not a digit
		
	    Pattern pSymbols = Pattern.compile(regexSymbols);
	    Pattern pNumbers = Pattern.compile(regexNumbers);
	    
	    String[] symbols = pSymbols.split(formula);
	    String[] numbers = pNumbers.split(formula);
	    
	    int numberCount = 1;
	    for (int i = 0; i < symbols.length; i++) {
			//create temporary atom with symbol and "configure" it
			IAtom a = new Atom(symbols[i]);
			
			IsotopeFactory isofac = IsotopeFactory.getInstance(new ChemObject().getBuilder());
	        isofac.configure(a);
	        
	        
//	        int temp = Symbols.getAtomicNumber(symbols[i]);
//	        Symbols.
			
			//Bugfix for: C15H11O2N2Cl1
			//while(numbers[numberCount].isEmpty())
			//	numberCount++;
	        
	        //fix if the digit is not written
	        if(numbers[numberCount].isEmpty() && numberCount > 0)
	        	numbers[numberCount] = "1";
			  
			double mass =  a.getExactMass();
			mass = mass *  Double.parseDouble(numbers[numberCount]);
			numberCount++;
			parsedFormula.put(symbols[i], mass);
	    }
		
	    return parsedFormula;
	}
	
	/**
	 * Parses the formula. Each entry contains the summed up mass.
	 * 
	 * @param formula the formula
	 * 
	 * @return the hash map< string, double>
	 * @throws IOException 
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static Map<String, Double> parseFormula(IMolecularFormula formula) throws IOException
	{
		HashMap<String, Double> parsedFormula = new HashMap<String, Double>();		
		
		List<IElement> elements = MolecularFormulaManipulator.elements(formula);
		for (IElement element : elements) {
			int elementCount = MolecularFormulaManipulator.getElementCount(formula, element);
			String symbol = element.getSymbol();
			IAtom a = new Atom(symbol);
			IsotopeFactory isofac = IsotopeFactory.getInstance(new ChemObject().getBuilder());
	        isofac.configure(a);
			double mass =  a.getExactMass();
			mass = mass *  (double)elementCount;
			parsedFormula.put(symbol, mass);
		}
	    return parsedFormula;
	}
	
	/**
	 * Checks if it is a biological compound. Contains no other element than
	 * C,H,O,N,P and S and not only C and H.
	 * 
	 * @param formula the formula
	 * 
	 * @return true, if is biological compound
	 * @throws InvalidSmilesException 
	 */
	public static boolean isBiologicalCompound(String smiles) throws InvalidSmilesException
	{
		boolean test = false;
		
		//test for only c
		if(smiles.contains("C") && (!smiles.contains("O") && !smiles.contains("N") && !smiles.contains("S") && !smiles.contains("P")))
			return false;
		
		//check for chonsp
		SmilesParser sp1 = new SmilesParser(DefaultChemObjectBuilder.getInstance());
		int temp = Tools.checkCHONSP(MolecularFormulaManipulator.getString(MolecularFormulaManipulator.getMolecularFormula(sp1.parseSmiles(smiles))));
		if(temp == 1)
			return true;
		
		return test;
	}
	
	
	/**
	 * Checks if it is a biological compound. Contains no other element than
	 * C,H,O,N,P and S and not only C and H.
	 * 
	 * @param formula the formula
	 * 
	 * @return true, if is biological compound
	 * @throws InvalidSmilesException 
	 */
	public static boolean isBiologicalCompound(IAtomContainer mol) throws InvalidSmilesException
	{
		boolean test = false;
		SmilesGenerator sg = new SmilesGenerator();
		IMolecule molecule = new Molecule(mol);
		String smiles = sg.createSMILES(molecule);
		
		//test for only c
		if(smiles.contains("C") && (!smiles.contains("O") && !smiles.contains("N") && !smiles.contains("S") && !smiles.contains("P")))
			return false;
		
		//check for chonsp
		SmilesParser sp1 = new SmilesParser(DefaultChemObjectBuilder.getInstance());
		int temp = Tools.checkCHONSP(MolecularFormulaManipulator.getString(MolecularFormulaManipulator.getMolecularFormula(sp1.parseSmiles(smiles))));
		if(temp == 1)
			return true;
		
		return test;
	}
	
	
	/**
	 * Checks if it is possible neutral loss from the original fragment.
	 * 
	 * @param originalFormula the original formula
	 * @param neutralLossFormula the neutral loss formula
	 * 
	 * @return true, if is possible neutral loss
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static boolean isPossibleNeutralLoss(Map<String, Double> originalFormulaMap, IMolecularFormula neutralLossFormula) throws IOException
	{
		boolean isPossible = false;
		Map<String, Double> neutralLossFormulaMap = parseFormula(neutralLossFormula);
		
		for (String element : neutralLossFormulaMap.keySet()) {
			if(originalFormulaMap.containsKey(element))
			{
				double massElementOrig = originalFormulaMap.get(element);
				double massNeutralLoss = neutralLossFormulaMap.get(element);
				if((massElementOrig - massNeutralLoss) < 0)
				{
					isPossible = false;
					break;
				}
				else
					//neutral loss is possible with this formula
					isPossible = true;		
			}
			//element not contained in candidate fragment
			else
				break;
		}
		
		
		return isPossible;
	}
}
