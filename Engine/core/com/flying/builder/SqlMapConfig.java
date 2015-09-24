package com.flying.builder;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.flying.exception.FlyingException;
import com.flying.init.Item;
import com.flying.init.StaticVariable;
import com.flying.logging.Log;
import com.flying.logging.LogFactory;
import com.flying.service.Engine;
import com.flying.service.EngineParameter;
import com.flying.util.FileUtil;
/**
 * 
 * <B>描述：</B>sqlmap配置工具类<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 *
 */
public class SqlMapConfig {
	private static Log log = LogFactory.getLog(SqlMapConfig.class);//日志
	/**
	 * 向sqlmap配置文件中，添加一条记录
	 * @param item
	 * @throws FlyingException
	 */
	public static void insert(Item item) throws FlyingException{
		log.debug("向sql-map-"+StaticVariable.MODULE+".xml注册文件开始");
		
		String tableName = item.getName();// 表名
		
		String chineseName = item.getAlias();// 表中文名
		
		String sqlMapConfigPath = BuilderUtil.getSqlmapConfigPath();
		File sqlMapConfigFile = FileUtil.createFile(sqlMapConfigPath);
		// 构建Dom树
		Document document = FileUtil.readXml(sqlMapConfigFile);
		/**
		 * 判断sql-map-config.xml底下是否有此表的ibatis配置文件
		 * 如果有，则不注册
		 * 如果无，则注册
		 * 
		 */
		String resource = "sql/"+StaticVariable.MODULE + "/" + StaticVariable.DB + "/" + tableName + ".xml";
		// 标记true，则表示没有，false，则表示有
		boolean mark = true;
		/**
		 * 判断sql-map-config-core.xml底下是否已经有属性是resource的节点 如果有,不添加节点 如果无，添加节点
		 */
		//获取sqlMapConfig标签底下的sqlMap标签的属性resourse
		List sqlMapList = document
				.selectNodes("/sqlMapImport/sqlMap/@resource");
		Iterator sqlMapIter = sqlMapList.iterator();
		while (sqlMapIter.hasNext()) {
			Attribute attribute = (Attribute) sqlMapIter.next();
			if (resource.equals(attribute.getValue())) {
				mark = false;
				break;
			}
		}
		// 如果没有相同的resource，则新加入节点
		if (mark) {
			// 根元素sqlMapConfig
			Element sqlMapConfig = (Element) document.getRootElement();
			// 构建新增节点
			Element sqlMap = sqlMapConfig.addElement("sqlMap");
			sqlMap.addAttribute("resource", resource);
		}

		// 保持到文件
		FileUtil.writeXml(document,sqlMapConfigFile);
		
		log.debug("向sql-map-"+StaticVariable.MODULE+".xml注册文件结束");
	}
	
	/**
	 * 删除ibatisxml文件
	 * 
	 * @param bmc
	 * @throws FlyingException 
	 */
	public static void delete(String bmc) throws FlyingException{		
		String sqlMapConfigPath = BuilderUtil.getSqlmapConfigPath();
		File sqlMapConfigFile = FileUtil.createFile(sqlMapConfigPath);
		// 构建Dom树
		Document document = FileUtil.readXml(sqlMapConfigFile);

		String resource = "sql/"+StaticVariable.MODULE + "/" + StaticVariable.DB + "/" + bmc + ".xml";
		//获取sqlMapConfig标签底下的sqlMap标签的属性resourse
		List sqlMapList = document
				.selectNodes("/sqlMapImport/sqlMap");
		Iterator sqlMapIter = sqlMapList.iterator();
		while (sqlMapIter.hasNext()) {
			Element itemElement = (Element) sqlMapIter.next();//元素
			Attribute attribute = itemElement.attribute("resource");
			if (resource.equals(attribute.getValue())) {
				itemElement.getParent().remove(itemElement);//删除元素
			}
		}
		
		FileUtil.writeXml(document,sqlMapConfigFile);//写回操作
	
		log.debug("删除sql-map-"+StaticVariable.MODULE+".xml中注册名："+resource+"的文件");
	}
	
