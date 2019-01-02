package com.neusoft.prss.imax.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.neusoft.framework.common.persistence.annotation.MyBatisDao;

@MyBatisDao("imaxDao")
public interface IMAXDao {

	/*******首页******/
	Map<String, Object> getSafeRunTime_index();
	
	String getLeader_index();
	
	List<Map<String, Object>> getCarNums_index();
	
	List<Map<String, Object>> getPersonNums_index();
	
	List<Map<String, Object>> getFlightNumsList_index();
	
	Map<String, Object> getFlightNums_index();
	
	Map<String, Object> getFlightGf_index();
	
	Map<String, Object> getFlightYw_index();
	
	List<Map<String, Object>> getFlightRate_index();
	
	Map<String, Object> getMonitorNums_index();
	
	List<Map<String, Object>> getDepartmentIllegal_index();
	
	List<Map<String, Object>> getPersonIllegal_index();
	
	/******航班运行情况1*****/
	List<Map<String, Object>> flightChart_run1();
	
	List<Map<String, Object>> flightText_run1();
	
	List<Map<String, Object>> flightTable_run1();
	
	/******航班运行情况2*******/
	Map<String, Object> runText_run2();
	
	List<Map<String, Object>> runTable_run2();
	
	List<Map<String, Object>> runChart_run2();

	List<Map<String, Object>> ywChart_run2();
	
	Map<String, Object> ywText_run2();
	
	/******设备资源情况1********/
	List<String> getDepList_resource1();
	
	List<Map<String, Object>> getMonitorChart_resource1(@Param("depId")String depId);
	
	Map<String, Object> getMonitorText_resource1(@Param("depId")String depId);
	
	Map<String, Object> occupyChart_resource1(@Param("depId")String depId);
	
	List<Map<String, Object>> occupyTable_resource1(@Param("depId")String depId);
	
	
	/******设备资源情况2********/
	List<String> getDepList_resource2();

	List<Map<String, Object>> getMonitorChart_resource2(@Param("depId")String depId);
	
	Map<String, Object> getMonitorText_resource2(@Param("depId")String depId);
	
	Map<String, Object> occupyChart_resource2(@Param("depId")String depId);

	List<Map<String, Object>> occupyTable_resource2(@Param("depId")String depId);

	/******违章******************/
	List<Map<String, Object>> getDept_illegal();

	/******航班保障标准********/
	List<Map<String, Object>> getBarList_monitor();

	List<Map<String, Object>> getTableList_monitor();

	List<Map<String, Object>> getBzObj_monitor(@Param("inOut")String inOut);
}
