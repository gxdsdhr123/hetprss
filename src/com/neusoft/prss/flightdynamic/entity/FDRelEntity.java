package com.neusoft.prss.flightdynamic.entity;

import java.io.Serializable;

public class FDRelEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String inFltid;
	private String outFltid;
	private String flightDate;
	private String actstandCode;
	private String status;
	private String inFlightNo;
	private String outFlightNo;
	private String ganttBtm;
	private String ganttEtm;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
	public String getFlightDate() {
		return flightDate;
	}
	public void setFlightDate(String flightDate) {
		this.flightDate = flightDate;
	}
	public String getActstandCode() {
		return actstandCode;
	}
	public void setActstandCode(String actstandCode) {
		this.actstandCode = actstandCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getInFlightNo() {
		return inFlightNo;
	}
	public void setInFlightNo(String inFlightNo) {
		this.inFlightNo = inFlightNo;
	}
	public String getOutFlightNo() {
		return outFlightNo;
	}
	public void setOutFlightNo(String outFlightNo) {
		this.outFlightNo = outFlightNo;
	}
	public String getGanttBtm() {
		return ganttBtm;
	}
	public void setGanttBtm(String ganttBtm) {
		this.ganttBtm = ganttBtm;
	}
	public String getGanttEtm() {
		return ganttEtm;
	}
	public void setGanttEtm(String ganttEtm) {
		this.ganttEtm = ganttEtm;
	}
}
