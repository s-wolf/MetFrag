/**
 * FieldType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.www.soap.eutils.einfo;

public class FieldType  implements java.io.Serializable {
    private java.lang.String name;

    private java.lang.String fullName;

    private java.lang.String description;

    private java.lang.String termCount;

    private java.lang.String isDate;

    private java.lang.String isNumerical;

    private java.lang.String singleToken;

    private java.lang.String hierarchy;

    private java.lang.String isHidden;

    public FieldType() {
    }

    public FieldType(
           java.lang.String name,
           java.lang.String fullName,
           java.lang.String description,
           java.lang.String termCount,
           java.lang.String isDate,
           java.lang.String isNumerical,
           java.lang.String singleToken,
           java.lang.String hierarchy,
           java.lang.String isHidden) {
           this.name = name;
           this.fullName = fullName;
           this.description = description;
           this.termCount = termCount;
           this.isDate = isDate;
           this.isNumerical = isNumerical;
           this.singleToken = singleToken;
           this.hierarchy = hierarchy;
           this.isHidden = isHidden;
    }


    /**
     * Gets the name value for this FieldType.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this FieldType.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the fullName value for this FieldType.
     * 
     * @return fullName
     */
    public java.lang.String getFullName() {
        return fullName;
    }


    /**
     * Sets the fullName value for this FieldType.
     * 
     * @param fullName
     */
    public void setFullName(java.lang.String fullName) {
        this.fullName = fullName;
    }


    /**
     * Gets the description value for this FieldType.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this FieldType.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }


    /**
     * Gets the termCount value for this FieldType.
     * 
     * @return termCount
     */
    public java.lang.String getTermCount() {
        return termCount;
    }


    /**
     * Sets the termCount value for this FieldType.
     * 
     * @param termCount
     */
    public void setTermCount(java.lang.String termCount) {
        this.termCount = termCount;
    }


    /**
     * Gets the isDate value for this FieldType.
     * 
     * @return isDate
     */
    public java.lang.String getIsDate() {
        return isDate;
    }


    /**
     * Sets the isDate value for this FieldType.
     * 
     * @param isDate
     */
    public void setIsDate(java.lang.String isDate) {
        this.isDate = isDate;
    }


    /**
     * Gets the isNumerical value for this FieldType.
     * 
     * @return isNumerical
     */
    public java.lang.String getIsNumerical() {
        return isNumerical;
    }


    /**
     * Sets the isNumerical value for this FieldType.
     * 
     * @param isNumerical
     */
    public void setIsNumerical(java.lang.String isNumerical) {
        this.isNumerical = isNumerical;
    }


    /**
     * Gets the singleToken value for this FieldType.
     * 
     * @return singleToken
     */
    public java.lang.String getSingleToken() {
        return singleToken;
    }


    /**
     * Sets the singleToken value for this FieldType.
     * 
     * @param singleToken
     */
    public void setSingleToken(java.lang.String singleToken) {
        this.singleToken = singleToken;
    }


    /**
     * Gets the hierarchy value for this FieldType.
     * 
     * @return hierarchy
     */
    public java.lang.String getHierarchy() {
        return hierarchy;
    }


    /**
     * Sets the hierarchy value for this FieldType.
     * 
     * @param hierarchy
     */
    public void setHierarchy(java.lang.String hierarchy) {
        this.hierarchy = hierarchy;
    }


    /**
     * Gets the isHidden value for this FieldType.
     * 
     * @return isHidden
     */
    public java.lang.String getIsHidden() {
        return isHidden;
    }


    /**
     * Sets the isHidden value for this FieldType.
     * 
     * @param isHidden
     */
    public void setIsHidden(java.lang.String isHidden) {
        this.isHidden = isHidden;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof FieldType)) return false;
        FieldType other = (FieldType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.fullName==null && other.getFullName()==null) || 
             (this.fullName!=null &&
              this.fullName.equals(other.getFullName()))) &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            ((this.termCount==null && other.getTermCount()==null) || 
             (this.termCount!=null &&
              this.termCount.equals(other.getTermCount()))) &&
            ((this.isDate==null && other.getIsDate()==null) || 
             (this.isDate!=null &&
              this.isDate.equals(other.getIsDate()))) &&
            ((this.isNumerical==null && other.getIsNumerical()==null) || 
             (this.isNumerical!=null &&
              this.isNumerical.equals(other.getIsNumerical()))) &&
            ((this.singleToken==null && other.getSingleToken()==null) || 
             (this.singleToken!=null &&
              this.singleToken.equals(other.getSingleToken()))) &&
            ((this.hierarchy==null && other.getHierarchy()==null) || 
             (this.hierarchy!=null &&
              this.hierarchy.equals(other.getHierarchy()))) &&
            ((this.isHidden==null && other.getIsHidden()==null) || 
             (this.isHidden!=null &&
              this.isHidden.equals(other.getIsHidden())));
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
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getFullName() != null) {
            _hashCode += getFullName().hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        if (getTermCount() != null) {
            _hashCode += getTermCount().hashCode();
        }
        if (getIsDate() != null) {
            _hashCode += getIsDate().hashCode();
        }
        if (getIsNumerical() != null) {
            _hashCode += getIsNumerical().hashCode();
        }
        if (getSingleToken() != null) {
            _hashCode += getSingleToken().hashCode();
        }
        if (getHierarchy() != null) {
            _hashCode += getHierarchy().hashCode();
        }
        if (getIsHidden() != null) {
            _hashCode += getIsHidden().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(FieldType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/einfo", "FieldType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/einfo", "Name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fullName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/einfo", "FullName"));
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
        elemField.setFieldName("termCount");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/einfo", "TermCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/einfo", "IsDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isNumerical");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/einfo", "IsNumerical"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("singleToken");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/einfo", "SingleToken"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hierarchy");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/einfo", "Hierarchy"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isHidden");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/einfo", "IsHidden"));
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
