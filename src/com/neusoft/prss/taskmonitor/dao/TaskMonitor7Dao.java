package com.neusoft.prss.taskmonitor.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.neusoft.framework.common.persistence.annotation.MyBatisDao;
import com.neusoft.prss.taskmonitor.entity.TaskFlightInfo;
import com.neusoft.prss.taskmonitor.entity.TaskNode;

@MyBatisDao
public interface TaskMonitor7Dao {

	List<HashMap<String, Object>> getPersonTaskData(@Param(value="dayOrNight")String dayOrNight, @Param(value="isPart")Integer isPart);
	
	List<HashMap<String, Object>> getPersonTaskByPersonIds(@Param(value="personIds")List<String> personIds, @Param(value="isPart")Integer isPart);
	
	List<HashMap<String, Object>> getFlightTaskData(@Param(value="yOrN")String yOrN,@Param(value="inOrOut")String inOrOut);
	
	TaskNode getTaskInfo(@Param(value="taskId")String taskId);

	Map<String, Object> getIfTimeConflict(@Param(value="operatorId")String operatorId, @Param(value="taskId")String taskId);

	Integer getIfOffWork(@Param(value="operatorId")String operatorId, @Param(value="taskId")String taskId);
	
	Integer getIfCarEnough(@Param(value="operatorId")String operatorId, 
			@Param(value="taskId")String taskId, @Param(value="fltid")String fltid);

	List<Map<String, Object>> getRules(@Param(value="jobType")String jobType, @Param(value="inOut")String inOut);

	List<Integer> getNodeIds(@Param(value="taskId")String taskId);

	TaskFlightInfo getInFlight(@Param(value="fltid")String fltid);

	TaskFlightInfo getOutFlight(@Param(value="fltid")String fltid);

	TaskFlightInfo getInOutFlight(@Param(value="fltid")String fltid);

	/*******预排*******/
	
	List<HashMap<String, Object>> getPersonWalkthroughData(@Param(value="dayOrNight")String dayOrNight);
	
	List<HashMap<String, Object>> getPersonWalkthroughByPersonIds(@Param(value="personIds")List<String> personIds);

	List<HashMap<String, Object>> getFlightWalkthroughData(@Param(value="area")String area);

	List<String> highTasks(@Param(value="taskId")String taskId);
	
	
}
