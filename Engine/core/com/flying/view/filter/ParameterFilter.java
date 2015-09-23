package com.flying.view.filter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.flying.logging.Log;
import com.flying.logging.LogFactory;
import com.flying.service.EngineParameter;

/**
 * 
 * <B>描述：</B>参数处理过滤器<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 */
public class ParameterFilter implements Filter {

	private static Log log = LogFactory.getLog(ParameterFilter.class);
    /**
     * Default constructor. 
     */
    public ParameterFilter() {
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
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss E");
		long start = System.currentTimeMillis();
		Date startDate = new Date(start);
		log.debug("**************** 请求开始 【"+dateFormat.format(startDate)+"】 ****************");
		//flex编码
		String flexEncode = req.getParameter("flexEncode");
		//存储参数变量
		Map<String,String> paramMap = new HashMap<String,String>();
		String command = null;
		String commandType = null;
		String fileDownloadName = null;
		//获取参数名称
		Enumeration paramNameEnum = req.getParameterNames();
		 while(paramNameEnum.hasMoreElements()){
			 String paramName = paramNameEnum.nextElement().toString();
			 if("command".equals(paramName)){
				 command = req.getParameter(paramName);
				 continue;
			 }else if("commandType".equals(paramName)){
				 commandType = req.getParameter(paramName);
				 continue;
			 }else if("FILE_DOWNLOAD_NAME".equals(paramName)){
				 fileDownloadName = req.getParameter(paramName);
				 continue;
			 }
			 
			 if(flexEncode == null || "".equals(flexEncode)){
				 paramMap.put(paramName,req.getParameter(paramName));
			 }else{
				 paramMap.put(paramName,java.net.URLDecoder.decode(req.getParameter(paramName),"UTF-8"));
			 }
		 }
		 EngineParameter ep = new EngineParameter(command);
		 ep.setCommandType(commandType);
		 ep.setFileDownloadName(fileDownloadName);
		 ep.setParamMap(paramMap);//设置传入参数
		 
		 if(command == null || "".equals(command)){
			 log.error("地址："+req.getContextPath()+req.getServletPath()+" 错误：传入的数据没有command参数！");
			 return;
		 }else{
			 log.info("地址："+req.getContextPath()+req.getServletPath()+" 执行命令："+command);
		 }
		 
		 
		 //将参数存储到Request
		 req.setAttribute("ep", ep);
		 //过滤器向下传递
		chain.doFilter(req, res);
		long end = System.currentTimeMillis();
		Date endDate = new Date(end);
		log.debug("~~~~~~~~~~~~~~~~ 请求结束 【"+dateFormat.format(endDate)+"】 ,耗时："+(end-start)+"(ms) ");

	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
