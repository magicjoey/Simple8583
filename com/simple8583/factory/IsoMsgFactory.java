package com.simple8583.factory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.simple8583.key.SimpleConstants;
import com.simple8583.model.BitMap;
import com.simple8583.model.IsoField;
import com.simple8583.model.IsoPackage;
import com.simple8583.util.EncodeUtil;
import com.simple8583.util.SimpleUtil;
import com.simple8583.util.TripleDES;

/**
 * Description: XmlFactory Author: joey Update: joey(2014-06-09 17:41)
 */
public class IsoMsgFactory {

	private String bocMac;

	public void setBocMac(String bocMac) {
		this.bocMac = bocMac;
	}
	
	private  static IsoMsgFactory isoMsgFactory=new IsoMsgFactory();

	private IsoMsgFactory() {

	}
	
	public static IsoMsgFactory getInstance(){
		return isoMsgFactory;
	}

	// 入口和出口
	public byte[] pack(Map<String, String> dataMap, IsoPackage pack)
			throws IOException {

		IsoPackage packClone = (IsoPackage) pack.clone();
		StringBuffer bitMapAccum = new StringBuffer();
		for (String key : dataMap.keySet()) {
			IsoField field = packClone.getIsoField(key);
			if (field == null) {
				continue;
			}
			field.setValue(dataMap.get(key));
			//数据域
			if (SimpleUtil.isNumeric(key)) {
				bitMapAccum.append(key + ",");
			}
		}
		// 生成BitMap
		BitMap bitMap = new BitMap(64);
		byte[] bitMapByte = bitMap.addBits(bitMapAccum.toString());
		// System.out.println(EncodeUtil.hex(bitMapByte));
		packClone.getIsoField(SimpleConstants.BIT_MAP).setByteValue(bitMapByte);
		return merge(packClone);
	}

	public Map<String, String> unpack(byte[] bts, IsoPackage pack)
			throws InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, NoSuchAlgorithmException,
			NoSuchProviderException, NoSuchPaddingException, IOException {

		if (pack == null || pack.size() == 0) {
			throw new IllegalArgumentException("配置为空，请检查IsoPackage是否为空");
		}
		Map<String, String> returnMap = new HashMap<String, String>();
		// 起判断的作用
		int offset = 0;
		IsoPackage target = (IsoPackage) pack.clone();
		// 获取到bitMap
		boolean hasBitMap = false;
		BitMap bitMap = null;
		for (IsoField field : target) {

			if (field.isAppData()) {
				if (hasBitMap) {
					int index = Integer.valueOf(field.getId());
					if (bitMap.getBit(index - 1) == 1) {
						offset += subByte(bts, offset, field);
						returnMap.put(field.getId(), field.getValue());
						if (index == 64) {
							System.out.println("密钥验证");
							byte[] returnByte = mac(target);
							System.out.println("加密密钥"
									+ EncodeUtil.hex(returnByte));
							System.out.println("返回密钥"
									+ EncodeUtil.hex(field.getByteValue()));
						}
					}
				}
			} else {
				offset += subByte(bts, offset, field);
				returnMap.put(field.getId(), field.getValue());
				if (field.getId().equalsIgnoreCase(SimpleConstants.BIT_MAP)) {
					hasBitMap = true;
					bitMap = BitMap.addBits(field.getByteValue());
				}
			}
		}
		return returnMap;
	}

