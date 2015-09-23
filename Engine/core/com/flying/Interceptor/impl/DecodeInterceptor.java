package com.flying.Interceptor.impl;

import com.flying.Interceptor.AbstractInterceptor;
import com.flying.service.EngineParameter;
/**
 * 
 * <B>描述：</B>unicode编码参数转换，用于flex传递进入的参数<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 *
 */
public class DecodeInterceptor extends AbstractInterceptor {

	@Override
	public void before(EngineParameter ep) throws Exception {
		//paramMap中sql，此处将UTF-8编码的转换成正常字符。
		if(ep.getParam("sql") != null && ep.getParam("encode") != null && "true".equals(ep.getParam("encode"))){
			ep.putParam("sql",java.net.URLDecoder.decode(ep.getParam("sql").toString(),"UTF-8"));
			log.debug("将sql参数的数据，进行UTF-8转换");
		}
		
		if(ep.getParam("menuName") != null && ep.getParam("encode") != null && "true".equals(ep.getParam("encode"))){
			ep.putParam("menuName",java.net.URLDecoder.decode(ep.getParam("menuName").toString(),"UTF-8"));
			log.debug("将menuName参数的数据，进行UTF-8转换");
		}
	}

	@Override
	public void after(EngineParameter ep) throws Exception {

	}

}
