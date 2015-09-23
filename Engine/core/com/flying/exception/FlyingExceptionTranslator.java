package com.flying.exception;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.UncategorizedSQLException;

import com.flying.logging.Log;
import com.flying.logging.LogFactory;
/**
 * 
 * <B>描述：</B>统一异常处理类，将后台异常转换为前台的用户异常<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zjx 1.0
 *
 * @author zdf 2.0
 *
 */
public class FlyingExceptionTranslator {
	
	private static Log log = LogFactory.getLog(FlyingExceptionTranslator.class);//日志
	
	public static FlyingException translate(Throwable t) {
		
		String exStr = "";//返回前台的异常提示信息
		if(t instanceof BadSqlGrammarException){	//nonTrasactionEx
			exStr = "sql语句拼写不正确";
		}else if(t instanceof DataIntegrityViolationException){
			exStr = "插入数据违反数据一致性";
		}else if(t instanceof DataAccessResourceFailureException){
			exStr = "数据库连接不上";
		}else if(t instanceof PermissionDeniedDataAccessException){
			exStr = "数据库连接没有权限";
		}else if(t instanceof ConcurrencyFailureException){//TrasactionEx
			exStr = "事务并发出错";
		}else if(t instanceof TransientDataAccessResourceException){
			exStr = "事务访问出错";
		}else if(t instanceof RecoverableDataAccessException){//RecoveralbeEx
			exStr = "事务回滚恢复出错";
		}else if(t instanceof UncategorizedSQLException){
			exStr = "Spring无法捕获异常";
		}else if(t instanceof DataAccessException){//spring Base Ex
			exStr = "Spring数据访问异常";
		}else if(t instanceof NumberFormatException){//business Function Ex
			exStr = "数字类型变量转换异常";
		}else if(t instanceof NullPointerException){
			exStr = "某些对象或者某些传入值，为空！";
		}else if(t instanceof FlyingException){
			exStr = t.getMessage();
		}else{
			exStr = "其他未知异常";
		}
		
		return new FlyingException(exStr,t);
	}
}
