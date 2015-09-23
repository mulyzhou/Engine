package com.flying.Interceptor.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.flying.Interceptor.AbstractInterceptor;
import com.flying.service.Engine;
import com.flying.service.EngineParameter;
/**
 * <B>描述：</B>部门处理拦截类<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 *
 */
public class SelectAllOfXmlInterceptor extends AbstractInterceptor {
	@Override
	public void before(EngineParameter ep) throws Exception{
		
		EngineParameter selfEp = new EngineParameter("T_BASE_TABLE.selectAll");
		if(ep.getParam("JC") != null){
			selfEp.putParam("filter", "BMC LIKE '%T_"+ep.getParam("JC")+"%'");
		}
		
		Engine.execute(selfEp);
		
		List<Map> tableList = (List<Map>) selfEp.getResult("data");
		
		Document document = DocumentHelper.createDocument();
		
		Element tablesElement = document.addElement("tables");
		
		for(int i = 0;i < tableList.size() ;i++){
			Element tableElement = tablesElement.addElement("table");
			tableElement.addAttribute("id", tableList.get(i).get("BID").toString());
			tableElement.addAttribute("name", tableList.get(i).get("BMC").toString());
			tableElement.addAttribute("desc", tableList.get(i).get("BZS").toString());
			tableElement.addAttribute("clientState", "1");
			tableElement.addAttribute("state", "1");
			tableElement.addAttribute("x", tableList.get(i).get("X").toString());
			tableElement.addAttribute("y", tableList.get(i).get("Y").toString());
			tableElement.addAttribute("width", tableList.get(i).get("WIDTH").toString());
			tableElement.addAttribute("height", tableList.get(i).get("HEIGHT").toString());
			
			selfEp = new EngineParameter("T_BASE_FIELD.selectByBid");
			selfEp.putParam("BID", tableList.get(i).get("BID").toString());
			Engine.execute(selfEp);
			
			List<Map> fieldList = (List<Map>) selfEp.getResult("data");
			for(int j = 0;j < fieldList.size() ;j++){
				Element fieldElement = tableElement.addElement("column");
				fieldElement.addAttribute("name", fieldList.get(j).get("ZDMC").toString());
				fieldElement.addAttribute("desc", fieldList.get(j).get("ZDZS").toString());
				fieldElement.addAttribute("type", fieldList.get(j).get("ZDLX").toString());
				fieldElement.addAttribute("length", fieldList.get(j).get("ZDCD").toString());
				fieldElement.addAttribute("isNull", "true");
				fieldElement.addAttribute("isPk", fieldList.get(j).get("SFZJ").toString());
				fieldElement.addAttribute("isFk", fieldList.get(j).get("SFWK").toString());
				fieldElement.addAttribute("rountId", "0");
			}
			
		}
		
		ep.putResult("data", document.asXML());
	}
	
	@Override
	public void after(EngineParameter ep){
		
	}
}
