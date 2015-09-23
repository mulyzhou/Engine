package com.flying.test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.flying.builder.BuilderUtil;
import com.flying.exception.FlyingException;
import com.flying.init.StaticVariable;
import com.flying.service.Engine;
import com.flying.util.FileUtil;

public class JsTest {

	@Before
	public void set() {
		ApplicationContext context = new FileSystemXmlApplicationContext("*/applicationContext*.xml");
		Engine.ac = context;
		
		StaticVariable.DEBUG = true;
	}
	
	@Test
	public void checkJs() throws Exception{
		String command = "T_BASE_MENU.insert";
		
		String tableName = command.substring(0,command.indexOf(".")); 
		
		String extPage = BuilderUtil.getPagePathWithoutModule(tableName);
		
		File extFile = new File(extPage);
		
		if(!extFile.exists()){
			throw new FlyingException("ext前台文件不存在！");
		}
		
		String extStr = FileUtil.fileToString(extFile);
		
		JSONObject jsonObj = JSONObject.fromObject(extStr);
		
		JSONArray jsonArr = jsonObj.getJSONArray("columns");
		
		Map columnMap = new HashMap();
		for(int i = 0; i < jsonArr.size() ; i++){
			JSONObject columnObj = jsonArr.getJSONObject(i);
			columnMap.put(columnObj.get("dataIndex"), columnObj);
		}
		
		System.out.println(columnMap);
	}
}
