package com.simple8583.client;


/**
 * <p>发送客户端.</p>
 *
 * @author Magic Joey
 * @version AbstractClient.java 1.0 Created@2014-07-10 10:43 $
 */
public class SimpleClient extends AbstractClient{
	
	//构造方法
	public SimpleClient(String ip, int port){
		super(ip, port);
	}
	
	public SimpleClient(String ip,int port,int timeout) {
		super(ip, port, timeout);
	}

	
	// 接口长度的定义方式，可根据需求更改
	// 第一字节值=len/256;
	// 第二字节值=len%256;
	@Override
	protected int computeLength(byte[] lenBts){
		if(lenBts.length!=2){
			throw new IllegalArgumentException("字节长度不正确，预期值为2，实际值为："+lenBts.length);
		}
		// int size = ((lenbuf[0] & 0xff) << 8) | (lenbuf[1] & 0xff);//普通的长度编码
		return (lenBts[0] & 0xff) * 256
				+ (lenBts[1] & 0xff);
	}

}
