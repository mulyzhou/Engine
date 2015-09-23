package com.flying.view.servlet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.flying.init.StaticVariable;
import com.flying.logging.Log;
import com.flying.logging.LogFactory;
import com.flying.service.EngineParameter;
import com.flying.util.ExportExcel;
import com.flying.util.FileUtil;
/**
 * 
 * <B>描述：</B>输出工具类<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 *
 */
public class CommonResponse {
	
	private static Log log = LogFactory.getLog(CommonResponse.class);//日志
	
	public static final int FLYING_PAGE_REDIRECT = 403;
	/**
	 * 返回Map数据
	 * 
	 * @param response
	 * @param resultMap
	 * @throws Exception
	 */
	public static void responseJson(HttpServletResponse response, Map resultMap) {
		if(resultMap.size()>0){
			resultMap.put("success", true);
			JSONObject jsonObject = JSONObject.fromObject(resultMap);
			responseTxt(response, jsonObject.toString());
		}else{
			CommonResponse.responseJson(response, true, "执行成功!");
		}
	}
	/**
	 * 返回list数据
	 * 
	 * @param response
	 * @param result
	 * @throws Exception
	 */
	public static void responseJson(HttpServletResponse response, List result) {
		JSONArray jsonArray = JSONArray.fromObject(result);
		responseTxt(response, jsonArray.toString());
	}

	/**
	 * 针对于AJAX的的返回请求
	 * 
	 * @param response
	 * @param success
	 * @param msg
	 * @throws Exception
	 */
	public static void responseJson(HttpServletResponse response, boolean success,String msg) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("success", success);
		jsonObject.put("msg", msg);

		responseTxt(response, jsonObject.toString());
	}
	/**
	 * 将字符返回到前台
	 * 
	 * @throws IOException
	 */
	public static void responseTxt(HttpServletResponse response, String result) {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter pw = null;
		try {
			pw = response.getWriter();
			pw.write(result);
			pw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			pw.close();
		}
	}
	
	public static void download(EngineParameter ep,HttpServletResponse response, Map resultMap) {
		String fileName = ep.getFileDownloadName();
		
		String currentTime= String.valueOf(new Date().getTime());
    	String downloadFileName= fileName+"_"+currentTime+".xls";
    	
        try{         
        	response.setContentType( "application/doc;charset=GB2312");
            response.setHeader("Content-disposition",  
                                  "attachment; filename=" + new String(downloadFileName.getBytes("gb2312"),"iso-8859-1"));
            
            JSONArray columns = JSONArray.fromObject(java.net.URLDecoder.decode(ep.getParam("FILE_DOWNLOAD_PROPERTY").toString(),"UTF-8"));
    		
            InputStream is = ExportExcel.expertExcel(fileName,columns, (List)resultMap.get("data"));            
            OutputStream bos = response.getOutputStream(); 

            byte[] buff = new byte[1024];
            int readCount = 0;
            readCount = is.read(buff);
            while (readCount != -1){
               bos.write(buff, 0, readCount); 
               readCount = is.read(buff);
            }
            if (is!=null)
                is.close();            
            if (bos!=null)
                bos.close();

        }catch(Exception e){
           e.printStackTrace();
        }
	}
	/**
	 * 执行进行跳转（地址不变）
	 * 
	 * @param path
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public static void forward(ServletContext servletContext,String path, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		servletContext.getRequestDispatcher(path).forward(request,response);
	}
	/**
	 * 执行进行直接跳转（地址变化）
	 * 
	 * @param path
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public static void redirect(String path,HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		response.sendRedirect(path);
	}
}
