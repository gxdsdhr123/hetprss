/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2018 Neusoft LTD.
 *company:Neusoft
 *time:2018年3月5日 上午8:46:11
 *@author:Heqg
 *@version:[v1.0]
 */
package com.neusoft.prss.flightdynamic.service;

import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
public interface FdRelService {
	/**
	 * 机号拆分查询
	 * @param fltNo
	 * @param outFltId 
	 * @param inFltId 
	 * @param fltDate
	 * @return
	 */
	public JSONArray getFltIO(Map<String,String> params);
	/**
	 * 保存配对
	 * @param arr
	 * @return
	 */
	public JSONObject saveRel(JSONArray dataList,String userId);
}
