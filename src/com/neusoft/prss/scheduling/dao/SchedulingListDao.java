package com.neusoft.prss.scheduling.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.persistence.BaseDao;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface SchedulingListDao extends BaseDao {

	JSONArray getBaseData(Map<String, Object> params);

	JSONArray getOtherData(Map<String, String> otherParam);

	JSONArray getConfigData(Map<String, Object> otherParam);

	JSONArray getFilterOrderSqlbyShema(String schemaid);

	JSONArray getAirlines(Map<String, Object> param);

	int getAirlineTotNum(Map<String, Object> param);

	JSONArray getActTypes(Map<String, Object> param);

	int getActTypeTotNum(Map<String, Object> param);

	int getAirportTotNum(Map<String, Object> param);

	JSONArray getAirports(Map<String, Object> param);

	JSONObject getSemaByOfficeId(@Param("officeId") String officeId);

	JSONObject getFiltConf(@Param("reskind") String reskind);

	void updateSema(@Param("sql") String sql);

	void setSemaLog(@Param("officeId") String officeId, @Param("str") String str, @Param("operator") String operator);

	String getAttrCode(@Param("fileId") String fileId);
	
	String getDGate(@Param("fileId") String fileId);
	
	void updateDGate(@Param("fileId") String fileId, @Param("dGate") String dGate);

	JSONArray getPrintColInfo(@Param("schema")String schema);

	/**
     * 获取装卸调用列表任务指派单航班信息
     */
	JSONObject getFltInfo(Map<String, Object> param);
	
	/**
     * 获取装卸调用列表任务指派单进港货邮行、进港行李解析信息
     */
	JSONArray getFltMailInfo(Map<String, Object> param);
	
	/**
	 * 删除货邮行、进港行李解析信息
	 */
	int delFltMailInfo(Map<String,Object> paramMap);
	
	/**
	 * 更新货邮行、进港行李解析信息
	 */
	int updateFltMailInfo(JSONObject dataObj);
	
	/**
	 * 导入货邮行、进港行李解析信息
	 */
	int importFltMailInfo(List<JSONObject> dataList);
	
	int saveFilterConf(@Param("type") String type,@Param("fieldName") String fieldName, 
			@Param("value") String value);
	JSONArray getFilterConf();
}
