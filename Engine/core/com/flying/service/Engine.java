package com.flying.service;

import java.util.List;

import org.springframework.context.ApplicationContext;

import com.flying.exception.FlyingException;
/**
 * 
 * <B>描述：</B>引擎类<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 *
 */
public class Engine{
	
	public static ApplicationContext ac = null;
	/**
	 * 引擎执行主方法
	 */
	public static void execute(EngineParameter ep)throws Exception{
		if(ac == null) throw new FlyingException("spring的环境未初始化完成！");
		EngineIface serviceEngine = (EngineIface)(ac.getBean("ServiceEngine"));
		serviceEngine.execute(ep);
	}
	
	/**
	 * 批量执行操作
	 */
	public static void batchExecute(List<EngineParameter> epList,String batchType)throws Exception{
		if(ac == null) throw new FlyingException("spring的环境未初始化完成！");
		EngineIface serviceEngine = (EngineIface)(ac.getBean("ServiceEngine"));
		serviceEngine.batchExecute(epList,batchType);
	}
}
