package com.neusoft.prss.taskmonitor.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.neusoft.prss.common.bean.PageBean;
import com.neusoft.prss.common.bean.ResponseVO;
import com.neusoft.prss.taskmonitor.bean.TaskMonitorVO;
import com.neusoft.prss.taskmonitor.entity.TaskFlightInfo;
import com.neusoft.prss.taskmonitor.entity.TaskInfo;
import com.neusoft.prss.taskmonitor.entity.TaskNode;

public interface TaskMonitorService {

	TaskMonitorVO getTaskMonitorData(String dayOrNight, Integer isPart, Map<String, String> params) throws Exception;
	
	String defSwitch() throws Exception;

	TaskFlightInfo getFlightInfo(String fltid, String type) throws Exception;

	TaskNode getTaskInfo(String taskId) throws Exception;

	List<String> highTasks(String taskId) throws Exception;

	ResponseVO<Map<String, String>> getIfTimeConflict(String operatorId, String taskId,String fltid) throws Exception;

	List<String> getSelectPersons(String taskId) throws Exception;

	List<Map<String, Object>> getRules(String jobType, String inOut, String fltid) throws Exception;

	ResponseVO<String> operatorReturn(String operator, String posType,
			String posName) throws Exception;
	
	List<String> getOnlineUsers() throws Exception;

	JSONArray getShifts() throws Exception;

	String setOverWorkTime(String ids, String start, String end) throws Exception;

	List<String> getWorkingGroupMember(String id) throws Exception;

	String getUnreadErrorNum(String time, String officeId) throws Exception;

	List<HashMap<String,Object>> selectFengong(String operator, String officeId, String schemaId) throws Exception;

	List<HashMap<String,Object>> selectJiWei(String operator, String officeId, String schemaId, String fengongId) throws Exception;

	int saveFengongJiwei(String fengongId, String jiweiId, String schemaId, String operator, String officeId) throws Exception;

	Map<String, Object> getNoCleanData(Map<String, Object> param) throws Exception;

	int saveNoClean(String inFltId,String type) throws Exception;

	int deleteNoCleanByFltId(String inFltId,String type) throws Exception;

	List<Map<String, Object>> getTaskIdsByJobKind(String jobKind,String fltid) throws Exception;
	
	String flightOutScreen(String jobKind,String fltid)  throws Exception;

	String releaseVehicle(String id) throws Exception;

	PageBean<TaskInfo> getTaskList(String operator) throws Exception;

	List<String> startWalkthrough() throws Exception;

	String releaseOperator(String taskId, String personId, boolean autoDispatch) throws Exception;

}
