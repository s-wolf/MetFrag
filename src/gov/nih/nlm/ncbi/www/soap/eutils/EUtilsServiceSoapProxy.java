package gov.nih.nlm.ncbi.www.soap.eutils;

public class EUtilsServiceSoapProxy implements gov.nih.nlm.ncbi.www.soap.eutils.EUtilsServiceSoap {
  private String _endpoint = null;
  private gov.nih.nlm.ncbi.www.soap.eutils.EUtilsServiceSoap eUtilsServiceSoap = null;
  
  public EUtilsServiceSoapProxy() {
    _initEUtilsServiceSoapProxy();
  }
  
  public EUtilsServiceSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initEUtilsServiceSoapProxy();
  }
  
  private void _initEUtilsServiceSoapProxy() {
    try {
      eUtilsServiceSoap = (new gov.nih.nlm.ncbi.www.soap.eutils.EUtilsServiceLocator()).geteUtilsServiceSoap();
      if (eUtilsServiceSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)eUtilsServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)eUtilsServiceSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (eUtilsServiceSoap != null)
      ((javax.xml.rpc.Stub)eUtilsServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public gov.nih.nlm.ncbi.www.soap.eutils.EUtilsServiceSoap getEUtilsServiceSoap() {
    if (eUtilsServiceSoap == null)
      _initEUtilsServiceSoapProxy();
    return eUtilsServiceSoap;
  }
  
  public gov.nih.nlm.ncbi.www.soap.eutils.egquery.Result run_eGquery(gov.nih.nlm.ncbi.www.soap.eutils.egquery.EGqueryRequest request) throws java.rmi.RemoteException{
    if (eUtilsServiceSoap == null)
      _initEUtilsServiceSoapProxy();
    return eUtilsServiceSoap.run_eGquery(request);
  }
  
  public gov.nih.nlm.ncbi.www.soap.eutils.einfo.EInfoResult run_eInfo(gov.nih.nlm.ncbi.www.soap.eutils.einfo.EInfoRequest request) throws java.rmi.RemoteException{
    if (eUtilsServiceSoap == null)
      _initEUtilsServiceSoapProxy();
    return eUtilsServiceSoap.run_eInfo(request);
  }
  
  public gov.nih.nlm.ncbi.www.soap.eutils.esearch.ESearchResult run_eSearch(gov.nih.nlm.ncbi.www.soap.eutils.esearch.ESearchRequest request) throws java.rmi.RemoteException{
    if (eUtilsServiceSoap == null)
      _initEUtilsServiceSoapProxy();
    return eUtilsServiceSoap.run_eSearch(request);
  }
  
  public gov.nih.nlm.ncbi.www.soap.eutils.esummary.ESummaryResult run_eSummary(gov.nih.nlm.ncbi.www.soap.eutils.esummary.ESummaryRequest request) throws java.rmi.RemoteException{
    if (eUtilsServiceSoap == null)
      _initEUtilsServiceSoapProxy();
    return eUtilsServiceSoap.run_eSummary(request);
  }
  
  public gov.nih.nlm.ncbi.www.soap.eutils.elink.ELinkResult run_eLink(gov.nih.nlm.ncbi.www.soap.eutils.elink.ELinkRequest request) throws java.rmi.RemoteException{
    if (eUtilsServiceSoap == null)
      _initEUtilsServiceSoapProxy();
    return eUtilsServiceSoap.run_eLink(request);
  }
  
  public gov.nih.nlm.ncbi.www.soap.eutils.espell.ESpellResult run_eSpell(gov.nih.nlm.ncbi.www.soap.eutils.espell.ESpellRequest request) throws java.rmi.RemoteException{
    if (eUtilsServiceSoap == null)
      _initEUtilsServiceSoapProxy();
    return eUtilsServiceSoap.run_eSpell(request);
  }
  
  public gov.nih.nlm.ncbi.www.soap.eutils.epost.EPostResult run_ePost(gov.nih.nlm.ncbi.www.soap.eutils.epost.EPostRequest request) throws java.rmi.RemoteException{
    if (eUtilsServiceSoap == null)
      _initEUtilsServiceSoapProxy();
    return eUtilsServiceSoap.run_ePost(request);
  }
  
  public gov.nih.nlm.ncbi.www.soap.eutils.egquery.Result run_eGquery_MS(java.lang.String term, java.lang.String tool, java.lang.String email) throws java.rmi.RemoteException{
    if (eUtilsServiceSoap == null)
      _initEUtilsServiceSoapProxy();
    return eUtilsServiceSoap.run_eGquery_MS(term, tool, email);
  }
  
  public gov.nih.nlm.ncbi.www.soap.eutils.einfo.EInfoResult run_eInfo_MS(java.lang.String db, java.lang.String tool, java.lang.String email) throws java.rmi.RemoteException{
    if (eUtilsServiceSoap == null)
      _initEUtilsServiceSoapProxy();
    return eUtilsServiceSoap.run_eInfo_MS(db, tool, email);
  }
  
  public gov.nih.nlm.ncbi.www.soap.eutils.esearch.ESearchResult run_eSearch_MS(java.lang.String db, java.lang.String term, java.lang.String webEnv, java.lang.String queryKey, java.lang.String usehistory, java.lang.String tool, java.lang.String email, java.lang.String field, java.lang.String reldate, java.lang.String mindate, java.lang.String maxdate, java.lang.String datetype, java.lang.String retStart, java.lang.String retMax, java.lang.String rettype, java.lang.String sort) throws java.rmi.RemoteException{
    if (eUtilsServiceSoap == null)
      _initEUtilsServiceSoapProxy();
    return eUtilsServiceSoap.run_eSearch_MS(db, term, webEnv, queryKey, usehistory, tool, email, field, reldate, mindate, maxdate, datetype, retStart, retMax, rettype, sort);
  }
  
  public gov.nih.nlm.ncbi.www.soap.eutils.esummary.ESummaryResult run_eSummary_MS(java.lang.String db, java.lang.String id, java.lang.String webEnv, java.lang.String query_key, java.lang.String retstart, java.lang.String retmax, java.lang.String tool, java.lang.String email) throws java.rmi.RemoteException{
    if (eUtilsServiceSoap == null)
      _initEUtilsServiceSoapProxy();
    return eUtilsServiceSoap.run_eSummary_MS(db, id, webEnv, query_key, retstart, retmax, tool, email);
  }
  
  public gov.nih.nlm.ncbi.www.soap.eutils.elink.ELinkResult run_eLink_MS(java.lang.String db, java.lang.String id, java.lang.String reldate, java.lang.String mindate, java.lang.String maxdate, java.lang.String datetype, java.lang.String term, java.lang.String dbfrom, java.lang.String linkname, java.lang.String webEnv, java.lang.String query_key, java.lang.String cmd, java.lang.String tool, java.lang.String email) throws java.rmi.RemoteException{
    if (eUtilsServiceSoap == null)
      _initEUtilsServiceSoapProxy();
    return eUtilsServiceSoap.run_eLink_MS(db, id, reldate, mindate, maxdate, datetype, term, dbfrom, linkname, webEnv, query_key, cmd, tool, email);
  }
  
  public gov.nih.nlm.ncbi.www.soap.eutils.espell.ESpellResult run_eSpell_MS(java.lang.String db, java.lang.String term, java.lang.String tool, java.lang.String email) throws java.rmi.RemoteException{
    if (eUtilsServiceSoap == null)
      _initEUtilsServiceSoapProxy();
    return eUtilsServiceSoap.run_eSpell_MS(db, term, tool, email);
  }
  
  public gov.nih.nlm.ncbi.www.soap.eutils.epost.EPostResult run_ePost_MS(java.lang.String db, java.lang.String id, java.lang.String webEnv, java.lang.String tool, java.lang.String email) throws java.rmi.RemoteException{
    if (eUtilsServiceSoap == null)
      _initEUtilsServiceSoapProxy();
    return eUtilsServiceSoap.run_ePost_MS(db, id, webEnv, tool, email);
  }
  
  
}