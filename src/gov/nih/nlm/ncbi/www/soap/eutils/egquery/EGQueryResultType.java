/**
 * EGQueryResultType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.www.soap.eutils.egquery;

public class EGQueryResultType  implements java.io.Serializable {
    private java.lang.String ERROR;

    private gov.nih.nlm.ncbi.www.soap.eutils.egquery.ResultItemType[] resultItem;

    public EGQueryResultType() {
    }

    public EGQueryResultType(
           java.lang.String ERROR,
           gov.nih.nlm.ncbi.www.soap.eutils.egquery.ResultItemType[] resultItem) {
           this.ERROR = ERROR;
           this.resultItem = resultItem;
    }


    /**
     * Gets the ERROR value for this EGQueryResultType.
     * 
     * @return ERROR
     */
    public java.lang.String getERROR() {
        return ERROR;
    }


    /**
     * Sets the ERROR value for this EGQueryResultType.
     * 
     * @param ERROR
     */
    public void setERROR(java.lang.String ERROR) {
        this.ERROR = ERROR;
    }


    /**
     * Gets the resultItem value for this EGQueryResultType.
     * 
     * @return resultItem
     */
    public gov.nih.nlm.ncbi.www.soap.eutils.egquery.ResultItemType[] getResultItem() {
        return resultItem;
    }


    /**
     * Sets the resultItem value for this EGQueryResultType.
     * 
     * @param resultItem
     */
    public void setResultItem(gov.nih.nlm.ncbi.www.soap.eutils.egquery.ResultItemType[] resultItem) {
        this.resultItem = resultItem;
    }

    public gov.nih.nlm.ncbi.www.soap.eutils.egquery.ResultItemType getResultItem(int i) {
        return this.resultItem[i];
    }

    public void setResultItem(int i, gov.nih.nlm.ncbi.www.soap.eutils.egquery.ResultItemType _value) {
        this.resultItem[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EGQueryResultType)) return false;
        EGQueryResultType other = (EGQueryResultType) obj;
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
            ((this.resultItem==null && other.getResultItem()==null) || 
             (this.resultItem!=null &&
              java.util.Arrays.equals(this.resultItem, other.getResultItem())));
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
        if (getResultItem() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getResultItem());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getResultItem(), i);
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
        new org.apache.axis.description.TypeDesc(EGQueryResultType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/egquery", "eGQueryResultType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ERROR");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/egquery", "ERROR"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resultItem");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/egquery", "ResultItem"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/egquery", "ResultItemType"));
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
