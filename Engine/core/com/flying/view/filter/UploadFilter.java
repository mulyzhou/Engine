package com.flying.view.filter;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.util.FileCopyUtils;

import com.flying.init.StaticVariable;
import com.flying.logging.Log;
import com.flying.logging.LogFactory;
import com.flying.service.Engine;
import com.flying.service.EngineParameter;
import com.flying.util.FileUtil;
import com.flying.view.servlet.CommonResponse;

/**
 * <B>描述：</B>上传过滤器<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 */
public class UploadFilter implements Filter {
	private static Log log = LogFactory.getLog(UploadFilter.class);//日志
	
	private long maxSize = 1048576;//1024*1024
    /**
     * Default constructor. 
     */
    public UploadFilter() {
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

		 //获取Map参数
		 EngineParameter ep = (EngineParameter) req.getAttribute("ep");
		 
		 List<Map> fieldList = null;
		 
		if(ServletFileUpload.isMultipartContent(req)){

     		String command = ep.getCommand();
     		String tablename = command.substring(0, command.indexOf("."));
     		EngineParameter selfEp = new EngineParameter(StaticVariable.DB + ".selectFieldByBmc");
     		selfEp.putParam("BMC", tablename);
     		try {
				Engine.execute(selfEp);
				fieldList = (List<Map>) selfEp.getResult("data");
			} catch (Exception e) {
				e.printStackTrace();
			}
			//文件保存目录路径
			String savePath = req.getRealPath("/") + "attached/";

			//文件保存目录URL
			String saveUrl  = req.getContextPath() + "/attached/";
			
			//定义允许上传的文件扩展名
			HashMap<String, String> extMap = new HashMap<String, String>();
			extMap.put("image", "gif,jpg,jpeg,png,bmp");
			extMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
			extMap.put("file", "doc,docx,xls,xlsx,ppt,htm,html,txt,zip,rar,gz,bz2");
			//创建目录
			File saveDirFile = new File(savePath);
			if (!saveDirFile.exists()) {
				saveDirFile.mkdirs();
			}
			//按照天，记录上传的文件
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String ymd = sdf.format(new Date());
			savePath += ymd + "/";
			saveUrl += ymd + "/";
			File dirFile = new File(savePath);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
			//请求流
	        ServletInputStream in = req.getInputStream();
	        
	        byte[] buf = new byte[4048];
	        int len = in.readLine(buf, 0, buf.length); // 取出第一行
	        String header = new String(buf, 0, len,"UTF-8"); // 把第一行变成字符串，方便下面的匹配
	        header = header.replaceAll("\r|\n", "");
	        
	        while ((len = in.readLine(buf, 0, buf.length)) != -1) {
	            String row = new String(buf, 0, len,"UTF-8");
	            int i = row.lastIndexOf("form-data"); // 文件名以"\"开始，这个处理方式在Linux下就不行了，如果在Linux下运行，这个地方要修改一下。
	            int j = row.lastIndexOf("name"); // 文件名是以"""结尾的。
	            int l = row.lastIndexOf("filename");
	            //处理表单非文件
	            if (i != -1 && j != -1 && l == -1) {
	            	row = row.replaceAll("\r|\n", "");
	            	String name = row.substring(row.indexOf("form-data; ")+17,row.length()-1);//表单项名称
	            	in.readLine(buf, 0, buf.length);//剔除换行符
	            	String value = "";
	            	while((len = in.readLine(buf, 0, buf.length)) != -1){
	            		String str = new String(buf, 0, len,"UTF-8");
	            		if(str.contains(header)){
	            			break;
	            		}else{
	            			value += str;
	            		}
	            	}
	            	value = value.replaceAll("\r|\n", "");//表单项的值
	            	
	            	ep.putParam(name.trim(), value.trim());//放入参数
	                continue;
	            //处理表单中的文件
	            }else if(i != -1 && j != -1 && l != -1){
	            	row = row.replaceAll("\r|\n", "");
	            	in.readLine(buf, 0, buf.length);
	            	String textType = new String(buf, 0, len,"UTF-8");//剔除Content-Type: text/plain
	            	/**验证数据传输格式
	            	 * application/octet-stream：不认识的文件后缀及二进制文件
	            	 * text/plain ： 文本文件*/
//	            	if(textType.contains("application/octet-stream")){
//	            		continue;
//	            	}else {//if(textType.contains("text/plain")){
	            		String name = row.substring(row.indexOf("form-data; ")+17,row.indexOf("; filename")-1);
		            	String oldFileName = row.substring(row.indexOf("; filename")+12,row.length()-1);
		            	String extName = oldFileName.substring(oldFileName.lastIndexOf(".")+1,oldFileName.length());
		            	SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
						String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + "." + extName;
						
						//原始文件属性
						Map originalFilePropertyMap = new HashMap();
						originalFilePropertyMap.put("FILE_NAME", oldFileName);
						
		            	in.readLine(buf, 0, buf.length);//剔除换行符
		            	
		            	DataOutputStream fileStream = new DataOutputStream(
	                            new BufferedOutputStream(new FileOutputStream(savePath + newFileName)));
		            	int fileLength = 0;
		            	while((len = in.readLine(buf, 0, buf.length)) != -1){
		            		fileLength += len; 
		            		if(fileLength > maxSize){
		            			fileStream.close();
		            			FileUtil.deleteFile(new File(savePath + newFileName));
		            			CommonResponse.responseJson(res,false,oldFileName+"文件大小超过"+(maxSize/(1024*1024))+"M！");
		            			log.debug(oldFileName+"文件大小超过"+(maxSize/(1024*1024))+"M！");
		            			return;
		            		}
		            		String str = new String(buf, 0, len,"UTF-8");
		            		if(str.contains(header)){
		            			break;
		            		}else{
		            			//输出成文件
		            			fileStream.write(buf, 0, len); // 写入
		            		}
		            	}
		            	fileStream.close();
		            	//原始文件大小
		            	originalFilePropertyMap.put("FILE_LENGTH", fileLength);
		            	ep.putParam("ORIGINAL_FILE_PROPERTY", originalFilePropertyMap);
		            	
		            	for(int m = 0;m<fieldList.size();m++){
		            		Map fieldMap = fieldList.get(m);
		            		if(name.trim().equals(fieldMap.get("ZDMC").toString())){
		            			if("BLOB".equals(fieldMap.get("ZDLX").toString())){
		            				FileInputStream fis = new FileInputStream(new File(savePath + newFileName));
		    		            	byte[] fileByte = FileCopyUtils.copyToByteArray(fis);
		    		            	FileUtil.deleteFile(new File(savePath + newFileName));
		    		            	//保存二进制流
		    		            	ep.putParam(name.trim(), fileByte);//将新创建的文件名称放入参数中
		            			}else if(fieldMap.get("ZDLX").toString().toUpperCase().contains("VARCHAR")){
		            				//保存文件路径
		    		            	ep.putParam(name.trim(), ymd+"/"+newFileName);//将新创建的文件名称放入参数中
		            			}
		            		}
		            	}
		            	
		            	if(ep.getParam(name.trim()) == null){
		            		FileInputStream fis = new FileInputStream(new File(savePath + newFileName));
    		            	byte[] fileByte = FileCopyUtils.copyToByteArray(fis);
    		            	FileUtil.deleteFile(new File(savePath + newFileName));
    		            	//保存二进制流
    		            	ep.putParam(name.trim(), fileByte);//将新创建的文件名称放入参数中
		            	}
	            	//}
	            }else{
	            	
	            }
	        }
	        in.close();
		}
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		if(fConfig.getInitParameter("maxSize") != null && !"".equals(fConfig.getInitParameter("maxSize"))){
			maxSize = Long.parseLong(fConfig.getInitParameter("maxSize"));
		}

	}
}
