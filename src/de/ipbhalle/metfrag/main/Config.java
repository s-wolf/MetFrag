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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * The Class Config.
 */
public class Config {
	
	private String jdbc = "";
    private String username = "";
    private String password = "";
    private String jdbcPostgres = "";
    private String usernamePostgres = "";
    private String passwordPostgres = "";
    private String file = "";
    private String folder = "";
    private int treeDepth = 0;
    private String keggPath = "";
    private boolean pdf = false;
    private boolean showDiagrams = false;
    private boolean folderRead = false;
    private boolean hierarchical = false;
    private boolean recreateFrags = false;
    private boolean createTree = false;
    private boolean breakAromaticRings = false;
    private boolean sumFormulaRedundancyCheck = false;
    private String comment = "";
    private String database = "";
    private Properties properties = null;
    private double mzabs = 0.0;
    private double mzppm = 0.0;
    private int searchPPM = 10;
    private boolean hydrogenTest = false;
    private int threads = 1;
    private boolean neutralLossAdd = false;
    private boolean bondEnergyScoring = false;
    private boolean isOnlyBreakSelectedBonds = false;
    private int maximumNeutralLossCombination = 3;
    private String chemspiderToken = "";
	
	/**
	 * Instantiates a new config.
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Config() throws IOException
	{
		//load the property file (Settings.properties) from java classpath
		properties = getConfig();
		readConfig();
	}
	
	public Config(String test) throws IOException
	{
		//load the property file (Settings.properties) from java classpath
		properties = getConfigOutside();
		readConfig();
	}
	
	/**
	 * Gets the config.
	 * 
	 * @return the config
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Properties getConfig() throws IOException
	{
		Properties properties = new Properties();
//		File dir1 = new File (".");
//		String path = dir1.getCanonicalPath() + "/conf/Settings.properties";
//		System.out.println("Pfad: " + path);
//		properties.load(new FileInputStream(new File(path)));
	    
		//original code
		URL url = ClassLoader.getSystemResource("Settings.properties");
	    properties.load(url.openStream());

		return properties;
	}
	
	
	/**
	 * Gets the config outside. The path has to be passed like:
	 * java -Dproperty.file.path=/home/swolf/FragSearchTest/KEGG/Settings.properties -Xms256m -Xmx768m -jar....
	 * 
	 * @return the config outside
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Properties getConfigOutside() throws IOException
	{
		Properties properties = new Properties();
		String file = System.getProperty("property.file.path");
		properties.load(new FileInputStream(new File(file + "Settings.properties")));

		return properties;
	}
	
	/**
	 * Read config.
	 */
	private void readConfig()
	{
		//************************************************************************************************
		//CONFIGURATION
		
		//JDBC URL --> MYSQL connection
		setJdbc(properties.getProperty("jdbc"));
		//Username
		setUsername(properties.getProperty("username"));
		//Password 
		setPassword(properties.getProperty("password"));
		
		
		//JDBC URL --> Postgres connection
		setJdbcPostgres(properties.getProperty("jdbcPostgres"));
		//Username
		setUsernamePostgres(properties.getProperty("usernamePostgres"));
		//Password 
		setPasswordPostgres(properties.getProperty("passwordPostgres"));
		
		//Set folder and file to fragment...a new folder is created with the name of the file (in the folder given)
		setFile(properties.getProperty("file"));
		
		//folder where the .txt file is in
		setFolder(properties.getProperty("folder"));

		//mzabs and mzppm
		setMzabs(Double.parseDouble(properties.getProperty("mzabs")));
		setMzppm(Double.parseDouble(properties.getProperty("mzppm")));
		
		//searchppm
		setSearchPPM(Integer.parseInt(properties.getProperty("searchppm")));
		
		//Output fragments in pdf file
		setPdf(false); //create pdf output....uses a lot memory
		if(properties.getProperty("pdf") != null && properties.getProperty("pdf").equals("true"))
			setPdf(true);
		
		//Output fragments in pdf file
		setHierarchical(false); //create pdf output....uses a lot memory
		if(properties.getProperty("Hierarchical") != null && properties.getProperty("Hierarchical").equals("true"))
			setHierarchical(true);
		
		setShowDiagrams(false); //show graphical output?
		if(properties.getProperty("showDiagrams") != null && properties.getProperty("showDiagrams").equals("true"))
			setShowDiagrams(true);
		
		setFolderRead(false); //read complete folder
		if(properties.getProperty("folderRead") != null && properties.getProperty("folderRead").equals("true"))
			setFolderRead(true);
		
		
		setRecreateFrags(false);
		if(properties.getProperty("recreateFrags") != null && properties.getProperty("recreateFrags").equals("true"))
			setRecreateFrags(true);
		
		setCreateTree(false);
		if(properties.getProperty("createTree") != null && properties.getProperty("createTree").equals("true"))
			setCreateTree(true);
		
		setBreakAromaticRings(false);
		if(properties.getProperty("breakAromaticRings") != null && properties.getProperty("breakAromaticRings").equals("true"))
			setBreakAromaticRings(true);
		
		setSumFormulaRedundancyCheck(false);
		if(properties.getProperty("sumFormulaRedundancyCheck") != null && properties.getProperty("sumFormulaRedundancyCheck").equals("true"))
			setSumFormulaRedundancyCheck(true);
		
		
		setHydrogenTest(false);
		if(properties.getProperty("hydrogenTest") != null && properties.getProperty("hydrogenTest").equals("true"))
			setHydrogenTest(true);
		
		setTreeDepth(Integer.parseInt(properties.getProperty("treeDepth")));
		setKeggPath(properties.getProperty("keggPath"));
		setComment(properties.getProperty("comment"));
		setThreads(Integer.parseInt(properties.getProperty("threads")));
		setDatabase(properties.getProperty("database"));
		
		if(properties.getProperty("neutralLossAdd") != null && properties.getProperty("neutralLossAdd").equals("true"))
			setNeutralLossAdd(true);
		
		if(properties.getProperty("bondEnergyScoring") != null && properties.getProperty("bondEnergyScoring").equals("true"))
			setBondEnergyScoring(true);
		
		if(properties.getProperty("breakOnlySelectedBonds") != null && properties.getProperty("breakOnlySelectedBonds").equals("true"))
			setOnlyBreakSelectedBonds(isOnlyBreakSelectedBonds);
		
		setChemspiderToken(properties.getProperty("chemspiderToken"));
		
		setMaximumNeutralLossCombination(Integer.parseInt(properties.getProperty("maximumNeutralLossCombination")));
	}

