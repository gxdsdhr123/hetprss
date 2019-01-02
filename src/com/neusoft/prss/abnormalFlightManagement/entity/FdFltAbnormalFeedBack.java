package com.neusoft.prss.abnormalFlightManagement.entity;
/**
 * 不正常航班反馈实体类
 * @author lwg
 * @date 2017/12/19
 */
public class FdFltAbnormalFeedBack {
	// 主键
	private String id;
	// 关联FD_FLT_ABNORMAL的ID
	private String abnormalId;
	// 部门ID
	private String officeId;
	// 反馈信息
	private String content;
	// 值班员
	private String operatror;
	// 反馈日期
	private String operDate;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAbnormalId() {
		return abnormalId;
	}
	public void setAbnormalId(String abnormalId) {
		this.abnormalId = abnormalId;
	}
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getOperatror() {
		return operatror;
	}
	public void setOperatror(String operatror) {
		this.operatror = operatror;
	}
	public String getOperDate() {
		return operDate;
	}
	public void setOperDate(String operDate) {
		this.operDate = operDate;
	}
	
	

}
