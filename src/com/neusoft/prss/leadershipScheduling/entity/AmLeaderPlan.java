package com.neusoft.prss.leadershipScheduling.entity;
/**
 * 领导排班计划
 * @author LWG
 * @date 2017/12/08
 */
public class AmLeaderPlan {
	// id
	private String id;
	// 排班日期
	private String pdate;
	// 员工id
	private String workerId;
	// 开始时间
	private String startTm;
	// 结束时间
	private String endTm;
	// 部门id
	private String officeId;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPdate() {
		return pdate;
	}
	public void setPdate(String pdate) {
		this.pdate = pdate;
	}
	public String getWorkerId() {
		return workerId;
	}
	public void setWorkerId(String workerId) {
		this.workerId = workerId;
	}
	public String getStartTm() {
		return startTm;
	}
	public void setStartTm(String startTm) {
		this.startTm = startTm;
	}
	public String getEndTm() {
		return endTm;
	}
	public void setEndTm(String endTm) {
		this.endTm = endTm;
	}
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	
}
