package com.neusoft.prss.taskmonitor.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.neusoft.framework.common.persistence.annotation.MyBatisDao;
import com.neusoft.prss.taskmonitor.entity.TaskFlightInfo;
import com.neusoft.prss.taskmonitor.entity.TaskInfo;
import com.neusoft.prss.taskmonitor.entity.TaskNode;

@MyBatisDao
public interface TaskMonitor9Dao {

List<HashMap<String, Object>> getPersonTaskData(@Param(value="dayOrNight")String dayOrNight, @Param(value="isPart")Integer isPart);
	
	List<HashMap<String, Object>> getPersonTaskByPersonIds(@Param(value="personIds")List<String> personIds, @Param(value="isPart")Integer isPart);
	
	List<HashMap<String, Object>> getVehicleData();
	
	List<HashMap<String, Object>> getFlightTaskData(@Param(value="ioTag")String ioTag, @Param(value="ynTag")String ynTag);
	
	TaskFlightInfo getOutFlight(@Param(value="fltid")String fltid);

	TaskFlightInfo getInFlight(@Param(value="fltid")String fltid);
	
	TaskFlightInfo getInOutFlight(@Param(value="fltid")String fltid);

	Map<String, Object> getIfTimeConflict(@Param(value="operatorId")String operatorId, @Param(value="taskId")String taskId);

	Integer getIfOffWork(@Param(value="operatorId")String operatorId, @Param(value="taskId")String taskId);
	
	List<Map<String, Object>> getRules(@Param(value="jobType")String jobType, @Param(value="inOut")String inOut);

	List<Integer> getNodeIds(@Param(value="taskId")String taskId);

	HashMap<String, Object> getInFlightByFltId(@Param(value="inFltId")String inFltId);

}
