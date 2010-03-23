/**
 * ItemType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.www.soap.eutils.esummary;

public class ItemType  implements java.io.Serializable {
    private gov.nih.nlm.ncbi.www.soap.eutils.esummary.ItemType[] item;

    private java.lang.String itemContent;

    private java.lang.String name;  // attribute

    private gov.nih.nlm.ncbi.www.soap.eutils.esummary.ItemTypeType type;  // attribute

    public ItemType() {
    }

    public ItemType(
           gov.nih.nlm.ncbi.www.soap.eutils.esummary.ItemType[] item,
           java.lang.String itemContent,
           java.lang.String name,
           gov.nih.nlm.ncbi.www.soap.eutils.esummary.ItemTypeType type) {
           this.item = item;
           this.itemContent = itemContent;
           this.name = name;
           this.type = type;
    }


    /**
     * Gets the item value for this ItemType.
     * 
     * @return item
     */
    public gov.nih.nlm.ncbi.www.soap.eutils.esummary.ItemType[] getItem() {
        return item;
    }


    /**
     * Sets the item value for this ItemType.
     * 
     * @param item
     */
    public void setItem(gov.nih.nlm.ncbi.www.soap.eutils.esummary.ItemType[] item) {
        this.item = item;
    }

    public gov.nih.nlm.ncbi.www.soap.eutils.esummary.ItemType getItem(int i) {
        return this.item[i];
    }

    public void setItem(int i, gov.nih.nlm.ncbi.www.soap.eutils.esummary.ItemType _value) {
        this.item[i] = _value;
    }


    /**
     * Gets the itemContent value for this ItemType.
     * 
     * @return itemContent
     */
    public java.lang.String getItemContent() {
        return itemContent;
    }


    /**
     * Sets the itemContent value for this ItemType.
     * 
     * @param itemContent
     */
    public void setItemContent(java.lang.String itemContent) {
        this.itemContent = itemContent;
    }


    /**
     * Gets the name value for this ItemType.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this ItemType.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the type value for this ItemType.
     * 
     * @return type
     */
    public gov.nih.nlm.ncbi.www.soap.eutils.esummary.ItemTypeType getType() {
        return type;
    }


    /**
     * Sets the type value for this ItemType.
     * 
     * @param type
     */
    public void setType(gov.nih.nlm.ncbi.www.soap.eutils.esummary.ItemTypeType type) {
        this.type = type;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ItemType)) return false;
        ItemType other = (ItemType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.item==null && other.getItem()==null) || 
             (this.item!=null &&
              java.util.Arrays.equals(this.item, other.getItem()))) &&
            ((this.itemContent==null && other.getItemContent()==null) || 
             (this.itemContent!=null &&
              this.itemContent.equals(other.getItemContent()))) &&
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.type==null && other.getType()==null) || 
             (this.type!=null &&
              this.type.equals(other.getType())));
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
        if (getItem() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getItem());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getItem(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getItemContent() != null) {
            _hashCode += getItemContent().hashCode();
        }
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getType() != null) {
            _hashCode += getType().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ItemType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esummary", "ItemType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("name");
        attrField.setXmlName(new javax.xml.namespace.QName("", "Name"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("type");
        attrField.setXmlName(new javax.xml.namespace.QName("", "Type"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esummary", ">ItemType>Type"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("item");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esummary", "Item"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esummary", "ItemType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("itemContent");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esummary", "ItemContent"));
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
