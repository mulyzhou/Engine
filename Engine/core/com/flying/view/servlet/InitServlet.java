package com.flying.view.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServlet;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.flying.builder.BuilderUtil;
import com.flying.exception.FlyingException;
import com.flying.init.StaticVariable;
import com.flying.logging.Log;
import com.flying.logging.LogFactory;
import com.flying.service.Engine;
import com.flying.service.EngineParameter;
import com.flying.task.ITask;
import com.flying.util.FileUtil;

/**
 * 
 * <B>描述：</B>初始化类<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 */
public class InitServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(InitServlet.class);
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public InitServlet() {
		super();
	}

	/**
	 * 读取tablename.xml 解析 放入缓存
	 */
	public void init() {
		log.info("系统初始化！");
		
		/* 初始化环境参数 */
		initEnvironment();

		/* 判断使用的数据库*/
		initDB();
		
		/* 初始化spring容器 */
		initContainer();

		/* 初始化根路径 */
		if (!StaticVariable.DEBUG) {
			log.debug("系统在容器中运行，生成的文件将直接放在容器中");
			StaticVariable.PATH = getServletContext().getRealPath("/") + "/";
		} else {
			log.debug("系统在调试模式下运行，生成的文件将直接放在开发工具中");
		}
		/* 进行生成工作 */
		try {
			BuilderUtil.startBuilder();
		} catch (Exception e) {
			log.error("代码生成过程有误！",e);
		}
		/* 检查模块环境*/
		checkModule();
		/* 执行各个业务启动时代码*/
		executeStartup();
	}
	
	private void initEnvironment() {
		String realPath = getServletContext().getRealPath("/") + "/";
		
		StaticVariable.SERVER_PATH = realPath;
		log.debug(realPath);
		String uri = realPath + "WEB-INF/classes/config/flying.properties";

		FileInputStream in = null;// 文件输入流
		Properties pp = new Properties();// 数据库属性
		try {
			in = new FileInputStream(uri);// 将文件编程输入流
		} catch (FileNotFoundException e) {
			log.error(uri + " 无法找到文件！",e);
			log.debug("根据maven的路径结构进行查找文件！");
			realPath = getServletContext().getRealPath("/").replace("webapp", "resources");
			uri = realPath + "/config/flying.properties";
			try{
				in = new FileInputStream(uri);// 将文件编程输入流
				StaticVariable.MAVEN = true;
			}catch(FileNotFoundException ee){
				log.error(uri + "无法找到文件！",e);
			}
		}

		try{
			pp.load(in);// 将输入流编程属性文件
		}catch(IOException e){
			log.error("flying.properties 配置文件有错误！",e);
		}
		
		String debug = pp.getProperty("flying.DEBUG") == null ? "" : pp
				.getProperty("flying.DEBUG");
		String path = pp.getProperty("flying.PATH") == null ? "" : pp
				.getProperty("flying.PATH");
		String module = pp.getProperty("flying.MODULE") == null ? "" : pp
				.getProperty("flying.MODULE");
		String alias = pp.getProperty("flying.ALIAS") == null ? "" : pp
				.getProperty("flying.ALIAS");
		String authentication = pp.getProperty("flying.AUTHENTICATION") == null ? "" : pp
				.getProperty("flying.AUTHENTICATION");
		String ssoAuth = pp.getProperty("flying.SSO_AUTH") == null ? "" : pp
				.getProperty("flying.SSO_AUTH");
		String flyinglog = pp.getProperty("flying.LOG") == null ? "" : pp
				.getProperty("flying.LOG");
		String rootmenu = pp.getProperty("flying.ROOT_MENU") == null ? "" : pp
				.getProperty("flying.ROOT_MENU");
		try{
			if (!"".equals(debug)
					&& ("true".equals(debug) || "false".equals(debug))) {// 设置调试模式
				StaticVariable.DEBUG = Boolean.parseBoolean(debug);
			} else {
				throw new FlyingException("flying.DEBUG 参数为空或者有误!");
			}
			
			if(StaticVariable.DEBUG){
				if (!"".equals(path)) {// 调试模式下，开发环境地址
					StaticVariable.PATH = path;
				} else {
					throw new FlyingException("flying.PATH 参数为空!");
				}
			}else{
				StaticVariable.PATH = realPath;
			}
			
	
			if (!"".equals(module)) {// 生成ibatis配置文件的别名
				StaticVariable.MODULE = module.toLowerCase();
			} else {
				throw new FlyingException("flying.MODULE 参数为空!");
			}
	
			if (!"".equals(alias)) {// 生成ibatis配置文件的别名
				StaticVariable.ALIAS = alias;
			} else {
				throw new FlyingException("flying.ALIAS 参数为空!");
			}
			
			if (!"".equals(authentication)
					&& ("true".equals(authentication) || "false".equals(authentication))) {// 设置调试模式
				StaticVariable.AUTHENTICATION = Boolean.parseBoolean(authentication);
			} else {
				throw new FlyingException("flying.AUTHENTICATION 参数为空或者有误!");
			}
			
			if (!"".equals(ssoAuth)) {//是否开启单点登录
				StaticVariable.SSO_AUTH = Boolean.parseBoolean(ssoAuth);
			}
			
			if (!"".equals(flyinglog)
					&& ("true".equals(flyinglog) || "false".equals(flyinglog))) {// 设置调试模式
				StaticVariable.LOG = Boolean.parseBoolean(flyinglog);
			}
			
			if (!"".equals(rootmenu)) {// 生产的根节点ID
				StaticVariable.ROOT_MENU = rootmenu;
			} 
			
			if(StaticVariable.MAVEN){
				StaticVariable.CONFIG_PATH = "src/main/resources";
				StaticVariable.DEBUG = true;
			}else{
				StaticVariable.CONFIG_PATH = "src";
			}
		}catch(FlyingException e){
			e.printStackTrace();
		}
		
		log.debug("解析flying.properties配置文件完成");
	}
	
	private void initDB(){
		/**
		 * 在系统初始化的时候，根据用户连接数据的驱动信息，配置用户使用的数据库类型信息
		 */
		String uri = "";
		if (StaticVariable.DEBUG) {
			uri = StaticVariable.PATH + "src/sql/database.properties";
		} else {
			uri = StaticVariable.PATH
					+ "WEB-INF/classes/sql/database.properties";// 数据库文件路径
		}
		FileInputStream in;// 文件输入流
		Properties pp = new Properties();// 数据库属性
		try {
			in = new FileInputStream(uri);// 将文件编程输入流
			pp.load(in);// 将输入流编程属性文件
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String driver = pp.getProperty("driver");// 获取驱动属性

		if (driver.contains("mysql")) {// 驱动中含有mysql，则是mysql数据库
			StaticVariable.DB = "mysql";

		} else if (driver.contains("oracle")) {// 驱动中含有oracle，则是oracle数据库
			StaticVariable.DB = "oracle";

		} else {// 其他则为空字符
			StaticVariable.DB = "";
		}

		log.debug("本系统使用的数据库是：" + StaticVariable.DB);
	}
	
	private void checkModule(){
		try {
			//调试模式才需要去检查
			if(StaticVariable.DEBUG){
				/* 检查有没有tablename-module.xml配置文件*/
				File tableNameFile = FileUtil.createFile(BuilderUtil.getTableNameXmlPath());
				if(!tableNameFile.exists()){
					Document tableNameDocument  = DocumentHelper.createDocument();//创建document文件
					
					tableNameDocument.addElement("tablename");//创建根节点
					
					FileUtil.writeXml(tableNameDocument, tableNameFile);//写回操作
					
					/*将tablename注册入系统*/
					String tableNameImportXmlPath = BuilderUtil.getTableNameImportXmlPath();
					File tableNameImportXmlFile = FileUtil.createFile(tableNameImportXmlPath);
					Document tableNameImportDocument = FileUtil.readXml(tableNameImportXmlFile);
					Element tablenamesElement = (Element) tableNameImportDocument.getRootElement();
					// 构建新增节点
					Element importElement = tablenamesElement.addElement("import");
					importElement.addAttribute("resource", "config/tablename-"+StaticVariable.MODULE+".xml");
					// 保存到文件
					FileUtil.writeXml(tableNameImportDocument,tableNameImportXmlFile);
					
					log.debug("在tablename-import.xml中注册 "+ StaticVariable.MODULE +" 子系统");
				}
				
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
		} catch (FlyingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initContainer() {
		String applicationContext = "config/applicationContext.xml";
		//xfire的webservice支持
		//String xfireContext = "org/codehaus/xfire/spring/xfire.xml";
		
		/* 构建spring运行环境 */
		ApplicationContext wac = new ClassPathXmlApplicationContext(
				new String[] { applicationContext });//,xfireContext }); // WebApplicationContextUtils.getWebApplicationContext(application);

		/* 设置引擎运行环境 */
		Engine.ac = wac;
	}
	
	private void executeStartup(){
		for(int i = 0 ;i < StaticVariable.TASKS.size() ;i++){
			ITask task = StaticVariable.TASKS.get(i);
			task.execute();
		}
	}
}