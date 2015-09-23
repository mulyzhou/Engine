package com.flying.Interceptor.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;

import com.flying.Interceptor.AbstractInterceptor;
import com.flying.init.StaticVariable;
import com.flying.service.Engine;
import com.flying.service.EngineParameter;
import com.flying.util.FlyingUtil;
/**
 * 
 * <B>描述：</B>在数据库中，创建表的外键关系<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 *
 */
public class CreateForeignKeyInterceptor extends AbstractInterceptor {
	/**
	 * 系统构建第三步
	 */
	@Override
	public void before(EngineParameter ep) throws Exception{
		Document tableDoc = (Document) ep.getParam("tableDoc");
		Map table = null;
		List<Map> foreignKey = new ArrayList<Map>();
		
		//处理外键关联
		List routes = tableDoc.selectNodes("/tables/route");
		Iterator iterRoute = routes.iterator();
		while (iterRoute.hasNext()) {
			Element route = (Element) iterRoute.next();
			String fromElementId = route.attributeValue("fromElementId").trim();
			String toElementId = route.attributeValue("toElementId").trim();
			String fromElement = route.attributeValue("fromElement").trim();
			String toElement = route.attributeValue("toElement").trim();
			String fromName = route.attributeValue("fromName").trim();
			String toName = route.attributeValue("toName").trim();
			
			Element fElement = (Element) tableDoc.selectSingleNode("//table[@id='"+fromElementId+"']");
			Element tElement = (Element) tableDoc.selectSingleNode("//table[@id='"+toElementId+"']");
			
			int fState = Integer.parseInt(fElement.attributeValue("state"));
			int fClientState = Integer.parseInt(fElement.attributeValue("clientState"));
			int tState = Integer.parseInt(tElement.attributeValue("state"));
			int tClientState = Integer.parseInt(tElement.attributeValue("clientState"));
			
			if(fClientState == 1 && tClientState == 1 && (fState == 0 || tState == 0)){
				Map foreignKeyMap = new HashMap();
				String fkName = "FK_"+fromElement.toUpperCase().substring(fromElement.lastIndexOf("_")+1)+"_"+toElement.toUpperCase().substring(toElement.lastIndexOf("_")+1);
				if(fkName.length()>30){
					fkName = fkName.substring(0,29);
				}
				foreignKeyMap.put("WJMC", fkName);
				foreignKeyMap.put("ZBMC", fromElement.toUpperCase());
				foreignKeyMap.put("WBMC", toElement.toUpperCase());
				foreignKeyMap.put("ZBZD", fromName.toUpperCase());
				foreignKeyMap.put("WBZD", toName.toUpperCase());
				
				String sql = null;
				if("mysql".equals(StaticVariable.DB)){
					sql = mysqlCreateForeignKey(foreignKeyMap);
				}else if("oracle".equals(StaticVariable.DB)){
					sql = oracleCreateForeignKey(foreignKeyMap);
				}
				
				EngineParameter selfEp = new EngineParameter("init.execute");
				selfEp.putParam("sql", sql);
				selfEp.setCommandType("update");
				Engine.execute(selfEp);
			}
		}
	}
	
	@Override
	public void after(EngineParameter ep) throws Exception{
		
	}
	
	//创建表的sql
	public String mysqlCreateForeignKey(Map fk){
		/**
		 * alter table T_WF_Property add constraint FK_Reference_3 foreign key (property_type)
      		references T_WF_Step (step_Id) on delete restrict on update restrict;
		 */
		
		String sql = "ALTER TABLE "+fk.get("ZBMC")+
				" ADD CONSTRAINT "+fk.get("WJMC")+" FOREIGN KEY("+fk.get("ZBZD")+")" +
				" REFERENCES "+fk.get("WBMC")+" ("+fk.get("WBZD")+")" +
				" ON DELETE RESTRICT ON UPDATE RESTRICT;";
		
		log.debug("建立外键的语句："+sql);
		return sql;
	}
	
	//创建表的sql
	public String oracleCreateForeignKey(Map fk){
		/**
		 * alter table T_BASE_FIELD
   			add constraint FK_T_BASE_F_FK_T_BASE_T_BASE_T foreign key (BID)
      		references T_BASE_TABLE (BID)
		 */
		
		String sql = "ALTER TABLE "+fk.get("ZBMC")+
				" ADD CONSTRAINT "+fk.get("WJMC")+" FOREIGN KEY("+fk.get("ZBZD")+")" +
				" REFERENCES "+fk.get("WBMC")+" ("+fk.get("WBZD")+")";
		
		log.debug("建立外键的语句："+sql);
		return sql;
	}
}