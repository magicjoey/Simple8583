import com.simple8583.factory.XmlReader;
import com.simple8583.server.DefaultServer;


/**
 * <p>服务器端测试类.</p>
 *
 * @author Magic Joey
 * @version Server8583Test.java 1.0 Created@2015-06-16 13:19 $
 */
public class ServerTest {


    public static void main(String[] args) throws  Exception {
        XmlReader xmlReader  = new XmlReader("simple8583.xml");
        new DefaultServer().init(xmlReader);
    }


}
