/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年12月2日 下午3:47:07
 *@author:SunJ
 *@version:[v1.0]
 */
package com.neusoft.prss.scheduling.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;

public interface SchedulingHisGanttService {

	JSONArray getSdYData(String officeId, String hisDate);

	String ganttData(Map<String, Object> params);

	Map<String, Object> getSingleHisFlightInfo(Map<String, String> params);

	JSONArray getFdSingleHisYData(Map<String, String> params);

	String ganttSingleHisData(Map<String, String> params);

	List<List<String>> getGanttDetail(String id, String schemaId);

}
