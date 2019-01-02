package com.neusoft.prss.flightdynamic.bean;

import java.io.Serializable;

public class FDChangeFltDate implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5679272619227224868L;
	String fltid;
	String oldFlightDate;
	String newFlightDate;
	String std;
	String etd;
	String atd;
	String sta;
	String eta;
	String ata;

	public String getFltid() {
		return fltid;
	}
	public void setFltid(String fltid) {
		this.fltid = fltid;
	}
	public String getOldFlightDate() {
		return oldFlightDate;
	}
	public void setOldFlightDate(String oldFlightDate) {
		this.oldFlightDate = oldFlightDate;
	}
	public String getNewFlightDate() {
		return newFlightDate;
	}
	public void setNewFlightDate(String newFlightDate) {
		this.newFlightDate = newFlightDate;
	}
	public String getStd() {
		return std;
	}
	public void setStd(String std) {
		this.std = std;
	}
	public String getEtd() {
		return etd;
	}
	public void setEtd(String etd) {
		this.etd = etd;
	}
	public String getAtd() {
		return atd;
	}
	public void setAtd(String atd) {
		this.atd = atd;
	}
	public String getSta() {
		return sta;
	}
	public void setSta(String sta) {
		this.sta = sta;
	}
	public String getEta() {
		return eta;
	}
	public void setEta(String eta) {
		this.eta = eta;
	}
	public String getAta() {
		return ata;
	}
	public void setAta(String ata) {
		this.ata = ata;
	}
	

}
