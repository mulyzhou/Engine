package com.flying.service;

import java.util.HashMap;
import java.util.Map;
/**
 * 
 * <B>描述：</B>引擎参数<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 *
 */
public class EngineParameter {
	//执行的sqlid
	private String command = null;
	//执行的类型
	private String commandType = null;
	//传递的参数
	private Map paramMap = new HashMap();
	//接受的数据
	private Map resultMap = new HashMap();
	//下载的文件名称
	private String fileDownloadName = null;
	//调整页面名称
	private String redirectPageName = null;
	//设置redis的过期时间,默认不过期
	private int redisExpire = -1;
	//是否将执行改成空执行
	private boolean isSpace = false;
	//是否中断执行
	private boolean isBreak = false;
	/**
	 * 传递command的构造
	 * 
	 * @param command参数 
	 */
	public EngineParameter(String command){
		this.command = command;
	}
	/**
	 * 从传入参数（paramMap）中移除名称是key的值
	 * 
	 * @param key
	 */
	public void removeParam(Object key){
		paramMap.remove(key);
	}
	/**
	 * 向传入参数（paramMap）中，插入key-value的键值对
	 * 
	 * @param key
	 * @param value
	 */
	public void putParam(Object key,Object value){
		paramMap.put(key, value);
	}
	
	/**
	 * 从传入参数（paramMap）中，获取key对应的值
	 * 
	 * @param key
	 * @return 从paramMap中，返回key对应的值，类型是Object
	 */
	public Object getParam(Object key){
		return paramMap.get(key);
	}
	
	/**
	 * 获取paramMap对象
	 * 
	 * @return 类型是Map
	 */
	public Map getParamMap() {
		return paramMap;
	}
	
	/**
	 * 设置传入的paramMap,建议使用putParam方法传递参数
	 * 
	 * @param paramMap Map类型
	 */
	@Deprecated
	public void setParamMap(Map paramMap) {
		this.paramMap = paramMap;
	}
	
	/**
	 * 从传出参数（resultMap）中移除名称是key的值
	 * 
	 * @param key
	 */
	public void removeResult(Object key){
		resultMap.remove(key);
	}
	
	/**
	 * 向传出参数（resultMap）中，插入key-value的键值对
	 * 
	 * @param key
	 * @param value
	 */
	public void putResult(Object key,Object value){
		resultMap.put(key, value);
	}
	/**
	 * 从传出参数（resultMap）中，获取key对应的值
	 * 
	 * @param key
	 * @return 从resultMap中，返回key对应的值，类型是Object
	 */
	public Object getResult(Object key){
		return resultMap.get(key);
	}
	
	/**
	 * 获取resultMap对象
	 * 
	 * @return 类型是Map
	 */
	public Map getResultMap() {
		return resultMap;
	}
	
	/**
	 * 设置传入的resultMap,建议改成putResult传递参数
	 * 
	 * @param resultMap Map类型
	 */
	@Deprecated
	public void setResultMap(Map resultMap) {
		this.resultMap = resultMap;
	}
	/**
	 * Engine执行的sqlid
	 * 
	 * @return sqlid 类型字符串
	 */
	public String getCommand() {
		return command;
	}
	/**
	 * 获取Engine执行的sqlid的类型
	 * 
	 * @return sqlid的执行类型
	 */
	public String getCommandType() {
		return commandType;
	}
	/**
	 * 设置Engine执行的sqlid的类型
	 * 
	 * @param commandType
	 */
	public void setCommandType(String commandType) {
		this.commandType = commandType;
	}
	
	/**
	 * 获取下载文件名称，当执行的是文件下载时，下载文件的名称
	 * 
	 * @return 下载文件名称
	 */
	public String getFileDownloadName() {
		return fileDownloadName;
	}
	/**
	 * 设置下载文件的名称
	 * 
	 * @param fileDownloadName 设置下载文件名称
	 */
	public void setFileDownloadName(String fileDownloadName) {
		this.fileDownloadName = fileDownloadName;
	}
	
	/**
	 * 获取跳转到的页面名称
	 * 
	 * @return	跳转到的页面名称
	 */
	public String getRedirectPageName() {
		return redirectPageName;
	}
	
	/**
	 * 设置跳转到的页面名称
	 * 
	 * @param redirectPageName 跳转到的页面名称
	 */
	public void setRedirectPageName(String redirectPageName) {
		this.redirectPageName = redirectPageName;
	}
	/**
	 * 获取redis缓存的过期值
	 * @return redisExpire -1：不过期
	 */
	public int getRedisExpire() {
		return redisExpire;
	}
	/**
	 * 设置redis缓存的过期值
	 * @param redisExpire
	 */
	public void setRedisExpire(int redisExpire) {
		this.redisExpire = redisExpire;
	}
	/**
	 * 返回跳过主执行标示
	 * 
	 * @return true：跳过；false：不跳过；默认false
	 */
	public boolean isSpace() {
		return isSpace;
	}
	/**
	 * 设置是否跳过主执行标示
	 * 
	 * @param isSpace true：跳过；false：不跳过
	 */
	public void setSpace(boolean isSpace) {
		this.isSpace = isSpace;
	}
	/**
	 * 返回是否中断执行标示
	 * 
	 * @return true：中断；false：不中断；默认false
	 */
	public boolean isBreak() {
		return isBreak;
	}
	/**
	 * 设置是否中断执行标示
	 * 
	 * @param isBreak 中断；false：不中断
	 */
	public void setBreak(boolean isBreak) {
		this.isBreak = isBreak;
	}	
}
