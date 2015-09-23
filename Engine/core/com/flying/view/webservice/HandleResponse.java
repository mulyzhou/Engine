package com.flying.view.webservice;

import com.flying.logging.Log;
import com.flying.logging.LogFactory;

public class HandleResponse {
	private static Log log = LogFactory.getLog(HandleResponse.class);
	
	private IResponseType responseType = null;
	
	public HandleResponse(){
		this(null);
	}
	
	public HandleResponse(String type){
		if(type == null){
			responseType = new JsonResponseType();
		}else if(type.toUpperCase().equals("JSON")){
			responseType = new JsonResponseType();
		}else if(type.toUpperCase().equals("XML")){
			responseType = new XmlResponseType();
		}else{
			log.error("传入了未知的输入类型，无法正常输出");
		}
	}
	
	public String handler(String type,Object obj){
		Object resultObj = responseType.handler(type,obj);
		return resultObj.toString();
	}
}
