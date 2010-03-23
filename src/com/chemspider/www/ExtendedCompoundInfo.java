/**
 * ExtendedCompoundInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.chemspider.www;

public class ExtendedCompoundInfo  implements java.io.Serializable {
    private int CSID;

    private java.lang.String MF;

    private java.lang.String SMILES;

    private java.lang.String inChI;

    private java.lang.String inChIKey;

    private double averageMass;

    private double molecularWeight;

    private double monoisotopicMass;

    private double nominalMass;

    private double ALogP;

    private double XLogP;

    private java.lang.String commonName;

    public ExtendedCompoundInfo() {
    }

    public ExtendedCompoundInfo(
           int CSID,
           java.lang.String MF,
           java.lang.String SMILES,
           java.lang.String inChI,
           java.lang.String inChIKey,
           double averageMass,
           double molecularWeight,
           double monoisotopicMass,
           double nominalMass,
           double ALogP,
           double XLogP,
           java.lang.String commonName) {
           this.CSID = CSID;
           this.MF = MF;
           this.SMILES = SMILES;
           this.inChI = inChI;
           this.inChIKey = inChIKey;
           this.averageMass = averageMass;
           this.molecularWeight = molecularWeight;
           this.monoisotopicMass = monoisotopicMass;
           this.nominalMass = nominalMass;
           this.ALogP = ALogP;
           this.XLogP = XLogP;
           this.commonName = commonName;
    }


    /**
     * Gets the CSID value for this ExtendedCompoundInfo.
     * 
     * @return CSID
     */
    public int getCSID() {
        return CSID;
    }


    /**
     * Sets the CSID value for this ExtendedCompoundInfo.
     * 
     * @param CSID
     */
    public void setCSID(int CSID) {
        this.CSID = CSID;
    }


    /**
     * Gets the MF value for this ExtendedCompoundInfo.
     * 
     * @return MF
     */
    public java.lang.String getMF() {
        return MF;
    }


    /**
     * Sets the MF value for this ExtendedCompoundInfo.
     * 
     * @param MF
     */
    public void setMF(java.lang.String MF) {
        this.MF = MF;
    }


    /**
     * Gets the SMILES value for this ExtendedCompoundInfo.
     * 
     * @return SMILES
     */
    public java.lang.String getSMILES() {
        return SMILES;
    }


    /**
     * Sets the SMILES value for this ExtendedCompoundInfo.
     * 
     * @param SMILES
     */
    public void setSMILES(java.lang.String SMILES) {
        this.SMILES = SMILES;
    }


    /**
     * Gets the inChI value for this ExtendedCompoundInfo.
     * 
     * @return inChI
     */
    public java.lang.String getInChI() {
        return inChI;
    }


    /**
     * Sets the inChI value for this ExtendedCompoundInfo.
     * 
     * @param inChI
     */
    public void setInChI(java.lang.String inChI) {
        this.inChI = inChI;
    }


    /**
     * Gets the inChIKey value for this ExtendedCompoundInfo.
     * 
     * @return inChIKey
     */
    public java.lang.String getInChIKey() {
        return inChIKey;
    }


    /**
     * Sets the inChIKey value for this ExtendedCompoundInfo.
     * 
     * @param inChIKey
     */
    public void setInChIKey(java.lang.String inChIKey) {
        this.inChIKey = inChIKey;
    }


    /**
     * Gets the averageMass value for this ExtendedCompoundInfo.
     * 
     * @return averageMass
     */
    public double getAverageMass() {
        return averageMass;
    }


    /**
     * Sets the averageMass value for this ExtendedCompoundInfo.
     * 
     * @param averageMass
     */
    public void setAverageMass(double averageMass) {
        this.averageMass = averageMass;
    }


    /**
     * Gets the molecularWeight value for this ExtendedCompoundInfo.
     * 
     * @return molecularWeight
     */
    public double getMolecularWeight() {
        return molecularWeight;
    }


    /**
     * Sets the molecularWeight value for this ExtendedCompoundInfo.
     * 
     * @param molecularWeight
     */
    public void setMolecularWeight(double molecularWeight) {
        this.molecularWeight = molecularWeight;
    }


    /**
     * Gets the monoisotopicMass value for this ExtendedCompoundInfo.
     * 
     * @return monoisotopicMass
     */
    public double getMonoisotopicMass() {
        return monoisotopicMass;
    }


    /**
     * Sets the monoisotopicMass value for this ExtendedCompoundInfo.
     * 
     * @param monoisotopicMass
     */
    public void setMonoisotopicMass(double monoisotopicMass) {
        this.monoisotopicMass = monoisotopicMass;
    }


    /**
     * Gets the nominalMass value for this ExtendedCompoundInfo.
     * 
     * @return nominalMass
     */
    public double getNominalMass() {
        return nominalMass;
    }


    /**
     * Sets the nominalMass value for this ExtendedCompoundInfo.
     * 
     * @param nominalMass
     */
    public void setNominalMass(double nominalMass) {
        this.nominalMass = nominalMass;
    }


    /**
     * Gets the ALogP value for this ExtendedCompoundInfo.
     * 
     * @return ALogP
     */
    public double getALogP() {
        return ALogP;
    }


    /**
     * Sets the ALogP value for this ExtendedCompoundInfo.
     * 
     * @param ALogP
     */
    public void setALogP(double ALogP) {
        this.ALogP = ALogP;
    }


    /**
     * Gets the XLogP value for this ExtendedCompoundInfo.
     * 
     * @return XLogP
     */
    public double getXLogP() {
        return XLogP;
    }


    /**
     * Sets the XLogP value for this ExtendedCompoundInfo.
     * 
     * @param XLogP
     */
    public void setXLogP(double XLogP) {
        this.XLogP = XLogP;
    }


    /**
     * Gets the commonName value for this ExtendedCompoundInfo.
     * 
     * @return commonName
     */
    public java.lang.String getCommonName() {
        return commonName;
    }


    /**
     * Sets the commonName value for this ExtendedCompoundInfo.
     * 
     * @param commonName
     */
    public void setCommonName(java.lang.String commonName) {
        this.commonName = commonName;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ExtendedCompoundInfo)) return false;
        ExtendedCompoundInfo other = (ExtendedCompoundInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.CSID == other.getCSID() &&
            ((this.MF==null && other.getMF()==null) || 
             (this.MF!=null &&
              this.MF.equals(other.getMF()))) &&
            ((this.SMILES==null && other.getSMILES()==null) || 
             (this.SMILES!=null &&
              this.SMILES.equals(other.getSMILES()))) &&
            ((this.inChI==null && other.getInChI()==null) || 
             (this.inChI!=null &&
              this.inChI.equals(other.getInChI()))) &&
            ((this.inChIKey==null && other.getInChIKey()==null) || 
             (this.inChIKey!=null &&
              this.inChIKey.equals(other.getInChIKey()))) &&
            this.averageMass == other.getAverageMass() &&
            this.molecularWeight == other.getMolecularWeight() &&
            this.monoisotopicMass == other.getMonoisotopicMass() &&
            this.nominalMass == other.getNominalMass() &&
            this.ALogP == other.getALogP() &&
            this.XLogP == other.getXLogP() &&
            ((this.commonName==null && other.getCommonName()==null) || 
             (this.commonName!=null &&
              this.commonName.equals(other.getCommonName())));
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
        _hashCode += getCSID();
        if (getMF() != null) {
            _hashCode += getMF().hashCode();
        }
        if (getSMILES() != null) {
            _hashCode += getSMILES().hashCode();
        }
        if (getInChI() != null) {
            _hashCode += getInChI().hashCode();
        }
        if (getInChIKey() != null) {
            _hashCode += getInChIKey().hashCode();
        }
        _hashCode += new Double(getAverageMass()).hashCode();
        _hashCode += new Double(getMolecularWeight()).hashCode();
        _hashCode += new Double(getMonoisotopicMass()).hashCode();
        _hashCode += new Double(getNominalMass()).hashCode();
        _hashCode += new Double(getALogP()).hashCode();
        _hashCode += new Double(getXLogP()).hashCode();
        if (getCommonName() != null) {
            _hashCode += getCommonName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ExtendedCompoundInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.chemspider.com/", "ExtendedCompoundInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CSID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "CSID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("MF");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "MF"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("SMILES");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "SMILES"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("inChI");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "InChI"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("inChIKey");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "InChIKey"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("averageMass");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "AverageMass"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("molecularWeight");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "MolecularWeight"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("monoisotopicMass");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "MonoisotopicMass"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nominalMass");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "NominalMass"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ALogP");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "ALogP"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("XLogP");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "XLogP"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("commonName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.chemspider.com/", "CommonName"));
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
