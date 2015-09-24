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
import com.flying.builder.SqlMapConfig;
import com.flying.builder.TableNameFile;
import com.flying.exception.FlyingException;
import com.flying.init.EngineInit;
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
		log.info("Web系统初始化！");
		
		//系统初始化
		EngineInit.webStart();
		
		/* 初始化根路径 */
		if (!StaticVariable.DEBUG) {
			log.debug("系统在容器中运行，生成的文件将直接放在容器中");
			String realPath = getServletContext().getRealPath("/") + "/";
			StaticVariable.SERVER_PATH = realPath;
			StaticVariable.PATH = realPath;
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
	/**
	 * 检查基础模块是否存在
	 */
	private void checkModule(){
		try {
			//调试模式才需要去检查
			if(StaticVariable.DEBUG){
				//检查tablename-module.xml配置文件是否存在
				TableNameFile.checkTablename();
				
				// 检查sql-map-module.xml配置文件是否存在
				SqlMapConfig.checkSqlMapConfig();
			}
		} catch (FlyingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 系统运行时，用户自定义事件
	 */
	private void executeStartup(){
		for(int i = 0 ;i < StaticVariable.TASKS.size() ;i++){
			ITask task = StaticVariable.TASKS.get(i);
			task.execute();
		}
	}
}