package com.neusoft.prss.getfltinfo.service;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface GetFltInfoService {
	/** 
	* @Description 获取参数值
	* @param
	* @return JSONObject
	* @update 2017年10月29日 上午10:07:48 hujl 
	*/
	JSONObject queryParamVars(String fltid, String varcols);
	
	/** 
	* @Description 查询colID所对应的参数值
	* @param colID :(List<String> 列ID)
	* @param fltID : 航班号ID
	* @return JSONObject :{"fltID":航班号,"列别名1":value1,"列别名2:value2}
	* @update 2017年10月30日 上午8:56:13 hujl 
	*/
	JSONObject getFltInfo(List<String> colID, String fltID,String planID);
	
	JSONArray getJobNode(String fltid);
	
	

}