	/**
	 * Sets the jdbc.
	 * 
	 * @param jdbc the new jdbc
	 */
	public void setJdbc(String jdbc) {
		this.jdbc = jdbc;
	}

	/**
	 * Gets the jdbc.
	 * 
	 * @return the jdbc
	 */
	public String getJdbc() {
		return jdbc;
	}

	/**
	 * Sets the username.
	 * 
	 * @param username the new username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets the username.
	 * 
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the password.
	 * 
	 * @param password the new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the password.
	 * 
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the file.
	 * 
	 * @param file the new file
	 */
	public void setFile(String file) {
		this.file = file;
	}

	/**
	 * Gets the file.
	 * 
	 * @return the file
	 */
	public String getFile() {
		return file;
	}

	/**
	 * Sets the folder.
	 * 
	 * @param folder the new folder
	 */
	public void setFolder(String folder) {
		this.folder = folder;
	}

	/**
	 * Gets the folder.
	 * 
	 * @return the folder
	 */
	public String getFolder() {
		return folder;
	}

	/**
	 * Sets the tree depth.
	 * 
	 * @param treeDepth the new tree depth
	 */
	public void setTreeDepth(int treeDepth) {
		this.treeDepth = treeDepth;
	}

	/**
	 * Gets the tree depth.
	 * 
	 * @return the tree depth
	 */
	public int getTreeDepth() {
		return treeDepth;
	}

	/**
	 * Sets the kegg path.
	 * 
	 * @param keggPath the new kegg path
	 */
	public void setKeggPath(String keggPath) {
		this.keggPath = keggPath;
	}

	/**
	 * Gets the kegg path.
	 * 
	 * @return the kegg path
	 */
	public String getKeggPath() {
		return keggPath;
	}

	/**
	 * Sets the pdf.
	 * 
	 * @param pdf the new pdf
	 */
	public void setPdf(boolean pdf) {
		this.pdf = pdf;
	}

	/**
	 * Checks if is pdf.
	 * 
	 * @return true, if is pdf
	 */
	public boolean isPdf() {
		return pdf;
	}

	/**
	 * Sets the show diagrams.
	 * 
	 * @param showDiagrams the new show diagrams
	 */
	public void setShowDiagrams(boolean showDiagrams) {
		this.showDiagrams = showDiagrams;
	}

	/**
	 * Checks if is show diagrams.
	 * 
	 * @return true, if is show diagrams
	 */
	public boolean isShowDiagrams() {
		return showDiagrams;
	}

	/**
	 * Sets the folder read.
	 * 
	 * @param folderRead the new folder read
	 */
	public void setFolderRead(boolean folderRead) {
		this.folderRead = folderRead;
	}

	/**
	 * Checks if is folder read.
	 * 
	 * @return true, if is folder read
	 */
	public boolean isFolderRead() {
		return folderRead;
	}

	/**
	 * Sets the hierarchical.
	 * 
	 * @param hierarchical the new hierarchical
	 */
	public void setHierarchical(boolean hierarchical) {
		this.hierarchical = hierarchical;
	}

	/**
	 * Checks if is hierarchical.
	 * 
	 * @return true, if is hierarchical
	 */
	public boolean isHierarchical() {
		return hierarchical;
	}


