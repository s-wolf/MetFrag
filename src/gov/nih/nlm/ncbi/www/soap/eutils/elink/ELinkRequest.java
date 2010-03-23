/**
 * ELinkRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.www.soap.eutils.elink;

public class ELinkRequest  implements java.io.Serializable {
    private java.lang.String db;

    private java.lang.String[] id;

    private java.lang.String reldate;

    private java.lang.String mindate;

    private java.lang.String maxdate;

    private java.lang.String datetype;

    private java.lang.String term;

    private java.lang.String dbfrom;

    private java.lang.String linkname;

    private java.lang.String webEnv;

    private java.lang.String query_key;

    private java.lang.String cmd;

    private java.lang.String tool;

    private java.lang.String email;

    public ELinkRequest() {
    }

    public ELinkRequest(
           java.lang.String db,
           java.lang.String[] id,
           java.lang.String reldate,
           java.lang.String mindate,
           java.lang.String maxdate,
           java.lang.String datetype,
           java.lang.String term,
           java.lang.String dbfrom,
           java.lang.String linkname,
           java.lang.String webEnv,
           java.lang.String query_key,
           java.lang.String cmd,
           java.lang.String tool,
           java.lang.String email) {
           this.db = db;
           this.id = id;
           this.reldate = reldate;
           this.mindate = mindate;
           this.maxdate = maxdate;
           this.datetype = datetype;
           this.term = term;
           this.dbfrom = dbfrom;
           this.linkname = linkname;
           this.webEnv = webEnv;
           this.query_key = query_key;
           this.cmd = cmd;
           this.tool = tool;
           this.email = email;
    }


    /**
     * Gets the db value for this ELinkRequest.
     * 
     * @return db
     */
    public java.lang.String getDb() {
        return db;
    }


    /**
     * Sets the db value for this ELinkRequest.
     * 
     * @param db
     */
    public void setDb(java.lang.String db) {
        this.db = db;
    }


    /**
     * Gets the id value for this ELinkRequest.
     * 
     * @return id
     */
    public java.lang.String[] getId() {
        return id;
    }


    /**
     * Sets the id value for this ELinkRequest.
     * 
     * @param id
     */
    public void setId(java.lang.String[] id) {
        this.id = id;
    }

    public java.lang.String getId(int i) {
        return this.id[i];
    }

    public void setId(int i, java.lang.String _value) {
        this.id[i] = _value;
    }


    /**
     * Gets the reldate value for this ELinkRequest.
     * 
     * @return reldate
     */
    public java.lang.String getReldate() {
        return reldate;
    }


    /**
     * Sets the reldate value for this ELinkRequest.
     * 
     * @param reldate
     */
    public void setReldate(java.lang.String reldate) {
        this.reldate = reldate;
    }


    /**
     * Gets the mindate value for this ELinkRequest.
     * 
     * @return mindate
     */
    public java.lang.String getMindate() {
        return mindate;
    }


    /**
     * Sets the mindate value for this ELinkRequest.
     * 
     * @param mindate
     */
    public void setMindate(java.lang.String mindate) {
        this.mindate = mindate;
    }


    /**
     * Gets the maxdate value for this ELinkRequest.
     * 
     * @return maxdate
     */
    public java.lang.String getMaxdate() {
        return maxdate;
    }


    /**
     * Sets the maxdate value for this ELinkRequest.
     * 
     * @param maxdate
     */
    public void setMaxdate(java.lang.String maxdate) {
        this.maxdate = maxdate;
    }


    /**
     * Gets the datetype value for this ELinkRequest.
     * 
     * @return datetype
     */
    public java.lang.String getDatetype() {
        return datetype;
    }


    /**
     * Sets the datetype value for this ELinkRequest.
     * 
     * @param datetype
     */
    public void setDatetype(java.lang.String datetype) {
        this.datetype = datetype;
    }


    /**
     * Gets the term value for this ELinkRequest.
     * 
     * @return term
     */
    public java.lang.String getTerm() {
        return term;
    }


    /**
     * Sets the term value for this ELinkRequest.
     * 
     * @param term
     */
    public void setTerm(java.lang.String term) {
        this.term = term;
    }


    /**
     * Gets the dbfrom value for this ELinkRequest.
     * 
     * @return dbfrom
     */
    public java.lang.String getDbfrom() {
        return dbfrom;
    }


    /**
     * Sets the dbfrom value for this ELinkRequest.
     * 
     * @param dbfrom
     */
    public void setDbfrom(java.lang.String dbfrom) {
        this.dbfrom = dbfrom;
    }


    /**
     * Gets the linkname value for this ELinkRequest.
     * 
     * @return linkname
     */
    public java.lang.String getLinkname() {
        return linkname;
    }


    /**
     * Sets the linkname value for this ELinkRequest.
     * 
     * @param linkname
     */
    public void setLinkname(java.lang.String linkname) {
        this.linkname = linkname;
    }


    /**
     * Gets the webEnv value for this ELinkRequest.
     * 
     * @return webEnv
     */
    public java.lang.String getWebEnv() {
        return webEnv;
    }


    /**
     * Sets the webEnv value for this ELinkRequest.
     * 
     * @param webEnv
     */
    public void setWebEnv(java.lang.String webEnv) {
        this.webEnv = webEnv;
    }


    /**
     * Gets the query_key value for this ELinkRequest.
     * 
     * @return query_key
     */
    public java.lang.String getQuery_key() {
        return query_key;
    }


    /**
     * Sets the query_key value for this ELinkRequest.
     * 
     * @param query_key
     */
    public void setQuery_key(java.lang.String query_key) {
        this.query_key = query_key;
    }


    /**
     * Gets the cmd value for this ELinkRequest.
     * 
     * @return cmd
     */
    public java.lang.String getCmd() {
        return cmd;
    }


    /**
     * Sets the cmd value for this ELinkRequest.
     * 
     * @param cmd
     */
    public void setCmd(java.lang.String cmd) {
        this.cmd = cmd;
    }


    /**
     * Gets the tool value for this ELinkRequest.
     * 
     * @return tool
     */
    public java.lang.String getTool() {
        return tool;
    }


    /**
     * Sets the tool value for this ELinkRequest.
     * 
     * @param tool
     */
    public void setTool(java.lang.String tool) {
        this.tool = tool;
    }


    /**
     * Gets the email value for this ELinkRequest.
     * 
     * @return email
     */
    public java.lang.String getEmail() {
        return email;
    }


    /**
     * Sets the email value for this ELinkRequest.
     * 
     * @param email
     */
    public void setEmail(java.lang.String email) {
        this.email = email;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ELinkRequest)) return false;
        ELinkRequest other = (ELinkRequest) obj;
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
              java.util.Arrays.equals(this.id, other.getId()))) &&
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
            ((this.term==null && other.getTerm()==null) || 
             (this.term!=null &&
              this.term.equals(other.getTerm()))) &&
            ((this.dbfrom==null && other.getDbfrom()==null) || 
             (this.dbfrom!=null &&
              this.dbfrom.equals(other.getDbfrom()))) &&
            ((this.linkname==null && other.getLinkname()==null) || 
             (this.linkname!=null &&
              this.linkname.equals(other.getLinkname()))) &&
            ((this.webEnv==null && other.getWebEnv()==null) || 
             (this.webEnv!=null &&
              this.webEnv.equals(other.getWebEnv()))) &&
            ((this.query_key==null && other.getQuery_key()==null) || 
             (this.query_key!=null &&
              this.query_key.equals(other.getQuery_key()))) &&
            ((this.cmd==null && other.getCmd()==null) || 
             (this.cmd!=null &&
              this.cmd.equals(other.getCmd()))) &&
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
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getId());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getId(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
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
        if (getTerm() != null) {
            _hashCode += getTerm().hashCode();
        }
        if (getDbfrom() != null) {
            _hashCode += getDbfrom().hashCode();
        }
        if (getLinkname() != null) {
            _hashCode += getLinkname().hashCode();
        }
        if (getWebEnv() != null) {
            _hashCode += getWebEnv().hashCode();
        }
        if (getQuery_key() != null) {
            _hashCode += getQuery_key().hashCode();
        }
        if (getCmd() != null) {
            _hashCode += getCmd().hashCode();
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
        new org.apache.axis.description.TypeDesc(ELinkRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", ">eLinkRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("db");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "db"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "id"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reldate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "reldate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mindate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "mindate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("maxdate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "maxdate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("datetype");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "datetype"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("term");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "term"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dbfrom");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "dbfrom"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("linkname");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "linkname"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("webEnv");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "WebEnv"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("query_key");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "query_key"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cmd");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "cmd"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tool");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "tool"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("email");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "email"));
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