	/**
	 * 检查sql-map-module.xml配置文件是否存在；如果没有，就创建；同时在数据库中创建一个模块根节点
	 * @throws Exception 
	 */
	public static void checkSqlMapConfig() throws Exception{
		/* 检查有没有sql-map-module.xml配置文件*/
		File sqlMapConfigFile = FileUtil.createFile(BuilderUtil.getSqlmapConfigPath());
		if(!sqlMapConfigFile.exists()){
			Document sqlmapDocument  = DocumentHelper.createDocument();//创建document文件
			
			sqlmapDocument.addElement("sqlMapImport");//创建根节点
			
			FileUtil.writeXml(sqlmapDocument, sqlMapConfigFile);//写回操作
			
			/* 检查有没有sql-map-import.xml注册此配置文件*/
			File sqlMapImportFile = FileUtil.createFile(BuilderUtil.getSqlmapImportPath());
			// 构建Dom树
			Document sqlmapImportdocument = FileUtil.readXml(sqlMapImportFile);
			List sqlMapList = sqlmapImportdocument.selectNodes("/sqlMapConfig/sqlMapImport/@resource");
			Iterator sqlMapIter = sqlMapList.iterator();
			String importFile = "sql/"+StaticVariable.MODULE+"/"+ StaticVariable.DB +"/sql-map-"+StaticVariable.MODULE+".xml";
			boolean mark = true;
			while (sqlMapIter.hasNext()) {
				Attribute attribute = (Attribute) sqlMapIter.next();
				if (importFile.equals(attribute.getValue())) {
					mark = false;
					break;
				}
			}
		
			// 如果没有相同的importFile，则新加入节点
			if (mark) {
				// 根元素sqlMapConfig
				Element sqlMapConfig = (Element) sqlmapImportdocument.getRootElement();
				// 构建新增节点
				Element sqlMap = sqlMapConfig.addElement("sqlMapImport");
				sqlMap.addAttribute("resource", importFile);
			}
			// 保持到文件
			FileUtil.writeXml(sqlmapImportdocument,sqlMapImportFile);
			//注册本系统根节点
			EngineParameter ep = new EngineParameter("T_SYS_RESOURCE.selectSome");
			ep.putParam("RESOURCE_ADDR", StaticVariable.MODULE);
			ep.putParam("FACETYPE", "subSystem");
			Engine.execute(ep);
			
			List<Map> listRoot = (List<Map>) ep.getResult("data");
			
			if(listRoot.size() == 0){
				long maxCode = 100;
				
				ep = new EngineParameter("T_SYS_RESOURCE.selectMaxCode");
				ep.putParam("RESOURCE_CODE", "___");
				Engine.execute(ep);
				
				if(!(ep.getResult("data") instanceof Map)){
					maxCode = Long.parseLong(ep.getResult("data").toString()) + 1;
				}
				
				ep = new EngineParameter("T_SYS_RESOURCE.insert");
				ep.setCommandType("insert");
				ep.putParam("RESOURCE_CODE", maxCode + "");
				ep.putParam("RESOURCE_TYPE_ID", "19AADE52436C4FA99BC3B9897E7B9408");
				ep.putParam("RESOURCE_NAME", StaticVariable.MODULE);
				ep.putParam("RESOURCE_ADDR", StaticVariable.MODULE);
				ep.putParam("RESOURCE_HELPINFO", StaticVariable.MODULE);
				ep.putParam("SECURITY_NAME", StaticVariable.MODULE);
				ep.putParam("PID", "72E79D997AE4441E90D4EB7842AE0F1D");
				ep.putParam("CACHE", "1");
				ep.putParam("FACETYPE", "subSystem");
				
				Engine.execute(ep);
			}
		}
	}
	
}
