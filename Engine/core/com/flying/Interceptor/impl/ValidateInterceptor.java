package com.flying.Interceptor.impl;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.flying.Interceptor.AbstractInterceptor;
import com.flying.builder.BuilderUtil;
import com.flying.exception.FlyingException;
import com.flying.service.EngineParameter;
import com.flying.util.FileUtil;

/**
 * <B>描述：</B>后台验证拦截器<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 *
 */
public class ValidateInterceptor  extends AbstractInterceptor {

	@Override
	public void before(EngineParameter ep) throws Exception {
		String command = ep.getCommand();
		
		String tableName = command.substring(0,command.indexOf(".")); 
		
		String extPage = BuilderUtil.getPagePathWithoutModule(tableName);
		
		File extFile = new File(extPage);
		
		if(!extFile.exists()){
			throw new FlyingException("ext前台文件不存在！");
		}
		
		String extStr = FileUtil.fileToString(extFile);
		
		JSONObject jsonObj = JSONObject.fromObject(extStr);
		
		JSONArray jsonArr = jsonObj.getJSONArray("columns");
		
		Map<String,JSONObject> columnMap = new HashMap();
		
		for(int i = 0; i < jsonArr.size() ; i++){
			JSONObject columnObj = jsonArr.getJSONObject(i);
			columnMap.put((String) columnObj.get("dataIndex"), columnObj);
		}
		
		check(ep.getParamMap(),columnMap);
	}

	@Override
	public void after(EngineParameter ep) throws Exception {
		
	}
	
	private void check(Map paramMap,Map<String,JSONObject> columnMap) throws FlyingException{
		Set keySet = paramMap.keySet();
		Iterator iterKey = keySet.iterator();
		while(iterKey.hasNext()){
			String key = (String) iterKey.next();
			Object value = paramMap.get(key)==null?"":paramMap.get(key);
			
			JSONObject jsonObj = columnMap.get(key);
			
			if(jsonObj == null) continue;
			
			if(jsonObj.has("allowBlank") && !jsonObj.getBoolean("allowBlank")){
				if(value == null || !"".equals(value)){
					throw new FlyingException(key +" 必须为非空！");
				}
			}
			
			if(jsonObj.has("maxLength")){
				if(jsonObj.getInt("maxLength") < String.valueOf(value).length()){
					throw new FlyingException(key +" 最大长度是"+jsonObj.getInt("maxLength"));
				}
			}
			
			if(jsonObj.has("minLength")){
				if(jsonObj.getInt("minLength") > String.valueOf(value).length()){
					throw new FlyingException(key +" 最小长度是"+jsonObj.getInt("maxLength"));
				}
			}
			
			if(jsonObj.has("regex")){
				Pattern pattern = Pattern.compile(jsonObj.getString("regex"));
				Matcher matcher = pattern.matcher(String.valueOf(value));
				if(!matcher.matches()){
					throw new FlyingException(key +" 的值不符合正则表达式！");
				}
			}
			
			if(jsonObj.has("maxValue")){
				if(jsonObj.getDouble("maxValue") < Double.parseDouble(String.valueOf(value))){
					throw new FlyingException(key +" 最大值是"+jsonObj.getDouble("maxValue"));
				}
			}
			
			if(jsonObj.has("minValue")){
				if(jsonObj.getInt("minValue") > Double.parseDouble(String.valueOf(value))){
					throw new FlyingException(key +" 最小值是"+jsonObj.getInt("minValue"));
				}
			}
			
		}
	}
}
