/**
 * DbInfoType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.www.soap.eutils.einfo;

public class DbInfoType  implements java.io.Serializable {
    private java.lang.String dbName;

    private java.lang.String menuName;

    private java.lang.String description;

    private java.lang.String count;

    private java.lang.String lastUpdate;

    private gov.nih.nlm.ncbi.www.soap.eutils.einfo.FieldType[] fieldList;

    private gov.nih.nlm.ncbi.www.soap.eutils.einfo.LinkType[] linkList;

    public DbInfoType() {
    }

    public DbInfoType(
           java.lang.String dbName,
           java.lang.String menuName,
           java.lang.String description,
           java.lang.String count,
           java.lang.String lastUpdate,
           gov.nih.nlm.ncbi.www.soap.eutils.einfo.FieldType[] fieldList,
           gov.nih.nlm.ncbi.www.soap.eutils.einfo.LinkType[] linkList) {
           this.dbName = dbName;
           this.menuName = menuName;
           this.description = description;
           this.count = count;
           this.lastUpdate = lastUpdate;
           this.fieldList = fieldList;
           this.linkList = linkList;
    }


    /**
     * Gets the dbName value for this DbInfoType.
     * 
     * @return dbName
     */
    public java.lang.String getDbName() {
        return dbName;
    }


    /**
     * Sets the dbName value for this DbInfoType.
     * 
     * @param dbName
     */
    public void setDbName(java.lang.String dbName) {
        this.dbName = dbName;
    }


    /**
     * Gets the menuName value for this DbInfoType.
     * 
     * @return menuName
     */
    public java.lang.String getMenuName() {
        return menuName;
    }


    /**
     * Sets the menuName value for this DbInfoType.
     * 
     * @param menuName
     */
    public void setMenuName(java.lang.String menuName) {
        this.menuName = menuName;
    }


    /**
     * Gets the description value for this DbInfoType.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this DbInfoType.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the count value for this DbInfoType.
     * 
     * @return count
     */
    public java.lang.String getCount() {
        return count;
    }


    /**
     * Sets the count value for this DbInfoType.
     * 
     * @param count
     */
    public void setCount(java.lang.String count) {
        this.count = count;
    }


    /**
     * Gets the lastUpdate value for this DbInfoType.
     * 
     * @return lastUpdate
     */
    public java.lang.String getLastUpdate() {
        return lastUpdate;
    }


    /**
     * Sets the lastUpdate value for this DbInfoType.
     * 
     * @param lastUpdate
     */
    public void setLastUpdate(java.lang.String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }


    /**
     * Gets the fieldList value for this DbInfoType.
     * 
     * @return fieldList
     */
    public gov.nih.nlm.ncbi.www.soap.eutils.einfo.FieldType[] getFieldList() {
        return fieldList;
    }


    /**
     * Sets the fieldList value for this DbInfoType.
     * 
     * @param fieldList
     */
    public void setFieldList(gov.nih.nlm.ncbi.www.soap.eutils.einfo.FieldType[] fieldList) {
        this.fieldList = fieldList;
    }


    /**
     * Gets the linkList value for this DbInfoType.
     * 
     * @return linkList
     */
    public gov.nih.nlm.ncbi.www.soap.eutils.einfo.LinkType[] getLinkList() {
        return linkList;
    }


    /**
     * Sets the linkList value for this DbInfoType.
     * 
     * @param linkList
     */
    public void setLinkList(gov.nih.nlm.ncbi.www.soap.eutils.einfo.LinkType[] linkList) {
        this.linkList = linkList;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DbInfoType)) return false;
        DbInfoType other = (DbInfoType) obj;
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
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            ((this.count==null && other.getCount()==null) || 
             (this.count!=null &&
              this.count.equals(other.getCount()))) &&
            ((this.lastUpdate==null && other.getLastUpdate()==null) || 
             (this.lastUpdate!=null &&
              this.lastUpdate.equals(other.getLastUpdate()))) &&
            ((this.fieldList==null && other.getFieldList()==null) || 
             (this.fieldList!=null &&
              java.util.Arrays.equals(this.fieldList, other.getFieldList()))) &&
            ((this.linkList==null && other.getLinkList()==null) || 
             (this.linkList!=null &&
              java.util.Arrays.equals(this.linkList, other.getLinkList())));
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
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getCount() != null) {
            _hashCode += getCount().hashCode();
        }
        if (getLastUpdate() != null) {
            _hashCode += getLastUpdate().hashCode();
        }
        if (getFieldList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getFieldList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getFieldList(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getLinkList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getLinkList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getLinkList(), i);
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
        new org.apache.axis.description.TypeDesc(DbInfoType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/einfo", "DbInfoType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dbName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/einfo", "DbName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("menuName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/einfo", "MenuName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/einfo", "Description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("count");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/einfo", "Count"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("lastUpdate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/einfo", "LastUpdate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fieldList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/einfo", "FieldList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/einfo", "FieldType"));
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/einfo", "Field"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("linkList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/einfo", "LinkList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/einfo", "LinkType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/einfo", "Link"));
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
