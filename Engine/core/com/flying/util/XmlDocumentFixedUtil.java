package com.flying.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.ibatis.common.resources.Resources;
/**
 * 
 * <B>描述：</B>ibatis配合类<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 *
 */
public class XmlDocumentFixedUtil {
	private transient static Log logger = LogFactory.getLog(XmlDocumentFixedUtil.class);

	public static Document getDocumentFromFile(String resource, String url) {
		try {
			InputStream inputStream = null;
			if (resource != null) {
				inputStream = Resources.getResourceAsStream(resource);
			} else if (url != null) {
				inputStream = Resources.getUrlAsStream(url);
			} else {
				logger.error("resource & url are both null");
				throw new NullPointerException("resource & url are both null");
			}
			return new SAXReader().read(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return null;
	}

	public static Document fixedAllDocuments(List<Document> documents, Document targetDocument) {
		for (Document document : documents) {
			List<Element> list = document.selectNodes("/sqlMapImport/sqlMap");
			for (Element element : list) {
				targetDocument.getRootElement().add(element.createCopy());
			}
		}
		logger.debug(targetDocument.asXML());
		return targetDocument;
	}

	public static InputStream convert2InputStream(Document document) {
		try {
			//return new ReaderInputStream(new StringReader(document.asXML()));
			ByteArrayInputStream is = new ByteArrayInputStream(document.asXML().getBytes("UTF-8"));
			return is;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("resource & url are both null");
		} finally {

		}
		return null;
	}

}
