package com.flying.view.xfire;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONObject;

import com.flying.logging.Log;
import com.flying.logging.LogFactory;
import com.flying.service.Engine;
import com.flying.service.EngineParameter;
import com.flying.util.FlyingUtil;
import com.flying.view.webservice.HandleResponse;

public class WsCenterImpl implements WsCenter {
	private static Log log = LogFactory.getLog(WsCenterImpl.class);
	
	@Override
	public String execute(String paramJson) {
 		HandleResponse res = new HandleResponse();
 		Object resultObj = null;
 		//参数处理
 		JSONObject paramObj = JSONObject.fromObject(paramJson);
 		//执行的命令
 		String command = paramObj.getString("command")==null?"":paramObj.getString("command");
 		//判断命令是否可用
 		if("".equals(command)){
 			log.error("没有执行的命令！");
 			resultObj = res.handler("FAIL", "没有执行的命令！");
 		}
 		
 		Map paramMap = new HashMap();
 		if(paramObj.has("paramMap")){
 	 		paramMap = FlyingUtil.changeJsonObject2HashMap(paramObj.getJSONObject("paramMap"));
 		}
 		
 		if(paramMap.containsKey("flexEncode")){
 			 Iterator keySetIter = paramMap.keySet().iterator();
 			 while(keySetIter.hasNext()){
 				 String keyName = (String) keySetIter.next();
 				 if(paramMap.get(keyName) != null && !"".equals(paramMap.get(keyName)) && paramMap.get(keyName) instanceof String){
 	 				 try {
						paramMap.put(keyName,java.net.URLDecoder.decode(paramMap.get(keyName).toString(),"UTF-8"));
					} catch (UnsupportedEncodingException e) {
						log.error("中文转码出错！", e);
					}
 				 }
 			 }
 		}
 		//准备执行
 		EngineParameter ep = new EngineParameter(command);
 		ep.setParamMap(paramMap);
 		
 		//执行
 		try {
			Engine.execute(ep);
			resultObj = res.handler("SUCCESS", ep.getResultMap());
			
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			resultObj = res.handler("FAIL", e.getMessage());
		}
 		//返回
 		return resultObj.toString();
	}

}
