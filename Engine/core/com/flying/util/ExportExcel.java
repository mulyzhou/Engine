package com.flying.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BuiltinFormats;

/**
 * <B>描述：</B>利用开源组件POI3.0.2动态导出EXCEL文档<br/>
 * 注意这里为了简单起见，boolean型的属性xxx的get器方式为getXxx(),而不是isXxx()
 * byte[]表jpg格式的图片数据<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @param <T> 应用泛型，代表任意一个符合javabean风格的类
 * @author zdf
 */
public class ExportExcel<T> {
   
   public ExportExcel(){
	   
   }
   /**
    *给某个工作簿添加样式
    * 
    * @param workbook 工作簿
    * @param type 样式类型 1：头，2：体
    * @return HSSFCellStyle样式
    */
   private static HSSFCellStyle setStyle(HSSFWorkbook workbook,int type,boolean locked,String datetype)
   {
	   	  // 生成一个样式
	      HSSFCellStyle style = workbook.createCellStyle();
	      // 设置这些样式
	      style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	      style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
	      style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
	      style.setBorderRight(HSSFCellStyle.BORDER_THIN);
	      style.setBorderTop(HSSFCellStyle.BORDER_THIN);
	      style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	      // 生成一个字体
	      HSSFFont font = workbook.createFont();
	   	  if(type == 1){
	   		  style.setFillForegroundColor(HSSFColor.WHITE.index);
		      font.setColor(HSSFColor.BLACK.index);
		      font.setFontHeightInPoints((short) 11);
		      font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		      // 把字体应用到当前的样式
		      style.setFont(font);
	   	  }else if(type == 2){
	   		style.setFillForegroundColor(HSSFColor.WHITE.index);
	        // 生成另一个字体
	        font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
	        // 把字体应用到当前的样式
	        style.setFont(font);
	        //设置单元格样式
	        style.setDataFormat((short)getDataFormat(datetype));
	   	  }
	   	  //设置单元格的锁定状态
	   	  style.setLocked(locked);
	   	  
	   return style;
   }
   /**
    * 静态方法，根据条件获取一张excel表
    * 
    * @param title sheet标题
    * @param columns 数据格式
    * @param dataset 数据，数据存放在List<HashMap>中
    * @return InputStream 输出流
    * @throws Exception
    */
   public static InputStream expertExcel(String title,JSONArray columns,List<HashMap> dataset) throws Exception{
		  int height = 400;
		  for(int j=0;j<columns.size();j++){
			  if(columns.getJSONObject(j).get("height") != null){
				  if(Integer.parseInt(columns.getJSONObject(j).get("height").toString())>height){
					  height = Integer.parseInt(columns.getJSONObject(j).get("height").toString());
				  }
			  }
		  }
	   	  // 声明一个工作薄
	      HSSFWorkbook workbook = new HSSFWorkbook();
	      // 生成一个表格
	      HSSFSheet sheet = workbook.createSheet(title);
	      // 设置表格默认列宽度为15个字节
	      sheet.setDefaultColumnWidth((short) 15);
	      String pk = "";
	      //设置宽度
	      for(int p=0;p<columns.size();p++){
	    	  if(columns.getJSONObject(p).has("isPk") && "true".equals(columns.getJSONObject(p).getString("isPk").toLowerCase())){
					pk = columns.getJSONObject(p).getString("dataIndex");
				 }
	      }
	      
	      //设置宽度
	      for(int k=0;k<columns.size();k++){
	    	  if(columns.getJSONObject(k).has("isPk") || (columns.getJSONObject(k).has("dataIndex") && pk.equals(columns.getJSONObject(k).getString("dataIndex"))) || columns.getJSONObject(k).has("isDownload")){
	    		  if(columns.getJSONObject(k).has("manual") && "true".equals(columns.getJSONObject(k).getString("manual").toLowerCase())){
	    			  int pk_width = columns.getJSONObject(k).has("width") ? Integer.parseInt(columns.getJSONObject(k).get("width").toString())*40 : 4000;
	    			  sheet.setColumnWidth((short)   k,   (short)pk_width);
	    		  }else{
	    			  sheet.setColumnWidth((short)   k,   (short)0);
	    		  }
	    	  }else	if(columns.getJSONObject(k).get("width") != null){
				  sheet.setColumnWidth((short)   k,   Integer.parseInt(columns.getJSONObject(k).get("width").toString())*40);
			  }else{
				  sheet.setColumnWidth((short)   k,   (short)4000);
			  }
		  }
	      
	      // 声明一个画图的顶级管理器
	      HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
	      // 定义注释的大小和位置,详见文档
	      //HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
	      // 设置注释内容
	      //comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
	      // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
	      //comment.setAuthor("zdf");
	      
	      //产生表格标题行
	      HSSFRow row = sheet.createRow(0);
	      for (short i = 0; i < columns.size(); i++) {
	         HSSFCell cell = row.createCell(i);
	         //定义样式
			 boolean locked = false;
			 //当此项为主键或者主动锁定，则为true，否则为false。
			 if(columns.getJSONObject(i).has("isPk") && "true".equals(columns.getJSONObject(i).getString("isPk").toLowerCase())){
				locked = true;
			 }
			 if(columns.getJSONObject(i).has("locked") && "true".equals(columns.getJSONObject(i).getString("locked").toLowerCase())){
				locked = true;
			 }
	         cell.setCellStyle(setStyle(workbook,1,locked,"string"));
	         HSSFRichTextString text = new HSSFRichTextString(columns.getJSONObject(i).getString("header"));
	         cell.setCellValue(text);
	      }
	      
	      HSSFCell[] cells = new HSSFCell[columns.size()];
	      for(int m = 0;m <dataset.size() ;m++){
				HSSFRow rows = sheet.createRow((short) (m+1));
				rows.setHeight((short)height);
				for(int n = 0;n<columns.size();n++){
					//创建列
					cells[n] = rows.createCell((short)n);
					//定义类型
					//cells[n].setCellType(HSSFCell.CELL_TYPE_STRING);
					//定义样式
					boolean locked = false;
					//当此项为主键或者主动锁定，则为true，否则为false。
					//当此项为主键或者主动锁定，则为true，否则为false。
					if(columns.getJSONObject(n).has("isPk") && "true".equals(columns.getJSONObject(n).getString("isPk"))){
						locked = true;
					}
					if(columns.getJSONObject(n).has("locked") && "true".equals(columns.getJSONObject(n).getString("locked"))){
						locked = true;
					}
					String type = "string";
					if(columns.getJSONObject(n).has("type") && columns.getJSONObject(n).get("type") != null){
						type = columns.getJSONObject(n).getString("type").toLowerCase();
					}
					cells[n].setCellStyle(setStyle(workbook,2,false,type));
					
					String content = dataset.get(m).get(columns.getJSONObject(n).getString("dataIndex"))== null ?"":dataset.get(m).get(columns.getJSONObject(n).getString("dataIndex")).toString();
					HSSFRichTextString text = new HSSFRichTextString(content);
					cells[n].setCellValue(text);
				}
	      }
	  
	      ByteArrayOutputStream baos = new ByteArrayOutputStream();
	      workbook.write(baos);
		  byte[] ba = baos.toByteArray();
		  ByteArrayInputStream bais = new ByteArrayInputStream(ba);
		  
 		  return bais;
   }
   
