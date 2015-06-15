package com.simple8583.model;

import java.lang.IllegalArgumentException;
import java.util.List;

import com.simple8583.util.EncodeUtil;

/**
 * <p>位图类.</p>
 *
 * @author Magic Joey
 * @version BitMap.java 1.0 Created@2014-07-12 13:28 $
 */
public class BitMap {

	private int[] bits = null;
	private int length;
	private final static int[] bitValue = { 0x80000000, 0x40000000, 0x20000000,
			0x10000000, 0x08000000, 0x04000000, 0x02000000, 0x01000000,
			0x00800000, 0x00400000, 0x00200000, 0x00100000, 0x00080000,
			0x00040000, 0x00020000, 0x00010000, 0x00008000, 0x00004000,
			0x00002000, 0x00001000, 0x00000800, 0x00000400, 0x00000200,
			0x00000100, 0x00000080, 0x00000040, 0x00000020, 0x00000010,
			0x00000008, 0x00000004, 0x00000002, 0x00000001 };

	public BitMap(int length) {
		if (length < 0) {
			throw new IllegalArgumentException("长度必须大于0!");
		}
		bits = new int[length / 32 + ((length % 32) > 0 ? 1 : 0)];
		this.length = length;
	}

	public int getBit(int index) {
		if (index < 0 || index > length) {
			throw new IllegalArgumentException("length value illegal!");
		}
		int intData = bits[index / 32];
		return ((intData & bitValue[index % 32]) >>> (32 - index % 32 - 1));
	}
	
	public byte[] addBits(List<Integer> list){
		for(int val:list){
			//本处-1的原因是，BitMap在1~64之间，而实际存储为0~63
			addBit(val-1);
		}
		return getBitMap();
	}
	
	public byte[] addBits(int[] bitArray){
		for(int i=0;i<bitArray.length;i++){
			if(bitArray[i]==1){
				addBit(i);
			}
		}
		return getBitMap();
	}
	
	public byte[] addBits(String regix){
		String[] vals = regix.split(",");
		for(String val:vals){
			//本处-1的原因是，BitMap在1~64之间，而实际存储为0~63
			addBit(Integer.valueOf(val)-1);
		}
		return getBitMap();
	}
	
	//根据返回的bit值
	public static BitMap addBits(byte[] bits){
		BitMap bitMap = new BitMap(bits.length*8);
		int pointer=0;
		for(byte bt:bits){
			for(int pos=7;pos>=0;pos--){
				//判断该位是否为0
				if((int)(bt>>>pos&0x01)==1){
					bitMap.addBit(pointer);
				}
				pointer++;
			}
		}
		return bitMap;
	}

	public void addBit(int index) {
		if (index < 0 || index > length) {
			throw new IllegalArgumentException("长度有误!");
		}
		int intData = bits[index / 32];
		bits[index / 32] = intData | bitValue[index % 32];
//		if (value == 1) {
//		}/* else {
//			bits[index / 32] = intData & ~bitValue[index % 32];
//		}*/
	}
	
	
	public byte[] getBitMap(){
		StringBuffer accum = new StringBuffer();
		for(int index=1;index<=length;index++){
//			System.out.println(index);
			accum.append(this.getBit(index-1));
		}
		return EncodeUtil.binary(accum.toString());
	}

	public int getLength() {
		return length;
	}
	
	@Override
	public String toString(){
		StringBuffer accum = new StringBuffer();
		accum.append("length:"+this.length+",选中的域:");
		for(int i=0;i<length;i++){
			if(this.getBit(i)==1){
				accum.append(i+1+",");
			}
		}
		return accum.toString();
	}

	public static void main(String[] args) {
//		BitMap bitArray = new BitMap(64);
//		bitArray.addBits("1,2,15,");
//		System.out.println(EncodeUtil.hex(bitArray.getBitMap()));
//		byte[] i=new byte[8];
//		for(int pos = 0;pos<8;pos++){
//			i[pos]=(byte)10;
//		}
//		addBits(i);
		byte[] bts = EncodeUtil.bcd("7124058104F18005");
		int[] intArr = new int[bts.length*8];
		int curse = 0;
		StringBuilder accum = new StringBuilder();
		for(int i=0;i<8;i++){
			for(int j=7;j>=0;j--){
				intArr[curse++] = (bts[i]>>>j)&0x01;
			}
		}
		int c=0;
		
		for(int i:intArr){
			if(i==1){
				accum.append(c+1+",");
				System.out.println(c+1);
			}
			c++;
		}
		System.out.println(accum.toString());
	}

}
