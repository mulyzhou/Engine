package com.flying.builder;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.flying.exception.FlyingException;
import com.flying.init.Item;
import com.flying.init.StaticVariable;
import com.flying.logging.Log;
import com.flying.logging.LogFactory;
import com.flying.service.Engine;
import com.flying.service.EngineParameter;
import com.flying.util.FileUtil;

/**
 * 
 * <B>描述：</B>获取表结构，用于构建ibatis配置文件<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 */
public class IbatisXml {
	private static Log log = LogFactory.getLog(IbatisXml.class);// 日志
	/**
	 * <B>描述：</B>生成前台ibatis配置文件<br/>
	 * 
	 *  1.读取ibatis配置文件的模板文件<br/>
	 *  2.通过获取表中的属性，构建一些特色字符串<br/>
	 *  3.替换模板文件中的通用字符串，构建相应表的配置文件<br/>
	 *  
	 * @param item tablename中的配置项
	 * @throws Exception 
	 */
	public static void insert(Item item) throws Exception {
		log.debug("生成ibatis配置文件开始");

		if("mysql".equals(StaticVariable.DB)){
			EngineParameter ep = new EngineParameter("mysql.selectTableByBmc");
			ep.setCommandType("object");
			ep.putParam("BMC", item.getName());
			Engine.execute(ep);
			
			Object table = ep.getResult("data");//根据表名称，获取表信息
			
			if(table != null && table instanceof Map && ((Map)table).size()>0){//t_base_table中，存在此表，则执行默认操作
				mysqlMode(item);//执行mysql个性方式
				log.debug("通过mysql的元数据，生成ibaits文件");
			}
		}else if("oracle".equals(StaticVariable.DB)){
			EngineParameter ep = new EngineParameter("oracle.selectTableByBmc");
			ep.setCommandType("object");
			ep.putParam("BMC", item.getName());
			Engine.execute(ep);
			
			Object table = ep.getResult("data");//根据表名称，获取表信息
			
			if(table != null && table instanceof Map && ((Map)table).size()>0){//t_base_table中，存在此表，则执行默认操作
				oracleMode(item);//执行oracle个性方式
				log.warn("通过oracle的元数据，生成ibatis文件，未实现！");
			}
		}else{
			throw new FlyingException(StaticVariable.DB + " 暂不支持此数据库！");
		}

		log.debug("生成ibatis配置文件结束");
	}
	/**
	 * 根据表名删除ibatis配置文件
	 * 
	 * @param bmc
	 * @throws FlyingException
	 */
	public static void delete(String bmc) throws FlyingException {
		String fileName = BuilderUtil.getIbatisXmlPath(bmc);

		FileUtil.deleteFile(FileUtil.createFile(fileName));// 删除文件操作

		log.debug("删除名称是" + bmc + ".xml的文件");
	}

