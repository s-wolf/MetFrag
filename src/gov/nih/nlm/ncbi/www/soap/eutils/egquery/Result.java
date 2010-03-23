/**
 * Result.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.www.soap.eutils.egquery;

public class Result  implements java.io.Serializable {
    private java.lang.String term;

    private gov.nih.nlm.ncbi.www.soap.eutils.egquery.EGQueryResultType eGQueryResult;

    public Result() {
    }

    public Result(
           java.lang.String term,
           gov.nih.nlm.ncbi.www.soap.eutils.egquery.EGQueryResultType eGQueryResult) {
           this.term = term;
           this.eGQueryResult = eGQueryResult;
    }


    /**
     * Gets the term value for this Result.
     * 
     * @return term
     */
    public java.lang.String getTerm() {
        return term;
    }


    /**
     * Sets the term value for this Result.
     * 
     * @param term
     */
    public void setTerm(java.lang.String term) {
        this.term = term;
    }


    /**
     * Gets the eGQueryResult value for this Result.
     * 
     * @return eGQueryResult
     */
    public gov.nih.nlm.ncbi.www.soap.eutils.egquery.EGQueryResultType getEGQueryResult() {
        return eGQueryResult;
    }


    /**
     * Sets the eGQueryResult value for this Result.
     * 
     * @param eGQueryResult
     */
    public void setEGQueryResult(gov.nih.nlm.ncbi.www.soap.eutils.egquery.EGQueryResultType eGQueryResult) {
        this.eGQueryResult = eGQueryResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Result)) return false;
        Result other = (Result) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.term==null && other.getTerm()==null) || 
             (this.term!=null &&
              this.term.equals(other.getTerm()))) &&
            ((this.eGQueryResult==null && other.getEGQueryResult()==null) || 
             (this.eGQueryResult!=null &&
              this.eGQueryResult.equals(other.getEGQueryResult())));
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
        if (getTerm() != null) {
            _hashCode += getTerm().hashCode();
        }
        if (getEGQueryResult() != null) {
            _hashCode += getEGQueryResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Result.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/egquery", ">Result"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("term");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/egquery", "Term"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("EGQueryResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/egquery", "eGQueryResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/egquery", "eGQueryResultType"));
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
