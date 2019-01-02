/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年10月11日 上午9:33:28
 *@author:yu-zd
 *@version:[v1.0]
 */
package com.neusoft.prss.flightdynamic.entity;

public class InfoChg {
	private static final long serialVersionUID = 1L;
	private String id;

	private String fltid;

	private String colId;

	private String oldValue;

	private String newValue;

	private String flightDate;

	private String updateUser;

	private String updateTime;

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

	public String getColId() {
		return colId;
	}

	public void setColId(String colId) {
		this.colId = colId;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue == null ? null : oldValue.trim();
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue == null ? null : newValue.trim();
	}

	public String getFlightDate() {
		return flightDate;
	}

	public void setFlightDate(String flightDate) {
		this.flightDate = flightDate == null ? null : flightDate.trim();
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser == null ? null : updateUser.trim();
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}
