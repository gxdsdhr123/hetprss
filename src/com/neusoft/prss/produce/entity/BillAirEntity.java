package com.neusoft.prss.produce.entity;

import java.io.Serializable;

public class BillAirEntity extends BillCommonEntity implements Serializable {
	private String type;
	private String routeName;
	private String fxFlag;
	private String propertyCode;
	private String signRemark;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRouteName() {
		return routeName;
	}
	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}
	public String getFxFlag() {
		return fxFlag;
	}
	public void setFxFlag(String fxFlag) {
		this.fxFlag = fxFlag;
	}
	public String getPropertyCode() {
		return propertyCode;
	}
	public void setPropertyCode(String propertyCode) {
		this.propertyCode = propertyCode;
	}
	public String getSignRemark(){
		return signRemark;
	}
	public void setSignRemark(String signRemark){
		this.signRemark = signRemark;
	}
}
