package com.simple8583.factory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.simple8583.key.SimpleConstants;
import com.simple8583.model.BitMap;
import com.simple8583.model.IsoField;
import com.simple8583.model.IsoPackage;
import com.simple8583.util.EncodeUtil;
import com.simple8583.util.SimpleUtil;


/**
 * <p>报文组装抽象类.</p>
 *
 * @author Magic Joey
 * @version AbstractIsoMsgFactory.java 1.0 Created@2014-07-10 10:43 $
 */
public abstract class AbstractIsoMsgFactory {

	protected String macKey;

	protected AbstractIsoMsgFactory() {

	}
	
	// 入口和出口
	public byte[] pack(Map<String, String> dataMap,final IsoPackage pack)
            throws IOException, ClassNotFoundException {
        //深度拷贝，对拷贝后的对象进行操作，
		IsoPackage packClone = pack.deepClone();

		List<Integer> dataFieldList = new ArrayList<Integer>(dataMap.size());
		for (String key : dataMap.keySet()) {
			IsoField field = packClone.getIsoField(key);
			if (field == null) {
				continue;
			}
			field.setValue(dataMap.get(key));
			//数据域
			if (SimpleUtil.isNumeric(key)) {
				int val = Integer.valueOf(key);
				if(packClone.isBit64()&&val>64){
                    //设置位非64位图模式，即128模式
                    packClone.setBit64(false);
                    //将bitMap第一位置为1，表示这个数据域为128位长
					dataFieldList.add(1);
				}
				dataFieldList.add(val);
			}
		}
		// 生成BitMap
		BitMap bitMap = null;
		if(packClone.isBit64()){
            bitMap = new BitMap(64);
		}else{
            bitMap = new BitMap(128);
		}
		byte[] bitMapByte = bitMap.addBits(dataFieldList);
		//设置BitMap的值
		packClone.getIsoField(SimpleConstants.BIT_MAP).setByteValue(bitMapByte);
		//将数组合并
		return merge(packClone);
	}

    /**
     * 将返回信息拆成Map返回
     * @param bts
     * @param pack
     * @return
     * @throws Exception
     */
	public Map<String, String> unpack(byte[] bts,final IsoPackage pack)
			throws Exception {
		if (pack == null || pack.size() == 0) {
			throw new IllegalArgumentException("配置为空，请检查IsoPackage是否为空");
		}
		Map<String, String> returnMap = new HashMap<String, String>();
		// 起判断的作用
		int offset = 0;
        //深度拷贝
		IsoPackage target =  pack.deepClone();
		// 获取到bitMap
		boolean hasBitMap = false;
		BitMap bitMap = null;
		for (IsoField field : target) {
				
			if (field.isAppData()) {
				if (hasBitMap) {
					int index = Integer.valueOf(field.getId());
					if(index==1){
						continue;//第一位不处理，只是标志位
					}
					if (bitMap.getBit(index - 1) == 1) {
						offset += subByte(bts, offset, field);
						returnMap.put(field.getId(), field.getValue());
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
		//MAC校验
		macValidate(pack,returnMap);
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
                //Mac
				if (isoPackage.isMacPos(field.getId())) {
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
		byte[] lenArr = msgLength(beforeSend.length);
		//
		System.arraycopy(lenArr, 0, bts, 0, 2);
		System.arraycopy(beforeSend, 0, bts, 2, beforeSend.length);
		return bts;
	}
	
	//生成前两个字节的长度位
	//根据约定不同需要对此方法进行重写
	protected abstract byte[] msgLength(int length);
	
	//生成最后一位的MAC加密
	protected abstract byte[] mac(IsoPackage isoPackage) throws Exception;
	
	//对返回的数据进行MAC校验
	protected abstract void macValidate(IsoPackage isoPackage,Map<String,String> map);

    public void setMacKey(String macKey) {
        this.macKey = macKey;
    }
}
