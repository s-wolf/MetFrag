/**
 * ESearchResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package gov.nih.nlm.ncbi.www.soap.eutils.esearch;

public class ESearchResult  implements java.io.Serializable {
    private java.lang.String ERROR;

    private java.lang.String count;

    private java.lang.String retMax;

    private java.lang.String retStart;

    private java.lang.String queryKey;

    private java.lang.String webEnv;

    private java.lang.String[] idList;

    private gov.nih.nlm.ncbi.www.soap.eutils.esearch.TranslationType[] translationSet;

    private gov.nih.nlm.ncbi.www.soap.eutils.esearch.TranslationStackType translationStack;

    private java.lang.String queryTranslation;

    private gov.nih.nlm.ncbi.www.soap.eutils.esearch.ErrorListType errorList;

    private gov.nih.nlm.ncbi.www.soap.eutils.esearch.WarningListType warningList;

    public ESearchResult() {
    }

    public ESearchResult(
           java.lang.String ERROR,
           java.lang.String count,
           java.lang.String retMax,
           java.lang.String retStart,
           java.lang.String queryKey,
           java.lang.String webEnv,
           java.lang.String[] idList,
           gov.nih.nlm.ncbi.www.soap.eutils.esearch.TranslationType[] translationSet,
           gov.nih.nlm.ncbi.www.soap.eutils.esearch.TranslationStackType translationStack,
           java.lang.String queryTranslation,
           gov.nih.nlm.ncbi.www.soap.eutils.esearch.ErrorListType errorList,
           gov.nih.nlm.ncbi.www.soap.eutils.esearch.WarningListType warningList) {
           this.ERROR = ERROR;
           this.count = count;
           this.retMax = retMax;
           this.retStart = retStart;
           this.queryKey = queryKey;
           this.webEnv = webEnv;
           this.idList = idList;
           this.translationSet = translationSet;
           this.translationStack = translationStack;
           this.queryTranslation = queryTranslation;
           this.errorList = errorList;
           this.warningList = warningList;
    }


    /**
     * Gets the ERROR value for this ESearchResult.
     * 
     * @return ERROR
     */
    public java.lang.String getERROR() {
        return ERROR;
    }


    /**
     * Sets the ERROR value for this ESearchResult.
     * 
     * @param ERROR
     */
    public void setERROR(java.lang.String ERROR) {
        this.ERROR = ERROR;
    }


    /**
     * Gets the count value for this ESearchResult.
     * 
     * @return count
     */
    public java.lang.String getCount() {
        return count;
    }


    /**
     * Sets the count value for this ESearchResult.
     * 
     * @param count
     */
    public void setCount(java.lang.String count) {
        this.count = count;
    }


    /**
     * Gets the retMax value for this ESearchResult.
     * 
     * @return retMax
     */
    public java.lang.String getRetMax() {
        return retMax;
    }


    /**
     * Sets the retMax value for this ESearchResult.
     * 
     * @param retMax
     */
    public void setRetMax(java.lang.String retMax) {
        this.retMax = retMax;
    }


    /**
     * Gets the retStart value for this ESearchResult.
     * 
     * @return retStart
     */
    public java.lang.String getRetStart() {
        return retStart;
    }


    /**
     * Sets the retStart value for this ESearchResult.
     * 
     * @param retStart
     */
    public void setRetStart(java.lang.String retStart) {
        this.retStart = retStart;
    }


    /**
     * Gets the queryKey value for this ESearchResult.
     * 
     * @return queryKey
     */
    public java.lang.String getQueryKey() {
        return queryKey;
    }


    /**
     * Sets the queryKey value for this ESearchResult.
     * 
     * @param queryKey
     */
    public void setQueryKey(java.lang.String queryKey) {
        this.queryKey = queryKey;
    }


    /**
     * Gets the webEnv value for this ESearchResult.
     * 
     * @return webEnv
     */
    public java.lang.String getWebEnv() {
        return webEnv;
    }


    /**
     * Sets the webEnv value for this ESearchResult.
     * 
     * @param webEnv
     */
    public void setWebEnv(java.lang.String webEnv) {
        this.webEnv = webEnv;
    }


    /**
     * Gets the idList value for this ESearchResult.
     * 
     * @return idList
     */
    public java.lang.String[] getIdList() {
        return idList;
    }


    /**
     * Sets the idList value for this ESearchResult.
     * 
     * @param idList
     */
    public void setIdList(java.lang.String[] idList) {
        this.idList = idList;
    }


    /**
     * Gets the translationSet value for this ESearchResult.
     * 
     * @return translationSet
     */
    public gov.nih.nlm.ncbi.www.soap.eutils.esearch.TranslationType[] getTranslationSet() {
        return translationSet;
    }


    /**
     * Sets the translationSet value for this ESearchResult.
     * 
     * @param translationSet
     */
    public void setTranslationSet(gov.nih.nlm.ncbi.www.soap.eutils.esearch.TranslationType[] translationSet) {
        this.translationSet = translationSet;
    }


    /**
     * Gets the translationStack value for this ESearchResult.
     * 
     * @return translationStack
     */
    public gov.nih.nlm.ncbi.www.soap.eutils.esearch.TranslationStackType getTranslationStack() {
        return translationStack;
    }


    /**
     * Sets the translationStack value for this ESearchResult.
     * 
     * @param translationStack
     */
    public void setTranslationStack(gov.nih.nlm.ncbi.www.soap.eutils.esearch.TranslationStackType translationStack) {
        this.translationStack = translationStack;
    }


    /**
     * Gets the queryTranslation value for this ESearchResult.
     * 
     * @return queryTranslation
     */
    public java.lang.String getQueryTranslation() {
        return queryTranslation;
    }


    /**
     * Sets the queryTranslation value for this ESearchResult.
     * 
     * @param queryTranslation
     */
    public void setQueryTranslation(java.lang.String queryTranslation) {
        this.queryTranslation = queryTranslation;
    }


    /**
     * Gets the errorList value for this ESearchResult.
     * 
     * @return errorList
     */
    public gov.nih.nlm.ncbi.www.soap.eutils.esearch.ErrorListType getErrorList() {
        return errorList;
    }


    /**
     * Sets the errorList value for this ESearchResult.
     * 
     * @param errorList
     */
    public void setErrorList(gov.nih.nlm.ncbi.www.soap.eutils.esearch.ErrorListType errorList) {
        this.errorList = errorList;
    }


    /**
     * Gets the warningList value for this ESearchResult.
     * 
     * @return warningList
     */
    public gov.nih.nlm.ncbi.www.soap.eutils.esearch.WarningListType getWarningList() {
        return warningList;
    }


    /**
     * Sets the warningList value for this ESearchResult.
     * 
     * @param warningList
     */
    public void setWarningList(gov.nih.nlm.ncbi.www.soap.eutils.esearch.WarningListType warningList) {
        this.warningList = warningList;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ESearchResult)) return false;
        ESearchResult other = (ESearchResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.ERROR==null && other.getERROR()==null) || 
             (this.ERROR!=null &&
              this.ERROR.equals(other.getERROR()))) &&
            ((this.count==null && other.getCount()==null) || 
             (this.count!=null &&
              this.count.equals(other.getCount()))) &&
            ((this.retMax==null && other.getRetMax()==null) || 
             (this.retMax!=null &&
              this.retMax.equals(other.getRetMax()))) &&
            ((this.retStart==null && other.getRetStart()==null) || 
             (this.retStart!=null &&
              this.retStart.equals(other.getRetStart()))) &&
            ((this.queryKey==null && other.getQueryKey()==null) || 
             (this.queryKey!=null &&
              this.queryKey.equals(other.getQueryKey()))) &&
            ((this.webEnv==null && other.getWebEnv()==null) || 
             (this.webEnv!=null &&
              this.webEnv.equals(other.getWebEnv()))) &&
            ((this.idList==null && other.getIdList()==null) || 
             (this.idList!=null &&
              java.util.Arrays.equals(this.idList, other.getIdList()))) &&
            ((this.translationSet==null && other.getTranslationSet()==null) || 
             (this.translationSet!=null &&
              java.util.Arrays.equals(this.translationSet, other.getTranslationSet()))) &&
            ((this.translationStack==null && other.getTranslationStack()==null) || 
             (this.translationStack!=null &&
              this.translationStack.equals(other.getTranslationStack()))) &&
            ((this.queryTranslation==null && other.getQueryTranslation()==null) || 
             (this.queryTranslation!=null &&
              this.queryTranslation.equals(other.getQueryTranslation()))) &&
            ((this.errorList==null && other.getErrorList()==null) || 
             (this.errorList!=null &&
              this.errorList.equals(other.getErrorList()))) &&
            ((this.warningList==null && other.getWarningList()==null) || 
             (this.warningList!=null &&
              this.warningList.equals(other.getWarningList())));
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
        if (getERROR() != null) {
            _hashCode += getERROR().hashCode();
        }
        if (getCount() != null) {
            _hashCode += getCount().hashCode();
        }
        if (getRetMax() != null) {
            _hashCode += getRetMax().hashCode();
        }
        if (getRetStart() != null) {
            _hashCode += getRetStart().hashCode();
        }
        if (getQueryKey() != null) {
            _hashCode += getQueryKey().hashCode();
        }
        if (getWebEnv() != null) {
            _hashCode += getWebEnv().hashCode();
        }
        if (getIdList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getIdList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getIdList(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getTranslationSet() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTranslationSet());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTranslationSet(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getTranslationStack() != null) {
            _hashCode += getTranslationStack().hashCode();
        }
        if (getQueryTranslation() != null) {
            _hashCode += getQueryTranslation().hashCode();
        }
        if (getErrorList() != null) {
            _hashCode += getErrorList().hashCode();
        }
        if (getWarningList() != null) {
            _hashCode += getWarningList().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ESearchResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", ">eSearchResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ERROR");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "ERROR"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("count");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "Count"));
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
        elemField.setFieldName("retStart");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "RetStart"));
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
        elemField.setFieldName("webEnv");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "WebEnv"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "IdList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "Id"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("translationSet");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "TranslationSet"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "TranslationType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "Translation"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("translationStack");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "TranslationStack"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "TranslationStackType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("queryTranslation");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "QueryTranslation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("errorList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "ErrorList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "ErrorListType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("warningList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "WarningList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.ncbi.nlm.nih.gov/soap/eutils/esearch", "WarningListType"));
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
