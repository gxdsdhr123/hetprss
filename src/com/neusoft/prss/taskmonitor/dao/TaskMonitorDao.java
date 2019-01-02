package com.neusoft.prss.taskmonitor.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONArray;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;
import com.neusoft.prss.taskmonitor.entity.TaskInfo;
import com.neusoft.prss.taskmonitor.entity.TaskNode;
import com.neusoft.prss.taskmonitor.entity.TaskNode.Node;

@MyBatisDao
public interface TaskMonitorDao {

	Integer getDefSwitch();

	List<String> highTasks(@Param(value="taskId")String taskId);

	Map<String, Object> getJmTask(@Param(value="taskId")String taskId);

	int saveWorkerPos(Map<String, Object> newWorkerPos);

	int getUnfinishTaskCount(@Param(value="operator")String operator);

	JSONArray getShifts(@Param(value="officeId")String officeId);

	JSONArray getWorkerShiftsInfo(@Param(value="workerId")String workerId , @Param(value="plus")String plus);

	void setOverWorkTime(@Param(value="officeId")String officeId, @Param(value="workerId")String wokerId, 
			@Param(value="startTime")String startTime, @Param(value="endTime")String endTime, 
			@Param(value="start")String start, @Param(value="end")String end, @Param(value="plus")String plus);

	TaskNode getTaskInfo(@Param(value="taskId")String taskId);
	
	List<Node> getNodeIds(@Param(value="taskId")String taskId);
	
	List<Node> getNodeVal(@Param(value="taskId")String taskId);

	List<String> getWorkingGroupMember(@Param(value="officeId")String officeId, @Param(value="id")String id);

	int getUnreadErrorNum(@Param(value="time") String time, @Param(value="officeId") String officeId);

	List<HashMap<String,Object>> selectFengong(@Param(value="operator")String operator,
			@Param(value="officeId")String officeId,@Param(value="schemaId")String schemaId);

	List<HashMap<String,Object>> selectJiWei(@Param(value="operator")String operator,
			@Param(value="officeId")String officeId,@Param(value="schemaId")String schemaId,@Param(value="fengongId")String fengongId);

	int saveFengongJiwei(@Param(value="fengongId")String fengongId,@Param(value="jiweiId")String jiweiId,
			@Param(value="schemaId")String schemaId);

	String getJiWeiNameById(@Param(value="jiweiId")String jiweiId);
	
	List<Map<String, Object>> getTaskIdsByJobKind(@Param(value="jobKind")String jobKind, @Param(value="fltid")String fltid);

	int flightOutScreen(@Param(value="jobKind")String jobKind,@Param(value="fltid")String fltid);

	HashMap<String, Object> getInFlightByFltId(String inFltId);

	int saveNoClean(HashMap<String, Object> map);

	int getListCount(Map<String, Object> param);

	List<Map<String, Object>> getList(Map<String, Object> param);

	int deleteNoCleanByFltId(@Param(value="inFltId")String inFltId,@Param(value="type")String type);

	int releaseVehicle(@Param(value="id")String id);

	int logReleaseVehicle(@Param(value="id")String id, @Param(value="userId")String userId, @Param(value="officeId")String officeId);

	List<TaskInfo> getTaskList(@Param(value="operator")String operator, @Param(value="jobKind")String jobKind);

	List<Map<String, String>> getWalkthroughTask(@Param(value="jobKind")String jobKind);

	int releaseTaskOperator(@Param(value="taskId")String taskId);
}
