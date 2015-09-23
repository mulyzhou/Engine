package com.flying.view.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import com.flying.init.StaticVariable;
import com.flying.logging.Log;
import com.flying.logging.LogFactory;
import com.flying.service.Engine;
import com.flying.service.EngineParameter;

public class SSOFilter  implements Filter{
	private static Log log = LogFactory.getLog(SSOFilter.class);//日志
	
	private FilterConfig filterConfig = null;
	
	private String SSOLoginPage = "http://localhost:8081/EngineSSO/login.html";
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		
		if (filterConfig != null) {
			log.info("SSOFilter:Initializing filter");
		}
		
		this.SSOLoginPage = filterConfig.getInitParameter("SSOLoginPage");
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;
		//进行单点登录开关
		if(StaticVariable.SSO_AUTH){
			//获取用户权限信息
	    	HttpSession session = req.getSession(false);
	    	//单点登录标示
	    	String uuid = req.getParameter("SSO_UUID_IENC_CN");
	    	if(session == null){
	    		if(uuid == null || "".equals(uuid)){
	    			log.debug("此用户未登陆！");
	    			String goback = req.getScheme()+"://"+req.getServerName()+":"+req.getServerPort()+req.getContextPath()+"/desktop.html";
	    			String url = this.SSOLoginPage + "?SSO_GOBACK_IENC_CN="+goback;
	    			res.sendRedirect(url);
	    			return;
	        	}else{
	    			log.debug("检验！");
	    			String authUrl = this.SSOLoginPage + "?SSO_UUID_IENC_CN=" + uuid;
	    			HttpClient httpclient = new HttpClient();
	    			GetMethod httpget = new GetMethod(authUrl);
	    			try {
	    				httpclient.executeMethod(httpget);
	    				String resultStr = httpget.getResponseBodyAsString();
	    				JSONObject resultJson = JSONObject.fromObject(resultStr);
	    				if(resultJson.getBoolean("success")){
	    					String personUuid = resultJson.getString("msg");
	    					session = req.getSession();
	    					EngineParameter selfEp = new EngineParameter("T_SYS_USERINFO.selectById");
	    					selfEp.putParam("USER_ID", personUuid);
	    					Engine.execute(selfEp);
	    					Map resultObject = (Map)selfEp.getResult("data");
	    					session.setAttribute("USERINFO", resultObject);
	    				}else{
	    					String goback = req.getScheme()+"://"+req.getServerName()+":"+req.getServerPort()+req.getContextPath()+"/desktop.html";
	    	    			String url = this.SSOLoginPage + "?SSO_GOBACK_IENC_CN="+goback;
	    	    			res.sendRedirect(url);
	    	    			return;
	    				}
	    			} catch (Exception e) {
						e.printStackTrace();
					} finally {
	    				httpget.releaseConnection();
	    			}
	        	}
	    	}
		}
		
    	//过滤器向下传递
    	chain.doFilter(req, res);
	}
	
	@Override
	public void destroy() {
		
	}

}
