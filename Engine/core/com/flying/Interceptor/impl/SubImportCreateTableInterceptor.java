package com.flying.Interceptor.impl;

import java.util.List;
import java.util.Map;

import com.flying.Interceptor.AbstractInterceptor;
import com.flying.service.Engine;
import com.flying.service.EngineParameter;
/**
 * <B>描述：</B>子模块创建表<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 *
 */
public class SubImportCreateTableInterceptor extends AbstractInterceptor {

	@Override
	public void before(EngineParameter ep) throws Exception {
		String subName = ep.getParam("SUB_NAME").toString().trim();
		
		log.debug("创建对应的数据表");
		EngineParameter selfEp = null;
		List<Integer> bids = (List<Integer>) ep.getParam("BIDS");
		Map table = null;
		List<Map> fields = null;
		
		if(bids == null || bids.size()==0){
			return;
		}
		for(int m=0;m<bids.size();m++){
			//获取表信息
			selfEp =  new EngineParameter("T_BASE_TABLE.selectById");
			selfEp.putParam("BID", bids.get(m));
			selfEp.setCommandType("object");
			Engine.execute(selfEp);
			table = (Map) selfEp.getResult("data");
			
			//获取字段信息
			selfEp =  new EngineParameter("T_BASE_FIELD.selectByBid");
			selfEp.putParam("BID", bids.get(m));
			selfEp.setCommandType("list");
			Engine.execute(selfEp);
			fields = (List) selfEp.getResult("data");
			/**
			 * 0 如果存在表，先删除
			 * 1 创建表
			 * 2 修改配置文件
			 */
			//删除已有表
			selfEp = new EngineParameter("init.execute");
			selfEp.setCommandType("update");
			selfEp.putParam("sql", "DROP TABLE IF EXISTS "+table.get("BMC"));
			selfEp.removeParam("BID");
			Engine.execute(selfEp);
			log.debug("删除库中的"+table.get("BMC"));
			//创建新表
			String sql = createSql(table,fields);
			selfEp.setCommandType("update");
			selfEp.putParam("sql", sql);
			Engine.execute(selfEp);
			log.debug("创建新的"+table.get("BMC"));
		}
	}

	@Override
	public void after(EngineParameter ep) throws Exception {

	}
	
	//创建表的sql
	private String createSql(Map table,List<Map> fields){
		String sql = "CREATE TABLE "+table.get("BMC")+" (";
		String pk = "";
		for(int i =0;i<fields.size();i++){
			Map field = fields.get(i);
			sql += " "+field.get("ZDMC");//+field.get("ZDLX")+"("+field.get("ZDCD")+")"+
			if(field.get("ZDLX").equals("int")){
				String zdcd = field.get("ZDCD")==null?"11":field.get("ZDCD").toString();
				sql += " "+field.get("ZDLX")+"("+zdcd+")";
			}else if(field.get("ZDLX").equals("varchar")){
				String zdcd = field.get("ZDCD")==null?"20":field.get("ZDCD").toString();
				sql += " "+field.get("ZDLX")+"("+zdcd+") COLLATE utf8_bin";
			}else if(field.get("ZDLX").equals("boolean")){
				sql += " tinyint(1)";
			}else if(field.get("ZDLX").equals("date")){
				sql += " datetime";
			}
		
			if((Boolean)field.get("SFWK")){
				sql += " NOT NULL";
			}else{
				sql += " DEFAULT NULL";
			}
			
			if((Boolean)field.get("SFZJ")){
				//如果是int，则默认添加自增
				if(field.get("ZDLX").equals("int")){
					sql += " AUTO_INCREMENT";
				}
				pk += field.get("ZDMC")+",";
			}
			
			sql += " COMMENT '"+field.get("ZDZS")+"',";
		}
		pk = pk.substring(0,pk.length()-1);
		//如果是联合主键，则去掉主键自增属性
		if(pk.indexOf(",")>0){
			sql = sql.replaceAll(" AUTO_INCREMENT", "");
		}
		sql += " PRIMARY KEY ("+pk+")";
		sql +=") ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;";
		
		log.debug("建表语句："+sql);
		return sql;
	}
}
