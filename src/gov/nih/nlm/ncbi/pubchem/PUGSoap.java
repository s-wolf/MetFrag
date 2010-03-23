/**
 * PUGSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.pubchem;

public interface PUGSoap extends java.rmi.Remote {

    /**
     * Given an assay key, prepare for download a file containing
     * an assay data table
     * 	in the selected format. See the assay query section of the PUG service
     * documentation (http://pubchem.ncbi.nlm.nih.gov/pug/pughelp.html)
     * 	for more detail on the supported formats. Compression is optional
     * and defaults to gzip (.gz). Returns a download key. Asynchronous.
     */
    public java.lang.String assayDownload(java.lang.String assayKey, gov.nih.nlm.ncbi.pubchem.AssayFormatType assayFormat, gov.nih.nlm.ncbi.pubchem.CompressType eCompress) throws java.rmi.RemoteException;

    /**
     * Given a list key, prepare for download a file
     *       containing those records in the selected format. See the web
     * download service documentation
     *       (http://pubchem.ncbi.nlm.nih.gov/pc_fetch/pc_fetch-help.html)
     * for more detail on the supported formats and file types.
     *       Returns a download key. Asynchronous.
     */
    public java.lang.String download(java.lang.String listKey, gov.nih.nlm.ncbi.pubchem.FormatType eFormat, gov.nih.nlm.ncbi.pubchem.CompressType eCompress, java.lang.Boolean use3D) throws java.rmi.RemoteException;

    /**
     * Get the description of column (readout) in a BioAssay,
     *       which may be the outcome, score, or a TID from the given AID.
     * Synchronous.
     */
    public gov.nih.nlm.ncbi.pubchem.ColumnDescriptionType getAssayColumnDescription(int AID, gov.nih.nlm.ncbi.pubchem.HeadingType heading, java.lang.Integer TID) throws java.rmi.RemoteException;

    /**
     * Get the description of all columns (readouts)
     *       in a BioAssay. Synchronous.
     */
    public gov.nih.nlm.ncbi.pubchem.ColumnDescriptionType[] getAssayColumnDescriptions(int AID) throws java.rmi.RemoteException;

    /**
     * Get the descriptive information for a BioAssay, including
     *       the number of user-specified readouts (TIDs) and whether a score
     * readout is present. Synchronous.
     */
    public gov.nih.nlm.ncbi.pubchem.AssayDescriptionType getAssayDescription(int AID) throws java.rmi.RemoteException;

    /**
     * Given a download key, return an FTP URL that may be used to
     * download the requested file. Synchronous.
     */
    public java.lang.String getDownloadUrl(java.lang.String downloadKey) throws java.rmi.RemoteException;

    /**
     * Given a list key, return an Entrez history key (db,
     *       query key, and WebEnv) corresponding to that list. Synchronous.
     */
    public gov.nih.nlm.ncbi.pubchem.EntrezKey getEntrezKey(java.lang.String listKey) throws java.rmi.RemoteException;

    /**
     * Given an Entrez history key (db, query key, and WebEnv),
     *       return an HTTP URL that may be used to view the list in Entrez.
     * Synchronous.
     */
    public java.lang.String getEntrezUrl(gov.nih.nlm.ncbi.pubchem.EntrezKey entrezKey) throws java.rmi.RemoteException;

    /**
     * Given a list key, return the identifiers as an array
     *       of integers. Synchronous.
     */
    public int[] getIDList(java.lang.String listKey) throws java.rmi.RemoteException;

    /**
     * Return the number of IDs in the set represented by the
     *       given list key. Synchronous.
     */
    public int getListItemsCount(java.lang.String listKey) throws java.rmi.RemoteException;

    /**
     * Given a key for any asynchronous operation, return
     *       the status of that operation. Possible return values are:  Success,
     * the operation completed normally; HitLimit,
     *       TimeLimit: the operation finished normally, but one of the limits
     * was reached (e.g. before the entire database
     *       was searched); ServerError, InputError, DataError, Stopped:
     * there was a problem with the input or on the server,
     *       and the job has died; Queued: the operation is waiting its turn
     * in the public queue; Running: the operation is
     *       in progress. Synchronous.
     */
    public gov.nih.nlm.ncbi.pubchem.StatusType getOperationStatus(java.lang.String anyKey) throws java.rmi.RemoteException;

    /**
     * Given a structure key that has been processed by
     *       Standardize, return the corresponding PubChem Compound database
     * CID, or an empty value if the structure
     *       is not present in PubChem. Synchronous.
     */
    public java.lang.Integer getStandardizedCID(java.lang.String strKey) throws java.rmi.RemoteException;

    /**
     * Given a structure key that has been processed by
     *       Standardize, return the chemical structure in as SMILES or InChI
     * strings. Synchronous.
     */
    public java.lang.String getStandardizedStructure(java.lang.String strKey, gov.nih.nlm.ncbi.pubchem.FormatType format) throws java.rmi.RemoteException;

    /**
     * Given a structure key that has been processed by
     *       Standardize, return the chemical structure as ASN, XML, or SDF,
     * returned as a Base64-encoded string.
     *       Synchronous.
     */
    public byte[] getStandardizedStructureBase64(java.lang.String strKey, gov.nih.nlm.ncbi.pubchem.FormatType format) throws java.rmi.RemoteException;

    /**
     * Given a key for any asynchronous operation,
     *       return any system messages (error messages, job info, etc.)
     * associated with the operation,
     *       if any. Synchronous.
     */
    public java.lang.String getStatusMessage(java.lang.String anyKey) throws java.rmi.RemoteException;

    /**
     * Search PubChem Compound for structures identical to
     *       the one given by the structure key input, based on a user-selected
     * level of chemical identity: connectivity only,
     *       match isotopes and/or stereo, etc. The search may be limited
     * by elapsed time or number of records found, or restricted
     *       to search only within a previous result set (given by a list
     * key). Returns a list key. Asynchronous.
     */
    public java.lang.String identitySearch(java.lang.String strKey, gov.nih.nlm.ncbi.pubchem.IdentitySearchOptions idOptions, gov.nih.nlm.ncbi.pubchem.LimitsType limits) throws java.rmi.RemoteException;

    /**
     * Specify an assay table from a BioAssay AID. The table may be
     * complete,
     * 	concise, or include a ListKey-specified set of readouts (TIDs). By
     * default, all tested substances are included, but can be restricted
     * to a
     * 	ListKey-specified set of SIDs or CIDs. Returns an assay key. Synchronous.
     */
    public java.lang.String inputAssay(int AID, gov.nih.nlm.ncbi.pubchem.AssayColumnsType columns, java.lang.String listKeyTIDs, java.lang.String listKeySCIDs) throws java.rmi.RemoteException;

    /**
     * Input an Entrez history key (db, query key, and WebEnv).
     *       Returns a list key. Synchronous.
     */
    public java.lang.String inputEntrez(gov.nih.nlm.ncbi.pubchem.EntrezKey entrezKey) throws java.rmi.RemoteException;

    /**
     * Input a set of identifiers for a PubChem database,
     *       as an array of integers. Returns a list key. Synchronous.
     */
    public java.lang.String inputList(int[] ids, gov.nih.nlm.ncbi.pubchem.PCIDType idType) throws java.rmi.RemoteException;

    /**
     * Input a set of identifiers for a PubChem database,
     *       as a simple string of integer values separated by commas and/or
     * whitespace. Returns a list key. Synchronous.
     */
    public java.lang.String inputListText(java.lang.String ids, gov.nih.nlm.ncbi.pubchem.PCIDType idType) throws java.rmi.RemoteException;

    /**
     * Input a chemical structure as a
     *       simple (one-line) string, either SMILES or InChI. Returns a
     * structure key. Synchronous.
     */
    public java.lang.String inputStructure(java.lang.String structure, gov.nih.nlm.ncbi.pubchem.FormatType format) throws java.rmi.RemoteException;

    /**
     * Input a chemical structure in ASN.1 (text or binary),
     *       XML, or SDF format. The structure must be encoded as a Base64
     * string. Currently only single structures
     *       are supported. Returns a structure key. Synchronous.
     */
    public java.lang.String inputStructureBase64(byte[] structure, gov.nih.nlm.ncbi.pubchem.FormatType format) throws java.rmi.RemoteException;

    /**
     * Search PubChem Compound for structures of a given
     *       molecular formula, optionally allowing elements not specified
     * to be present. The search may be limited by elapsed
     *       time or number of records found, or restricted to search only
     * within a previous result set (given by a list key).
     *       Returns a list key. Asynchronous.
     */
    public java.lang.String MFSearch(java.lang.String MF, gov.nih.nlm.ncbi.pubchem.MFSearchOptions mfOptions, gov.nih.nlm.ncbi.pubchem.LimitsType limits) throws java.rmi.RemoteException;

    /**
     * Compute a matrix of scores from one or two
     *       lists of IDs (if one, the IDs will be self-scored), of the selected
     * type and in the selected format.
     *       Compression is optional and defaults to gzip (.gz). Returns
     * a download key. Asynchronous.
     */
    public java.lang.String scoreMatrix(java.lang.String listKey, java.lang.String secondaryListKey, gov.nih.nlm.ncbi.pubchem.ScoreTypeType scoreType, gov.nih.nlm.ncbi.pubchem.MatrixFormatType matrixFormat, gov.nih.nlm.ncbi.pubchem.CompressType eCompress) throws java.rmi.RemoteException;

    /**
     * Search PubChem Compound for structures similar to
     *       the one given by the structure key input, based on the given
     * Tanimoto-based similarity score. The search may be
     *       limited by elapsed time or number of records found, or restricted
     * to search only within a previous result set (given
     *       by a list key). Returns a list key. Asynchronous.
     */
    public java.lang.String similaritySearch2D(java.lang.String strKey, gov.nih.nlm.ncbi.pubchem.SimilaritySearchOptions simOptions, gov.nih.nlm.ncbi.pubchem.LimitsType limits) throws java.rmi.RemoteException;

    /**
     * Standardize the structure given by the structure
     *       key input, using the same algorithm PubChem uses to construct
     * the Compound database.
     *       Returns a structure key. Asynchronous.
     */
    public java.lang.String standardize(java.lang.String strKey) throws java.rmi.RemoteException;

    /**
     * Search PubChem Compound for structures containing the
     *       one given by the structure key input, based on a user-selected
     * level of chemical identity: connectivity only, match
     *       isotopes and/or stereo, etc. The search may be limited by elapsed
     * time or number of records found, or restricted to
     *       search only within a previous result set (given by a list key).
     * Returns a list key. Asynchronous.
     */
    public java.lang.String substructureSearch(java.lang.String strKey, gov.nih.nlm.ncbi.pubchem.StructureSearchOptions ssOptions, gov.nih.nlm.ncbi.pubchem.LimitsType limits) throws java.rmi.RemoteException;

    /**
     * Search PubChem Compound for structures contained
     *       within the one given by the structure key input, based on a
     * user-selected level of chemical identity: connectivity
     *       only, match isotopes and/or stereo, etc. The search may be limited
     * by elapsed time or number of records found, or
     *       restricted to search only within a previous result set (given
     * by a list key). Returns a list key.
     *       Asynchronous.
     */
    public java.lang.String superstructureSearch(java.lang.String strKey, gov.nih.nlm.ncbi.pubchem.StructureSearchOptions ssOptions, gov.nih.nlm.ncbi.pubchem.LimitsType limits) throws java.rmi.RemoteException;
}
