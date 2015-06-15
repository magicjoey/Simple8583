package com.simple8583.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>编码转换工具类.如:BCD和HEX</p>
 *
 * @author Magic Joey
 * @version EncodeUtil.java 1.0 @2014-07-10 09:51 $
 */
public class SimpleUtil {

    //不允许实例化
    private SimpleUtil(){

    }
	
	//判断字符串是否都是由数字组成
	public static boolean isNumeric(String str){
		if(str==null){
			return false;
		}
	   Pattern pattern = Pattern.compile("[0-9]*"); 
	   Matcher isNum = pattern.matcher(str);
	   if( !isNum.matches() ){
	       return false; 
	   } 
	   return true; 
	}
	
	//用于处理tlv格式的数据,返回tag+length+flag格式
	public static String tlv(String tag,String flag){
		StringBuffer accum = new StringBuffer(tag);
		//将flag长度偶数化
		accum.append(evenLength(flag));
		accum.append(flag);
		return accum.toString();
	}
	
	public static String evenLength(String value){
		if(String.valueOf(value.length()).length()%2==1){
			return "0"+value.length();			
		}else{
			return String.valueOf(value.length());
		}
	}
	
	public static String date(){
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		return format.format(new Date());
	}
	
	public static String time(){
		SimpleDateFormat format = new SimpleDateFormat("HHmmss");
		return format.format(new Date());
	}

}