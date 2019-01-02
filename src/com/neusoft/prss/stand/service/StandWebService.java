package com.neusoft.prss.stand.service;

import java.util.Map;

import com.alibaba.fastjson.JSONArray;

public interface StandWebService {

	JSONArray fdWalkthroughGanttYData(String userId);

	JSONArray fdWalkthroughGanttData(Map<String, Object> params);

	String lockFlight(String id);

	String unlockFlight(String id);

	String removeStand(String id);

	String setStand(String id, String stand);

	String addStay(String actNum, String stand ,String userId);

	String addStop(Map<String,String> param);

	String hideStand(String stand, String userId);

	JSONArray getHidedStand(String userId);

	String showStand(String stand, String userId);

	String saveStand(String userId);

	String cancelStand();

	void saveDragFlightHis(Map<String, Object> param);

	JSONArray getTaskTable();

	void updateDragFlightHis(Map<String, Object> map);

	void saveDragFlight(Map<String, Object> param);

	void cancelStop(String id);
	
	JSONArray getStopStand(Map<String,String> param);
	
	String editStay(Map<String,String> param);
	
	JSONArray getInOutFlight(String fltId);

	JSONArray getDim(String idCol, String textCol, String tableName);

	String saveInFieldStand(String id, String fltid, String stand, String oldStand, String userId);

	JSONArray fdxlzpGanttYData(String userId);

	JSONArray fdxlzpGanttData(Map<String, Object> params);

	String saveCarousel(JSONArray params, String userId);

	String delStay(String id, String fltid);

	JSONArray keepStandGanttYData();

	String immediateRemoveStand(String id, String user);

	String immediateSetStand(String id, String stand, String user);
}
