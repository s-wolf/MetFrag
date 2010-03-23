/**
 * LinkSetType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.www.soap.eutils.elink;

public class LinkSetType  implements java.io.Serializable {
    private java.lang.String dbFrom;

    private java.lang.String ERROR;

    private gov.nih.nlm.ncbi.www.soap.eutils.elink.IdType[] idList;

    private gov.nih.nlm.ncbi.www.soap.eutils.elink.LinkSetDbType[] linkSetDb;

    private gov.nih.nlm.ncbi.www.soap.eutils.elink.IdUrlListType idUrlList;

    private gov.nih.nlm.ncbi.www.soap.eutils.elink.IdCheckListType idCheckList;

    public LinkSetType() {
    }

    public LinkSetType(
           java.lang.String dbFrom,
           java.lang.String ERROR,
           gov.nih.nlm.ncbi.www.soap.eutils.elink.IdType[] idList,
           gov.nih.nlm.ncbi.www.soap.eutils.elink.LinkSetDbType[] linkSetDb,
           gov.nih.nlm.ncbi.www.soap.eutils.elink.IdUrlListType idUrlList,
           gov.nih.nlm.ncbi.www.soap.eutils.elink.IdCheckListType idCheckList) {
           this.dbFrom = dbFrom;
           this.ERROR = ERROR;
           this.idList = idList;
           this.linkSetDb = linkSetDb;
           this.idUrlList = idUrlList;
           this.idCheckList = idCheckList;
    }


    /**
     * Gets the dbFrom value for this LinkSetType.
     * 
     * @return dbFrom
     */
    public java.lang.String getDbFrom() {
        return dbFrom;
    }


    /**
     * Sets the dbFrom value for this LinkSetType.
     * 
     * @param dbFrom
     */
    public void setDbFrom(java.lang.String dbFrom) {
        this.dbFrom = dbFrom;
    }


    /**
     * Gets the ERROR value for this LinkSetType.
     * 
     * @return ERROR
     */
    public java.lang.String getERROR() {
        return ERROR;
    }


    /**
     * Sets the ERROR value for this LinkSetType.
     * 
     * @param ERROR
     */
    public void setERROR(java.lang.String ERROR) {
        this.ERROR = ERROR;
    }


    /**
     * Gets the idList value for this LinkSetType.
     * 
     * @return idList
     */
    public gov.nih.nlm.ncbi.www.soap.eutils.elink.IdType[] getIdList() {
        return idList;
    }


    /**
     * Sets the idList value for this LinkSetType.
     * 
     * @param idList
     */
    public void setIdList(gov.nih.nlm.ncbi.www.soap.eutils.elink.IdType[] idList) {
        this.idList = idList;
    }


    /**
     * Gets the linkSetDb value for this LinkSetType.
     * 
     * @return linkSetDb
     */
    public gov.nih.nlm.ncbi.www.soap.eutils.elink.LinkSetDbType[] getLinkSetDb() {
        return linkSetDb;
    }


    /**
     * Sets the linkSetDb value for this LinkSetType.
     * 
     * @param linkSetDb
     */
    public void setLinkSetDb(gov.nih.nlm.ncbi.www.soap.eutils.elink.LinkSetDbType[] linkSetDb) {
        this.linkSetDb = linkSetDb;
    }

    public gov.nih.nlm.ncbi.www.soap.eutils.elink.LinkSetDbType getLinkSetDb(int i) {
        return this.linkSetDb[i];
    }

    public void setLinkSetDb(int i, gov.nih.nlm.ncbi.www.soap.eutils.elink.LinkSetDbType _value) {
        this.linkSetDb[i] = _value;
    }


    /**
     * Gets the idUrlList value for this LinkSetType.
     * 
     * @return idUrlList
     */
    public gov.nih.nlm.ncbi.www.soap.eutils.elink.IdUrlListType getIdUrlList() {
        return idUrlList;
    }


    /**
     * Sets the idUrlList value for this LinkSetType.
     * 
     * @param idUrlList
     */
    public void setIdUrlList(gov.nih.nlm.ncbi.www.soap.eutils.elink.IdUrlListType idUrlList) {
        this.idUrlList = idUrlList;
    }


    /**
     * Gets the idCheckList value for this LinkSetType.
     * 
     * @return idCheckList
     */
    public gov.nih.nlm.ncbi.www.soap.eutils.elink.IdCheckListType getIdCheckList() {
        return idCheckList;
    }


    /**
     * Sets the idCheckList value for this LinkSetType.
     * 
     * @param idCheckList
     */
    public void setIdCheckList(gov.nih.nlm.ncbi.www.soap.eutils.elink.IdCheckListType idCheckList) {
        this.idCheckList = idCheckList;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof LinkSetType)) return false;
        LinkSetType other = (LinkSetType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.dbFrom==null && other.getDbFrom()==null) || 
             (this.dbFrom!=null &&
              this.dbFrom.equals(other.getDbFrom()))) &&
            ((this.ERROR==null && other.getERROR()==null) || 
             (this.ERROR!=null &&
              this.ERROR.equals(other.getERROR()))) &&
            ((this.idList==null && other.getIdList()==null) || 
             (this.idList!=null &&
              java.util.Arrays.equals(this.idList, other.getIdList()))) &&
            ((this.linkSetDb==null && other.getLinkSetDb()==null) || 
             (this.linkSetDb!=null &&
              java.util.Arrays.equals(this.linkSetDb, other.getLinkSetDb()))) &&
            ((this.idUrlList==null && other.getIdUrlList()==null) || 
             (this.idUrlList!=null &&
              this.idUrlList.equals(other.getIdUrlList()))) &&
            ((this.idCheckList==null && other.getIdCheckList()==null) || 
             (this.idCheckList!=null &&
              this.idCheckList.equals(other.getIdCheckList())));
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
        if (getDbFrom() != null) {
            _hashCode += getDbFrom().hashCode();
        }
        if (getERROR() != null) {
            _hashCode += getERROR().hashCode();
        }
        if (getIdList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getIdList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getIdList(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getLinkSetDb() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getLinkSetDb());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getLinkSetDb(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getIdUrlList() != null) {
            _hashCode += getIdUrlList().hashCode();
        }
        if (getIdCheckList() != null) {
            _hashCode += getIdCheckList().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(LinkSetType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "LinkSetType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dbFrom");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "DbFrom"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ERROR");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "ERROR"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "IdList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "IdType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "Id"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("linkSetDb");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "LinkSetDb"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "LinkSetDbType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idUrlList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "IdUrlList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "IdUrlListType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idCheckList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "IdCheckList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "IdCheckListType"));
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
