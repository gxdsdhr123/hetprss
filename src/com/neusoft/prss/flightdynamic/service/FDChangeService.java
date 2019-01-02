package com.neusoft.prss.flightdynamic.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.neusoft.prss.flightdynamic.bean.FDChangeFltDate;

/**
 * 航班动态列表双击单元格处理Service
 * @author baochl
 *
 */
public interface FDChangeService {
	
	
	
	public boolean saveAircraftNumber(List<String> list,String actNumber);
	
	public boolean saveAlnProperty(String fltid,String newPropertyCode);
	
	public boolean saveAttrCode(String fltid,String newValue);
	
	public boolean saveFltDate(FDChangeFltDate changeFltDate);

	public boolean saveActType(List<String> list,String newActType);
	
	public boolean saveBay(List<String> idList,String bay);
	
	public boolean saveDelayReason(JSONObject object);
	
	public boolean saveDepartSort(String fltid,String newValue);
	
	public boolean saveDiversion(String fltid,String diversionPort,String diversionRes,
			String diversionATD,String diversionResDetail);
	
	
	/**
	 * 检查机型和机位是否冲突
	 * @param newActType 机型
	 * @param actStandCode 机位
	 * @return
	 */
	public boolean checkActStand(String newActType ,String actStandCode);
	

	/**
	 * 从维表中获取当前机号对应的机型
	 * @param actNumber
	 * @return
	 */
	
	public String getDimActType(String actNumber);
	
	/**
	 * 根据fltid获取列的信息
	 * @param fltid
	 * @return
	 */
	public List<Map<String,String>> getDataById(String fltid);
	
	/**
	 * 根据fltid获取DEPART表的列信息
	 * @param fltid
	 * @return
	 */
	public List<Map<String,String>> getDepartDataById(String fltid);

	
	public boolean savePushoutTime(String fltid,String newValue);
	
	public boolean saveGateOrBagCrsl(String fltid,String newValue,String type);
	
	public boolean saveGroundHOTim(String fltid,String newValue);
	
	public void saveBoardingStatus(String fltid, String boardingStatus, String oldval, String user);

	public void saveGate(String fltid, String newVal, String oldval, String user);
	
	public JSONObject getCounters(String fltid);

	public void saveCounter(String fltid, String counter, String oldval, String user);

	public void saveCounterStatus(String fltid, String counterStatus, String oldval, String user);

	public boolean saveBrdTime(String fltid, String field, String brdTime);
	
	public boolean saveTZDJKTime(String fltid, String field, String brdTime);
}
