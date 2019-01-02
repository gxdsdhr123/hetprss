package com.neusoft.prss.gate.service;

import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface GateWebService {

	JSONArray fdGateGanttYData(String userId);
	
	JSONArray fdGateGanttData(Map<String, Object> params);
	
	String removeGate(String fltid,String userId);
	
	String setGate(String fltid, String gate);
	
	String saveGate(String userId);
	
	String cancelGate();

	JSONObject getGateSettings();

	void saveGateSettings(Map<String, String> param);

}
