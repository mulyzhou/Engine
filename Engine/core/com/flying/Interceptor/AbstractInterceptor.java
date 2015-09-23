package com.flying.Interceptor;

import com.flying.exception.FlyingException;
import com.flying.logging.Log;
import com.flying.logging.LogFactory;
import com.flying.service.EngineIface;
import com.flying.service.EngineParameter;
/**
 * 
 * <B>描述：</B>拦截器抽象类<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 *
 */
public abstract class AbstractInterceptor implements Interceptor {
	
	protected Log log = LogFactory.getLog(AbstractInterceptor.class);//日志
	
	/**
	 * 前拦截方法
	 * 
	 * @param ep
	 * @throws Exception
	 */
	public abstract void before(EngineParameter ep) throws Exception;
	
	@Override
	public void intercept(EngineIface engineImpl, EngineParameter ep) throws Exception {
		log.debug("前拦截方法开始");
		before(ep);
		log.debug("前拦截方法结束");
		String command = ep.getCommand()==null?"":ep.getCommand();
		if("".equals(command)){
			throw new FlyingException(log.getClassName()+" 前拦截方法中，将command置为空,操作不合法!");
		}
		String commandType = ep.getCommandType()==null?"":ep.getCommandType();
		if("".equals(commandType)){
			throw new FlyingException(log.getClassName()+" 前拦截方法中，将commandType置为空,操作不合法!");
		}
		if(ep.isBreak()){
			log.warn("警告：执行被中断！");
		}else{
			engineImpl.execute(ep);
			log.debug("后拦截方法开始");
			after(ep);
			log.debug("后拦截方法结束");
		}
	}
	/**
	 * 后拦截方法
	 * 
	 * @param ep
	 * @throws Exception
	 */
	public abstract void after(EngineParameter ep) throws Exception;
	
	/**
	 * 初始化日志
	 */
	public void initLog(Class clazz){
		log = LogFactory.getLog(clazz);
	}
}
