package com.neusoft.prss.plan.entity;

import java.io.Serializable;

public class TravelskyPlanEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String id;//id
	public String fltNo;//航班号
	public String fltDate;//执行日期
	public String std;//计起
	public String sta;//计落
	public String departApt;//起场
	public String arrivalApt;//落场
	public String actType;//机型
	public String shareFltNo;//共享航班号
	public String createUser;
	public String flag;//A进港 D出港
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFltNo() {
		return fltNo;
	}
	public void setFltNo(String fltNo) {
		this.fltNo = fltNo;
	}
	public String getFltDate() {
		return fltDate;
	}
	public void setFltDate(String fltDate) {
		this.fltDate = fltDate;
	}
	public String getStd() {
		return std;
	}
	public void setStd(String std) {
		this.std = std;
	}
	public String getSta() {
		return sta;
	}
	public void setSta(String sta) {
		this.sta = sta;
	}
	public String getDepartApt() {
		return departApt;
	}
	public void setDepartApt(String departApt) {
		this.departApt = departApt;
	}
	public String getArrivalApt() {
		return arrivalApt;
	}
	public void setArrivalApt(String arrivalApt) {
		this.arrivalApt = arrivalApt;
	}
	
	public String getActType() {
		return actType;
	}
	public void setActType(String actType) {
		this.actType = actType;
	}
	public String getShareFltNo() {
		return shareFltNo;
	}
	public void setShareFltNo(String shareFltNo) {
		this.shareFltNo = shareFltNo;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getFlag()
	{
		return flag;
	}
	public void setFlag(String flag)
	{
		this.flag = flag;
	}
}
