/**
 * StructureSearchOptions.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.pubchem;

public class StructureSearchOptions  implements java.io.Serializable {
    private java.lang.Boolean matchIsotopes;

    private java.lang.Boolean matchCharges;

    private java.lang.Boolean matchTautomers;

    private java.lang.Boolean ringsNotEmbedded;

    private java.lang.Boolean singeDoubleBondsMatch;

    private java.lang.Boolean chainsMatchRings;

    private java.lang.Boolean stripHydrogen;

    private gov.nih.nlm.ncbi.pubchem.StereoType eStereo;

    private java.lang.String toWebEnv;

    public StructureSearchOptions() {
    }

    public StructureSearchOptions(
           java.lang.Boolean matchIsotopes,
           java.lang.Boolean matchCharges,
           java.lang.Boolean matchTautomers,
           java.lang.Boolean ringsNotEmbedded,
           java.lang.Boolean singeDoubleBondsMatch,
           java.lang.Boolean chainsMatchRings,
           java.lang.Boolean stripHydrogen,
           gov.nih.nlm.ncbi.pubchem.StereoType eStereo,
           java.lang.String toWebEnv) {
           this.matchIsotopes = matchIsotopes;
           this.matchCharges = matchCharges;
           this.matchTautomers = matchTautomers;
           this.ringsNotEmbedded = ringsNotEmbedded;
           this.singeDoubleBondsMatch = singeDoubleBondsMatch;
           this.chainsMatchRings = chainsMatchRings;
           this.stripHydrogen = stripHydrogen;
           this.eStereo = eStereo;
           this.toWebEnv = toWebEnv;
    }


    /**
     * Gets the matchIsotopes value for this StructureSearchOptions.
     * 
     * @return matchIsotopes
     */
    public java.lang.Boolean getMatchIsotopes() {
        return matchIsotopes;
    }


    /**
     * Sets the matchIsotopes value for this StructureSearchOptions.
     * 
     * @param matchIsotopes
     */
    public void setMatchIsotopes(java.lang.Boolean matchIsotopes) {
        this.matchIsotopes = matchIsotopes;
    }


    /**
     * Gets the matchCharges value for this StructureSearchOptions.
     * 
     * @return matchCharges
     */
    public java.lang.Boolean getMatchCharges() {
        return matchCharges;
    }


    /**
     * Sets the matchCharges value for this StructureSearchOptions.
     * 
     * @param matchCharges
     */
    public void setMatchCharges(java.lang.Boolean matchCharges) {
        this.matchCharges = matchCharges;
    }


    /**
     * Gets the matchTautomers value for this StructureSearchOptions.
     * 
     * @return matchTautomers
     */
    public java.lang.Boolean getMatchTautomers() {
        return matchTautomers;
    }


    /**
     * Sets the matchTautomers value for this StructureSearchOptions.
     * 
     * @param matchTautomers
     */
    public void setMatchTautomers(java.lang.Boolean matchTautomers) {
        this.matchTautomers = matchTautomers;
    }


    /**
     * Gets the ringsNotEmbedded value for this StructureSearchOptions.
     * 
     * @return ringsNotEmbedded
     */
    public java.lang.Boolean getRingsNotEmbedded() {
        return ringsNotEmbedded;
    }


    /**
     * Sets the ringsNotEmbedded value for this StructureSearchOptions.
     * 
     * @param ringsNotEmbedded
     */
    public void setRingsNotEmbedded(java.lang.Boolean ringsNotEmbedded) {
        this.ringsNotEmbedded = ringsNotEmbedded;
    }


    /**
     * Gets the singeDoubleBondsMatch value for this StructureSearchOptions.
     * 
     * @return singeDoubleBondsMatch
     */
    public java.lang.Boolean getSingeDoubleBondsMatch() {
        return singeDoubleBondsMatch;
    }


    /**
     * Sets the singeDoubleBondsMatch value for this StructureSearchOptions.
     * 
     * @param singeDoubleBondsMatch
     */
    public void setSingeDoubleBondsMatch(java.lang.Boolean singeDoubleBondsMatch) {
        this.singeDoubleBondsMatch = singeDoubleBondsMatch;
    }


    /**
     * Gets the chainsMatchRings value for this StructureSearchOptions.
     * 
     * @return chainsMatchRings
     */
    public java.lang.Boolean getChainsMatchRings() {
        return chainsMatchRings;
    }


    /**
     * Sets the chainsMatchRings value for this StructureSearchOptions.
     * 
     * @param chainsMatchRings
     */
    public void setChainsMatchRings(java.lang.Boolean chainsMatchRings) {
        this.chainsMatchRings = chainsMatchRings;
    }


    /**
     * Gets the stripHydrogen value for this StructureSearchOptions.
     * 
     * @return stripHydrogen
     */
    public java.lang.Boolean getStripHydrogen() {
        return stripHydrogen;
    }


    /**
     * Sets the stripHydrogen value for this StructureSearchOptions.
     * 
     * @param stripHydrogen
     */
    public void setStripHydrogen(java.lang.Boolean stripHydrogen) {
        this.stripHydrogen = stripHydrogen;
    }


    /**
     * Gets the eStereo value for this StructureSearchOptions.
     * 
     * @return eStereo
     */
    public gov.nih.nlm.ncbi.pubchem.StereoType getEStereo() {
        return eStereo;
    }


    /**
     * Sets the eStereo value for this StructureSearchOptions.
     * 
     * @param eStereo
     */
    public void setEStereo(gov.nih.nlm.ncbi.pubchem.StereoType eStereo) {
        this.eStereo = eStereo;
    }


    /**
     * Gets the toWebEnv value for this StructureSearchOptions.
     * 
     * @return toWebEnv
     */
    public java.lang.String getToWebEnv() {
        return toWebEnv;
    }


    /**
     * Sets the toWebEnv value for this StructureSearchOptions.
     * 
     * @param toWebEnv
     */
    public void setToWebEnv(java.lang.String toWebEnv) {
        this.toWebEnv = toWebEnv;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof StructureSearchOptions)) return false;
        StructureSearchOptions other = (StructureSearchOptions) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.matchIsotopes==null && other.getMatchIsotopes()==null) || 
             (this.matchIsotopes!=null &&
              this.matchIsotopes.equals(other.getMatchIsotopes()))) &&
            ((this.matchCharges==null && other.getMatchCharges()==null) || 
             (this.matchCharges!=null &&
              this.matchCharges.equals(other.getMatchCharges()))) &&
            ((this.matchTautomers==null && other.getMatchTautomers()==null) || 
             (this.matchTautomers!=null &&
              this.matchTautomers.equals(other.getMatchTautomers()))) &&
            ((this.ringsNotEmbedded==null && other.getRingsNotEmbedded()==null) || 
             (this.ringsNotEmbedded!=null &&
              this.ringsNotEmbedded.equals(other.getRingsNotEmbedded()))) &&
            ((this.singeDoubleBondsMatch==null && other.getSingeDoubleBondsMatch()==null) || 
             (this.singeDoubleBondsMatch!=null &&
              this.singeDoubleBondsMatch.equals(other.getSingeDoubleBondsMatch()))) &&
            ((this.chainsMatchRings==null && other.getChainsMatchRings()==null) || 
             (this.chainsMatchRings!=null &&
              this.chainsMatchRings.equals(other.getChainsMatchRings()))) &&
            ((this.stripHydrogen==null && other.getStripHydrogen()==null) || 
             (this.stripHydrogen!=null &&
              this.stripHydrogen.equals(other.getStripHydrogen()))) &&
            ((this.eStereo==null && other.getEStereo()==null) || 
             (this.eStereo!=null &&
              this.eStereo.equals(other.getEStereo()))) &&
            ((this.toWebEnv==null && other.getToWebEnv()==null) || 
             (this.toWebEnv!=null &&
              this.toWebEnv.equals(other.getToWebEnv())));
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
        if (getMatchIsotopes() != null) {
            _hashCode += getMatchIsotopes().hashCode();
        }
        if (getMatchCharges() != null) {
            _hashCode += getMatchCharges().hashCode();
        }
        if (getMatchTautomers() != null) {
            _hashCode += getMatchTautomers().hashCode();
        }
        if (getRingsNotEmbedded() != null) {
            _hashCode += getRingsNotEmbedded().hashCode();
        }
        if (getSingeDoubleBondsMatch() != null) {
            _hashCode += getSingeDoubleBondsMatch().hashCode();
        }
        if (getChainsMatchRings() != null) {
            _hashCode += getChainsMatchRings().hashCode();
        }
        if (getStripHydrogen() != null) {
            _hashCode += getStripHydrogen().hashCode();
        }
        if (getEStereo() != null) {
            _hashCode += getEStereo().hashCode();
        }
        if (getToWebEnv() != null) {
            _hashCode += getToWebEnv().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(StructureSearchOptions.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "StructureSearchOptions"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("matchIsotopes");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "MatchIsotopes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("matchCharges");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "MatchCharges"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("matchTautomers");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "MatchTautomers"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ringsNotEmbedded");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "RingsNotEmbedded"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("singeDoubleBondsMatch");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "SingeDoubleBondsMatch"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("chainsMatchRings");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "ChainsMatchRings"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stripHydrogen");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "StripHydrogen"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("EStereo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "eStereo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "StereoType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("toWebEnv");
        elemField.setXmlName(new javax.xml.namespace.QName("http://pubchem.ncbi.nlm.nih.gov/", "ToWebEnv"));
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
