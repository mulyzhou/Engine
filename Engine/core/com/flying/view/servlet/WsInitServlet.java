package com.flying.view.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.xml.ws.Endpoint;

import com.flying.logging.Log;
import com.flying.logging.LogFactory;

/**
 * Servlet implementation class WsInitServlet
 */
public class WsInitServlet extends HttpServlet {
	private static Log log = LogFactory.getLog(WsInitServlet.class);
	private static final long serialVersionUID = 1L;
    
	//端口号
	private int port = 8081;
	//服务名称
	private String ws_name = "ws";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WsInitServlet() {
        super();
    }
    
    
    @Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.port = Integer.parseInt(config.getInitParameter("port")==null?"8081":config.getInitParameter("port"));
		this.ws_name = config.getInitParameter("ws_name");
		
		log.info("初始化webservice！");
		//Endpoint.publish("http://localhost:"+this.port+"/"+this.ws_name, new WebserviceCenter());
    }
}
