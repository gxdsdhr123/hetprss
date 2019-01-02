package com.neusoft.prss.scheduling.entity;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

/**
 * 作业任务实体类
 * 
 * @author baochl
 *
 */
public class JobTaskEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8739718900461391789L;
	private String id;
	private String name;//任务名称
	private String jobKind;// 保障类型
	private String jobType;// 作业类型
	private String jobTypeId;//作业类型id
	private String personId;// 作业人id
	private String person;// 作业人
	private String fltid;// 航班ID
	private String fltNo;// 航班号
	private String fltDate;// 航班日期
	private String inOutFlag;// 进出港标识
	private String processId;// 流程模板
	private String processName;// 流程模板
	private String state;// 保障状态
	private String actArrangeTime;//实际分配时间
	private String startTime;// 开始时间
	private String endTime;// 结束时间
	private String ruleId;//规则ID
	private String ruleProcId;//规则流程模板管理ID
	private String orderId;//流程实例ID
	private String flowTaskId;//流程节点实例id
	private String currTask;//当前节点
	private String autoFlag;//0自动 1手动
	private JSONObject plusData;//任务扩展属性

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public String getFltid() {
		return fltid;
	}

	public void setFltid(String fltid) {
		this.fltid = fltid;
	}

	public String getFltNo() {
		return fltNo;
	}

	public void setFltNo(String fltNo) {
		this.fltNo = fltNo;
	}

	public String getInOutFlag() {
		return inOutFlag;
	}

	public void setInOutFlag(String inOutFlag) {
		this.inOutFlag = inOutFlag;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getJobKind() {
		return jobKind;
	}

	public void setJobKind(String jobKind) {
		this.jobKind = jobKind;
	}

	public String getFltDate() {
		return fltDate;
	}

	public void setFltDate(String fltDate) {
		this.fltDate = fltDate;
	}

	public String getRuleProcId() {
		return ruleProcId;
	}

	public void setRuleProcId(String ruleProcId) {
		this.ruleProcId = ruleProcId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getFlowTaskId() {
		return flowTaskId;
	}

	public void setFlowTaskId(String flowTaskId) {
		this.flowTaskId = flowTaskId;
	}

	public String getJobTypeId() {
		return jobTypeId;
	}

	public void setJobTypeId(String jobTypeId) {
		this.jobTypeId = jobTypeId;
	}

	public String getCurrTask() {
		return currTask;
	}

	public void setCurrTask(String currTask) {
		this.currTask = currTask;
	}

	public JSONObject getPlusData() {
		return plusData;
	}

	public void setPlusData(JSONObject plusData) {
		this.plusData = plusData;
	}

	public String getActArrangeTime() {
		return actArrangeTime;
	}

	public void setActArrangeTime(String actArrangeTime) {
		this.actArrangeTime = actArrangeTime;
	}

	public String getAutoFlag() {
		return autoFlag;
	}

	public void setAutoFlag(String autoFlag) {
		this.autoFlag = autoFlag;
	}
}
