/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.neusoft.framework.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonBooleanFormatVisitor;

/**
 * 日期工具类, 继承org.apache.commons.lang.time.DateUtils类
 * @author ThinkGem
 * @version 2014-4-15
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
	
	private static String[] parsePatterns = {
		"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM", 
		"yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
		"yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};
	
	/**
	 * 得到当前日期时间戳
	 */
	public static String getTimestamp() {
		return new Date().getTime()+"";
	}
	
	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd）
	 */
	public static String getDate() {
		return getDate("yyyy-MM-dd");
	}
	
	/**
	 * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String getDate(String pattern) {
		return DateFormatUtils.format(new Date(), pattern);
	}
	
	/**
	 * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
	 */
	public static String formatDate(Date date, Object... pattern) {
		String formatDate = null;
		if (pattern != null && pattern.length > 0) {
			formatDate = DateFormatUtils.format(date, pattern[0].toString());
		} else {
			formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
		}
		return formatDate;
	}
	
	/**
	 * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String formatDateTime(Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前时间字符串 格式（HH:mm:ss）
	 */
	public static String getTime() {
		return formatDate(new Date(), "HH:mm:ss");
	}

	/**
	 * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
	 */
	public static String getDateTime() {
		return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 得到当前年份字符串 格式（yyyy）
	 */
	public static String getYear() {
		return formatDate(new Date(), "yyyy");
	}

	/**
	 * 得到当前月份字符串 格式（MM）
	 */
	public static String getMonth() {
		return formatDate(new Date(), "MM");
	}

	/**
	 * 得到当天字符串 格式（dd）
	 */
	public static String getDay() {
		return formatDate(new Date(), "dd");
	}

	/**
	 * 得到当前星期字符串 格式（E）星期几
	 */
	public static String getWeek() {
		return formatDate(new Date(), "E");
	}
	
	/**
	 * 日期型字符串转化为日期 格式
	 * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", 
	 *   "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
	 *   "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
	 */
	public static Date parseDate(Object str) {
		if (str == null){
			return null;
		}
		try {
			return parseDate(str.toString(), parsePatterns);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 获取过去的天数
	 * @param date
	 * @return
	 */
	public static long pastDays(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(24*60*60*1000);
	}

	/**
	 * 获取过去的小时
	 * @param date
	 * @return
	 */
	public static long pastHour(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(60*60*1000);
	}
	
	/**
	 * 获取过去的分钟
	 * @param date
	 * @return
	 */
	public static long pastMinutes(Date date) {
		long t = new Date().getTime()-date.getTime();
		return t/(60*1000);
	}
	
	/**
	 * 转换为时间（天,时:分:秒.毫秒）
	 * @param timeMillis
	 * @return
	 */
    public static String formatDateTime(long timeMillis){
		long day = timeMillis/(24*60*60*1000);
		long hour = (timeMillis/(60*60*1000)-day*24);
		long min = ((timeMillis/(60*1000))-day*24*60-hour*60);
		long s = (timeMillis/1000-day*24*60*60-hour*60*60-min*60);
		long sss = (timeMillis-day*24*60*60*1000-hour*60*60*1000-min*60*1000-s*1000);
		return (day>0?day+",":"")+hour+":"+min+":"+s+"."+sss;
    }
	
	/**
	 * 获取两个日期之间的天数
	 * 
	 * @param before
	 * @param after
	 * @return
	 */
	public static double getDistanceOfTwoDate(Date before, Date after) {
		long beforeTime = before.getTime();
		long afterTime = after.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
	}
	/**
	 * 获取两个日期之间的天数
	 * 
	 * @param before
	 * @param after
	 * @return
	 * @throws ParseException 
	 */
	public static double getDistanceOfTwoDate(String before, String after,String parsePattern) throws ParseException {
		SimpleDateFormat sdf=new SimpleDateFormat(parsePattern);
		Date beforeDate=sdf.parse(before);
		Date afterDate=sdf.parse(after);
		long beforeTime = beforeDate.getTime();
		long afterTime = afterDate.getTime();
		return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
	}
	
	/**
	 * 日期格式化为当天时返回hh24mi否则返回(dd)hh24mi
	 * @param str
	 * @return
	 */
	public static String formatToFltDate(Object str){
		if(str == null){
			return null;
		}
		try {
			Calendar inDate = Calendar.getInstance();
			inDate.setTime(parseDate(str));
			Calendar now = Calendar.getInstance();
			if(now.get(Calendar.DAY_OF_YEAR) > inDate.get(Calendar.DAY_OF_YEAR) ){
				return formatDate(inDate.getTime(),"(dd)HHmm");
			}else{
				return formatDate(inDate.getTime(),"HHmm");
			}
		} catch (Exception e) {
			return str.toString();
		}
	}
	
	/**
	 * 日期格式化为当天时返回hh24mi否则返回(dd)hh24mi
	 * @param str
	 * @return
	 */
	public static String formatToFltDate(Date date){
		if(date == null){
			return null;
		}
		Calendar inDate = Calendar.getInstance();
		inDate.setTime(date);
		Calendar now = Calendar.getInstance();
		if(now.get(Calendar.DAY_OF_YEAR) > inDate.get(Calendar.DAY_OF_YEAR) ){
			return formatDate(inDate.getTime(),"(dd)HHmm");
		}else{
			return formatDate(inDate.getTime(),"HHmm");
		}
	}
	
	/**
	 * 日期转换
	 * @param dateStr	格式：当天 hh24mi 否则(dd)hh24mi
	 * @return
	 */
	public static Date parseFromFltDate(String dateStr){
		if(StringUtils.isEmpty(dateStr)){
			return null;
		}
		Calendar cal = Calendar.getInstance();
		try {
			if(dateStr.length() > 4){
				Calendar inCal = Calendar.getInstance();
				inCal.setTime(parseDate(dateStr,"(dd)HHmm"));
				cal.set(Calendar.DAY_OF_MONTH, inCal.get(Calendar.DAY_OF_MONTH));
				cal.set(Calendar.HOUR_OF_DAY, inCal.get(Calendar.HOUR_OF_DAY));
				cal.set(Calendar.MINUTE, inCal.get(Calendar.MINUTE));
				if(cal.after(Calendar.getInstance())){
					cal.add(Calendar.MONTH, -1);
				}
			}else{
				Calendar inCal = Calendar.getInstance();
				inCal.setTime(parseDate(dateStr,"HHmm"));
				cal.set(Calendar.HOUR_OF_DAY, inCal.get(Calendar.HOUR_OF_DAY));
				cal.set(Calendar.MINUTE, inCal.get(Calendar.MINUTE));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return cal.getTime();
	}
	/**
	 * 获取今天之前/之后的日期
	 * @param dayNum
	 * @return
	 */
	public static Date getCalculateDate(int dayNum){
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.DATE,dayNum);
		return cal.getTime();
	}
	/**
	 * 获取指定日期之前/之后的日期
	 * @param dayNum
	 * @return
	 * @throws ParseException 
	 */
	public static Date getCalculateDate(String dateStr,int dayNum) throws ParseException{
		Calendar cal=Calendar.getInstance();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		Date date=sdf.parse(dateStr);
		cal.setTime(date);
		cal.add(Calendar.DATE,dayNum);
		return cal.getTime();
	}
	/**
	 * 获取指定日期之前/之后的日期
	 * @param dayNum
	 * @return
	 * @throws ParseException 
	 */
	public static Date getCalculateYear(String dateStr,int yearNum) throws ParseException{
		Calendar cal=Calendar.getInstance();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		Date date=sdf.parse(dateStr);
		cal.setTime(date);
		cal.add(Calendar.YEAR,yearNum);
		return cal.getTime();
	}
	/**
	 * 获取某个周期之前/之后的日期
	 * @param dayNum
	 * @return
	 * @throws ParseException 
	 */
	public static Date getCalculateCycle(String dateStr,int cycleNum) throws ParseException{
		Calendar cal=Calendar.getInstance();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		Date date=sdf.parse(dateStr);
		cal.setTime(date);
		cal.add(Calendar.DATE,cycleNum);
		return cal.getTime();
	}
	/**
	 * 获取指定年月的第一天
	 * @param year
	 * @param month
	 * @param format
	 * @return
	 */
	 public static String getFirstDayOfMonth(int year, int month,String format) {     
         Calendar cal = Calendar.getInstance();   
         //设置年份
         cal.set(Calendar.YEAR, year);
         //设置月份 
         cal.set(Calendar.MONTH, month-1); 
         //获取某月最小天数
         int firstDay = cal.getMinimum(Calendar.DATE);
         //设置日历中月份的最小天数 
         cal.set(Calendar.DAY_OF_MONTH,firstDay);  
         //格式化日期
         SimpleDateFormat sdf = new SimpleDateFormat(format);
         return sdf.format(cal.getTime());  
     }
	/**
	 * 获取指定年月的最后一天
	 * @param year
	 * @param month
	 * @param format
	 * @return
	 */
	 public static String getLastDayOfMonth(int year, int month,String format) {     
         Calendar cal = Calendar.getInstance();     
         //设置年份  
         cal.set(Calendar.YEAR, year);  
         //设置月份  
         cal.set(Calendar.MONTH, month-1); 
         //获取某月最大天数
         int lastDay = cal.getActualMaximum(Calendar.DATE);
         //设置日历中月份的最大天数  
         cal.set(Calendar.DAY_OF_MONTH, lastDay);  
         //格式化日期
         SimpleDateFormat sdf = new SimpleDateFormat(format);  
         return sdf.format(cal.getTime());
     }

	
	
	/**
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
//		System.out.println(formatDate(parseDate("2010/3/6")));
//		System.out.println(getDate("yyyy年MM月dd日 E"));
//		long time = new Date().getTime()-parseDate("2012-11-19").getTime();
//		System.out.println(time/(24*60*60*1000));
		System.out.println(formatToFltDate("2017-11-29 14:20:11"));
		System.out.println(formatToFltDate("asdfa123a"));
		System.out.println(formatDateTime(parseFromFltDate("0714")));
		System.out.println(formatDateTime(parseFromFltDate("(30)0714")));
	}
	

}
