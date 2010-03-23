/**
 * TranslationStackType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.www.soap.eutils.esearch;

public class TranslationStackType  implements java.io.Serializable {
    private gov.nih.nlm.ncbi.www.soap.eutils.esearch.TermSetType termSet;

    private java.lang.String OP;

    public TranslationStackType() {
    }

    public TranslationStackType(
           gov.nih.nlm.ncbi.www.soap.eutils.esearch.TermSetType termSet,
           java.lang.String OP) {
           this.termSet = termSet;
           this.OP = OP;
    }


    /**
     * Gets the termSet value for this TranslationStackType.
     * 
     * @return termSet
     */
    public gov.nih.nlm.ncbi.www.soap.eutils.esearch.TermSetType getTermSet() {
        return termSet;
    }


    /**
     * Sets the termSet value for this TranslationStackType.
     * 
     * @param termSet
     */
    public void setTermSet(gov.nih.nlm.ncbi.www.soap.eutils.esearch.TermSetType termSet) {
        this.termSet = termSet;
    }


    /**
     * Gets the OP value for this TranslationStackType.
     * 
     * @return OP
     */
    public java.lang.String getOP() {
        return OP;
    }


    /**
     * Sets the OP value for this TranslationStackType.
     * 
     * @param OP
     */
    public void setOP(java.lang.String OP) {
        this.OP = OP;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TranslationStackType)) return false;
        TranslationStackType other = (TranslationStackType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.termSet==null && other.getTermSet()==null) || 
             (this.termSet!=null &&
              this.termSet.equals(other.getTermSet()))) &&
            ((this.OP==null && other.getOP()==null) || 
             (this.OP!=null &&
              this.OP.equals(other.getOP())));
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
        if (getTermSet() != null) {
            _hashCode += getTermSet().hashCode();
        }
        if (getOP() != null) {
            _hashCode += getOP().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TranslationStackType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "TranslationStackType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("termSet");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "TermSet"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "TermSetType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("OP");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "OP"));
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
