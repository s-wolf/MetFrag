/**
 * SpelledQuery.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.www.soap.eutils.espell;

public class SpelledQuery  implements java.io.Serializable {
    private java.lang.String replaced;

    private java.lang.String original;

    public SpelledQuery() {
    }

    public SpelledQuery(
           java.lang.String replaced,
           java.lang.String original) {
           this.replaced = replaced;
           this.original = original;
    }


    /**
     * Gets the replaced value for this SpelledQuery.
     * 
     * @return replaced
     */
    public java.lang.String getReplaced() {
        return replaced;
    }


    /**
     * Sets the replaced value for this SpelledQuery.
     * 
     * @param replaced
     */
    public void setReplaced(java.lang.String replaced) {
        this.replaced = replaced;
    }


    /**
     * Gets the original value for this SpelledQuery.
     * 
     * @return original
     */
    public java.lang.String getOriginal() {
        return original;
    }


    /**
     * Sets the original value for this SpelledQuery.
     * 
     * @param original
     */
    public void setOriginal(java.lang.String original) {
        this.original = original;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SpelledQuery)) return false;
        SpelledQuery other = (SpelledQuery) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.replaced==null && other.getReplaced()==null) || 
             (this.replaced!=null &&
              this.replaced.equals(other.getReplaced()))) &&
            ((this.original==null && other.getOriginal()==null) || 
             (this.original!=null &&
              this.original.equals(other.getOriginal())));
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
        if (getReplaced() != null) {
            _hashCode += getReplaced().hashCode();
        }
        if (getOriginal() != null) {
            _hashCode += getOriginal().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SpelledQuery.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/espell", ">SpelledQuery"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("replaced");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/espell", "Replaced"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("original");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/espell", "Original"));
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
