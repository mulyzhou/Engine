package com.flying.service;

import java.util.List;

/**
 * 
 * <B>描述：</B>引擎接口<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 *
 */
public interface EngineIface {
	public static final String INSERT  = "insert";
	public static final String UPDATE  = "update";
	public static final String DELETE  = "delete";
	public static final String OBJECT  = "object";
	public static final String LIST  = "list";
	public static final String MAP  = "map";
	public static final String SQL  = "sql";
	public static final String SPACE  = "space";
	/**
	 * 执行引擎，进行数据操作
	 * @param ep 传递的参数
	 * @throws Exception
	 */
	public void execute(EngineParameter ep) throws Exception;
	/**
	 * 批量执行操作
	 * @param epList 执行语句
	 * @param batchType 执行类型
	 * @throws Exception
	 */
	public void batchExecute(List<EngineParameter> epList, String batchType) throws Exception;
}
