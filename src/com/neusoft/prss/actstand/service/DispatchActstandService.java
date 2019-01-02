/**
 *application name:service-provider-test
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年8月19日 下午1:31:35
 *@author:gaojingdan
 *@version:[v1.0]
 */
package com.neusoft.prss.actstand.service;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface DispatchActstandService {
	
	/**
	 * @Title: dispatchActstandByDate 
	 * @Description: 根据航班日期分配机位
	 * @param date 日期,格式"yyyy-MM-dd"
	 * @return
	 * @throws
	 */
	public JSONArray dispatchActstandByDate(String date);
	
	/**
	 * 
	 * @Title: dispatchActstandByTime 
	 * @Description: 根据输入时间段分配机位
	 * @param startTm 开始时间
	 * @param endTm 结束时间
	 * @return JSONArray
	 * @throws
	 */
	public String dispatchActstandByTime(String startTm, String endTm);
	
	/**
	 * 2018-06-15
	 * @Title: manualSelectActStand 
	 * @Description: 根据输入id选择机型可用机位
	 * @param id: 机位动态表中的id
	 * @return List<String>
	 * @throws
	 */
	public List<String> manualSelectActStandById(String id);
	
	/**
	 * 2018-06-15
	 * @Title: manualJudgeActStand 
	 * @Description: 根据输入id和机位编码，判断是否互斥
	 * @param id: 机位动态表中的id
	 * @param code: 待判断的机位编码
	 * @return JSONObject
	 * @throws
	 */
	public JSONObject manualJudgeActStand(String id, String code);
	
	/**
	 * 
	 * @Title: dispatchActstandForNewFlt 
	 * @Description: 新增航班动态时插入机位动态信息。
	 * @param inFltid 进港航班ID
	 * @param outFltid 出港航班ID
	 * @param standCode 机位编码
	 * @param oper 操作人ID
	 * @return
	 * @throws
	 */
	public String dispatchActstandForNewFlt(String inFltid,String outFltid,String oper);
	
	/**
	 * 
	 * @Title: setFltXTA 
	 * @Description: 更新航班ETA\ATA之后调用此接口，更新机位动态。
	 * @param fltId
	 * @param xta
	 * @param type 0：更新ETA   1：更新ATA
	 * @return String
	 * @throws
	 */
	public String setFltXTA(String fltId,String xta,int type);
	
	/**
	 * 
	 * @Title: setFltXTD 
	 * @Description: 更新航班ETD\ATD之后调用此接口，更新机位动态。
	 * @param fltId
	 * @param xtd
	 * @param type 0：更新ETD   1：更新ATD
	 * @return
	 * @return String
	 * @throws
	 */
	public String setFltXTD(String fltId,String xtd,int type);
	
	/**
	 * 
	 * @Title: deleteBDInfo 
	 * @Description: 根据航班ID删除其对应的机位动态信息。
	 * @param inFltid
	 * @param outFltid
	 * @param oper
	 * @return
	 * @return String
	 * @throws
	 */
	public String deleteBDInfo(String inFltid,String outFltid,String oper);

	/**
	 * 
	 * @Title: cancleFlt 
	 * @Description: 取消航班
	 * @param inFltid
	 * @param outFltid
	 * @param oper
	 * @return
	 * @return String
	 * @throws
	 */
	public String cancleFlt(String inFltid,String outFltid,String oper);
	
	/**
	 * 
	 * @Title: makePair 
	 * @Description: 航班配对
	 * @param inFltid
	 * @param outFltid
	 * @param oper
	 * @return
	 * @return String
	 * @throws
	 */
	public String makePair(String inFltid,String outFltid,String oper);
	
	/**
	 * 
	 * @Title: addStay 
	 * @Description: 自动增加驻场接口
	 * @return
	 * @return String
	 * @throws
	 */
	public String addStay();
}
