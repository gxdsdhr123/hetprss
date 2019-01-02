package com.neusoft.prss.plan.service;

import java.util.List;
import java.util.Map;


public interface PlanQueryService {
	
	public Map<String,Object> getLeftDataList(Map<String,Object> param);
	
	public Map<String,Object> getRightDataList(Map<String,Object> param);
	
	public List<Map<String, Object>> getAirlines();
	
	public List<Map<String, Object>> getAirports();
	
	public List<Map<String, Object>> getChartData(Map<String,Object> param);
}
