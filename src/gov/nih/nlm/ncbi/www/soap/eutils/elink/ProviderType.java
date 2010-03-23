/**
 * ProviderType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.www.soap.eutils.elink;

public class ProviderType  implements java.io.Serializable {
    private java.lang.String name;

    private java.lang.String nameAbbr;

    private gov.nih.nlm.ncbi.www.soap.eutils.elink.IdType id;

    private java.lang.String url;

    private java.lang.String iconUrl;

    public ProviderType() {
    }

    public ProviderType(
           java.lang.String name,
           java.lang.String nameAbbr,
           gov.nih.nlm.ncbi.www.soap.eutils.elink.IdType id,
           java.lang.String url,
           java.lang.String iconUrl) {
           this.name = name;
           this.nameAbbr = nameAbbr;
           this.id = id;
           this.url = url;
           this.iconUrl = iconUrl;
    }


    /**
     * Gets the name value for this ProviderType.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this ProviderType.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the nameAbbr value for this ProviderType.
     * 
     * @return nameAbbr
     */
    public java.lang.String getNameAbbr() {
        return nameAbbr;
    }


    /**
     * Sets the nameAbbr value for this ProviderType.
     * 
     * @param nameAbbr
     */
    public void setNameAbbr(java.lang.String nameAbbr) {
        this.nameAbbr = nameAbbr;
    }


    /**
     * Gets the id value for this ProviderType.
     * 
     * @return id
     */
    public gov.nih.nlm.ncbi.www.soap.eutils.elink.IdType getId() {
        return id;
    }


    /**
     * Sets the id value for this ProviderType.
     * 
     * @param id
     */
    public void setId(gov.nih.nlm.ncbi.www.soap.eutils.elink.IdType id) {
        this.id = id;
    }


    /**
     * Gets the url value for this ProviderType.
     * 
     * @return url
     */
    public java.lang.String getUrl() {
        return url;
    }


    /**
     * Sets the url value for this ProviderType.
     * 
     * @param url
     */
    public void setUrl(java.lang.String url) {
        this.url = url;
    }


    /**
     * Gets the iconUrl value for this ProviderType.
     * 
     * @return iconUrl
     */
    public java.lang.String getIconUrl() {
        return iconUrl;
    }


    /**
     * Sets the iconUrl value for this ProviderType.
     * 
     * @param iconUrl
     */
    public void setIconUrl(java.lang.String iconUrl) {
        this.iconUrl = iconUrl;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ProviderType)) return false;
        ProviderType other = (ProviderType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.nameAbbr==null && other.getNameAbbr()==null) || 
             (this.nameAbbr!=null &&
              this.nameAbbr.equals(other.getNameAbbr()))) &&
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            ((this.url==null && other.getUrl()==null) || 
             (this.url!=null &&
              this.url.equals(other.getUrl()))) &&
            ((this.iconUrl==null && other.getIconUrl()==null) || 
             (this.iconUrl!=null &&
              this.iconUrl.equals(other.getIconUrl())));
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
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getNameAbbr() != null) {
            _hashCode += getNameAbbr().hashCode();
        }
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getUrl() != null) {
            _hashCode += getUrl().hashCode();
        }
        if (getIconUrl() != null) {
            _hashCode += getIconUrl().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ProviderType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "ProviderType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "Name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nameAbbr");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "NameAbbr"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "Id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "IdType"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("url");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "Url"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("iconUrl");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/elink", "IconUrl"));
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
