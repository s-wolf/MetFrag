/**
 * WarningListType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.www.soap.eutils.esearch;

public class WarningListType  implements java.io.Serializable {
    private java.lang.String[] phraseIgnored;

    private java.lang.String[] quotedPhraseNotFound;

    private java.lang.String[] outputMessage;

    public WarningListType() {
    }

    public WarningListType(
           java.lang.String[] phraseIgnored,
           java.lang.String[] quotedPhraseNotFound,
           java.lang.String[] outputMessage) {
           this.phraseIgnored = phraseIgnored;
           this.quotedPhraseNotFound = quotedPhraseNotFound;
           this.outputMessage = outputMessage;
    }


    /**
     * Gets the phraseIgnored value for this WarningListType.
     * 
     * @return phraseIgnored
     */
    public java.lang.String[] getPhraseIgnored() {
        return phraseIgnored;
    }


    /**
     * Sets the phraseIgnored value for this WarningListType.
     * 
     * @param phraseIgnored
     */
    public void setPhraseIgnored(java.lang.String[] phraseIgnored) {
        this.phraseIgnored = phraseIgnored;
    }

    public java.lang.String getPhraseIgnored(int i) {
        return this.phraseIgnored[i];
    }

    public void setPhraseIgnored(int i, java.lang.String _value) {
        this.phraseIgnored[i] = _value;
    }


    /**
     * Gets the quotedPhraseNotFound value for this WarningListType.
     * 
     * @return quotedPhraseNotFound
     */
    public java.lang.String[] getQuotedPhraseNotFound() {
        return quotedPhraseNotFound;
    }


    /**
     * Sets the quotedPhraseNotFound value for this WarningListType.
     * 
     * @param quotedPhraseNotFound
     */
    public void setQuotedPhraseNotFound(java.lang.String[] quotedPhraseNotFound) {
        this.quotedPhraseNotFound = quotedPhraseNotFound;
    }

    public java.lang.String getQuotedPhraseNotFound(int i) {
        return this.quotedPhraseNotFound[i];
    }

    public void setQuotedPhraseNotFound(int i, java.lang.String _value) {
        this.quotedPhraseNotFound[i] = _value;
    }


    /**
     * Gets the outputMessage value for this WarningListType.
     * 
     * @return outputMessage
     */
    public java.lang.String[] getOutputMessage() {
        return outputMessage;
    }


    /**
     * Sets the outputMessage value for this WarningListType.
     * 
     * @param outputMessage
     */
    public void setOutputMessage(java.lang.String[] outputMessage) {
        this.outputMessage = outputMessage;
    }

    public java.lang.String getOutputMessage(int i) {
        return this.outputMessage[i];
    }

    public void setOutputMessage(int i, java.lang.String _value) {
        this.outputMessage[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof WarningListType)) return false;
        WarningListType other = (WarningListType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.phraseIgnored==null && other.getPhraseIgnored()==null) || 
             (this.phraseIgnored!=null &&
              java.util.Arrays.equals(this.phraseIgnored, other.getPhraseIgnored()))) &&
            ((this.quotedPhraseNotFound==null && other.getQuotedPhraseNotFound()==null) || 
             (this.quotedPhraseNotFound!=null &&
              java.util.Arrays.equals(this.quotedPhraseNotFound, other.getQuotedPhraseNotFound()))) &&
            ((this.outputMessage==null && other.getOutputMessage()==null) || 
             (this.outputMessage!=null &&
              java.util.Arrays.equals(this.outputMessage, other.getOutputMessage())));
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
        if (getPhraseIgnored() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getPhraseIgnored());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getPhraseIgnored(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getQuotedPhraseNotFound() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getQuotedPhraseNotFound());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getQuotedPhraseNotFound(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getOutputMessage() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getOutputMessage());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getOutputMessage(), i);
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
        new org.apache.axis.description.TypeDesc(WarningListType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "WarningListType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("phraseIgnored");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "PhraseIgnored"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "PhraseIgnored"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("quotedPhraseNotFound");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "QuotedPhraseNotFound"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "QuotedPhraseNotFound"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("outputMessage");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "OutputMessage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "OutputMessage"));
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
