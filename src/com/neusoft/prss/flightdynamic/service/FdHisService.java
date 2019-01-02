/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年11月27日 下午4:16:11
 *@author:yu-zd
 *@version:[v1.0]
 */
package com.neusoft.prss.flightdynamic.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.prss.flightdynamic.entity.FltInfo;
import com.neusoft.prss.flightdynamic.entity.PassengerInfo;
import com.neusoft.prss.flightdynamic.entity.PersonEvent.TimeData;

public interface FdHisService {

    public JSONArray getChg(String fltid,String item);

    public JSONObject getNumOfPassenger(String fltid);

    public JSONArray getTelegraphXmlData(Map<String,String> data);

    public JSONArray getDynamic(Map<String,Object>... params);
    /**
     * 获取航行数据
     * 
     * @param inFltid
     * @param outFltid
     * @return
     */
    FltInfo getFltInfo(String inFltid,String outFltid);

    public JSONArray getVipHis(String hisDate);

    public JSONArray getVipInfo(String fltid);
    /**
     * 获取外航数据录入信息
     * @param ldmid
     * @param destination
     * @param personnum
     */
    public JSONObject getFltTimeData(Map<String,String> params);

    public JSONArray getExp(String officeId,String fltid,String hisDate,String img,String vol,String vid);

    public JSONObject dOriAndAttrCode(String fltid);

    public JSONArray getTransferInfo(String fltid);

    public JSONObject getLDMPerson(String fltid,String type,String ship);

    public JSONObject getPlusInfo(String fltid);

    public JSONArray getCargoData(String fltid);
    
    public JSONArray getDelayInfo(String fltid);
    /**
     * 
     *Discription:获取报文列表.
     *@param param
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年5月15日 neusoft [变更描述]
     */
    public Map<String,Object> getTelegraphList(Map<String,String> param);
    /**
     * 
     *Discription:获取报文详情.
     *@param param
     *@return
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2018年5月15日 neusoft [变更描述]
     */
    public JSONObject getTelegraphInfo(Map<String,String> param);
    
    /**
	 * 获取历史航班动态航班信息
	 * @param inFltId
	 * @param outFltId
	 * @return
	 */
    public JSONArray getHisFltInfo(@Param("inFltId")String inFltId, @Param("outFltId")String outFltId);

    /**
     * 获取历史航班动态form页面表格信息
     * @param param
     * @return
     */
	public JSONArray getFormGridData(Map<String, String> param);
	
    /* ----------------------------------监控图--------------------------*/

    /**
     * 
     * @param fltid
     * @return
     */
    FltInfo getInFltInfo(String fltid);

    /**
     * 
     * @param fltid
     * @return
     */
    FltInfo getOutFltInfo(String fltid);


	public HashMap<String, Object> getFltmonitorInOutData(String inFltid,
			String outFltid);

	public HashMap<String, Object> getFltmonitorInData(String inFltid);

	public HashMap<String, Object> getFltmonitorOutData(String outFltid);
	
    /**
     * 
     * @param ifSuc
     * @param resultList
     * @param inFltid
     * @param outFltid
     * @param nodeId
     */
	HashMap<String, Object> getNodeData(HashMap<String,Object> sendMap);

    /**
     * 
     * @param fltid
     * @return
     */
    PassengerInfo getInPassengerInfo(String fltid);

    /**
     * 
     * @param fltid
     * @return
     */
    PassengerInfo getOutPassengerInfo(String fltid);

    /**
     * 
     * @param taskId
     * @return
     */
    Map<String,Object> getPersonFlow(String taskId);

    List<TimeData> getTaskMsg(String taskId);

    List<TimeData> getAbnormalityEvent(String taskId);
    
}
