package com.flying.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import junit.framework.Assert;

import net.sf.json.JSONObject;

import org.junit.Test;

import com.flying.dao.mongoDB.MongoDbBuilder;
import com.flying.init.EngineInit;
import com.flying.logging.Log;
import com.flying.logging.LogFactory;
import com.flying.service.Engine;
import com.flying.service.EngineParameter;

public class MongoDBTest {
	private static Log log = LogFactory.getLog(MongoDBTest.class);

	static{
		if(Engine.ac == null){
			log.info("java应用系统初始化！");
			//系统初始化
			EngineInit.appStart();
			//解决配置文件
			MongoDbBuilder.parse();
		}else{
			log.debug("spring 环境已经初始化完毕！");
		}
	}
	@Test
    public void insertArray() throws Exception{
		//"field":["uuid","name","age","sex","abc","address","huji"]
		String uuid = UUID.randomUUID().toString();
		Map testMap = new HashMap();
		testMap.put("uuid", uuid);
		testMap.put("sex", false);
		testMap.put("name", "zheng");
		testMap.put("age", 123);
		testMap.put("address", JSONObject.fromObject(("{'phone':[123456,78090]}")));
		testMap.put("huji", "man2");
		
		EngineParameter ep = new EngineParameter("MONGODB_TEST.insertArray");
		ep.setCommandType("insert");
		ep.setParamMap(testMap);
		
		Engine.execute(ep);
		
		ep = new EngineParameter("MONGODB_TEST.selectByUuid");
		ep.setCommandType("object");
		ep.putParam("uuid", uuid);
		Engine.execute(ep);
		
		Map selectMap = (Map) ep.getResult("data");
		String _id = selectMap.get("_id").toString();
		Assert.assertTrue(_id.length() > 0);
		Assert.assertEquals(testMap.get("sex"), selectMap.get("sex"));
		Assert.assertEquals(testMap.get("name"), selectMap.get("name"));
		Assert.assertEquals(testMap.get("sex"), selectMap.get("sex"));
		Assert.assertEquals(testMap.get("age"), selectMap.get("age"));
		Assert.assertEquals(testMap.get("address"), JSONObject.fromObject(selectMap.get("address").toString()));
		
		ep = new EngineParameter("MONGODB_TEST.deleteById");
		ep.setCommandType("delete");
		ep.putParam("_id", selectMap.get("_id"));
		
		Engine.execute(ep);
		
		ep = new EngineParameter("MONGODB_TEST.selectById");
		ep.setCommandType("object");
		ep.putParam("_id", _id);
		Engine.execute(ep);
		
		selectMap = (Map) ep.getResult("data");
		
		Assert.assertTrue(selectMap.size()==0);
		
    }
	@Test
    public void insertJson() throws Exception{
		//"field":{"uuid":"#","name":"#","age":"#","sex":"#","address":"#"}
		String uuid = UUID.randomUUID().toString();
		Map testMap = new HashMap();
		
		testMap.put("uuid", uuid);
		testMap.put("sex", true);
		testMap.put("name", "zheng1");
		testMap.put("age", 12);
		testMap.put("address", "{'jiedao':'jianghanqu','code':420000}");
		testMap.put("huji", "man3");
		
		EngineParameter ep = new EngineParameter("MONGODB_TEST.insertArray");
		ep.setCommandType("insert");
		ep.setParamMap(testMap);
		
		Engine.execute(ep);
		
		ep = new EngineParameter("MONGODB_TEST.selectByUuid");
		ep.setCommandType("object");
		ep.putParam("uuid", uuid);
		Engine.execute(ep);
		
		Map selectMap = (Map) ep.getResult("data");
		
		String _id = selectMap.get("_id").toString();
		Assert.assertTrue(_id.length() > 0);
		Assert.assertEquals(testMap.get("sex"), selectMap.get("sex"));
		Assert.assertEquals(testMap.get("name"), selectMap.get("name"));
		Assert.assertEquals(testMap.get("sex"), selectMap.get("sex"));
		Assert.assertEquals(testMap.get("age"), selectMap.get("age"));
		Assert.assertEquals(testMap.get("address"), selectMap.get("address"));
		
		ep = new EngineParameter("MONGODB_TEST.deleteById");
		ep.setCommandType("delete");
		ep.putParam("_id", selectMap.get("_id"));
		
		Engine.execute(ep);
		
		ep = new EngineParameter("MONGODB_TEST.selectByUuid");
		ep.setCommandType("object");
		ep.putParam("uuid", uuid);
		Engine.execute(ep);
		
		selectMap = (Map) ep.getResult("data");
		
		Assert.assertTrue(selectMap.size()==0);
    }
	@Test
    public void updateById() throws Exception{
		String uuid = UUID.randomUUID().toString();
		Map testMap = new HashMap();
		testMap.put("uuid", uuid);
		testMap.put("sex", false);
		testMap.put("name", "zheng1");
		testMap.put("age", 44);
		testMap.put("address", "{'jiedao':'jianghanqu1','code':421000}");
		testMap.put("huji", "man2");
		
		EngineParameter ep = new EngineParameter("MONGODB_TEST.insertArray");
		ep.setCommandType("insert");
		ep.setParamMap(testMap);
		
		Engine.execute(ep);
		
		ep = new EngineParameter("MONGODB_TEST.selectByUuid");
		ep.setCommandType("object");
		ep.putParam("uuid", uuid);
		Engine.execute(ep);
		
		Map selectMap = (Map) ep.getResult("data");
		
		String _id = selectMap.get("_id").toString();
		
		ep = new EngineParameter("MONGODB_TEST.updateById");
		ep.setCommandType("update");
		ep.putParam("_id", _id);
		ep.putParam("sex", true);
		ep.putParam("name", "zheng");
		ep.putParam("age", 123);
		ep.putParam("address", JSONObject.fromObject(("{'phone':[123456,78090]}")));
		ep.putParam("huji", "man2");
		Engine.execute(ep);
		
		ep = new EngineParameter("MONGODB_TEST.selectById");
		ep.setCommandType("object");
		ep.putParam("_id", _id);
		Engine.execute(ep);
		
		selectMap = (Map) ep.getResult("data");
		Assert.assertEquals(selectMap.get("sex"),false);
		Assert.assertEquals(selectMap.get("name"),"zheng");
		Assert.assertEquals(selectMap.get("age"),123);
		Assert.assertEquals(selectMap.get("address"),testMap.get("address"));
		
		ep = new EngineParameter("MONGODB_TEST.deleteById");
		ep.setCommandType("delete");
		ep.putParam("_id", _id);
		
		Engine.execute(ep);
		
    }
	
