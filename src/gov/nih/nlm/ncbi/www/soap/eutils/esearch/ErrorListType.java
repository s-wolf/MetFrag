/**
 * ErrorListType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.www.soap.eutils.esearch;

public class ErrorListType  implements java.io.Serializable {
    private java.lang.String[] phraseNotFound;

    private java.lang.String[] fieldNotFound;

    public ErrorListType() {
    }

    public ErrorListType(
           java.lang.String[] phraseNotFound,
           java.lang.String[] fieldNotFound) {
           this.phraseNotFound = phraseNotFound;
           this.fieldNotFound = fieldNotFound;
    }


    /**
     * Gets the phraseNotFound value for this ErrorListType.
     * 
     * @return phraseNotFound
     */
    public java.lang.String[] getPhraseNotFound() {
        return phraseNotFound;
    }


    /**
     * Sets the phraseNotFound value for this ErrorListType.
     * 
     * @param phraseNotFound
     */
    public void setPhraseNotFound(java.lang.String[] phraseNotFound) {
        this.phraseNotFound = phraseNotFound;
    }

    public java.lang.String getPhraseNotFound(int i) {
        return this.phraseNotFound[i];
    }

    public void setPhraseNotFound(int i, java.lang.String _value) {
        this.phraseNotFound[i] = _value;
    }


    /**
     * Gets the fieldNotFound value for this ErrorListType.
     * 
     * @return fieldNotFound
     */
    public java.lang.String[] getFieldNotFound() {
        return fieldNotFound;
    }


    /**
     * Sets the fieldNotFound value for this ErrorListType.
     * 
     * @param fieldNotFound
     */
    public void setFieldNotFound(java.lang.String[] fieldNotFound) {
        this.fieldNotFound = fieldNotFound;
    }

    public java.lang.String getFieldNotFound(int i) {
        return this.fieldNotFound[i];
    }

    public void setFieldNotFound(int i, java.lang.String _value) {
        this.fieldNotFound[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ErrorListType)) return false;
        ErrorListType other = (ErrorListType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.phraseNotFound==null && other.getPhraseNotFound()==null) || 
             (this.phraseNotFound!=null &&
              java.util.Arrays.equals(this.phraseNotFound, other.getPhraseNotFound()))) &&
            ((this.fieldNotFound==null && other.getFieldNotFound()==null) || 
             (this.fieldNotFound!=null &&
              java.util.Arrays.equals(this.fieldNotFound, other.getFieldNotFound())));
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
        if (getPhraseNotFound() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getPhraseNotFound());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getPhraseNotFound(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getFieldNotFound() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getFieldNotFound());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getFieldNotFound(), i);
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
        new org.apache.axis.description.TypeDesc(ErrorListType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "ErrorListType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("phraseNotFound");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "PhraseNotFound"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "PhraseNotFound"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fieldNotFound");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "FieldNotFound"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "FieldNotFound"));
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
