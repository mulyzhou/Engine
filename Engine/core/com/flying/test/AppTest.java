package com.flying.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.Oid;

import com.flying.dao.MongoDBDAO;
import com.flying.dao.mongoDB.MongoDbBuilder;
import com.flying.init.EngineInit;
import com.flying.init.Item;
import com.flying.service.Engine;
import com.flying.service.EngineParameter;
import com.flying.util.GenerateUtil;
import com.mongodb.DB;
import com.mongodb.MongoClient;

public class AppTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EngineInit.appStart();
		
		MongoDbBuilder.builder();
		
		EngineParameter ep = new EngineParameter("T_BASE_FIELD.insert");
		ep.putParam("id", 2);
		ep.putParam("sex", false);
		ep.putParam("name", "zheng");
		ep.putParam("age", 123);
		ep.putParam("address", "wuhan1");
		ep.putParam("huji", "man2");
		ep.putParam("_id", "566a67ea5c5e7e41c31e81ad");
		//ep.putParam("start", 1);
		//ep.putParam("limit", 2);
		try {
			Engine.execute(ep);
			
			System.out.println(ep.getResultMap());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	public static void generateTest(){
		Map map = new HashMap();
		map.put("BMC", "T_SYS_ROLE");
		map.put("BZS", "角色信息");
		List<Map> listMap = new ArrayList<Map>();
		listMap.add(map);
		
		Item item = new Item();
		item.setAlias("角色信息");
		item.setName("T_SYS_ROLE");
		
		try {
			EngineParameter ep = new EngineParameter("T_BASE_TABLE.selectAll");
			Engine.execute(ep);
			
			ep = new EngineParameter("T_BASE_LOG.selectAll");
			Engine.execute(ep);
			//GenerateUtil.generateExtPage(item, "D:\\test\\");
			//GenerateUtil.generateTablename(listMap, "D:\\test\\");
			//GenerateUtil.generateModuleTablename("D:\\test\\");
			//GenerateUtil.generateTablename(listMap, "D:\\test\\");
			//GenerateUtil.generateModuleJunitCode(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

