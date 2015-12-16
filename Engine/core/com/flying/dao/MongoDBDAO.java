package com.flying.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.jongo.Distinct;
import org.jongo.Find;
import org.jongo.FindOne;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.Oid;
import org.jongo.Update;

import com.flying.dao.mongoDB.MongoDbBuilder;
import com.flying.logging.Log;
import com.flying.logging.LogFactory;
import com.flying.service.EngineParameter;
import com.flying.util.FlyingUtil;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;
import com.mongodb.client.MongoDatabase;

public class MongoDBDAO implements IDAO{
	private static Log log = LogFactory.getLog(MongoDBDAO.class);//日志
    /**
     * MongoClient的实例代表数据库连接池，是线程安全的，可以被多线程共享，客户端在多线程条件下仅维持一个实例即可 
     * Mongo是非线程安全的，目前mongodb API中已经建议用MongoClient替代Mongo 
     */  
	public static MongoClient mongoClient = null;
	
	public static String DB_NAME = null;
	
	private static Hashtable SEARCH_CATHE = new Hashtable();
	
	private static String regeJsonArray = "^\\[([\"|']{0,1}[\\w]+[\"|']{0,1},{0,1})+\\]$";
	
	private static String regeJsonObject = "^\\{([\"|']{0,1}[\\w]+[\"|']{0,1}:[\"|']{0,1}([\\w]+|#)[\"|']{0,1},{0,1})+\\}$";  

	@Override
	public Object insert(EngineParameter ep) throws Exception {
		//结果集
		WriteResult wr = null;
		//处理参数
		String command = ep.getCommand();
		Object moObj = MongoDbBuilder.BO.get(command);
		
		//jongo处理过程
		DB db = mongoClient.getDB(DB_NAME);
		Jongo jongo = new Jongo(db);
		MongoCollection moColl =jongo.getCollection(command.substring(0,command.indexOf(".")));
		if(moColl != null){
			if(MongoDbBuilder.BO.get(command+".field") == null){
				wr = moColl.insert(filterParam(ep.getParamMap()));
			}else if(Pattern.matches(regeJsonArray,MongoDbBuilder.BO.get(command+".field").toString())){
				JSONArray fieldObjArr = JSONArray.fromObject(MongoDbBuilder.BO.get(command+".field").toString());
				Map fieldMap = new HashMap();
				for(int i =0;i<fieldObjArr.size();i++){
					String fieldKey = fieldObjArr.get(i).toString();
					if(ep.getParam(fieldKey) != null){
						fieldMap.put(fieldKey, ep.getParam(fieldKey));
					}
				}
				wr = moColl.insert(fieldMap);
			}else if(Pattern.matches(regeJsonObject,MongoDbBuilder.BO.get(command+".field").toString())){
				String insertStr = MongoDbBuilder.BO.get(command+".field").toString();
				if(insertStr.indexOf("#") > 0){
					wr = moColl.insert(replaceParam(insertStr,ep.getParamMap()));
				}else{
					wr = moColl.insert(insertStr);
				}
			}else{
				log.warn("insert操作：未知的field参数！");
			}
		}
		return wr==null?0:wr.getN();
	}

	@Override
	public int insertAll(List<EngineParameter> epList) throws Exception {
		return 0;
	}

