package com.simple8583.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleUtil {
	
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
	
	//恒返回两字节
	public static byte[] msgLength(int length){
		byte[] rByte = new byte[2];
		rByte[0] = (byte)(length/255);
		rByte[1] = (byte)(length%255);
		return rByte;
	}
	
	public static String tlv(String tag,String flag){
		StringBuffer accum = new StringBuffer(tag);
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
	
	public static void main(String[] args) {
		String a = EncodeUtil.hex(msgLength(255));
		System.out.println(a);
	}
	
}