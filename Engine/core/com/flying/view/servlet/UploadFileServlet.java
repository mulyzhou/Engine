package com.flying.view.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.flying.service.Engine;
import com.flying.service.EngineParameter;
import com.flying.util.FileUtil;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class UploadFileServlet
 */
public class UploadFileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadFileServlet() {
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
		//打印上传成功信息
        response.setContentType("text/json;charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        String savePath = request.getRealPath("/") + "attached/";
        //按照天，记录上传的文件
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String ymd = sdf.format(new Date());
		savePath += ymd + "/";
		
		//如果文件夹不存在，则创建
		File folder = new File(savePath);
    	if(!folder.exists()){
    		folder.mkdirs();
    	}
    	
    	//最大文件大小
		long maxSize = 1000000;

		//检查是否有文件
		if(!ServletFileUpload.isMultipartContent(request)){
			JSONObject json = new JSONObject();
        	json.put("success", false);
        	json.put("msg", "请选择文件。");
			out.println(json.toString());
			return;
		}
		//检查目录
		File uploadDir = new File(savePath);
		if(!uploadDir.isDirectory()){
			JSONObject json = new JSONObject();
        	json.put("success", false);
        	json.put("msg", "上传目录不存在。");
			out.println(json.toString());
			return;
		}
		//检查目录写权限
		if(!uploadDir.canWrite()){
			JSONObject json = new JSONObject();
        	json.put("success", false);
        	json.put("msg", "上传目录没有写权限。");
			out.println(json.toString());
			return;
		}

		//开始保存文件
		FileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setHeaderEncoding("UTF-8");
		List items = new ArrayList();
		try {
			items = upload.parseRequest(request);
		} catch (FileUploadException e1) {
			e1.printStackTrace();
		}
		Iterator itr = items.iterator();
		while (itr.hasNext()) {
			FileItem item = (FileItem) itr.next();
			String fileName = item.getName();
			if (!item.isFormField()) {
				//检查文件大小
//				if(item.getSize() > maxSize){
//					JSONObject json = new JSONObject();
//		        	json.put("success", false);
//		        	json.put("msg", "上传文件大小超过限制。");
//					out.println(json.toString());
//					return;
//				}
				//去掉扩展名的文件
				String fileNameNoExt = fileName.substring(0,fileName.lastIndexOf(".")).toLowerCase();
				String fileExt = fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase();

				//判断文件是否重名
				File saveFile = new File(savePath+fileName);
				if(saveFile.exists()){	//如果文件重复，直接返回并给出文件提示信息
					JSONObject json = new JSONObject();
		        	json.put("success", false);
		        	json.put("msg", "上传失败!文件名重复,请修改文件名并重新上传");
					out.println(json.toString());
					return;
				}

				String newFileName = fileNameNoExt+"."+fileExt;
				//保存文件
				File uploadedFile = new File(savePath, newFileName);
				try {
					item.write(uploadedFile);
				} catch (Exception e1) {
					JSONObject json = new JSONObject();
		        	json.put("success", false);
		        	json.put("msg", "文件保存到本地失败！");
					out.println(json.toString());
					e1.printStackTrace();
				}
				//保存数据库
				try{
					//文件基本属性
					Map fileMap = new HashMap();
					fileMap.put("fileName", newFileName);
					fileMap.put("createTime", new Date());
					fileMap.put("size", item.getSize());
					fileMap.put("fileExt", fileExt);
					fileMap.put("url", "attached/" + ymd + "/" + newFileName);
					//保存成功后，写入数据库
					String command = getServletConfig().getInitParameter("command");
					EngineParameter selfEp = new EngineParameter(command);
					selfEp.setParamMap(fileMap);
					
					Engine.execute(selfEp);
					
					String fileId = (String) selfEp.getResult("data");
					
					fileMap.put("id", fileId);
					//返回成功信息
					JSONObject json = new JSONObject();
		        	json.put("success", true);
		        	json.put("data", fileMap);
		        	json.put("msg", "上传成功。");
					out.println(json.toString());
				}catch(Exception e){
					FileUtil.deleteFile(uploadedFile);
					JSONObject json = new JSONObject();
		        	json.put("success", false);
		        	json.put("msg", "文件信息保存到数据库失败！");
					out.println(json.toString());
					return;
				}
			}
		}
	}

}
