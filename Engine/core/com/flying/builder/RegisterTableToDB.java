package com.flying.builder;

import java.util.List;
import java.util.Map;

import com.flying.init.Item;
import com.flying.init.StaticVariable;
import com.flying.logging.Log;
import com.flying.logging.LogFactory;
import com.flying.service.Engine;
import com.flying.service.EngineParameter;
/**
 * 
 * <B>描述：</B>向菜单表中添加一条记录<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 *
 */
public class RegisterTableToDB {
	private static Log log = LogFactory.getLog(RegisterTableToDB.class);//日志
	/**
	 * 当添加一张表的时候，向菜单中添加一条记录
	 * 
	 * @param item
	 * @throws Exception 
	 */
	public synchronized static void insert(Item item) throws Exception{
		log.debug("向T_SYS_RESOURCE插入数据开始");
		
		String tableName = item.getName();// 表名
	
		String chineseName = item.getAlias();// 表中文名
		
		String pid = "72E79D997AE4441E90D4EB7842AE0F1D";
		long maxCode = 100;
		
		boolean mark = false;
		EngineParameter ep = null;
		
		if(!"".equals(StaticVariable.ROOT_MENU)){
			ep = new EngineParameter("T_SYS_RESOURCE.selectById");
			ep.putParam("RESOURCE_ID", StaticVariable.ROOT_MENU);
			Engine.execute(ep);
			
			Map resourceMap = (Map) ep.getResult("data");
			
			if(resourceMap.size() > 0){
				String rc = resourceMap.get("RESOURCE_CODE").toString();
				ep = new EngineParameter("T_SYS_RESOURCE.selectMaxCode");
				ep.putParam("RESOURCE_CODE", rc + "___");
				Engine.execute(ep);
				
				pid = StaticVariable.ROOT_MENU;
				if(!(ep.getResult("data") instanceof Map)){
					maxCode = Long.parseLong(ep.getResult("data").toString()) + 1;
				}else{
					maxCode = Long.parseLong(rc + maxCode);
				}
			}else{
				mark = true;
			}
		}
		
		if("".equals(StaticVariable.ROOT_MENU) || mark){
			ep = new EngineParameter("T_SYS_RESOURCE.selectSome");
			ep.putParam("RESOURCE_ADDR", StaticVariable.MODULE);
			ep.putParam("FACETYPE", "subSystem");
			Engine.execute(ep);
			List<Map> listRoot = (List<Map>) ep.getResult("data");
			
			if(listRoot.size() == 0){
				ep = new EngineParameter("T_SYS_RESOURCE.selectMaxCode");
				ep.putParam("RESOURCE_CODE", "___");
				Engine.execute(ep);
				
				if(!(ep.getResult("data") instanceof Map)){
					maxCode = Long.parseLong(ep.getResult("data").toString()) + 1;
				}
			}else{
				pid = listRoot.get(0).get("RESOURCE_ID").toString();
				ep = new EngineParameter("T_SYS_RESOURCE.selectMaxCode");
				ep.putParam("RESOURCE_CODE", listRoot.get(0).get("RESOURCE_CODE").toString() + "___");
				Engine.execute(ep);
				
				if(!(ep.getResult("data") instanceof Map)){
					maxCode = Long.parseLong(ep.getResult("data").toString()) + 1;
				}else{
					maxCode = Long.parseLong(listRoot.get(0).get("RESOURCE_CODE").toString() + maxCode);
				}
			}
		}
		
		//添加菜单信息T_SYS_RESOURCE
		ep = new EngineParameter("T_SYS_RESOURCE.insert");
		ep.setCommandType("insert");
		ep.putParam("RESOURCE_CODE", maxCode + "");
		ep.putParam("RESOURCE_TYPE_ID", "19AADE52436C4FA99BC3B9897E7B9408");
		ep.putParam("RESOURCE_NAME", chineseName);
		ep.putParam("RESOURCE_ADDR", "biz/"+StaticVariable.MODULE+"/"+tableName+".js");
		ep.putParam("RESOURCE_HELPINFO", chineseName);
		ep.putParam("SECURITY_NAME", "tableName");
		ep.putParam("PID", pid);
		ep.putParam("CACHE", "1");
		ep.putParam("FACETYPE", "permission");
		ep.putParam("ICON", "img/icon/splash_pink.png");
		
		Engine.execute(ep);
		String resourceId = (String) ep.getResult("data");
		
		ep = new EngineParameter("T_SYS_RESOURCE.insert");
		ep.setCommandType("insert");
		ep.putParam("RESOURCE_CODE", maxCode+"100");
		ep.putParam("RESOURCE_TYPE_ID", "8434A75F4FF1426CBA0368AFD05B3CAD");
		ep.putParam("RESOURCE_NAME", "查询所有" + chineseName);
		ep.putParam("RESOURCE_ADDR", tableName + ".selectAll");
		ep.putParam("PID", resourceId);
		Engine.execute(ep);
		
		ep = new EngineParameter("T_SYS_RESOURCE.insert");
		ep.setCommandType("insert");
		ep.putParam("RESOURCE_CODE", maxCode+"101");
		ep.putParam("RESOURCE_TYPE_ID", "61505B4AD5A443CD8D230F95B21012BB");
		ep.putParam("RESOURCE_NAME", "添加");
		ep.putParam("SECURITY_NAME", tableName + "_btnAdd");
		ep.putParam("PID", resourceId);
		
		Engine.execute(ep);
		String addButtonId = (String) ep.getResult("data");
		
		ep = new EngineParameter("T_SYS_RESOURCE.insert");
		ep.setCommandType("insert");
		ep.putParam("RESOURCE_CODE", maxCode+"101100");
		ep.putParam("RESOURCE_TYPE_ID", "8434A75F4FF1426CBA0368AFD05B3CAD");
		ep.putParam("RESOURCE_NAME", "添加一条" + chineseName);
		ep.putParam("RESOURCE_ADDR", tableName + ".insert");
		ep.putParam("PID", addButtonId);
		Engine.execute(ep);
		
		ep = new EngineParameter("T_SYS_RESOURCE.insert");
		ep.setCommandType("insert");
		ep.putParam("RESOURCE_CODE", maxCode+"102");
		ep.putParam("RESOURCE_TYPE_ID", "61505B4AD5A443CD8D230F95B21012BB");
		ep.putParam("RESOURCE_NAME", "修改");
		ep.putParam("SECURITY_NAME", tableName + "_btnModify");
		ep.putParam("PID", resourceId);
		
		Engine.execute(ep);
		String modifyButtonId = (String) ep.getResult("data");
		
		ep = new EngineParameter("T_SYS_RESOURCE.insert");
		ep.setCommandType("insert");
		ep.putParam("RESOURCE_CODE", maxCode+"102100");
		ep.putParam("RESOURCE_TYPE_ID", "8434A75F4FF1426CBA0368AFD05B3CAD");
		ep.putParam("RESOURCE_NAME", "修改一条" + chineseName);
		ep.putParam("RESOURCE_ADDR", tableName + ".update");
		ep.putParam("PID", modifyButtonId);
		Engine.execute(ep);
		
		ep = new EngineParameter("T_SYS_RESOURCE.insert");
		ep.setCommandType("insert");
		ep.putParam("RESOURCE_CODE", maxCode+"103");
		ep.putParam("RESOURCE_TYPE_ID", "61505B4AD5A443CD8D230F95B21012BB");
		ep.putParam("RESOURCE_NAME", "删除");
		ep.putParam("SECURITY_NAME", tableName + "_btnDelete");
		ep.putParam("PID", resourceId);
		Engine.execute(ep);
		
		String deleteButtonId = (String) ep.getResult("data");
		
		ep = new EngineParameter("T_SYS_RESOURCE.insert");
		ep.setCommandType("insert");
		ep.putParam("RESOURCE_CODE", maxCode+"103100");
		ep.putParam("RESOURCE_TYPE_ID", "8434A75F4FF1426CBA0368AFD05B3CAD");
		ep.putParam("RESOURCE_NAME", "删除一条" + chineseName);
		ep.putParam("RESOURCE_ADDR", tableName + ".delete");
		ep.putParam("PID", deleteButtonId);
		Engine.execute(ep);
		
		ep = new EngineParameter("T_SYS_RESOURCE.insert");
		ep.setCommandType("insert");
		ep.putParam("RESOURCE_CODE", maxCode+"104");
		ep.putParam("RESOURCE_TYPE_ID", "61505B4AD5A443CD8D230F95B21012BB");
		ep.putParam("RESOURCE_NAME", "导出");
		ep.putParam("SECURITY_NAME", tableName + "_btnDownload");
		ep.putParam("PID", resourceId);
		Engine.execute(ep);
			
		log.debug("向T_SYS_RESOURCE插入数据结束");
	}
	/**
	 * 根据表名称，删除菜单表中的数据
	 * 
	 * @param bmc
	 * @throws Exception
	 */
	public static void delete(String bmc) throws Exception{
		String addr = "biz/"+StaticVariable.MODULE+"/"+bmc+".js";
		
		EngineParameter ep = new EngineParameter("T_SYS_RESOURCE.deleteByAddr");
		ep.putParam("RESOURCE_ADDR", addr);
		
		Engine.execute(ep);
		
		log.debug("删除T_SYS_RESOURCE中，src=biz/"+StaticVariable.MODULE+"/"+bmc+".js的数据");
	}
}
