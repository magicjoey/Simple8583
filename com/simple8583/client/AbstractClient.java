package com.simple8583.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.bind.JAXBException;

import com.simple8583.factory.IsoMsgFactory;
import com.simple8583.factory.XmlReader;
import com.simple8583.model.IsoPackage;
import com.simple8583.util.EncodeUtil;

public abstract class AbstractClient {
	protected int timeout = 8000;
	protected String ip;
	protected int port;

	protected String bocMac;

	public void setBocMac(String bocMac) {
		this.bocMac = bocMac;
	}

	public AbstractClient(String ip, int port){
		this.ip = ip;
		this.port = port;
	}

	public AbstractClient(String ip, int port, int timeout) {
		this(ip, port);
		this.timeout = timeout;
	}

	//
	public Map<String, String> client(Map<String, String> dataMap,
			XmlReader xmlReader) throws JAXBException, IOException,
			InvalidKeyException, IllegalBlockSizeException,
			BadPaddingException, NoSuchAlgorithmException,
			NoSuchProviderException, NoSuchPaddingException {
		//单例
		IsoMsgFactory factory = IsoMsgFactory.getInstance();
		factory.setBocMac(bocMac);
		String mti = dataMap.get("mti");
		IsoPackage pack = xmlReader.getIsoConfig().get(mti);
		byte[] buf = null;
		try {
			byte[] sendData = factory.pack(dataMap, pack);
			Socket socket = new Socket();
			InetAddress inetAddress = InetAddress.getByName(this.ip);
			InetSocketAddress address = new InetSocketAddress(inetAddress, port);
			try {
				// 发送
				System.out.println("发送报文：" + EncodeUtil.hex(sendData));
				socket.setReuseAddress(true);
				socket.connect(address);
				socket.setSoTimeout(this.timeout);
				socket.getOutputStream().write(sendData);
				socket.getOutputStream().flush();
				byte[] lenbuf = new byte[2];

				while (socket != null && socket.isConnected()) {

					if (socket.getInputStream().read(lenbuf) == 2) {
						
						int size = computeLength(lenbuf);
						buf = new byte[size];
						socket.getInputStream().read(buf);
						break;
					}
				}
				System.out.println(EncodeUtil.hex(buf));
			} finally {
				if (socket != null) {
					try {
						socket.close();
					} catch (Exception e) {

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		pack.remove(0);
		return factory.unpack(buf, pack);
	}
	
	protected abstract int computeLength(byte[] lenBts);

}
