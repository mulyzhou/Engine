package com.flying.Interceptor.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.flying.Interceptor.AbstractInterceptor;
import com.flying.exception.FlyingException;
import com.flying.service.Engine;
import com.flying.service.EngineParameter;
/**
 * 
 * <B>描述：</B>获取一个报表信息<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 *
 */
public class GetStaticsInterceptor extends AbstractInterceptor {

	@Override
	public void before(EngineParameter ep) throws Exception {
	}

	@Override
	public void after(EngineParameter ep) throws Exception {
		Map resultObject = (Map) ep.getResult("data");
		String BBJY = resultObject.get("BBJY") == null ?"" :resultObject.get("BBJY").toString();
		
		if("".equals(BBJY)){
			throw new FlyingException("报表语句为空，无法获取数据。");
		}
		
		/**添加过滤条件*/
		String filter = ep.getParam("filter") == null?"":ep.getParam("filter").toString();
		if(!"".equals(filter) && BBJY.indexOf(" WHERE ") < 0){
			BBJY = BBJY + " WHERE " + filter;
		}
		/**添加分页条件*/
		int start = ep.getParam("start") == null?0:Integer.parseInt(ep.getParam("start").toString());
		int limit = ep.getParam("limit") == null?10:Integer.parseInt(ep.getParam("limit").toString());
		
		EngineParameter selfEp = new EngineParameter("init.execute");
		selfEp.setCommandType("list");
		selfEp.putParam("sql", BBJY);
		Engine.execute(selfEp);
		log.debug("执行的报表sql："+BBJY);
		
		List resultList = new ArrayList();
		List totalResultList = (List)selfEp.getResult("data");//获取查询列表
		
		/** 内存分页 */
		for(int i = start;i < limit; i++){
			if(i >= totalResultList.size()) break;
			resultList.add(totalResultList.get(i));
		}
		ep.getResultMap().clear();
		ep.putResult("data", resultList);
		ep.putResult("total", totalResultList.size());
	}

}
