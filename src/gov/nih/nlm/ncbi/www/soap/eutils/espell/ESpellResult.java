/**
 * ESpellResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.www.soap.eutils.espell;

public class ESpellResult  implements java.io.Serializable {
    private java.lang.String database;

    private java.lang.String query;

    private java.lang.String correctedQuery;

    private gov.nih.nlm.ncbi.www.soap.eutils.espell.SpelledQuery spelledQuery;

    private java.lang.String ERROR;

    public ESpellResult() {
    }

    public ESpellResult(
           java.lang.String database,
           java.lang.String query,
           java.lang.String correctedQuery,
           gov.nih.nlm.ncbi.www.soap.eutils.espell.SpelledQuery spelledQuery,
           java.lang.String ERROR) {
           this.database = database;
           this.query = query;
           this.correctedQuery = correctedQuery;
           this.spelledQuery = spelledQuery;
           this.ERROR = ERROR;
    }


    /**
     * Gets the database value for this ESpellResult.
     * 
     * @return database
     */
    public java.lang.String getDatabase() {
        return database;
    }


    /**
     * Sets the database value for this ESpellResult.
     * 
     * @param database
     */
    public void setDatabase(java.lang.String database) {
        this.database = database;
    }


    /**
     * Gets the query value for this ESpellResult.
     * 
     * @return query
     */
    public java.lang.String getQuery() {
        return query;
    }


    /**
     * Sets the query value for this ESpellResult.
     * 
     * @param query
     */
    public void setQuery(java.lang.String query) {
        this.query = query;
    }


    /**
     * Gets the correctedQuery value for this ESpellResult.
     * 
     * @return correctedQuery
     */
    public java.lang.String getCorrectedQuery() {
        return correctedQuery;
    }


    /**
     * Sets the correctedQuery value for this ESpellResult.
     * 
     * @param correctedQuery
     */
    public void setCorrectedQuery(java.lang.String correctedQuery) {
        this.correctedQuery = correctedQuery;
    }


    /**
     * Gets the spelledQuery value for this ESpellResult.
     * 
     * @return spelledQuery
     */
    public gov.nih.nlm.ncbi.www.soap.eutils.espell.SpelledQuery getSpelledQuery() {
        return spelledQuery;
    }


    /**
     * Sets the spelledQuery value for this ESpellResult.
     * 
     * @param spelledQuery
     */
    public void setSpelledQuery(gov.nih.nlm.ncbi.www.soap.eutils.espell.SpelledQuery spelledQuery) {
        this.spelledQuery = spelledQuery;
    }


    /**
     * Gets the ERROR value for this ESpellResult.
     * 
     * @return ERROR
     */
    public java.lang.String getERROR() {
        return ERROR;
    }


    /**
     * Sets the ERROR value for this ESpellResult.
     * 
     * @param ERROR
     */
    public void setERROR(java.lang.String ERROR) {
        this.ERROR = ERROR;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ESpellResult)) return false;
        ESpellResult other = (ESpellResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.database==null && other.getDatabase()==null) || 
             (this.database!=null &&
              this.database.equals(other.getDatabase()))) &&
            ((this.query==null && other.getQuery()==null) || 
             (this.query!=null &&
              this.query.equals(other.getQuery()))) &&
            ((this.correctedQuery==null && other.getCorrectedQuery()==null) || 
             (this.correctedQuery!=null &&
              this.correctedQuery.equals(other.getCorrectedQuery()))) &&
            ((this.spelledQuery==null && other.getSpelledQuery()==null) || 
             (this.spelledQuery!=null &&
              this.spelledQuery.equals(other.getSpelledQuery()))) &&
            ((this.ERROR==null && other.getERROR()==null) || 
             (this.ERROR!=null &&
              this.ERROR.equals(other.getERROR())));
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
        if (getDatabase() != null) {
            _hashCode += getDatabase().hashCode();
        }
        if (getQuery() != null) {
            _hashCode += getQuery().hashCode();
        }
        if (getCorrectedQuery() != null) {
            _hashCode += getCorrectedQuery().hashCode();
        }
        if (getSpelledQuery() != null) {
            _hashCode += getSpelledQuery().hashCode();
        }
        if (getERROR() != null) {
            _hashCode += getERROR().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ESpellResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/espell", ">eSpellResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("database");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/espell", "Database"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("query");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/espell", "Query"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("correctedQuery");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/espell", "CorrectedQuery"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("spelledQuery");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/espell", "SpelledQuery"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/espell", ">SpelledQuery"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ERROR");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/espell", "ERROR"));
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
