package com.flying.logging;

/**
 * 
 * <B>描述：</B>自定义日志接口<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 *
 */
public interface Log {
	/**
	 * 是否启动调试模式
	 * 
	 * @return boolean
	 */
	public boolean isDebugEnabled();
	/**
	 * 获取出现异常的类名称
	 * 
	 * @return 返回类名
	 */
	public String getClassName();
	/**
	 * 调试信息
	 * 
	 * @param s
	 */
	public void debug(String s);
	/**
	 * 一般信息
	 * @param s
	 */
	public void info(String s);
	/**
	 * 警告信息
	 * 
	 * @param s
	 */
	public void warn(String s);
	/**
	 * 错误信息
	 * 
	 * @param s
	 */
	public void error(String s);
	/**
	 * 错误信息
	 * 
	 * @param s
	 * @param e
	 */
	public void error(String s, Throwable e);
	/**
	 * 存放数据库操作日志
	 * 
	 * @param threadId
	 * @param ep
	 */
	public void log(String threadId,String command,String commandType,String commandParams);
}
