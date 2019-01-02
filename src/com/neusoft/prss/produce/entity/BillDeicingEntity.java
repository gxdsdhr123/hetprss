package com.neusoft.prss.produce.entity;

import java.io.Serializable;

public class BillDeicingEntity extends BillCommonEntity implements Serializable{
	private String defidCarnum;
	private String routeName;
	private String temperature;
	private String defidModel;
	private String defidDosage;
	private String aifidModel;
	private String aifidDosage;
	private String reason;
	private String type ;
	private String station;
	private String ata;
	private String signRemark;
	
	public String getAta() {
		return ata;
	}
	public void setAta(String ata) {
		this.ata = ata;
	}
	public String getStation() {
		return station;
	}
	public void setStation(String station) {
		this.station = station;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDefidCarnum() {
		return defidCarnum;
	}
	public void setDefidCarnum(String defidCarnum) {
		this.defidCarnum = defidCarnum;
	}
	public String getRouteName() {
		return routeName;
	}
	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getDefidModel() {
		return defidModel;
	}
	public void setDefidModel(String defidModel) {
		this.defidModel = defidModel;
	}
	public String getDefidDosage() {
		return defidDosage;
	}
	public void setDefidDosage(String defidDosage) {
		this.defidDosage = defidDosage;
	}
	public String getAifidModel() {
		return aifidModel;
	}
	public void setAifidModel(String aifidModel) {
		this.aifidModel = aifidModel;
	}
	public String getAifidDosage() {
		return aifidDosage;
	}
	public void setAifidDosage(String aifidDosage) {
		this.aifidDosage = aifidDosage;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getSignRemark(){
		return signRemark;
	}
	public void setSignRemark(String signRemark){
		this.signRemark = signRemark;
	}
}
