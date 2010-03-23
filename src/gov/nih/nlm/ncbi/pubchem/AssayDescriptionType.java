/**
 * AssayDescriptionType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.pubchem;

public class AssayDescriptionType  implements java.io.Serializable {
    private java.lang.String name;

    private java.lang.String[] description;

    private java.lang.String[] protocol;

    private java.lang.String[] comment;

    private int numberOfTIDs;

    private boolean hasScore;

    private java.lang.String method;

    private gov.nih.nlm.ncbi.pubchem.AssayTargetType[] targets;

    public AssayDescriptionType() {
    }

    public AssayDescriptionType(
           java.lang.String name,
           java.lang.String[] description,
           java.lang.String[] protocol,
           java.lang.String[] comment,
           int numberOfTIDs,
           boolean hasScore,
           java.lang.String method,
           gov.nih.nlm.ncbi.pubchem.AssayTargetType[] targets) {
           this.name = name;
           this.description = description;
           this.protocol = protocol;
           this.comment = comment;
           this.numberOfTIDs = numberOfTIDs;
           this.hasScore = hasScore;
           this.method = method;
           this.targets = targets;
    }


    /**
     * Gets the name value for this AssayDescriptionType.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this AssayDescriptionType.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the description value for this AssayDescriptionType.
     * 
     * @return description
     */
    public java.lang.String[] getDescription() {
        return description;
    }


    /**
     * Sets the description value for this AssayDescriptionType.
     * 
     * @param description
     */
    public void setDescription(java.lang.String[] description) {
        this.description = description;
    }


    /**
     * Gets the protocol value for this AssayDescriptionType.
     * 
     * @return protocol
     */
    public java.lang.String[] getProtocol() {
        return protocol;
    }


    /**
     * Sets the protocol value for this AssayDescriptionType.
     * 
     * @param protocol
     */
    public void setProtocol(java.lang.String[] protocol) {
        this.protocol = protocol;
    }


    /**
     * Gets the comment value for this AssayDescriptionType.
     * 
     * @return comment
     */
    public java.lang.String[] getComment() {
        return comment;
    }


    /**
     * Sets the comment value for this AssayDescriptionType.
     * 
     * @param comment
     */
    public void setComment(java.lang.String[] comment) {
        this.comment = comment;
    }


    /**
     * Gets the numberOfTIDs value for this AssayDescriptionType.
     * 
     * @return numberOfTIDs
     */
    public int getNumberOfTIDs() {
        return numberOfTIDs;
    }


    /**
     * Sets the numberOfTIDs value for this AssayDescriptionType.
     * 
     * @param numberOfTIDs
     */
    public void setNumberOfTIDs(int numberOfTIDs) {
        this.numberOfTIDs = numberOfTIDs;
    }


    /**
     * Gets the hasScore value for this AssayDescriptionType.
     * 
     * @return hasScore
     */
    public boolean isHasScore() {
        return hasScore;
    }


    /**
     * Sets the hasScore value for this AssayDescriptionType.
     * 
     * @param hasScore
     */
    public void setHasScore(boolean hasScore) {
        this.hasScore = hasScore;
    }


    /**
     * Gets the method value for this AssayDescriptionType.
     * 
     * @return method
     */
    public java.lang.String getMethod() {
        return method;
    }


    /**
     * Sets the method value for this AssayDescriptionType.
     * 
     * @param method
     */
    public void setMethod(java.lang.String method) {
        this.method = method;
    }


    /**
     * Gets the targets value for this AssayDescriptionType.
     * 
     * @return targets
     */
    public gov.nih.nlm.ncbi.pubchem.AssayTargetType[] getTargets() {
        return targets;
    }


    /**
     * Sets the targets value for this AssayDescriptionType.
     * 
     * @param targets
     */
    public void setTargets(gov.nih.nlm.ncbi.pubchem.AssayTargetType[] targets) {
        this.targets = targets;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AssayDescriptionType)) return false;
        AssayDescriptionType other = (AssayDescriptionType) obj;
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
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              java.util.Arrays.equals(this.description, other.getDescription()))) &&
            ((this.protocol==null && other.getProtocol()==null) || 
             (this.protocol!=null &&
              java.util.Arrays.equals(this.protocol, other.getProtocol()))) &&
            ((this.comment==null && other.getComment()==null) || 
             (this.comment!=null &&
              java.util.Arrays.equals(this.comment, other.getComment()))) &&
            this.numberOfTIDs == other.getNumberOfTIDs() &&
            this.hasScore == other.isHasScore() &&
            ((this.method==null && other.getMethod()==null) || 
             (this.method!=null &&
              this.method.equals(other.getMethod()))) &&
            ((this.targets==null && other.getTargets()==null) || 
             (this.targets!=null &&
              java.util.Arrays.equals(this.targets, other.getTargets())));
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
        if (getProtocol() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getProtocol());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getProtocol(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getComment() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getComment());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getComment(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += getNumberOfTIDs();
        _hashCode += (isHasScore() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getMethod() != null) {
            _hashCode += getMethod().hashCode();
        }
        if (getTargets() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTargets());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTargets(), i);
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
        new org.apache.axis.description.TypeDesc(AssayDescriptionType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "AssayDescriptionType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "Name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
        elemField.setFieldName("protocol");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "Protocol"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("comment");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "Comment"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numberOfTIDs");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "NumberOfTIDs"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("hasScore");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "HasScore"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("method");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "Method"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("targets");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "Targets"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "AssayTargetType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "Target"));
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