	private int subByte(byte[] bts, int offset, IsoField field)
			throws UnsupportedEncodingException {
		byte[] val = null;
		int length = field.getLength();
		switch (field.getIsoType()) {
		case NUMERIC:
		case CHAR:
		case BINARY:
			val = new byte[field.getLength()];
			System.out.println(field.getId());
			System.arraycopy(bts, offset, val, 0, length);
			break;
		case LLVAR_NUMERIC:
			byte[] llvarNumLen = new byte[1];
			llvarNumLen[0] = bts[offset];
			// 除以2的原因是LLVAR_NUMERIC前面的报文域长度是数字长度而非字节长度
			int firstNumLen = Integer.valueOf(EncodeUtil.hex(llvarNumLen)) / 2;
			val = new byte[firstNumLen];
			System.arraycopy(bts, offset + 1, val, 0, firstNumLen);
			length = 1 + firstNumLen;
			break;
		case LLVAR:
			byte[] llvarLen = new byte[1];
			llvarLen[0] = bts[offset];
			int firstLen = Integer.valueOf(EncodeUtil.hex(llvarLen));
			val = new byte[firstLen];
			System.arraycopy(bts, offset + 1, val, 0, firstLen);
			length = 1 + firstLen;
			break;
		case LLLVAR:
			byte[] lllvarLen = new byte[2];
			lllvarLen[0] = bts[offset];
			lllvarLen[1] = bts[offset + 1];
			int first2Len = Integer.valueOf(EncodeUtil.hex(lllvarLen));
			val = new byte[first2Len];
			System.arraycopy(bts, offset + 2, val, 0, first2Len);
			length = 2 + first2Len;
			break;
		default:
			break;
		}
		field.setByteValue(val);
		return length;
	}

	// Byte数组的合并，不同byte数组域将被整合为一个大的byte数组
	private byte[] merge(IsoPackage isoPackage) throws IOException {
		ByteArrayOutputStream byteOutPut = new ByteArrayOutputStream(100);
		for (IsoField field : isoPackage) {
			if (field.isChecked()) {
				if (field.getId().equals("64")) {
					try {
						byteOutPut.write(mac(isoPackage));
					} catch (Exception e) {
						e.printStackTrace();
					}
					continue;
				}
				switch (field.getIsoType()) {
				case LLVAR_NUMERIC:
					byte[] lengthByte0 = new byte[1];
					lengthByte0 = EncodeUtil.bcd(field.getValue().length(), 1);
					byteOutPut.write(lengthByte0);
					break;
				case LLVAR:
					byte[] lengthByte = new byte[1];
					lengthByte = EncodeUtil.bcd(field.getByteValue().length, 1);
					byteOutPut.write(lengthByte);
					break;
				case LLLVAR:
					byte[] lengthByte2 = new byte[2];
					lengthByte2 = EncodeUtil
							.bcd(field.getByteValue().length, 2);
					byteOutPut.write(lengthByte2);
					break;
				default:
					break;
				}
				System.out.println(field.getId() + ":"
						+ EncodeUtil.hex(field.getByteValue()));

				byteOutPut.write(field.getByteValue());
			}
		}
		byte[] beforeSend = byteOutPut.toByteArray();
		byte[] bts = new byte[beforeSend.length + 2];
		byte[] lenArr = SimpleUtil.msgLength(beforeSend.length);
		System.arraycopy(lenArr, 0, bts, 0, 2);
		System.arraycopy(beforeSend, 0, bts, 2, beforeSend.length);
		return bts;
	}

	// 编码规则
	// 2 Primary Account Number
	// 3 Processing Code
	// 4 Amount, Transaction
	// 11 System Trace Audit Number
	// 12 Time, Local Transaction
	// 13 Date, Local Transaction
	// 49 Currency Code, Transaction
	// 38 Authorization Identification Response
	// 39 Response Code
	// 41 Card Acceptor Terminal Identification
	private byte[] mac(IsoPackage isoPackage) throws IOException,
			InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, NoSuchAlgorithmException,
			NoSuchProviderException, NoSuchPaddingException {
		String[] md5Array = { "2", "3", "4", "11", "12", "13", "49", "38",
				"39", "41" };
		StringBuffer accum = new StringBuffer();
		for (String key : md5Array) {
			IsoField field = isoPackage.getIsoField(key);
			if (field == null) {
				continue;
			}
			if (field.getValue() == null) {
				continue;
			}
			if (field.getId().equals("49")) {
				accum.append(field.getValue().substring(1));
			} else {
				accum.append(field.getValue());
			}
		}
		String original = accum.toString();
		String val = TripleDES.getMac(bocMac, original);
		return EncodeUtil.bcd(val);
	}
}
