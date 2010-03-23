/**
 * ESearchRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.www.soap.eutils.esearch;

public class ESearchRequest  implements java.io.Serializable {
    private java.lang.String db;

    private java.lang.String term;

    private java.lang.String webEnv;

    private java.lang.String queryKey;

    private java.lang.String usehistory;

    private java.lang.String tool;

    private java.lang.String email;

    private java.lang.String field;

    private java.lang.String reldate;

    private java.lang.String mindate;

    private java.lang.String maxdate;

    private java.lang.String datetype;

    private java.lang.String retStart;

    private java.lang.String retMax;

    private java.lang.String rettype;

    private java.lang.String sort;

    public ESearchRequest() {
    }

    public ESearchRequest(
           java.lang.String db,
           java.lang.String term,
           java.lang.String webEnv,
           java.lang.String queryKey,
           java.lang.String usehistory,
           java.lang.String tool,
           java.lang.String email,
           java.lang.String field,
           java.lang.String reldate,
           java.lang.String mindate,
           java.lang.String maxdate,
           java.lang.String datetype,
           java.lang.String retStart,
           java.lang.String retMax,
           java.lang.String rettype,
           java.lang.String sort) {
           this.db = db;
           this.term = term;
           this.webEnv = webEnv;
           this.queryKey = queryKey;
           this.usehistory = usehistory;
           this.tool = tool;
           this.email = email;
           this.field = field;
           this.reldate = reldate;
           this.mindate = mindate;
           this.maxdate = maxdate;
           this.datetype = datetype;
           this.retStart = retStart;
           this.retMax = retMax;
           this.rettype = rettype;
           this.sort = sort;
    }


    /**
     * Gets the db value for this ESearchRequest.
     * 
     * @return db
     */
    public java.lang.String getDb() {
        return db;
    }


    /**
     * Sets the db value for this ESearchRequest.
     * 
     * @param db
     */
    public void setDb(java.lang.String db) {
        this.db = db;
    }


    /**
     * Gets the term value for this ESearchRequest.
     * 
     * @return term
     */
    public java.lang.String getTerm() {
        return term;
    }


    /**
     * Sets the term value for this ESearchRequest.
     * 
     * @param term
     */
    public void setTerm(java.lang.String term) {
        this.term = term;
    }


    /**
     * Gets the webEnv value for this ESearchRequest.
     * 
     * @return webEnv
     */
    public java.lang.String getWebEnv() {
        return webEnv;
    }


    /**
     * Sets the webEnv value for this ESearchRequest.
     * 
     * @param webEnv
     */
    public void setWebEnv(java.lang.String webEnv) {
        this.webEnv = webEnv;
    }


    /**
     * Gets the queryKey value for this ESearchRequest.
     * 
     * @return queryKey
     */
    public java.lang.String getQueryKey() {
        return queryKey;
    }


    /**
     * Sets the queryKey value for this ESearchRequest.
     * 
     * @param queryKey
     */
    public void setQueryKey(java.lang.String queryKey) {
        this.queryKey = queryKey;
    }


    /**
     * Gets the usehistory value for this ESearchRequest.
     * 
     * @return usehistory
     */
    public java.lang.String getUsehistory() {
        return usehistory;
    }


    /**
     * Sets the usehistory value for this ESearchRequest.
     * 
     * @param usehistory
     */
    public void setUsehistory(java.lang.String usehistory) {
        this.usehistory = usehistory;
    }


    /**
     * Gets the tool value for this ESearchRequest.
     * 
     * @return tool
     */
    public java.lang.String getTool() {
        return tool;
    }


    /**
     * Sets the tool value for this ESearchRequest.
     * 
     * @param tool
     */
    public void setTool(java.lang.String tool) {
        this.tool = tool;
    }


    /**
     * Gets the email value for this ESearchRequest.
     * 
     * @return email
     */
    public java.lang.String getEmail() {
        return email;
    }


    /**
     * Sets the email value for this ESearchRequest.
     * 
     * @param email
     */
    public void setEmail(java.lang.String email) {
        this.email = email;
    }


    /**
     * Gets the field value for this ESearchRequest.
     * 
     * @return field
     */
    public java.lang.String getField() {
        return field;
    }


    /**
     * Sets the field value for this ESearchRequest.
     * 
     * @param field
     */
    public void setField(java.lang.String field) {
        this.field = field;
    }


    /**
     * Gets the reldate value for this ESearchRequest.
     * 
     * @return reldate
     */
    public java.lang.String getReldate() {
        return reldate;
    }


    /**
     * Sets the reldate value for this ESearchRequest.
     * 
     * @param reldate
     */
    public void setReldate(java.lang.String reldate) {
        this.reldate = reldate;
    }


    /**
     * Gets the mindate value for this ESearchRequest.
     * 
     * @return mindate
     */
    public java.lang.String getMindate() {
        return mindate;
    }


    /**
     * Sets the mindate value for this ESearchRequest.
     * 
     * @param mindate
     */
    public void setMindate(java.lang.String mindate) {
        this.mindate = mindate;
    }


    /**
     * Gets the maxdate value for this ESearchRequest.
     * 
     * @return maxdate
     */
    public java.lang.String getMaxdate() {
        return maxdate;
    }


    /**
     * Sets the maxdate value for this ESearchRequest.
     * 
     * @param maxdate
     */
    public void setMaxdate(java.lang.String maxdate) {
        this.maxdate = maxdate;
    }


    /**
     * Gets the datetype value for this ESearchRequest.
     * 
     * @return datetype
     */
    public java.lang.String getDatetype() {
        return datetype;
    }


    /**
     * Sets the datetype value for this ESearchRequest.
     * 
     * @param datetype
     */
    public void setDatetype(java.lang.String datetype) {
        this.datetype = datetype;
    }


    /**
     * Gets the retStart value for this ESearchRequest.
     * 
     * @return retStart
     */
    public java.lang.String getRetStart() {
        return retStart;
    }


    /**
     * Sets the retStart value for this ESearchRequest.
     * 
     * @param retStart
     */
    public void setRetStart(java.lang.String retStart) {
        this.retStart = retStart;
    }


    /**
     * Gets the retMax value for this ESearchRequest.
     * 
     * @return retMax
     */
    public java.lang.String getRetMax() {
        return retMax;
    }


    /**
     * Sets the retMax value for this ESearchRequest.
     * 
     * @param retMax
     */
    public void setRetMax(java.lang.String retMax) {
        this.retMax = retMax;
    }


    /**
     * Gets the rettype value for this ESearchRequest.
     * 
     * @return rettype
     */
    public java.lang.String getRettype() {
        return rettype;
    }


    /**
     * Sets the rettype value for this ESearchRequest.
     * 
     * @param rettype
     */
    public void setRettype(java.lang.String rettype) {
        this.rettype = rettype;
    }


    /**
     * Gets the sort value for this ESearchRequest.
     * 
     * @return sort
     */
    public java.lang.String getSort() {
        return sort;
    }


    /**
     * Sets the sort value for this ESearchRequest.
     * 
     * @param sort
     */
    public void setSort(java.lang.String sort) {
        this.sort = sort;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ESearchRequest)) return false;
        ESearchRequest other = (ESearchRequest) obj;
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
            ((this.term==null && other.getTerm()==null) || 
             (this.term!=null &&
              this.term.equals(other.getTerm()))) &&
            ((this.webEnv==null && other.getWebEnv()==null) || 
             (this.webEnv!=null &&
              this.webEnv.equals(other.getWebEnv()))) &&
            ((this.queryKey==null && other.getQueryKey()==null) || 
             (this.queryKey!=null &&
              this.queryKey.equals(other.getQueryKey()))) &&
            ((this.usehistory==null && other.getUsehistory()==null) || 
             (this.usehistory!=null &&
              this.usehistory.equals(other.getUsehistory()))) &&
            ((this.tool==null && other.getTool()==null) || 
             (this.tool!=null &&
              this.tool.equals(other.getTool()))) &&
            ((this.email==null && other.getEmail()==null) || 
             (this.email!=null &&
              this.email.equals(other.getEmail()))) &&
            ((this.field==null && other.getField()==null) || 
             (this.field!=null &&
              this.field.equals(other.getField()))) &&
            ((this.reldate==null && other.getReldate()==null) || 
             (this.reldate!=null &&
              this.reldate.equals(other.getReldate()))) &&
            ((this.mindate==null && other.getMindate()==null) || 
             (this.mindate!=null &&
              this.mindate.equals(other.getMindate()))) &&
            ((this.maxdate==null && other.getMaxdate()==null) || 
             (this.maxdate!=null &&
              this.maxdate.equals(other.getMaxdate()))) &&
            ((this.datetype==null && other.getDatetype()==null) || 
             (this.datetype!=null &&
              this.datetype.equals(other.getDatetype()))) &&
            ((this.retStart==null && other.getRetStart()==null) || 
             (this.retStart!=null &&
              this.retStart.equals(other.getRetStart()))) &&
            ((this.retMax==null && other.getRetMax()==null) || 
             (this.retMax!=null &&
              this.retMax.equals(other.getRetMax()))) &&
            ((this.rettype==null && other.getRettype()==null) || 
             (this.rettype!=null &&
              this.rettype.equals(other.getRettype()))) &&
            ((this.sort==null && other.getSort()==null) || 
             (this.sort!=null &&
              this.sort.equals(other.getSort())));
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
        if (getTerm() != null) {
            _hashCode += getTerm().hashCode();
        }
        if (getWebEnv() != null) {
            _hashCode += getWebEnv().hashCode();
        }
        if (getQueryKey() != null) {
            _hashCode += getQueryKey().hashCode();
        }
        if (getUsehistory() != null) {
            _hashCode += getUsehistory().hashCode();
        }
        if (getTool() != null) {
            _hashCode += getTool().hashCode();
        }
        if (getEmail() != null) {
            _hashCode += getEmail().hashCode();
        }
        if (getField() != null) {
            _hashCode += getField().hashCode();
        }
        if (getReldate() != null) {
            _hashCode += getReldate().hashCode();
        }
        if (getMindate() != null) {
            _hashCode += getMindate().hashCode();
        }
        if (getMaxdate() != null) {
            _hashCode += getMaxdate().hashCode();
        }
        if (getDatetype() != null) {
            _hashCode += getDatetype().hashCode();
        }
        if (getRetStart() != null) {
            _hashCode += getRetStart().hashCode();
        }
        if (getRetMax() != null) {
            _hashCode += getRetMax().hashCode();
        }
        if (getRettype() != null) {
            _hashCode += getRettype().hashCode();
        }
        if (getSort() != null) {
            _hashCode += getSort().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ESearchRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", ">eSearchRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("db");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "db"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("term");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "term"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("webEnv");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "WebEnv"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("queryKey");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "QueryKey"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("usehistory");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "usehistory"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tool");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "tool"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("email");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "email"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("field");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "field"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reldate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "reldate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mindate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "mindate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("maxdate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "maxdate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("datetype");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "datetype"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("retStart");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "RetStart"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("retMax");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "RetMax"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rettype");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "rettype"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sort");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "sort"));
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
