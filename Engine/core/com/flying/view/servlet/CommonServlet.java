package com.flying.view.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.flying.exception.FlyingException;
import com.flying.exception.FlyingExceptionTranslator;
import com.flying.logging.Log;
import com.flying.logging.LogFactory;
import com.flying.service.Engine;
import com.flying.service.EngineParameter;

/**
 * 
 * <B>描述：</B>请求处理类<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 */
public class CommonServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(CommonServlet.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CommonServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		process(request, response);
	}
	/**
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private void process(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		EngineParameter ep = (EngineParameter) request.getAttribute("ep");// 获取参数
		/* 业务操作 */
		try{
			Engine.execute(ep);
			
			if(ep.getFileDownloadName()!= null && !"".equals(ep.getFileDownloadName())){
				CommonResponse.download(ep,response,ep.getResultMap());//导出excel
			}else if(ep.getRedirectPageName() != null && !"".equals(ep.getRedirectPageName())){
				response.setStatus(CommonResponse.FLYING_PAGE_REDIRECT);
				CommonResponse.responseJson(response, false, ep.getRedirectPageName());//进行跳转
			}else{
				CommonResponse.responseJson(response,ep.getResultMap());//返回结果json
			}			
		}catch(Throwable e){//处理系统级错误
			FlyingException fue = FlyingExceptionTranslator.translate(e);
			CommonResponse.responseJson(response, false, fue.getMessage());//执行失败，向前台输出错误信息json
		}
	}
}
