/**
 * MassSpecAPISoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.chemspider.www;

public interface MassSpecAPISoap extends java.rmi.Remote {

    /**
     * Get the list of datasources in ChemSpider.
     */
    public java.lang.String[] getDatabases() throws java.rmi.RemoteException;

    /**
     * Search ChemSpider compounds by mass +/- range within specified
     * datasources list. <span style="color: red; font-weight: bold;">This
     * operation is deprecated and will be removed soon - use SearchByMassAsync
     * instead.</span>
     */
    public java.lang.String[] searchByMass(double mass, double range, java.lang.String[] dbs) throws java.rmi.RemoteException;

    /**
     * Search ChemSpider compounds by mass +/- range.
     */
    public java.lang.String[] searchByMass2(double mass, double range) throws java.rmi.RemoteException;

    /**
     * Search ChemSpider compounds by molecular formula within specified
     * datasources list. <span style="color: red; font-weight: bold;">This
     * operation is deprecated and will be removed soon - use SearchByFormulaAsync
     * instead.</span>
     */
    public java.lang.String[] searchByFormula(java.lang.String formula, java.lang.String[] dbs) throws java.rmi.RemoteException;

    /**
     * Search ChemSpider compounds by molecular formula.
     */
    public java.lang.String[] searchByFormula2(java.lang.String formula) throws java.rmi.RemoteException;

    /**
     * Return specified compound details. <span style="color: red;
     * font-weight: bold;">This operation is deprecated and will be removed
     * soon - use GetExtendedCompoundInfo instead.</span>
     */
    public java.lang.String[] getCompoundDetails(java.lang.String cmp_id) throws java.rmi.RemoteException;

    /**
     * Returns ChemSpider record in MOL format or empty string in
     * case of failure. cacl3d parameter specifies if 3D coordinates should
     * be calculated before returning record data. Security token is required
     * to get access to this service.
     */
    public java.lang.String getRecordMol(java.lang.String csid, boolean calc3D, java.lang.String token) throws java.rmi.RemoteException;

    /**
     * Get extended record details by CSID. Security token is required
     * to get access to this service.
     */
    public com.chemspider.www.ExtendedCompoundInfo getExtendedCompoundInfo(int CSID, java.lang.String token) throws java.rmi.RemoteException;

    /**
     * Get array of extended record details by an array of CSIDs.
     * Security token is required to get access to this service.
     */
    public com.chemspider.www.ExtendedCompoundInfo[] getExtendedCompoundInfoArray(int[] CSIDs, java.lang.String token) throws java.rmi.RemoteException;

    /**
     * Search ChemSpider compounds by mass +/- range within specified
     * datasources list. Security token is required to get access to this
     * service.
     */
    public java.lang.String searchByMassAsync(double mass, double range, java.lang.String[] dbs, java.lang.String token) throws java.rmi.RemoteException;

    /**
     * Search ChemSpider compounds by molecular formula within specified
     * datasources list. Security token is required to get access to this
     * service.
     */
    public java.lang.String searchByFormulaAsync(java.lang.String formula, java.lang.String[] dbs, java.lang.String token) throws java.rmi.RemoteException;
}
