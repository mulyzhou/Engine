package com.flying.builder;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

import com.flying.exception.FlyingException;
import com.flying.init.Item;
import com.flying.init.StaticVariable;
import com.flying.logging.Log;
import com.flying.logging.LogFactory;
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
	
}
