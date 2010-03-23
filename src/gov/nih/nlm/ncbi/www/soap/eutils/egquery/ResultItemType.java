/**
 * ResultItemType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.www.soap.eutils.egquery;

public class ResultItemType  implements java.io.Serializable {
    private java.lang.String dbName;

    private java.lang.String menuName;

    private java.lang.String count;

    private java.lang.String status;

    public ResultItemType() {
    }

    public ResultItemType(
           java.lang.String dbName,
           java.lang.String menuName,
           java.lang.String count,
           java.lang.String status) {
           this.dbName = dbName;
           this.menuName = menuName;
           this.count = count;
           this.status = status;
    }


    /**
     * Gets the dbName value for this ResultItemType.
     * 
     * @return dbName
     */
    public java.lang.String getDbName() {
        return dbName;
    }


    /**
     * Sets the dbName value for this ResultItemType.
     * 
     * @param dbName
     */
    public void setDbName(java.lang.String dbName) {
        this.dbName = dbName;
    }


    /**
     * Gets the menuName value for this ResultItemType.
     * 
     * @return menuName
     */
    public java.lang.String getMenuName() {
        return menuName;
    }


    /**
     * Sets the menuName value for this ResultItemType.
     * 
     * @param menuName
     */
    public void setMenuName(java.lang.String menuName) {
        this.menuName = menuName;
    }


    /**
     * Gets the count value for this ResultItemType.
     * 
     * @return count
     */
    public java.lang.String getCount() {
        return count;
    }


    /**
     * Sets the count value for this ResultItemType.
     * 
     * @param count
     */
    public void setCount(java.lang.String count) {
        this.count = count;
    }


    /**
     * Gets the status value for this ResultItemType.
     * 
     * @return status
     */
    public java.lang.String getStatus() {
        return status;
    }


    /**
     * Sets the status value for this ResultItemType.
     * 
     * @param status
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ResultItemType)) return false;
        ResultItemType other = (ResultItemType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.dbName==null && other.getDbName()==null) || 
             (this.dbName!=null &&
              this.dbName.equals(other.getDbName()))) &&
            ((this.menuName==null && other.getMenuName()==null) || 
             (this.menuName!=null &&
              this.menuName.equals(other.getMenuName()))) &&
            ((this.count==null && other.getCount()==null) || 
             (this.count!=null &&
              this.count.equals(other.getCount()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus())));
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
        if (getDbName() != null) {
            _hashCode += getDbName().hashCode();
        }
        if (getMenuName() != null) {
            _hashCode += getMenuName().hashCode();
        }
        if (getCount() != null) {
            _hashCode += getCount().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ResultItemType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/egquery", "ResultItemType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dbName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/egquery", "DbName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("menuName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/egquery", "MenuName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("count");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/egquery", "Count"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/egquery", "Status"));
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
