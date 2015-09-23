package com.flying.view.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.flying.init.StaticVariable;
import com.flying.logging.Log;
import com.flying.logging.LogFactory;

/**
 * <B>描述：</B>导入模块<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 */
public class Import extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(Import.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Import() {
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
		File importFile = null;
		String importPath = "";
		
		if(StaticVariable.DEBUG){
			importPath = StaticVariable.PATH + "WebContent";
		}else{
			importPath = StaticVariable.PATH;
		}
		log.debug("导入的路径：" + importPath);
		 //判断提交过来的表单是否为文件上传菜单 
        boolean isMultipart= ServletFileUpload.isMultipartContent(request);
        if(isMultipart){
            //构造一个文件上传处理对象
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            
            Iterator items;
            try{
                //解析表单中提交的所有文件内容
                items=upload.parseRequest(request).iterator();
                while(items.hasNext()){
                    FileItem item = (FileItem) items.next();
                    if(!item.isFormField()){
//                        //取出上传文件的文件名称
//                        String name = item.getName();
//                        //取得上传文件以后的存储路径
//                        String fileName=name.substring(name.lastIndexOf('\\')+1,name.length());
//                        //上传文件以后的存储路径
//                        String path= StaticVariable.PATH + "jar/" + fileName;
//                        //上传文件
//                        importFile = FileUtil.createFile(path);
//                        item.write(importFile);
//                        
//                        ZipCompressor.deCompress(importPath, importFile);
//                        
//                        FileUtil.deleteFile(new File(StaticVariable.PATH + "jar/"));
//                        //注册子系统
//                        Map param = new HashMap();
//                        String subName = fileName.substring(0,fileName.indexOf("."));
//                        param.put("command", "T_BASE_TABLE.import");
//                        param.put("SUB_NAME", subName);
//                        Engine.execute(param);
                        
                        //打印上传成功信息
                        response.setContentType("text/html");
                        response.setCharacterEncoding("GB2312");
                        PrintWriter out = response.getWriter();
                        
                        out.print("{success:true,msg:'子系统导入成功'}");
                        
                        
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }

        }
	}

}
