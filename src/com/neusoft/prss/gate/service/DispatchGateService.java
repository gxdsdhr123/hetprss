/**
 *application name:service-provider-test
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年8月19日 下午1:31:35
 *@author:gaojingdan
 *@version:[v1.0]
 */
package com.neusoft.prss.gate.service;

import java.util.List;

public interface DispatchGateService {
	
	/**
	 * 
	 * @Title: dispatchGate 
	 * @Description: 登机口分配接口
	 * @param fltList 航班列表
	 * @return String
	 * @throws
	 */
	public String dispatchGate(List<String> fltList);
	
	/**
	 * 2018-08-14
	 * @Title: manualSelectGate 
	 * @Description: 根据输入fltid选择机型可用机位
	 * @param fltid:
	 * @return List<String>
	 * @throws
	 */
	public List<String> manualSelectGateByFltid(List<String> fltList);

}
