package com.flying.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA1 {
	public SHA1(){
		
	}
	
	/**
	 * 进行sha1加密
	 * 
	 * @param sourceStr
	 * @return 加密之后的字符串
	 */
	public static String getSHA1(String sourceStr) 
	{ 
		try {
            MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
            digest.update(sourceStr.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();
 
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
	}
}
