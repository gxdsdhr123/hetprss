/**
 *application name:service-provider-test
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年8月19日 下午1:31:35
 *@author:gaojingdan
 *@version:[v1.0]
 */
package com.neusoft.prss.flight.service;

import com.alibaba.fastjson.JSONArray;

public interface FlightPairService {
	
	/**
	 * @Title: pairFlightId 
	 * @Description: 根据航班日期对航班进行配对
	 * @param date 日期,格式"yyyy-MM-dd"
	 * @return
	 * @throws
	 */
	public String pairFlight(String date) throws Exception;
	
	/**
	 * 
	 * @Title: pairSpecifiedFlight 
	 * @Description: 根据传入的信息对航班进行配对
	 * @param 进出港类型:INOUTFLAG; 机号:AIRCRAFTNUMBER; 航空公司三字码:ALN_3CODE; 航班ID:FLTID;
	 * @param TIME(进港取STA/ETA,出港取STD/ETD)(yyyy-MM-dd HH:mm);
	 * @return String
	 * @throws
	 */
	public JSONArray pairSpecifiedFlight(JSONArray JSONArray);
}
