/**
 * EUtilsServiceSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.www.soap.eutils;

public interface EUtilsServiceSoap extends java.rmi.Remote {
    public gov.nih.nlm.ncbi.www.soap.eutils.egquery.Result run_eGquery(gov.nih.nlm.ncbi.www.soap.eutils.egquery.EGqueryRequest request) throws java.rmi.RemoteException;
    public gov.nih.nlm.ncbi.www.soap.eutils.egquery.Result run_eGquery_MS(java.lang.String term, java.lang.String tool, java.lang.String email) throws java.rmi.RemoteException;
    public gov.nih.nlm.ncbi.www.soap.eutils.einfo.EInfoResult run_eInfo(gov.nih.nlm.ncbi.www.soap.eutils.einfo.EInfoRequest request) throws java.rmi.RemoteException;
    public gov.nih.nlm.ncbi.www.soap.eutils.einfo.EInfoResult run_eInfo_MS(java.lang.String db, java.lang.String tool, java.lang.String email) throws java.rmi.RemoteException;
    public gov.nih.nlm.ncbi.www.soap.eutils.esearch.ESearchResult run_eSearch(gov.nih.nlm.ncbi.www.soap.eutils.esearch.ESearchRequest request) throws java.rmi.RemoteException;
    public gov.nih.nlm.ncbi.www.soap.eutils.esearch.ESearchResult run_eSearch_MS(java.lang.String db, java.lang.String term, java.lang.String webEnv, java.lang.String queryKey, java.lang.String usehistory, java.lang.String tool, java.lang.String email, java.lang.String field, java.lang.String reldate, java.lang.String mindate, java.lang.String maxdate, java.lang.String datetype, java.lang.String retStart, java.lang.String retMax, java.lang.String rettype, java.lang.String sort) throws java.rmi.RemoteException;
    public gov.nih.nlm.ncbi.www.soap.eutils.esummary.ESummaryResult run_eSummary(gov.nih.nlm.ncbi.www.soap.eutils.esummary.ESummaryRequest request) throws java.rmi.RemoteException;
    public gov.nih.nlm.ncbi.www.soap.eutils.esummary.ESummaryResult run_eSummary_MS(java.lang.String db, java.lang.String id, java.lang.String webEnv, java.lang.String query_key, java.lang.String retstart, java.lang.String retmax, java.lang.String tool, java.lang.String email) throws java.rmi.RemoteException;
    public gov.nih.nlm.ncbi.www.soap.eutils.elink.ELinkResult run_eLink(gov.nih.nlm.ncbi.www.soap.eutils.elink.ELinkRequest request) throws java.rmi.RemoteException;
    public gov.nih.nlm.ncbi.www.soap.eutils.elink.ELinkResult run_eLink_MS(java.lang.String db, java.lang.String id, java.lang.String reldate, java.lang.String mindate, java.lang.String maxdate, java.lang.String datetype, java.lang.String term, java.lang.String dbfrom, java.lang.String linkname, java.lang.String webEnv, java.lang.String query_key, java.lang.String cmd, java.lang.String tool, java.lang.String email) throws java.rmi.RemoteException;
    public gov.nih.nlm.ncbi.www.soap.eutils.espell.ESpellResult run_eSpell(gov.nih.nlm.ncbi.www.soap.eutils.espell.ESpellRequest request) throws java.rmi.RemoteException;
    public gov.nih.nlm.ncbi.www.soap.eutils.espell.ESpellResult run_eSpell_MS(java.lang.String db, java.lang.String term, java.lang.String tool, java.lang.String email) throws java.rmi.RemoteException;
    public gov.nih.nlm.ncbi.www.soap.eutils.epost.EPostResult run_ePost(gov.nih.nlm.ncbi.www.soap.eutils.epost.EPostRequest request) throws java.rmi.RemoteException;
    public gov.nih.nlm.ncbi.www.soap.eutils.epost.EPostResult run_ePost_MS(java.lang.String db, java.lang.String id, java.lang.String webEnv, java.lang.String tool, java.lang.String email) throws java.rmi.RemoteException;
}
