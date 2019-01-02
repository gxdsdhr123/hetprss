package com.neusoft.prss.fltinfo.service;

import com.alibaba.fastjson.JSONObject;

public interface UpdateFltinfoService {
	
	/**
	 * 解析PLN/PLAN报文
	 * 
	 * @author mby
	 * @param updateFltinfo
	 * @return
	 */
	public String updateFltinfo();
	
	/**
	 * 更新动态后向事件表event_record_t中插入记录
	 * @param fltid 航班ID
	 * @param oldValue 变更前的值
	 * @param newValue 变更后的值
	 * @param operator 操作人ID
	 * @param tabName 表名
	 * @param colName 列名
	 * @return
	 */
	public String insertEvent(String fltid,String oldValue,String newValue,String operator,String tabName,String colName);

	/**
	 * 计划发布事件插入
	 * @param fltids 所有需要发布的fltid,包括other表
	 * @param operator 操作人ID
	 * @param type 0:计划发布，1：新增航班
	 * @return
	 */
	public String insertSCHDEvent(String fltids,String operator,int type);
	
	/**
	 * 机号变更时更新机位动态的接口
	 * @param aircraftNo 新机号
	 * @param inFltId 进港航班id
	 * @param outFltid 出港航班id
	 * @param oper 操作者id
	 * @return
	 */
	public JSONObject updateStandInfoByFltNo(String aircraftNo,String inFltId,String outFltid,String oper);
	
	/**
	 * 判断机号变更是否可行
	 * @param aircraftNo
	 * @param inFltId
	 * @param outFltid
	 * @return
	 */
	public JSONObject getAlertInfo(String aircraftNo,String inFltId,String outFltid);
}
