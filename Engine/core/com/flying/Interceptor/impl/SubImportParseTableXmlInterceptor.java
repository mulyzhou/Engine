package com.flying.Interceptor.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;

import com.flying.Interceptor.AbstractInterceptor;
import com.flying.init.StaticVariable;
import com.flying.service.Engine;
import com.flying.service.EngineParameter;
import com.flying.util.FileUtil;
/**
 * <B>描述：</B>子模块解析表<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 *
 */
public class SubImportParseTableXmlInterceptor extends AbstractInterceptor {

	@Override
	public void before(EngineParameter ep) throws Exception {
		String subName = ep.getParam("SUB_NAME").toString().trim();
		
		String subTableXmlPath = "";
		if(StaticVariable.DEBUG){
			subTableXmlPath = StaticVariable.PATH + "WebContent/db/"+ subName +"/tableXml.xml";
		}else{
			subTableXmlPath = StaticVariable.PATH;
		}
		log.debug("解析 " + subTableXmlPath);
		//将前台文件，变成dom结构
		Document tableDoc = FileUtil.readXml(new File(subTableXmlPath));
		//处理外键关联
		List routes = tableDoc.selectNodes("/tables/route");
		Iterator iterRoute = routes.iterator();
		while (iterRoute.hasNext()) {
			Element route = (Element) iterRoute.next();
			String id = route.attributeValue("id").trim();
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
			int tClientState = Integer.parseInt(fElement.attributeValue("clientState"));
			
			if(fClientState == 1 && tClientState == 1 && fState == 1 & tState == 1){
				EngineParameter selfEp = new EngineParameter("T_BASE_FOREIGNKEY.insert");
				selfEp.putParam("WJMC", "FK_"+fromElement.toUpperCase()+"_"+toElement.toUpperCase()+"_"+id);
				selfEp.putParam("ZBMC", fromElement.toUpperCase());
				selfEp.putParam("WBMC", toElement.toUpperCase());
				selfEp.putParam("ZBZD", fromName.toUpperCase());
				selfEp.putParam("WBZD", toName.toUpperCase());
				selfEp.putParam("STATE", 0);
				
				selfEp.setCommandType("insert");
				Engine.execute(selfEp);
				log.debug("插入外键信息：FK_"+fromElement.toUpperCase()+"_"+toElement.toUpperCase()+"_"+id);
			}
		}
		
		//解析表结构
		List tables = tableDoc.selectNodes("/tables/table");
		Iterator iterTable = tables.iterator();
		List<Integer> bids = new ArrayList<Integer>();
		while (iterTable.hasNext()) {
			Element table = (Element) iterTable.next();
			String bmc = table.attributeValue("name").trim().toUpperCase();
			String bzs = table.attributeValue("desc").trim().toUpperCase();
			String x =  table.attributeValue("x").trim();
			String y =  table.attributeValue("y").trim();
			String width =  table.attributeValue("width").trim();
			String height =  table.attributeValue("height").trim();
			String state =  table.attributeValue("state").trim();
			String clientState =  table.attributeValue("clientState").trim();
			/**
			 * 根据state的状态，进行编号
			 * state=1 ：可以改变table的位置
			 * state=0
			 * 	clientState = 1：需要生成代码
			 * 	clientState = 0：只需要将表存入数据库
			 */
			//根据名称查询
			EngineParameter selfEp = new EngineParameter("T_BASE_TABLE.selectByBmc");
			selfEp.putParam("BMC", bmc);
			selfEp.setCommandType("list");
			Engine.execute(selfEp);
			List<Map> lm = (List<Map>) selfEp.getResult("data");
			//进行业务处理
			if(Integer.parseInt(state) == 1 && Integer.parseInt(clientState) == 1){				
				//前台需要传递到后台，需要生成管理页面
				//1 存入数据库
				selfEp = new EngineParameter("T_BASE_TABLE.insert");
				selfEp.putParam("BMC", bmc);
				selfEp.putParam("BZS", bzs);
				selfEp.putParam("X", x);
				selfEp.putParam("Y", y);
				selfEp.putParam("WIDTH", width);
				selfEp.putParam("HEIGHT", height);
				selfEp.putParam("STATE", 1);
				selfEp.setCommandType("insert");
				Engine.execute(selfEp);
				int bid = (Integer) selfEp.getResult("data");
				log.debug("t_base_table添加"+bmc+"的信息");
				
				List fields = table.selectNodes("column");
				Iterator iterField = fields.iterator();
				while (iterField.hasNext()) {
					selfEp = new EngineParameter("T_BASE_FIELD.insert");
					Element field = (Element) iterField.next();
					selfEp.putParam("BID", bid);
					selfEp.putParam("ZDMC", field.attributeValue("name").trim().toUpperCase());
					selfEp.putParam("ZDZS", field.attributeValue("desc").trim().toUpperCase());
					selfEp.putParam("ZDLX", field.attributeValue("type").trim());
					selfEp.putParam("ZDCD", field.attributeValue("length").trim());
					Boolean isPk = Boolean.parseBoolean(field.attributeValue("isPk").trim());
					if(isPk){
						selfEp.putParam("SFZJ",1);
					}else{
						selfEp.putParam("SFZJ",0);
					}
					
					Boolean isNull = Boolean.parseBoolean(field.attributeValue("isNull").trim());
					if(isPk){
						selfEp.putParam("SFWK",1);
					}else{
						selfEp.putParam("SFWK",0);
					}
					selfEp.putParam("ZDMC", field.attributeValue("name").trim().toUpperCase());
					
					selfEp.setCommandType("insert");
					Engine.execute(selfEp);
				}
				
				log.debug("t_base_field插入"+bmc+"的字段信息");
				//将需要生成表的id存入变量
				bids.add(bid);
				//添加菜单信息T_BASE_MENU				
				selfEp = new EngineParameter("T_SYS_RESOURCE.insert");
				selfEp.putParam("RESOURCE_CODE", 100);
				selfEp.putParam("RESOURCE_TYPE_ID", 104);
				selfEp.putParam("RESOURCE_NAME", bzs);
				selfEp.putParam("RESOURCE_ADDR", "biz/"+ subName +"/"+bmc+".js");
				selfEp.putParam("RESOURCE_HELPINFO", bzs);
				selfEp.putParam("SECURITY_NAME", "tableName");
				selfEp.putParam("PID", "0");
				selfEp.putParam("CACHE", "1");
				selfEp.putParam("FACETYPE", "permission");
				
				Engine.execute(selfEp);
			}
		}
		
		//将需要生成的表存入paramMap中
		ep.putParam("BIDS", bids);
	}

	@Override
	public void after(EngineParameter ep) throws Exception {

	}

}
