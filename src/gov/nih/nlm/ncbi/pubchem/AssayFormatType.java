/**
 * AssayFormatType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.pubchem;

public class AssayFormatType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected AssayFormatType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _eAssayFormat_XML = "eAssayFormat_XML";
    public static final java.lang.String _eAssayFormat_ASN_Text = "eAssayFormat_ASN_Text";
    public static final java.lang.String _eAssayFormat_ASN_Binary = "eAssayFormat_ASN_Binary";
    public static final java.lang.String _eAssayFormat_CSV = "eAssayFormat_CSV";
    public static final AssayFormatType eAssayFormat_XML = new AssayFormatType(_eAssayFormat_XML);
    public static final AssayFormatType eAssayFormat_ASN_Text = new AssayFormatType(_eAssayFormat_ASN_Text);
    public static final AssayFormatType eAssayFormat_ASN_Binary = new AssayFormatType(_eAssayFormat_ASN_Binary);
    public static final AssayFormatType eAssayFormat_CSV = new AssayFormatType(_eAssayFormat_CSV);
    public java.lang.String getValue() { return _value_;}
    public static AssayFormatType fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        AssayFormatType enumeration = (AssayFormatType)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static AssayFormatType fromString(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        return fromValue(value);
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_;}
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
        new org.apache.axis.description.TypeDesc(AssayFormatType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "AssayFormatType"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
