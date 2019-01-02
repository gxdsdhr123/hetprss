package com.neusoft.prss.flightdynamic.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FDChangeUtil {
	
	public static String getParseFltDate(String oldValue,String newValue,String dateStr) throws ParseException{
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Date oldDate = df.parse(oldValue);
		Date newDate = df.parse(newValue);
		long cha=((newDate.getTime()-oldDate.getTime())/(24*60*60*1000));
		Date strDate=df2.parse(dateStr);
		Date newSTD=new Date(strDate.getTime()+(cha*24*60*60*1000));
		return df2.format(newSTD);
	}
}
