package com.neusoft.prss.produce.entity;

import java.io.Serializable;

public class BillSpecialEntity extends BillCommonEntity  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -287794198939179953L;
	private String type;
	private String station;
	private String arrivalTime;
	private String departTime;
	private String propertyCode;
	private String servicePos;
	private String unitPrice;
	private String duration;
	private String allPrice;
	private String signRemark;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getStation() {
		return station;
	}
	public void setStation(String station) {
		this.station = station;
	}
	public String getArrivalTime() {
		return arrivalTime;
	}
	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	public String getDepartTime() {
		return departTime;
	}
	public void setDepartTime(String departTime) {
		this.departTime = departTime;
	}
	public String getPropertyCode() {
		return propertyCode;
	}
	public void setPropertyCode(String propertyCode) {
		this.propertyCode = propertyCode;
	}
	public String getServicePos() {
		return servicePos;
	}
	public void setServicePos(String servicePos) {
		this.servicePos = servicePos;
	}
	public String getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getAllPrice() {
		return allPrice;
	}
	public void setAllPrice(String allPrice) {
		this.allPrice = allPrice;
	}
	public String getSignRemark()
	{
		return signRemark;
	}
	public void setSignRemark(String signRemark)
	{
		this.signRemark = signRemark;
	}
	
}
