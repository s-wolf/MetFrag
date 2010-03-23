/**
 * LinkSetDbType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.www.soap.eutils.elink;

public class LinkSetDbType  implements java.io.Serializable {
    private java.lang.String dbTo;

    private java.lang.String linkName;

    private gov.nih.nlm.ncbi.www.soap.eutils.elink.LinkType[] link;

    private java.lang.String info;

    private java.lang.String ERROR;

    public LinkSetDbType() {
    }

    public LinkSetDbType(
           java.lang.String dbTo,
           java.lang.String linkName,
           gov.nih.nlm.ncbi.www.soap.eutils.elink.LinkType[] link,
           java.lang.String info,
           java.lang.String ERROR) {
           this.dbTo = dbTo;
           this.linkName = linkName;
           this.link = link;
           this.info = info;
           this.ERROR = ERROR;
    }


    /**
     * Gets the dbTo value for this LinkSetDbType.
     * 
     * @return dbTo
     */
    public java.lang.String getDbTo() {
        return dbTo;
    }


    /**
     * Sets the dbTo value for this LinkSetDbType.
     * 
     * @param dbTo
     */
    public void setDbTo(java.lang.String dbTo) {
        this.dbTo = dbTo;
    }


    /**
     * Gets the linkName value for this LinkSetDbType.
     * 
     * @return linkName
     */
    public java.lang.String getLinkName() {
        return linkName;
    }


    /**
     * Sets the linkName value for this LinkSetDbType.
     * 
     * @param linkName
     */
    public void setLinkName(java.lang.String linkName) {
        this.linkName = linkName;
    }


    /**
     * Gets the link value for this LinkSetDbType.
     * 
     * @return link
     */
    public gov.nih.nlm.ncbi.www.soap.eutils.elink.LinkType[] getLink() {
        return link;
    }


    /**
     * Sets the link value for this LinkSetDbType.
     * 
     * @param link
     */
    public void setLink(gov.nih.nlm.ncbi.www.soap.eutils.elink.LinkType[] link) {
        this.link = link;
    }

    public gov.nih.nlm.ncbi.www.soap.eutils.elink.LinkType getLink(int i) {
        return this.link[i];
    }

    public void setLink(int i, gov.nih.nlm.ncbi.www.soap.eutils.elink.LinkType _value) {
        this.link[i] = _value;
    }


    /**
     * Gets the info value for this LinkSetDbType.
     * 
     * @return info
     */
    public java.lang.String getInfo() {
        return info;
    }


    /**
     * Sets the info value for this LinkSetDbType.
     * 
     * @param info
     */
    public void setInfo(java.lang.String info) {
        this.info = info;
    }


    /**
     * Gets the ERROR value for this LinkSetDbType.
     * 
     * @return ERROR
     */
    public java.lang.String getERROR() {
        return ERROR;
    }


    /**
     * Sets the ERROR value for this LinkSetDbType.
     * 
     * @param ERROR
     */
    public void setERROR(java.lang.String ERROR) {
        this.ERROR = ERROR;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof LinkSetDbType)) return false;
        LinkSetDbType other = (LinkSetDbType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.dbTo==null && other.getDbTo()==null) || 
             (this.dbTo!=null &&
              this.dbTo.equals(other.getDbTo()))) &&
            ((this.linkName==null && other.getLinkName()==null) || 
             (this.linkName!=null &&
              this.linkName.equals(other.getLinkName()))) &&
            ((this.link==null && other.getLink()==null) || 
             (this.link!=null &&
              java.util.Arrays.equals(this.link, other.getLink()))) &&
            ((this.info==null && other.getInfo()==null) || 
             (this.info!=null &&
              this.info.equals(other.getInfo()))) &&
            ((this.ERROR==null && other.getERROR()==null) || 
             (this.ERROR!=null &&
              this.ERROR.equals(other.getERROR())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getDbTo() != null) {
            _hashCode += getDbTo().hashCode();
        }
        if (getLinkName() != null) {
            _hashCode += getLinkName().hashCode();
        }
        if (getLink() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getLink());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getLink(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getInfo() != null) {
            _hashCode += getInfo().hashCode();
        }
        if (getERROR() != null) {
            _hashCode += getERROR().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(LinkSetDbType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "LinkSetDbType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dbTo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "DbTo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("linkName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "LinkName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("link");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "Link"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "LinkType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("info");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "Info"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ERROR");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "ERROR"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
