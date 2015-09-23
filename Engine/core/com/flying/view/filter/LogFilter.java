package com.flying.view.filter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import com.flying.init.StaticVariable;
import com.flying.logging.Log;
import com.flying.logging.LogCache;
import com.flying.logging.LogFactory;
import com.flying.service.EngineParameter;
import com.flying.util.FlyingUtil;

/**
 * 
 * <B>描述：</B>日志过滤器<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 */
public class LogFilter implements Filter {
	private static Log log = LogFactory.getLog(LogFilter.class);//日志
	
	private String encoding;
    /**
     * Default constructor. 
     */
    public LogFilter() {
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		
		if(StaticVariable.LOG){
			Map userLog = new HashMap();
			//获取IP地址
			String ip = getIpAddr(req);
			userLog.put("IP", ip);
			//获取用户权限信息
	    	HttpSession session = req.getSession(false);
	    	//将session信息传入后台
	    	EngineParameter ep = (EngineParameter) req.getAttribute("ep");
	    	userLog.put("COMMAND", ep.getCommand());
	    	userLog.put("COMMAND_PARAM", FlyingUtil.changeMap2JsonString(ep.getParamMap()));
	    	userLog.put("STARTTIME", new Date());
	    	
	    	if(session == null || session.getAttribute("USERINFO") == null){
				userLog.put("USER_ID", null);
				userLog.put("USER_NAME", null);
			}else{
				Map userInfo = (Map)session.getAttribute("USERINFO");
				userLog.put("USER_ID", userInfo.get("USER_ID"));
				userLog.put("USER_NAME", userInfo.get("USER_NAME"));
			}
	    	//记录用户日志信息
			LogCache.THREAD_INFO.put(Thread.currentThread().getId()+"", userLog);
		}
    	
		chain.doFilter(request, response);
		
		if(StaticVariable.LOG){
			//清理过期的用户日志信息，当用户日志池超过2条信息，就进行清理
			if(LogCache.THREAD_INFO.size() > 2){
				LogCache.clearLog();
			}
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}
	/**
	 * 获取访问的IP地址
	 */
	public String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
