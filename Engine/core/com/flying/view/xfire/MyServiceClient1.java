package com.flying.view.xfire;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.codehaus.xfire.XFire;
import org.codehaus.xfire.XFireFactory;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;


public class MyServiceClient1 {

	public static void main(String[] args) {
		Service serviceModel = new ObjectServiceFactory()
				.create(WsCenter.class);

		XFire xfire = XFireFactory.newInstance().getXFire();
		XFireProxyFactory factory = new XFireProxyFactory(xfire);
		String serviceUrl = "http://127.0.0.1:8080/EngineSecurity/services/WsCenter";

		WsCenter client = null;
		try {
			client = (WsCenter) factory.create(serviceModel, serviceUrl);
		} catch (MalformedURLException e) {
			System.out.println("Client call webservice has exception: " + e.toString());
		}
		
		JSONObject paramJson = new JSONObject();
    	paramJson.put("command", "oracle.selectTableByBmc");
    	Map map = new HashMap();
    	map.put("BMC", "T_SYS_USERINFO");
    	paramJson.put("paramMap", map);
    	System.out.println(paramJson);
    	
    	String returnContent = client.execute(paramJson.toString());		
		
		System.out.println(returnContent);
	}

}