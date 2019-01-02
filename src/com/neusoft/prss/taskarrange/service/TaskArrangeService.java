package com.neusoft.prss.taskarrange.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface TaskArrangeService {

	/**
	 * 手动任务分配
	 * 
	 * @author shuyx
	 * @update 2017年11月2日17:09:26
	 */
	JSONArray manualTaskArrange(JSONObject object);

	/**
	 * 根据航班号和规则生成任务
	 * 
	 * @author shuyx
	 * @update 2018年6月26日11:27:21
	 * @param fltId
	 * @param ruleInfo
	 */
	JSONObject taskArrange(JSONObject object);
}
