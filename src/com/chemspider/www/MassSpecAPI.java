/**
 * MassSpecAPI.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.chemspider.www;

public interface MassSpecAPI extends javax.xml.rpc.Service {

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
    public java.lang.String getMassSpecAPISoap12Address();

    public com.chemspider.www.MassSpecAPISoap getMassSpecAPISoap12() throws javax.xml.rpc.ServiceException;

    public com.chemspider.www.MassSpecAPISoap getMassSpecAPISoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
    public java.lang.String getMassSpecAPISoapAddress();

    public com.chemspider.www.MassSpecAPISoap getMassSpecAPISoap() throws javax.xml.rpc.ServiceException;

    public com.chemspider.www.MassSpecAPISoap getMassSpecAPISoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
