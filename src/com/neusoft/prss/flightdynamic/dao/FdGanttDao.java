/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年10月26日 下午2:29:15
 *@author:SunJ
 *@version:[v1.0]
 */
package com.neusoft.prss.flightdynamic.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface FdGanttDao {

	JSONArray getFdYData();

	JSONArray getFdData(Map<String, Object> params);
	
	JSONArray getOneFltData(Map<String, Object> params);

	String getGanttStatus(@Param("id") String id);

	JSONObject getGanttInfo(@Param("id") String inFltid);

	JSONArray getOneFltGanttY(@Param("fltid") String fltid);

	List<Map<String,Object>> getSingleFlightInfo(Map<String,String> params);

	JSONArray getFdSingleYData(Map<String, String> params);

	JSONArray ganttSingleData(Map<String, String> params);
}
