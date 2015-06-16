package com.simple8583.type;

import java.io.Serializable;

/**
 * <p>报文域类型接口定义.</p>
 * <p>继承Serializable接口，实例需要被序列化</p>
 *
 * @author Magic Joey
 * @version IsoType.java 1.0 Created@2015-06-15 22:42 $
 */
public interface IsoType extends Serializable{

    /**
     * 是否为变长域
     * @return
     */
    public boolean isLVar();

    /**
     * 字节数组转换为字符串储存
     * @param bts
     * @return
     */
    public abstract String setByteValue(byte[] bts);

    /**
     * 字符串转换为字节数组
     * @param bts
     * @return
     */
    public abstract byte[] setValue(String bts);


    /**
     * 变长长度
     * @return
     */
    public abstract int varLength(byte[] bytes);


}
