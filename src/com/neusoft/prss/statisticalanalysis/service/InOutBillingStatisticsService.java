package com.neusoft.prss.statisticalanalysis.service;

import com.alibaba.fastjson.JSONArray;

public interface InOutBillingStatisticsService {

	String getDataListCount(String startDate, String endDate, String inOut);

	JSONArray getDataList(Integer pageNumber, Integer pageSize, String startDate, String endDate, String inOut);

	JSONArray getAllDataList(String startDate, String endDate, String inOut);
	
}
