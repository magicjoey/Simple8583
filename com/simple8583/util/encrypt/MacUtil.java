package com.simple8583.util.encrypt;


import java.io.UnsupportedEncodingException;
import java.util.Random;

import com.simple8583.util.EncodeUtil;

/**
 * <p>DES加密类</p>
 *
 * @author Magic Joey
 * @version MacUtil.java 1.0 @2014-06-09 17:57$
 */
public class MacUtil {
	public static String ZPK = "46FE4CA7D38564DF1A86B962BF2F0291";
	public static String ZAK = "A1D610A2D9E00B5D5446E6F43E293719";
	
	private static final int[][] s1 = 
		{
			{14,4, 13,1, 2, 15,11,8, 3, 10,6, 12,5, 9, 0, 7},
			{0, 15,7, 4, 14,2, 13,1, 10,6, 12,11,9, 5, 3, 8},
			{4, 1, 14,8, 13,6, 2, 11,15,12,9, 7, 3, 10,5, 0},
			{15,12,8, 2, 4, 9, 1, 7, 5, 11,3, 14,10,0, 6, 13}
		 };

	private static final int[][] s2 = 
		{
			{15,1, 8, 14,6, 11,3, 4, 9, 7, 2, 13,12,0, 5, 10},
			{3, 13,4, 7, 15,2, 8, 14,12,0, 1, 10,6, 9, 11,5},
			{0, 14,7, 11,10,4, 13,1, 5, 8, 12,6, 9, 3, 2, 15},
			{13,8, 10,1, 3, 15,4, 2, 11,6, 7, 12,0, 5, 14,9}
		};
	private static final int[][] s3 = 
		{
			{10,0, 9, 14,6, 3, 15,5, 1, 13,12,7, 11,4, 2, 8},
		 	{13,7, 0, 9, 3, 4, 6, 10,2, 8, 5, 14,12,11,15,1},
		    {13,6, 4, 9, 8, 15,3, 0, 11,1, 2, 12,5, 10,14,7},
		    {1, 10,13,0, 6, 9, 8, 7, 4, 15,14,3, 11,5, 2, 12}
		};
	private static final int[][] s4 = 
		{
			{7, 13,14,3, 0, 6, 9, 10,1, 2, 8, 5, 11,12,4, 15},
		    {13,8, 11,5, 6, 15,0, 3, 4, 7, 2, 12,1, 10,14,9},
		    {10,6, 9, 0, 12,11,7, 13,15,1, 3, 14,5, 2, 8, 4},
		    {3, 15,0, 6, 10,1, 13,8, 9, 4, 5, 11,12,7, 2, 14}
		};
	private static final int[][] s5 = 
		{
			{2, 12,4, 1, 7, 10,11,6, 8, 5, 3, 15,13,0, 14,9},
		    {14,11,2, 12,4, 7, 13,1, 5, 0, 15,10,3, 9, 8, 6},
		    {4, 2, 1, 11,10,13,7, 8, 15,9, 12,5, 6, 3, 0, 14},
		    {11,8, 12,7, 1, 14,2, 13,6, 15,0, 9, 10,4, 5, 3}
		};