	/**
	 * 通用方式，通过自主建立的表，进行生成工作
	 * 
	 * @param item
	 * @throws Exception 
	 */
	private static void commonMode(Item item) throws Exception {
		String tableName = item.getName();// 表名

		String chineseName = item.getAlias();// 表中文名

		String fieldStr = "";// 属性字符串

		String fieldAliasStr = "";// 属性字符串+别名

		String insertFieldStr = "";// 插入属性序列

		String updateFieldStr = "";// 更新属性字符串
		
		String beforeReturnID = "";//再插入前返回主键
		
		String afterReturnID = "";// 在插入之后返回主键
		
		String pk = "";//主键
		
		String selectPk = "";// 查询条件

		String deletePk = "";// 删除条件
		
		String updatePk = "";//修改条件

		EngineParameter ep = null;
		
		if(!StaticVariable.ENABLE_DATATABLE){
			if("oracle".equals(StaticVariable.DB)){
				ep = new EngineParameter("oracle.selectFieldByBmc");
			}else if("mysql".equals(StaticVariable.DB)){
				ep = new EngineParameter("mysql.selectFieldByBmc");
			}
		}else{
			ep = new EngineParameter("T_BASE_FIELD.selectByBmc");
		}
		
		ep.setCommandType("list");
		ep.putParam("BMC", tableName);
		Engine.execute(ep);
		
		//Map resultMap = ep.getResultMap();// 执行
		List<Map> listColumn = ep.getResult("data") == null ? null
				: (List<Map>) ep.getResult("data");// 获取列数据
		
		//在列中，去掉主键限制重复列
		if("oracle".equals(StaticVariable.DB)){
			Map column = null;
			boolean mark = false;
			
			for(int m = 0; m < listColumn.size(); m++){
				column = listColumn.get(m);
				if (column.get("ZDXZ") != null && "P".equals(column.get("ZDXZ").toString())){
					pk = column.get("ZDMC").toString();
					break;
				}
			}
			
			for(int m = 0; m < listColumn.size(); m++){
				column = listColumn.get(m);
				if (column.get("ZDXZ") != null && "C".equals(column.get("ZDXZ").toString()) && pk.equals(column.get("ZDMC").toString())){
					mark = true;
					break;
				}
			}
			
			if(mark){
				listColumn.remove(column);
			}
		}
		

		for (int i = 0; i < listColumn.size(); i++) {// 遍历构造数据结构
			Map column = listColumn.get(i);
			if ((column.get("SFZJ") != null && ("true".equals(column.get("SFZJ").toString())|| "1".equals(column.get("SFZJ").toString()))) 
					|| (column.get("ZDXZ") != null && "P".equals(column.get("ZDXZ").toString())) || (column.get("NULL") != null && column.get("KEY") != null && "PRI".equals(column.get("KEY").toString()))) {
				if ("".equals(selectPk)) {
					pk = column.get("ZDMC").toString();
					
					selectPk += StaticVariable.ALIAS + "."
							+ column.get("ZDMC") + " = #"
							+ column.get("ZDMC") + "#";
					deletePk += column.get("ZDMC") + " = #"
							+ column.get("ZDMC") + "#";
					updatePk = deletePk;
				} else {
					pk += ","+column.get("ZDMC").toString();
					
					selectPk += " AND " + StaticVariable.ALIAS + "."
							+ column.get("ZDMC") + " = #"
							+ column.get("ZDMC") + "#";
					deletePk += " AND " + column.get("ZDMC") + " = #"
							+ column.get("ZDMC") + "#";
					updatePk = deletePk;
				}
				
				// 如果ID自增，无需在插入中添加主键项,在添加时，返回自增ID
				if ((column.get("ZDLX").toString().contains("int") || "NUMBER".equals(column.get("ZDLX").toString())) && "mysql".equals(StaticVariable.DB)) {
					afterReturnID = "<selectKey  resultClass =\"int\"  keyProperty =\""+column.get("ZDMC")+"\" >   "
							+ "<![CDATA[ SELECT LAST_INSERT_ID() AS "+column.get("ZDMC")+"  ]]>     "
							+ "</selectKey >";
				}else if (column.get("ZDLX").toString().toLowerCase().contains("varchar") && "mysql".equals(StaticVariable.DB)) {
					fieldStr += column.get("ZDMC") + ",";
					insertFieldStr += "#" + column.get("ZDMC") + ":VARCHAR#,";
					int len = Integer.parseInt(column.get("ZDLX").toString().substring(column.get("ZDLX").toString().indexOf("(")+1,column.get("ZDLX").toString().indexOf(")")));
					if(len >= 32){
						beforeReturnID = "<selectKey  resultClass =\"string\"  keyProperty =\""+column.get("ZDMC")+"\" >   "
								+ "<![CDATA[ SELECT UPPER(REPLACE(UUID(),'-','')) AS "+column.get("ZDMC")+" ]]>     "
								+ "</selectKey >";
					}else{
						updatePk = column.get("ZDMC") + " = #OLD_"
								+ column.get("ZDMC") + "#";
						updateFieldStr += column.get("ZDMC") + " = #"+ column.get("ZDMC") + ":VARCHAR#,";
						
					}
				}else if((column.get("ZDLX").toString().contains("int") || "NUMBER".equals(column.get("ZDLX").toString())) && "oracle".equals(StaticVariable.DB)){
					String seqStr = tableName.substring(tableName.indexOf("_")+1, tableName.length());
					beforeReturnID = "<selectKey  resultClass =\"int\"  keyProperty =\""+column.get("ZDMC")+"\" >   "
						+ "<![CDATA[ SELECT SEQ_"+seqStr+".NEXTVAL AS "+column.get("ZDMC")+" FROM DUAL ]]>     "
						+ "</selectKey >";
				}else if(column.get("ZDLX").toString().toLowerCase().contains("varchar") && "oracle".equals(StaticVariable.DB)){
					int len = Integer.parseInt(column.get("ZDCD").toString());
					if(len >= 32)
						beforeReturnID = "<selectKey  resultClass =\"string\"  keyProperty =\""+column.get("ZDMC")+"\" >   "
								+ "<![CDATA[ SELECT SYS_GUID() AS "+column.get("ZDMC")+" FROM DUAL ]]>     "
								+ "</selectKey >";
					}else{
						updatePk = column.get("ZDMC") + " = #OLD_"
								+ column.get("ZDMC") + "#";
						updateFieldStr += column.get("ZDMC") + " = #"+ column.get("ZDMC") + ":VARCHAR#,";
					}
			}
			if(column.get("ZDLX").toString().toLowerCase().contains("date") && "mysql".equals(StaticVariable.DB)){
				fieldAliasStr += "DATE_FORMAT(" + StaticVariable.ALIAS + "." + column.get("ZDMC") + ",\"%Y-%m-%d\") " + column.get("ZDMC")+ ",";
			}else if(column.get("ZDLX").toString().toLowerCase().contains("date") && "oracle".equals(StaticVariable.DB)){
				fieldAliasStr += "TO_CHAR(" + StaticVariable.ALIAS + "." + column.get("ZDMC") + ",'YYYY-MM-DD') " + column.get("ZDMC")+ ",";//'YYYY-MM-DD HH24:MI:SS'
			}else{
				fieldAliasStr += StaticVariable.ALIAS + "."+ column.get("ZDMC") + ",";
			}
			
			String zdlx = "";
			if(column.get("ZDLX").toString().contains("int") || "NUMBER".equals(column.get("ZDLX").toString())){
				zdlx = ":INTEGER";
			}else if(column.get("ZDLX").toString().toLowerCase().contains("varchar")){
				zdlx = ":VARCHAR";
			}else if(column.get("ZDLX").toString().toLowerCase().contains("date")){
				zdlx = ":DATE";
			}else if(column.get("ZDLX").toString().contains("boolean")){
				zdlx = ":INTEGER";
			}
			// 更新字符串无需主键
			if("mysql".equals(StaticVariable.DB)){
				if (column.get("SFZJ") != null && !("true".equals(column.get("SFZJ").toString()) || "1".equals(column.get("SFZJ").toString()))) {
					fieldStr += column.get("ZDMC") + ",";
					insertFieldStr += "#" + column.get("ZDMC") + zdlx + "#,";
					updateFieldStr += column.get("ZDMC") + " = #"+ column.get("ZDMC") + zdlx + "#,";
				}
				
				if(column.get("NULL") != null && !(column.get("KEY") != null && "PRI".equals(column.get("KEY").toString()))){
					fieldStr += column.get("ZDMC") + ",";
					insertFieldStr += "#" + column.get("ZDMC") + zdlx + "#,";
					updateFieldStr += column.get("ZDMC") + " = #"+ column.get("ZDMC") + zdlx + "#,";
				}
				
			}else if("oracle".equals(StaticVariable.DB)){
				if (column.get("SFZJ") != null && !("true".equals(column.get("SFZJ").toString()) || "1".equals(column.get("SFZJ").toString()))) {
					updateFieldStr += column.get("ZDMC") + " = #"+ column.get("ZDMC") + zdlx + "#,";
				}
				
				if(column.get("ZDXZ") != null && !"P".equals(column.get("ZDXZ").toString())){
					updateFieldStr += column.get("ZDMC") + " = #"+ column.get("ZDMC") + zdlx + "#,";
				}
				
				fieldStr += column.get("ZDMC") + ",";
				insertFieldStr += "#" + column.get("ZDMC") + zdlx + "#,";
			}
			
		}
		// 去掉最后的逗号
		fieldStr = fieldStr.substring(0, fieldStr.length() - 1);
		fieldAliasStr = fieldAliasStr.substring(0,
				fieldAliasStr.length() - 1);
		insertFieldStr = insertFieldStr.substring(0,
				insertFieldStr.length() - 1);
		updateFieldStr = updateFieldStr.substring(0,
				updateFieldStr.length() - 1);
		// 获取模板文件
		String templatePath = BuilderUtil.getIbatisTemplatePath();
		File templateFile = FileUtil.createFile(templatePath);
		// 将模板文件变成String
		String template ="";
		if(templateFile.exists()){
			template = FileUtil.fileToString(templateFile);
		}else{
			template = FileUtil.streamToString(Thread.currentThread().getContextClassLoader().getResourceAsStream("config/template-" + StaticVariable.DB + ".vm"));
		}
		
		/**
		 * 进行替换 1.提供所有的表名 2.替换属性序列 3.替换所有的主键 4.替换插入序列 5.替换更新序列 6.表中文名称
		 */
		// 替换表名
		template = template.replaceAll("&TABLE&", tableName + " "
				+ StaticVariable.ALIAS);
		// 命名空间
		template = template.replaceAll("&NAMESPACE&", tableName);

		// 替换属性序列
		template = template.replaceAll("&FIELDALIAS&", fieldAliasStr);
		// 替换主键
		template = template.replaceAll("&pk&", pk);
		// 替换查询主键
		template = template.replaceAll("&selectPk&", selectPk);
		// 替换操作删除主键
		template = template.replaceAll("&deletePk&", deletePk);
		// 替换操作修改主键
		template = template.replaceAll("&updatePk&", updatePk);
		// 替换前返回值
		template = template.replaceAll("&beforeReturnID&", beforeReturnID);
		// 替换后返回值
		template = template.replaceAll("&afterReturnID&", afterReturnID);
		// 替换插入序列
		template = template.replaceAll("&FIELD&", fieldStr);
		template = template.replaceAll("&INSERTFIELD&", insertFieldStr);
		// 替换更新序列
		template = template.replaceAll("&UPDATEFIELD&", updateFieldStr);
		// 中文名称
		template = template.replaceAll("&CNNAME&", chineseName);
		// 将修改好的配置文件放入系统
		String fileName = BuilderUtil.getIbatisXmlPath(tableName);
		FileUtil.stringToFile(template, FileUtil.createFile(fileName));
	}

	/**
	 * 通过读取mysql数据的元数据，进行生成工作
	 * 
	 * @param item
	 * @throws Exception 
	 */
	private static void mysqlMode(Item item) throws Exception {
		commonMode(item);
	}
	/**
	 * 通过读取mysql数据的元数据，进行生成工作
	 * 
	 * @param item
	 * @throws Exception
	 */
	private static void oracleMode(Item item) throws Exception {
		commonMode(item);
	}
}
