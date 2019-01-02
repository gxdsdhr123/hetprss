package com.neusoft.prss.abnormalFlightManagement.entity;

import java.util.Date;

/**
 * 不正常航班管理实体类
 * @author lwg
 * @date 2017/12/19
 */
public class FdFltAbnorrmal {
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
	// 运控值班员
	private String operatror;
	// 日期
	private Date operDate;
	// CDM判责
	private String cdmContent;
	// CDM值班经理
	private String cdmUser;
	// CDM判责日期
	private String cdmDate;
	// 机号
	private String aircraftNumber;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getOperatror() {
		return operatror;
	}
	public void setOperatror(String operatror) {
		this.operatror = operatror;
	}
	public Date getOperDate() {
		return operDate;
	}
	public void setOperDate(Date operDate) {
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
