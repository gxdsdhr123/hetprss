package com.neusoft.prss.statisticalanalysis.service;

import java.util.Map;

import com.alibaba.fastjson.JSONArray;

public interface WorkloadService {
	
	public  Map<String,Object> getDataList(Map<String,Object> param);

	public JSONArray getJobKinds();

}
