package com.flying.view.xfire;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.codehaus.xfire.client.Client;

import com.flying.service.EngineParameter;
import com.flying.util.FlyingUtil;
/**
 * webservice代理方法
 * 
 * @author zdf
 *
 */
public class WsCenterProxy {
	//webservice地址
	private String url = "";
	
	public WsCenterProxy(){
		this.url = "http://127.0.0.1:8080/Engine/services/WsCenter?wsdl";
	}
	
	public WsCenterProxy(String url){
		this.url = url;
	}
	
	/**
	 * 执行webservce方法
	 * 
	 * @param paramJson 必须含有command，paramMap参数
	 * @return
	 * @throws MalformedURLException
	 * @throws Exception
	 */
	public String execute(String paramJson) throws MalformedURLException, Exception{
		Client client = new Client(new URL(url));
    	
		Object[] resultObj = client.invoke("execute", new Object[] { paramJson });
		
		return resultObj[0].toString();
	}
	
	/**
	 * 通过EngineParameter执行webservice方法
	 * 
	 * @param ep
	 * @throws MalformedURLException
	 * @throws Exception
	 */
	public void execute(EngineParameter ep) throws MalformedURLException, Exception{
		
		Client client = new Client(new URL(url));
    	
		JSONObject paramJson = new JSONObject();
    	paramJson.put("command", ep.getCommand());
    	paramJson.put("paramMap", ep.getParamMap());
    	
		Object[] resultObj = client.invoke("execute", new Object[] { paramJson.toString() });
		
		JSONObject resultJsonObj = JSONObject.fromObject(resultObj[0].toString());
		
		ep.setResultMap(FlyingUtil.changeJsonObject2HashMap(resultJsonObj));
	}
}
