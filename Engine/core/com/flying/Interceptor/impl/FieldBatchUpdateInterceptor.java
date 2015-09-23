package com.flying.Interceptor.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.flying.Interceptor.AbstractInterceptor;
import com.flying.service.Engine;
import com.flying.service.EngineParameter;
import com.flying.util.FlyingUtil;
/**
 * <B>描述：</B>部门处理拦截类<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 *
 */
public class FieldBatchUpdateInterceptor extends AbstractInterceptor {
	@Override
	public void before(EngineParameter ep) throws Exception{
		String fieldArray = ep.getParam("fieldArray")==null?"":ep.getParam("fieldArray").toString();
		
		JSONArray fieldJsonArray = JSONArray.fromObject(fieldArray);
		
		for(int i = 0; i < fieldJsonArray.size();i++){
			Map map = FlyingUtil.changeJsonObject2HashMap(fieldJsonArray.getJSONObject(i));
			if("0".equals(map.get("ZDID").toString())){
				EngineParameter selfEp = new EngineParameter("T_BASE_FIELD.insert");
				selfEp.setParamMap(map);
				Engine.execute(selfEp);
			}else{
				EngineParameter selfEp = new EngineParameter("T_BASE_FIELD.update");
				selfEp.setParamMap(map);
				Engine.execute(selfEp);
			}
		}
	}
	@Override
	public void after(EngineParameter ep){

	}
}
