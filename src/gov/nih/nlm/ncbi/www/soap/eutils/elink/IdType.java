/**
 * IdType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.www.soap.eutils.elink;

public class IdType  implements java.io.Serializable, org.apache.axis.encoding.SimpleType {
    private java.lang.String _value;

    private gov.nih.nlm.ncbi.www.soap.eutils.elink.IdTypeHasLinkOut hasLinkOut;  // attribute

    private gov.nih.nlm.ncbi.www.soap.eutils.elink.IdTypeHasNeighbor hasNeighbor;  // attribute

    public IdType() {
    }

    // Simple Types must have a String constructor
    public IdType(java.lang.String _value) {
        this._value = _value;
    }
    // Simple Types must have a toString for serializing the value
    public java.lang.String toString() {
        return _value;
    }


    /**
     * Gets the _value value for this IdType.
     * 
     * @return _value
     */
    public java.lang.String get_value() {
        return _value;
    }


    /**
     * Sets the _value value for this IdType.
     * 
     * @param _value
     */
    public void set_value(java.lang.String _value) {
        this._value = _value;
    }


    /**
     * Gets the hasLinkOut value for this IdType.
     * 
     * @return hasLinkOut
     */
    public gov.nih.nlm.ncbi.www.soap.eutils.elink.IdTypeHasLinkOut getHasLinkOut() {
        return hasLinkOut;
    }


    /**
     * Sets the hasLinkOut value for this IdType.
     * 
     * @param hasLinkOut
     */
    public void setHasLinkOut(gov.nih.nlm.ncbi.www.soap.eutils.elink.IdTypeHasLinkOut hasLinkOut) {
        this.hasLinkOut = hasLinkOut;
    }


    /**
     * Gets the hasNeighbor value for this IdType.
     * 
     * @return hasNeighbor
     */
    public gov.nih.nlm.ncbi.www.soap.eutils.elink.IdTypeHasNeighbor getHasNeighbor() {
        return hasNeighbor;
    }


    /**
     * Sets the hasNeighbor value for this IdType.
     * 
     * @param hasNeighbor
     */
    public void setHasNeighbor(gov.nih.nlm.ncbi.www.soap.eutils.elink.IdTypeHasNeighbor hasNeighbor) {
        this.hasNeighbor = hasNeighbor;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof IdType)) return false;
        IdType other = (IdType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this._value==null && other.get_value()==null) || 
             (this._value!=null &&
              this._value.equals(other.get_value()))) &&
            ((this.hasLinkOut==null && other.getHasLinkOut()==null) || 
             (this.hasLinkOut!=null &&
              this.hasLinkOut.equals(other.getHasLinkOut()))) &&
            ((this.hasNeighbor==null && other.getHasNeighbor()==null) || 
             (this.hasNeighbor!=null &&
              this.hasNeighbor.equals(other.getHasNeighbor())));
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
        if (get_value() != null) {
            _hashCode += get_value().hashCode();
        }
        if (getHasLinkOut() != null) {
            _hashCode += getHasLinkOut().hashCode();
        }
        if (getHasNeighbor() != null) {
            _hashCode += getHasNeighbor().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(IdType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "IdType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("hasLinkOut");
        attrField.setXmlName(new javax.xml.namespace.QName("", "HasLinkOut"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", ">IdType>HasLinkOut"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("hasNeighbor");
        attrField.setXmlName(new javax.xml.namespace.QName("", "HasNeighbor"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", ">IdType>HasNeighbor"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("_value");
        elemField.setXmlName(new javax.xml.namespace.QName("", "_value"));
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
          new  org.apache.axis.encoding.ser.SimpleSerializer(
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
          new  org.apache.axis.encoding.ser.SimpleDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
