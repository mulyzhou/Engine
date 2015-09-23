package com.flying.Interceptor.impl;

import java.util.Iterator;
import java.util.Set;

import com.flying.Interceptor.AbstractInterceptor;
import com.flying.service.EngineParameter;
/**
 * 
 * <B>描述：</B>Null参数处理类<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 *
 */
public class NullParameterSwitchInterceptor extends AbstractInterceptor {

	@Override
	public void before(EngineParameter ep) throws Exception {
		Set paramSet = ep.getParamMap().keySet();//获取key集合
		Iterator paramIter = paramSet.iterator();
		while(paramIter.hasNext()){//遍历集合
			String paramKey = (String) paramIter.next();//key名称
			
			if("".equals(ep.getParam(paramKey))){
				ep.putParam(paramKey, null);
			}
		}
	}

	@Override
	public void after(EngineParameter ep) throws Exception {

	}

}
