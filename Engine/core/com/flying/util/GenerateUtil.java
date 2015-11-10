package com.flying.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.flying.builder.ExtPage;
import com.flying.builder.IbatisXml;
import com.flying.builder.TableNameFile;
import com.flying.exception.FlyingException;
import com.flying.init.EngineInit;
import com.flying.init.Item;
import com.flying.init.StaticVariable;
import com.flying.service.Engine;
import com.flying.service.EngineParameter;
import com.flying.test.FlyingJunitSuite;

public class GenerateUtil {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EngineInit.appStart();
	}
	/**
	 * 系统手动生成
	 * 生成前台，后台，数据库
	 */
	public static void generateModule(String dir){
		//生成tablename配置文件
		generateModuleTablename(dir);
		//生成后台ibatis文件
		generateModuleIbatisXml(dir);
		//生成前台ext文件
		generateModuleExtPage(dir);
	}
	
	/**
	 * 根据模块生成Ext前台文件
	 * @param dir
	 */
	public static void generateModuleExtPage(String dir){
		//根据模块名称，获取模块下对应的表
		List<Map> tableList = selectTableByModule();
		
		for (int i = 0; i < tableList.size(); i++) {
			Map tableMap = tableList.get(i);
			String tableName = tableMap.get("BMC").toString().toUpperCase();
			if(tableName.contains("_"+ StaticVariable.MODULE.toUpperCase() + "_")){
				Item item = new Item(tableMap.get("BMC")==null?"":tableMap.get("BMC").toString(),tableMap.get("BZS")==null?"":tableMap.get("BZS").toString());
				try {
					ExtPage.insert(item,dir);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 生成Ext前台文件
	 * @param dir
	 */
	public static void generateExtPage(Item item ,String dir){
		try {
			ExtPage.insert(item,dir);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 根据模块生成后台ibatis文件
	 * @param dir
	 */
	public static void generateModuleIbatisXml(String dir){
		//根据模块名称，获取模块下对应的表
		List<Map> tableList = selectTableByModule();
		
		for (int i = 0; i < tableList.size(); i++) {
			Map tableMap = tableList.get(i);
			String tableName = tableMap.get("BMC").toString().toUpperCase();
			if(tableName.contains("_"+ StaticVariable.MODULE.toUpperCase() + "_")){
				Item item = new Item(tableMap.get("BMC")==null?"":tableMap.get("BMC").toString(),tableMap.get("BZS")==null?"":tableMap.get("BZS").toString());
				try {
					IbatisXml.insert(item,dir);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 生成后台ibatis文件
	 * @param dir
	 */
	public static void generateIbatisXml(Item item ,String dir){
		try {
			IbatisXml.insert(item,dir);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 默認生成tablename-sub.xml
	 */
	public static void generateModuleTablename(String dir){
		//根据模块名称，获取模块下对应的表
		List<Map> tableList = selectTableByModule();
		
		List<Map> newTableList = new ArrayList<Map>();
		for (int i = 0; i < tableList.size(); i++) {
			Map tableMap = tableList.get(i);
			String tableName = tableMap.get("BMC").toString().toUpperCase();
			if(tableName.contains("_"+ StaticVariable.MODULE.toUpperCase() + "_")){
				newTableList.add(tableMap);
			}
		}
		
		try {
			TableNameFile.generateItem(newTableList,dir);
		} catch (FlyingException e) {
			e.printStackTrace();
		}
	}
	
	public static void generateTablename(List<Map> newTableList,String dir) throws FlyingException{
		TableNameFile.generateItem(newTableList, dir);
	}
	/**
	 * 默認生成junit代碼
	 */
	public static void generateModuleJunitCode(String dir){
		String schema = StaticVariable.DB_URL.substring(StaticVariable.DB_URL.lastIndexOf("/")  +1);
		if(schema.contains("?")){
			schema = schema.substring(0,schema.indexOf("?"));
		}
		
		EngineParameter ep = new EngineParameter("mysql.selectTable");
		ep.putParam("filter", "TABLE_TYPE = 'BASE TABLE' AND TABLE_SCHEMA = '"+ schema +"'");
		try {
			Engine.execute(ep);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		List<Map> tableList = (List<Map>) ep.getResult("data");
		for (int i = 0; i < tableList.size(); i++) {
			Map tableMap = tableList.get(i);
			String tableName = tableMap.get("BMC").toString().toUpperCase();
			if(tableName.contains("_"+ StaticVariable.MODULE.toUpperCase() + "_")){
				Item item = new Item();
				item.setName(tableName);
				item.setAlias(tableMap.get("BZS").toString());
				
				try {
					generateJunitCode(item, dir);
					registerJUnitConfig(item, dir);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 通用方式，通过自主建立的表，进行生成工作
	 * 
	 * @param item
	 * @throws Exception
	 */
	public static void generateJunitCode(Item item, String dir) throws Exception {
		String tableName = item.getName();// 表名

		String chineseName = item.getAlias();// 表中文名

		String[] tableFragment = tableName.split("_");

		String sub = tableFragment[1].toLowerCase();

		String Sub = sub.substring(0, 1).toUpperCase() + sub.substring(1);

		String table = tableFragment[2].toLowerCase();

		String Table = table.substring(0, 1).toUpperCase() + table.substring(1);

		String ID = "";// 主键

		EngineParameter ep = null;

		if ("oracle".equals(StaticVariable.DB)) {
			ep = new EngineParameter("oracle.selectFieldByBmc");
		} else if ("mysql".equals(StaticVariable.DB)) {
			ep = new EngineParameter("mysql.selectFieldByBmc");
		}

		ep.setCommandType("list");
		ep.putParam("BMC", tableName);
		Engine.execute(ep);

		List<Map> listColumn = ep.getResult("data") == null ? null
				: (List<Map>) ep.getResult("data");// 获取列数据

		// 在列中，去掉主键限制重复列
		if ("oracle".equals(StaticVariable.DB)) {
			Map column = null;
			boolean mark = false;

			for (int m = 0; m < listColumn.size(); m++) {
				column = listColumn.get(m);
				if (column.get("ZDXZ") != null
						&& "P".equals(column.get("ZDXZ").toString())) {
					ID = column.get("ZDMC").toString();
					break;
				}
			}

			for (int m = 0; m < listColumn.size(); m++) {
				column = listColumn.get(m);
				if (column.get("ZDXZ") != null
						&& "C".equals(column.get("ZDXZ").toString())
						&& ID.equals(column.get("ZDMC").toString())) {
					mark = true;
					break;
				}
			}

			if (mark) {
				listColumn.remove(column);
			}
		}

		StringBuffer tableMapInitStr = new StringBuffer();
		StringBuffer tableMapUpdateStr = new StringBuffer();
		StringBuffer assertEqualsTableMapStr = new StringBuffer();
		for (int i = 0; i < listColumn.size(); i++) {// 遍历构造数据结构
			Map column = listColumn.get(i);
			if ((column.get("SFZJ") != null && ("true".equals(column
					.get("SFZJ").toString()) || "1".equals(column.get("SFZJ")
					.toString())))
					|| (column.get("ZDXZ") != null && "P".equals(column.get(
							"ZDXZ").toString()))
					|| (column.get("NULL") != null && column.get("KEY") != null && "PRI"
							.equals(column.get("KEY").toString()))) {
				ID = column.get("ZDMC").toString().toUpperCase();

			} else if (column.get("ZDLX").toString().contains("int")
					|| "NUMBER".equals(column.get("ZDLX").toString())) {
				Random ra = new Random();
				tableMapInitStr.append(sub + Table + "Map.put(\""
						+ column.get("ZDMC").toString().toUpperCase() + "\", "
						+ ra.nextInt() + ");\n");
				tableMapUpdateStr.append(sub + Table + "Map.put(\""
						+ column.get("ZDMC").toString().toUpperCase() + "\", "
						+ ra.nextInt() + ");\n");
				assertEqualsTableMapStr.append("Assert.assertEquals(" + sub
						+ Table + "Map.get(\""
						+ column.get("ZDMC").toString().toUpperCase()
						+ "\"), ((Map)ep.getResult(\"data\")).get(\""
						+ column.get("ZDMC").toString().toUpperCase()
						+ "\"));\n");

			} else if (column.get("ZDLX").toString().toLowerCase()
					.contains("varchar")) {
				int len = 0;
				if (column.get("ZDCD") != null) {
					len = Integer.parseInt(column.get("ZDCD").toString());
				} else {
					len = Integer
							.parseInt(column
									.get("ZDLX")
									.toString()
									.substring(
											column.get("ZDLX").toString()
													.indexOf("(") + 1,
											column.get("ZDLX").toString()
													.indexOf(")")));
				}
				tableMapInitStr.append(sub + Table + "Map.put(\""
						+ column.get("ZDMC").toString().toUpperCase()
						+ "\", \"" + FlyingUtil.getRandomJianHan(len) + "\");\n");
				tableMapUpdateStr.append(sub + Table + "Map.put(\""
						+ column.get("ZDMC").toString().toUpperCase()
						+ "\", \"" + FlyingUtil.getRandomJianHan(len) + "\");\n");
				assertEqualsTableMapStr.append("Assert.assertEquals(" + sub
						+ Table + "Map.get(\""
						+ column.get("ZDMC").toString().toUpperCase()
						+ "\"), ((Map)ep.getResult(\"data\")).get(\""
						+ column.get("ZDMC").toString().toUpperCase()
						+ "\"));\n");

			} else if (column.get("ZDLX").toString().toLowerCase()
					.contains("date")) {
				tableMapInitStr.append(sub + Table + "Map.put(\""
						+ column.get("ZDMC").toString().toUpperCase()
						+ "\", new Date());\n");
				tableMapUpdateStr.append(sub + Table + "Map.put(\""
						+ column.get("ZDMC").toString().toUpperCase()
						+ "\", new Date());\n");
				assertEqualsTableMapStr
						.append("Assert.assertTrue(((Date)"
								+ sub
								+ Table
								+ "Map.get(\""
								+ column.get("ZDMC").toString().toUpperCase()
								+ "\")).getTime() - ((Date)((Map)ep.getResult(\"data\")).get(\""
								+ column.get("ZDMC").toString().toUpperCase()
								+ "\")).getTime() < 1000);");
			}

		}
		// 将模板文件变成String
		String template = FileUtil.streamToString(Thread.currentThread()
				.getContextClassLoader()
				.getResourceAsStream("config/TestObject.vm"));

		/**
		 * 进行替换 1.前缀 sub Sub 2.表名称 table Table 3.主键 ID 4.对应的Table 5.初始化TableMap
		 * 6.断言TableMap
		 */
		// 前缀 sub Sub
		template = template.replaceAll("&sub&", sub);
		template = template.replaceAll("&Sub&", Sub);
		// 表名称 table Table
		template = template.replaceAll("&table&", table);
		template = template.replaceAll("&Table&", Table);
		// 主键 ID
		template = template.replaceAll("&ID&", ID);
		// 对应的Table
		template = template
				.replaceAll("&T_SUB_TABLE&", tableName.toUpperCase());
		// 初始化TableMap
		template = template.replaceAll("&tableMap_init&",
				tableMapInitStr.toString());
		// 修改TableMap
		template = template.replaceAll("&tableMap_update&",
				tableMapUpdateStr.toString());
		// 断言TableMap
		template = template.replaceAll("&Assert.assertEquals.tableMap&",
				assertEqualsTableMapStr.toString());
		// 创建测试文件
		if (dir == null) {
			dir = StaticVariable.PATH + sub + "/com/flying/test/" + sub
					+ "/Test" + Sub + Table + ".java";
		} else {
			dir = dir + "/Test" + Sub + Table + ".java";
		}

		FileUtil.stringToFile(template, FileUtil.createFile(dir));
	}

	/**
	 * 将生产的测试类在junit-sub.xml文件中进行注册
	 * 
	 * @param item
	 * @param dir
	 * @throws FlyingException
	 */
	public static void registerJUnitConfig(Item item, String dir)
			throws FlyingException {
		String tableName = item.getName();// 表名

		String[] tableFragment = tableName.split("_");

		String sub = tableFragment[1].toLowerCase();

		String Sub = sub.substring(0, 1).toUpperCase() + sub.substring(1);

		String table = tableFragment[2].toLowerCase();

		String Table = table.substring(0, 1).toUpperCase() + table.substring(1);

		// 将文件注册到junit-sys.xml文件
		String junitConfig = null;
		if (dir == null) {
			junitConfig = StaticVariable.PATH + "src/config/junit-" + sub
					+ ".xml";
		} else {
			junitConfig = dir + "src/config/junit-" + sub + ".xml";
		}

		File junitConfigFile = FileUtil.createFile(junitConfig);
		if(!junitConfigFile.exists()){
			Document junitConfigDocument  = DocumentHelper.createDocument();//创建document文件
			
			junitConfigDocument.addElement("suite");//创建根节点
			
			FileUtil.writeXml(junitConfigDocument, junitConfigFile);//写回操作
		}
		// 构建Dom树
		Document junitDocument = FileUtil.readXml(junitConfigFile);
		/**
		 * 判断junit-sub.xml底下是否有此表的junit测试类 如果有，则不注册 如果无，则注册
		 * 
		 */
		String junitName = "com.flying.test." + sub + ".Test" + Sub + Table;
		// 标记true，则表示没有，false，则表示有
		boolean mark = true;
		/**
		 * 判断junit-sub.xml底下是否已经有属性是class的节点 如果有,不添加节点 如果无，添加节点
		 */
		// 获取sqlMapConfig标签底下的sqlMap标签的属性resourse
		List junitList = junitDocument.selectNodes("/suite/test/@class");
		Iterator junitIter = junitList.iterator();
		while (junitIter.hasNext()) {
			Attribute attribute = (Attribute) junitIter.next();
			if (junitName.equals(attribute.getValue())) {
				mark = false;
				break;
			}
		}
		// 如果没有相同的resource，则新加入节点
		if (mark) {
			// 根元素sqlMapConfig
			Element junitConfigRoot = (Element) junitDocument.getRootElement();
			// 构建新增节点
			Element junitEl = junitConfigRoot.addElement("test");
			junitEl.addAttribute("alias", item.getAlias());
			junitEl.addAttribute("class", junitName);
		}

		// 保持到文件
		FileUtil.writeXml(junitDocument, junitConfigFile);
	}
	
	private static List<Map> selectTableByModule(){
		String schema = StaticVariable.DB_URL.substring(StaticVariable.DB_URL.lastIndexOf("/")  +1);
		if(schema.contains("?")){
			schema = schema.substring(0,schema.indexOf("?"));
		}
		
		EngineParameter ep = new EngineParameter("mysql.selectTable");
		ep.putParam("filter", "TABLE_TYPE = 'BASE TABLE' AND TABLE_SCHEMA = '"+ schema +"'");
		try {
			Engine.execute(ep);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		return (List<Map>) ep.getResult("data");
		
	}
}
