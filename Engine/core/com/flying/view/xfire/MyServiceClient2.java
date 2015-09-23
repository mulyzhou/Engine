package com.flying.view.xfire;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.codehaus.xfire.client.Client;


public class MyServiceClient2 {

	public static void main(String[] args) {
		Client client = null;
		try {
			client = new Client(new URL(
					"http://127.0.0.1:8080/Engine/services/WsCenter?wsdl"));
			
			JSONObject paramJson = new JSONObject();
	    	paramJson.put("command", "oracle.selectTableByBmc");
	    	Map map = new HashMap();
	    	map.put("BMC", "T_SYS_USERINFO");
	    	paramJson.put("paramMap", map);

			Object[] result1 = client.invoke("execute", new Object[] { paramJson.toString() });
			System.out.println(result1[0]);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}