/**
 * StereoType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.pubchem;

public class StereoType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected StereoType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _eStereo_Ignore = "eStereo_Ignore";
    public static final java.lang.String _eStereo_Exact = "eStereo_Exact";
    public static final java.lang.String _eStereo_Relative = "eStereo_Relative";
    public static final java.lang.String _eStereo_NonConflicting = "eStereo_NonConflicting";
    public static final StereoType eStereo_Ignore = new StereoType(_eStereo_Ignore);
    public static final StereoType eStereo_Exact = new StereoType(_eStereo_Exact);
    public static final StereoType eStereo_Relative = new StereoType(_eStereo_Relative);
    public static final StereoType eStereo_NonConflicting = new StereoType(_eStereo_NonConflicting);
    public java.lang.String getValue() { return _value_;}
    public static StereoType fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        StereoType enumeration = (StereoType)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static StereoType fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(StereoType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "StereoType"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