	private static final int[][] s6 = 
		{
			{12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
		    {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
		    {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
		    {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}
		};
	private static final int[][] s7 =
		{
			{4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
		    {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
		    {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
		    {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}
		};

	private static final int[][] s8 = 
		{
			{13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
			{1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
			{7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
			{2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
		};

	private static final int[] ip = 
		{58, 50, 42, 34, 26, 18, 10, 2,
		60, 52, 44, 36, 28, 20, 12, 4,
		62, 54, 46, 38, 30, 22, 14, 6,
		64, 56, 48, 40, 32, 24, 16, 8,
		57, 49, 41, 33, 25, 17, 9, 1,
		59, 51, 43, 35, 27, 19, 11, 3,
		61, 53, 45, 37, 29, 21, 13, 5,
		63, 55, 47, 39, 31, 23, 15, 7};

	private static final int[] _ip = 
		{40, 8, 48, 16, 56, 24, 64, 32,
		39, 7, 47, 15, 55, 23, 63, 31,
		38, 6, 46, 14, 54, 22, 62, 30,
		37, 5, 45, 13, 53, 21, 61, 29,
		36, 4, 44, 12, 52, 20, 60, 28,
		35, 3, 43, 11, 51, 19, 59, 27,
		34, 2, 42, 10, 50, 18, 58, 26,
		33, 1, 41, 9, 49, 17, 57, 25};

	private static final int[] LS = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1}; 
	private static int[][] subKey = new int[16][48];
	private static int HEX = 0;

	private static int ASC = 1;
	
	
	/**
	 * 将16进制字符转换成对应的int类型值:如字符'A'被转换后为10;'B'将被转换为11
	 * @param ch 需要被转换的目标字符
	 * @return	转换后的int类型值
	 * @throws Exception 如果传入字符为非16进制字符，将会报'getIntByChar was wrong'自定义异常
	 */
	public static int getIntByChar(char ch) throws Exception
	{
		char t = Character.toUpperCase(ch);
		int i = 0;
		switch(t){
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				i = Integer.parseInt(Character.toString(t));
				break;
			case 'A':
				i = 10;
				break;
			case 'B':
				i = 11;
				break;
			case 'C':
				i = 12;
				break;
			case 'D':
				i = 13;
				break;
			case 'E':
				i = 14;
				break;
			case 'F':
				i = 15;
				break;
			default:
				throw new Exception("getIntByChar was wrong");
		}
		return i;
	}

	/**
	 * 将16进制表示的字符串转换为0,1组成的int数组
	 * 列如："ABC" 被转换后的结果为{1,0,1,0,1,0,1,1,1,1,0,0}
	 * @param source 十六进制字符串
	 * @return 0,1组成的int数组
	 */
	public static int[] string2Binary(String source)
	{
		int len = source.length();
		int[] dest = new int[len*4];
		char[] arr = source.toCharArray();
		for(int i=0; i<len; i++)
		{
			int t = 0;
			try {
				t = getIntByChar(arr[i]);
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
	
			String[] str = Integer.toBinaryString(t).split("");
			int k = i*4 + 3;
			for(int j=str.length-1; j>0; j--)
			{
				dest[k] = Integer.parseInt(str[j]);
				k--;
			}
		}
		return dest;
	}


	/**
	 * 完成对底数X的Y次方操作,如输入值分别为：2和4 返回值为16
	 * @param x 执行幂操作的底数
	 * @param y	执行幂操作的指数
	 * @return	幂操作的结果
	 */
	public static int getXY(int x,int y)
	{
		int temp = x;
		if(y == 0) x = 1;
		for(int i=2; i<=y; i++)
		{
			x *= temp;  
		}
		return x;
	}

	/**
	 * 将由0,1组成的长度为4的字符串转化为对应的16进制字符串表示，例如输入字符串"1111",输出值为"F"
	 * 注意此方法输入的字符串长度最大为4，一次只能转换成为一个16进制字符
	 * @param s 0,1组成的字符串
	 * @return 转换后的16进制字符
	 */
	public static String binary2Hex(String s)
	{
		int len = s.length();
		int result = 0;
		int k = 0;
		if(len > 4)
			return null;
		for(int i=len; i>0; i--)
		{
			result += Integer.parseInt(s.substring(i-1, i))*getXY(2,k);
			k++;
		}
		switch(result){
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
				return ""+result;
			case 10:
				return "A";
			case 11:
				return "B";
			case 12:
				return "C";
			case 13:
				return "D";
			case 14:
				return "E";
			case 15:
				return "F";
			default :
				return null;
		}
	}

	/**
	 * 将一个小于16的数转换为一个对应的十六进制字符
	 * @param i 小于16的整数
	 * @return 十六进制字符
	 */
	public static String int2Hex(int i)
	{
		switch(i){
			case 0:
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
				return ""+i;
			case 10:
				return "A";
			case 11:
				return "B";
			case 12:
				return "C";
			case 13:
				return "D";
			case 14:
				return "E";
			case 15:
				return "F";
			default :
				return null;
		}
	}

	/**
	 * 将0,1组成的字符串转换为对应的十六进制字符串，例如输入字符串"111100001111"输出为"FOF"
	 * 此方法输入的字符串长度不限
	 * @param s 0,1组成的字符串
	 * @return	十六进制字符串
	 */
	public static String binary2ASC(String s)
	{
		String str = "";
		int ii = 0;
		int len = s.length();
		if(len%4 != 0)
		{
			while(ii<4-len%4)
			{
				s = "0" + s;
			}
		}
		for(int i=0; i<len/4; i++)
		{
			str += binary2Hex(s.substring(i*4, i*4+4));
		}
		return str;
	}

	public static int[] changeIP(int[] source)
	{
		int[] dest = new int[64];
		for(int i=0; i<64; i++)
		{
			dest[i] = source[ip[i]-1];
		}
		return dest;
	}


	public static int[] changeInverseIP(int[] source)
	{
		int[] dest = new int[64];
		for(int i=0; i<64; i++)
		{
			dest[i] = source[_ip[i]-1];
		}
		return dest;
	}

		 
	public static int[] expend(int[] source)
	{
		int[] ret = new int[48];
		int[] temp = {32, 1, 2, 3, 4, 5,
		4, 5, 6, 7, 8, 9,
		8, 9, 10, 11, 12, 13,
		12, 13, 14, 15, 16, 17,
		16, 17, 18, 19, 20, 21,
		20, 21, 22, 23, 24, 25,
		24, 25, 26, 27, 28, 29,
		28, 29, 30, 31, 32, 1};
		for(int i=0; i<48; i++)
		{
			ret[i] = source[temp[i]-1];
		}
		return ret;
	} 


	public static int[] press(int[] source)
	{
		int[] ret = new int[32];
		int[][] temp = new int[8][6];
		int[][][] s = {s1,s2,s3,s4,s5,s6,s7,s8};
		StringBuffer str = new StringBuffer();
		for(int i=0; i<8; i++)
		{
			for(int j=0; j<6; j++)
			{
				temp[i][j] = source[i*6+j];
			}
		}

		for(int i=0; i<8; i++)
		{
			int x = temp[i][0]*2 + temp[i][5];
			int y = temp[i][1]*8 + temp[i][2]*4 + temp[i][3]*2 + temp[i][4];
			int val = s[i][x][y];
			String ch = int2Hex(val);
			str.append(ch);
		}

		ret = string2Binary(str.toString());
		ret = dataP(ret);
		
		return ret;
	}

	public static int[] dataP(int[] source)
	{
		int[] dest = new int[32];
		int[] temp = 
		{16, 7, 20, 21,
		 29, 12, 28, 17,
		 1, 15, 23, 26,
		 5, 18, 31, 10,
		 2, 8, 24, 14,
		 32, 27, 3, 9,
		 19, 13, 30, 6,
		 22, 11, 4, 25};

		int len = source.length;
		for(int i=0; i<len; i++)
		{
			dest[i] = source[temp[i]-1];
		}

		return dest;
	}

	public static int[] f(int[] R,int[] K)
	{
		int[] dest = new int[32];
		int[] temp = new int[48]; 
		int[] expendR = expend(R);

		temp = diffOr(expendR, K);
		dest = press(temp);

		return dest;
	}


	/**
	 * 对两个int类型数组执行异或操作
	 * @param source1 操作数1
	 * @param source2 操作数2
	 * @return 异或结果
	 */
	public static int[] diffOr(int[] source1,int[] source2)
	{
		int len = source1.length;
		int[] dest = new int[len];

		for(int i=0; i<len; i++)
		{
			dest[i] = source1[i] ^ source2[i];
		}

		return dest;
	}

	/**
	 * 数据加密
	 * @param D 被加密数据
	 * @param K 加密密钥
	 * @return 加密结果
	 */
	public static String encryption(String D, String K)
	{
		String str = "";
		int[] temp = new int[64];
		int[] data = string2Binary(D);

		data = changeIP(data);
		int[][] left = new int[17][32];
		int[][] right = new int[17][32];

		for(int j=0; j<32; j++)
		{
			left[0][j] = data[j];
			right[0][j] = data[j+32];
		}

		setKey(K);
		for(int i=1; i<17; i++)
		{
			int[] key = subKey[i-1];
			left[i] = right[i-1];
			int[] fTemp = f(right[i-1],key);
			right[i] = diffOr(left[i-1],fTemp);
		}

		for(int i=0; i<32; i++)
		{
			temp[i] = right[16][i];
			temp[32+i] = left[16][i];
		}

		temp = changeInverseIP(temp);
		str = binary2ASC(intArr2Str(temp));

		return str;
	}


	/**
	 * 解密
	 * @param source 解密目标
	 * @param key 解密密钥
	 * @return 解密结果
	 */
	public static String discryption(String source,String key){
		String str = "";
		int[] data = string2Binary(source);
		data = changeIP(data);
		int[] left = new int[32];
		int[] right = new int[32];
		int[] tmp = new int[32];
		for(int j=0; j<32; j++)
		{
			left[j] = data[j];
			right[j] = data[j+32];
		}

		setKey(key);
		for(int i=16; i>0; i--)
		{
			int[] sKey = subKey[i-1];
			tmp = left;
			left = right;
			int[] fTemp = f(right,sKey);
			right = diffOr(tmp,fTemp);
		}

		for(int i=0; i<32; i++)
		{
			data[i] = right[i];
			data[32+i] = left[i];
		}

		data = changeInverseIP(data);
		for(int i=0; i<data.length; i++)
		{
			str += data[i];
		}

		str = binary2ASC(str);
		
		return str;
	}

	/**
	 * DES加解密 type 参数为0 表示加密，type 为1 表示解密
	 * @param source 目标数据
	 * @param key 密钥
	 * @param type 加解密类型
	 * @return 加加解密结果
	 */
	public static String DES_1(String source,String key,int type)
	{
		if(source.length() != 16 || key.length() != 16)
			return null;
		if(type==0)
		{
			return encryption(source, key);
		}
		if(type==1)
		{
			return discryption(source, key);
		}

		return null;
	}


	/**
	 * 3DES加解密
	 * type参数为0表示加密，参数为1表示解密
	 * @param source 目标数据
	 * @param key 密钥
	 * @param type 加解密类型
	 * @return 加解密结果
	 */
	public static String DES_3(String source,String key,int type)
	{
		if(key.length() != 32 || source.length() != 16)
			return null;
		String temp = null;
		String K1 = key.substring(0, key.length()/2);
		String K2 = key.substring(key.length()/2);

		if(type==0)
		{
			temp = encryption(source, K1);
			temp = discryption(temp, K2);
	
			return encryption(temp, K1);
		}

		if(type==1)
		{
			temp = discryption(source, K1);
			temp = encryption(temp, K2);
			return discryption(temp, K1);
		}

		return null;
	}

	public static int[] keyPC_1(int[] source)
	{
		int[] dest = new int[56];
		int[] temp = 
		{57, 49, 41, 33, 25, 17, 9,
		 1, 58, 50, 42, 34, 26, 18,
		 10, 2, 59, 51, 43, 35, 27,
		 19, 11, 3, 60, 52, 44, 36,
		 63, 55, 47, 39, 31, 23, 15,
		 7, 62, 54, 46, 38, 30, 22,
		 14, 6, 61, 53, 45, 37, 29,
		 21, 13, 5, 28, 20, 12, 4};

		for(int i=0; i<56; i++)
		{
			dest[i] = source[temp[i]-1];
		}
		
		return dest;
	}

	public static int[] keyLeftMove(int[] source, int i){
		int temp = 0;
		int len = source.length;
		int ls = LS[i];
		for(int k=0; k<ls; k++)
		{
			temp = source[0];
			for(int j=0; j<len-1; j++)
			{
				source[j] = source[j+1];
			}
			source[len-1] = temp;
		}
		return source;
	}

	public static int[] keyPC_2(int[] source)
	{
		int[] dest = new int[48];
		int[] temp = 
		{14, 17, 11, 24, 1, 5,
		 3, 28, 15, 6, 21, 10,
		 23, 19, 12, 4, 26, 8,
		 16, 7, 27, 20, 13, 2,
		 41, 52, 31, 37, 47, 55,
		 30, 40, 51, 45, 33, 48,
		 44, 49, 39, 56, 34, 53,
		 46, 42, 50, 36, 29, 32};

		for(int i=0; i<48; i++)
		{
			dest[i] = source[temp[i]-1];
		}

		return dest;
	}

	public static void setKey(String source)
	{
		if(subKey.length > 0)
			subKey = new int[16][48];

		int[] temp = string2Binary(source);
		int[] left = new int[28];
		int[] right = new int[28];
		int[] temp1 = new int[56];
		temp1 = keyPC_1(temp);

		for(int i=0; i<28; i++)
		{
			left[i] = temp1[i];
			right[i] = temp1[i+28];
		}

		for(int i=0; i<16; i++)
		{
			left = keyLeftMove(left, LS[i]);
			right = keyLeftMove(right, LS[i]);
	
			for(int j=0; j<28; j++)
			{
				temp1[j] = left[j];
				temp1[j+28] = right[j];
			}
			subKey[i] = keyPC_2(temp1);
		}
	}

	/**
	 * 将字符串转换为16进制表示,首先调用getBytes方法获取字符串的字节表示方法
	 * 再将字节数组转换为16进制字符串表示
	 * @param asc 转换目标
	 * @return 转换结果
	 */
	public static String ASC_2_HEX(String asc)
	{
		StringBuffer hex = new StringBuffer();
		try 
		{
			byte[] bs = asc.toUpperCase().getBytes("UTF-8");
			for(byte b : bs)
			{
				hex.append(Integer.toHexString(new Byte(b).intValue()));
			}
		} 
		catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		}
		return hex.toString();
	}

	public static String HEX_2_ASC(String hex)
	{
		String asc = null;
		int len = hex.length();
		byte[] bs = new byte[len/2];
		for(int i=0; i<len/2; i++)
		{
			bs[i] = Byte.parseByte(hex.substring(i*2, i*2+2), 16);
		}
		try 
		{
			asc = new String(bs,"UTF-8");
		} 
		catch (UnsupportedEncodingException e) 
		{
			e.printStackTrace();
		}
		
		return asc;
	}

	public static String xOr(String s1,String s2)
	{
		int[] iArr = diffOr(string2Binary(s1), string2Binary(s2));
		return binary2ASC(intArr2Str(iArr));
	}


	public static String intArr2Str(int[] arr)
	{
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<arr.length; i++)
		{
			sb.append(arr[i]);
		}

		return sb.toString();
	}

	public static String divData(String data,String key,int type){
		String left = null;
		String right = null;
		if(type == HEX)
		{
			left = key.substring(0, 16);
			right = key.substring(16,32);
		}

		if(type == ASC)
		{
			left = ASC_2_HEX(key.substring(0, 8));
			right = ASC_2_HEX(key.substring(8,16));
		}

		data = DES_1(data, left, 0);
		data = DES_1(data, right, 1);
		data = DES_1(data, left, 0);

		return data;
	}	

	public static String reverse(String source)
	{
		int[] data = string2Binary(source);
		int j = 0;
		for(int i : data)
		{
			data[j++] = 1 - i;
		}
		return binary2ASC(intArr2Str(data));
	}
	
	public static String getDPK(String issuerFlag,String appNo,String mpk)
	{
		StringBuffer issuerMPK = new StringBuffer();
		issuerMPK.append(divData(issuerFlag, mpk, 0));
		issuerMPK.append(divData(reverse(issuerFlag), mpk, 0));
		StringBuffer dpk = new StringBuffer();
		dpk.append(divData(appNo, issuerMPK.toString(), 0));
		dpk.append(divData(reverse(appNo), issuerMPK.toString(), 0));
		
		return dpk.toString();
	}
		
	public static String xOrString(String pan,String pin)
		{
			//TODO
			//System.out.println("参与异或的因子分别为：" + pan + "和" + pin);
			if(pan.length() != pin.length())
			{
				new Exception("异或因子长度不一致").printStackTrace();
				return null;
			}
			
			byte [] bytepan = EncodeUtil.bcd(pan);
			byte [] bytepin = EncodeUtil.bcd(pin);
			
			byte [] result = new byte[bytepan.length];
			
			for(int i = 0;i < result.length;i++)
			{
				result [i] = (byte)(bytepan[i] ^ bytepin[i]);
			}
			
			//TODO
			//System.out.println("异或后的结果为：" + ByteUtil.getHexStr(result));
			return EncodeUtil.hex(result);
		}
		
		public static String createPwd(String cardNo,String pwd,String key)
		{
			//TODO
			System.out.println("账号和密码分别为: " + cardNo + "和" + pwd);
			String last6 = cardNo.substring(cardNo.length() - 13,cardNo.length() - 1);
			String first2 = "0000";
			String pan = first2 + last6;
			
			String pin = "06" + pwd + "FFFFFFFF";
			
			String result = xOrString(pan,pin);
			
			String ret = DES_3(result,key,0);
			//TODO
			System.out.println("加密pin为:" + ret);
			return ret;
		}
		
		/**
		 * 生成ANSI-X9.9-MAC校验码
		 * 所有的输入输出数据必须是16进制字符串
		 * 密钥只能为16位长度的16进制字符串,否则将返回null
		 * 初始向量为16为长度的16进制字符串,如果不符合条件,自动设置为"0000000000000000"
		 * 原始数据,16进制表示的字符串
		 * @param key	密钥
		 * @param vector	初始向量
		 * @param data	加密数据
		 * @return	加密后的数据
		 */
		public static String MAC_ASC(String key,String vector,String data){
			return MAC(key,vector,EncodeUtil.hex(data.getBytes()));
		}
		
		public static String MAC(String key,String vector,String data)
		{
			if(key.length() != 16)
			{
				new Exception("key's length must be 16!").printStackTrace();
				return null;
			}
			
			if(vector == null || vector.length() != 16)
				vector = "0000000000000000";
			
			StringBuffer sb = new StringBuffer(data);
			int mod = data.length()%16;
			if(mod != 0)
			{
				for(int i = 0;i < 16 - mod;i++)
				{
					sb.append("0");
				}
			}
			String operator = sb.toString();
			//TODO
			System.out.println("补位后的操作数为：" + operator);
			int count = operator.length()/16;
			String [] blocks = new String[count];
			for(int i = 0;i < count;i++)
			{
				blocks[i] = operator.substring(i*16, i*16 + 16);
			}
			//循环进行异或,DES加密
			for(int i = 0;i < count;i++)
			{
				String xor = xOrString(vector,blocks[i]);
				vector = DES_1(xor,key,0);
			}
			return vector;
		}
		
		/**
		 * 按照ANSIX9.19算法标准，生成MAC
		 * @param key	加密密钥32位16进制字符串
		 * @param vector	初始向量16位16进制字符串
		 * @param data	生成mac的原始数据的16进制表示
		 * @return	最终生成mac的字符串
		 */
		public static String Mac_919(String key,String vector,String data)
		{
			if(key.length() != 32)
			{
				new Exception("key of ansix9.19 must be 32").printStackTrace();
				return null;
			}
			
			String left = key.substring(0, 16);
			String right = key.substring(16);
			
			String mac = MAC(left,null,data);
			String result1 = DES_1(mac,right,1);
			String result2 = DES_1(result1,left,0);
			
			String ret = result2.substring(0,8);
			return ret;
		}
		
		public static String createRandom(int length)
		{
			StringBuffer sb = new StringBuffer("");
			Random random = new Random();
			for(int i = 0;i < length;i++)
			{
				int abs = Math.abs(random.nextInt()%10);
				sb.append("" + abs);
			}
			return sb.toString();
		}
	
	public static void main(String [] args)
	{
		String mac = MAC_ASC("5883F8DA898A3149","0000000000000000",EncodeUtil.hex("409666552865948200000000000000000193322515631010000".getBytes()));
		System.out.println("生成mac为：" + mac);
		
//		System.out.println("生成的随机数为：" + createRandom(6));
	}
}
