/**
 * ELinkResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.www.soap.eutils.elink;

public class ELinkResult  implements java.io.Serializable {
    private java.lang.String ERROR;

    private gov.nih.nlm.ncbi.www.soap.eutils.elink.LinkSetType[] linkSet;

    public ELinkResult() {
    }

    public ELinkResult(
           java.lang.String ERROR,
           gov.nih.nlm.ncbi.www.soap.eutils.elink.LinkSetType[] linkSet) {
           this.ERROR = ERROR;
           this.linkSet = linkSet;
    }


    /**
     * Gets the ERROR value for this ELinkResult.
     * 
     * @return ERROR
     */
    public java.lang.String getERROR() {
        return ERROR;
    }


    /**
     * Sets the ERROR value for this ELinkResult.
     * 
     * @param ERROR
     */
    public void setERROR(java.lang.String ERROR) {
        this.ERROR = ERROR;
    }


    /**
     * Gets the linkSet value for this ELinkResult.
     * 
     * @return linkSet
     */
    public gov.nih.nlm.ncbi.www.soap.eutils.elink.LinkSetType[] getLinkSet() {
        return linkSet;
    }


    /**
     * Sets the linkSet value for this ELinkResult.
     * 
     * @param linkSet
     */
    public void setLinkSet(gov.nih.nlm.ncbi.www.soap.eutils.elink.LinkSetType[] linkSet) {
        this.linkSet = linkSet;
    }

    public gov.nih.nlm.ncbi.www.soap.eutils.elink.LinkSetType getLinkSet(int i) {
        return this.linkSet[i];
    }

    public void setLinkSet(int i, gov.nih.nlm.ncbi.www.soap.eutils.elink.LinkSetType _value) {
        this.linkSet[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ELinkResult)) return false;
        ELinkResult other = (ELinkResult) obj;
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
            ((this.linkSet==null && other.getLinkSet()==null) || 
             (this.linkSet!=null &&
              java.util.Arrays.equals(this.linkSet, other.getLinkSet())));
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
        if (getLinkSet() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getLinkSet());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getLinkSet(), i);
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
        new org.apache.axis.description.TypeDesc(ELinkResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", ">eLinkResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ERROR");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "ERROR"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("linkSet");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "LinkSet"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "LinkSetType"));
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
