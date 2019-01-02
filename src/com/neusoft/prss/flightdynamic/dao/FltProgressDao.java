package com.neusoft.prss.flightdynamic.dao;

import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.persistence.BaseDao;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface FltProgressDao extends BaseDao {
	/**
	 * 获取航班及任务信息
	 * @param params
	 * @return
	 */
	JSONArray getFltList(Map<String, String> params);
	/**
	 * 根据航班ID获取航班详情
	 * 
	 * @param fltid
	 * @return
	 */
	public JSONObject getFltById(String fltid);
	/**
	 * 获取任务列表
	 * 
	 * @param params
	 * @return
	 */
	public JSONArray getJobTaskList(Map<String, String> params);
	/**
	 * 获取任务人员晚到信息
	 * @param params
	 * @return
	 */
	public JSONArray getJobLateState(Map<String, String> params);
}
