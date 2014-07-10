package com.simple8583.util;

import java.io.UnsupportedEncodingException;

import com.simple8583.util.encrypt.MacUtil;

public class TripleDES extends DES {

	// 双倍长计算密钥长度
	public static String DESDouble(String source, String key, int mode) {
		String first = DES_2(source.substring(0, 16), key, mode);
		String second = DES_2(source.substring(16), key, mode);
		return first + second;
	}

	public static String getMacValue(String macKey) {
		String data = DESDouble("00000000000000000000000000000000", macKey, 0);
		return data.substring(0, 6);
	}

	public static String getMac(String macKey, String data) {
		String lk = macKey.substring(0, 16);
		String rk = macKey.substring(16);
		String second_result = MacUtil.MAC_ASC(lk, "0000000000000000", data);// ANSI-X9.9-MAC计算mac
		System.out.println("第一步结果:" + second_result);
		String triple_result = DES_1(second_result, rk, 1);// 1为解密，第三部结果
		System.out.println("第二步结果:" + triple_result);
		String fourth_result = DES_1(triple_result, lk, 0);// 0为加密，使用左16位密钥
		System.out.println("第三步结果:" + fourth_result);
		return fourth_result;
	}

//	private static String getMac(String bocMac, String zmk_lmk, String data) {
//		String key = DESDouble(bocMac, zmk_lmk, 1);// 1解密
//		System.out.println("第一步结果:" + key);
//		String lk = key.substring(0, 16);
//		String rk = key.substring(16);
//		String second_result = MacUtil.MAC(lk, "0000000000000000", data);// ANSI-X9.9-MAC计算mac
//		System.out.println("第二部结果:" + second_result);
//		String triple_result = DES_1(second_result, rk, 1);// 1为解密，第三部结果
//		System.out.println("第三步结果:" + triple_result);
//		String fourth_result = DES_1(triple_result, lk, 0);// 0为加密，使用左16位密钥
//		System.out.println("第四部结果:" + fourth_result);
//		return fourth_result;
//	}
//
//	private static String getData(String data, String key) {
//		String lk = key.substring(0, 16);
//		String rk = key.substring(16);
//		// 第一步，左密钥解密
//		String first_result = DES_1(data, lk, 1);
//		// 第二步，右边密钥加密
//		String second_result = DES_1(data, rk, 0);
//		// 第三步，使用做密钥解MAC
//
//		return null;
//	}

	public static void main(String[] args) throws UnsupportedEncodingException {

		// System.out.println(DESDouble("D1A1DA31D3221AD2345DA4A78DADA123",
		// "15656156165165165165156165102044", 1));
		// String key = "D59B49C2A78FF4E6";
		// String vector = "0000000000000000";
		// String value =
		// "343039363636353532383635393438323130303030303030303030313331303130303030";
		// int type=1;
		// String re = MAC(key,vector,value,type);
		// System.out.println(re);
		// String bocMac = "ACFB515645FAB4544FA454AB12649528";
		// String zmk_lmk = "123525964972ADB9913578025046931F";
		// String data =
		// "564561487446515618949848123615645645646515615615616516489498";
		//
		// String dd = getMac(bocMac,zmk_lmk,data);
		// System.out.println(dd);

		//
		// String lmk = "CF8C6A404793D373CF8C6A404793D373";
		// String bocMac = "9E3C3C9F5127E4A285496DF46261346B";
		// String data =
		// "4096665528659482000000000000000001750206113521201407040156E331010000";
		// //
		// String result = getMac(lmk, bocMac, data);
		// System.out.println(result);

		// // /8EF9089200D9720B
		// System.out.println("预期结果："+EncodeUtil.hex(EncodeUtil.binary("1000111011111001000010001001001000000000110110010111001000001011")));
		// System.out.println(new
		// String(EncodeUtil.bcd("4096665528659482"),"GBK"));

		// String source = "7BC1B6CB52487E706FC36FD95B2DF040";
		// String key = "33333333333333333333333333333333";
		// int mode = 1;
		// String mac_key = DESDouble(source, key, mode);
		// String data =
		// DESDouble("00000000000000000000000000000000",mac_key,0);
//		String data = "409666552865948200000000000000000193322515631010000";
		String macKey = "5883F8DA898A31495DF792A8DA15E013";
		String data = "409666552865948200000000000000000193322515631010000";//.getBytes());
		// System.out.println(data.substring(0,6));
		System.out.println(getMac(macKey, data));
		System.out.println("预期值：" + "AFC74C7A");
		// String encrypt = new
		// String(EncodeUtil.bcd("41E2E66D00D9ABF6"),"GBK");
		// System.out.println(encrypt);
	}

}
