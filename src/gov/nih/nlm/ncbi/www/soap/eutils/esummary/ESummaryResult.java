/**
 * ESummaryResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.www.soap.eutils.esummary;

public class ESummaryResult  implements java.io.Serializable {
    private java.lang.String ERROR;

    private gov.nih.nlm.ncbi.www.soap.eutils.esummary.DocSumType[] docSum;

    public ESummaryResult() {
    }

    public ESummaryResult(
           java.lang.String ERROR,
           gov.nih.nlm.ncbi.www.soap.eutils.esummary.DocSumType[] docSum) {
           this.ERROR = ERROR;
           this.docSum = docSum;
    }


    /**
     * Gets the ERROR value for this ESummaryResult.
     * 
     * @return ERROR
     */
    public java.lang.String getERROR() {
        return ERROR;
    }


    /**
     * Sets the ERROR value for this ESummaryResult.
     * 
     * @param ERROR
     */
    public void setERROR(java.lang.String ERROR) {
        this.ERROR = ERROR;
    }


    /**
     * Gets the docSum value for this ESummaryResult.
     * 
     * @return docSum
     */
    public gov.nih.nlm.ncbi.www.soap.eutils.esummary.DocSumType[] getDocSum() {
        return docSum;
    }


    /**
     * Sets the docSum value for this ESummaryResult.
     * 
     * @param docSum
     */
    public void setDocSum(gov.nih.nlm.ncbi.www.soap.eutils.esummary.DocSumType[] docSum) {
        this.docSum = docSum;
    }

    public gov.nih.nlm.ncbi.www.soap.eutils.esummary.DocSumType getDocSum(int i) {
        return this.docSum[i];
    }

    public void setDocSum(int i, gov.nih.nlm.ncbi.www.soap.eutils.esummary.DocSumType _value) {
        this.docSum[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ESummaryResult)) return false;
        ESummaryResult other = (ESummaryResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.ERROR==null && other.getERROR()==null) || 
             (this.ERROR!=null &&
              this.ERROR.equals(other.getERROR()))) &&
            ((this.docSum==null && other.getDocSum()==null) || 
             (this.docSum!=null &&
              java.util.Arrays.equals(this.docSum, other.getDocSum())));
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
        if (getERROR() != null) {
            _hashCode += getERROR().hashCode();
        }
        if (getDocSum() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDocSum());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDocSum(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ESummaryResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esummary", ">eSummaryResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ERROR");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esummary", "ERROR"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("docSum");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esummary", "DocSum"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esummary", "DocSumType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
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
