/**
 * StatusType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.pubchem;

public class StatusType implements java.io.Serializable {
    private java.lang.String _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected StatusType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final java.lang.String _eStatus_Unknown = "eStatus_Unknown";
    public static final java.lang.String _eStatus_Success = "eStatus_Success";
    public static final java.lang.String _eStatus_ServerError = "eStatus_ServerError";
    public static final java.lang.String _eStatus_HitLimit = "eStatus_HitLimit";
    public static final java.lang.String _eStatus_TimeLimit = "eStatus_TimeLimit";
    public static final java.lang.String _eStatus_InputError = "eStatus_InputError";
    public static final java.lang.String _eStatus_DataError = "eStatus_DataError";
    public static final java.lang.String _eStatus_Stopped = "eStatus_Stopped";
    public static final java.lang.String _eStatus_Running = "eStatus_Running";
    public static final java.lang.String _eStatus_Queued = "eStatus_Queued";
    public static final StatusType eStatus_Unknown = new StatusType(_eStatus_Unknown);
    public static final StatusType eStatus_Success = new StatusType(_eStatus_Success);
    public static final StatusType eStatus_ServerError = new StatusType(_eStatus_ServerError);
    public static final StatusType eStatus_HitLimit = new StatusType(_eStatus_HitLimit);
    public static final StatusType eStatus_TimeLimit = new StatusType(_eStatus_TimeLimit);
    public static final StatusType eStatus_InputError = new StatusType(_eStatus_InputError);
    public static final StatusType eStatus_DataError = new StatusType(_eStatus_DataError);
    public static final StatusType eStatus_Stopped = new StatusType(_eStatus_Stopped);
    public static final StatusType eStatus_Running = new StatusType(_eStatus_Running);
    public static final StatusType eStatus_Queued = new StatusType(_eStatus_Queued);
    public java.lang.String getValue() { return _value_;}
    public static StatusType fromValue(java.lang.String value)
          throws java.lang.IllegalArgumentException {
        StatusType enumeration = (StatusType)
            _table_.get(value);
        if (enumeration==null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }
    public static StatusType fromString(java.lang.String value)
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
        new org.apache.axis.description.TypeDesc(StatusType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "StatusType"));
    }
    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

}
