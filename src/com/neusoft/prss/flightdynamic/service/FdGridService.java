package com.neusoft.prss.flightdynamic.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface FdGridService {

	@Transactional(readOnly = true)
	public String getNewFltId();

	public JSONArray getOptions(Map<String, Object> param);

	public int getOptTotNum(Map<String, Object> param);

	boolean ifStopOver(JSONArray detailArray);

	public JSONArray getTelegraphXmlData(Map<String, String> data);

	public JSONArray getChg(String fltid, String item);

	public JSONObject getNumOfPassenger(String fltid);

	public void updateNumOfPassenger(String fltid, String dPaxNum, String iPaxNum, String paxNum);

	public JSONArray getExp(String officeId, String fltid, String img, String vol, String vid);

	public List<String> getFileId(String id, String type);

	public byte[] downloadFile(String id);

	public String getFileName(String fileId);

	public String getAttrCode(String id);

	public boolean isByHand(String fltid);

	public JSONArray getCarousel();

	public JSONArray getBay();

	public JSONArray getChute();

	public JSONArray getCounter();

	/**
	 * 新增保存航班动态
	 */
	public JSONObject saveFlt(JSONObject data, Map<String, Object> params);

	public void cancleFlt(String fltid, String type);

	public JSONObject delFlt(Map<String, String> params);

	public JSONObject getFltInfo(String sql);

	public JSONObject dOriAndAttrCode(String fltid);

	public JSONArray getTransferInfo(String fltid);

	public JSONObject getLDMPerson(String fltid, String type, String ship);

	public String getLDMid(String fltid);

	public void updateLDMPerson(String ldmid, String total, String adult, String children, String baby, String crew,
			String jumpseat, String type, String shipping_type);

	public void delTransferInfo(String ldmid);

	public void updateTransferInfo(String ldmid, String destination, String personnum);

	public String getAirline2Code(String fltid);

	public JSONArray getTmCode(String aln2Code);

	public JSONArray getDelayInfo(String fltid);

	public JSONObject getPlusInfo(String fltid);

	public void updatePlusInfo(String fltid, String captainName, String duration);

	public void delDelayInfo(String fltid);

	public void insertDelayInfo(String fltid, JSONObject delayInfo, String user);

	public JSONArray getDelayType();

	public JSONObject getFltTimeData(Map<String, String> params);

	public void saveFltData(Map<String, String> params);

	public JSONArray getVipInfo(String fltid);

	public JSONArray getVipFlag();

	public void delVipInfo(String fltid);

	public void emptiedVipInfo();

	public void insertVipInfo(String fltid, JSONObject vipInfo, String user);

	public JSONArray getVipImportDate();

	public void delVipInfoById(String id);

	public void synchroVip();

	public JSONArray showDetailInfo(Map<String, String> param);

	/**
	 * 校验录入机号是否与机型匹配
	 * 
	 * @param param
	 * @return
	 */
	public int validActNumber(Map<String, String> param);

	/**
	 * 获取进出港性质
	 * 
	 * @return
	 */
	public JSONArray getFlightProperty();

	public void insertFltInfoRecycle(String fltId);

	public JSONArray getBaseData(Map<String, Object> param);

	/**
	 * 
	 * Discription:获取报文列表.
	 * 
	 * @param param
	 * @return
	 * @return:返回值意义
	 * @author:l.ran@neusoft.com
	 * @update:2018年5月15日 neusoft [变更描述]
	 */
	public Map<String, Object> getTelegraphList(Map<String, String> param);

	/**
	 * 
	 * Discription:获取报文详情.
	 * 
	 * @param param
	 * @return
	 * @return:返回值意义
	 * @author:l.ran@neusoft.com
	 * @update:2018年5月15日 neusoft [变更描述]
	 */
	public JSONObject getTelegraphInfo(Map<String, String> param);

	public JSONArray getFltInfo(String inFltId, String outFltId);

	/**
	 * 航班动态修改也表格
	 * 
	 * @param param
	 * @return
	 */
	public JSONArray getFormGridData(Map<String, String> param);
	/**
	 * 判断航班是否存在
	 * @param param
	 * @return
	 */
	public int isFltExist(Map<String, String> param);

	public JSONArray getPassengerData();
	
	/*
	 * 值机柜台相关
	 */
	public JSONArray getIslands();

	public JSONArray getCounters();

	public JSONArray getAirlines(String param);

	public String getChoosedAirlines(String dim, String island);

	public void saveIslandAllot(String dim, String island, String airlines, String user);

	public void saveMIslandAllot(JSONArray dataArray, String delStr, String user);

	public JSONArray getMData();

	public JSONArray getCounterAllotTable();

	public void deleteCounterAllotById(String ids);
	
	/**
     * 根据离港航班fltid，获取离港航班初始航班、登机口信息
     */
    public JSONArray getOutFltInfo(Map<String,Object> paramMap);
}
