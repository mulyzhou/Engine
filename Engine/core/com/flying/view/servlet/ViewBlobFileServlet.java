package com.flying.view.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.sql.BLOB;

import com.flying.service.Engine;
import com.flying.service.EngineParameter;
import com.flying.util.FileUtil;

import org.springframework.orm.ibatis.support.BlobByteArrayTypeHandler;
import org.springframework.util.FileCopyUtils;
/**
 * Servlet implementation class ViewBlobFileServlet
 */
public class ViewBlobFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewBlobFileServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String tablename = request.getParameter("TABLENAME");
		String id = request.getParameter("ID");
		String idName = request.getParameter("IDNAME");
		String contentName = request.getParameter("CONTENTNAME");
		String filename = request.getParameter("FILENAME");
		
		EngineParameter selfEp = new EngineParameter(tablename + ".selectById");
		selfEp.putParam(idName, id);
		try {
			Engine.execute(selfEp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map contentMap = (Map) selfEp.getResult("data");
		
		byte[] contentByte = null;
		if(contentMap.size() > 0 && contentMap.get(contentName) != null){
			if(contentMap.get(contentName)instanceof String){
				String filepath =  request.getRealPath("/") + "attached/" + contentMap.get(contentName);

	    		contentByte =  FileCopyUtils.copyToByteArray(new FileInputStream(new File(filepath)));
			}else{
				contentByte =  (byte[]) contentMap.get(contentName);
			}
		}else{
			String path = request.getScheme() + "://"
		    		+ request.getServerName() + ":" + request.getServerPort()
		    		+ request.getContextPath() + "/img/no_pic.gif";
    		URL filepath = new URL(path);
    		
    		InputStream is = filepath.openStream();
    		contentByte = new byte[is.available()];
    		is.read(contentByte);
			
		}
		
		if(filename !=null && !"".equals(filename)){
			if(filename.contains(".doc")){
				response.setHeader("Content-Type", "application/vnd.ms-word");
			}else if(filename.contains(".xls")){
				response.setHeader("Content-Type", "application/vnd.ms-excel");
			}
			response.setHeader("Content-disposition",  
                     "attachment; filename=" + new String(filename.getBytes("gb2312"),"iso-8859-1"));
		}
		response.getOutputStream().write(contentByte);
	}
}
