package com.neusoft.prss.abnormalFlightManagement.entity;

/**
 * 不正常航班报告打印word文件实体类
 * 
 * @author yunwq
 * @date 2018/02/23
 *
 */
public class AbnormalFlightWordVo {
	// 航班日期
	private String flightDate;
	// 航班号
	private String flightNumber;
	// 发送部门
	private String officeId;
	// 信息来源
	private String infoSource;
	//进港航班号
	private String inFlightNumber;
	//机号
	private String aircraftNumber;
	//机型
	private String acttypeCode;
	//出港航班号
	private String outFlightNumber;
	//STA
	private String sta;
	//STD
	private String std;
	//机位
	private String actstandCode;
	//ATA
	private String ata;
	//ATD
	private String atd;
	//客关时间
	private String htchCloTm;
	//货关时间
	private String goodclose;
	//首次TSAT
	private String ftsatTm;
	//起场
	private String departApt4code;
	//落场
	private String arrivalApt4code;
	//TSAT
	private String tsat;
	//延误原因/时间
	private String dealyReason;
	// 备注
	private String remark;
	// 运控值班员
	private String dutyName;
	// 运控值班员日期
	private String operDate;
	//保障部门
	private String feedBackOfficeName;
	//保障部门值班员
	private String feedBackOperName;
	//保障部门反馈日期
	private String operDate1;
	//保障部门反馈
	private String deptFeedBackContent;
	// CDM判责
	private String cmdContent;
	// CDM值班经理
	private String cmdName;
	// CDM判责日期
	private String operDate2;
	public String getFlightDate() {
		return flightDate;
	}
	public void setFlightDate(String flightDate) {
		this.flightDate = flightDate;
	}
	public String getFlightNumber() {
		return flightNumber;
	}
	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}
	public String getOfficeId() {
		return officeId;
	}
	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	public String getInfoSource() {
		return infoSource;
	}
	public void setInfoSource(String infoSource) {
		this.infoSource = infoSource;
	}
	public String getInFlightNumber() {
		return inFlightNumber;
	}
	public void setInFlightNumber(String inFlightNumber) {
		this.inFlightNumber = inFlightNumber;
	}
	public String getAircraftNumber() {
		return aircraftNumber;
	}
	public void setAircraftNumber(String aircraftNumber) {
		this.aircraftNumber = aircraftNumber;
	}
	public String getActtypeCode() {
		return acttypeCode;
	}
	public void setActtypeCode(String acttypeCode) {
		this.acttypeCode = acttypeCode;
	}
	public String getOutFlightNumber() {
		return outFlightNumber;
	}
	public void setOutFlightNumber(String outFlightNumber) {
		this.outFlightNumber = outFlightNumber;
	}
	public String getSta() {
		return sta;
	}
	public void setSta(String sta) {
		this.sta = sta;
	}
	public String getStd() {
		return std;
	}
	public void setStd(String std) {
		this.std = std;
	}
	public String getActstandCode() {
		return actstandCode;
	}
	public void setActstandCode(String actstandCode) {
		this.actstandCode = actstandCode;
	}
	public String getAta() {
		return ata;
	}
	public void setAta(String ata) {
		this.ata = ata;
	}
	public String getAtd() {
		return atd;
	}
	public void setAtd(String atd) {
		this.atd = atd;
	}
	public String getHtchCloTm() {
		return htchCloTm;
	}
	public void setHtchCloTm(String htchCloTm) {
		this.htchCloTm = htchCloTm;
	}
	public String getGoodclose() {
		return goodclose;
	}
	public void setGoodclose(String goodclose) {
		this.goodclose = goodclose;
	}
	public String getFtsatTm() {
		return ftsatTm;
	}
	public void setFtsatTm(String ftsatTm) {
		this.ftsatTm = ftsatTm;
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
	public String getTsat() {
		return tsat;
	}
	public void setTsat(String tsat) {
		this.tsat = tsat;
	}
	public String getDealyReason() {
		return dealyReason;
	}
	public void setDealyReason(String dealyReason) {
		this.dealyReason = dealyReason;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getDutyName() {
		return dutyName;
	}
	public void setDutyName(String dutyName) {
		this.dutyName = dutyName;
	}
	public String getOperDate() {
		return operDate;
	}
	public void setOperDate(String operDate) {
		this.operDate = operDate;
	}
	public String getFeedBackOfficeName() {
		return feedBackOfficeName;
	}
	public void setFeedBackOfficeName(String feedBackOfficeName) {
		this.feedBackOfficeName = feedBackOfficeName;
	}
	public String getFeedBackOperName() {
		return feedBackOperName;
	}
	public void setFeedBackOperName(String feedBackOperName) {
		this.feedBackOperName = feedBackOperName;
	}
	public String getOperDate1() {
		return operDate1;
	}
	public void setOperDate1(String operDate1) {
		this.operDate1 = operDate1;
	}
	public String getDeptFeedBackContent() {
		return deptFeedBackContent;
	}
	public void setDeptFeedBackContent(String deptFeedBackContent) {
		this.deptFeedBackContent = deptFeedBackContent;
	}
	public String getCmdContent() {
		return cmdContent;
	}
	public void setCmdContent(String cmdContent) {
		this.cmdContent = cmdContent;
	}
	public String getCmdName() {
		return cmdName;
	}
	public void setCmdName(String cmdName) {
		this.cmdName = cmdName;
	}
	public String getOperDate2() {
		return operDate2;
	}
	public void setOperDate2(String operDate2) {
		this.operDate2 = operDate2;
	}
}
