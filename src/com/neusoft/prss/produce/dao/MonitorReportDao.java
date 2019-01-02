package com.neusoft.prss.produce.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.neusoft.framework.common.persistence.annotation.MyBatisDao;
import com.neusoft.prss.produce.entity.MonitorReportEntity;

/**
 * 航班监控报告
 * @author xuhw
 *
 */
@MyBatisDao
public interface MonitorReportDao {

	Integer getDataCount(@Param("searchText")String searchText, @Param("startDate")String startDate, @Param("endDate")String endDate);
	
	List<MonitorReportEntity> getData(@Param("offset")Integer offset, @Param("limit")Integer limit, 
			@Param("searchText")String searchText, @Param("startDate")String startDate, @Param("endDate")String endDate);
	
	MonitorReportEntity getDataById(@Param("id")Integer id);
	
	int saveData(MonitorReportEntity entity);

	int updateData(MonitorReportEntity entity);
	
	int delData(@Param("id")Integer id);
	
	List<Map<String, Object>> getOfficeRestype();
	
	MonitorReportEntity getDataFromView(@Param("flightNumber")String flightNumber,@Param("statDay")String statDay);

	List<String> getProcRecords(@Param("fltids")List<String> fltids, @Param("statDay")String statDay,
			@Param("jobTypeArr")List<String> jobTypeArr);
	
	List<String> getEventRecords(@Param("fltids")List<String> fltids, @Param("statDay")String statDay);

	int deleteReport(@Param("statDay")String statDay, @Param("flightNumber")String flightNumber);
	
	List<Integer> getReport(@Param("statDay")String statDay, @Param("flightNumber")String flightNumber);
	
}
