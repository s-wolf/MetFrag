/**
 * SimilaritySearchOptions.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.pubchem;

public class SimilaritySearchOptions  implements java.io.Serializable {
    private int threshold;

    private java.lang.String toWebEnv;

    public SimilaritySearchOptions() {
    }

    public SimilaritySearchOptions(
           int threshold,
           java.lang.String toWebEnv) {
           this.threshold = threshold;
           this.toWebEnv = toWebEnv;
    }


    /**
     * Gets the threshold value for this SimilaritySearchOptions.
     * 
     * @return threshold
     */
    public int getThreshold() {
        return threshold;
    }


    /**
     * Sets the threshold value for this SimilaritySearchOptions.
     * 
     * @param threshold
     */
    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }


    /**
     * Gets the toWebEnv value for this SimilaritySearchOptions.
     * 
     * @return toWebEnv
     */
    public java.lang.String getToWebEnv() {
        return toWebEnv;
    }


    /**
     * Sets the toWebEnv value for this SimilaritySearchOptions.
     * 
     * @param toWebEnv
     */
    public void setToWebEnv(java.lang.String toWebEnv) {
        this.toWebEnv = toWebEnv;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SimilaritySearchOptions)) return false;
        SimilaritySearchOptions other = (SimilaritySearchOptions) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.threshold == other.getThreshold() &&
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
        _hashCode += getThreshold();
        if (getToWebEnv() != null) {
            _hashCode += getToWebEnv().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SimilaritySearchOptions.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "SimilaritySearchOptions"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("threshold");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "threshold"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
