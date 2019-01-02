/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年11月8日 下午3:10:50
 *@author:SunJ
 *@version:[v1.0]
 */
package com.neusoft.prss.scheduling.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.persistence.BaseDao;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface SchedulingGanttDao extends BaseDao {

	JSONArray getSdYData(Map<String, String> param);

	JSONArray getSdData(Map<String, Object> params);

	void cancleTask(@Param("id") String id);

	void allocationTask(@Param("id") String jobTaskId,@Param("actor") String targetActor);

	int getTaskOperatorCount(@Param("id") String jobTaskId,@Param("actor") String targetActor);
	
	int getTaskCount(@Param("id") String jobTaskId);
	
	int getOperatorCount(@Param("actor") String targetActor);

	List<Map<String, String>> getLimitTypes(Map<String, String> param);

	JSONArray getSdQYCYData(Map<String, String> param);

	JSONArray getSdQCSBYData(Map<String, String> param);

	JSONArray getSdQCSBData(Map<String, Object> params);

	JSONObject getGanttInfo(@Param("id") String id);

	JSONArray getSdQCCZYData(Map<String, String> param);

	JSONArray getSdQCCZData(Map<String, Object> params);

	JSONArray getMembers(@Param("id") String id);

	JSONArray getMonitorYData(Map<String, String> param);

	JSONArray getGanttNodeTime(@Param("taskId")String id);

	JSONArray getResGanttArea();

	Long saveResGanttArea(Map<String, String> params);

	void saveResGanttAreaEle(Map<String, Object> datas);

	JSONArray getAllJW();

	JSONArray getJWById(@Param("id") String id);

	JSONArray getJXById(@Param("id") String id);

	JSONArray getJXExceptId(@Param("id") String id);
	
	JSONArray getAllJX();

	void updateResGanttArea(Map<String, String> params);

	void deleteResGanttAreaEle(@Param("id") String id);

	void deleteResGanttArea(@Param("id") String id);

	JSONArray getResGanttSet();

	String getAreaByNum(@Param("num") String num);
	
	String getDefAreaByNum(@Param("num") String num);

	JSONArray getAreaByStands(@Param("stands") String stands);

	JSONObject getAreaInfoByNum(@Param("num") String num);

	JSONArray getAreaByType(@Param("type") String type);

	void deleteResSet(@Param("num") String num);

	int saveResSet(Map<String, String> params);
	
	int updateResSet(Map<String, String> params);

	JSONArray getResGanttYData();

	void stopVehicle(@Param("id") String id, @Param("reason") String reason);

	void resumeVehicle(@Param("id") String id);

	int getVehicleFltCount(@Param("id") String id);

	void updateResCarArea(Map<String, String> params);

	void releaseTasks(@Param("tasks") String tasks);

	JSONArray getStandsByArea(@Param("area") String area);

	JSONArray manualSelectCars(@Param("actType") String actType, @Param("stand") String stand);

	void changeCar(Map<String, String> params);

	void saveResDefaultStartTime(@Param("start") String start);

	void saveResDefaultEndTime(@Param("end") String end);

	JSONArray getResDefaultTimes();

	void truncateTasks(Map<String, String> params);

	JSONArray checkCar(Map<String, String> params);

	void insertCar(@Param("taskId") String taskId, @Param("car") String car);

	void resetWithoutTask(Map<String, String> params);
	/**
	 * 获取当前处于停用状态作业人id
	 * @param param
	 * @return
	 */
	List<String> getCurrentBlockupWorker(Map<String,String> param);

	String getReskind(@Param("officeId") String officeId);

	JSONArray getWthYData(Map<String, String> param);

	JSONArray ganttWthData(Map<String, Object> param);

	void wthResetTask(@Param("id") String jobTaskId);

	JSONArray getOnlineYData(@Param("officeId") String officeId, @Param("onlineStr") String onlineStr);

	void restoreTask(@Param("id") String jobTaskId);

	void logCancleTask(@Param("id") String id, @Param("user") String user);

}
