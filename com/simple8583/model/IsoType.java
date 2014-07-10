package com.simple8583.model;

/**
 * Description: IsoType
 * Author: joey
 * Update: joey(2014-07- 13:59)
 * 定义了8583支持的数据类型
 */
public enum IsoType {

    //定义了8583协议中的几种数据格式，注：时间类型以及金额都当做NUMERIC类型处理
    //acs编码字符串为CHARACTER,
	//二进制类型为BINARY
	//,数字及bcd编码的格式为NUMERIC,
	//LLVAR为前置1字节长度字段,
	//LLLVAR为前置两字节长度字段
	//LLVAR_NUMERIC：一字节变长的数字类型(BCD编码)
    BINARY,CHAR,NUMERIC,LLVAR,LLLVAR,LLVAR_NUMERIC;

}




