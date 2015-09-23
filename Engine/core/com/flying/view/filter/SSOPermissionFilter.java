package com.flying.view.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.flying.logging.Log;
import com.flying.logging.LogFactory;
import com.flying.service.Engine;
import com.flying.service.EngineParameter;
import com.flying.view.servlet.CommonResponse;

public class SSOPermissionFilter  implements Filter{
	private static Log log = LogFactory.getLog(SSOPermissionFilter.class);//日志
	
	private static Map<String,String> loginPersons = new HashMap<String,String>();
	@Override
	public void init(FilterConfig fConfig) throws ServletException {
		
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;
		
    	//单点登录验证标示
    	String ssoUuid = req.getParameter("SSO_UUID_IENC_CN");
    	if(ssoUuid != null && !"".equals(ssoUuid)){
    		String personUuid = loginPersons.get(ssoUuid);
    		if(personUuid != null && !"".equals(personUuid)){
    			CommonResponse.responseJson(res, true, personUuid);
    		}else{
    			CommonResponse.responseJson(res, false, "未知的UUID");//进行跳转
    		}
    		return;
    	}
    	
    	//单点登录
    	String goback = req.getParameter("SSO_GOBACK_IENC_CN");
    	String command = req.getParameter("command");
    	if(goback != null && !"".equals(goback) && "T_SYS_USERINFO.login".equals(command)){
    		HttpSession session = req.getSession();
    		EngineParameter selfEp = new EngineParameter("T_SYS_USERINFO.login");
    		selfEp.putParam("LOGIN_NAME", req.getParameter("LOGIN_NAME"));
    		selfEp.putParam("PASSWORD", req.getParameter("PASSWORD"));
    		selfEp.putParam("session", session);
    		try {
				Engine.execute(selfEp);
			} catch (Exception e) {
				e.printStackTrace();
				CommonResponse.responseJson(res, false, e.getMessage());
				return;
			}
    		
    		String uuid = UUID.randomUUID().toString();
			Map resultObject = (Map)selfEp.getResult("data");
			
			loginPersons.put(uuid,resultObject.get("PERSON_UUID").toString());
			
			res.setStatus(CommonResponse.FLYING_PAGE_REDIRECT);
			String url = goback + "?SSO_UUID_IENC_CN="+uuid;
			CommonResponse.responseJson(res, false, url);
    	}
    	//过滤器向下传递
    	chain.doFilter(req, res);
	}
	
	@Override
	public void destroy() {
		
	}

}
