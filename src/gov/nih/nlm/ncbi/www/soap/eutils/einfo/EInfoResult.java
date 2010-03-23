/**
 * EInfoResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.www.soap.eutils.einfo;

public class EInfoResult  implements java.io.Serializable {
    private java.lang.String ERROR;

    private gov.nih.nlm.ncbi.www.soap.eutils.einfo.DbListType dbList;

    private gov.nih.nlm.ncbi.www.soap.eutils.einfo.DbInfoType dbInfo;

    public EInfoResult() {
    }

    public EInfoResult(
           java.lang.String ERROR,
           gov.nih.nlm.ncbi.www.soap.eutils.einfo.DbListType dbList,
           gov.nih.nlm.ncbi.www.soap.eutils.einfo.DbInfoType dbInfo) {
           this.ERROR = ERROR;
           this.dbList = dbList;
           this.dbInfo = dbInfo;
    }


    /**
     * Gets the ERROR value for this EInfoResult.
     * 
     * @return ERROR
     */
    public java.lang.String getERROR() {
        return ERROR;
    }


    /**
     * Sets the ERROR value for this EInfoResult.
     * 
     * @param ERROR
     */
    public void setERROR(java.lang.String ERROR) {
        this.ERROR = ERROR;
    }


    /**
     * Gets the dbList value for this EInfoResult.
     * 
     * @return dbList
     */
    public gov.nih.nlm.ncbi.www.soap.eutils.einfo.DbListType getDbList() {
        return dbList;
    }


    /**
     * Sets the dbList value for this EInfoResult.
     * 
     * @param dbList
     */
    public void setDbList(gov.nih.nlm.ncbi.www.soap.eutils.einfo.DbListType dbList) {
        this.dbList = dbList;
    }


    /**
     * Gets the dbInfo value for this EInfoResult.
     * 
     * @return dbInfo
     */
    public gov.nih.nlm.ncbi.www.soap.eutils.einfo.DbInfoType getDbInfo() {
        return dbInfo;
    }


    /**
     * Sets the dbInfo value for this EInfoResult.
     * 
     * @param dbInfo
     */
    public void setDbInfo(gov.nih.nlm.ncbi.www.soap.eutils.einfo.DbInfoType dbInfo) {
        this.dbInfo = dbInfo;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EInfoResult)) return false;
        EInfoResult other = (EInfoResult) obj;
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
            ((this.dbList==null && other.getDbList()==null) || 
             (this.dbList!=null &&
              this.dbList.equals(other.getDbList()))) &&
            ((this.dbInfo==null && other.getDbInfo()==null) || 
             (this.dbInfo!=null &&
              this.dbInfo.equals(other.getDbInfo())));
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
        if (getDbList() != null) {
            _hashCode += getDbList().hashCode();
        }
        if (getDbInfo() != null) {
            _hashCode += getDbInfo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EInfoResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/einfo", ">eInfoResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ERROR");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/einfo", "ERROR"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dbList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/einfo", "DbList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/einfo", "DbListType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dbInfo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/einfo", "DbInfo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/einfo", "DbInfoType"));
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
