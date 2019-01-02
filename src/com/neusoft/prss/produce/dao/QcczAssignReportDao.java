package com.neusoft.prss.produce.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface QcczAssignReportDao {

	int getListCount(Map<String, Object> param);

	List<Map<String, Object>> getList(Map<String, Object> param);

	HashMap<String, Object> getDataById(String id);

	JSONArray getFlightDetail(Map<String, Object> map);
	
	JSONArray getFlightDetailPrssp(Map<String, Object> map);

	ArrayList<HashMap<String, Object>> getPeople();

	void saveReport(Map<String, Object> map);

	void saveHis(Map<String, Object> param);

	HashMap<String, Object> getDataDetail(Map<String, Object> param);

	HashMap<String, Object> getDataDetailPrssp(Map<String, Object> param);
	
	ArrayList<HashMap<String, Object>> getSelectPeople(String id);

	int deleteReport(String reportId);

	int deleteHis(String reportId);

	void updateReport(Map<String, Object> map);

	String getPeopleName(String id);

	JSONObject getTaskDetail(Map<String, Object> map);

	JSONObject getTaskDetailPrssp(Map<String, Object> map);

	String getOperator(Map<String, Object> map);

	String getOperatorPrssp(Map<String, Object> map);

}
