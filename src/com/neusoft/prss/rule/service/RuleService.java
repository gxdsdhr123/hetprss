/**
 *application name:service-provider-test
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年8月19日 下午1:31:35
 *@author:gaojingdan
 *@version:[v1.0]
 */
package com.neusoft.prss.rule.service;

public interface RuleService {
	/**
	 * 
	 *Discription:执行drools规则
	 *@param fltId航班动态ID
	 *@param ruleId 规则ID
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年11月2日 gaojingdan [变更描述]
	 */
	public boolean doMatch(String fltId, int ruleId);
}
