package com.flying.dao;

import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
/**
 * 
 * <B>描述：</B>基础自ibatis执行类，获取ibatis执行对象<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 *
 */
public class IbatisBaseDAO extends SqlMapClientDaoSupport{
	
	protected SqlMapClientTemplate smcTemplate = this.getSqlMapClientTemplate();
	
	public IbatisBaseDAO(){}
}
