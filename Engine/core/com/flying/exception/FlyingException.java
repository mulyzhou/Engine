package com.flying.exception;

import com.flying.logging.Log;
import com.flying.logging.LogFactory;
/**
 * 
 * <B>描述：</B>flying框架中，自定义的异常处理类<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 *
 */
public class FlyingException extends Exception {

	private static final long serialVersionUID = 1L;
	
	private static Log log = LogFactory.getLog(FlyingException.class);//日志
	
	public FlyingException(String msg){
		super(msg);
		
		log.error(msg,this);
	}
	
	public FlyingException(String msg,Throwable e){
		super(msg,e);
		
		log.error(msg, e);
	}
}
