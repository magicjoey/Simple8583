package com.simple8583.model;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * Description: IsoPackage
 * Author: joey
 * Update: joey(2014-07-10 10:43)
 */
 @XmlAccessorType(XmlAccessType.FIELD)
 public class IsoPackage extends LinkedList<IsoField> implements Cloneable{

	private static final long serialVersionUID = 2715585287358366066L;
	
	@XmlAttribute(name="mti")
    private String mti;
	
    @XmlElement(name="field")
    public List<IsoField> getFields(){
        return this;
    }

    public String getMti() {
        return mti;
    }

    public void setMti(String mti) {
        this.mti = mti;
    }
    
    public IsoField getIsoField(String key){
    	for(IsoField isoField:this){
    		if(isoField.getId().equalsIgnoreCase(key)){
    			return isoField;
    		}
    	}
    	return null;
    }
    
    @Override
    public String toString(){
    	StringBuffer accum = new StringBuffer("[");
    	for(IsoField isoField:this){
    		accum.append(isoField.getId()).append(":").append(isoField.getValue()).append(",");
    	}
    	accum.append("]");
    	return accum.toString();
    }
    
 }

