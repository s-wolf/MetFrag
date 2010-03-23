/**
 * TermSetType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.www.soap.eutils.esearch;

public class TermSetType  implements java.io.Serializable {
    private java.lang.String term;

    private java.lang.String field;

    private java.lang.String count;

    private java.lang.String explode;

    public TermSetType() {
    }

    public TermSetType(
           java.lang.String term,
           java.lang.String field,
           java.lang.String count,
           java.lang.String explode) {
           this.term = term;
           this.field = field;
           this.count = count;
           this.explode = explode;
    }


    /**
     * Gets the term value for this TermSetType.
     * 
     * @return term
     */
    public java.lang.String getTerm() {
        return term;
    }


    /**
     * Sets the term value for this TermSetType.
     * 
     * @param term
     */
    public void setTerm(java.lang.String term) {
        this.term = term;
    }


    /**
     * Gets the field value for this TermSetType.
     * 
     * @return field
     */
    public java.lang.String getField() {
        return field;
    }


    /**
     * Sets the field value for this TermSetType.
     * 
     * @param field
     */
    public void setField(java.lang.String field) {
        this.field = field;
    }


    /**
     * Gets the count value for this TermSetType.
     * 
     * @return count
     */
    public java.lang.String getCount() {
        return count;
    }


    /**
     * Sets the count value for this TermSetType.
     * 
     * @param count
     */
    public void setCount(java.lang.String count) {
        this.count = count;
    }


    /**
     * Gets the explode value for this TermSetType.
     * 
     * @return explode
     */
    public java.lang.String getExplode() {
        return explode;
    }


    /**
     * Sets the explode value for this TermSetType.
     * 
     * @param explode
     */
    public void setExplode(java.lang.String explode) {
        this.explode = explode;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TermSetType)) return false;
        TermSetType other = (TermSetType) obj;
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
            ((this.field==null && other.getField()==null) || 
             (this.field!=null &&
              this.field.equals(other.getField()))) &&
            ((this.count==null && other.getCount()==null) || 
             (this.count!=null &&
              this.count.equals(other.getCount()))) &&
            ((this.explode==null && other.getExplode()==null) || 
             (this.explode!=null &&
              this.explode.equals(other.getExplode())));
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
        if (getField() != null) {
            _hashCode += getField().hashCode();
        }
        if (getCount() != null) {
            _hashCode += getCount().hashCode();
        }
        if (getExplode() != null) {
            _hashCode += getExplode().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TermSetType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "TermSetType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("term");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "Term"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("field");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "Field"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("count");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "Count"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("explode");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "Explode"));
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
