package de.ipbhalle.metfrag.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.aromaticity.CDKHueckelAromaticityDetector;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.formula.MolecularFormula;
import org.openscience.cdk.inchi.InChIGenerator;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecularFormula;
import org.openscience.cdk.io.IChemObjectReaderErrorHandler;
import org.openscience.cdk.io.MDLReader;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.io.inchi.INChIContentProcessorTool;
import org.openscience.cdk.io.iterator.IteratingMDLReader;
import org.openscience.cdk.nonotify.NoNotificationChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.ChemFileManipulator;
import org.openscience.cdk.tools.manipulator.MolecularFormulaManipulator;
import org.postgresql.util.PSQLException;

import de.ipbhalle.metfrag.main.Config;

public class ChebiToDatabase {	
	
	public void run(File file)
	{		
		Connection con = null;
		
		try {
			
			if(con == null || con.isClosed())
			{
				String driver = "org.postgresql.Driver"; 
				Class.forName(driver);
				DriverManager.registerDriver(new org.postgresql.Driver()); 
		        //database data
		        Config c = new Config("outside");
		        String url = c.getJdbcPostgres();
		        String username = c.getUsernamePostgres();
		        String password = c.getPasswordPostgres();
		        con = DriverManager.getConnection(url, username, password);
			    con.setAutoCommit(false);
			}    
			
			
			System.out.println("Processing SDF: " + file.toString());
						
			IteratingMDLReader reader = new IteratingMDLReader(new FileInputStream(file), DefaultChemObjectBuilder.getInstance());
			
			while (reader.hasNext()) {
				IAtomContainer molecule = (IAtomContainer)reader.next();
			    Map<Object, Object> properties = molecule.getProperties();
			    
			    //RECORD data
			    String chebiID = (String)properties.get("ChEBI ID");
			    System.out.println("Processing: " + chebiID);
			    String molecularFormula = "";
			    double exactMass = 0.0;
			    String smiles = "";
			    String inchi = null;
			    String inchiKeyArray[] = null;
			    try
			    {
			    	molecularFormula = (String)properties.get("Formulae");
			    	smiles = (String)properties.get("SMILES");
			    	
			    	if(smiles.contains("*") || molecularFormula.contains("("))
			    		continue;
			    	
				    inchi = (String)properties.get("InChI");
				    String inchiKey = (String)properties.get("InChIKey");
				    inchiKey = inchiKey.split("=")[1];
				    //this should split in 3 parts
				    inchiKeyArray = inchiKey.split("-");
				    if(molecularFormula != null && !molecularFormula.equals(""))
				    {
				    	IMolecularFormula mf = new MolecularFormula();
					    exactMass = MolecularFormulaManipulator.getTotalExactMass(MolecularFormulaManipulator.getMolecularFormula(molecularFormula, mf));
				    }
				    else
				    {
				    	SmilesParser sp = new SmilesParser(NoNotificationChemObjectBuilder.getInstance());
				    	IAtomContainer molRead = sp.parseSmiles(smiles);
				    	CDKHueckelAromaticityDetector.detectAromaticity(molRead);				        
						AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molRead);
						
				        CDKHydrogenAdder hAdder = CDKHydrogenAdder.getInstance(molRead.getBuilder());
				        hAdder.addImplicitHydrogens(molRead);
				        AtomContainerManipulator.convertImplicitToExplicitHydrogens(molRead);
				        exactMass = MolecularFormulaManipulator.getTotalExactMass(MolecularFormulaManipulator.getMolecularFormula(molRead));
				    }
			    }
			    catch(Exception e)
			    {
			    	try
			    	{
				    	CDKHueckelAromaticityDetector.detectAromaticity(molecule);				        
						AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);
						
				        CDKHydrogenAdder hAdder = CDKHydrogenAdder.getInstance(molecule.getBuilder());
				        hAdder.addImplicitHydrogens(molecule);
				        AtomContainerManipulator.convertImplicitToExplicitHydrogens(molecule);
				        
				    	SmilesGenerator sg = new SmilesGenerator();
				    	smiles = sg.createSMILES(molecule);
				    	
				    	IMolecularFormula formula = MolecularFormulaManipulator.getMolecularFormula(molecule);
				    	molecularFormula = MolecularFormulaManipulator.getString(formula, false);
				    	
				    	//skip entries with R
				    	if(molecularFormula.contains("R"))
				    		continue;
				    	
				    	// Get InChIGenerator
				    	InChIGenerator gen = InChIGeneratorFactory.getInstance().getInChIGenerator(molecule);
				    	inchi = gen.getInchi();
				    	inchiKeyArray = gen.getInchiKey().split("-");
				    	
				        exactMass = MolecularFormulaManipulator.getTotalExactMass(formula);
			    	}
			    	catch(Exception e1)
			    	{
			    		System.err.println("CDK problem" + e.getMessage());
			    		e.printStackTrace();
			    		continue;
			    	}
			    }
			    
			    
			    
			    
			    
			    
			    //first check if the compound already exists (do not insert the same compound from different databases in the compound table)
			    PreparedStatement pstmtCheck = con.prepareStatement("SELECT compound_id from compound where inchi_key_1 = ? and inchi_key_2 = ?");
		        pstmtCheck.setString(1, inchiKeyArray[0]);
		        pstmtCheck.setString(2, inchiKeyArray[1]);
		        ResultSet res = pstmtCheck.executeQuery();
		        Integer compoundID = null;
//		        Integer compoundID = 1;
		        while(res.next()){
		        	compoundID = res.getInt(1);
		        }
		        pstmtCheck.close();
		        
		        ResultSet rs = null;
		        //no previously inserted compound matches
		        if(compoundID == null)
		        {
		        	Statement stmt = null;
		    	    stmt = con.createStatement();
		        	rs = stmt.executeQuery("SELECT nextval('compound_compound_id_seq')");
				    rs.next();
				    compoundID = rs.getInt(1);
				    
				    String savepointName = chebiID;
				    Savepoint savePoint = con.setSavepoint(savepointName);
				    PreparedStatement pstmtCompound = con.prepareStatement("INSERT INTO compound (compound_id, mol_structure, exact_mass, formula, smiles, inchi, inchi_key_1, inchi_key_2, inchi_key_3) VALUES (?,cast(? as molecule),?,?,?,?,?,?,?)");
				    pstmtCompound.setInt(1, compoundID);
			        pstmtCompound.setString(2, inchi);
			        pstmtCompound.setDouble(3, exactMass);
			        pstmtCompound.setString(4, molecularFormula);
			        pstmtCompound.setString(5, smiles);
			        pstmtCompound.setString(6, inchi);
			        pstmtCompound.setString(7, inchiKeyArray[0]);
			        pstmtCompound.setString(8, inchiKeyArray[1]);
			        pstmtCompound.setString(9, "");
//			        System.out.println(pstmtCompound.toString());
			        try
			        {
			        	pstmtCompound.executeUpdate();
			        }
			        catch(PSQLException e)
			        {
			        	pstmtCompound.cancel();
			        	pstmtCompound.close();
			        	con.rollback(savePoint);
			        	System.err.println("Postgres mol generation error...SKIPPED!");
			        	continue;
			        }
			        pstmtCompound.close();
		        }
		        else
		        {
		        	pstmtCheck = con.prepareStatement("SELECT accession from substance where compound_id = ?");
			        pstmtCheck.setInt(1, compoundID);
			        res = pstmtCheck.executeQuery();
			        boolean check = false;
			        while(res.next()){
			        	//already contained!
			        	if(res.getString(1).contains("CHEBI"))
			        	{
			        		System.out.println("Already Done!");
			        		check = true;
			        		break;
			        	}
			        }
			        pstmtCheck.close();
			        if(check)
			        	continue;
		        	System.out.println("Duplicate found!");
		        }
		        
		        
		        //now get the next substance_id
		        Statement stmt = null;
			    stmt = con.createStatement();
			    rs = stmt.executeQuery("SELECT nextval('substance_substance_id_seq')");
			    rs.next();
			    Integer substanceID = rs.getInt(1);
//		        Integer substanceID = 1;
		        
		        //insert the information also in substance table
		        
		        //pubchem has library id = 2
			    PreparedStatement pstmtSubstance = con.prepareStatement("INSERT INTO substance (substance_id, library_id, compound_id, accession) VALUES (?,?,?,?)");
			    pstmtSubstance.setInt(1, substanceID);
		        pstmtSubstance.setInt(2, 3);
		        pstmtSubstance.setInt(3, compoundID);
		        pstmtSubstance.setString(4, chebiID);
//		        System.out.println(pstmtSubstance.toString());
		        pstmtSubstance.executeUpdate();
		        pstmtSubstance.close();
		        
		        //now insert all strings into database
		        String iupacNames = (String)properties.get("IUPAC Names");
		        if(iupacNames != null && !iupacNames.equals(""))
		        {
		        	String[] iupacNameArray = iupacNames.split("\\n");
			        for (int i = 0; i < iupacNameArray.length; i++) {
						PreparedStatement pstmtName = con.prepareStatement("INSERT INTO name (substance_id, name) VALUES (?,?)");
				        pstmtName.setInt(1, substanceID);
				        pstmtName.setString(2, iupacNameArray[i]);
//				        System.out.println(pstmtName.toString());
				        pstmtName.executeUpdate();
				        pstmtName.close();
					}
		        }
		        
//		        String synonymes = (String)properties.get("Synonyms");
//		        String[] synonymesArray = synonymes.split("\\n");
//		        for (int i = 0; i < synonymesArray.length; i++) {
//					PreparedStatement pstmtName = con.prepareStatement("INSERT INTO name (substance_id, name) VALUES (?,?)");
//			        pstmtName.setInt(1, substanceID);
//			        pstmtName.setString(2, synonymesArray[i]);
////			        System.out.println(pstmtName.toString());
//			        pstmtName.executeUpdate();
//				}			    
			}
			
