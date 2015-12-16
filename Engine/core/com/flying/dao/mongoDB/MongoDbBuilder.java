package com.flying.dao.mongoDB;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.flying.dao.BaseDAO;
import com.flying.logging.Log;
import com.flying.logging.LogFactory;
import com.flying.util.FileUtil;

public class MongoDbBuilder {
	private static Log log = LogFactory.getLog(MongoDbBuilder.class);//日志

	public static Map BO = new HashMap();
	
	public static void builder(){
		String objName = "T_BASE_LOG";
		InputStream in;// 文件输入流
		try {
			in = Thread.currentThread().getContextClassLoader().getResourceAsStream("sql/core/mongodb/"+ objName +".json");// 将文件编程输入流
			//T_BASE_LOG对象
			JSONObject jsonObj = JSONObject.fromObject(FileUtil.streamToString(in));
			//T_BASE_LOG里面的selectAll，update，insert，delete对象
			Iterator jsonIter = jsonObj.keys();
			while(jsonIter.hasNext()){
				//selectAll ： {}
				String jsonKey = (String) jsonIter.next();
				Object keyObj = jsonObj.get(jsonKey);
				
				if(keyObj instanceof String && Pattern.matches("^\\{([\"|']{0,1}[\\w]+[\"|']{0,1}:[\"|']{0,1}[\\w]+[\"|']{0,1},{0,1})+\\}", ((String)keyObj).trim())){
					BO.put(objName + "." + jsonKey + ".condition", keyObj);
				}else if(keyObj instanceof JSONObject){
					/** mongo的condition,sort、skip、limit、hint、count 开始 */
					String[] keywords = new String[]{"condition","field","sort","skip","limit","hint","count","distinct"};
					for(int i=0;i<keywords.length;i++){
						Object subObj = ((JSONObject)keyObj).get(keywords[i]);
						if(subObj != null){
							String resultStr = analysisParam(keywords[i],subObj);
							if(resultStr != null)
								BO.put(objName + "." + jsonKey + "." + keywords[i], resultStr);
						}
					}
				}else{
					log.warn(jsonKey + " 参数无法识别");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 分析传入参数
	 * 正则规则：
	 * {"abc" : 234}
	 * ^\\{[\"|']{0,1}[\\w]+[\"|']{0,1}:[0-9]+\\}
	 * 
	 * {abc:13432,cdf:34,2324:343}
	 * ^\\{([\"|']{0,1}[\\w]+[\"|']{0,1}:[0-9]+,{0,1})+\\}+
	 * 
	 *  {"abc":13df432,cdf:"df34",2324:df343}
	 * ^\\{([\"|']{0,1}[\\w]+[\"|']{0,1}:[\"|']{0,1}[\\w]+[\"|']{0,1},{0,1})+\\}
	 * 
	 * [abc,cd23,"bced",3]
	 * ^\\[([\"|']{0,1}[\\w]+[\"|']{0,1},{0,1})+\\]$
	 * 
	 * @param paramName
	 * @param analysisObj
	 * @return
	 */
	private static String analysisParam(String paramName,Object analysisObj){
		String analysisStr = null;
		
		String regeNum = "^[\\d]+$";//123
		String regeStr = "^[\\w]+$";//abc123_
		String regeSingleJson = "^\\{[\"|']{0,1}[\\w]+[\"|']{0,1}:[0-9]+\\}$";//{"abc" : 234}
		String regeJson = "^\\{([\"|']{0,1}[\\w]+[\"|']{0,1}:[\"|']{0,1}([\\w]+|#)[\"|']{0,1},{0,1})+\\}$";//{"abc":13df432,cdf:"df34",2324:df343}
		
		if(Pattern.matches("^(limit)|(skip)$",paramName)){
			if(analysisObj instanceof Integer || (analysisObj instanceof String && Pattern.matches(regeNum, ((String)analysisObj).trim()))){
				analysisStr = String.valueOf(analysisObj);
			}else{
				log.warn(paramName + "参数：" + analysisObj + "不符合要求");
			}
		}
		
		if(Pattern.matches("^(sort)|(hint)$",paramName)){
			if(analysisObj instanceof String){
				if(Pattern.matches(regeStr, ((String)analysisObj).trim())){
					analysisStr = "{" + ((String)analysisObj) + ": 1}";
				}else if(Pattern.matches(regeSingleJson, ((String)analysisObj).trim())){
					analysisStr = ((String)analysisObj).trim();
				}else{
					log.warn(paramName + "参数：" + analysisObj + "不符合要求");
				}
			}else if(analysisObj instanceof JSONObject){
				analysisStr = ((JSONObject)analysisObj).toString();
			}
		}
		
		if(Pattern.matches("^(condition)|(count)|(field)$",paramName)){
			if(analysisObj instanceof String && (Pattern.matches(regeJson, ((String)analysisObj).trim()) || "_id".equals(analysisObj))){
				analysisStr = ((String)analysisObj).trim();
			}else if(analysisObj instanceof JSONObject){
				analysisStr = ((JSONObject)analysisObj).toString();
			}else if(analysisObj instanceof JSONArray){
				analysisStr = ((JSONArray)analysisObj).toString();
			}else{
				log.warn(paramName + "参数：" + analysisObj + "不符合要求");
			}
		}
		
		if(Pattern.matches("^(distinct)$",paramName)){
			if(analysisObj instanceof String && Pattern.matches(regeStr, ((String)analysisObj).trim())){
				analysisStr = ((String)analysisObj).trim();
			}else{
				log.warn(paramName + "参数：" + analysisObj + "不符合要求");
			}
		}
		return analysisStr.replaceAll(" ", "");
	}
	public static void main(String[] args) {
		builder();
		System.out.println(BO);
		//System.out.println(Pattern.matches("^\\[([\"|']{0,1}[\\w]+[\"|']{0,1},{0,1})+\\]$","[\"abc]"));
	}
}
