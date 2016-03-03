package com.flying.init;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.sql.rowset.serial.SerialArray;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.flying.builder.TableNameFile;
import com.flying.dao.MongoDBDAO;
import com.flying.exception.FlyingException;
import com.flying.logging.Log;
import com.flying.logging.LogFactory;
import com.flying.service.Engine;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;

public class EngineInit {
	private static Log log = LogFactory.getLog(EngineInit.class);
	
	/**
	 * 初始化系统参数
	 */
	static{
		InputStream in = null;// 文件输入流
		Properties pp = new Properties();// 数据库属性

		try{
			in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config/flying.properties");
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
		String isRedis = pp.getProperty("flying.REDIS") == null ? "" : pp
				.getProperty("flying.REDIS");
		String mongoDB = pp.getProperty("flying.MONGODB") == null ? "" : pp
				.getProperty("flying.MONGODB");
		String rootmenu = pp.getProperty("flying.ROOT_MENU") == null ? "" : pp
				.getProperty("flying.ROOT_MENU");
		try{
			if (!"".equals(debug)
					&& ("true".equals(debug) || "false".equals(debug))) {// 设置调试模式
				StaticVariable.DEBUG = Boolean.parseBoolean(debug);
			} else {
				throw new FlyingException("flying.DEBUG 参数为空或者有误!");
			}
			
			StaticVariable.PATH = path;
			if(StaticVariable.DEBUG){
				if ("".equals(path)) {// 调试模式下，开发环境地址
					throw new FlyingException("flying.PATH 参数为空!");
				}
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
			
			if (!"".equals(isRedis)
					&& ("true".equals(isRedis) || "false".equals(isRedis))) {// 设置启动redis
				StaticVariable.REDIS = Boolean.parseBoolean(isRedis);
			}
			
			if (!"".equals(mongoDB)
					&& ("true".equals(mongoDB) || "false".equals(mongoDB))) {// 设置启动mongoDB
				StaticVariable.MONGODB = Boolean.parseBoolean(mongoDB);
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
	/**
	 * 初始系统的数据库连接参数
	 */
	static{
		/**
		 * 在系统初始化的时候，根据用户连接数据的驱动信息，配置用户使用的数据库类型信息
		 */
		InputStream in;// 文件输入流
		Properties pp = new Properties();// 数据库属性
		try {
			in = Thread.currentThread().getContextClassLoader().getResourceAsStream("sql/database.properties");// 将文件编程输入流
			pp.load(in);// 将输入流编程属性文件
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String driver = pp.getProperty("driver");// 获取驱动属性
		StaticVariable.DB_URL = pp.getProperty("url");// 获取数据库地址
		if (driver.contains("mysql")) {// 驱动中含有mysql，则是mysql数据库
			StaticVariable.DB = "mysql";

		} else if (driver.contains("oracle")) {// 驱动中含有oracle，则是oracle数据库
			StaticVariable.DB = "oracle";

		} else {// 其他则为空字符
			StaticVariable.DB = "";
		}

		log.debug("本系统使用的数据库是：" + StaticVariable.DB);
	}
	
	/**
	 * 根据配置初始化mongoDB
	 */
	static{
		if(StaticVariable.MONGODB){
			InputStream in;// 文件输入流
			Properties pp = new Properties();// 数据库属性
			try {
				in = Thread.currentThread().getContextClassLoader().getResourceAsStream("sql/mongodb.properties");// 将文件编程输入流
				pp.load(in);// 将输入流编程属性文件
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			String ip = pp.getProperty("ip") == null ? "127.0.0.1" : pp
    				.getProperty("ip");
            String port = pp.getProperty("port") == null ? "27017" : pp
    				.getProperty("port");
            String dbName = pp.getProperty("dbname") == null ? StaticVariable.ALIAS : pp
    				.getProperty("dbname");
            int connectionsPerHost = pp.getProperty("connectionsPerHost") == null ? 50 : Integer.parseInt(pp
    				.getProperty("connectionsPerHost"));
            int threadsAllowedToBlockForConnectionMultiplier = pp.getProperty("threadsAllowedToBlockForConnectionMultiplier") == null ? 50 : Integer.parseInt(pp
    				.getProperty("threadsAllowedToBlockForConnectionMultiplier"));
            int maxWaitTime = pp.getProperty("maxWaitTime") == null ? 120000 : Integer.parseInt(pp
    				.getProperty("maxWaitTime"));
            int connectTimeout = pp.getProperty("connectTimeout") == null ? 60000 : Integer.parseInt(pp
    				.getProperty("connectTimeout"));
            //连接路径
            ServerAddress sd = new ServerAddress(ip,Integer.parseInt(port));
            //连接配置
			MongoClientOptions.Builder build = new MongoClientOptions.Builder();    
            build.connectionsPerHost(connectionsPerHost);   //与目标数据库能够建立的最大connection数量为50
            build.threadsAllowedToBlockForConnectionMultiplier(threadsAllowedToBlockForConnectionMultiplier); //如果当前所有的connection都在使用中，则每个connection上可以有50个线程排队等待   
            /* 
             * 一个线程访问数据库的时候，在成功获取到一个可用数据库连接之前的最长等待时间为2分钟 
             * 这里比较危险，如果超过maxWaitTime都没有获取到这个连接的话，该线程就会抛出Exception 
             * 故这里设置的maxWaitTime应该足够大，以免由于排队线程过多造成的数据库访问失败 
             */  
            build.maxWaitTime(maxWaitTime);  
            build.connectTimeout(connectTimeout);    //与数据库建立连接的timeout设置为1分钟   
              
            MongoClientOptions myOptions = build.build(); 
            
            try {  
                //数据库客户端实例   
            	MongoDBDAO.mongoClient = new MongoClient(sd, myOptions);   
            	//数据库实例名
            	MongoDBDAO.DB_NAME = dbName;
            } catch (MongoException e){  
                e.printStackTrace();  
            }  
           
			log.debug("mongoDB初始化完成！");	
		}
	}
	/**
	 * 
	 */
	public static void appStart(){
		log.info("java应用系统初始化！");
		//通用的引导
		webStart();
		
		//解析tablename文件
		try {
			TableNameFile.parse();
		} catch (FlyingException e) {
			log.error("tablename解析失败！", e);
		}
	}
	/**
	 * 系统初始化引导方法
	 * 引导初始化的代码片段执行
	 * @author zdf
	 */
	public static void webStart(){
		String applicationContext = "config/applicationContext2MongoDB.xml";
		
		/* 构建spring运行环境 */
		ApplicationContext wac = new ClassPathXmlApplicationContext(
				new String[] { applicationContext });
		
		/* 设置引擎运行环境 */
		Engine.ac = wac;
	}
}
