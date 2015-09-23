package com.flying.builder;

import java.io.File;
import java.util.List;

import com.flying.init.Item;
import com.flying.init.StaticVariable;
import com.flying.logging.Log;
import com.flying.logging.LogFactory;

/**
 * <B>描述：</B>代码生成工具类<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 * 
 */
public class BuilderUtil {

	private static Log log = LogFactory.getLog(BuilderUtil.class);// 日志

	/**
	 * 开始生成工作 1.解析tablename.xml （下面的几个过程，可以考虑使用多线程进行生成过程） 2.生成数据库数据
	 * 3.创建ibatis配置文件 4.注册ibatis配置文件 5.生成前台页面
	 * @throws Exception 
	 */
	public static void startBuilder() throws Exception {
		List<Item> needGenerateTable = TableNameFile.parse();// (1)解析tablename.xml开始

		for (int x = 0; x < needGenerateTable.size(); x++) {// 遍历需要处理的table

			RegisterTableToDB.insert(needGenerateTable.get(x));// (2)注册到数据库T_SYS_RESOURCE

			IbatisXml.insert(needGenerateTable.get(x));// (3)根据模板创建此表的Ibatis配置文件

			SqlMapConfig.insert(needGenerateTable.get(x));// (4)注册到ibatis配置文件中sql-map-config-core.xml

			ExtPage.insert(needGenerateTable.get(x));// (5)根据模板生成前台页面
		}
	}

	/**
	 * 删除系统文件（tablename.xml文件，ibatis文件，前台页面）
	 * 
	 * @param BMC
	 * @throws Exception 
	 */
	public static void deleteBuilder(String BMC) throws Exception {

		RegisterTableToDB.delete(BMC);
		
		SqlMapConfig.delete(BMC);

		IbatisXml.delete(BMC);

		ExtPage.delete(BMC);
		
		TableNameFile.delete(BMC);
	}
	/**
	 * 
	 * @return 获取根路径
	 */
	public static String getRootPath() {
		String rootPath = "";
		if (StaticVariable.DEBUG) {
			rootPath = StaticVariable.PATH + "src/";
		} else {
			rootPath = StaticVariable.PATH + "WEB-INF/classes/";
		}
		
		return rootPath;
	}
	/**
	 * ibatis配置文件路径，根据flying.properties配置文件中模块名称，返回不同的ibatis配置文件路径
	 * 
	 * @param tableName 表名
	 * @return 返回ibatis配置文件路径
	 */
	public static String getIbatisXmlPath(String tableName) {
		String fileName = "";
		if (StaticVariable.DEBUG) {
			fileName = StaticVariable.PATH + "src/sql/" + StaticVariable.MODULE
					+ "/" + StaticVariable.DB + "/" + tableName + ".xml";
		} else {
			fileName = StaticVariable.PATH + "WEB-INF/classes" + "/sql/"
					+ StaticVariable.MODULE + "/" + StaticVariable.DB + "/" + tableName + ".xml";// 路径
		}

		log.debug("ibatis配置的路径：" + fileName);
		return fileName;
	}

	/**
	 * sql-map-.xml文件路径，根据flying.properties配置文件中模块名称，返回不同模块的文件路径
	 * 
	 * @return 返回sql-map配置文件路径
	 */
	public static String getSqlmapConfigPath() {
		String sqlMapConfigPath = "";
		if (StaticVariable.DEBUG) {
			sqlMapConfigPath = StaticVariable.PATH + "src/sql/"
					+ StaticVariable.MODULE + "/" + StaticVariable.DB + "/sql-map-"
					+ StaticVariable.MODULE + ".xml";
		} else {
			sqlMapConfigPath = StaticVariable.PATH + "WEB-INF/classes/sql/"
					+ StaticVariable.MODULE + "/" + StaticVariable.DB + "/sql-map-"
					+ StaticVariable.MODULE + ".xml";// 路径
		}

		log.debug("sql-map-config.xml的路径：" + sqlMapConfigPath);
		return sqlMapConfigPath;
	}

	/**
	 * sql-map-import.xml文件路径
	 * 
	 * @return 返回sql-map-import.xml文件路径
	 */
	public static String getSqlmapImportPath() {
		String sqlMapImportPath = "";
		if (StaticVariable.DEBUG) {
			sqlMapImportPath = StaticVariable.PATH
					+ "src/sql/sql-map-import.xml";
		} else {
			sqlMapImportPath = StaticVariable.PATH
					+ "WEB-INF/classes/sql/sql-map-import.xml";// 路径
		}

		log.debug("sql-map-import.xml的路径：" + sqlMapImportPath);
		return sqlMapImportPath;
	}

