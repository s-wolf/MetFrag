/**
 * PUGLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.pubchem;

public class PUGLocator extends org.apache.axis.client.Service implements gov.nih.nlm.ncbi.pubchem.PUG {

    public PUGLocator() {
    }


    public PUGLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public PUGLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for PUGSoap
    private java.lang.String PUGSoap_address = "http://pubchem.ncbi.nlm.nih.gov/pug_soap/pug_soap.cgi";

    public java.lang.String getPUGSoapAddress() {
        return PUGSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String PUGSoapWSDDServiceName = "PUGSoap";

    public java.lang.String getPUGSoapWSDDServiceName() {
        return PUGSoapWSDDServiceName;
    }

    public void setPUGSoapWSDDServiceName(java.lang.String name) {
        PUGSoapWSDDServiceName = name;
    }

    public gov.nih.nlm.ncbi.pubchem.PUGSoap getPUGSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(PUGSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getPUGSoap(endpoint);
    }

    public gov.nih.nlm.ncbi.pubchem.PUGSoap getPUGSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            gov.nih.nlm.ncbi.pubchem.PUGSoapStub _stub = new gov.nih.nlm.ncbi.pubchem.PUGSoapStub(portAddress, this);
            _stub.setPortName(getPUGSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setPUGSoapEndpointAddress(java.lang.String address) {
        PUGSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (gov.nih.nlm.ncbi.pubchem.PUGSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                gov.nih.nlm.ncbi.pubchem.PUGSoapStub _stub = new gov.nih.nlm.ncbi.pubchem.PUGSoapStub(new java.net.URL(PUGSoap_address), this);
                _stub.setPortName(getPUGSoapWSDDServiceName());
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
        if ("PUGSoap".equals(inputPortName)) {
            return getPUGSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "PUG");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "PUGSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("PUGSoap".equals(portName)) {
            setPUGSoapEndpointAddress(address);
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
