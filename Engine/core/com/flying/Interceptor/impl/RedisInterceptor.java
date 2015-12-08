package com.flying.Interceptor.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.flying.Interceptor.AbstractInterceptor;
import com.flying.init.StaticVariable;
import com.flying.service.EngineParameter;
import com.flying.util.FlyingUtil;

public class RedisInterceptor extends AbstractInterceptor {
	
	@Override
	public void before(EngineParameter ep) throws Exception {
		if(StaticVariable.REDIS){
			Jedis jedis = StaticVariable.JEDIS_POOL.getResource();
			if(jedis == null){
				log.warn("【redis无法获取资源】redis系统拦截器before方法执行结束。");
			}else{
				//构建redis的KEY
				String commandType = ep.getCommandType();
		    	String redisKey = ep.getCommand() + ":" + ep.getCommandType();
		    	if(ep.getParamMap().size() > 0){		    		
		    		redisKey += ":" + FlyingUtil.changeMap2JsonString(ep.getParamMap());
		    	}
		    	
		    	if("map".equals(commandType) || "list".equals(commandType) || "object".equals(commandType)){
		        	if(jedis.exists(redisKey)){
		        		changeRedisResult2Ep(jedis.get(redisKey),ep);
		    			log.info("从redis缓存中，获取["+ redisKey +"]数据。");
		    			ep.setBreak(true);
		    		}
		    	}else if("insert".equals(commandType) || "update".equals(commandType) || "delete".equals(commandType)){
		    		String tableName = ep.getCommand().substring(0,ep.getCommand().indexOf("."));
		    		Set keySet = jedis.keys(tableName+"*");
		    		Iterator keyIter = keySet.iterator();
		    		while(keyIter.hasNext()){
		    			String keyName = (String) keyIter.next();
		    			if(jedis.ttl(keyName) == -1){
			    			jedis.del(keyName);
		    			}
		    			log.info("执行了"+ ep.getCommand() +"操作，删除["+ keyName +"]缓存！");
		    		}
		    	}
		    	
		    	StaticVariable.JEDIS_POOL.returnResource(jedis);
		    	log.info("【redis成功执行，连接返回资源池】redis系统拦截器before方法执行结束。");
			}
		}else{
			log.info("【redis未开启】");
		}
	}

	@Override
	public void after(EngineParameter ep) throws Exception {
		if(StaticVariable.REDIS){
			Jedis jedis = StaticVariable.JEDIS_POOL.getResource();
			
			if(jedis == null){
				log.warn("【redis无法获取资源】redis系统拦截器after方法执行结束。");
			}else{
				//构建redis的KEY
				String commandType = ep.getCommandType();
		    	String redisKey = ep.getCommand() + ":" + ep.getCommandType();
		    	if(ep.getParamMap().size() > 0){
		    		redisKey += ":" + FlyingUtil.changeMap2JsonString(ep.getParamMap());
		    	}
		    	if("map".equals(commandType) || "list".equals(commandType) || "object".equals(commandType)){
		    		jedis.set(redisKey, changeEp2RedisResult(ep));
		    		log.info("缓存["+ redisKey +"]:成功！");
		    		//设置有效时间
		    		if(ep.getRedisExpire() >= 0){
		    			jedis.expire(redisKey, ep.getRedisExpire());
		    			log.info("["+ redisKey +"]:存储时间：" + ep.getRedisExpire() + "s");
		    		}else{
		    			log.info("["+ redisKey +"]:无过期时间");
		    		}
		    	}
		    	
		    	StaticVariable.JEDIS_POOL.returnResource(jedis);
		    	log.info("【redis成功执行，连接返回资源池】redis系统拦截器after方法执行结束。");
			}
		}else{
			log.info("【redis未开启】");
		}
	}
	
	private String changeEp2RedisResult(EngineParameter ep){
		Map map = new HashMap();
		map.put("resultMap", ep.getResultMap());
		if(ep.getFileDownloadName() !=null && !"".equals(ep.getFileDownloadName())){
			map.put("fileDownloadName", ep.getFileDownloadName());
		}
		if(ep.getRedirectPageName() !=null && !"".equals(ep.getRedirectPageName())){
			map.put("redirectPageName", ep.getRedirectPageName());
		}
		
		return JSONObject.fromObject(map).toString();
	}
	
	public void changeRedisResult2Ep(String redisResult,EngineParameter ep){
		JSONObject jsonObj = JSONObject.fromObject(redisResult);
		//结果集
		ep.setResultMap(FlyingUtil.changeJsonObject2HashMap(jsonObj.getJSONObject("resultMap")));
		//是否下载
		if(jsonObj.get("fileDownloadName") !=null && !"".equals(jsonObj.getString("fileDownloadName"))){
			ep.setFileDownloadName(jsonObj.getString("fileDownloadName"));
		}
		//是否跳转
		if(jsonObj.get("redirectPageName") !=null && !"".equals(jsonObj.getString("redirectPageName"))){
			ep.setRedirectPageName(jsonObj.getString("redirectPageName"));
		}
	}
}
