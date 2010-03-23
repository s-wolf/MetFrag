package com.chemspider.www;

public class MassSpecAPISoapProxy implements com.chemspider.www.MassSpecAPISoap {
  private String _endpoint = null;
  private com.chemspider.www.MassSpecAPISoap massSpecAPISoap = null;
  
  public MassSpecAPISoapProxy() {
    _initMassSpecAPISoapProxy();
  }
  
  public MassSpecAPISoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initMassSpecAPISoapProxy();
  }
  
  private void _initMassSpecAPISoapProxy() {
    try {
      massSpecAPISoap = (new com.chemspider.www.MassSpecAPILocator()).getMassSpecAPISoap();
      if (massSpecAPISoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)massSpecAPISoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)massSpecAPISoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (massSpecAPISoap != null)
      ((javax.xml.rpc.Stub)massSpecAPISoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.chemspider.www.MassSpecAPISoap getMassSpecAPISoap() {
    if (massSpecAPISoap == null)
      _initMassSpecAPISoapProxy();
    return massSpecAPISoap;
  }
  
  public java.lang.String[] getDatabases() throws java.rmi.RemoteException{
    if (massSpecAPISoap == null)
      _initMassSpecAPISoapProxy();
    return massSpecAPISoap.getDatabases();
  }
  
  public java.lang.String[] searchByMass(double mass, double range, java.lang.String[] dbs) throws java.rmi.RemoteException{
    if (massSpecAPISoap == null)
      _initMassSpecAPISoapProxy();
    return massSpecAPISoap.searchByMass(mass, range, dbs);
  }
  
  public java.lang.String[] searchByMass2(double mass, double range) throws java.rmi.RemoteException{
    if (massSpecAPISoap == null)
      _initMassSpecAPISoapProxy();
    return massSpecAPISoap.searchByMass2(mass, range);
  }
  
  public java.lang.String[] searchByFormula(java.lang.String formula, java.lang.String[] dbs) throws java.rmi.RemoteException{
    if (massSpecAPISoap == null)
      _initMassSpecAPISoapProxy();
    return massSpecAPISoap.searchByFormula(formula, dbs);
  }
  
  public java.lang.String[] searchByFormula2(java.lang.String formula) throws java.rmi.RemoteException{
    if (massSpecAPISoap == null)
      _initMassSpecAPISoapProxy();
    return massSpecAPISoap.searchByFormula2(formula);
  }
  
  public java.lang.String[] getCompoundDetails(java.lang.String cmp_id) throws java.rmi.RemoteException{
    if (massSpecAPISoap == null)
      _initMassSpecAPISoapProxy();
    return massSpecAPISoap.getCompoundDetails(cmp_id);
  }
  
  public java.lang.String getRecordMol(java.lang.String csid, boolean calc3D, java.lang.String token) throws java.rmi.RemoteException{
    if (massSpecAPISoap == null)
      _initMassSpecAPISoapProxy();
    return massSpecAPISoap.getRecordMol(csid, calc3D, token);
  }
  
  public com.chemspider.www.ExtendedCompoundInfo getExtendedCompoundInfo(int CSID, java.lang.String token) throws java.rmi.RemoteException{
    if (massSpecAPISoap == null)
      _initMassSpecAPISoapProxy();
    return massSpecAPISoap.getExtendedCompoundInfo(CSID, token);
  }
  
  public com.chemspider.www.ExtendedCompoundInfo[] getExtendedCompoundInfoArray(int[] CSIDs, java.lang.String token) throws java.rmi.RemoteException{
    if (massSpecAPISoap == null)
      _initMassSpecAPISoapProxy();
    return massSpecAPISoap.getExtendedCompoundInfoArray(CSIDs, token);
  }
  
  public java.lang.String searchByMassAsync(double mass, double range, java.lang.String[] dbs, java.lang.String token) throws java.rmi.RemoteException{
    if (massSpecAPISoap == null)
      _initMassSpecAPISoapProxy();
    return massSpecAPISoap.searchByMassAsync(mass, range, dbs, token);
  }
  
  public java.lang.String searchByFormulaAsync(java.lang.String formula, java.lang.String[] dbs, java.lang.String token) throws java.rmi.RemoteException{
    if (massSpecAPISoap == null)
      _initMassSpecAPISoapProxy();
    return massSpecAPISoap.searchByFormulaAsync(formula, dbs, token);
  }
  
  
}