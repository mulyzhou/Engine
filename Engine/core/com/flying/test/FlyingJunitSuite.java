package com.flying.test;

import java.util.Iterator;
import java.util.List;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.dom4j.Document;
import org.dom4j.Element;

import com.flying.builder.TableNameFile;
import com.flying.exception.FlyingException;
import com.flying.init.EngineInit;
import com.flying.init.StaticVariable;
import com.flying.logging.Log;
import com.flying.logging.LogFactory;
import com.flying.service.Engine;
import com.flying.util.FileUtil;
import com.flying.util.FlyingUtil;

public class FlyingJunitSuite {
	private static Log log = LogFactory.getLog(FlyingJunitSuite.class);
	
	static{
		if(Engine.ac == null){
			log.info("java应用系统初始化！");
			//系统初始化
			EngineInit.appStart();
		}else{
			log.debug("spring 环境已经初始化完毕！");
		}
	}
	
	public static Test suite(){
		//构建suite
		TestSuite ts = new TestSuite();
		//初始化junit suite
		log.debug("初始化junit suite");
		Document junitImportDocument = FileUtil.readXml(Thread.currentThread().getContextClassLoader().getResourceAsStream("config/junit-import.xml"));
		//设置junit suite名称
		ts.setName(junitImportDocument.getRootElement().attributeValue("name") != null ? junitImportDocument.getRootElement().attributeValue("name") : "Suite");
		
		List junitList = junitImportDocument.selectNodes("/suites/import");
		// 遍历
		Iterator junitIter = junitList.iterator();
		while (junitIter.hasNext()) {
			Element itemElement = (Element) junitIter.next();
			
			String resource = "";
			if(FlyingUtil.validateData(itemElement.attribute("resource")) && FlyingUtil.validateData(itemElement.attribute("resource").getValue())){
				resource = itemElement.attribute("resource").getValue().trim();
			}else{
				log.warn("解析的resource为空 ！");
				continue;
			}
			
			log.debug("解析： " + resource +" 开始");

			try {
				ts.addTest(singleParse(resource));
			} catch (ClassNotFoundException e) {
				log.error("未找到的测试类！",e);
			}
			
			log.debug("解析 "+ resource +" 结束");
		}
		
		return ts;
	}
	
	private static TestSuite singleParse(String subJunitSuitePath) throws ClassNotFoundException{
		TestSuite subTs = new TestSuite();
		//解析成document
		Document subJunitSuiteDocument = FileUtil.readXml(Thread.currentThread().getContextClassLoader().getResourceAsStream(subJunitSuitePath));
		subTs.setName(subJunitSuiteDocument.getRootElement().attributeValue("name") != null ? subJunitSuiteDocument.getRootElement().attributeValue("name") : "Sub Suite");
		//模块集合
		List testList = subJunitSuiteDocument.selectNodes("/suite/test");
		// 遍历
		Iterator testIter = testList.iterator();
		while (testIter.hasNext()) {
			Element testElement = (Element) testIter.next();
			if(FlyingUtil.validateData(testElement.attribute("class")) && FlyingUtil.validateData(testElement.attribute("class").getValue())){
				String className = testElement.attribute("class").getValue().trim();
				subTs.addTest(new JUnit4TestAdapter(Class.forName(className)));
				String alias = testElement.attribute("alias") == null?"":testElement.attribute("alias").getValue();
				if(!FlyingUtil.validateData(alias)){
					alias = "对象";
				}
				log.debug(alias + " : " + className + " 加入测试队列");
			}else{
				log.warn("解析的suite的test的class为空 ！");
				continue;
			}
		}
		return subTs;
	}
}
