package com.flying.logging;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.flying.service.Engine;
import com.flying.service.EngineParameter;

public class LogCache {
	private static Log log = LogFactory.getLog(LogCache.class);// 日志
	//用于存放当前线程日志信息
	public static Map<String,Map> THREAD_INFO = new Hashtable<String,Map>();
	/**
	 * 添加用户日志信息
	 * 
	 * @param threadId
	 * @param command
	 * @param commandType
	 * @param commandParams
	 */
	public static void addLog(final String threadId,final String command,final String commandType,final String commandParams){
		Thread t = new Thread(new Runnable(){
			@Override
			public void run(){
				try {
					//用户消息
					Map userLog = null;
					if("T_BASE_LOG.insert".equals(command)){
						log.debug("T_BASE_LOG.insert 无需记录！");
						return;
					}else if(!THREAD_INFO.containsKey(threadId)){
						log.error("日志线程池中，没有ID是 " + threadId +" 的线程！");
						return;
					}else{
						userLog = THREAD_INFO.get(threadId);
					}
					
					EngineParameter selfEp = new EngineParameter("T_BASE_LOG.insert");
					selfEp.putParam("IP", userLog.get("IP"));
					selfEp.putParam("COMMAND", userLog.get("COMMAND"));
			    	selfEp.putParam("COMMAND_PARAM", userLog.get("COMMAND_PARAM"));
			    	selfEp.putParam("STARTTIME", userLog.get("STARTTIME"));
			    	selfEp.putParam("USER_ID", userLog.get("USER_ID"));
					selfEp.putParam("USER_NAME", userLog.get("USER_NAME"));
					selfEp.putParam("SQLID", command);
					selfEp.putParam("SQLID_TYPE", commandType);
					selfEp.putParam("TABLENAME", command.substring(0, command.indexOf(".")));
					selfEp.putParam("SQLID_PARAM", commandParams);
					
					Engine.execute(selfEp);
				} catch (Exception e) {
					log.error("添加数据库操作日志出错", e);
				}
			}
		});
		
		t.start();
	}
	
	/**
	 * 清理过期的用户日志信息
	 */
	public static void clearLog(){
		Thread t = new Thread(new Runnable(){
			@Override
			public void run(){
				List<String> removeThreadIds = new ArrayList();
				Iterator<String> keys = THREAD_INFO.keySet().iterator();
		 		while(keys.hasNext()){
		 			String key = keys.next();
		 			Map userLog = THREAD_INFO.get(key);
		 			//将发起时间加2分钟
		 			Calendar calendar=Calendar.getInstance();   
		 		    calendar.setTime((Date)userLog.get("STARTTIME"));
		 		    calendar.add(Calendar.MINUTE, 2);
		 		    //如果过期了，则删除此线程的用户日志信息
		 			if(calendar.getTimeInMillis() < System.currentTimeMillis()){
		 				removeThreadIds.add(key);
		 			}
		 		}
		 		//删除动作
		 		for(int i = 0;i<removeThreadIds.size();i++){
		 			THREAD_INFO.remove(removeThreadIds.get(i));
		 		}
			}
		});
		
		t.start();
	}
}
