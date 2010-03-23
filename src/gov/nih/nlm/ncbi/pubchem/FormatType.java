/**
 * FormatType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.pubchem;

public class FormatType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected FormatType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _eFormat_ASNB = "eFormat_ASNB";
    public static final java.lang.String _eFormat_ASNT = "eFormat_ASNT";
    public static final java.lang.String _eFormat_XML = "eFormat_XML";
    public static final java.lang.String _eFormat_SDF = "eFormat_SDF";
    public static final java.lang.String _eFormat_SMILES = "eFormat_SMILES";
    public static final java.lang.String _eFormat_InChI = "eFormat_InChI";
    public static final java.lang.String _eFormat_Image = "eFormat_Image";
    public static final java.lang.String _eFormat_Thumbnail = "eFormat_Thumbnail";
    public static final FormatType eFormat_ASNB = new FormatType(_eFormat_ASNB);
    public static final FormatType eFormat_ASNT = new FormatType(_eFormat_ASNT);
    public static final FormatType eFormat_XML = new FormatType(_eFormat_XML);
    public static final FormatType eFormat_SDF = new FormatType(_eFormat_SDF);
    public static final FormatType eFormat_SMILES = new FormatType(_eFormat_SMILES);
    public static final FormatType eFormat_InChI = new FormatType(_eFormat_InChI);
    public static final FormatType eFormat_Image = new FormatType(_eFormat_Image);
    public static final FormatType eFormat_Thumbnail = new FormatType(_eFormat_Thumbnail);
    public java.lang.String getValue() { return _value_;}
    public static FormatType fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        FormatType enumeration = (FormatType)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static FormatType fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(FormatType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "FormatType"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
