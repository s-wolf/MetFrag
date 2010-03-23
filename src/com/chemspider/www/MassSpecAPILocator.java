/**
 * MassSpecAPILocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.chemspider.www;

public class MassSpecAPILocator extends org.apache.axis.client.Service implements com.chemspider.www.MassSpecAPI {

/**
 * <h1>Proudly Provided by the ChemSpider Team</h1><div align="center"><img
 * src="/images/finallogo.jpg"></div><h3>Welcome to the ChemSpider Web
 * Services for Mass Spectrometry. These services are offered free of
 * charge to our users during this period of testing, validation and
 * feedback. Some of these services will be made available commercially
 * in the future and we are proactively informing you of our intention
 * to do this. It is likely that these services will remain available
 * to academia at no charge.</h3><h3>Some of the web service operations
 * listed here require a "Security Token". To obtain a token please complete
 * the <a href="http://www.chemspider.com/Register.aspx">registration</a>
 * process and visit your <a href="/UserProfile.aspx">Profile</a> page.
 * Your Security Token is listed there.</h3><h3>Please send all feedback
 * to development-at-chemspider-dot-com</h3>
 */

    public MassSpecAPILocator() {
    }


    public MassSpecAPILocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public MassSpecAPILocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for MassSpecAPISoap12
    private java.lang.String MassSpecAPISoap12_address = "http://www.chemspider.com/MassSpecAPI.asmx";

    public java.lang.String getMassSpecAPISoap12Address() {
        return MassSpecAPISoap12_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String MassSpecAPISoap12WSDDServiceName = "MassSpecAPISoap12";

    public java.lang.String getMassSpecAPISoap12WSDDServiceName() {
        return MassSpecAPISoap12WSDDServiceName;
    }

    public void setMassSpecAPISoap12WSDDServiceName(java.lang.String name) {
        MassSpecAPISoap12WSDDServiceName = name;
    }

    public com.chemspider.www.MassSpecAPISoap getMassSpecAPISoap12() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(MassSpecAPISoap12_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getMassSpecAPISoap12(endpoint);
    }

    public com.chemspider.www.MassSpecAPISoap getMassSpecAPISoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.chemspider.www.MassSpecAPISoap12Stub _stub = new com.chemspider.www.MassSpecAPISoap12Stub(portAddress, this);
            _stub.setPortName(getMassSpecAPISoap12WSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setMassSpecAPISoap12EndpointAddress(java.lang.String address) {
        MassSpecAPISoap12_address = address;
    }


    // Use to get a proxy class for MassSpecAPISoap
    private java.lang.String MassSpecAPISoap_address = "http://www.chemspider.com/MassSpecAPI.asmx";

    public java.lang.String getMassSpecAPISoapAddress() {
        return MassSpecAPISoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String MassSpecAPISoapWSDDServiceName = "MassSpecAPISoap";

    public java.lang.String getMassSpecAPISoapWSDDServiceName() {
        return MassSpecAPISoapWSDDServiceName;
    }

    public void setMassSpecAPISoapWSDDServiceName(java.lang.String name) {
        MassSpecAPISoapWSDDServiceName = name;
    }

    public com.chemspider.www.MassSpecAPISoap getMassSpecAPISoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(MassSpecAPISoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getMassSpecAPISoap(endpoint);
    }

    public com.chemspider.www.MassSpecAPISoap getMassSpecAPISoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.chemspider.www.MassSpecAPISoapStub _stub = new com.chemspider.www.MassSpecAPISoapStub(portAddress, this);
            _stub.setPortName(getMassSpecAPISoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setMassSpecAPISoapEndpointAddress(java.lang.String address) {
        MassSpecAPISoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     * This service has multiple ports for a given interface;
     * the proxy implementation returned may be indeterminate.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.chemspider.www.MassSpecAPISoap.class.isAssignableFrom(serviceEndpointInterface)) {
                com.chemspider.www.MassSpecAPISoap12Stub _stub = new com.chemspider.www.MassSpecAPISoap12Stub(new java.net.URL(MassSpecAPISoap12_address), this);
                _stub.setPortName(getMassSpecAPISoap12WSDDServiceName());
                return _stub;
            }
            if (com.chemspider.www.MassSpecAPISoap.class.isAssignableFrom(serviceEndpointInterface)) {
                com.chemspider.www.MassSpecAPISoapStub _stub = new com.chemspider.www.MassSpecAPISoapStub(new java.net.URL(MassSpecAPISoap_address), this);
                _stub.setPortName(getMassSpecAPISoapWSDDServiceName());
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
        if ("MassSpecAPISoap12".equals(inputPortName)) {
            return getMassSpecAPISoap12();
        }
        else if ("MassSpecAPISoap".equals(inputPortName)) {
            return getMassSpecAPISoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.chemspider.com/", "MassSpecAPI");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.chemspider.com/", "MassSpecAPISoap12"));
            ports.add(new javax.xml.namespace.QName("http://www.chemspider.com/", "MassSpecAPISoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("MassSpecAPISoap12".equals(portName)) {
            setMassSpecAPISoap12EndpointAddress(address);
        }
        else 
if ("MassSpecAPISoap".equals(portName)) {
            setMassSpecAPISoapEndpointAddress(address);
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
