package com.flying.util;

import java.security.MessageDigest;
/**
 * 
 * <B>描述：</B>MD5加密工具类<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 */
public class MD5 
{ 
	public MD5() 
	{ 
		
	} 
	/**
	 * 进行md5加密
	 * 
	 * @param sourceStr
	 * @return 加密之后的字符串
	 */
	public static String getMD5(String sourceStr) 
	{ 
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'}; 
		try 
		{ 
		   byte[] strTemp = sourceStr.getBytes(); 
		   MessageDigest mdTemp = MessageDigest.getInstance("MD5"); 
		   mdTemp.update(strTemp); 
		   byte[] md = mdTemp.digest(); 
		   int j = md.length; 
		   char str[] = new char[j * 2]; 
		   int k = 0; 
		   for (int i = 0; i < j; i++) 
		   { 
			   byte byte0 = md[i]; 
			   str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中4 位的数字转换, >>> 为辑右移，将符号位右移 
			
			   str[k++] = hexDigits[byte0 & 0xf];  // 取字节中4 位的数字转换 
		   } 
		   return new String(str); 
		} 
			catch (Exception e) 
		{ 
			return null; 
		}  
			
		} 
} 
