package com.flying.view.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.flying.init.StaticVariable;
import com.flying.logging.Log;
import com.flying.logging.LogFactory;
import com.flying.util.ZipCompressor;

/**
 * <B>描述：</B>导出模块<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 */
public class Export extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Log log = LogFactory.getLog(Export.class);

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Export() {
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
		String exportFile = "";
		String classFilePath = "";
		String jsFilePath = "";
		String dbFileUrl = "";
		
		if(StaticVariable.DEBUG){
			exportFile = StaticVariable.PATH + StaticVariable.MODULE + ".app";
			classFilePath = StaticVariable.PATH + "build/classes";
			jsFilePath = StaticVariable.PATH + "WebContent/js/biz";
			dbFileUrl = StaticVariable.PATH + "WebContent/tableXml.xml";
		}else{
			exportFile = StaticVariable.PATH + StaticVariable.MODULE + ".app";
			classFilePath = StaticVariable.PATH + "WEB-INF/classes";
			jsFilePath = StaticVariable.PATH + "js/biz";
			dbFileUrl = StaticVariable.PATH + "tableXml.xml";
		}
		log.debug("class的导出路径：" + classFilePath);
		log.debug("js的导出的路径：" + jsFilePath);
		
		ZipCompressor.flyingCompress(classFilePath, jsFilePath,dbFileUrl, exportFile);
		
		
        response.setContentType( "application/doc;charset=GB2312");
        response.setHeader("Content-disposition",  
                              "attachment; filename=" + StaticVariable.MODULE + ".app" );
        
        try{
            File file = new File(exportFile);            
            FileInputStream bis = new FileInputStream(file);            
            OutputStream bos = response.getOutputStream(); 

            byte[] buff = new byte[1024];
            int readCount = 0;
            int i = 0;
            readCount = bis.read(buff);
            while (readCount != -1){
               bos.write(buff, 0, readCount); 
               readCount = bis.read(buff);
            }
            if (bis!=null)
                bis.close();            
            if (bos!=null)
                bos.close();

        }catch(Exception e){
           e.printStackTrace();
        }
	}

}
