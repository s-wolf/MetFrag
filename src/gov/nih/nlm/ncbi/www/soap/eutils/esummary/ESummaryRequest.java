/**
 * ESummaryRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.www.soap.eutils.esummary;

public class ESummaryRequest  implements java.io.Serializable {
    private java.lang.String db;

    private java.lang.String id;

    private java.lang.String webEnv;

    private java.lang.String query_key;

    private java.lang.String retstart;

    private java.lang.String retmax;

    private java.lang.String tool;

    private java.lang.String email;

    public ESummaryRequest() {
    }

    public ESummaryRequest(
           java.lang.String db,
           java.lang.String id,
           java.lang.String webEnv,
           java.lang.String query_key,
           java.lang.String retstart,
           java.lang.String retmax,
           java.lang.String tool,
           java.lang.String email) {
           this.db = db;
           this.id = id;
           this.webEnv = webEnv;
           this.query_key = query_key;
           this.retstart = retstart;
           this.retmax = retmax;
           this.tool = tool;
           this.email = email;
    }


    /**
     * Gets the db value for this ESummaryRequest.
     * 
     * @return db
     */
    public java.lang.String getDb() {
        return db;
    }


    /**
     * Sets the db value for this ESummaryRequest.
     * 
     * @param db
     */
    public void setDb(java.lang.String db) {
        this.db = db;
    }


    /**
     * Gets the id value for this ESummaryRequest.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this ESummaryRequest.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the webEnv value for this ESummaryRequest.
     * 
     * @return webEnv
     */
    public java.lang.String getWebEnv() {
        return webEnv;
    }


    /**
     * Sets the webEnv value for this ESummaryRequest.
     * 
     * @param webEnv
     */
    public void setWebEnv(java.lang.String webEnv) {
        this.webEnv = webEnv;
    }


    /**
     * Gets the query_key value for this ESummaryRequest.
     * 
     * @return query_key
     */
    public java.lang.String getQuery_key() {
        return query_key;
    }


    /**
     * Sets the query_key value for this ESummaryRequest.
     * 
     * @param query_key
     */
    public void setQuery_key(java.lang.String query_key) {
        this.query_key = query_key;
    }


    /**
     * Gets the retstart value for this ESummaryRequest.
     * 
     * @return retstart
     */
    public java.lang.String getRetstart() {
        return retstart;
    }


    /**
     * Sets the retstart value for this ESummaryRequest.
     * 
     * @param retstart
     */
    public void setRetstart(java.lang.String retstart) {
        this.retstart = retstart;
    }


    /**
     * Gets the retmax value for this ESummaryRequest.
     * 
     * @return retmax
     */
    public java.lang.String getRetmax() {
        return retmax;
    }


    /**
     * Sets the retmax value for this ESummaryRequest.
     * 
     * @param retmax
     */
    public void setRetmax(java.lang.String retmax) {
        this.retmax = retmax;
    }


    /**
     * Gets the tool value for this ESummaryRequest.
     * 
     * @return tool
     */
    public java.lang.String getTool() {
        return tool;
    }


    /**
     * Sets the tool value for this ESummaryRequest.
     * 
     * @param tool
     */
    public void setTool(java.lang.String tool) {
        this.tool = tool;
    }


    /**
     * Gets the email value for this ESummaryRequest.
     * 
     * @return email
     */
    public java.lang.String getEmail() {
        return email;
    }


    /**
     * Sets the email value for this ESummaryRequest.
     * 
     * @param email
     */
    public void setEmail(java.lang.String email) {
        this.email = email;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ESummaryRequest)) return false;
        ESummaryRequest other = (ESummaryRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.db==null && other.getDb()==null) || 
             (this.db!=null &&
              this.db.equals(other.getDb()))) &&
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            ((this.webEnv==null && other.getWebEnv()==null) || 
             (this.webEnv!=null &&
              this.webEnv.equals(other.getWebEnv()))) &&
            ((this.query_key==null && other.getQuery_key()==null) || 
             (this.query_key!=null &&
              this.query_key.equals(other.getQuery_key()))) &&
            ((this.retstart==null && other.getRetstart()==null) || 
             (this.retstart!=null &&
              this.retstart.equals(other.getRetstart()))) &&
            ((this.retmax==null && other.getRetmax()==null) || 
             (this.retmax!=null &&
              this.retmax.equals(other.getRetmax()))) &&
            ((this.tool==null && other.getTool()==null) || 
             (this.tool!=null &&
              this.tool.equals(other.getTool()))) &&
            ((this.email==null && other.getEmail()==null) || 
             (this.email!=null &&
              this.email.equals(other.getEmail())));
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
        if (getDb() != null) {
            _hashCode += getDb().hashCode();
        }
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getWebEnv() != null) {
            _hashCode += getWebEnv().hashCode();
        }
        if (getQuery_key() != null) {
            _hashCode += getQuery_key().hashCode();
        }
        if (getRetstart() != null) {
            _hashCode += getRetstart().hashCode();
        }
        if (getRetmax() != null) {
            _hashCode += getRetmax().hashCode();
        }
        if (getTool() != null) {
            _hashCode += getTool().hashCode();
        }
        if (getEmail() != null) {
            _hashCode += getEmail().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ESummaryRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esummary", ">eSummaryRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("db");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esummary", "db"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esummary", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("webEnv");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esummary", "WebEnv"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("query_key");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esummary", "query_key"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("retstart");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esummary", "retstart"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("retmax");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esummary", "retmax"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tool");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esummary", "tool"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("email");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esummary", "email"));
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
