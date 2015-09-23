package com.flying.view.webservice;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.flying.logging.Log;
import com.flying.logging.LogFactory;

public class JsonResponseType implements IResponseType {
	private static Log log = LogFactory.getLog(JsonResponseType.class);
	
	@Override
	public Object handler(String type, Object obj) {
		JSONObject resultObj = (JSONObject) handler(obj);
		
		//判断状态
		if(type == null){
			log.warn("未知的处理状态！");
		}else if(obj instanceof List){
			log.warn("List对象无法转换成JSONObject对象，所以无是否成功状态属性。");
			return handlerList((List)obj);
		}else if(type.toUpperCase().equals("SUCCESS")){
			resultObj.put("success", true);
		}else if(type.toUpperCase().equals("FAIL")){
			resultObj.put("success", false);
		}else{
			log.warn(type+ " 是未知的处理状态！");
		}
		//输出
		return resultObj;
	}

	@Override
	public Object handler(Object obj) {
		Object resultObj = null;
		//判断类型
		if(obj instanceof Map){
			resultObj = handlerMap((Map)obj);
		}else if(obj instanceof List){
			resultObj = handlerList((List)obj);
		}else if(obj instanceof String){
			resultObj = new JSONObject();
			((JSONObject) resultObj).put("msg", obj.toString());
		}else{
			resultObj = JSONObject.fromObject(obj);
		}
		
		return resultObj;
	}

	@Override
	public Object handlerMap(Map map) {
		return JSONObject.fromObject(map);
	}

	@Override
	public Object handlerList(List list) {
		JSONArray jsonArray = JSONArray.fromObject(list);
		return jsonArray;
	}
}
