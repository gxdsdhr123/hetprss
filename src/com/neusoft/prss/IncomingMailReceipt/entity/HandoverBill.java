package com.neusoft.prss.IncomingMailReceipt.entity;

import java.util.Date;

/**
 * 邮货单实体类
 * @author lwg
 * @date 2018/01/03
 */
public class HandoverBill {
	
	private String id;
	
	private String fltid;
	
	private String flightNumber;
	
	private String actType;
	
	private String aircraftNumber;
	
	private String operator;
	
	private String signatory;
	
	private Date createDate;
	
	private String actstandCode; 
	
	private String flightDate;
	
	private String eta; 
	
	private String etd;
	
	private String operatorName;
	
	private String data;
	
	private String sign;

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

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public String getActType() {
		return actType;
	}

	public void setActType(String actType) {
		this.actType = actType;
	}

	public String getAircraftNumber() {
		return aircraftNumber;
	}

	public void setAircraftNumber(String aircraftNumber) {
		this.aircraftNumber = aircraftNumber;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getSignatory() {
		return signatory;
	}

	public void setSignatory(String signatory) {
		this.signatory = signatory;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getActstandCode() {
		return actstandCode;
	}

	public void setActstandCode(String actstandCode) {
		this.actstandCode = actstandCode;
	}

	public String getFlightDate() {
		return flightDate;
	}

	public void setFlightDate(String flightDate) {
		this.flightDate = flightDate;
	}

	public String getEta() {
		return eta;
	}

	public void setEta(String eta) {
		this.eta = eta;
	}

	public String getEtd() {
		return etd;
	}

	public void setEtd(String etd) {
		this.etd = etd;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	} 
	
	
}
