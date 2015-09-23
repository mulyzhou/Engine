package com.flying.Interceptor.impl;

import java.util.List;
import java.util.Map;

import com.flying.Interceptor.AbstractInterceptor;
import com.flying.service.Engine;
import com.flying.service.EngineParameter;
import com.flying.util.FlyingUtil;

/**
 * <B>描述：</B>子模块导入，创建外键关系<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 *
 */
public class SubImportCreateForeignKeyInterceptor extends AbstractInterceptor {

	@Override
	public void before(EngineParameter ep) throws Exception {
		EngineParameter selfEp = null;
		List<Integer> bids = (List<Integer>) ep.getParam("BIDS");
		Map table = null;
		List<Map> foreignKey = null;

		if(bids == null || bids.size()==0){
			return;
		}
		for(int m=0;m<bids.size();m++){
			//获取表信息
			selfEp = new EngineParameter("T_BASE_TABLE.selectById");
			selfEp.putParam("BID", bids.get(m));
			selfEp.setCommandType("object");
			Engine.execute(selfEp);
			
			table = (Map) selfEp.getResult("data");
			
			//获取字段信息
			selfEp = new EngineParameter("T_BASE_FOREIGNKEY.selectByFromName");
			selfEp.putParam("MC", table.get("BMC"));
			selfEp.setCommandType("list");
			Engine.execute(selfEp);
			
			foreignKey = (List) selfEp.getResult("data");
			
			for(int n=0;n<foreignKey.size();n++){
				Map fk = foreignKey.get(n);
				int state = fk.get("STATE")==null?-1:Integer.parseInt((fk.get("STATE").toString()));
				if(state == 0){
					//创建外键
					String sql = createForeignKey(fk);
					selfEp = new EngineParameter("init.execute");
					selfEp.putParam("sql", sql);
					selfEp.setCommandType("update");
					Engine.execute(selfEp);
					
					selfEp = new EngineParameter("T_BASE_FOREIGNKEY.update");;
					FlyingUtil.change(fk, selfEp.getParamMap());
					selfEp.putParam("STATE", 1);
					selfEp.setCommandType("update");
					Engine.execute(selfEp);
				}
			}
		}
	}

	@Override
	public void after(EngineParameter ep) throws Exception {

	}
	
	//创建表的sql
	public String createForeignKey(Map fk){
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
}
