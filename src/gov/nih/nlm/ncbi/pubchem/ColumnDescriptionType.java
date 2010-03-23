/**
 * ColumnDescriptionType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.pubchem;

public class ColumnDescriptionType  implements java.io.Serializable {
    private gov.nih.nlm.ncbi.pubchem.HeadingType heading;

    private java.lang.Integer TID;

    private java.lang.String name;

    private java.lang.String[] description;

    private java.lang.String type;

    private java.lang.String unit;

    private gov.nih.nlm.ncbi.pubchem.TestedConcentrationType testedConcentration;

    private java.lang.Boolean activeConcentration;

    public ColumnDescriptionType() {
    }

    public ColumnDescriptionType(
           gov.nih.nlm.ncbi.pubchem.HeadingType heading,
           java.lang.Integer TID,
           java.lang.String name,
           java.lang.String[] description,
           java.lang.String type,
           java.lang.String unit,
           gov.nih.nlm.ncbi.pubchem.TestedConcentrationType testedConcentration,
           java.lang.Boolean activeConcentration) {
           this.heading = heading;
           this.TID = TID;
           this.name = name;
           this.description = description;
           this.type = type;
           this.unit = unit;
           this.testedConcentration = testedConcentration;
           this.activeConcentration = activeConcentration;
    }


    /**
     * Gets the heading value for this ColumnDescriptionType.
     * 
     * @return heading
     */
    public gov.nih.nlm.ncbi.pubchem.HeadingType getHeading() {
        return heading;
    }


    /**
     * Sets the heading value for this ColumnDescriptionType.
     * 
     * @param heading
     */
    public void setHeading(gov.nih.nlm.ncbi.pubchem.HeadingType heading) {
        this.heading = heading;
    }


    /**
     * Gets the TID value for this ColumnDescriptionType.
     * 
     * @return TID
     */
    public java.lang.Integer getTID() {
        return TID;
    }


    /**
     * Sets the TID value for this ColumnDescriptionType.
     * 
     * @param TID
     */
    public void setTID(java.lang.Integer TID) {
        this.TID = TID;
    }


    /**
     * Gets the name value for this ColumnDescriptionType.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this ColumnDescriptionType.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the description value for this ColumnDescriptionType.
     * 
     * @return description
     */
    public java.lang.String[] getDescription() {
        return description;
    }


    /**
     * Sets the description value for this ColumnDescriptionType.
     * 
     * @param description
     */
    public void setDescription(java.lang.String[] description) {
        this.description = description;
    }


    /**
     * Gets the type value for this ColumnDescriptionType.
     * 
     * @return type
     */
    public java.lang.String getType() {
        return type;
    }


    /**
     * Sets the type value for this ColumnDescriptionType.
     * 
     * @param type
     */
    public void setType(java.lang.String type) {
        this.type = type;
    }


    /**
     * Gets the unit value for this ColumnDescriptionType.
     * 
     * @return unit
     */
    public java.lang.String getUnit() {
        return unit;
    }


    /**
     * Sets the unit value for this ColumnDescriptionType.
     * 
     * @param unit
     */
    public void setUnit(java.lang.String unit) {
        this.unit = unit;
    }


    /**
     * Gets the testedConcentration value for this ColumnDescriptionType.
     * 
     * @return testedConcentration
     */
    public gov.nih.nlm.ncbi.pubchem.TestedConcentrationType getTestedConcentration() {
        return testedConcentration;
    }


    /**
     * Sets the testedConcentration value for this ColumnDescriptionType.
     * 
     * @param testedConcentration
     */
    public void setTestedConcentration(gov.nih.nlm.ncbi.pubchem.TestedConcentrationType testedConcentration) {
        this.testedConcentration = testedConcentration;
    }


    /**
     * Gets the activeConcentration value for this ColumnDescriptionType.
     * 
     * @return activeConcentration
     */
    public java.lang.Boolean getActiveConcentration() {
        return activeConcentration;
    }


    /**
     * Sets the activeConcentration value for this ColumnDescriptionType.
     * 
     * @param activeConcentration
     */
    public void setActiveConcentration(java.lang.Boolean activeConcentration) {
        this.activeConcentration = activeConcentration;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ColumnDescriptionType)) return false;
        ColumnDescriptionType other = (ColumnDescriptionType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.heading==null && other.getHeading()==null) || 
             (this.heading!=null &&
              this.heading.equals(other.getHeading()))) &&
            ((this.TID==null && other.getTID()==null) || 
             (this.TID!=null &&
              this.TID.equals(other.getTID()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              java.util.Arrays.equals(this.description, other.getDescription()))) &&
            ((this.type==null && other.getType()==null) || 
             (this.type!=null &&
              this.type.equals(other.getType()))) &&
            ((this.unit==null && other.getUnit()==null) || 
             (this.unit!=null &&
              this.unit.equals(other.getUnit()))) &&
            ((this.testedConcentration==null && other.getTestedConcentration()==null) || 
             (this.testedConcentration!=null &&
              this.testedConcentration.equals(other.getTestedConcentration()))) &&
            ((this.activeConcentration==null && other.getActiveConcentration()==null) || 
             (this.activeConcentration!=null &&
              this.activeConcentration.equals(other.getActiveConcentration())));
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
        if (getHeading() != null) {
            _hashCode += getHeading().hashCode();
        }
        if (getTID() != null) {
            _hashCode += getTID().hashCode();
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getDescription() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getDescription());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDescription(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getType() != null) {
            _hashCode += getType().hashCode();
        }
        if (getUnit() != null) {
            _hashCode += getUnit().hashCode();
        }
        if (getTestedConcentration() != null) {
            _hashCode += getTestedConcentration().hashCode();
        }
        if (getActiveConcentration() != null) {
            _hashCode += getActiveConcentration().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ColumnDescriptionType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "ColumnDescriptionType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("heading");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "Heading"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "HeadingType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("TID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "TID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "Name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "Description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("type");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "Type"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("unit");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "Unit"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("testedConcentration");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "TestedConcentration"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "TestedConcentrationType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("activeConcentration");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "ActiveConcentration"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
