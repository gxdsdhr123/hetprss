/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年11月4日 上午10:31:08
 *@author:gaojingdan
 *@version:[v1.0]
 */
package com.neusoft.prss.rule.entity;

public class RuleRelProcVO {
	private String ruleProcId;
	private String ruleId;//规则ID
	private String procId;//流程ID
	private String sortNum;//序号
	private String arrangeTm;//任务分配时间
	private String variableId;//变量ID
	private String arrangeTmCN;//任务分配时间
	
	/**
	 *@return the arrangeTmCN
	 */
	public String getArrangeTmCN() {
		return arrangeTmCN;
	}
	/**
	 *@param arrangeTmCN the arrangeTmCN to set
	 */
	public void setArrangeTmCN(String arrangeTmCN) {
		this.arrangeTmCN = arrangeTmCN;
	}
	/**
	 *@return the ruleProcId
	 */
	public String getRuleProcId() {
		return ruleProcId;
	}
	/**
	 *@param ruleProcId the ruleProcId to set
	 */
	public void setRuleProcId(String ruleProcId) {
		this.ruleProcId = ruleProcId;
	}
	/**
	 *@return the ruleId
	 */
	public String getRuleId() {
		return ruleId;
	}
	/**
	 *@param ruleId the ruleId to set
	 */
	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}
	/**
	 *@return the procId
	 */
	public String getProcId() {
		return procId;
	}
	/**
	 *@param procId the procId to set
	 */
	public void setProcId(String procId) {
		this.procId = procId;
	}
	/**
	 *@return the sortNum
	 */
	public String getSortNum() {
		return sortNum;
	}
	/**
	 *@param sortNum the sortNum to set
	 */
	public void setSortNum(String sortNum) {
		this.sortNum = sortNum;
	}
	/**
	 *@return the arrangeTm
	 */
	public String getArrangeTm() {
		return arrangeTm;
	}
	/**
	 *@param arrangeTm the arrangeTm to set
	 */
	public void setArrangeTm(String arrangeTm) {
		this.arrangeTm = arrangeTm;
	}
	/**
	 *@return the variableId
	 */
	public String getVariableId() {
		return variableId;
	}
	/**
	 *@param variableId the variableId to set
	 */
	public void setVariableId(String variableId) {
		this.variableId = variableId;
	}
	

}
