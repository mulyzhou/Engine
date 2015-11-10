package com.flying.builder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

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
 * <B>描述：</B>生成Ext前台工具类<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 *
 */
public class ExtPage{
	private static Log log = LogFactory.getLog(ExtPage.class);//日志
	/**
	 * 生成Ext前台文件
	 * 
	 * @param item
	 * @throws Exception
	 */
	public static void insert(Item item,String dir) throws Exception{
		log.debug("生成前台js文件开始");

		if("mysql".equals(StaticVariable.DB)){
			String schema = StaticVariable.DB_URL.substring(StaticVariable.DB_URL.lastIndexOf("/")  +1);
			if(schema.contains("?")){
				schema = schema.substring(0,schema.indexOf("?"));
			}
			
			EngineParameter ep = new EngineParameter("mysql.selectTable");
			ep.putParam("filter", "TABLE_TYPE = 'BASE TABLE' AND TABLE_SCHEMA = '"+ schema +"' AND TABLE_NAME = '" + item.getName() + "'");
			ep.setCommandType("object");
			
			Engine.execute(ep);
			
			Object table = ep.getResult("data");//根据表名称，获取表信息
			
			if(table != null && table instanceof Map && ((Map)table).size()>0){//t_base_table中，存在此表，则执行默认操作
				commonMode(item,dir);
				log.debug("通过mysql的元数据，生成前台文件");
			}
		}else if("oracle".equals(StaticVariable.DB)){
			EngineParameter ep = new EngineParameter("oracle.selectTableByBmc");
			ep.setCommandType("object");
			ep.putParam("BMC", item.getName());
			Engine.execute(ep);
			
			Object table = ep.getResult("data");//根据表名称，获取表信息
			
			if(table != null && table instanceof Map && ((Map)table).size()>0){//t_base_table中，存在此表，则执行默认操作
				commonMode(item,dir);
				log.warn("通过oracle的元数据，生成前台文件，未实现！");
			}
		}else{
			throw new FlyingException(StaticVariable.DB + " 暂不支持此数据库！");
		}
		
		log.debug("生成前台js文件结束");
	}
	/**
	 *根据表名删除已经生成的Ext前台文件
	 * 
	 * @param bmc
	 * @throws FlyingException
	 */
	public static void delete(String bmc) throws FlyingException{
		String fileName = BuilderUtil.getPagePath(bmc);
		
		FileUtil.deleteFile(FileUtil.createFile(fileName));//删除文件操作
		
		log.debug("删除名称是"+bmc+".js的文件");
	}
	/**
	 * 通用模式，生成前台文件
	 * 
	 * @param item
	 * @throws Exception
	 */
	private static void commonMode(Item item,String dir) throws Exception{
		
		JSONObject jsonPage = new JSONObject();
		String en = item.getName();
		String cn = item.getAlias();
		
		jsonPage.put("en", en);
		jsonPage.put("cn", cn);
		
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
		ep.putParam("BMC", en);
		Engine.execute(ep);
		
		//Map resultMap = ep.getResultMap();//执行
		
		List<Map> btns = new ArrayList<Map>();//模板中的buttons
		List<Map> columns = new ArrayList<Map>();//模板中的columns
		
		List<Map> listColumn = ep.getResult("data")==null?null:(List<Map>)ep.getResult("data");//获取列数据

		
		//在列中，去掉主键限制重复列
		if("oracle".equals(StaticVariable.DB)){
			Map column = null;
			boolean mark = false;
			String pk = "";
			
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
			Map column = listColumn.get(i);//数据库中的列
			
			columns.add(createColumn(en,column));//列项集合
		}
		Map refreshBtn = new HashMap();//刷新按钮
		refreshBtn.put("btype", "refresh");
		btns.add(refreshBtn);
		
		Map addBtn = new HashMap();//添加按钮
		addBtn.put("btype", "add");
		addBtn.put("securityname", en+"_btnAdd");
		btns.add(addBtn);
		
		Map modifyBtn = new HashMap();//修改按钮
		modifyBtn.put("btype", "modify");
		modifyBtn.put("securityname", en+"_btnModify");
		btns.add(modifyBtn);
		
		Map deleteBtn = new HashMap();//删除按钮
		deleteBtn.put("btype", "delete");
		deleteBtn.put("securityname", en+"_btnDelete");
		btns.add(deleteBtn);
		
		Map downloadBtn = new HashMap();//下载按钮
		downloadBtn.put("btype", "download");
		downloadBtn.put("securityname", en+"_btnDownload");
		btns.add(downloadBtn);
		
		jsonPage.put("btns", btns);//按钮集合
		jsonPage.put("columns", columns);//列集合
		
		if(dir == null){
			// 将修改好的配置文件放入系统
			String fileName = BuilderUtil.getPagePath(en);
			FileUtil.stringToFile(jsonPage.toString(),FileUtil.createFile(fileName));
		}else{
			// 将修改好的配置文件放入系统
			String fileName = dir + "/"+ en + ".js";
			FileUtil.stringToFile(jsonPage.toString(),FileUtil.createFile(fileName));
		}
	}
	
