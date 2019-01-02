package com.neusoft.prss.produce.entity;

import java.math.BigDecimal;

public class BillTcChargeEntity extends BillCommonEntity{
	private String type;
	private BigDecimal nums;
	private BigDecimal duration; 
	private String servicePos;
	private String startTime;
	private String endTime;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public BigDecimal getNums() {
		return nums;
	}
	public void setNums(BigDecimal nums) {
		this.nums = nums;
	}
	public BigDecimal getDuration() {
		return duration;
	}
	public void setDuration(BigDecimal duration) {
		this.duration = duration;
	}
	public String getServicePos() {
		return servicePos;
	}
	public void setServicePos(String servicePos) {
		this.servicePos = servicePos;
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

}
