import com.simple8583.client.SimpleClient;
import com.simple8583.factory.XmlReader;
import com.simple8583.key.SimpleConstants;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>测试类.</p>
 *
 * @author Magic Joey
 * @version Simple8583Test.java 1.0 Created@2015-6-15 12:46 $
 */
public class Simple8583Test {

    public static void main(String[] args) throws Exception {
        Map<String,String> requestMap = new HashMap<String,String>();
        requestMap.put(SimpleConstants.MTI,"0200");
        requestMap.put("2","6228481718463331999");
        requestMap.put("","");//TODO come on...

        String ip = "";
        int port = 8080;
        int timeout = 15000;//15s超时

        String macKey = "helloSimple8583";
        SimpleClient simpleClient = new SimpleClient(ip,port,timeout);
        simpleClient.setMacKey(macKey);
        XmlReader xmlReader = new XmlReader("simple8583.xml");
        Map<String,String> resultMap = simpleClient.sendToBank(requestMap,xmlReader);
        System.out.println(resultMap);
    }


}