	@Override
	public void update(EngineParameter ep) throws Exception {
		//处理参数
		String command = ep.getCommand();
		Object moObj = MongoDbBuilder.BO.get(command);
		
		//jongo处理过程
		DB db = mongoClient.getDB(DB_NAME);
		Jongo jongo = new Jongo(db);
		MongoCollection moColl =jongo.getCollection(command.substring(0,command.indexOf(".")));
		
		if(moColl != null){
			Update moUpdate = null;
			if(MongoDbBuilder.BO.get(command+".condition") != null){
				String conditionStr = (String)MongoDbBuilder.BO.get(command+".condition");
				if("_id".equals(conditionStr) && ep.getParam("_id") != null){
					moUpdate = moColl.update(new ObjectId(ep.getParam("_id").toString()));
				}else if(conditionStr.indexOf("#") > 0){
					moUpdate = moColl.update(replaceParam(conditionStr,ep.getParamMap()));
				}else{
					moUpdate = moColl.update(conditionStr);
				}
			}else{
				log.warn("update操作：更新没有传入条件！");
			}
			
			if(MongoDbBuilder.BO.get(command+".field") != null){
				//获取更新值
				String fieldStr = MongoDbBuilder.BO.get(command+".field").toString();
				if(fieldStr.indexOf("#") > 0){
					moUpdate.multi().with(replaceParam(fieldStr,ep.getParamMap()));
				}else{
					moUpdate.multi().with(fieldStr);
				}
			}
		}
	}

	@Override
	public void updateAll(List<EngineParameter> epList) throws Exception {
		
	}

	@Override
	public void delete(EngineParameter ep) throws Exception {
		//处理参数
		String command = ep.getCommand();
		Object moObj = MongoDbBuilder.BO.get(command);
		
		//jongo处理过程
		DB db = mongoClient.getDB(DB_NAME);
		Jongo jongo = new Jongo(db);
		MongoCollection moColl =jongo.getCollection(command.substring(0,command.indexOf(".")));
		
		if(moColl != null){
			String conditionStr = null;
			if(MongoDbBuilder.BO.get(command+".condition") != null){
				conditionStr = (String)MongoDbBuilder.BO.get(command+".condition");
				if("_id".equals(conditionStr) && ep.getParam("_id") != null){
					moColl.remove(new ObjectId(ep.getParam("_id").toString()));
				}else if(conditionStr.indexOf("#") > 0){
					moColl.remove(replaceParam(conditionStr,ep.getParamMap()));
				}else{
					moColl.remove(conditionStr);
				}
			}else{
				moColl.remove();
			}
		}
	}

	@Override
	public void deleteAll(List<EngineParameter> epList) throws Exception {
		
	}

	@Override
	public Object selectOne(EngineParameter ep) throws Exception {
		//结果集
		Map resultMap = new HashMap();
		//处理参数
		String command = ep.getCommand();
		Object moObj = MongoDbBuilder.BO.get(command);
		
		//jongo处理过程
		DB db = mongoClient.getDB(DB_NAME);
		Jongo jongo = new Jongo(db);
		MongoCollection moColl =jongo.getCollection(command.substring(0,command.indexOf(".")));
		
		if(moColl != null){
			//求数量
			if(MongoDbBuilder.BO.get(command+".count") != null){
				if("all".equals(MongoDbBuilder.BO.get(command+".count").toString())){
					return moColl.count();
				}else{
					return moColl.count(MongoDbBuilder.BO.get(command+".count").toString());
				}
			}
			
			//条件过滤
			FindOne moFind = null;
			if(MongoDbBuilder.BO.get(command+".condition") != null){
				String conditionStr = (String)MongoDbBuilder.BO.get(command+".condition");
				if("_id".equals(conditionStr) && ep.getParam("_id") != null){
					moFind = moColl.findOne(Oid.withOid(ep.getParam("_id").toString()));
				}else if(conditionStr.indexOf("#") > 0){
					moFind = moColl.findOne(replaceParam(conditionStr,ep.getParamMap()));
				}else{
					moFind = moColl.findOne(conditionStr);
				}
			}else{
				moFind = moColl.findOne();
			}
			
			//输出字段
			if(MongoDbBuilder.BO.get(command+".field") != null){
				if(SEARCH_CATHE.get(command) == null){
					SEARCH_CATHE.put(command, selectField(MongoDbBuilder.BO.get(command+".field").toString()));
				}
				moFind = moFind.projection((String)SEARCH_CATHE.get(command));
			}
			
			resultMap = moFind.as(Map.class);
		}
		
		return resultMap;
	}