			con.commit();
		} catch (NumberFormatException e) {
			System.err.println("File: " + file.getName());
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.err.println("File: " + file.getName());
			e.printStackTrace();
		} catch (SQLException e) {
			System.err.println("File: " + file.getName());
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("File: " + file.getName());
			e.printStackTrace();
		}
		finally{
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	
	}
	
	public static void main(String[] args) {
		ChebiToDatabase chebi = new ChebiToDatabase();
		chebi.run(new File("/home/swolf/Downloads/ChEBI_complete_CLEAN.sdf"));
//		MDLReader reader;
//		List<IAtomContainer> containersList;
//		List<IAtomContainer> ret = new ArrayList<IAtomContainer>();
//		
//		File f = new File("/home/swolf/Downloads/ChEBI_complete_CLEAN.sdf");
//		
//		if(f.isFile())
//		{
//			try {
////				reader = new MDLReader(new FileReader(f));
////				reader.setErrorHandler(new ErrorHandler());
////				ChemFile chemFile = (ChemFile)reader.read((ChemObject)new ChemFile());
////		        containersList = ChemFileManipulator.getAllAtomContainers(chemFile);
////		        System.out.println("Containers count: " + containersList.size());
//		        
//		        IteratingMDLReader reader2 = new IteratingMDLReader(new FileInputStream(f), DefaultChemObjectBuilder.getInstance());
//		        reader2.setErrorHandler(new ErrorHandler());
//		        int count = 0;
//				while (reader2.hasNext()) {
//					IAtomContainer molecule = (IAtomContainer)reader2.next();
//					count++;
//					System.out.println(molecule.getProperty("ChEBI ID") + " - " + count);
//				}
//		        System.out.println("Containers iterating count: " + count);
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
////			catch (CDKException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
//	        
//	        
//		}
	}

}
