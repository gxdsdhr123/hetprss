package com.neusoft.prss.parameter.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class AirborneHours implements Serializable{

	/**
	 * 空中飞行时间实体
	 * wangtg
	 */
	private static final long serialVersionUID = -3888140133851202245L;
	private BigDecimal id;
	private String departApt3Code;
	private String arrivalApt3Code;
	private String actType;
	private BigDecimal standardFlightTime;
	private BigDecimal driftValue;
	private String beginFlightDate;
	private String endFlightDate;
	private String operType;
	private String operUser;
	private Date operDate;
	private BigDecimal calcFltTime;
	
	public BigDecimal getId() {
		return id;
	}
	public void setId(BigDecimal id) {
		this.id = id;
	}
	public String getDepartApt3Code() {
		return departApt3Code;
	}
	public void setDepartApt3Code(String departApt3Code) {
		this.departApt3Code = departApt3Code;
	}
	public String getArrivalApt3Code() {
		return arrivalApt3Code;
	}
	public void setArrivalApt3Code(String arrivalApt3Code) {
		this.arrivalApt3Code = arrivalApt3Code;
	}
	public String getActType() {
		return actType;
	}
	public void setActType(String actType) {
		this.actType = actType;
	}
	public BigDecimal getStandardFlightTime() {
		return standardFlightTime;
	}
	public void setStandardFlightTime(BigDecimal standardFlightTime) {
		this.standardFlightTime = standardFlightTime;
	}
	public BigDecimal getDriftValue() {
		return driftValue;
	}
	public void setDriftValue(BigDecimal driftValue) {
		this.driftValue = driftValue;
	}
	public String getBeginFlightDate() {
		return beginFlightDate;
	}
	public void setBeginFlightDate(String beginFlightDate) {
		this.beginFlightDate = beginFlightDate;
	}
	public String getEndFlightDate() {
		return endFlightDate;
	}
	public void setEndFlightDate(String endFlightDate) {
		this.endFlightDate = endFlightDate;
	}
	public String getOperType() {
		return operType;
	}
	public void setOperType(String operType) {
		this.operType = operType;
	}
	public String getOperUser() {
		return operUser;
	}
	public void setOperUser(String operUser) {
		this.operUser = operUser;
	}
	public Date getOperDate() {
		return operDate;
	}
	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public BigDecimal getCalcFltTime() {
		return calcFltTime;
	}
	public void setCalcFltTime(BigDecimal calcFltTime) {
		this.calcFltTime = calcFltTime;
	}
	

	

}
