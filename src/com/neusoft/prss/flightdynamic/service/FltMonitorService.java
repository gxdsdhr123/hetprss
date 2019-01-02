package com.neusoft.prss.flightdynamic.service;

import java.util.Map;

import com.neusoft.prss.flightdynamic.bean.FlitMonitorData;
import com.neusoft.prss.flightdynamic.entity.FltInfo;

public interface FltMonitorService {

	/**
	 * 获取保障图数据
	 * @param inFltid
	 * @param outFltid
	 * @param isY
	 * @param hisFlag 
	 * @return
	 * @throws Exception 
	 */
	Map<String, Object> getFltmonitorData(String inFltid, String outFltid,
			String isY, String hisFlag) throws Exception;

	/**
	 * 获取航行数据
	 * @param inFltid
	 * @param outFltid
	 * @param hisFlag 
	 * @return
	 * @throws Exception 
	 */
	FltInfo getFltInfo(String inFltid, String outFltid, String hisFlag) throws Exception;

	/**
	 * 节点弹出数据
	 * @param fltid
	 * @param nodeId
	 * @param hisFlag 
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getNodeData(String inFltid, String outFltid,
			String nodeId, String hisFlag) throws Exception;

	/**
	 * 旅客详细信息
	 * @param fltid
	 * @param inout
	 * @param hisFlag 
	 * @return
	 */
	Map<String, Object> getPassengerInfo(String fltid, String inout, String hisFlag) throws Exception;

	/**
	 * 人员事件详情
	 * @param fltid
	 * @param personCode
	 * @param taskId
	 * @param hisFlag 
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getPersonEvent(String fltid, String personCode,
			String taskId, String hisFlag) throws Exception;

	/**
	 * 根据用户ID获取电话号码，用于通信
	 * @param userId
	 * @return
	 */
	String getPhoneByUserId(String userId) throws Exception;

}
