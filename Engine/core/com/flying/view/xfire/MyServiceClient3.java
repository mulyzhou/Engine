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

import com.flying.service.EngineParameter;
import com.flying.util.MD5;


public class MyServiceClient3 {

	public static void main(String[] args) throws MalformedURLException, Exception {
		
		WsCenterProxy wcp = new WsCenterProxy("http://27.17.26.93:8000/security/services/WsCenter?wsdl");
		
		JSONObject paramJson = new JSONObject();
    	paramJson.put("command", "T_SYS_USERINFO.wslogin");
    	Map map = new HashMap();
    	map.put("LOGIN_NAME", "admin");
    	map.put("PASSWORD", MD5.getMD5("1"));
    	paramJson.put("paramMap", map);	
    	
		System.out.println(wcp.execute(paramJson.toString()));
		
		//EngineParameter selfEp = new EngineParameter("oracle.selectTableByBmc");
		//selfEp.putParam("BMC", "T_SYS_USERINFO");
		
		//wcp.execute(selfEp);
		
		//System.out.println(selfEp.getResultMap());
	}

}