	private static Map createColumn(String bmc,Map column) throws Exception{
		Map col = new HashMap();
		
		col.put("header", column.get("ZDZS"));
		col.put("dataIndex", column.get("ZDMC"));
		
		if(column.get("SFZJ") != null && column.get("SFWK") != null){
			if("true".equals(column.get("SFZJ").toString()) || "1".equals(column.get("SFZJ").toString())){
				col.put("isPk", true);
			}else if("true".equals(column.get("SFWK").toString()) || "1".equals(column.get("SFWK").toString())){
				col.put("allowBlank", false);
			}
		}
		
		if(column.get("MRZ") != null){
			col.put("value", column.get("MRZ"));
		}
		
		EngineParameter ep = null;
		//oracle
		if(column.get("ZDXZ") != null){
			if("P".equals(column.get("ZDXZ").toString())){//是否主键
				col.put("isPk", true);
				if(column.get("ZDLX").toString().toLowerCase().contains("varchar") && Integer.parseInt(column.get("ZDCD").toString()) <= 32){
					col.put("manual", true);
					col.put("allowBlank", false);
				}
			}else if("R".equals(column.get("ZDXZ").toString())){//是否外键
				col.put("allowBlank", false);
				
				ep = new EngineParameter("oracle.selectByZbzd");
				ep.putParam("ZBMC", bmc);
				ep.putParam("ZBZD", column.get("ZDMC"));
				Engine.execute(ep);
				
				List<Map> list = (List) ep.getResult("data");
				
				col.put("xtype", "combo");
				col.put("displayField", list.get(0).get("WBZD"));
				col.put("valueField", list.get(0).get("WBZD"));
				col.put("url", "common.action?command="+list.get(0).get("WBMC").toString().toUpperCase()+".selectAll");
				
				return col;
			}
		}
		//mysql
		if(column.get("NULL") != null){
			if(column.get("KEY") != null && "PRI".equals(column.get("KEY").toString())){//是否主键
				col.put("isPk", true);
				int len = Integer.parseInt(column.get("ZDLX").toString().substring(column.get("ZDLX").toString().indexOf("(")+1,column.get("ZDLX").toString().indexOf(")")));
				if(column.get("ZDLX").toString().toLowerCase().contains("varchar") && len <= 32){
					col.put("manual", true);
					col.put("allowBlank", false);
				}
			}else if(column.get("KEY") != null && "MUL".equals(column.get("KEY").toString())){//是否外键
				col.put("allowBlank", false);
				
				ep = new EngineParameter("mysql.selectByZbzd");
				ep.putParam("ZBMC", bmc);
				ep.putParam("ZBZD", column.get("ZDMC"));
				Engine.execute(ep);
				
				List<Map> list = (List) ep.getResult("data");
				
				col.put("xtype", "combo");
				col.put("displayField", list.get(0).get("WBZD"));
				col.put("valueField", list.get(0).get("WBZD"));
				col.put("url", "common.action?command="+list.get(0).get("WBMC").toString()+".selectAll");
				
				return col;
			}
		}
		
		if(column.get("ZDLX").toString().contains("int") || String.valueOf(column.get("ZDLX")).equals("NUMBER")){
			col.put("xtype", "numberfield");
			if(column.get("ZDCD") != null){
				if(!StaticVariable.ENABLE_DATATABLE && String.valueOf(column.get("ZDLX")).equals("NUMBER")){
					if(column.get("DATA_P") != null && column.get("DATA_S") != null){
						int dataP = Integer.parseInt(column.get("DATA_P").toString());
						int dataS = Integer.parseInt(column.get("DATA_S").toString());
						if(dataS == 0){
							col.put("maxLength", dataP);
							col.put("maxValue", formatDouble(Math.pow(10,dataP)-1,0));
									
						}else{
							col.put("maxLength", dataP+1);
							col.put("maxValue", formatDouble(Math.pow(10,dataP-dataS)-1/Math.pow(10,dataS),dataS));
						}
					}
				}else{
					col.put("maxLength", Integer.parseInt(column.get("ZDCD").toString()));
				}
			}else{
				int len = Integer.parseInt(column.get("ZDLX").toString().substring(column.get("ZDLX").toString().indexOf("(")+1,column.get("ZDLX").toString().indexOf(")")));
				col.put("maxLength", len);
			}
			
		}else if(column.get("ZDLX").toString().toLowerCase().contains("varchar")){
			col.put("xtype", "textfield");
			if(column.get("ZDCD") != null){
				col.put("maxLength", Integer.parseInt(column.get("ZDCD").toString())/2);
			}else{
				int len = Integer.parseInt(column.get("ZDLX").toString().substring(column.get("ZDLX").toString().indexOf("(")+1,column.get("ZDLX").toString().indexOf(")")));
				col.put("maxLength", len);
			}
		}else if(column.get("ZDLX").toString().equals("boolean")){
			col.put("xtype", "combo");
			col.put("displayField", "name");
			col.put("valueField", "id");
			col.put("data",createTrueFalseList());//"[[ true, '是' ],[ false, '否' ]]"
			
		}else if(column.get("ZDLX").toString().toLowerCase().equals("date")){
			col.put("xtype", "datefield");
		}
		
		return col;
	}
	/**
	 * 返回double类型的字符串
	 * @param v
	 * @param s
	 * @return
	 */
	private static String formatDouble(double v, int s)
    {
        String retValue = null;
        DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(0);
        df.setMaximumFractionDigits(s);
        retValue = df.format(v);
        retValue = retValue.replaceAll(",", "");
        return retValue;  
   }
	
	private static List createTrueFalseList(){
		List true_false = new ArrayList();
		List trueList = new ArrayList();
		List falseList = new ArrayList();
		
		trueList.add("是");trueList.add(1);
		falseList.add("否");falseList.add(0);
		
		true_false.add(trueList);
		true_false.add(falseList);
		
		return true_false;
	}
}
