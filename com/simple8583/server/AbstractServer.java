package com.simple8583.server;

import com.simple8583.exception.Simple8583Exception;
import com.simple8583.factory.IsoMsgFactory;
import com.simple8583.factory.XmlReader;
import com.simple8583.key.SimpleConstants;
import com.simple8583.model.IsoPackage;
import com.simple8583.util.EncodeUtil;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

/**
 * <p>Server端抽象类.</p>
 *
 * @author Magic Joey
 * @version AbstractServer.java 1.0 Created@2015-06-16 13:52 $
 */
public abstract class AbstractServer {

    private int port = 8999;

    public void init(final XmlReader xmlReader) throws Exception {
        ServerSocket serverSocket = null;
        Socket socket = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        serverSocket = new ServerSocket(port);
        System.out.println("服务器已启动,监听端口:" + port);

        IsoMsgFactory factory = IsoMsgFactory.getInstance();
        while (true) {
            try {
                socket = serverSocket.accept();
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();

                //存储长度字节
                byte[] lengthBytes = new byte[2];
                //读取输入
                if(inputStream.read(lengthBytes)==2){
                    int length = msgLength(lengthBytes);
                    byte[] byteBody = new byte[length];
                    inputStream.read(byteBody);
                    IsoPackage isoPackage = xmlReader.getIsoConfig().get(getMti(byteBody));
                    Map<String,String> receiveMap = factory.unpack(byteBody,isoPackage);
                    Map<String,String> responseMap = response(receiveMap);
                    String responseMti = responseMap.get(SimpleConstants.MTI);
                    IsoPackage responsePackage = xmlReader.getIsoConfig().get(responseMti);
                    if(responsePackage==null){
                        throw new Simple8583Exception("获取响应报文格式失败:"+responseMti+",报文内容:"+ EncodeUtil.hex(byteBody));
                    }
                    byte[] responseBytes = factory.pack(responseMap,responsePackage);
                    System.out.println("响应报文:"+EncodeUtil.hex(responseBytes));
                    //返回信息
                    outputStream.write(responseBytes);
                }else{
                    System.out.println("读取字节长度失败");
                }
            }finally {
                if(socket!=null){
                    socket.close();
                }
                if(inputStream!=null){
                    inputStream.close();
                }
                if(outputStream!=null){
                    outputStream.close();
                }
            }

        }
    }

    //响应用户请求
    protected abstract Map<String,String> response(Map<String, String> receiveMap);

    private String getMti(byte[] byteBody){
        return "0200";
    }

    protected int msgLength(byte[] lenBts){
        return  (lenBts[0] & 0xff) * 256
                + (lenBts[1] & 0xff);
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
