package com.neusoft.prss.produce.entity;

import java.math.BigDecimal;

public class BillCommonEntity {
	private BigDecimal id;
	private String fltid;
	private String flightDate;
	private String flightNumber;
	private String aln2code;
	private String aln3code;
	private String acttypeCode;
	private String aircraftNumber;
	private String remark;
	private String updateTime;
	private String createUser;
	private String inOutFlag;
	private String operator;
	private String operatorName;
	private String sign;
	public BigDecimal getId() {
		return id;
	}
	public void setId(BigDecimal id) {
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
	public String getAln2code() {
		return aln2code;
	}
	public void setAln2code(String aln2code) {
		this.aln2code = aln2code;
	}
	public String getAln3code() {
		return aln3code;
	}
	public void setAln3code(String aln3code) {
		this.aln3code = aln3code;
	}
	public String getActtypeCode() {
		return acttypeCode;
	}
	public void setActtypeCode(String acttypeCode) {
		this.acttypeCode = acttypeCode;
	}
	public String getAircraftNumber() {
		return aircraftNumber;
	}
	public void setAircraftNumber(String aircraftNumber) {
		this.aircraftNumber = aircraftNumber;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getInOutFlag() {
		return inOutFlag;
	}
	public void setInOutFlag(String inOutFlag) {
		this.inOutFlag = inOutFlag;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	

}