	@Test
    public void updateByName() throws Exception{
		String uuid = UUID.randomUUID().toString();
		Map testMap = new HashMap();
		testMap.put("uuid", uuid);
		testMap.put("sex", false);
		testMap.put("name", "zheng1");
		testMap.put("age", 44);
		testMap.put("address", "{'jiedao':'jianghanqu1','code':421000}");
		testMap.put("huji", "man2");
		
		EngineParameter ep = new EngineParameter("MONGODB_TEST.insertArray");
		ep.setCommandType("insert");
		ep.setParamMap(testMap);
		
		Engine.execute(ep);
		
		ep = new EngineParameter("MONGODB_TEST.selectByUuid");
		ep.setCommandType("object");
		ep.putParam("uuid", uuid);
		Engine.execute(ep);
		
		Map selectMap = (Map) ep.getResult("data");
		
		String _id = selectMap.get("_id").toString();
		
		ep = new EngineParameter("MONGODB_TEST.updateByName");
		ep.setCommandType("update");
		ep.putParam("sex", true);
		ep.putParam("name", "zheng1");
		ep.putParam("age", 123);
		ep.putParam("address", JSONObject.fromObject(("{'phone':[123456,78090]}")));
		ep.putParam("huji", "man2");
		Engine.execute(ep);
		
		ep = new EngineParameter("MONGODB_TEST.selectById");
		ep.setCommandType("object");
		ep.putParam("_id", _id);
		Engine.execute(ep);
		
		selectMap = (Map) ep.getResult("data");
		Assert.assertNull(selectMap.get("sex"));
		Assert.assertEquals(selectMap.get("name"),"zheng1");
		Assert.assertEquals(selectMap.get("age"),123);
		Assert.assertNull(selectMap.get("huji"));
		
		ep = new EngineParameter("MONGODB_TEST.deleteById");
		ep.setCommandType("delete");
		ep.putParam("_id", _id);
		
		Engine.execute(ep);
    }
	
