/**
 * IdentityType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.pubchem;

public class IdentityType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected IdentityType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _eIdentity_SameConnectivity = "eIdentity_SameConnectivity";
    public static final java.lang.String _eIdentity_AnyTautomer = "eIdentity_AnyTautomer";
    public static final java.lang.String _eIdentity_SameStereo = "eIdentity_SameStereo";
    public static final java.lang.String _eIdentity_SameIsotope = "eIdentity_SameIsotope";
    public static final java.lang.String _eIdentity_SameStereoIsotope = "eIdentity_SameStereoIsotope";
    public static final java.lang.String _eIdentity_SameNonconflictStereo = "eIdentity_SameNonconflictStereo";
    public static final java.lang.String _eIdentity_SameIsotopeNonconflictStereo = "eIdentity_SameIsotopeNonconflictStereo";
    public static final IdentityType eIdentity_SameConnectivity = new IdentityType(_eIdentity_SameConnectivity);
    public static final IdentityType eIdentity_AnyTautomer = new IdentityType(_eIdentity_AnyTautomer);
    public static final IdentityType eIdentity_SameStereo = new IdentityType(_eIdentity_SameStereo);
    public static final IdentityType eIdentity_SameIsotope = new IdentityType(_eIdentity_SameIsotope);
    public static final IdentityType eIdentity_SameStereoIsotope = new IdentityType(_eIdentity_SameStereoIsotope);
    public static final IdentityType eIdentity_SameNonconflictStereo = new IdentityType(_eIdentity_SameNonconflictStereo);
    public static final IdentityType eIdentity_SameIsotopeNonconflictStereo = new IdentityType(_eIdentity_SameIsotopeNonconflictStereo);
    public java.lang.String getValue() { return _value_;}
    public static IdentityType fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        IdentityType enumeration = (IdentityType)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static IdentityType fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(IdentityType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "IdentityType"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