	@Override
	public List<Object> selectList(EngineParameter ep) throws Exception {
		//结果集
		List<Object> resultList = new ArrayList<Object>();
		//处理参数
		String command = ep.getCommand();
		Object moObj = MongoDbBuilder.BO.get(command);
		
		//jongo处理过程
		DB db = mongoClient.getDB(DB_NAME);
		Jongo jongo = new Jongo(db);
		MongoCollection moColl =jongo.getCollection(command.substring(0,command.indexOf(".")));
		
		if(moColl != null){
			//按distinct分组
			Distinct moDistinct = null;
			if(MongoDbBuilder.BO.get(command+".distinct") != null){
				moDistinct = moColl.distinct(MongoDbBuilder.BO.get(command+".distinct").toString());
				if(MongoDbBuilder.BO.get(command+".condition") != null){
					String conditionStr = (String)MongoDbBuilder.BO.get(command+".condition");
					if(conditionStr.indexOf("#") > 0){
						conditionStr = replaceParam(conditionStr,ep.getParamMap());
					}
					resultList = moDistinct.query(conditionStr).as(Object.class);
				}else{
					resultList = moDistinct.as(Object.class);
				}
				
				return resultList;
			}
			//条件过滤
			Find moFind = null;
			if(MongoDbBuilder.BO.get(command+".condition") != null){
				String conditionStr = (String)MongoDbBuilder.BO.get(command+".condition");
				if("_id".equals(conditionStr) && ep.getParam("_id") != null){
					moFind = moColl.find(Oid.withOid(ep.getParam("_id").toString()));
				}else if(conditionStr.indexOf("#") > 0){
					moFind = moColl.find(replaceParam(conditionStr,ep.getParamMap()));
				}else{
					moFind = moColl.find(conditionStr);
				}
			}else{
				moFind = moColl.find();
			}

			//数据排序
			if(MongoDbBuilder.BO.get(command+".sort") != null){
				moFind = moFind.sort(MongoDbBuilder.BO.get(command+".sort").toString());
			}else{
				if(ep.getParam("sort") != null){
					String[] sortArr = ep.getParam("sort").toString().split(" ");
					if(sortArr.length == 3){
						moFind = moFind.sort("{\""+ sortArr[2]+"\":1}");
					}else if(sortArr.length == 4){
						moFind = moFind.sort("{\""+ sortArr[2]+ "\":"+ ("ASC".equals(sortArr[3])?1:0) +"}");
					}else{
						log.warn(ep.getParam("sort").toString()+":排序参数不合法！");
					}
				}
			}
			//跳过skip条数据
			if(MongoDbBuilder.BO.get(command+".skip") != null){
				moFind = moFind.skip(Integer.parseInt(MongoDbBuilder.BO.get(command+".skip").toString()));
			}else if(ep.getParam("start") != null){
				moFind = moFind.skip(Integer.parseInt(ep.getParam("start").toString()));
			}
			//limit条数据
			if(MongoDbBuilder.BO.get(command+".limit") != null){
				moFind = moFind.limit(Integer.parseInt(MongoDbBuilder.BO.get(command+".limit").toString()));
			}else if(ep.getParam("limit") != null){
				moFind = moFind.limit(Integer.parseInt(ep.getParam("limit").toString()));
			}
			
			//按照指定索引方式查询
			if(MongoDbBuilder.BO.get(command+".hint") != null){
				moFind = moFind.hint(MongoDbBuilder.BO.get(command+".hint").toString());
			}
			
			//输出字段
			if(MongoDbBuilder.BO.get(command+".field") != null){
				String fieldStr = MongoDbBuilder.BO.get(command+".field").toString();
				if(Pattern.matches(regeJsonArray,fieldStr)){
					JSONArray fieldObjArr = JSONArray.fromObject(fieldStr);
					JSONObject fieldJsonObj = new JSONObject();
					for(int i =0;i<fieldObjArr.size();i++){
						fieldJsonObj.put(fieldObjArr.get(i).toString(), 1);
					}
					fieldStr = fieldJsonObj.toString();
				}else if(fieldStr.indexOf("#") > 0){
					fieldStr = replaceParam(fieldStr,ep.getParamMap());
				}
				moFind = moFind.projection(fieldStr);
			}
			
			Iterator<Map> iter = moFind.as(Map.class);
			while(iter.hasNext()){
				resultList.add(iter.next());
			}
		}
		
		return resultList;
	}

