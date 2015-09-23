package com.flying.view.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.flying.init.StaticVariable;
import com.flying.logging.Log;
import com.flying.logging.LogFactory;
import com.flying.service.Engine;
import com.flying.service.EngineParameter;
import com.flying.util.RandomValidateCode;
import com.flying.view.servlet.CommonResponse;

/**
 * 
 * <B>描述：</B>权限处理过滤器<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 */
public class PermissionFilter implements Filter {
	private static Log log = LogFactory.getLog(PermissionFilter.class);//日志
    /**
     * Default constructor. 
     */
    public PermissionFilter() {
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
		HttpServletResponse res = (HttpServletResponse)response;
		//获取用户权限信息
    	HttpSession session = req.getSession(false);
    	//将session信息传入后台
		EngineParameter ep = (EngineParameter) req.getAttribute("ep");
		
		if("T_SYS_USERINFO.login".equals(ep.getCommand())){
			session = req.getSession();

			if(!ep.getParam("VALIDATECODE").toString().toUpperCase().equals(session.getAttribute(RandomValidateCode.RANDOMCODEKEY))){
				log.debug("验证码输入错误！");
				CommonResponse.responseJson(res, false, "验证码输入错误！");
				return;
			}else{
				session.invalidate();//清空session
				Cookie cookie = req.getCookies()[0];//获取cookie
				cookie.setMaxAge(0);//让cookie过期

				session = req.getSession(true);
				ep.putParam("session", session);
			}
		}else if(!StaticVariable.AUTHENTICATION){
			log.debug("无需权限验证模式运行！");
			ep.putParam("session", session);
		}else if(StaticVariable.LOGOUT_WHITE_LIST.containsKey(ep.getCommand())){
			log.debug("无需登陆白名单！");
			ep.putParam("session", session);
		}else{
			if(session == null || session.getAttribute("USERINFO") == null){
				log.debug("此用户未登陆！");
				res.setStatus(CommonResponse.FLYING_PAGE_REDIRECT);
				CommonResponse.responseJson(res, false, "login.html");
				return;
			}else if(StaticVariable.LOGIN_WHITE_LIST.containsKey(ep.getCommand())){
				log.debug("登陆白名单，无需验证！");
				ep.putParam("session", session);
			}else{
				log.debug("进行权限验证！");
				ep.putParam("session", session);
				
				//屏蔽了子资源的验证过程
				if(false){
					EngineParameter selfEp = new EngineParameter("T_SYS_RESOURCE.getSubResourceByUserId");
					String command = ep.getCommand();
					selfEp.putParam("RESOURCE_ADDR", command);
					
					Map userInfo = (Map)session.getAttribute("USERINFO");
					selfEp.putParam("USER_ID", userInfo.get("USER_ID"));
					try {
						Engine.execute(selfEp);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					if(Integer.parseInt(selfEp.getResult("data").toString()) == 0 ){
						log.debug(userInfo.get("LOGIN_NAME")+" 没有访问 " + command +" 的权限！");
						res.setStatus(CommonResponse.FLYING_PAGE_REDIRECT);
						CommonResponse.responseJson(res, false, "login.html");//进行跳转
						return;
					}
				}
			}
		}
		//过滤器向下传递
		chain.doFilter(req, res);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