   /**
    * 对象方法
    * 
    * @param title sheet标题
    * @param strJson  数据格式
    * @param dataset 数据存放在List<Object>中
    * @param obj 待定义对象
    * @return InputStream 输出流
    * @throws Exception
    */
   public InputStream expertExcel(String title,String[] strJson,List<T> dataset,Object obj) throws Exception{
	   	  // 声明一个工作薄
	      HSSFWorkbook workbook = new HSSFWorkbook();
	      // 生成一个表格
	      HSSFSheet sheet = workbook.createSheet(title);
	      sheet.protectSheet("dsideal");
	      //解析json数据 
	   	  JSONObject[] jsonObject = new JSONObject[strJson.length];
		   for(int i=0;i<strJson.length;i++){
			   jsonObject[i] = JSONObject.fromObject(strJson[i]);
		   }
		  //读取excel单元格的高度，默认为18像素，poi中使用的好像是缇，转换方式，高度：n*15.625。
		  int height = 0;
		  for(int j=0;j<jsonObject.length;j++){
			  if(jsonObject[j].has("height") && jsonObject[j].get("height") != null){
				  if(jsonObject[j].get("height").toString().matches("^\\d*$") && Integer.parseInt(jsonObject[j].get("height").toString())>height){
					  height = Integer.parseInt(jsonObject[j].get("height").toString())*15;
				  }
			  }
		  }
		  //设置行默认高度
		  sheet.setDefaultRowHeightInPoints(18);
	      // 设置表格默认列宽度为15个字节
	      sheet.setDefaultColumnWidth(72*35);
	      //设置列的宽度，默认72像素，宽度：n*35.7
	      for(int k=0;k<jsonObject.length;k++){
			  if(jsonObject[k].has("width") && jsonObject[k].get("width") != null){
				  if(jsonObject[k].get("width").toString().matches("^\\d*$")){
					  sheet.setColumnWidth(k,Integer.parseInt(jsonObject[k].get("width").toString())*35);
				  }
			  }
		  }
	      
	      // 声明一个画图的顶级管理器
	      //HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
	      // 定义注释的大小和位置,详见文档
	      //HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
	      // 设置注释内容
	      //comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
	      // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
	      //comment.setAuthor("zdf");
	      
	      //第一行：产生表格标题行
	      HSSFRow row = sheet.createRow(0);
	      for (short i = 0; i < jsonObject.length; i++) {
	         HSSFCell cell = row.createCell(i);
	         //定义样式
			 boolean locked = false;
			 //当此项为主键或者主动锁定，则为true，否则为false。
			 if(jsonObject[i].has("pk") && "true".equals(jsonObject[i].getString("pk"))){
				locked = true;
			 }
			 if(jsonObject[i].has("locked") && "true".equals(jsonObject[i].getString("locked"))){
				locked = true;
			 }
	         cell.setCellStyle(setStyle(workbook,1,locked,"string"));
	         //设置表头
	         HSSFRichTextString text = new HSSFRichTextString();
	         if(jsonObject[i].has("header")){
	        	 text = new HSSFRichTextString(jsonObject[i].getString("header"));
	         }
	         cell.setCellValue(text);
	      }
	      
	      //第二行开始
	      HSSFCell[] cells = new HSSFCell[jsonObject.length];
	      for(int m = 0;m <dataset.size() ;m++){
				HSSFRow rows = sheet.createRow((short) (m+1));
				rows.setHeight((short)height);
				for(int n = 0;n<jsonObject.length;n++){
					//创建列
					cells[n] = rows.createCell((short)n);
					//定义类型
					//cells[n].setCellType(HSSFCell.CELL_TYPE_STRING);
					//定义样式
					boolean locked = false;
					//当此项为主键或者主动锁定，则为true，否则为false。
					if(jsonObject[n].has("pk") && "true".equals(jsonObject[n].getString("pk").toLowerCase())){
						locked = true;
					}
					if(jsonObject[n].has("locked") && "true".equals(jsonObject[n].getString("locked").toLowerCase())){
						locked = true;
					}
					String type = "string";
					if(jsonObject[n].has("type") && jsonObject[n].get("type") != null){
						type = jsonObject[n].getString("type").toLowerCase();
					}
					cells[n].setCellStyle(setStyle(workbook,2,locked,type));
					//获取pojo的属性
					if(!jsonObject[n].has("content")){
						cells[n].setCellValue(new HSSFRichTextString());
						continue;
					}
					String property = jsonObject[n].getString("content");
					String getProperty = "";
					String[] properties = property.split(".");
					//获取内容
					String content = reflectProperty(jsonObject[n].getString("content"),dataset.get(m));//dataset.get(m).get(jsonObject[n].getString("content"))== null ?"":dataset.get(m).get(jsonObject[n].getString("content")).toString();
					
					HSSFRichTextString text = new HSSFRichTextString(content);
					cells[n].setCellValue(text);
				}
	      }
	  
	      ByteArrayOutputStream baos = new ByteArrayOutputStream();
	      workbook.write(baos);
		  byte[] ba = baos.toByteArray();
		  ByteArrayInputStream bais = new ByteArrayInputStream(ba);
		  
 		  return bais;
   }
   /**
    * 获取单元格样式
    * 
    * @param type
    * @return
    */
   private static int getDataFormat(String datatype){
	   int formatNum = 0;
	   if("int".equals(datatype)){
		   formatNum = BuiltinFormats.getBuiltinFormat("0");
	   }
	   else if("string".equals(datatype)){
		   formatNum = BuiltinFormats.getBuiltinFormat("General");
	   }
	   else if("date".equals(datatype)){
		   formatNum = BuiltinFormats.getBuiltinFormat("m/d/yy");
	   }
	   else if("boolean".equals(datatype)){
		   formatNum = BuiltinFormats.getBuiltinFormat("General");
	   }
	   else if("image".equals(datatype)){
		   formatNum = BuiltinFormats.getBuiltinFormat("General");
	   }
	   else{
		   formatNum = BuiltinFormats.getBuiltinFormat("General");
	   }
	   return formatNum;
   }
    /**
     *根据属性名称获取对象的get属性值
     * 
     * @param property 属性名称
     * @param obj 对象
     * @return 根据属性名称获取属性值
     * @throws Exception
     */
	public String reflectProperty(String property,Object obj) throws Exception{
		//属性
		//get属性
		String getProperty = "";
		//返回结果
		String result = "";
		//属性列表
		String[] properties = property.split("\\.");
		
		//反射对象
		Object ob = obj;
		//获取Class实例
		Class cl = ob.getClass();
		//循环获取属性值
		for(int i=0;i<properties.length;i++){
			//拼接get方法
			getProperty = "get"+properties[i].substring(0,1).toUpperCase()+properties[i].substring(1,properties[i].length());
			//反射对象
			Method me = cl.getMethod(getProperty);
			//执行get方法
			Object meResult = me.invoke(ob);
			//如何属性为空，则返回空字符串，返回
			if(meResult == null){
				result = "";
				break;
			}
			//获取属性值
			ob = meResult;
			cl = ob.getClass();
			//递归到最后一个属性的时候，返回属性值
			if(i== properties.length -1)
				result = meResult.toString();
		}
		//返回
		return result;
	}
   /**
    * 此方法用于web环境中，用于定义下载excel的名称
    * 
    * @param fileName 名称（可以放入中文名称）
    * @throws Exception 
    */
   public static void creatExcelName(String fileName)throws Exception{
		//ServletActionContext.getResponse().setHeader("Content-Disposition", "attachment; filename="+new String(fileName.getBytes("gb2312"),"iso-8859-1"));
   }
   /**
    * 这是一个通用的方法，利用了JAVA的反射机制，可以将放置在JAVA集合中并且符合一定条件的数据以EXCEL 的形式输出到指定IO设备上    * 
    * @param title
    *            表格标题名
    * @param headers
    *            表格属性列名数组
    * @param dataset
    *            需要显示的数据集合,集合中一定要放置符合javabean风格的类的对象。此方法支持的
    *            javabean属性的数据类型有基本数据类型及String,Date,byte[](图片数据)
    * @param out
    *            与输出设备关联的流对象，可以将EXCEL文档导出到本地文件或者网络中
    * @param pattern
    *            如果有时间数据，设定输出格式。默认为"yyy-MM-dd"
    */
   @SuppressWarnings("unchecked")
   public void exportExcel(String title, String[] headers,
      Collection<T> dataset, OutputStream out, String pattern) {
      // 声明一个工作薄
      HSSFWorkbook workbook = new HSSFWorkbook();
      // 生成一个表格
      HSSFSheet sheet = workbook.createSheet(title);
      // 设置表格默认列宽度为15个字节
      sheet.setDefaultColumnWidth((short) 15);
      // 生成一个样式
      HSSFCellStyle style = workbook.createCellStyle();
      // 设置这些样式
      style.setFillForegroundColor(HSSFColor.SKY_BLUE.index);
      style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
      style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
      style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
      style.setBorderRight(HSSFCellStyle.BORDER_THIN);
      style.setBorderTop(HSSFCellStyle.BORDER_THIN);
      style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
      // 生成一个字体
      HSSFFont font = workbook.createFont();
      font.setColor(HSSFColor.VIOLET.index);
      font.setFontHeightInPoints((short) 12);
      font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
      // 把字体应用到当前的样式
      style.setFont(font);
      // 生成并设置另一个样式
      HSSFCellStyle style2 = workbook.createCellStyle();
      style2.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
      style2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
      style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
      style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
      style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
      style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
      style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
      style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
      // 生成另一个字体
      HSSFFont font2 = workbook.createFont();
      font2.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
      // 把字体应用到当前的样式
      style2.setFont(font2);
      
      // 声明一个画图的顶级管理器
      HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
      // 定义注释的大小和位置,详见文档
      HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0, 0, 0, 0, (short) 4, 2, (short) 6, 5));
      // 设置注释内容
      comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
      // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
      comment.setAuthor("zdf");
 
      //产生表格标题行
      HSSFRow row = sheet.createRow(0);
      for (short i = 0; i < headers.length; i++) {
         HSSFCell cell = row.createCell(i);
         cell.setCellStyle(style);
         HSSFRichTextString text = new HSSFRichTextString(headers[i]);
         cell.setCellValue(text);
      } 

      //遍历集合数据，产生数据行
      Iterator<T> it = dataset.iterator();
      int index = 0;
      while (it.hasNext()) {
         index++;
         row = sheet.createRow(index);
         T t = (T) it.next();
         //利用反射，根据javabean属性的先后顺序，动态调用getXxx()方法得到属性值
         Field[] fields = t.getClass().getDeclaredFields();
         for (short i = 0; i < fields.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellStyle(style2);
            Field field = fields[i];
            String fieldName = field.getName();
            String getMethodName = "get"
                   + fieldName.substring(0, 1).toUpperCase()
                   + fieldName.substring(1);
            try {
                Class tCls = t.getClass();
                Method getMethod = tCls.getMethod(getMethodName,
                      new Class[] {});
                Object value = getMethod.invoke(t, new Object[] {});
                //判断值的类型后进行强制类型转换
                String textValue = null;
          
                if (value instanceof Boolean) {
                   boolean bValue = (Boolean) value;
                   textValue = "男";
                   if (!bValue) {
                      textValue ="女";
                   }
                } else if (value instanceof Date) {
                   Date date = (Date) value;
                   SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                    textValue = sdf.format(date);
                }  else if (value instanceof byte[]) {
                   // 有图片时，设置行高为60px;
                   row.setHeightInPoints(60);
                   // 设置图片所在列宽度为80px,注意这里单位的一个换算
                   sheet.setColumnWidth(i, (short) (35.7 * 80));
                   byte[] bsValue = (byte[]) value;
                   HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0,
                         1023, 255, (short) 6, index, (short) 6, index);
                   anchor.setAnchorType(2);
                   patriarch.createPicture(anchor, workbook.addPicture(
                         bsValue, HSSFWorkbook.PICTURE_TYPE_JPEG));
                } else{
                   //其它数据类型都当作字符串简单处理
                   textValue = value.toString();
                }
                //如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
                if(textValue!=null){
                   Pattern p = Pattern.compile("^\\d+(\\.\\d+)?$");   
                   Matcher matcher = p.matcher(textValue);
                   if(matcher.matches()){
                      //是数字当作double处理
                      cell.setCellValue(Double.parseDouble(textValue));
                   }else{
                      HSSFRichTextString richString = new HSSFRichTextString(textValue);
                      HSSFFont font3 = workbook.createFont();
                      font3.setColor(HSSFColor.BLUE.index);
                      richString.applyFont(font3);
                      cell.setCellValue(richString);
                   }
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } finally {
                //清理资源
            }
         }
      }
      try {
         workbook.write(out);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
   
   public static void main(String[] args){
	   
   }
}

class Style
{
	private int width;
	private int height;
	
	public Style(){}
	
	public Style(int width,int height)
	{
		this.height = height;
		this.width = width;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
}