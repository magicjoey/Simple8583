package com.simple8583.client;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Map;

import com.simple8583.exception.Simple8583Exception;
import com.simple8583.factory.IsoMsgFactory;
import com.simple8583.factory.XmlReader;
import com.simple8583.model.IsoPackage;

/**
 * <p>发送客户端抽象类.</p>
 *
 * @author Magic Joey
 * @version AbstractClient.java 1.0 Created@2014-07-10 10:43 $
 */
public abstract class AbstractClient {
    //默认18s超时
	protected int timeout = 18000;
    //Ip地址
	protected String ip;
    //端口号
	protected int port;
    //加密串，用于MD5或者DES加密
	protected String macKey;

	public AbstractClient(String ip, int port){
		this.ip = ip;
		this.port = port;
	}

	//设置访问的Ip地址，端口号和超时时间
	public AbstractClient(String ip, int port, int timeout) {
		this(ip, port);
		this.timeout = timeout;
	}

	//发送接受报文的方法
	public Map<String, String> sendToBank(Map<String, String> dataMap,
			XmlReader xmlReader) throws Exception {
		//单例
		IsoMsgFactory factory = IsoMsgFactory.getInstance();
		factory.setMacKey(macKey);
		String mti = dataMap.get("mti");
		IsoPackage pack = xmlReader.getIsoConfig().get(mti);
		byte[] buf = null;
		try {
			byte[] sendData = factory.pack(dataMap, pack);
			Socket socket = new Socket();
			InetAddress inetAddress = InetAddress.getByName(this.ip);
			InetSocketAddress address = new InetSocketAddress(inetAddress, port);
			try {
				// 发送的报文
//				System.out.println("发送报文：" + EncodeUtil.hex(sendData));
				socket.setReuseAddress(true);
				socket.connect(address);
				socket.setSoTimeout(this.timeout);
				//发送数据
				socket.getOutputStream().write(sendData);
				socket.getOutputStream().flush();
				//两字节长度
				byte[] lenbuf = new byte[2];
				while (socket != null && socket.isConnected()) {
					if (socket.getInputStream().read(lenbuf) == 2) {
						//计算前两位报文所表示的报文长度
						int size = computeLength(lenbuf);
						//新建对应长度字节数组
						buf = new byte[size];
						//读取数据
						socket.getInputStream().read(buf);
						break;
					}
				}
				
//				System.out.println(EncodeUtil.hex(buf));
			} finally {
				if (socket != null) {
					try {
						socket.close();
					} catch (Exception e) {
						
					}
				}
			}
		} catch (Exception e) {
//			e.printStackTrace();
			//抛出可能的异常，比如连接超时等
			throw new Simple8583Exception("网络通讯异常",e);
		}
		//将前面的MsgLength域剔除
		pack.remove(0);
		//根据IsoPackage的结构解析接受到的报文
		return factory.unpack(buf, pack);
	}
	
	protected abstract int computeLength(byte[] lenBts);


    public void setMacKey(String macKey) {
        this.macKey = macKey;
    }

}
