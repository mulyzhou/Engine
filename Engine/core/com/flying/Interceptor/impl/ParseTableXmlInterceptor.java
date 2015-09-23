package com.flying.Interceptor.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.flying.Interceptor.AbstractInterceptor;
import com.flying.builder.BuilderUtil;
import com.flying.exception.FlyingException;
import com.flying.init.StaticVariable;
import com.flying.service.Engine;
import com.flying.service.EngineParameter;
import com.flying.util.FileUtil;
/**
 * 
 * <B>描述：</B>前台使用flex设计表，之后将数据传递到后台保存元数据表中。<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 *
 */
public class ParseTableXmlInterceptor extends AbstractInterceptor {
	/**
	 * 系统构建第一步
	 */
	@Override
	public void before(EngineParameter ep) throws Exception {
 
/**		
		String tablesXml = "<?xml version='1.0' encoding='UTF-8'?><tables><table id='1' name='student' desc='学生' x='439' y='108' width='100' height='100'>"
				+ "<column name='id' desc='编号' type='number' length='' isNull='false' isPk='true'/>"
				+ "<column name='name' desc='名称' type='varchar' length='100' isNull='true' isPk='false'/>"
				+ "<column name='classid' desc='班级编号' type='number' length='' isNull='false' isPk='false'/>"
				+ "</table></tables>";
*/		
		String tablesXml = ep.getParam("xml")==null?null: ep.getParam("xml").toString().trim();
		if(tablesXml == null){
			throw new FlyingException("paramMap.xml为空,生成无法进行");
		}else{
			tablesXml = java.net.URLDecoder.decode(tablesXml,"UTF-8");
		}
		//将前台文件，变成dom结构
		Document tableDoc = DocumentHelper.parseText(tablesXml);
		
		if(StaticVariable.ENABLE_DATATABLE){
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
				
				if(fClientState == 1 && tClientState == 1 && (fState == 0 || tState == 0)){
					EngineParameter selfEp = new EngineParameter("T_BASE_FOREIGNKEY.insert");
					String fkName = "FK_"+fromElement.toUpperCase().substring(fromElement.lastIndexOf("_")+1)+"_"+toElement.toUpperCase().substring(toElement.lastIndexOf("_")+1)+"_"+id;
					if(fkName.length()>30){
						fkName = fkName.substring(0,29);
					}
					selfEp.putParam("WJMC", fkName);
					selfEp.putParam("ZBMC", fromElement.toUpperCase());
					selfEp.putParam("WBMC", toElement.toUpperCase());
					selfEp.putParam("ZBZD", fromName.toUpperCase());
					selfEp.putParam("WBZD", toName.toUpperCase());
					selfEp.putParam("STATE", 0);
					
					selfEp.setCommandType("insert");
					Engine.execute(selfEp);
					log.debug("插入外键信息："+fkName);
				}
			}
			
			//解析表结构
			List tables = tableDoc.selectNodes("/tables/table");
			Iterator iterTable = tables.iterator();
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
				if(Integer.parseInt(state) == 1){
					//对于已经生成的表，可以修改其坐标
					for(int i=0;i<lm.size();i++){
						selfEp = new EngineParameter("T_BASE_TABLE.update");
						selfEp.putParam("BMC", bmc);
						selfEp.putParam("BZS", bzs);
						selfEp.putParam("X", x);
						selfEp.putParam("Y", y);
						selfEp.putParam("WIDTH", width);
						selfEp.putParam("HEIGHT", height);
						selfEp.putParam("STATE", state);
						selfEp.putParam("BID", lm.get(i).get("BID"));
						selfEp.setCommandType("update");
						Engine.execute(selfEp);
						log.debug(bmc+"已经存在，修改坐标信息");
					}
				}else if(Integer.parseInt(state) == 0 && Integer.parseInt(clientState) == 1){				
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
				}
			}
		}

		//删除前台传入的，未处理的xml数据
		ep.removeParam("xml");
		//将table的document存入paramMap中
		ep.putParam("tableDoc", tableDoc);
	}
	/**
	 * 系统构建第五步（保存数据库设计文件，解析tablename.xml配置文件）
	 * 
	 * （动态加载ibatis配置文件），这一步展示没有完成，因为spring容器监控ibatis，默认进行动态加载
	 */
	@Override
	public void after(EngineParameter ep) throws Exception {
		Document tableDoc = (Document) ep.getParam("tableDoc");
		//修改状态
		List<Element> handlerTable = tableDoc.selectNodes("//table[@clientState='1' and @state='0']");
		for(int i = 0;i<handlerTable.size();i++){
			handlerTable.get(i).attribute("state").setValue("1");
		}
		//将文件写入本地文件
		File xmlFile = FileUtil.createFile(BuilderUtil.getTableXmlFile());
		FileUtil.writeXml(tableDoc, xmlFile);
		//开始生产操作
		BuilderUtil.startBuilder();
	}
}
