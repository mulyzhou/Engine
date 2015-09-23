package com.flying.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import com.flying.init.StaticVariable;
import com.flying.logging.Log;
import com.flying.logging.LogFactory;
/**
 * <B>描述：</B>zip压缩工具类<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 *
 */
public class ZipCompressor {   
	private static Log log = LogFactory.getLog(ZipCompressor.class);
    static final int BUFFER = 8192;
    
    private ZipCompressor() {     
    }
    
    /**
     * 将多个文件夹打包成一个压缩文件
     * 
     * @param srcPathNames 需要压缩的文件夹
     * @param zipFileName 压缩之后的文件
     */
    public static void compress(String[] srcPathNames,String zipFileName) {
        try {
        	File zipFile = FileUtil.createFile(zipFileName);
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);   
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,new CRC32());   
            ZipOutputStream out = new ZipOutputStream(cos);   
            String basedir = "";
            for(int m =0;m < srcPathNames.length;m++){
            	File file = new File(srcPathNames[m]);
            	
            	if (!file.exists()){
    	            throw new RuntimeException(srcPathNames[m] + "不存在！");   
    	        }   
                File[] fileChildren = file.listFiles();
                for(int i = 0;i<fileChildren.length;i++){
                	 compress(fileChildren[i], out, basedir);
                }
            }  
	        
            
            out.close();   
        } catch (Exception e) {   
            throw new RuntimeException(e);   
        }   
    }   
    
    /**
     * 满足Flying框架的压缩，将多个文件夹打包成一个压缩文件
     * 
     * @param classPathName 需要压缩的文件夹
     * @param jsPathName 需要压缩的js文件
     * @param dbFileUrl 需要压缩的数据文件
     * @param zipFileName 压缩之后的文件
     */
    public static void flyingCompress(String classPathName,String jsPathName,String dbFileUrl,String zipFileName) {
        try {
        	String jarPathName = StaticVariable.PATH + "jar/";
        	compress(new String[]{classPathName},jarPathName + "flying-" + StaticVariable.MODULE +".jar");
        	
        	File zipFile = new File(zipFileName);
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);   
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,new CRC32());   
            ZipOutputStream out = new ZipOutputStream(cos);   
            //生成class文件的压缩包
            String jarBasedir = "WEB-INF/lib/";
        	File classFile = new File(jarPathName);
        	if (!classFile.exists()){
	            throw new RuntimeException(jarPathName + "不存在！");   
	        }
        	
            File[] classFileChildren = classFile.listFiles();
            for(int i = 0;i<classFileChildren.length;i++){
            	 compress(classFileChildren[i], out, jarBasedir);
            }
	        
            FileUtil.deleteFile(new File(jarPathName));
            log.debug("生成jar文件完毕！");
            
            //生成js文件的压缩包
            String jsBasedir = "js/biz/";
        	File jsFile = new File(jsPathName);
        	if (!jsFile.exists()){
	            throw new RuntimeException(jarPathName + "不存在！");   
	        }
        	
            File[] jsFileChildren = jsFile.listFiles();
            for(int i = 0;i<jsFileChildren.length;i++){
            	 compress(jsFileChildren[i], out, jsBasedir);
            } 
            log.debug("生成js文件完毕！");
            
            //生成db文件的压缩包
            String dbBasedir = "db/" + StaticVariable.MODULE + "/";
        	File dbFile = new File(dbFileUrl);
        	if (!dbFile.exists()){
	            throw new RuntimeException(dbFileUrl + "不存在！");   
	        }
        	
        	compress(dbFile, out, dbBasedir);
        	log.debug("生成db文件完毕！");
        	
            out.close();   
        } catch (Exception e) {   
            throw new RuntimeException(e);   
        }   
    }
    /**
     * 压缩文件
     * 
     * @param file 需要压缩的文件
     * @param out 输出流
     * @param basedir 目录
     */
    private static void compress(File file, ZipOutputStream out, String basedir) {   
        /* 判断是目录还是文件 */  
        if (file.isDirectory()) {   
            log.debug("压缩：" + basedir + file.getName());   
            ZipCompressor.compressDirectory(file, out, basedir);   
        } else {   
        	log.debug("压缩：" + basedir + file.getName());   
            ZipCompressor.compressFile(file, out, basedir);   
        }   
    }   
  
    /** 压缩一个目录 */  
    private static void compressDirectory(File dir, ZipOutputStream out, String basedir) {   
        if (!dir.exists())   
            return;   
  
        File[] files = dir.listFiles();   
        for (int i = 0; i < files.length; i++) {   
            /* 递归 */  
            compress(files[i], out, basedir + dir.getName() + "/");   
        }   
    }   
  
    /** 压缩一个文件 */  
    private static void compressFile(File file, ZipOutputStream out, String basedir) {   
        if (!file.exists()) {   
            return;   
        }   
        try {   
            BufferedInputStream bis = new BufferedInputStream(   
                    new FileInputStream(file));   
            ZipEntry entry = new ZipEntry(basedir + file.getName());   
            out.putNextEntry(entry);   
            int count;   
            byte data[] = new byte[BUFFER];   
            while ((count = bis.read(data, 0, BUFFER)) != -1) {   
                out.write(data, 0, count);   
            }   
            bis.close();   
        } catch (Exception e) {   
            throw new RuntimeException(e);   
        }   
    }   
    /**
     * 解压文件
     * 
     * @param filePath 解压到的文件夹
     * @param srcfile 需要解压的zip文件
     */
    public static void deCompress(String filePath,File srcfile){
    	try {
    		ZipFile zipFile = new ZipFile(srcfile);
			Enumeration emu = zipFile.entries();
			int i=0; 
			while(emu.hasMoreElements()){
				ZipEntry entry = (ZipEntry)emu.nextElement(); 
				//会把目录作为一个file读出一次，所以只建立目录就可以，之下的文件还会被迭代到。
				if (entry.isDirectory()){
					new File(filePath +"/"+ entry.getName()).mkdirs(); 
					continue; 
				}
				BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry)); 
				log.debug("解压到：" + filePath + "/" + entry.getName());   
				File file = new File(filePath + "/" + entry.getName()); 
				//加入这个的原因是zipfile读取文件是随机读取的，这就造成可能先读取一个文件
				//而这个文件所在的目录还没有出现过，所以要建出目录来。
				File parent = file.getParentFile(); 
				if(parent != null && (!parent.exists())){
					parent.mkdirs(); 
				}
				FileOutputStream fos = new FileOutputStream(file); 
				BufferedOutputStream bos = new BufferedOutputStream(fos,BUFFER); 
				int count; 
				byte data[] = new byte[BUFFER]; 
				while ((count = bis.read(data, 0, BUFFER)) != -1){
					bos.write(data, 0, count); 
				}
				bos.flush(); 
				bos.close(); 
				bis.close(); 
			}
			zipFile.close(); 
		} catch (Exception e) {
			e.printStackTrace(); 
		}
    }
    
	public static void main(String[] args) {
		//ZipCompressor.compress(new String[]{"E:/test","E:/test123"},"E:/szhzip.zip");
		//ZipCompressor.deCompress("E:/testzdf", "E:/szhzip.zip");
	}
}