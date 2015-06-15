package com.simple8583.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <p>报文域详情.</p>
 *
 * @author Magic Joey
 * @version IsoContainer.java 1.0 Created@2014-06-06 13:57 $
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="simple8583-config")
public class IsoHeaderList  extends ArrayList<IsoField> implements Cloneable {

	private static final long serialVersionUID = 264233287032969509L;

	@XmlElementWrapper(name="header")
    @XmlElement(name="field")
    public List<IsoField> getHeaderList(){
        return this;
    }
}


