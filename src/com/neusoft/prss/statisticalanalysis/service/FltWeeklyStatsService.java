package com.neusoft.prss.statisticalanalysis.service;

import com.alibaba.fastjson.JSONArray;

public interface FltWeeklyStatsService{
	
	public JSONArray getDataList(String startDate,String endDate);

}
