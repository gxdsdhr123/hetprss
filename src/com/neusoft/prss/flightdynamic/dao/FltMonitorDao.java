package com.neusoft.prss.flightdynamic.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.neusoft.framework.common.persistence.annotation.MyBatisDao;
import com.neusoft.prss.flightdynamic.entity.FltInfo;
import com.neusoft.prss.flightdynamic.entity.PassengerInfo;
import com.neusoft.prss.flightdynamic.entity.PersonEvent.TimeData;

@MyBatisDao
public interface FltMonitorDao {

	/**
	 * 
	 * @param fltid
	 * @return
	 */
	FltInfo getInFltInfo(@Param(value="fltid")String fltid);
	/**
	 * 
	 * @param fltid
	 * @return
	 */
	FltInfo getOutFltInfo(@Param(value="fltid")String fltid);
	/**
	 * 
	 * @param viewName
	 * @param fltid
	 * @return
	 */
	HashMap<String, Object> getFltmonitorInData(@Param(value="fltid")String fltid);
	
	/**
	 * 
	 * @param inFltid
	 * @param outFltid
	 * @return
	 */
	HashMap<String, Object> getFltmonitorOutData(@Param(value="fltid")String fltid);
	
	/**
	 * 
	 * @param inFltid
	 * @param outFltid
	 * @return
	 */
	HashMap<String, Object> getFltmonitorInOutData(@Param(value="inFltid")String inFltid,
			@Param(value="outFltid")String outFltid);
	
	/**
	 * 
	 * @param ifSuc
	 * @param resultList
	 * @param inFltid
	 * @param outFltid
	 * @param nodeId
	 */
	void getNodeData(HashMap<String, Object> sendMap);
	
	/**
	 * 
	 * @param fltid
	 * @return
	 */
	PassengerInfo getInPassengerInfo(@Param(value="fltid")String fltid);
	
	/**
	 * 
	 * @param fltid
	 * @return
	 */
	PassengerInfo getOutPassengerInfo(@Param(value="fltid")String fltid);
	
	/**
	 * 
	 * @param taskId
	 * @return
	 */
	Map<String, Object> getPersonFlow(@Param(value="taskId")String taskId);
	
	
	List<TimeData> getTaskMsg(@Param(value="taskId")String taskId);
	
	List<TimeData> getAbnormalityEvent(@Param(value="taskId")String taskId);
	
	String getPhoneByUserId(@Param("userId") String userId);
}
