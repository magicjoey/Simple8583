package com.simple8583.util;

/**
 * <p>编码转换工具类.如:BCD和HEX</p>
 *
 * @author Magic Joey
 * @version EncodeUtil.java 1.0 @2014-07-10 09:51 $
 */
public class EncodeUtil {

    protected static final char[] HEX = new char[]{'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

    protected static final char[] BINARY = new char[]{'0','1'};

    private EncodeUtil(){

    }

    //传入参数为只有01的字符串
    public static byte[] binary(String binaryStr){
        //长度不是8倍数的话，无法知道在左边或右边补零，会引起歧义，导致结果不正确
        if(binaryStr.length()%8!=0){
            throw new IllegalArgumentException("传入的参数长度必须是8的倍数");
        }
        StringBuffer accum = new StringBuffer();
        for(int i=0;i<binaryStr.length();i+=4){
            String temp = binaryStr.substring(i,i+4);
            int value=0;
            for(int j=0;j<4;j++){
                if(temp.charAt(j)=='1'){
                    value+=Math.pow(2, 3 - j);//计算值
                }
            }
            accum.append(HEX[value]);
        }
        return bcd(accum.toString());
    }

    public static String hex(byte[] bts,int offset,int length){//
        if(offset<0||length<0||bts.length<offset+length-1){
            throw new IllegalArgumentException("参数非法：offset:"+offset+",length:"+length+",字节长度："+bts.length);
        }
        byte[] returnBt = new byte[length];
        System.arraycopy(returnBt,0,bts,offset,length);
        return hex(returnBt);
    }


    /**
     * 将byte数组转化为String类型的十六进制编码格式
     * 本方法实现的思路是：
     * 1）每位byte数组转换为2位的十六进制数
     * 2）将字节左移4位取得高四位字节数值，获取对应的char类型数组编码
     * 3）将字节与0x0F按位与，从而获取第四位的字节，同样获取编码
     */
    public static String hex(byte[] bParam){
        StringBuilder accum = new StringBuilder();
        for(byte bt:bParam){
             accum.append(HEX[bt>>4&0x0F]);//&0x0F的目的是为了转换负数
             accum.append(HEX[bt&0x0F]);
        }
        return accum.toString();
    }

    public static byte[] bcd(int code,int len){
        return EncodeUtil.bcd(String.valueOf(code),len);
    }

    
    public static String binary(byte[] bts){
    	StringBuffer accum = new StringBuffer();
    	for(byte bt:bts){
    		accum.append(binary(bt));
    	}
    	return accum.toString();
    }
    
    //本方法修改于Integer.toBinaryString
    //参数的每个字节都会转化为8位2进制字符串，如1会转换为00000001
    private static String binary(byte bt){
    	int num = bt&0xFF;
    	char[] arrayOfChar = new char[8];
		int i = 8;
		for(int times=0;times<8;times++){
			arrayOfChar[(--i)] = BINARY[(num & 0x01)];
			num >>>= 1;
		}
		return new String(arrayOfChar);
    }

    /**BCD编码(8421码)为一个4位表示一个10进制数，即每个字节表示两个数
    *本方法中的code为10进制数（本方法支持16进制数编码，每两位编为1字节）
    */
    public static byte[] bcd(String code){
        //控制byte数组的大小
        int len = code.length()%2==0?code.length()/2:code.length()/2+1;
        return bcd(code,len);
    }
    

    /**
     * @param code
     * @param length
     * @return
     */
    public static byte[] bcd(String code,int length){
        if(length<0){
            throw new IllegalArgumentException("参数length不能小于0,length:"+length);
        }else if(length==0){
            return new byte[0];
        }
        byte[] bt = new byte[length];
        //指示当前位置
        int point = 0;
        if(code.length()<2*length){
        	code = addBlankLeft(code,2*length-code.length(),"0");
        }
        
        //每两位合并为一个字节
        for(;point<code.length();point+=2){
            //(point+1)/2计算当前指向的值
            //Character.digit将对应的Char转为数字，如'8'-> 8
            //<<4左移四位：即为→_→（右边）的数字让开位置
            bt[(point+1)/2] = (byte)(Character.digit(code.charAt(point),16)<<4|Character.digit(code.charAt(point+1),16));
        }
        return bt;
    }
    
    public static String addBlankLeft(String origStr,int length,String fill){
    	if(length<=0){
    		return origStr;
    	}
    	StringBuffer accum = new StringBuffer(); 	
    	for(int i=0;i<length;i++){
    	 		accum.append(fill);
    	 	}
    	accum.append(origStr);
    	return accum.toString();
    }
    
    public static String addBlankRight(String origStr,int length,String fill){
    	if(length<=0){
    		return origStr;
    	}
    	StringBuffer accum = new StringBuffer(origStr); 	
    	for(int i=0;i<length;i++){
    	 		accum.append(fill);
    	 	}
    	return accum.toString();
    }
}
