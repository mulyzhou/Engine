package com.flying.Interceptor.impl;

import java.util.Iterator;
import java.util.Set;

import com.flying.Interceptor.AbstractInterceptor;
import com.flying.service.EngineParameter;
/**
 * 
 * <B>描述：</B>boolean参数处理类<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 *
 */
public class BooleanParameterSwitchInterceptor extends AbstractInterceptor {

	@Override
	public void before(EngineParameter ep) throws Exception {
		Set paramSet = ep.getParamMap().keySet();//获取key集合
		Iterator paramIter = paramSet.iterator();
		while(paramIter.hasNext()){//遍历集合
			String paramKey = (String) paramIter.next();//key名称
			
			if(ep.getParam(paramKey) instanceof String){//如果是值是字符类型
				String paramValue = (String)ep.getParam(paramKey);
				if("true".equals(paramValue)){
					ep.putParam(paramKey, 1);
					log.debug(paramKey+" 是布尔类型，值=true，已经转换成mysql的数据格式！");
				}else if("false".equals(paramValue)){
					ep.putParam(paramKey, 0);
					log.debug(paramKey+" 是布尔类型，值=false，已经转换成mysql的数据格式！");
				}
				
			}
			
			if(ep.getParam(paramKey) instanceof Boolean){//如果是值是布尔类型
				Boolean paramValue = (Boolean)ep.getParam(paramKey);
				if(paramValue){
					ep.putParam(paramKey, 1);
					log.debug(paramKey+" 是布尔类型，值=true，已经转换成mysql的数据格式！");
				}else{
					ep.putParam(paramKey, 0);
					log.debug(paramKey+" 是布尔类型，值=false，已经转换成mysql的数据格式！");
				}
			}
		}
	}

	@Override
	public void after(EngineParameter ep) throws Exception {

	}

}