	/**
	 * Sets the recreate frags.
	 * 
	 * @param recreateFrags the new recreate frags
	 */
	public void setRecreateFrags(boolean recreateFrags) {
		this.recreateFrags = recreateFrags;
	}

	/**
	 * Checks if is recreate frags.
	 * 
	 * @return true, if is recreate frags
	 */
	public boolean isRecreateFrags() {
		return recreateFrags;
	}

	/**
	 * Sets the creates the tree.
	 * 
	 * @param createTree the new creates the tree
	 */
	public void setCreateTree(boolean createTree) {
		this.createTree = createTree;
	}

	/**
	 * Checks if is creates the tree.
	 * 
	 * @return true, if is creates the tree
	 */
	public boolean isCreateTree() {
		return createTree;
	}

	/**
	 * Sets the break aromatic rings.
	 * 
	 * @param breakAromaticRings the new break aromatic rings
	 */
	public void setBreakAromaticRings(boolean breakAromaticRings) {
		this.breakAromaticRings = breakAromaticRings;
	}

	/**
	 * Checks if is break aromatic rings.
	 * 
	 * @return true, if is break aromatic rings
	 */
	public boolean isBreakAromaticRings() {
		return breakAromaticRings;
	}

	/**
	 * Sets the sum formula redundancy check.
	 * 
	 * @param sumFormulaRedundancyCheck the new sum formula redundancy check
	 */
	public void setSumFormulaRedundancyCheck(boolean sumFormulaRedundancyCheck) {
		this.sumFormulaRedundancyCheck = sumFormulaRedundancyCheck;
	}

	/**
	 * Checks if is sum formula redundancy check.
	 * 
	 * @return true, if is sum formula redundancy check
	 */
	public boolean isSumFormulaRedundancyCheck() {
		return sumFormulaRedundancyCheck;
	}

	/**
	 * Sets the mzabs.
	 * 
	 * @param mzabs the new mzabs
	 */
	public void setMzabs(double mzabs) {
		this.mzabs = mzabs;
	}

	/**
	 * Gets the mzabs.
	 * 
	 * @return the mzabs
	 */
	public double getMzabs() {
		return mzabs;
	}

	/**
	 * Sets the mzppm.
	 * 
	 * @param mzppm the new mzppm
	 */
	public void setMzppm(double mzppm) {
		this.mzppm = mzppm;
	}

	/**
	 * Gets the mzppm.
	 * 
	 * @return the mzppm
	 */
	public double getMzppm() {
		return mzppm;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getComment() {
		return comment;
	}

	public void setSearchPPM(int searchPPM) {
		this.searchPPM = searchPPM;
	}

	public int getSearchPPM() {
		return searchPPM;
	}

	public void setHydrogenTest(boolean hydrogenTest) {
		this.hydrogenTest = hydrogenTest;
	}

	public boolean isHydrogenTest() {
		return hydrogenTest;
	}

	public void setThreads(int threads) {
		this.threads = threads;
	}

	public int isThreads() {
		return threads;
	}

	public void setNeutralLossAdd(boolean neutralLossAdd) {
		this.neutralLossAdd = neutralLossAdd;
	}

	public boolean isNeutralLossAdd() {
		return neutralLossAdd;
	}

	public void setBondEnergyScoring(boolean bondEnergyScoring) {
		this.bondEnergyScoring = bondEnergyScoring;
	}

	public boolean isBondEnergyScoring() {
		return bondEnergyScoring;
	}

	public void setOnlyBreakSelectedBonds(boolean isOnlyBreakSelectedBonds) {
		this.isOnlyBreakSelectedBonds = isOnlyBreakSelectedBonds;
	}

	public boolean isOnlyBreakSelectedBonds() {
		return isOnlyBreakSelectedBonds;
	}

	public void setPasswordPostgres(String passwordPostgres) {
		this.passwordPostgres = passwordPostgres;
	}

	public String getPasswordPostgres() {
		return passwordPostgres;
	}

	public void setUsernamePostgres(String usernamePostgres) {
		this.usernamePostgres = usernamePostgres;
	}

	public String getUsernamePostgres() {
		return usernamePostgres;
	}

	public void setJdbcPostgres(String jdbcPostgres) {
		this.jdbcPostgres = jdbcPostgres;
	}

	public String getJdbcPostgres() {
		return jdbcPostgres;
	}

	public void setMaximumNeutralLossCombination(
			int maximumNeutralLossCombination) {
		this.maximumNeutralLossCombination = maximumNeutralLossCombination;
	}

	public int getMaximumNeutralLossCombination() {
		return maximumNeutralLossCombination;
	}

	public void setChemspiderToken(String chemspiderToken) {
		this.chemspiderToken = chemspiderToken;
	}

	/**
	 * Gets the chemspider token used for the webservice interface
	 *
	 * @return the chemspider token
	 */
	public String getChemspiderToken() {
		return chemspiderToken;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getDatabase() {
		return database;
	}

}
