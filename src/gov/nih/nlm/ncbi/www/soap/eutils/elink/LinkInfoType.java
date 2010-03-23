/**
 * LinkInfoType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.www.soap.eutils.elink;

public class LinkInfoType  implements java.io.Serializable {
    private java.lang.String dbTo;

    private java.lang.String linkName;

    private java.lang.String menuTag;

    public LinkInfoType() {
    }

    public LinkInfoType(
           java.lang.String dbTo,
           java.lang.String linkName,
           java.lang.String menuTag) {
           this.dbTo = dbTo;
           this.linkName = linkName;
           this.menuTag = menuTag;
    }


    /**
     * Gets the dbTo value for this LinkInfoType.
     * 
     * @return dbTo
     */
    public java.lang.String getDbTo() {
        return dbTo;
    }


    /**
     * Sets the dbTo value for this LinkInfoType.
     * 
     * @param dbTo
     */
    public void setDbTo(java.lang.String dbTo) {
        this.dbTo = dbTo;
    }


    /**
     * Gets the linkName value for this LinkInfoType.
     * 
     * @return linkName
     */
    public java.lang.String getLinkName() {
        return linkName;
    }


    /**
     * Sets the linkName value for this LinkInfoType.
     * 
     * @param linkName
     */
    public void setLinkName(java.lang.String linkName) {
        this.linkName = linkName;
    }


    /**
     * Gets the menuTag value for this LinkInfoType.
     * 
     * @return menuTag
     */
    public java.lang.String getMenuTag() {
        return menuTag;
    }


    /**
     * Sets the menuTag value for this LinkInfoType.
     * 
     * @param menuTag
     */
    public void setMenuTag(java.lang.String menuTag) {
        this.menuTag = menuTag;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof LinkInfoType)) return false;
        LinkInfoType other = (LinkInfoType) obj;
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
            ((this.menuTag==null && other.getMenuTag()==null) || 
             (this.menuTag!=null &&
              this.menuTag.equals(other.getMenuTag())));
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
        if (getMenuTag() != null) {
            _hashCode += getMenuTag().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(LinkInfoType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "LinkInfoType"));
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
        elemField.setFieldName("menuTag");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "MenuTag"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