	/**
	 * tablename*.xml的文件路径
	 * 
	 * @return 返回tablename*.xml的文件路径
	 */
	public static String getAllTableNameXmlPath() {
		String tableNamePath = "";
		if (StaticVariable.DEBUG) {
			tableNamePath = StaticVariable.PATH + "src/config/tablename*.xml";
		} else {
			tableNamePath = StaticVariable.PATH
					+ "WEB-INF/classes/config/tablename*.xml";// 数据库文件路径
		}

		log.debug("tablename.xml的路径：" + tableNamePath);
		return tableNamePath;
	}
	/**
	 * tablename-import.xml的文件路径，根据flying.properties配置文件中模块名称，返回不同模块的tablename文件路径
	 * 
	 * @return 返回不同模块的tablename文件路径
	 */
	public static String getTableNameImportXmlPath() {
		String tableNameImportPath = "";
		if (StaticVariable.DEBUG) {
			tableNameImportPath = StaticVariable.PATH + "src/config/tablename-import.xml";
		} else {
			tableNameImportPath = StaticVariable.PATH
					+ "WEB-INF/classes/config/tablename-import.xml";// 数据库文件路径
		}

		log.debug("tablename-import.xml的路径：" + tableNameImportPath);
		return tableNameImportPath;
	}
	/**
	 * tablename-.xml的文件路径，根据flying.properties配置文件中模块名称，返回不同模块的tablename文件路径
	 * 
	 * @return 返回不同模块的tablename文件路径
	 */
	public static String getTableNameXmlPath() {
		String tableNamePath = "";
		if (StaticVariable.DEBUG) {
			tableNamePath = StaticVariable.PATH + "src/config/tablename-"
					+ StaticVariable.MODULE + ".xml";
		} else {
			tableNamePath = StaticVariable.PATH
					+ "WEB-INF/classes/config/tablename-"
					+ StaticVariable.MODULE + ".xml";// 数据库文件路径
		}

		log.debug("tablename.xml的路径：" + tableNamePath);
		return tableNamePath;
	}

	/**
	 * 前台文件路径，根据flying.properties配置文件中模块名称，返回不同表的前台文件路径
	 * 
	 * @param tableName
	 *            表名
	 * @return 返回不同表的前台文件路径
	 */
	public static String getPagePath(String tableName) {
		String fileDir = "";
		String fileName = "";

		if (StaticVariable.DEBUG) {// 获取路径
			fileDir = StaticVariable.PATH + "WebContent/js/biz/"
					+ StaticVariable.MODULE + "/";
		} else {
			fileDir = StaticVariable.PATH + "js/biz/" + StaticVariable.MODULE
					+ "/";// 路径
		}

		if (tableName.lastIndexOf("/") > 0) {// 判断是否建立文件夹
			String dir = tableName.substring(0, tableName.lastIndexOf("/"));
			File file = new File(fileDir + dir);
			if (!file.isDirectory()) {
				file.mkdirs();
			}
		}

		fileName = fileDir + tableName + ".js";// 构建文件

		log.debug("前台js文件的路径：" + fileName);
		return fileName;
	}
	/**
	 * 前台文件路径，根据表名，获取前台文件，返回不同表的前台文件路径
	 * 
	 * @param tableName
	 *            表名
	 * @return 返回不同表的前台文件路径
	 */
	public static String getPagePathWithoutModule(String tableName) {
		String fileDir = "";
		String fileName = "";
		String moduleFile = tableName.split("_")[1].toLowerCase();
		if("base".equals(moduleFile)){
			moduleFile = "core";
		}
		
		if (StaticVariable.DEBUG) {// 获取路径
			fileDir = StaticVariable.PATH + "WebContent/js/biz/"
					+ moduleFile + "/";
		} else {
			fileDir = StaticVariable.PATH + "js/biz/" + moduleFile
					+ "/";// 路径
		}

		fileName = fileDir + tableName + ".js";// 构建文件

		log.debug("前台js文件的路径：" + fileName);
		return fileName;
	}
	/**
	 * ibatis模板文件路径
	 * 
	 * @return ibatis模板文件路径
	 */
	public static String getIbatisTemplatePath() {
		String templatePath = "";
		if (StaticVariable.DEBUG) {
			templatePath = StaticVariable.PATH + "src/config/template-"+StaticVariable.DB+".vm";
		} else {
			templatePath = StaticVariable.PATH
					+ "WEB-INF/classes/config/template-"+StaticVariable.DB+".vm";// 模板路径
		}

		log.debug("ibatis配置文件模板的路径：" + templatePath);
		return templatePath;
	}

	/**
	 *  返回表设计配置文件
	 *  
	 * @return 返回表设计配置文件
	 */
	public static String getTableXmlFile() {
		String path = StaticVariable.PATH;
		if (StaticVariable.DEBUG) {
			path = StaticVariable.PATH + "WebContent/tableXml.xml";
		} else {
			path = StaticVariable.PATH + "tableXml.xml";// 存放位置
		}

		return path;
	}
}
