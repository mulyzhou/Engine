package com.flying.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import net.sf.json.JSONObject;
/**
 * 
 * <B>描述：</B>Flying框架提供的特别工具类<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 *
 */
public class FlyingUtil {
	/**
	 * 一级层次的深复制
	 * @param oldMap 被复制的Map
	 * @param newMap 复制到的Map，当key相同时，保留原值。
	 */
	public static void change(Map<String ,Object> oldMap,Map<String,Object> newMap){
		if(oldMap == null || newMap == null){
			return;
		}
		Set<String> oldSet = oldMap.keySet();
		Iterator<String> oldIter = oldSet.iterator();
		while(oldIter.hasNext()){
			String name = oldIter.next();
			//当新map没有这个对象
			if(newMap.get(name)== null){
				newMap.put(name, oldMap.get(name));
			}
		}
	}
	/**
	 * 判断是否为空
	 * 
	 * @param obj
	 * @return false：为空；true：不为空
	 */
	public static boolean validateNull(Object obj){
		boolean remark = false;
		if(obj != null){
			remark = true;
		}
		return remark;
	}
	
	/**
	 * 数据检验函数
	 * 
	 * @param obj
	 * @return false ： 为空或者list,map长度为0；true：不为空，且list，map长度大于0
	 */
	public static boolean validateData(Object obj){
		boolean remark = false;
		if(obj instanceof String){
			if(obj != null && !"".equals(obj)){
				remark = true;
			}
		}else if(obj instanceof Map){
			if(obj != null && ((Map)obj).size()>0){
				remark = true;
			}
		}else if(obj instanceof List){
			if(obj != null && ((List)obj).size()>0){
				remark = true;
			}
		}else{
			if(obj != null){
				remark = true;
			}
		}
		return remark;
	}
	
	public static Map changeJsonObject2HashMap(JSONObject jsonObject){
 		Map map = new HashMap();
 		Iterator<String> keys = jsonObject.keys();
 		while(keys.hasNext()){
 			String key = keys.next();
 			
 			if(jsonObject.get(key) instanceof List){//如果是一个List集合
 				List beforeJsonObject = (List) jsonObject.get(key);
 				List afterJsonObject = new ArrayList();
 				for(int i = 0;i<beforeJsonObject.size();i++){
 					if(beforeJsonObject.get(i) instanceof JSONObject){
 	 					afterJsonObject.add(changeJsonObject2HashMap((JSONObject) beforeJsonObject.get(i)));
 					}else{
 	 					afterJsonObject.add(beforeJsonObject.get(i));
 					}
 				}
 				map.put(key, afterJsonObject);
 			}else if(jsonObject.get(key) instanceof Map){//如果是一个Map集合
 				map.put(key, changeJsonObject2HashMap((JSONObject) jsonObject.get(key)));
 			}else{//其他如简单类型，字符串；不支持对象类型
 				if(jsonObject.get(key) != null && "null".equals(jsonObject.get(key).toString())){//如果是null的字符串，则转换成空对象
 					map.put(key, null);
 				}else{
 					map.put(key, jsonObject.get(key));
 				}
 			}
 		}
 		
 		return map;
 	}
	
	public static String changeMap2JsonString(Map mapObj){
 		Map map = new HashMap();
 		Iterator<String> keys = mapObj.keySet().iterator();
 		while(keys.hasNext()){
 			String key = keys.next();
 			
 			if("session".equals(key)){
 				continue;
 			}else if(mapObj.get(key) instanceof File){
 				continue;
 			}else if(mapObj.get(key) instanceof byte[]){
 				continue;
 			}else{
 				map.put(key, mapObj.get(key));
 			}
 		}
 		
 		return JSONObject.fromObject(map).toString();
 	}
	
	/**
	 * 获取指定长度随机简体中文
	 * 
	 * @param len
	 *            int 长度
	 * @return String
	 */
	public static String getRandomJianHan(int len) {
		String ret = "";
		for (int i = 0; i < len; i++) {
			String str = null;
			int hightPos, lowPos; // 定义高低位
			Random random = new Random();
			hightPos = (176 + Math.abs(random.nextInt(39))); // 获取高位值
			lowPos = (161 + Math.abs(random.nextInt(93))); // 获取低位值
			byte[] b = new byte[2];
			b[0] = (new Integer(hightPos).byteValue());
			b[1] = (new Integer(lowPos).byteValue());
			try {
				str = new String(b, "GBk"); // 转成中文
			} catch (UnsupportedEncodingException ex) {
				ex.printStackTrace();
			}
			ret += str;
		}
		return ret;
	}
	
	public static void main(String[] args){
		Map map1 = new HashMap();
		Map map2 = new HashMap();
		Map map3 = new HashMap();
		
		map1.put("a1", "abc");
		map1.put("b1", 123);
		map1.put("c1", true);
		
		map3.put("a3", "abcd");
		map3.put("b3", 1234);
		map3.put("c3", false);
		
		List list1 = new ArrayList();
		list1.add(1);
		list1.add(2);
		list1.add(3);
		list1.add("a");
		list1.add("b");
		list1.add("c");
		
		map1.put("map3", map3);
		map1.put("list", list1);
		
		change(map1,map2);
		map3.remove("a3");
		System.out.println(map1);
		System.out.println(map2);
	}
}
