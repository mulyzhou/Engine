package com.flying.Interceptor.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.flying.Interceptor.AbstractInterceptor;
import com.flying.service.Engine;
import com.flying.service.EngineParameter;
import com.flying.util.FlyingUtil;
/**
 * 
 * <B>描述：</B>创建外键关系类<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 *
 */
public class RelationTableInterceptor extends AbstractInterceptor {

	@Override
	public void before(EngineParameter ep) throws Exception {

	}

	@Override
	public void after(EngineParameter ep) throws Exception {
		List<Map> resultList = (List)ep.getResult("data");
		if(resultList.size() == 0) return;
		
		//需要添加的结构
		List<Map> result2 = new ArrayList<Map>();
		
		EngineParameter selfEp = new EngineParameter("T_BASE_FOREIGNKEY.RelationTableOfwb");
		
		for(int i =0;i<resultList.size();i++){
			Map map = resultList.get(i);
			FlyingUtil.change(map, selfEp.getParamMap());
			if((Long)map.get("type") == 2){
				selfEp.setCommandType("list");
				Engine.execute(selfEp);
				
				Map obj = (Map)selfEp.getResult("data");
				if(obj.size()>0) result2.addAll((List<Map>)obj);
			}
		}
		
		//构建最后的数据结构
		resultList.addAll(result2);
	}

}
