package com.flying.dao;

import java.util.List;
import java.util.Map;

import com.flying.service.EngineParameter;
/**
 * 
 * <B>描述：</B>Dao层，数据执行接口<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 *
 */
public interface IDAO {
	/**
	 * 插入操作
	 * 
	 * @param ep EngineParameter对象
	 * @return 插入，返回主键
	 * @exception 抛出异常
	 */
	public Object insert(EngineParameter ep) throws Exception;
	/**
	 * 批量插入操作
	 * 
	 * @param epList List对象
	 * @return 插入，返回主键
	 * @exception 抛出异常
	 */
	public int insertAll(List<EngineParameter> epList) throws Exception;
	/**
	 * 更新操作
	 * 
	 * @param ep EngineParameter对象
	 * @exception 抛出异常
	 */	
	public void update(EngineParameter ep) throws Exception;
	/**
	 * 批量更新操作
	 * 
	 * @param epList List对象
	 * @return 插入，返回主键
	 * @exception 抛出异常
	 */
	public void updateAll(List<EngineParameter> epList) throws Exception;
	/**
	 * 删除操作
	 * 
	 * @param ep EngineParameter对象
	 * @exception 抛出异常
	 */
	public void delete(EngineParameter ep) throws Exception;
	/**
	 * 批量删除操作
	 * 
	 * @param epList List对象
	 * @return 插入，返回主键
	 * @exception 抛出异常
	 */
	public void deleteAll(List<EngineParameter> epList) throws Exception;
	/**
	 * 查询一个对象
	 *
	 * @param ep EngineParameter对象
	 * @return 返回一个对象，一般使用Map存储
	 * @exception 抛出异常
	 */
	public Object selectOne(EngineParameter ep) throws Exception;
	/**
	 * 查询一组对象
	 * 
	 * @param ep EngineParameter对象
	 * @return 返回一组对象，使用List对象
	 * @exception 抛出异常
	 */
	public List<Object> selectList(EngineParameter ep) throws Exception;
	/**
	 * 查询一组数据，带分页信息
	 * 
	 * @param ep EngineParameter对象
	 * @return 返回一个Map对象，其中key=data，存放数据，key=total存在总数量
	 * @exception 抛出异常
	 */
	public Map<String, Object> selectByPaging(EngineParameter ep) throws Exception;
	/**
	 * 查询执行的sql语句
	 * 
	 * @param ep EngineParameter对象
	 * @return 返回执行sql字符串
	 * @exception 抛出异常
	 */
	public String getCommandSql(EngineParameter ep) throws Exception;
}
