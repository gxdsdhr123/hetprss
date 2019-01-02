/**
 *application name:worker-arrange-service
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年10月13日14:02:24
 *@author zhang.jinxing
 *@version:[v1.0]
 */
package com.neusoft.prss.arrange.service;

import java.text.ParseException;

/**
 * @author zhang.jinxing
 *
 */
public interface WorkerArrangePlanService {
	
	/**
	 * 固定班制排班
	 * @param sTime1 计划来源开始时间
	 * @param eTime1 计划来源结束时间
	 * @param sTime2 生成开始时间
	 * @param eTime2 生成结束时间
	 * @param officeId 部门
	 * @return
	 * @throws ParseException
	 */
	public int doWorkerArrange(String sTime1,String eTime1,String sTime2,String eTime2,String officeId) throws ParseException;

	/**
	 * 非固定班制排班
	 * @param sTime 开始日期
	 * @param eTime 结束日期
	 * @param officeid 部门
	 * @return
	 * @throws ParseException 
	 */
	public int doWorkerAmbulatoryArrange(String sTime,String eTime,String officeid) throws ParseException;
}
