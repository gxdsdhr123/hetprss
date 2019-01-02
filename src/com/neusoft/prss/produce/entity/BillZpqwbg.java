package com.neusoft.prss.produce.entity;

import java.util.List;

/**
 * 
 * @author xuhw
 *
 */
public class BillZpqwbg {

	// 
	private Integer id;
	// 航班id
	private Integer fltid;
	// 航班号
	private String flightNumber;
	// 机型
	private String actType;
	// 机号
	private String aircraftNumber;
	// 操作人
	private String operator;
	// 签字人
	private String signatory;
	// 创建日期
	private String createDate;
	// 停机位
	private String actstandCode;
	// 航班日期
	private String flightDate;
	// 预起时间
	private String etd;
	// 预落时间
	private String eta;
	// 实起
	private String ata;
	// 实落
	private String atd;
	// 进港航班
	private String inFlightNumber;
	// 出港航班
	private String outFlightNumber;
	// 起场
	private String departApt4code;
	// 落场
	private String arrivalApt4code;
	// 舱门是否符合检查标准
	private String cmsf;
	// 舱内壁是否符合检查标准
	private String cnbsf;
	// 进港装在是否与电报一直
	private String jgzzsf;
	// 廊桥对靠时间
	private String lqdkTime;
	// 货舱门开启时间
	private String hcmkqTime;
	// 装舱完成时间
	private String zcwcTime;
	// 卸舱完成时间
	private String xcwcTime;
	// 最后货舱门关闭时间
	private String zhhcmgbTime;
	// 装舱指示取得时间
	private String zczsqdTime;
	// 散舱门关闭时间
	private String scmgbTime;
	// 开始装舱时间
	private String kszcTime;
	// 晚关舱门原因
	private String wgcmReason;
	// 进港行李交接时间
	private String jgxljjTime;
	// 进港货邮交接时间
	private String jghyjjTime;
	// 出港行李交接时间
	private String cgxljjTime;
	// 出港货邮交接时间
	private String cghyjjTime;
	// 航材交接时间
	private String hcjjTime;
	// 组员
	private String members;
	// 备注
	private String remark;
	// 进港航班id
	private String inFltid;
	// 出港航班id
	private String outFltid;
	// 操作人姓名
	private String operatorName;
	
	private String billZpqwbgGoodsStr;
	
	// 进出港类型
	private String inout;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getFltid() {
		return fltid;
	}
	public void setFltid(Integer fltid) {
		this.fltid = fltid;
	}
	public String getFlightNumber() {
		return flightNumber;
	}
	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}
	public String getActType() {
		return actType;
	}
	public void setActType(String actType) {
		this.actType = actType;
	}
	public String getAircraftNumber() {
		return aircraftNumber;
	}
	public void setAircraftNumber(String aircraftNumber) {
		this.aircraftNumber = aircraftNumber;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getSignatory() {
		return signatory;
	}
	public void setSignatory(String signatory) {
		this.signatory = signatory;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getActstandCode() {
		return actstandCode;
	}
	public void setActstandCode(String actstandCode) {
		this.actstandCode = actstandCode;
	}
	public String getFlightDate() {
		return flightDate;
	}
	public void setFlightDate(String flightDate) {
		this.flightDate = flightDate;
	}
	public String getEtd() {
		return etd;
	}
	public void setEtd(String etd) {
		this.etd = etd;
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
	public String getAtd() {
		return atd;
	}
	public void setAtd(String atd) {
		this.atd = atd;
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
	public String getCmsf() {
		return cmsf;
	}
	public void setCmsf(String cmsf) {
		this.cmsf = cmsf;
	}
	public String getCnbsf() {
		return cnbsf;
	}
	public void setCnbsf(String cnbsf) {
		this.cnbsf = cnbsf;
	}
	public String getJgzzsf() {
		return jgzzsf;
	}
	public void setJgzzsf(String jgzzsf) {
		this.jgzzsf = jgzzsf;
	}
	public String getLqdkTime() {
		return lqdkTime;
	}
	public void setLqdkTime(String lqdkTime) {
		this.lqdkTime = lqdkTime;
	}
	public String getHcmkqTime() {
		return hcmkqTime;
	}
	public void setHcmkqTime(String hcmkqTime) {
		this.hcmkqTime = hcmkqTime;
	}
	public String getZcwcTime() {
		return zcwcTime;
	}
	public void setZcwcTime(String zcwcTime) {
		this.zcwcTime = zcwcTime;
	}
	public String getXcwcTime() {
		return xcwcTime;
	}
	public void setXcwcTime(String xcwcTime) {
		this.xcwcTime = xcwcTime;
	}
	public String getZhhcmgbTime() {
		return zhhcmgbTime;
	}
	public void setZhhcmgbTime(String zhhcmgbTime) {
		this.zhhcmgbTime = zhhcmgbTime;
	}
	public String getZczsqdTime() {
		return zczsqdTime;
	}
	public void setZczsqdTime(String zczsqdTime) {
		this.zczsqdTime = zczsqdTime;
	}
	public String getScmgbTime() {
		return scmgbTime;
	}
	public void setScmgbTime(String scmgbTime) {
		this.scmgbTime = scmgbTime;
	}
	public String getKszcTime() {
		return kszcTime;
	}
	public void setKszcTime(String kszcTime) {
		this.kszcTime = kszcTime;
	}
	public String getWgcmReason() {
		return wgcmReason;
	}
	public void setWgcmReason(String wgcmReason) {
		this.wgcmReason = wgcmReason;
	}
	public String getJgxljjTime() {
		return jgxljjTime;
	}
	public void setJgxljjTime(String jgxljjTime) {
		this.jgxljjTime = jgxljjTime;
	}
	public String getJghyjjTime() {
		return jghyjjTime;
	}
	public void setJghyjjTime(String jghyjjTime) {
		this.jghyjjTime = jghyjjTime;
	}
	public String getCgxljjTime() {
		return cgxljjTime;
	}
	public void setCgxljjTime(String cgxljjTime) {
		this.cgxljjTime = cgxljjTime;
	}
	public String getCghyjjTime() {
		return cghyjjTime;
	}
	public void setCghyjjTime(String cghyjjTime) {
		this.cghyjjTime = cghyjjTime;
	}
	public String getHcjjTime() {
		return hcjjTime;
	}
	public void setHcjjTime(String hcjjTime) {
		this.hcjjTime = hcjjTime;
	}
	public String getMembers() {
		return members;
	}
	public void setMembers(String members) {
		this.members = members;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public String getBillZpqwbgGoodsStr() {
		return billZpqwbgGoodsStr;
	}
	public void setBillZpqwbgGoodsStr(String billZpqwbgGoodsStr) {
		this.billZpqwbgGoodsStr = billZpqwbgGoodsStr;
	}
	public String getInout() {
		return inout;
	}
	public void setInout(String inout) {
		this.inout = inout;
	}
	
	

}
