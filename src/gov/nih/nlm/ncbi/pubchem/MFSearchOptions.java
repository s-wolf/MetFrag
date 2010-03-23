/**
 * MFSearchOptions.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.pubchem;

public class MFSearchOptions  implements java.io.Serializable {
    private boolean allowOtherElements;

    private java.lang.String toWebEnv;

    public MFSearchOptions() {
    }

    public MFSearchOptions(
           boolean allowOtherElements,
           java.lang.String toWebEnv) {
           this.allowOtherElements = allowOtherElements;
           this.toWebEnv = toWebEnv;
    }


    /**
     * Gets the allowOtherElements value for this MFSearchOptions.
     * 
     * @return allowOtherElements
     */
    public boolean isAllowOtherElements() {
        return allowOtherElements;
    }


    /**
     * Sets the allowOtherElements value for this MFSearchOptions.
     * 
     * @param allowOtherElements
     */
    public void setAllowOtherElements(boolean allowOtherElements) {
        this.allowOtherElements = allowOtherElements;
    }


    /**
     * Gets the toWebEnv value for this MFSearchOptions.
     * 
     * @return toWebEnv
     */
    public java.lang.String getToWebEnv() {
        return toWebEnv;
    }


    /**
     * Sets the toWebEnv value for this MFSearchOptions.
     * 
     * @param toWebEnv
     */
    public void setToWebEnv(java.lang.String toWebEnv) {
        this.toWebEnv = toWebEnv;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MFSearchOptions)) return false;
        MFSearchOptions other = (MFSearchOptions) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.allowOtherElements == other.isAllowOtherElements() &&
            ((this.toWebEnv==null && other.getToWebEnv()==null) || 
             (this.toWebEnv!=null &&
              this.toWebEnv.equals(other.getToWebEnv())));
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
        _hashCode += (isAllowOtherElements() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getToWebEnv() != null) {
            _hashCode += getToWebEnv().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(MFSearchOptions.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "MFSearchOptions"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("allowOtherElements");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "AllowOtherElements"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("toWebEnv");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "ToWebEnv"));
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