	@Test
    public void deleteById() throws Exception{
		String uuid = UUID.randomUUID().toString();
		Map testMap = new HashMap();
		testMap.put("uuid", uuid);
		testMap.put("sex", false);
		testMap.put("name", "zheng1");
		testMap.put("age", 44);
		testMap.put("address", "{'jiedao':'jianghanqu1','code':421000}");
		testMap.put("huji", "man2");
		
		EngineParameter ep = new EngineParameter("MONGODB_TEST.insertArray");
		ep.setCommandType("insert");
		ep.setParamMap(testMap);
		
		Engine.execute(ep);
		
		ep = new EngineParameter("MONGODB_TEST.selectByUuid");
		ep.setCommandType("object");
		ep.putParam("uuid", uuid);
		Engine.execute(ep);
		
		Map selectMap = (Map) ep.getResult("data");
		
		String _id = selectMap.get("_id").toString();
		
		ep = new EngineParameter("MONGODB_TEST.deleteById");
		ep.setCommandType("delete");
		ep.putParam("_id", _id);
		
		Engine.execute(ep);
		
		ep = new EngineParameter("MONGODB_TEST.selectByUuid");
		ep.setCommandType("object");
		ep.putParam("uuid", uuid);
		Engine.execute(ep);
		
		selectMap = (Map) ep.getResult("data");
		
		Assert.assertTrue(selectMap.size()==0);
    }
	
	@Test
    public void deleteByIName() throws Exception{
		String uuid = UUID.randomUUID().toString();
		Map testMap = new HashMap();
		testMap.put("uuid", uuid);
		testMap.put("sex", false);
		testMap.put("name", "zheng1");
		testMap.put("age", 44);
		testMap.put("address", "{'jiedao':'jianghanqu1','code':421000}");
		testMap.put("huji", "man2");
		
		EngineParameter ep = new EngineParameter("MONGODB_TEST.insertArray");
		ep.setCommandType("insert");
		ep.setParamMap(testMap);
		
		Engine.execute(ep);

		ep = new EngineParameter("MONGODB_TEST.deleteByName");
		ep.setCommandType("delete");
		ep.putParam("age", 44);
		
		Engine.execute(ep);
		
		ep = new EngineParameter("MONGODB_TEST.selectByUuid");
		ep.setCommandType("object");
		ep.putParam("uuid", uuid);
		Engine.execute(ep);
		
		Map selectMap = (Map) ep.getResult("data");
		
		Assert.assertTrue(selectMap.size()==0);
    }
	
    @Test
	public void selectAll() throws Exception{
    	for(int i =0;i<10;i++){
    		Map testMap = new HashMap();
    		testMap.put("sex", false);
    		testMap.put("name", "zheng" + i);
    		testMap.put("age", 55);
    		testMap.put("address", "{'jiedao':'jianghanqu1','code':421000}");
    		testMap.put("huji", "man" + (10-i));
    		
    		EngineParameter ep = new EngineParameter("MONGODB_TEST.insertArray");
    		ep.setCommandType("insert");
    		ep.setParamMap(testMap);
    		
    		Engine.execute(ep);
    	}
    	
    	EngineParameter ep = new EngineParameter("MONGODB_TEST.selectAll");
		ep.setCommandType("map");
		
		Engine.execute(ep);
		
		int total = Integer.parseInt(ep.getResult("total").toString());
		List<Map> listResult = (List<Map>) ep.getResult("data");
		
		Assert.assertEquals(total, 10);
		Assert.assertEquals(listResult.get(2).get("name"),"zheng2");
		
		ep = new EngineParameter("MONGODB_TEST.selectAll");
		ep.setCommandType("map");
		ep.putParam("sort", "ORDER BY name DESC");
		ep.putParam("limit", 3);
		ep.putParam("start", 3);
		Engine.execute(ep);
		
		total = Integer.parseInt(ep.getResult("total").toString());
		listResult = (List<Map>) ep.getResult("data");
		
		Assert.assertEquals(total, 10);
		Assert.assertEquals(listResult.size(), 3);
		Assert.assertEquals(listResult.get(0).get("name"),"zheng6");
		
		ep = new EngineParameter("MONGODB_TEST.selectAllByName");
		ep.setCommandType("list");
		ep.putParam("name", "zheng2");
		Engine.execute(ep);
		
		listResult = (List<Map>) ep.getResult("data");
		
		Assert.assertEquals(1, listResult.size());
		Assert.assertFalse((Boolean)listResult.get(0).get("sex"));
		Assert.assertEquals(listResult.get(0).get("name"),"zheng2");
		Assert.assertEquals(listResult.get(0).get("age"),55);
		
		
		ep = new EngineParameter("MONGODB_TEST.selectAllByNameDistinct");
		ep.setCommandType("list");
		
		Engine.execute(ep);
		
		listResult = (List<Map>) ep.getResult("data");
		
		Assert.assertEquals(1, listResult.size());
		Assert.assertEquals(listResult.get(0),55);
		
		
		
		ep = new EngineParameter("MONGODB_TEST.deleteByName");
		ep.setCommandType("delete");
		ep.putParam("age", 55);
		
		Engine.execute(ep);
	}
}
