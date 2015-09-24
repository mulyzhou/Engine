package com.flying.Interceptor.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;

import com.flying.Interceptor.AbstractInterceptor;
import com.flying.builder.BuilderUtil;
import com.flying.builder.TableNameFile;
import com.flying.exception.FlyingException;
import com.flying.init.StaticVariable;
import com.flying.service.Engine;
import com.flying.service.EngineParameter;
import com.flying.util.FileUtil;
/**
 * 
 * <B>描述：</B>从元数据表中，读取数据，在数据库中创建表<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 *
 */
public class CreateTableInterceptor extends AbstractInterceptor {
	/**
	 * 系统构建第二步
	 */
	@Override
	public void before(EngineParameter ep) throws Exception{
		Document tableDoc = (Document) ep.getParam("tableDoc");
		List<Map> newTableList = new ArrayList<Map>();
		Map table = null;
		List<Map> fields = null;
		//解析表结构
		List tables = tableDoc.selectNodes("/tables/table");
		Iterator iterTable = tables.iterator();
		while (iterTable.hasNext()) {
			Element tableElem = (Element) iterTable.next();
			String state =  tableElem.attributeValue("state").trim();
			String clientState =  tableElem.attributeValue("clientState").trim();
			/**
			 * 根据state的状态，进行编号
			 * state=1 ：可以改变table的位置
			 * state=0
			 * 	clientState = 1：需要生成代码
			 * 	clientState = 0：只需要将表存入数据库
			 */
			//进行业务处理
			if(Integer.parseInt(state) == 0 && Integer.parseInt(clientState) == 1){	
				table = new HashMap();
				fields = new ArrayList<Map>();
				table.put("BMC",tableElem.attributeValue("name").trim().toUpperCase());
				table.put("BZS", tableElem.attributeValue("desc").trim().toUpperCase());
				//前台需要传递到后台，需要生成管理页面
				List fieldsElem = tableElem.selectNodes("column");
				Iterator iterField = fieldsElem.iterator();
				while (iterField.hasNext()) {
					Element field = (Element) iterField.next();
					Map fieldMap = new HashMap();
					fieldMap.put("ZDMC", field.attributeValue("name").trim().toUpperCase());
					fieldMap.put("ZDZS", field.attributeValue("desc").trim().toUpperCase());
					fieldMap.put("ZDLX", field.attributeValue("type").trim());
					fieldMap.put("ZDCD", field.attributeValue("length").trim());
					Boolean isPk = Boolean.parseBoolean(field.attributeValue("isPk").trim());
					if(isPk){
						fieldMap.put("SFZJ",1);
					}else{
						fieldMap.put("SFZJ",0);
					}
					
					Boolean isNull = Boolean.parseBoolean(field.attributeValue("isNull").trim());
					if(isPk){
						fieldMap.put("SFWK",1);
					}else{
						fieldMap.put("SFWK",0);
					}
					fieldMap.put("ZDMC", field.attributeValue("name").trim().toUpperCase());

					fields.add(fieldMap);
				}
				
				/**
				 * 0 如果存在表，先删除
				 * 1 创建表
				 * 2 修改配置文件
				 */
				//创建新表
				try{
					if("mysql".equals(StaticVariable.DB)){
						mysqlCreateTable(table,fields);
					}else if("oracle".equals(StaticVariable.DB)){
						oracleCreateTable(table,fields);
					}
				}catch(Exception e){//创建表出错处理
					for(int i = 0;i < newTableList.size();i++){
						//删除已经创建的表
						EngineParameter selfEp = new EngineParameter("init.execute");
						selfEp.setCommandType("update");
						selfEp.putParam("sql", "DROP TABLE "+newTableList.get(i).get("BMC"));
						
						Engine.execute(selfEp);
						log.debug("删除库中的 " + newTableList.get(i).get("BMC"));
					}
					
					throw new FlyingException("创建 " + table.get("BMC") +" 出错" ,e);
				}
				//修改配置文件
				newTableList.add(table);
			}
		}
		/**
		 * 系统构建第四步（修改tablename.xml配置文件）
		 */
		TableNameFile.generateItem(newTableList,null);
	}
	
	@Override
	public void after(EngineParameter ep) throws Exception{
		
	}
	
	/**
	 * mysql模式，创建表
	 * 
	 * @param table
	 * @param fields
	 * @throws Exception
	 */
	private void mysqlCreateTable(Map table,List<Map> fields) throws Exception{
		//删除现有表
		EngineParameter ep = new EngineParameter("init.execute");
		ep.setCommandType("update");
		ep.putParam("sql", "DROP TABLE IF EXISTS "+table.get("BMC"));
		
		Engine.execute(ep);
		log.debug("删除库中的 " + table.get("BMC"));
		
		//添加新表
		ep = new EngineParameter("init.execute");
		String sql = mysqlCreateSql(table,fields);
		ep.setCommandType("update");
		ep.putParam("sql", sql);
		Engine.execute(ep);
		
		log.debug("创建新的"+table.get("BMC"));
	}
	
