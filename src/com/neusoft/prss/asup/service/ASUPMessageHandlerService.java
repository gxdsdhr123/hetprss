package com.neusoft.prss.asup.service;

import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface ASUPMessageHandlerService {
	/**
	 * 获取报文原文
	 * @param eventRecord
	 * @return
	 */
	public String getSource(JSONObject eventRecord);
	/**
	 * 更新航班动态
	 * @param eventRecord
	 * @return
	 */
	public String update(JSONObject eventRecord);
	/**
	 * 获取新增航班信息
	 * @param params
	 * @return
	 */
	public JSONArray getNewFltData(Map<String, String> params);
	/**
	 * 更新事件状态
	 * @param params
	 * @return
	 */
	public void updateEventStatus(Map<String, String> params);
}
