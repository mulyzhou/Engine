package com.flying.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.flying.Interceptor.Interceptor;
import com.flying.dao.IDAO;
import com.flying.exception.FlyingException;
import com.flying.init.Item;
import com.flying.init.Operation;
import com.flying.init.StaticVariable;
import com.flying.logging.Log;
import com.flying.logging.LogFactory;
/**
 * 
 * <B>描述：</B>引擎实现类<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 *
 */
public class ServiceEngine implements EngineIface{

	private static Log log = LogFactory.getLog(ServiceEngine.class);//日志

	private List<Interceptor> interceptors;

	private IDAO dao;

	private int offset = -1;
	
	public ServiceEngine() {
	}
	
	public IDAO getDao() {
		return dao;
	}

	public void setDao(IDAO dao) {
		this.dao = dao;
	}
	
	/**
	 * 执行引擎
	 */
	public void execute(EngineParameter ep) throws Exception {
		//初始化本实例的拦截器
		if(this.interceptors == null){
			initInterceptor(ep);
		}
		this.offset++;

		if (interceptors != null && offset < interceptors.size()) {
			long start = System.currentTimeMillis();
			String intercepterName = interceptors.get(offset).getClass().getName();
			log.debug("拦截器："+intercepterName+" 开始执行");
			Interceptor interceptor = interceptors.get(offset);//实例化
			interceptor.intercept(this, ep);//执行拦截方法
			long end = System.currentTimeMillis();
			log.debug("拦截器："+intercepterName+" 执行完成，耗时："+(end-start)+"(ms)");
		} else {
			//执行主流程
			if(ep.isSpace()){
				log.warn("警告：拦截器强制执行了空操作！");
			}else{
				go(ep);
			}
		}
	}
	
	/**
	 * 根据传递的参数，获取本操作的拦截器
	 * 
	 * @param paramMap
	 * @throws FlyingException 
	 */
	private void initInterceptor(EngineParameter ep) throws FlyingException{
		//根据执行命令，获取命令下的拦截器
		String command = ep.getCommand();
		//从内存中获取commandType,如果内存中没有，则从tablename.xml中查找commandType
		String commandType = ep.getCommandType();
		if(commandType == null){
			//解析从配置文件中获取commandType
			String tableName = command.substring(0,command.indexOf("."));
			Item item = StaticVariable.FLYINGCONFIG.get(tableName);
			if(item == null){
				throw new FlyingException(tableName+" 未注册");
			}
			Map<String,Operation> operationMap = item.getMapOperation();
			if(operationMap.size() == 0){
				throw new FlyingException(tableName+" 没有操作项");
			}
			//获取操作项
			Operation operation = operationMap.get(command);
			if(operation == null){
				throw new FlyingException(tableName+" 没有注册 "+command);
			}
			if(operation.getType() ==  null){
				throw new FlyingException(command+" 未配置执行类型！");
			}else{
				 ep.setCommandType(operation.getType());
				 log.debug("执行命令："+command+" 	执行类型："+ operation.getType()+"	 "+operation.getAlias());
			}
			
			//获取此操作下得拦截器
			this.interceptors = operation.getInterceptorList()== null ? new ArrayList() : operation.getInterceptorList();
			if(interceptors.size() == 0){
				log.debug("执行命令："+command+" 没有注册拦截器！");
			}
		}
	}
	
	/**
	 * 执行本操作的主流程
	 * 
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	private void go(EngineParameter ep) throws Exception{
		//执行的命令
		String command = ep.getCommand();
		// 获取执行的类型
		String commandType = ep.getCommandType();
		if (INSERT.equals(commandType)) {
			Object obj = dao.insert(ep);
			ep.putResult("data", obj);
		} else if (UPDATE.equals(commandType)) {
			dao.update(ep);
		} else if (DELETE.equals(commandType)) {
			dao.delete(ep);
		} else if (OBJECT.equals(commandType)) {
			Object resultObject = dao.selectOne(ep);
			ep.putResult("data", resultObject);
		} else if (LIST.equals(commandType)) {
			List<Object> resultList = dao.selectList(ep);
			ep.putResult("data", resultList);
		} else if (MAP.equals(commandType)) {
			Map result = dao.selectByPaging(ep);
			ep.putResult("data", result.get("data"));
			ep.putResult("total", result.get("total"));
		} else if (SQL.equals(commandType)) {
			String resultSql = dao.getCommandSql(ep);
			ep.putResult("data", resultSql);
		}else if(SPACE.equals(commandType)){
			log.debug(command+" 执行了空操作！");
		}else{
			throw new FlyingException(commandType+" 未注册的执行类型");
		}
	}
	
	/**
	 * 批量执行
	 */
	public void batchExecute(List<EngineParameter> epList,String batchType) throws Exception {
		if (INSERT.equals(batchType)) {
			dao.insertAll(epList);
		}else if (UPDATE.equals(batchType)) {
			dao.updateAll(epList);
		}else if (DELETE.equals(batchType)) {
			dao.deleteAll(epList);
		}else {
			log.debug(batchType + "未实现的批量执行！");
		}
	}
}
