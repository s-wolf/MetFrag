/**
 * LimitsType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.pubchem;

public class LimitsType  implements java.io.Serializable {
    private java.lang.Integer seconds;

    private java.lang.Integer maxRecords;

    private java.lang.String listKey;

    public LimitsType() {
    }

    public LimitsType(
           java.lang.Integer seconds,
           java.lang.Integer maxRecords,
           java.lang.String listKey) {
           this.seconds = seconds;
           this.maxRecords = maxRecords;
           this.listKey = listKey;
    }


    /**
     * Gets the seconds value for this LimitsType.
     * 
     * @return seconds
     */
    public java.lang.Integer getSeconds() {
        return seconds;
    }


    /**
     * Sets the seconds value for this LimitsType.
     * 
     * @param seconds
     */
    public void setSeconds(java.lang.Integer seconds) {
        this.seconds = seconds;
    }


    /**
     * Gets the maxRecords value for this LimitsType.
     * 
     * @return maxRecords
     */
    public java.lang.Integer getMaxRecords() {
        return maxRecords;
    }


    /**
     * Sets the maxRecords value for this LimitsType.
     * 
     * @param maxRecords
     */
    public void setMaxRecords(java.lang.Integer maxRecords) {
        this.maxRecords = maxRecords;
    }


    /**
     * Gets the listKey value for this LimitsType.
     * 
     * @return listKey
     */
    public java.lang.String getListKey() {
        return listKey;
    }


    /**
     * Sets the listKey value for this LimitsType.
     * 
     * @param listKey
     */
    public void setListKey(java.lang.String listKey) {
        this.listKey = listKey;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof LimitsType)) return false;
        LimitsType other = (LimitsType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.seconds==null && other.getSeconds()==null) || 
             (this.seconds!=null &&
              this.seconds.equals(other.getSeconds()))) &&
            ((this.maxRecords==null && other.getMaxRecords()==null) || 
             (this.maxRecords!=null &&
              this.maxRecords.equals(other.getMaxRecords()))) &&
            ((this.listKey==null && other.getListKey()==null) || 
             (this.listKey!=null &&
              this.listKey.equals(other.getListKey())));
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
        if (getSeconds() != null) {
            _hashCode += getSeconds().hashCode();
        }
        if (getMaxRecords() != null) {
            _hashCode += getMaxRecords().hashCode();
        }
        if (getListKey() != null) {
            _hashCode += getListKey().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(LimitsType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "LimitsType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("seconds");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "seconds"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("maxRecords");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "maxRecords"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("listKey");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "ListKey"));
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
