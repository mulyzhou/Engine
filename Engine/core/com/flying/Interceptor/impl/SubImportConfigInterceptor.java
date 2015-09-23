package com.flying.Interceptor.impl;

import java.io.File;

import org.dom4j.Document;
import org.dom4j.Element;

import com.flying.Interceptor.AbstractInterceptor;
import com.flying.builder.BuilderUtil;
import com.flying.service.EngineParameter;
import com.flying.util.FileUtil;
/**
 * <B>描述：</B>子模块导入配置<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 *
 */
public class SubImportConfigInterceptor extends AbstractInterceptor {

	@Override
	public void before(EngineParameter ep) throws Exception {
		String subName = ep.getParam("SUB_NAME").toString().trim();
		
		log.debug("在tablename-import.xml中注册 "+ subName +" 子系统");
		String tableNameImportXmlPath = BuilderUtil.getTableNameImportXmlPath();
		File tableNameImportXmlFile = FileUtil.createFile(tableNameImportXmlPath);
		Document tableNameImportDocument = FileUtil.readXml(tableNameImportXmlFile);
		Element tablenamesElement = (Element) tableNameImportDocument.getRootElement();
		// 构建新增节点
		Element importElement = tablenamesElement.addElement("import");
		importElement.addAttribute("resource", "config/tablename-"+subName+".xml");
		// 保存到文件
		FileUtil.writeXml(tableNameImportDocument,tableNameImportXmlFile);
		
		log.debug("在sql-map-import.xml中注册 "+ subName +" 子系统");
		String sqlmapImportPath = BuilderUtil.getSqlmapImportPath();
		File sqlmapImportFile = FileUtil.createFile(sqlmapImportPath);
		Document sqlmapImportDocument = FileUtil.readXml(sqlmapImportFile);
		Element sqlMapConfigElement = (Element) sqlmapImportDocument.getRootElement();
		// 构建新增节点
		Element sqlMapImportElement = sqlMapConfigElement.addElement("sqlMapImport");
		sqlMapImportElement.addAttribute("resource", "sql/"+ subName +"/sql-map-"+ subName +".xml");
		// 保存到文件
		FileUtil.writeXml(sqlmapImportDocument,sqlmapImportFile);
	}

	@Override
	public void after(EngineParameter ep) throws Exception {
		
	}

}
