package com.neusoft.prss.flightdynamic.entity;

import java.io.Serializable;

import com.neusoft.framework.common.utils.DateUtils;

/**
 * 航班信息——保障图
 * 
 * @author xuhw
 *
 */
public class FltInfo implements Serializable {

	/**
	 * Discription:字段功能描述.
	 */
	private static final long serialVersionUID = 1L;
	private String inFltid;
	private String outFltid;
	// 机型
	private String acttypeCode;
	// 机位
	private String actstandCode;
	// 起场
	private String departApt4code;
	// 落场
	private String arrivalApt4code;
	// 机号
	private String aircraftNumber;
	// 登机口
	private String gate;
	// 航班号
	private String inFlightNumber;
	// 航班号
	private String outFlightNumber;
	// 计落
	private String sta;
	// 预落
	private String eta;
	// 实落
	private String ata;
	// 计起
	private String std;
	// 预起
	private String etd;
	// 实起
	private String atd;
	// 是否远机位
	private String actstandKind;

	public String getInFltid() {
		return inFltid;
	}

	public void setInFltid(String inFltid) {
		this.inFltid = inFltid;
	}

	public String getOutFltid() {
		return outFltid;
	}

	public void setOutFltid(String outFltid) {
		this.outFltid = outFltid;
	}

	public String getActtypeCode() {
		return acttypeCode;
	}

	public void setActtypeCode(String acttypeCode) {
		this.acttypeCode = acttypeCode;
	}

	public String getActstandCode() {
		return actstandCode;
	}

	public void setActstandCode(String actstandCode) {
		this.actstandCode = actstandCode;
	}

	public String getDepartApt4code() {
		return departApt4code;
	}

	public void setDepartApt4code(String departApt4code) {
		this.departApt4code = departApt4code;
	}

	public String getArrivalApt4code() {
		return arrivalApt4code;
	}

	public void setArrivalApt4code(String arrivalApt4code) {
		this.arrivalApt4code = arrivalApt4code;
	}

	public String getAircraftNumber() {
		return aircraftNumber;
	}

	public void setAircraftNumber(String aircraftNumber) {
		this.aircraftNumber = aircraftNumber;
	}

	public String getGate() {
		return gate;
	}

	public void setGate(String gate) {
		this.gate = gate;
	}

	public String getInFlightNumber() {
		return inFlightNumber;
	}

	public void setInFlightNumber(String inFlightNumber) {
		this.inFlightNumber = inFlightNumber;
	}

	public String getOutFlightNumber() {
		return outFlightNumber;
	}

	public void setOutFlightNumber(String outFlightNumber) {
		this.outFlightNumber = outFlightNumber;
	}

	public String getSta() {
		return DateUtils.formatToFltDate(sta);
	}

	public void setSta(String sta) {
		this.sta = sta;
	}

	public String getEta() {
		return DateUtils.formatToFltDate(eta);
	}

	public void setEta(String eta) {
		this.eta = eta;
	}

	public String getAta() {
		return DateUtils.formatToFltDate(ata);
	}

	public void setAta(String ata) {
		this.ata = ata;
	}

	public String getStd() {
		return DateUtils.formatToFltDate(std);
	}

	public void setStd(String std) {
		this.std = std;
	}

	public String getEtd() {
		return DateUtils.formatToFltDate(etd);
	}

	public void setEtd(String etd) {
		this.etd = etd;
	}

	public String getAtd() {
		return DateUtils.formatToFltDate(atd);
	}

	public void setAtd(String atd) {
		this.atd = atd;
	}

	public String getActstandKind() {
		return actstandKind;
	}

	public void setActstandKind(String actstandKind) {
		this.actstandKind = actstandKind;
	}

}
