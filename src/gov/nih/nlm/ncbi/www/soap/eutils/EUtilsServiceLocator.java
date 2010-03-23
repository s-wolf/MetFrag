/**
 * EUtilsServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.www.soap.eutils;

public class EUtilsServiceLocator extends org.apache.axis.client.Service implements gov.nih.nlm.ncbi.www.soap.eutils.EUtilsService {

    public EUtilsServiceLocator() {
    }


    public EUtilsServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public EUtilsServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for eUtilsServiceSoap
    private java.lang.String eUtilsServiceSoap_address = "http://eutils.ncbi.nlm.nih.gov/entrez/eutils/soap/v2.0/soap_adapter_2_0.cgi";

    public java.lang.String geteUtilsServiceSoapAddress() {
        return eUtilsServiceSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String eUtilsServiceSoapWSDDServiceName = "eUtilsServiceSoap";

    public java.lang.String geteUtilsServiceSoapWSDDServiceName() {
        return eUtilsServiceSoapWSDDServiceName;
    }

    public void seteUtilsServiceSoapWSDDServiceName(java.lang.String name) {
        eUtilsServiceSoapWSDDServiceName = name;
    }

    public gov.nih.nlm.ncbi.www.soap.eutils.EUtilsServiceSoap geteUtilsServiceSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(eUtilsServiceSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return geteUtilsServiceSoap(endpoint);
    }

    public gov.nih.nlm.ncbi.www.soap.eutils.EUtilsServiceSoap geteUtilsServiceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            gov.nih.nlm.ncbi.www.soap.eutils.EUtilsServiceSoapStub _stub = new gov.nih.nlm.ncbi.www.soap.eutils.EUtilsServiceSoapStub(portAddress, this);
            _stub.setPortName(geteUtilsServiceSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void seteUtilsServiceSoapEndpointAddress(java.lang.String address) {
        eUtilsServiceSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (gov.nih.nlm.ncbi.www.soap.eutils.EUtilsServiceSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                gov.nih.nlm.ncbi.www.soap.eutils.EUtilsServiceSoapStub _stub = new gov.nih.nlm.ncbi.www.soap.eutils.EUtilsServiceSoapStub(new java.net.URL(eUtilsServiceSoap_address), this);
                _stub.setPortName(geteUtilsServiceSoapWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("eUtilsServiceSoap".equals(inputPortName)) {
            return geteUtilsServiceSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/", "eUtilsService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/", "eUtilsServiceSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("eUtilsServiceSoap".equals(portName)) {
            seteUtilsServiceSoapEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
