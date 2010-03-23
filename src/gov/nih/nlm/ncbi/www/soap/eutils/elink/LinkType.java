/**
 * LinkType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.www.soap.eutils.elink;

public class LinkType  implements java.io.Serializable {
    private gov.nih.nlm.ncbi.www.soap.eutils.elink.IdType id;

    private java.lang.String score;

    public LinkType() {
    }

    public LinkType(
           gov.nih.nlm.ncbi.www.soap.eutils.elink.IdType id,
           java.lang.String score) {
           this.id = id;
           this.score = score;
    }


    /**
     * Gets the id value for this LinkType.
     * 
     * @return id
     */
    public gov.nih.nlm.ncbi.www.soap.eutils.elink.IdType getId() {
        return id;
    }


    /**
     * Sets the id value for this LinkType.
     * 
     * @param id
     */
    public void setId(gov.nih.nlm.ncbi.www.soap.eutils.elink.IdType id) {
        this.id = id;
    }


    /**
     * Gets the score value for this LinkType.
     * 
     * @return score
     */
    public java.lang.String getScore() {
        return score;
    }


    /**
     * Sets the score value for this LinkType.
     * 
     * @param score
     */
    public void setScore(java.lang.String score) {
        this.score = score;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof LinkType)) return false;
        LinkType other = (LinkType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            ((this.score==null && other.getScore()==null) || 
             (this.score!=null &&
              this.score.equals(other.getScore())));
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
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getScore() != null) {
            _hashCode += getScore().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(LinkType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "LinkType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "Id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "IdType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("score");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "Score"));
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
