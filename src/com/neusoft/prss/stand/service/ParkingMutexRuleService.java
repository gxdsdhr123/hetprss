package com.neusoft.prss.stand.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public interface ParkingMutexRuleService {
	
	public Map<String,Object> getDataList(Map<String,Object> param);
	
	public JSONObject getDataById(String id);
	
	public List<Map<String, Object>> getActStand();
	
	public List<Map<String, Object>> getAirCraft();
	
	public boolean save(Map<String,Object> param);

	public boolean edit(Map<String, Object> param);
	
	public boolean delMutex(String id);

}
