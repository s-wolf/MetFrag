/**
 * ItemTypeType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.www.soap.eutils.esummary;

public class ItemTypeType implements java.io.Serializable {
    private org.apache.axis.types.NMToken _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected ItemTypeType(org.apache.axis.types.NMToken value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final org.apache.axis.types.NMToken _Integer = new org.apache.axis.types.NMToken("Integer");
    public static final org.apache.axis.types.NMToken _Date = new org.apache.axis.types.NMToken("Date");
    public static final org.apache.axis.types.NMToken _String = new org.apache.axis.types.NMToken("String");
    public static final org.apache.axis.types.NMToken _Structure = new org.apache.axis.types.NMToken("Structure");
    public static final org.apache.axis.types.NMToken _List = new org.apache.axis.types.NMToken("List");
    public static final org.apache.axis.types.NMToken _Flags = new org.apache.axis.types.NMToken("Flags");
    public static final org.apache.axis.types.NMToken _Qualifier = new org.apache.axis.types.NMToken("Qualifier");
    public static final org.apache.axis.types.NMToken _Enumerator = new org.apache.axis.types.NMToken("Enumerator");
    public static final org.apache.axis.types.NMToken _Unknown = new org.apache.axis.types.NMToken("Unknown");
    public static final ItemTypeType Integer = new ItemTypeType(_Integer);
    public static final ItemTypeType Date = new ItemTypeType(_Date);
    public static final ItemTypeType String = new ItemTypeType(_String);
    public static final ItemTypeType Structure = new ItemTypeType(_Structure);
    public static final ItemTypeType List = new ItemTypeType(_List);
    public static final ItemTypeType Flags = new ItemTypeType(_Flags);
    public static final ItemTypeType Qualifier = new ItemTypeType(_Qualifier);
    public static final ItemTypeType Enumerator = new ItemTypeType(_Enumerator);
    public static final ItemTypeType Unknown = new ItemTypeType(_Unknown);
    public org.apache.axis.types.NMToken getValue() { return _value_;}
    public static ItemTypeType fromValue(org.apache.axis.types.NMToken value)
          throws java.lang.IllegalArgumentException {
        ItemTypeType enumeration = (ItemTypeType)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static ItemTypeType fromString(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        try {
            return fromValue(new org.apache.axis.types.NMToken(value));
        } catch (Exception e) {
            throw new java.lang.IllegalArgumentException();
        }
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_.toString();}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumSerializer(
            _javaType, _xmlType);
    }
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new org.apache.axis.encoding.ser.EnumDeserializer(
            _javaType, _xmlType);
    }
    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ItemTypeType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esummary", ">ItemType>Type"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
