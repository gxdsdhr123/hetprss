package com.neusoft.prss.statisticalanalysis.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public interface KCQJStatisticsService {
	
	public  Map<String,Object> getDataList(Map<String,Object> param);
	
	public  Map<String,Object> getMonthlyList(Map<String,Object> param);
	
	public Map<String,String> getTcCoefficient();
	
	public void saveTcCoefficient(List<JSONObject> list);

}