	//创建表的sql
	private String mysqlCreateSql(Map table,List<Map> fields){
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
				sql += " int(1)";
			}else if(field.get("ZDLX").equals("date")){
				sql += " datetime";
			}

			if("1".equals(field.get("SFWK").toString())){
				sql += " NOT NULL";
			}else{
				sql += " DEFAULT NULL";
			}
			
			if("1".equals(field.get("SFZJ").toString())){
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
		sql += " PRIMARY KEY ("+pk+")) ";
		if(table.get("BZS") != null && !"".equals(table.get("BZS"))){
			sql +="COMMENT='" + table.get("BZS") + "' ";
		}
		sql += "ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;";
		
		log.debug("建表语句："+sql);
		return sql;
	}
	
	/**
	 * oracle模式，创建表
	 * 
	 * @param table
	 * @param fields
	 * @throws Exception
	 */
	private void oracleCreateTable(Map table,List<Map> fields) throws Exception{
		String tableName = table.get("BMC").toString().trim();
		/*
		 *1.添加序列,如果主键不是Number类型，则不创建序列
		 *2.创建表
		 *3.创建注释
		 */
		EngineParameter ep = null;
		
		for(int i = 0;i<fields.size();i++){
			Map field = fields.get(i);
			if("1".equals(field.get("SFZJ").toString()) && "int".equals(field.get("ZDLX").toString())){
				String seqTableName = "SEQ_" + tableName.substring(tableName.indexOf("_")+1,tableName.length());
				ep = new EngineParameter("init.execute");
				String seqSql = oracleSeq(seqTableName);
				ep.setCommandType("update");
				ep.putParam("sql", seqSql);
				
				Engine.execute(ep);
				log.debug("创建表序列 " + seqTableName);
			}
		}
		
		ep = new EngineParameter("init.execute");
		String tableSql = oracleTable(table,fields);
		ep.setCommandType("update");
		ep.putParam("sql", tableSql);
		
		Engine.execute(ep);
		log.debug("创建表 " + tableName);
		
		ep = new EngineParameter("init.execute");
		String tableComment = "COMMENT ON TABLE "+tableName+" IS '"+table.get("BZS")+"'";
		ep.setCommandType("update");
		ep.putParam("sql", tableComment);
		
		Engine.execute(ep);
		log.debug("创建表注释 " + table.get("BZS"));
		
		for(int i = 0;i<fields.size();i++){
			ep = new EngineParameter("init.execute");
			String filedComment = "COMMENT ON COLUMN "+tableName+"."+fields.get(i).get("ZDMC")+" IS '"+fields.get(i).get("ZDZS")+"'";
			ep.setCommandType("update");
			ep.putParam("sql", filedComment);
			
			Engine.execute(ep);
			log.debug("创建字段注释 " + fields.get(i).get("ZDZS"));
		}
	}
	
	private String oracleSeq(String subTableName)
	{
		String seqSql = "CREATE SEQUENCE " + subTableName + " "+
						"INCREMENT BY 1 "+
						"START WITH 1 "+
						"NOMAXVALUE "+
						"NOMINVALUE "+
						"CACHE 10";
		log.debug("建序列语句："+seqSql);
		return seqSql;
	}
	
	private String oracleTable(Map table,List<Map> fields)
	{
		String sql = "CREATE TABLE "+table.get("BMC")+"  (";
		String pk = "";
		for(int i = 0; i <fields.size();i++){
			Map field = fields.get(i);
			sql += " "+field.get("ZDMC");
			if(field.get("ZDLX").equals("int")){
				String zdcd = field.get("ZDCD")==null?"11":field.get("ZDCD").toString();
				sql += " NUMBER("+zdcd+")";
			}else if(field.get("ZDLX").equals("varchar")){
				String zdcd = field.get("ZDCD")==null?"20":field.get("ZDCD").toString();
				sql += " VARCHAR2("+zdcd+" BYTE)";
			}else if(field.get("ZDLX").equals("boolean")){
				sql += " NUMBER(1)";
			}else if(field.get("ZDLX").equals("date")){
				sql += " date";
			}
		
			if("1".equals(field.get("SFWK").toString())){
				sql += " NOT NULL";
			}
			
			sql += ",";
			
			if("1".equals(field.get("SFZJ").toString())){
				//如果是int，则默认添加自增
				pk += field.get("ZDMC")+",";
			}
		}
		pk = pk.substring(0,pk.length()-1);
		
		sql += " CONSTRAINT PK_"+table.get("BMC")+" PRIMARY KEY ("+pk+")";
		sql +=")";
		
		log.debug("建表语句："+sql);
		return sql;
	}
}
