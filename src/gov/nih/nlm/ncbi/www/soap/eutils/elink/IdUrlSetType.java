/**
 * IdUrlSetType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.www.soap.eutils.elink;

public class IdUrlSetType  implements java.io.Serializable {
    private gov.nih.nlm.ncbi.www.soap.eutils.elink.IdType id;

    private gov.nih.nlm.ncbi.www.soap.eutils.elink.ObjUrlType[] objUrl;

    private java.lang.String info;

    public IdUrlSetType() {
    }

    public IdUrlSetType(
           gov.nih.nlm.ncbi.www.soap.eutils.elink.IdType id,
           gov.nih.nlm.ncbi.www.soap.eutils.elink.ObjUrlType[] objUrl,
           java.lang.String info) {
           this.id = id;
           this.objUrl = objUrl;
           this.info = info;
    }


    /**
     * Gets the id value for this IdUrlSetType.
     * 
     * @return id
     */
    public gov.nih.nlm.ncbi.www.soap.eutils.elink.IdType getId() {
        return id;
    }


    /**
     * Sets the id value for this IdUrlSetType.
     * 
     * @param id
     */
    public void setId(gov.nih.nlm.ncbi.www.soap.eutils.elink.IdType id) {
        this.id = id;
    }


    /**
     * Gets the objUrl value for this IdUrlSetType.
     * 
     * @return objUrl
     */
    public gov.nih.nlm.ncbi.www.soap.eutils.elink.ObjUrlType[] getObjUrl() {
        return objUrl;
    }


    /**
     * Sets the objUrl value for this IdUrlSetType.
     * 
     * @param objUrl
     */
    public void setObjUrl(gov.nih.nlm.ncbi.www.soap.eutils.elink.ObjUrlType[] objUrl) {
        this.objUrl = objUrl;
    }

    public gov.nih.nlm.ncbi.www.soap.eutils.elink.ObjUrlType getObjUrl(int i) {
        return this.objUrl[i];
    }

    public void setObjUrl(int i, gov.nih.nlm.ncbi.www.soap.eutils.elink.ObjUrlType _value) {
        this.objUrl[i] = _value;
    }


    /**
     * Gets the info value for this IdUrlSetType.
     * 
     * @return info
     */
    public java.lang.String getInfo() {
        return info;
    }


    /**
     * Sets the info value for this IdUrlSetType.
     * 
     * @param info
     */
    public void setInfo(java.lang.String info) {
        this.info = info;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof IdUrlSetType)) return false;
        IdUrlSetType other = (IdUrlSetType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            ((this.objUrl==null && other.getObjUrl()==null) || 
             (this.objUrl!=null &&
              java.util.Arrays.equals(this.objUrl, other.getObjUrl()))) &&
            ((this.info==null && other.getInfo()==null) || 
             (this.info!=null &&
              this.info.equals(other.getInfo())));
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
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getObjUrl() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getObjUrl());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getObjUrl(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getInfo() != null) {
            _hashCode += getInfo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(IdUrlSetType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "IdUrlSetType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "Id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "IdType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("objUrl");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "ObjUrl"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "ObjUrlType"));
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
