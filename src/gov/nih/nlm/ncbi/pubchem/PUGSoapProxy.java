package gov.nih.nlm.ncbi.pubchem;

public class PUGSoapProxy implements gov.nih.nlm.ncbi.pubchem.PUGSoap {
  private String _endpoint = null;
  private gov.nih.nlm.ncbi.pubchem.PUGSoap pUGSoap = null;
  
  public PUGSoapProxy() {
    _initPUGSoapProxy();
  }
  
  public PUGSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initPUGSoapProxy();
  }
  
  private void _initPUGSoapProxy() {
    try {
      pUGSoap = (new gov.nih.nlm.ncbi.pubchem.PUGLocator()).getPUGSoap();
      if (pUGSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)pUGSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)pUGSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (pUGSoap != null)
      ((javax.xml.rpc.Stub)pUGSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public gov.nih.nlm.ncbi.pubchem.PUGSoap getPUGSoap() {
    if (pUGSoap == null)
      _initPUGSoapProxy();
    return pUGSoap;
  }
  
  public java.lang.String assayDownload(java.lang.String assayKey, gov.nih.nlm.ncbi.pubchem.AssayFormatType assayFormat, gov.nih.nlm.ncbi.pubchem.CompressType eCompress) throws java.rmi.RemoteException{
    if (pUGSoap == null)
      _initPUGSoapProxy();
    return pUGSoap.assayDownload(assayKey, assayFormat, eCompress);
  }
  
  public java.lang.String download(java.lang.String listKey, gov.nih.nlm.ncbi.pubchem.FormatType eFormat, gov.nih.nlm.ncbi.pubchem.CompressType eCompress, java.lang.Boolean use3D) throws java.rmi.RemoteException{
    if (pUGSoap == null)
      _initPUGSoapProxy();
    return pUGSoap.download(listKey, eFormat, eCompress, use3D);
  }
  
  public gov.nih.nlm.ncbi.pubchem.ColumnDescriptionType getAssayColumnDescription(int AID, gov.nih.nlm.ncbi.pubchem.HeadingType heading, java.lang.Integer TID) throws java.rmi.RemoteException{
    if (pUGSoap == null)
      _initPUGSoapProxy();
    return pUGSoap.getAssayColumnDescription(AID, heading, TID);
  }
  
  public gov.nih.nlm.ncbi.pubchem.ColumnDescriptionType[] getAssayColumnDescriptions(int AID) throws java.rmi.RemoteException{
    if (pUGSoap == null)
      _initPUGSoapProxy();
    return pUGSoap.getAssayColumnDescriptions(AID);
  }
  
  public gov.nih.nlm.ncbi.pubchem.AssayDescriptionType getAssayDescription(int AID) throws java.rmi.RemoteException{
    if (pUGSoap == null)
      _initPUGSoapProxy();
    return pUGSoap.getAssayDescription(AID);
  }
  
  public java.lang.String getDownloadUrl(java.lang.String downloadKey) throws java.rmi.RemoteException{
    if (pUGSoap == null)
      _initPUGSoapProxy();
    return pUGSoap.getDownloadUrl(downloadKey);
  }
  
  public gov.nih.nlm.ncbi.pubchem.EntrezKey getEntrezKey(java.lang.String listKey) throws java.rmi.RemoteException{
    if (pUGSoap == null)
      _initPUGSoapProxy();
    return pUGSoap.getEntrezKey(listKey);
  }
  
  public java.lang.String getEntrezUrl(gov.nih.nlm.ncbi.pubchem.EntrezKey entrezKey) throws java.rmi.RemoteException{
    if (pUGSoap == null)
      _initPUGSoapProxy();
    return pUGSoap.getEntrezUrl(entrezKey);
  }
  
  public int[] getIDList(java.lang.String listKey) throws java.rmi.RemoteException{
    if (pUGSoap == null)
      _initPUGSoapProxy();
    return pUGSoap.getIDList(listKey);
  }
  
  public int getListItemsCount(java.lang.String listKey) throws java.rmi.RemoteException{
    if (pUGSoap == null)
      _initPUGSoapProxy();
    return pUGSoap.getListItemsCount(listKey);
  }
  
  public gov.nih.nlm.ncbi.pubchem.StatusType getOperationStatus(java.lang.String anyKey) throws java.rmi.RemoteException{
    if (pUGSoap == null)
      _initPUGSoapProxy();
    return pUGSoap.getOperationStatus(anyKey);
  }
  
  public java.lang.Integer getStandardizedCID(java.lang.String strKey) throws java.rmi.RemoteException{
    if (pUGSoap == null)
      _initPUGSoapProxy();
    return pUGSoap.getStandardizedCID(strKey);
  }
  
  public java.lang.String getStandardizedStructure(java.lang.String strKey, gov.nih.nlm.ncbi.pubchem.FormatType format) throws java.rmi.RemoteException{
    if (pUGSoap == null)
      _initPUGSoapProxy();
    return pUGSoap.getStandardizedStructure(strKey, format);
  }
  
  public byte[] getStandardizedStructureBase64(java.lang.String strKey, gov.nih.nlm.ncbi.pubchem.FormatType format) throws java.rmi.RemoteException{
    if (pUGSoap == null)
      _initPUGSoapProxy();
    return pUGSoap.getStandardizedStructureBase64(strKey, format);
  }
  
  public java.lang.String getStatusMessage(java.lang.String anyKey) throws java.rmi.RemoteException{
    if (pUGSoap == null)
      _initPUGSoapProxy();
    return pUGSoap.getStatusMessage(anyKey);
  }
  
  public java.lang.String identitySearch(java.lang.String strKey, gov.nih.nlm.ncbi.pubchem.IdentitySearchOptions idOptions, gov.nih.nlm.ncbi.pubchem.LimitsType limits) throws java.rmi.RemoteException{
    if (pUGSoap == null)
      _initPUGSoapProxy();
    return pUGSoap.identitySearch(strKey, idOptions, limits);
  }
  
  public java.lang.String inputAssay(int AID, gov.nih.nlm.ncbi.pubchem.AssayColumnsType columns, java.lang.String listKeyTIDs, java.lang.String listKeySCIDs) throws java.rmi.RemoteException{
    if (pUGSoap == null)
      _initPUGSoapProxy();
    return pUGSoap.inputAssay(AID, columns, listKeyTIDs, listKeySCIDs);
  }
  
  public java.lang.String inputEntrez(gov.nih.nlm.ncbi.pubchem.EntrezKey entrezKey) throws java.rmi.RemoteException{
    if (pUGSoap == null)
      _initPUGSoapProxy();
    return pUGSoap.inputEntrez(entrezKey);
  }
  
  public java.lang.String inputList(int[] ids, gov.nih.nlm.ncbi.pubchem.PCIDType idType) throws java.rmi.RemoteException{
    if (pUGSoap == null)
      _initPUGSoapProxy();
    return pUGSoap.inputList(ids, idType);
  }
  
  public java.lang.String inputListText(java.lang.String ids, gov.nih.nlm.ncbi.pubchem.PCIDType idType) throws java.rmi.RemoteException{
    if (pUGSoap == null)
      _initPUGSoapProxy();
    return pUGSoap.inputListText(ids, idType);
  }
  
  public java.lang.String inputStructure(java.lang.String structure, gov.nih.nlm.ncbi.pubchem.FormatType format) throws java.rmi.RemoteException{
    if (pUGSoap == null)
      _initPUGSoapProxy();
    return pUGSoap.inputStructure(structure, format);
  }
  
  public java.lang.String inputStructureBase64(byte[] structure, gov.nih.nlm.ncbi.pubchem.FormatType format) throws java.rmi.RemoteException{
    if (pUGSoap == null)
      _initPUGSoapProxy();
    return pUGSoap.inputStructureBase64(structure, format);
  }
  
  public java.lang.String MFSearch(java.lang.String MF, gov.nih.nlm.ncbi.pubchem.MFSearchOptions mfOptions, gov.nih.nlm.ncbi.pubchem.LimitsType limits) throws java.rmi.RemoteException{
    if (pUGSoap == null)
      _initPUGSoapProxy();
    return pUGSoap.MFSearch(MF, mfOptions, limits);
  }
  
  public java.lang.String scoreMatrix(java.lang.String listKey, java.lang.String secondaryListKey, gov.nih.nlm.ncbi.pubchem.ScoreTypeType scoreType, gov.nih.nlm.ncbi.pubchem.MatrixFormatType matrixFormat, gov.nih.nlm.ncbi.pubchem.CompressType eCompress) throws java.rmi.RemoteException{
    if (pUGSoap == null)
      _initPUGSoapProxy();
    return pUGSoap.scoreMatrix(listKey, secondaryListKey, scoreType, matrixFormat, eCompress);
  }
  
  public java.lang.String similaritySearch2D(java.lang.String strKey, gov.nih.nlm.ncbi.pubchem.SimilaritySearchOptions simOptions, gov.nih.nlm.ncbi.pubchem.LimitsType limits) throws java.rmi.RemoteException{
    if (pUGSoap == null)
      _initPUGSoapProxy();
    return pUGSoap.similaritySearch2D(strKey, simOptions, limits);
  }
  
  public java.lang.String standardize(java.lang.String strKey) throws java.rmi.RemoteException{
    if (pUGSoap == null)
      _initPUGSoapProxy();
    return pUGSoap.standardize(strKey);
  }
  
  public java.lang.String substructureSearch(java.lang.String strKey, gov.nih.nlm.ncbi.pubchem.StructureSearchOptions ssOptions, gov.nih.nlm.ncbi.pubchem.LimitsType limits) throws java.rmi.RemoteException{
    if (pUGSoap == null)
      _initPUGSoapProxy();
    return pUGSoap.substructureSearch(strKey, ssOptions, limits);
  }
  
  public java.lang.String superstructureSearch(java.lang.String strKey, gov.nih.nlm.ncbi.pubchem.StructureSearchOptions ssOptions, gov.nih.nlm.ncbi.pubchem.LimitsType limits) throws java.rmi.RemoteException{
    if (pUGSoap == null)
      _initPUGSoapProxy();
    return pUGSoap.superstructureSearch(strKey, ssOptions, limits);
  }
  
  
}