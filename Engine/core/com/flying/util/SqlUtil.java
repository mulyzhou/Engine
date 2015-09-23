package com.flying.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * <B>描述：</B>sql语句解析工具类<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 *
 */
public class SqlUtil {
	
	//private final static String SQL = "SELECT A.XS_ID,A.XSXM,XB,B.ZZMMMC,C.MZMC,A.DZ FROM T_XG_STUDENT A INNER JOIN T_BASE_ZZMM B ON A.ZZMM = B.ZZMM RIGHT JOIN T_BASE_MZ C ON A.MZ = C.MZ";
	private final static String SQL = "SELECT A.MZ,A.MZMC FROM T_BASE_MZ A LIMIT #start#,#limit#";
	//private final static String SQL = "SELECT A.XS_ID,A.XSXM,XB,B.ZZMMMC,A.MZ,A.DZ FROM T_XG_STUDENT A INNER JOIN T_BASE_ZZMM B ON A.ZZMM = B.ZZMM RIGHT JOIN T_BASE_MZ C ON A.MZ = C.MZ left join    t_base_xb d   on  a.xb = d.xb   ";
	/**
	 * 解析sql，eg：SELECT CONFIG_ID,CONFIG_NAME FROM T_BASE_CONFIG WHERE CONFIG_ID = #id#
	 * 
	 * @param sql
	 */
	public static synchronized Map resolveListSql(String sql){
		//定义返回值
		Map result = new HashMap();
		//表名
		List<String> tableName = new ArrayList<String>();
		//表别名
		Map<String,String>tableAliasName = new HashMap<String, String>();
		//字段名
		List<String> fieldName = new ArrayList<String>();
		//大写格式化
		sql = sql.toUpperCase();
		//去多空格
		sql = sql.replaceAll("( )+", " ");
		//字段解析
		String  fieldArea = sql.substring(0,sql.indexOf(" FROM "));
		fieldArea = fieldArea.replace("SELECT ", "");
		String[] fields = fieldArea.split(",");
		for(int i =0 ;i<fields.length;i++){
			fieldName.add(fields[i].trim());
		}
		
		/**表解析*/
		//表名区域，FROM WHERE之间
		String tableArea = sql;
		if(tableArea.indexOf(" LIMIT ") != -1){
			tableArea = tableArea.substring(0,tableArea.indexOf(" LIMIT "));
		}
		if(tableArea.indexOf(" GROUP ") != -1){
			tableArea = tableArea.substring(0,tableArea.indexOf(" GROUP "));
		}
		if(tableArea.indexOf(" WHERE ") == -1){
			tableArea = tableArea.substring(tableArea.indexOf(" FROM ")+6);
		}else{
			tableArea = tableArea.substring(tableArea.indexOf(" FROM ")+6,sql.indexOf(" WHERE "));
		}
		//如果查询多表，通过","进行分割多表
		String[] tables = tableArea.split(",");
		for(int i =0 ;i<tables.length;i++){
			//[表名 别名][表名 别名][表名 别名]
			String[] tablesChild = null;
			//[表名][别名]
			String[] tableAlias = null;
			//对Left Join进行解析
			//第一个Left的位置
			int firstLeftLocation = tables[i].indexOf(" LEFT JOIN ");
			if(firstLeftLocation != -1){
				tablesChild = tables[i].split(" LEFT JOIN ");
				//Left连接的表
				for(int a=1;a<tablesChild.length;a++){
					tableAlias = tablesChild[a].substring(0,tablesChild[a].indexOf(" ON ")).trim().split(" ");
					tableName.add(tableAlias[0].trim());
					tableAliasName.put(tableAlias[1].trim(),tableAlias[0].trim());
				}
			}
			//对Right Join进行解析
			//第一个Right的位置
			int firstRightLocation = tables[i].indexOf(" RIGHT JOIN ");
			if(firstRightLocation != -1){
				tablesChild = tables[i].split(" RIGHT JOIN ");
				//Right连接的表
				for(int b=1;b<tablesChild.length;b++){
					tableAlias = tablesChild[b].substring(0,tablesChild[b].indexOf(" ON ")).trim().split(" ");
					tableName.add(tableAlias[0].trim());
					tableAliasName.put(tableAlias[1].trim(),tableAlias[0].trim());
				}
			}
			//对Inner Join进行解析
			//第一个Inner的位置
			int firstInnerLocation = tables[i].indexOf(" INNER JOIN ");
			if(firstInnerLocation != -1){
				tablesChild = tables[i].split(" INNER JOIN ");
				//Inner连接的表
				for(int c=1;c<tablesChild.length;c++){
					tableAlias = tablesChild[c].substring(0,tablesChild[c].indexOf(" ON ")).trim().split(" ");
					tableName.add(tableAlias[0].trim());
					tableAliasName.put(tableAlias[1].trim(),tableAlias[0].trim());
				}
			}
			//获取第一张表
			if(firstLeftLocation == -1 && firstRightLocation == -1 && firstInnerLocation == -1){
				if(tables[i].trim().indexOf(" ") == -1){
					tableName.add(tables[i].trim());
					tableAliasName.put(tables[i].trim(),tables[i].trim());
				}else{
					tableAlias = tables[i].trim().split(" ");
					tableName.add(tableAlias[0].trim());
					tableAliasName.put(tableAlias[1].trim(),tableAlias[0].trim());
				}
			}else{
				int minValue = getMinNum(firstLeftLocation,firstRightLocation,firstInnerLocation);
				//第一张表
				tableAlias = tables[i].substring(0,minValue).trim().split(" ");
				tableName.add(tableAlias[0].trim());
				tableAliasName.put(tableAlias[1].trim(),tableAlias[0].trim());
			}
			
		}
		//设置到结果
		result.put("fieldName", fieldName);
		result.put("tableAliasName", tableAliasName);
		result.put("tableName", tableName);
		//返回
		return result;
	}
	/**
	 * 获取三个数中，不是-1的最小数
	 * 
	 * @param firstNum
	 * @param secondNum
	 * @param thirdNum
	 * @return
	 */
	private static int getMinNum(int firstNum,int secondNum,int thirdNum){
		int minValue = 0;
		if(firstNum >0){
			minValue = firstNum;
			if(0<secondNum && secondNum<minValue) minValue = secondNum;
			if(0<thirdNum && thirdNum<minValue) minValue = thirdNum;
		}
		if(secondNum >0){
			minValue = secondNum;
			if(0<firstNum && firstNum<minValue) minValue = firstNum;
			if(0<thirdNum && thirdNum<minValue) minValue = thirdNum;
		}
		if(thirdNum >0){
			minValue = thirdNum;
			if(0<secondNum && secondNum<minValue) minValue = secondNum;
			if(0<firstNum && firstNum<minValue) minValue = firstNum;
		}
		return minValue;
	}
	
	public static void main(String[] args){
		SqlUtil.resolveListSql(SQL);
	}
}