package com.flying.Interceptor.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.flying.Interceptor.AbstractInterceptor;
import com.flying.builder.BuilderUtil;
import com.flying.exception.FlyingException;
import com.flying.init.StaticVariable;
import com.flying.service.Engine;
import com.flying.service.EngineParameter;
import com.flying.util.FileUtil;
import com.flying.util.SqlUtil;
/**
 * <B>描述：</B>创建报表类<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 *
 */
public class CreateStaticsInterceptor extends AbstractInterceptor {

	@Override
	public void before(EngineParameter ep) throws Exception {
		String BBID = ep.getParam("BBID") == null?"":ep.getParam("BBID").toString().trim();
		String sqlName = ep.getParam("BBMC") == null?"":ep.getParam("BBMC").toString().trim();
		String sqlid = ep.getParam("SQLID") == null?"":ep.getParam("SQLID").toString().trim();
		String sql = ep.getParam("BBJY") == null?"":ep.getParam("BBJY").toString().toUpperCase().trim();
		
		ep.putParam("BBMC", sqlName);//去前后空格
		ep.putParam("SQLID", sqlid);//大写，去前后空格
		ep.putParam("BBJY", sql);//大写，去前后空格
		
		EngineParameter selfEp = null;//删除重复名称的记录
		if(!"".equals(BBID)) {
			log.debug("报表ID非空，则为修改状态，则不执行下面根据sqlid删除同名报表操作。");
			selfEp = new EngineParameter("T_BASE_MENU.deleteBySrc");
			selfEp.putParam("SRC", "biz/"+StaticVariable.MODULE+"/"+sqlid.replaceAll("\\.", "/")+".js");
			Engine.execute(selfEp);
		}else{
			log.debug("报表ID为空，则为添加状态，需要执行下面根据sqlid删除同名报表操作。");
			selfEp = new EngineParameter("T_BASE_STATICS.deleteBySqlid");
			selfEp.putParam("SQLID", sqlid);
			Engine.execute(selfEp);
		}
	}

	@Override
	public void after(EngineParameter ep) throws Exception {
		String sqlName = ep.getParam("BBMC") == null?"":ep.getParam("BBMC").toString();
		String sqlid = ep.getParam("SQLID") == null?"":ep.getParam("SQLID").toString();
		String sql = ep.getParam("BBJY") == null?"":ep.getParam("BBJY").toString();
		
		if("".equals(sqlid) || "".equals(sql)){//如有一项为空则退出
			throw new FlyingException("‘报表sqlid’ 或者 ‘报表执行sql’ 有一项为空，错误数据。");
		}
		
		// 解析sql
		Map resolveResult = SqlUtil.resolveListSql(sql);
		// 列集合
		List<String> fieldName = (List<String>) resolveResult.get("fieldName");
		// 表集合
		List<String> tableName = (List<String>) resolveResult.get("tableName");
		// 别名-表名
		Map<String, String> tableAliasName = (Map<String, String>) resolveResult.get("tableAliasName");
		// 定义 表名-列集合
		Map<String, List> columnMap = new HashMap<String, List>();
		// 构造 表名-列集合
		for (int i = 0; i < tableName.size(); i++) {
			columnMap.put(tableName.get(i),getColumnList(tableName.get(i)));
		}
		
		JSONObject jsonPage = new JSONObject();
		jsonPage.put("cn",sqlName);
		jsonPage.put("tableAction", "common.action?command=T_BASE_STATICS.selectBySqlid&SQLID="+sqlid);
		
		List<Map> btns = new ArrayList<Map>();//模板中的buttons
		List<Map> columns = new ArrayList<Map>();//模板中的columns
		
		for (int i = 0; i < fieldName.size(); i++) {// 遍历构造数据结构
			String _field = fieldName.get(i);
			/**判断是否有别名*/
			if(_field.indexOf(".") > 0){//有别名
				String[] _fields = _field.split("\\.");
				
				String _tableName = tableAliasName.get(_fields[0]);
				
				List<Map> _tableNameFields = columnMap.get(_tableName);
				
				for(int m =0;m<_tableNameFields.size();m++){
					if(_fields[1].equals(_tableNameFields.get(m).get("ZDMC"))){
						columns.add(createColumn(_tableNameFields.get(m)));//列项集合
					}
				}
			}else{//无别名
				for(int n = 0; n < tableName.size();n++){
					boolean mark = false;
					List<Map> _tableNameFields = columnMap.get(tableName.get(n));
					
					for(int m =0;m<_tableNameFields.size();m++){
						if(_field.equals(_tableNameFields.get(m).get("ZDMC"))){
							mark = true;
							columns.add(createColumn(_tableNameFields.get(m)));//列项集合
						}
					}
					
					if(mark){ break;}//此属性则跳出
				}
			}
		}
		
		Map refreshBtn = new HashMap();//刷新按钮
		refreshBtn.put("btype", "refresh");
		btns.add(refreshBtn);
		
		Map downloadBtn = new HashMap();//下载按钮
		downloadBtn.put("btype", "download");
		//btns.add(downloadBtn);
		
		jsonPage.put("btns", btns);//按钮集合
		jsonPage.put("columns", columns);//列集合
		
		// 将修改好的配置文件放入系统
		String fileName = BuilderUtil.getPagePath(sqlid.replaceAll("\\.", "/"));
		FileUtil.stringToFile(jsonPage.toString(),FileUtil.createFile(fileName));
		log.debug("生成js文件:"+jsonPage.toString());
		//存入数据库
		EngineParameter selfEp = new EngineParameter("T_BASE_MENU.insert");
		selfEp.putParam("CJ", 2);
		selfEp.putParam("PID", 105);
		selfEp.putParam("TEXT", sqlName);
		selfEp.putParam("TYPE", "permission");
		selfEp.putParam("ICON", "img/icon/splash_pink.png");
		selfEp.putParam("SRC", "biz/"+StaticVariable.MODULE+"/"+sqlid.replaceAll("\\.", "/")+".js");
		selfEp.putParam("EXPANDED", "1");
		selfEp.putParam("CACHE", "1");
		selfEp.putParam("LEAF", "1");
		selfEp.putParam("DESKTOP", "1");
		
		Engine.execute(selfEp);
		log.debug("添加一项报表菜单");
	}

	/**
	 * 获取某张表（tablename）的列列表
	 * 
	 * @param tableName
	 * @return List<Column>
	 * @throws Exception 
	 */
	private List getColumnList(String tableName) throws Exception {
		// 定义列属性
		EngineParameter ep = new EngineParameter("T_BASE_FIELD.selectByBmc");
		ep.putParam("BMC", tableName);
		Engine.execute(ep);
		
		List listColumn = (List)ep.getResult("data");
			
		return listColumn;
	}
	/**
	 * 创建列对象
	 * 
	 * @param column
	 * @return
	 */
	private Map createColumn(Map column){
		Map col = new HashMap();
		
		col.put("header", column.get("ZDZS"));
		col.put("dataIndex", column.get("ZDMC"));
		
		if("true".equals(column.get("SFZJ").toString()) || "1".equals(column.get("SFZJ").toString())){
			col.put("isPk", true);
		}else if("true".equals(column.get("SFWK").toString()) || "1".equals(column.get("SFWK").toString())){
			col.put("allowBlank", false);
		}
		
		return col;
	}
}
