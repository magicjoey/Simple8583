package com.simple8583.factory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.simple8583.exception.Simple8583Exception;
import com.simple8583.model.IsoContainer;
import com.simple8583.model.IsoField;
import com.simple8583.model.IsoHeaderList;
import com.simple8583.model.IsoPackage;

/**
 * <p>XmlReader 本方法的实现是通过JaxB来实现的，比较方便得将Xml文件与Object文件做相互转换.</p>
 *
 * @author Magic Joey
 * @version XmlReader.java 1.0 Created@2014-07-12 13:28 $
 */
public class XmlReader {

	// 配置的记录信息
	// mti
	private Map<String, IsoPackage> isoConfig = new ConcurrentHashMap<String, IsoPackage>();

	public Map<String, IsoPackage> getIsoConfig() {
		return isoConfig;
	}

	public XmlReader(String path) throws JAXBException, IOException {
		// 如果配置为空，则初始化，否则跳过
		if (isoConfig.size() == 0) {
			init(path);
		}
	}

	// 负责将配置文件解析缓存在
	@SuppressWarnings("unchecked")
	private void init(String path) throws IOException {
		InputStream is = null;
		try {
			// 获取
			is = ClassLoader.getSystemResourceAsStream(path);
			if (is == null) {
				throw new Simple8583Exception("配置文件路径错误:" + path);
			}
			IsoContainer container = this.readConfigFromStream(
					IsoContainer.class, is);
//			System.out.println(container.size());
			is = ClassLoader.getSystemResourceAsStream(path);
			IsoHeaderList headerList = readConfigFromStream(
					IsoHeaderList.class, is);
//			System.out.println(headerList.size());
			for (IsoPackage pack : container) {
				// 将读取到的header信息插入前面
				pack.addAll(0, (ArrayList<IsoField>) (headerList.clone()));
				this.isoConfig.put(String.valueOf(pack.getMti()), pack);
			}
		} finally {
			if (is != null) {
				is.close();
			}

		}
	}

	@SuppressWarnings("unchecked")
	private <T> T readConfigFromStream(Class<T> clazz,
			final InputStream dataStream) {
		try {
			JAXBContext jc = JAXBContext.newInstance(clazz);
			Unmarshaller u = jc.createUnmarshaller();
			return (T) u.unmarshal(dataStream);
		} catch (JAXBException e) {
			throw new Simple8583Exception(e);
		}
	}

}
