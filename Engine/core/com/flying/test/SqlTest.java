package com.flying.test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.flying.builder.BuilderUtil;
import com.flying.init.StaticVariable;
import com.flying.service.Engine;
import com.flying.service.EngineParameter;
import com.flying.util.FileUtil;
import com.flying.util.SqlUtil;

public class SqlTest {

	@Before
	public void set() {
		ApplicationContext context = new FileSystemXmlApplicationContext("*/applicationContext*.xml");
		Engine.ac = context;
		
		 /**进行生成工作*/
        try {
			BuilderUtil.startBuilder();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void buildStatics() throws Exception{
		String sqlName = "T_BG_FJ.selectXw";
		String sql = "SELECT A.FJID,A.FJMC,B.XWMC,B.XWDZ,A.FJDZ,A.FJMS FROM T_BG_FJ A ,T_BG_XW B WHERE A.XWID = B.XWID ";
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
		jsonPage.put("tableAction", "common.action?command="+sqlName);
		
		List<Map> btns = new ArrayList<Map>();//模板中的buttons
		List<Map> columns = new ArrayList<Map>();//模板中的columns
		
		for (int i = 0; i < fieldName.size(); i++) {// 遍历构造数据结构
			String _field = fieldName.get(i);
			/**判断是否有别名*/
			if(_field.indexOf("\\.") > 0){//有别名
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
					
					if(mark){ break;}//找此属性则跳出
				}
				
			}
			
		}
		
		Map refreshBtn = new HashMap();//刷新按钮
		refreshBtn.put("btype", "refresh");
		btns.add(refreshBtn);
		
		Map downloadBtn = new HashMap();//下载按钮
		downloadBtn.put("btype", "download");
		btns.add(downloadBtn);
		
		jsonPage.put("btns", btns);//按钮集合
		jsonPage.put("columns", columns);//列集合
		
		// 将修改好的配置文件放入系统
		String fileName = pagePath(sqlName.replaceAll(".", "/"));
		FileUtil.stringToFile(jsonPage.toString(),new File(fileName));
		
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
	
	/**
	 * ibatis配置文件路径，需要修改子项目名称core
	 * @param tableName 表名
	 * @return
	 */
	private String pagePath(String tableName) {
		String fileDir = "";
		String fileName = "";
		
		if(StaticVariable.DEBUG){//获取路径
			fileDir =StaticVariable.PATH + "WebContent/js/biz/"+StaticVariable.MODULE;
		}else{
			fileDir = StaticVariable.PATH +"js/biz/"+StaticVariable.MODULE;//路径
		}
		
		if(tableName.lastIndexOf("/")>0){//判断是否建立文件夹
			String dir = tableName.substring(0,tableName.lastIndexOf("/"));
			File file = new File(fileDir+dir);
			if(!file.isDirectory()){
				file.mkdirs();
			}
		}
		
		fileName = fileDir + tableName +".js";//构建文件
		
		return fileName;
	}
}
