package com.flying.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.Resource;
import org.springframework.orm.ibatis.SqlMapClientFactoryBean;
import org.springframework.util.ClassUtils;
import org.xml.sax.SAXException;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.client.SqlMapClientBuilder;
/**
 * 
 * <B>描述：</B>ibatis辅助类<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 *
 */
public class CustomSqlMapClientFactoryBean extends SqlMapClientFactoryBean {
	// Determine whether the SqlMapClientBuilder.buildSqlMapClient(InputStream)
	// method is available, for use in the "buildSqlMapClient" template method.
	private final static boolean buildSqlMapClientWithInputStreamMethodAvailable = ClassUtils.hasMethod(SqlMapClientBuilder.class, "buildSqlMapClient",
			new Class[] { InputStream.class });

	// Determine whether the SqlMapClientBuilder.buildSqlMapClient(InputStream, Properties)
	// method is available, for use in the "buildSqlMapClient" template method.
	private final static boolean buildSqlMapClientWithInputStreamAndPropertiesMethodAvailable = ClassUtils.hasMethod(SqlMapClientBuilder.class,
			"buildSqlMapClient", new Class[] { InputStream.class, Properties.class });

	private Resource extendConfigLocation;

	protected SqlMapClient buildSqlMapClient(Resource configLocation, Properties properties) throws IOException {
		if (extendConfigLocation == null) {
			return super.buildSqlMapClient(configLocation, properties);
		}
		InputStream is = configLocation.getInputStream();
		Document document = null;
		try {
			//
			SAXReader reader = new SAXReader(false);   
			try {
				reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
			} catch (SAXException e) {
				e.printStackTrace();
			}  
			document = reader.read(is);
			//document = new SAXReader().read(is);
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}

		// -- Custom start
		InputStream extendIs = extendConfigLocation.getInputStream();

		Document extendDocument = null;
		try {
			extendDocument = new SAXReader().read(extendIs);
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}
		List<Element> nodes = extendDocument.selectNodes("/sqlMapConfig/sqlMapImport");

		if (nodes != null && nodes.size() > 0) {
			List<Document> documents = new ArrayList<Document>();
			for (Element node : nodes) {
				String resource = node.attributeValue("resource");
				String url = node.attributeValue("url");

				documents.add(XmlDocumentFixedUtil.getDocumentFromFile(resource, url));
			}

			Document fixedDocument = XmlDocumentFixedUtil.fixedAllDocuments(documents, (Document) document.clone());
			
			//System.out.println(fixedDocument.asXML());

			is = XmlDocumentFixedUtil.convert2InputStream(fixedDocument);
		}
		// -- Custom end

		if (properties != null) {
			if (buildSqlMapClientWithInputStreamAndPropertiesMethodAvailable) {
				return SqlMapClientBuilder.buildSqlMapClient(is, properties);
			} else {
				return SqlMapClientBuilder.buildSqlMapClient(new InputStreamReader(is), properties);
			}
		} else {
			if (buildSqlMapClientWithInputStreamMethodAvailable) {
				return SqlMapClientBuilder.buildSqlMapClient(is);
			} else {
				return SqlMapClientBuilder.buildSqlMapClient(new InputStreamReader(is));
			}
		}
	}

	public void setExtendConfigLocation(Resource extendConfigLocation) {
		this.extendConfigLocation = extendConfigLocation;
	}

}
