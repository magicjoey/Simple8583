package com.simple8583.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>8583报文包.</p>
 *
 * @author Magic Joey
 * @version Simple8583Test.java 1.0 @2014-07-10 10:43 $
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

    /**
     * 深度拷贝方法
     * @return
     * @throws java.io.IOException
     * @throws ClassNotFoundException
     */
    public IsoPackage deepClone() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteOut = null;
        ObjectOutputStream out = null;
        ByteArrayInputStream byteIn = null;
        ObjectInputStream in = null;
        IsoPackage isoPackage;
        try{
            byteOut = new ByteArrayOutputStream();
            out =  new ObjectOutputStream(byteOut);
            out.writeObject(this);

            byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            in  =new ObjectInputStream(byteIn);
            isoPackage =  (IsoPackage)in.readObject();
        }finally{
            if(byteOut!=null){
                byteOut.close();
            }
            if(out!=null){
                out.close();
            }
            if(byteIn!=null){
                byteIn.close();
            }
            if(in!=null){
                in.close();
            }
        }
        return isoPackage;
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

