package com.flying.Interceptor;

import com.flying.service.EngineIface;
import com.flying.service.EngineParameter;
/**
 * 
 * <B>描述：</B>拦截器接口<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 *
 */
public interface Interceptor {
	/**
	 * 拦截器执行方法
	 * 
	 * @param engineImpl 引擎实例
	 * @param ep 传递参数
	 * @throws Exception
	 */
	public void intercept(EngineIface engineImpl,EngineParameter ep)throws Exception ;
	/**
	 * 初始化拦截器的日志方法
	 * 
	 * @param clazz
	 */
	public void initLog(Class clazz);
	
}
