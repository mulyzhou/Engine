package com.flying.Interceptor.impl;

import java.io.File;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;

import com.flying.Interceptor.AbstractInterceptor;
import com.flying.builder.BuilderUtil;
import com.flying.exception.FlyingException;
import com.flying.init.StaticVariable;
import com.flying.service.Engine;
import com.flying.service.EngineParameter;
import com.flying.util.FileUtil;
/**
 * 
 * <B>描述：</B>根据表名删除一张表数据，在删除的过程中，我们需要完成下面的工作<br/>
 * 1.删除表中的数据<br/>
 * 2.删除数据库中物理表<br/>
 * 3.删系统中，生成的文件<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 */
public class DeleteTableInterceptor extends AbstractInterceptor {
	@Override
	public void before(EngineParameter ep) throws Exception {
		String tablesXml = ep.getParam("xml")==null?null: ep.getParam("xml").toString().trim();
		if(tablesXml == null){
			throw new FlyingException("paramMap.xml为空,生成无法进行");
		}else{
			tablesXml = java.net.URLDecoder.decode(tablesXml,"UTF-8");
		}
		//将前台文件，变成dom结构
		Document tableDoc = DocumentHelper.parseText(tablesXml);
		//将文件写入本地文件
		File xmlFile = FileUtil.createFile(BuilderUtil.getTableXmlFile());
		FileUtil.writeXml(tableDoc, xmlFile);
		
		String BMC = ep.getParam("BMC")==null?"":ep.getParam("BMC").toString();
		EngineParameter selfEp = new EngineParameter("T_BASE_FIELD.deleteByBmc");
		selfEp.putParam("BMC", BMC);
		Engine.execute(selfEp);//删除字段
		
		log.debug("删除"+BMC+"的字段");
		
		selfEp = new EngineParameter("T_BASE_FOREIGNKEY.deleteByBmc");
		selfEp.putParam("BMC", BMC);
		Engine.execute(selfEp);//删除外键
		
		log.debug("删除"+BMC+"的外键");
	}

	@Override
	public void after(EngineParameter ep) throws Exception {
		String BMC = ep.getParam("BMC")==null?"":ep.getParam("BMC").toString();
		//删除数据文件
		EngineParameter selfEp = new EngineParameter("init.execute");
		selfEp.setCommandType("update");
		selfEp.putParam("sql", "DROP TABLE "+BMC);
		Engine.execute(selfEp);//删除物理表
		log.debug("从库中删除"+BMC);
		if("oracle".equals(StaticVariable.DB)){
			String seqTableName = "SEQ_" + BMC.substring(BMC.indexOf("_")+1,BMC.length());
			
			String sql = "SELECT COUNT(*) NUM FROM ALL_SEQUENCES WHERE SEQUENCE_NAME = '"+seqTableName.toUpperCase()+"'";
			selfEp = new EngineParameter("init.execute");
			selfEp.setCommandType("object");
			selfEp.putParam("sql", sql);
			Engine.execute(selfEp);
			
			Map numMap = (Map) selfEp.getResult("data");
			
			if(numMap != null && !"0".equals(numMap.get("NUM").toString())){
				selfEp = new EngineParameter("init.execute");
				selfEp.setCommandType("update");
				selfEp.putParam("sql", "DROP sequence "+seqTableName);
				Engine.execute(selfEp);//删除触发器
				log.debug("从库中删除"+seqTableName);
			}
		}
		//删除其他信息
		BuilderUtil.deleteBuilder(BMC);
	}
}
