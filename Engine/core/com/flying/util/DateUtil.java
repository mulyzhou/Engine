package com.flying.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 
 * <B>描述：</B>日期工具类<br/>
 * <B>版本：</B>v2.0<br/>
 * <B>创建时间：</B>2012-10-10<br/>
 * <B>版权：</B>flying团队<br/>
 * 
 * @author zdf
 *
 */
public class DateUtil {
	private final static String dateRegex = "(?:[0-9]{1,4}(?<!^0?0?0?0))-(?:0?[1-9]|1[0-2])-(?:0?[1-9]|1[0-9]|2[0-8]|(?:(?<=-(?:0?[13578]|1[02])-)(?:29|3[01]))|(?:(?<=-(?:0?[469]|11)-)(?:29|30))|(?:(?<=(?:(?:[0-9]{0,2}(?!0?0)(?:[02468]?(?<![13579])[048]|[13579][26]))|(?:(?:[02468]?[048]|[13579][26])00))-0?2-)(?:29)))";
	
	private final static String datetimeRegex = "(?:[0-9]{1,4}(?<!^0?0?0?0))-(?:0?[1-9]|1[0-2])-(?:0?[1-9]|1[0-9]|2[0-8]|(?:(?<=-(?:0?[13578]|1[02])-)(?:29|3[01]))|(?:(?<=-(?:0?[469]|11)-)(?:29|30))|(?:(?<=(?:(?:[0-9]{0,2}(?!0?0)(?:[02468]?(?<![13579])[048]|[13579][26]))|(?:(?:[02468]?[048]|[13579][26])00))-0?2-)(?:29)))";

	/**
	 * 验证字符串是否为日期格式
	 * @param date
	 * @return 是否合格
	 */
	public static boolean dateVerify(String date){
		Pattern pattern = Pattern.compile(dateRegex);
        Matcher matcher = pattern.matcher(date);
        
        return matcher.matches();
	}
	/**
	 * 验证字符串是否为日期格式
	 * @param datetime
	 * @return 是否合格
	 */
	public static boolean datetimeVerify(String date){
		Pattern pattern = Pattern.compile(datetimeRegex);
        Matcher matcher = pattern.matcher(date);
        
        return matcher.matches();
	}
	/**
	 * 将时间字符串转化成日期
	 * 
	 * @param dateStr
	 * @param format
	 * @return 将字符串变成日期
	 */
	public static Date stringToDate(String dateStr,String format){
		if("".equals(format) || format == null){
			format = "yyyy-MM-dd";
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		
		Date date = null;
		
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return date;
	}
	
	public static String dateToString(Date date,String format){
		if("".equals(format) || format == null){
			format = "yyyy-MM-dd";
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		
		String dateStr = sdf.format(date);
		
		return dateStr;
	}
}
