package com.neusoft.prss.abnormalFlightManagement.entity;

/**
 * 接受页面参数
 * 
 * @author lwg
 * @date 2017/12/19
 *
 */
public class AbnormalFlightVo {
	// 主键
	private String id;
	// 航班id
	private String fltid;
	// 航班日期
	private String flightDate;
	// 航班号
	private String flightNumber;
	// 信息来源
	private String infoSource;
	// 备注
	private String remark;
	// CDM判责
	private String cdmContent;
	// CDM值班经理
	private String cdmUser;
	// CDM判责日期
	private String cdmDate;
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
	// 机号
	private String aircraftNumber;

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

	public String getFltid() {
		return fltid;
	}

	public void setFltid(String fltid) {
		this.fltid = fltid;
	}

	public String getFlightDate() {
		return flightDate;
	}

	public void setFlightDate(String flightDate) {
		this.flightDate = flightDate;
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public String getInfoSource() {
		return infoSource;
	}

	public void setInfoSource(String infoSource) {
		this.infoSource = infoSource;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOperDate() {
		return operDate;
	}

	public void setOperDate(String operDate) {
		this.operDate = operDate;
	}

	public String getCdmContent() {
		return cdmContent;
	}

	public void setCdmContent(String cdmContent) {
		this.cdmContent = cdmContent;
	}

	public String getCdmUser() {
		return cdmUser;
	}

	public void setCdmUser(String cdmUser) {
		this.cdmUser = cdmUser;
	}

	public String getCdmDate() {
		return cdmDate;
	}

	public void setCdmDate(String cdmDate) {
		this.cdmDate = cdmDate;
	}

	public String getAircraftNumber() {
		return aircraftNumber;
	}

	public void setAircraftNumber(String aircraftNumber) {
		this.aircraftNumber = aircraftNumber;
	}
	

}
