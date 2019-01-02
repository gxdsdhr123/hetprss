package com.neusoft.prss.asup.service;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public interface FlightTimeService {
	/**
	 * 计算ETA
	 * @param time 传入时间yyyy-MM-dd HH:mm(std、etd等)
	 * @param actType 机型
	 * @param aptCode 对端机场
	 * @return String yyyy-MM-dd HH:mm
	 */
	String calculateETA(String time,String actType,String aptCode);
	/**
	 * 计算ETA
	 * @param time 传入时间yyyy-MM-dd HH:mm(std、etd等)
	 * @param actType 机型
	 * @param aptCode 对端机场
	 * @param resultFormatter 返回值格式化
	 * @return String yyyy-MM-dd HH:mm
	 */
	String calculateETA(String time,String actType,String aptCode,String resultFormatter);
	/**
	 * 批量获取ETA
	 * @param param [key:{actType:"",aptCode:"",time:"yyyy-MM-dd HH:mm"}]
	 * @return 
	 */
	Map<String,String> calculateETABatch(Map<String,JSONObject> paramMap);
}
