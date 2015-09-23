package com.flying.Interceptor.impl;

import java.util.Map;

import com.flying.Interceptor.AbstractInterceptor;
import com.flying.builder.BuilderUtil;
import com.flying.exception.FlyingException;
import com.flying.init.StaticVariable;
import com.flying.service.Engine;
import com.flying.service.EngineParameter;
import com.flying.util.FileUtil;
/**
 * 
 * <B>描述：</B>删除一个报表处理类<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 *
 */
public class DeleteStaticsInterceptor extends AbstractInterceptor {

	@Override
	public void before(EngineParameter ep) throws Exception {
		String BBID = ep.getParam("BBID") == null?"":ep.getParam("BBID").toString().trim();
		
		if("".equals(BBID)) {//为空则跳出程序
			throw new FlyingException("报表ID为空，无法执行删除操作");
		}
		
		EngineParameter selfEp = new EngineParameter("T_BASE_STATICS.selectById");//删除重复名称的记录
		selfEp.putParam("BBID", BBID);
		Engine.execute(selfEp);
		
		Map resultObject = (Map)selfEp.getResult("data");
		
		String sqlid = resultObject.get("SQLID") == null?"":resultObject.get("SQLID").toString().trim();
		
		// 将删除配置文件放入系统
		String fileName = BuilderUtil.getPagePath(sqlid.replaceAll("\\.", "/"));
		FileUtil.deleteFile(FileUtil.createFile(fileName));
		log.debug("删除文件:"+fileName);
		//从数据库里面删除菜单
		String src = "biz/"+StaticVariable.MODULE+"/"+sqlid.replaceAll("\\.", "/")+".js";
		
		selfEp = new EngineParameter("T_BASE_MENU.deleteBySrc");
		selfEp.putParam("SRC", src);
		log.debug("删除此报表的菜单信息");
		
		Engine.execute(selfEp);
	}

	@Override
	public void after(EngineParameter ep) throws Exception {
	}
}
