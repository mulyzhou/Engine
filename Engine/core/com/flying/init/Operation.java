package com.flying.init;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.flying.Interceptor.Interceptor;
import com.flying.logging.Log;
import com.flying.logging.LogFactory;

/**
 * 
 * <B>描述：</B>tablename的op对应的实体对象<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 *
 */
public class Operation {
	private static Log log = LogFactory.getLog(Operation.class);//日志
	//操作别名
	private String alias;
	//执行的sqlid
	private String sqlid;
	//执行sqlid的类型
	private String type;
	//redis过期时间
	private int expire;
	//是否需要进行验证
	private boolean validate;
	//批量操作属性
	private	String property;
	//批量操作数组属性
	private String arrayProperty;
	//拦截列表
	private List<Interceptor> interceptorList = new ArrayList<Interceptor>();
	//后台验证
	private List<Validate> validateList = new ArrayList<Validate>();
	
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getSqlid() {
		return sqlid;
	}
	public void setSqlid(String sqlid) {
		this.sqlid = sqlid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getExpire() {
		return expire;
	}
	public void setExpire(int expire) {
		this.expire = expire;
	}
	public boolean isValidate() {
		return validate;
	}
	public void setValidate(boolean validate) {
		this.validate = validate;
		
		if(this.validate){//添加验证拦截
			try {
				Interceptor interceptorClass = (Interceptor)(Class.forName("com.flying.Interceptor.impl.ValidateInterceptor").newInstance());
				this.setInterceptor(interceptorClass);
			} catch (ClassNotFoundException e) {
				log.error("系统中未找到验证拦截类",e);
			} catch (InstantiationException e) {
				log.error("系统实例化验证拦截类失败",e);
			} catch (IllegalAccessException e) {
				log.error("系统实例化验证拦截类失败",e);
			}
		}
	}
	public List<Interceptor> getInterceptorList() {
		return interceptorList;
	}
	public void setInterceptorList(List<Interceptor> interceptorList) {
		this.interceptorList = interceptorList;
	}
	public void setInterceptor(Interceptor interceptor) {
		this.interceptorList.add(interceptor);
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getArrayProperty() {
		return arrayProperty;
	}
	public void setArrayProperty(String arrayProperty) {
		this.arrayProperty = arrayProperty;
	}
}
