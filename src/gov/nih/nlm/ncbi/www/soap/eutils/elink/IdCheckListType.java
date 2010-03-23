/**
 * IdCheckListType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.www.soap.eutils.elink;

public class IdCheckListType  implements java.io.Serializable {
    private gov.nih.nlm.ncbi.www.soap.eutils.elink.IdType id;

    private gov.nih.nlm.ncbi.www.soap.eutils.elink.LinkInfoType[] idLinkSet;

    private java.lang.String ERROR;

    public IdCheckListType() {
    }

    public IdCheckListType(
           gov.nih.nlm.ncbi.www.soap.eutils.elink.IdType id,
           gov.nih.nlm.ncbi.www.soap.eutils.elink.LinkInfoType[] idLinkSet,
           java.lang.String ERROR) {
           this.id = id;
           this.idLinkSet = idLinkSet;
           this.ERROR = ERROR;
    }


    /**
     * Gets the id value for this IdCheckListType.
     * 
     * @return id
     */
    public gov.nih.nlm.ncbi.www.soap.eutils.elink.IdType getId() {
        return id;
    }


    /**
     * Sets the id value for this IdCheckListType.
     * 
     * @param id
     */
    public void setId(gov.nih.nlm.ncbi.www.soap.eutils.elink.IdType id) {
        this.id = id;
    }


    /**
     * Gets the idLinkSet value for this IdCheckListType.
     * 
     * @return idLinkSet
     */
    public gov.nih.nlm.ncbi.www.soap.eutils.elink.LinkInfoType[] getIdLinkSet() {
        return idLinkSet;
    }


    /**
     * Sets the idLinkSet value for this IdCheckListType.
     * 
     * @param idLinkSet
     */
    public void setIdLinkSet(gov.nih.nlm.ncbi.www.soap.eutils.elink.LinkInfoType[] idLinkSet) {
        this.idLinkSet = idLinkSet;
    }


    /**
     * Gets the ERROR value for this IdCheckListType.
     * 
     * @return ERROR
     */
    public java.lang.String getERROR() {
        return ERROR;
    }


    /**
     * Sets the ERROR value for this IdCheckListType.
     * 
     * @param ERROR
     */
    public void setERROR(java.lang.String ERROR) {
        this.ERROR = ERROR;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof IdCheckListType)) return false;
        IdCheckListType other = (IdCheckListType) obj;
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
            ((this.idLinkSet==null && other.getIdLinkSet()==null) || 
             (this.idLinkSet!=null &&
              java.util.Arrays.equals(this.idLinkSet, other.getIdLinkSet()))) &&
            ((this.ERROR==null && other.getERROR()==null) || 
             (this.ERROR!=null &&
              this.ERROR.equals(other.getERROR())));
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
        if (getIdLinkSet() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getIdLinkSet());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getIdLinkSet(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getERROR() != null) {
            _hashCode += getERROR().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(IdCheckListType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "IdCheckListType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "Id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "IdType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idLinkSet");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "IdLinkSet"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "LinkInfoType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "LinkInfo"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ERROR");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "ERROR"));
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