	@Override
	public Map<String, Object> selectByPaging(EngineParameter ep)throws Exception {
		//结果集
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//处理参数
		String command = ep.getCommand();
		Object moObj = MongoDbBuilder.BO.get(command);
		
		//jongo处理过程
		DB db = mongoClient.getDB(DB_NAME);
		Jongo jongo = new Jongo(db);
		MongoCollection moColl =jongo.getCollection(command.substring(0,command.indexOf(".")));
		
		if(moColl != null){
			if(MongoDbBuilder.BO.get(command+".condition") != null){
				String conditionStr = MongoDbBuilder.BO.get(command+".condition").toString();
				if(conditionStr.indexOf("#") > 0){
					conditionStr = replaceParam(conditionStr,ep.getParamMap());
				}
				resultMap.put("total", moColl.count(conditionStr));
			}else{
				resultMap.put("total", moColl.count());
			}
			resultMap.put("data", selectList(ep));
		}
		return resultMap;
	}

	@Override
	public String getCommandSql(EngineParameter ep) throws Exception {
		return null;
	}
	/**
	 * 查询时，输入对象列表
	 * @param fieldObj
	 * @return
	 */
	private String selectField(String fieldStr){
		String handlerSelectField = null;
		if(Pattern.matches(regeJsonArray,fieldStr)){
			JSONArray fieldObjArr = JSONArray.fromObject(fieldStr);
			JSONObject fieldJsonObj = new JSONObject();
			for(int i =0;i<fieldObjArr.size();i++){
				fieldJsonObj.put(fieldObjArr.get(i).toString(), 1);
			}
			handlerSelectField = fieldJsonObj.toString();
		}else{
			handlerSelectField = fieldStr;
		}
		return handlerSelectField;
	}
	/**
	 * 插入/修改时，过滤系统参数
	 * @param oldMap
	 * @param newMap
	 */
	private Map filterParam(Map<String ,Object> oldMap){
		//系统参数区
		String[] systemName = new String[]{"session","filter"};
		Map<String,Integer> systemNameMap = new HashMap<String,Integer>();
		for(int i =0;i<systemName.length;i++){
			systemNameMap.put(systemName[i], 1);
		}
		//过滤系统参数
		Map<String,Object> newMap = null;
		if(oldMap != null){
			newMap = new HashMap();
			
			Set<String> oldSet = oldMap.keySet();
			Iterator<String> oldIter = oldSet.iterator();
			while(oldIter.hasNext()){
				String name = oldIter.next();
				if(systemNameMap.get(name) != null){
					continue;
				}else{
					newMap.put(name, oldMap.get(name));
				}
			}
		}

		return newMap;
	}
	/**
	 * 参数处理，将#变成数值
	 * @param conditionStr
	 * @param paramMap
	 * @return
	 */
	private String replaceParam(String conditionStr,Map paramMap){
		JSONObject jsonCondition = JSONObject.fromObject(conditionStr);
		Iterator jsonIter = jsonCondition.keys();
		while(jsonIter.hasNext()){
			String keyName = (String)jsonIter.next();
			if(jsonCondition.get(keyName) instanceof String){
				if("#".equals(jsonCondition.get(keyName))){
					jsonCondition.put(keyName, paramMap.get(keyName));
				}
			}else if(jsonCondition.get(keyName) instanceof JSONObject){
				jsonCondition.put(keyName,replaceParam(((JSONObject)jsonCondition.get(keyName)).toString(),paramMap));
			}else{
				log.warn(keyName + "参数替换时，无效参数！");
			}
		}
		
		return jsonCondition.toString();
	}
}
