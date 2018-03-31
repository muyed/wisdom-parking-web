package com.muye.wp.dao.typehandlers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class DateHandler {
	public static final String PATTERN_YEAR = "yyyy";
	public static final String PATTERN_YEAR2MONTH = "yyyyMM";
	public static final String PATTERN_YEAR2DAY = "yyyyMMdd";
	public static final String PATTERN_YEAR2_DAY = "yyyy-MM-dd";
	public static final String PATTERN_YEAR2SECOND = "yyyyMMddHHmmss";
	public static final String PATTERN_YEAR2MILLISECOND = "yyyyMMddHHmmssSSS";
	public static final String PATTERN_NORMAL_YEAR2SECOND = "yyyy-MM-dd HH:mm:ss";
	public static final String PATTERN_NORMAL_YEAR2MILLISECOND = "yyyy-MM-dd HH:mm:ss.SSS";

	protected static ThreadLocal<Map<String, DateFormat>> formatLocal = new ThreadLocal<Map<String, DateFormat>>();

	/**
	 * 获取pattern样式的DateFormat 线程安全
	 * 
	 * @param pattern
	 * @return DateFormat
	 */
	public static DateFormat getFormat(String pattern) {
		Map<String, DateFormat> formatMap = formatLocal.get();

		if (formatMap == null) {
			formatMap = new HashMap<String, DateFormat>();
			formatLocal.set(formatMap);
		}

		DateFormat format = formatMap.get(pattern);

		if (format == null) {
			format = new SimpleDateFormat(pattern);
			formatMap.put(pattern, format);
		}

		return format;
	}
	
	/**
	 * 从long型毫秒数获取pattern样式日期字符串
	 * 
	 * @param pattern
	 * @return String
	 */
	public static String getFormat(Long milliseconds,String pattern) {
		
		 Date dat=new Date(milliseconds);  
         GregorianCalendar gc = new GregorianCalendar();   
         gc.setTime(dat);  
         SimpleDateFormat format = new SimpleDateFormat(pattern);  
         String st=format.format(gc.getTime());  
		return st;
	}

	/**
	 * 校验
	 * 
	 * @param dateStr
	 * @param pattern
	 * @return boolean
	 */
	public static boolean valid(String dateStr, String pattern) {
		try {
			getFormat(pattern).parse(dateStr);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	/**
	 * 解析日期字符串
	 * 
	 * @param dateStr
	 * @param pattern
	 * @return Date
	 */
	public static Date parse(String dateStr, String pattern) {
		if (dateStr == null) {
			return null;
		}
		try {
			return getFormat(pattern).parse(dateStr);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 格式化
	 * 
	 * @param date
	 * @param pattern
	 * @return String
	 */
	public static String format(Date date, String pattern) {
		if (date == null) {
			return null;
		}
		return getFormat(pattern).format(date);
	}

	/**
	 * 将时间转换成Long型
	 * 
	 * @param date
	 * @return Long
	 */
	public static Long parseToLong(Date date, String pattern) {
		return Long.valueOf(format(date, pattern));
	}

	/**
	 * 获取当前时间
	 * 
	 * @return Date
	 */
	public static Date now() {
		return new Date();
	}

	public static Long nowInMilliseconds() {
		return System.currentTimeMillis();
	}
	
	public static Integer nowInSeconds() {
		return (int) (System.currentTimeMillis() / 1000);
	}

	/**
	 * 获取一天个开始时间戳
	 * @return Long
	 */ 
	public static Long getBeginDateOfDay(Date date){
		String dateStr = DateHandler.format(date, DateHandler.PATTERN_YEAR2DAY);
		Date targetDate = DateHandler.parse(dateStr, DateHandler.PATTERN_YEAR2DAY);
		return targetDate.getTime();
	}
	
	/**
	 * 获取下一天个开始时间戳
	 * @return Long
	 */
	public static Long getBeginDateOfNextDay(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, 1);
		return getBeginDateOfDay(calendar.getTime());
	}
	
	/**
	 * 根据生日毫秒数获取年龄
	 * @return Integer
	 */
	public static Integer getAge(Long birthmilliseconds){
		Integer birth= Integer.parseInt(getFormat(birthmilliseconds,PATTERN_YEAR));
		Integer now= Integer.parseInt(getFormat(System.currentTimeMillis(),PATTERN_YEAR));
		return now-birth;
	}

}
