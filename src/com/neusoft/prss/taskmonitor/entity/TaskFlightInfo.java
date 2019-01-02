package com.neusoft.prss.taskmonitor.entity;

/**
 * 航班信息——摆渡车任务分配监控界面
 * @author xuhw
 *
 */
public class TaskFlightInfo {

	// fltid
	private String fltid;
    // 航班号
    private String flightNumber;
    // 机号
    private String aircraftNumber;
    // 机位
    private String actstandCode;
    // 到达口/登机口
    private String gate;
    // 国内登机口
    private String dGate;
    // sta
    private String sta;
    // eta
    private String eta;
    // ata
    private String ata;
    // std
    private String std;
    // etd
    private String etd;
    // atd
    private String atd;
    // tsat
    private String tsat;
    // 国际国内标识
    private String fltAttrCode;
    // vip
    private String vipFlag;
    // 重保
    private String ssgFlag;
    // 旅客数
    private String paxNum;
    // 机型
    private String acttypeCode;
    // 接飞/前飞
    private String befAftFltno;
    // 国内航班人数
    private String dPaxNum;
    // 国际航班人数
    private String iPaxNum;
    // 航班状态
    private String fltStatus;
    // 登机开始时间
    private String brdBtm;
    // 登机结束时间
    private String brdEtm;
	// 进港航班号
	private String inFltNum;
	// 出港航班号
	private String outFltNum;
	// 分公司
	private String sonAirline;
	// 进港机位
    private String inActstandCode;
    // 出港机位
    private String outActstandCode;
    // 进港VIP
    private String inVipFlag;
    // 出港VIP
    private String outVipFlag;
    // 作业人清单
    private String allOperator;
    // 加水量
    private String water;
    // 行李转盘/分拣口
    private String bag;
    //航班状态
    private String status;
    //航线
    private String hx;
    
	public String getFltid() {
		return fltid;
	}
	public void setFltid(String fltid) {
		this.fltid = fltid;
	}
	public String getFlightNumber() {
		return flightNumber;
	}
	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}
	public String getAircraftNumber() {
		return aircraftNumber;
	}
	public void setAircraftNumber(String aircraftNumber) {
		this.aircraftNumber = aircraftNumber;
	}
	public String getActstandCode() {
		return actstandCode;
	}
	public void setActstandCode(String actstandCode) {
		this.actstandCode = actstandCode;
	}
	public String getGate() {
		return gate;
	}
	public void setGate(String gate) {
		this.gate = gate;
	}
	public String getdGate() {
		return dGate;
	}
	public void setdGate(String dGate) {
		this.dGate = dGate;
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
	public String getTsat() {
		return tsat;
	}
	public void setTsat(String tsat) {
		this.tsat = tsat;
	}
	public String getFltAttrCode() {
		return fltAttrCode;
	}
	public void setFltAttrCode(String fltAttrCode) {
		this.fltAttrCode = fltAttrCode;
	}
	public String getVipFlag() {
		return vipFlag;
	}
	public void setVipFlag(String vipFlag) {
		this.vipFlag = vipFlag;
	}
	public String getSsgFlag() {
		return ssgFlag;
	}
	public void setSsgFlag(String ssgFlag) {
		this.ssgFlag = ssgFlag;
	}
	public String getPaxNum() {
		return paxNum;
	}
	public void setPaxNum(String paxNum) {
		this.paxNum = paxNum;
	}
	public String getActtypeCode() {
		return acttypeCode;
	}
	public void setActtypeCode(String acttypeCode) {
		this.acttypeCode = acttypeCode;
	}
	public String getBefAftFltno() {
		return befAftFltno;
	}
	public void setBefAftFltno(String befAftFltno) {
		this.befAftFltno = befAftFltno;
	}
	public String getdPaxNum() {
		return dPaxNum;
	}
	public void setdPaxNum(String dPaxNum) {
		this.dPaxNum = dPaxNum;
	}
	public String getiPaxNum() {
		return iPaxNum;
	}
	public void setiPaxNum(String iPaxNum) {
		this.iPaxNum = iPaxNum;
	}
	public String getFltStatus() {
		return fltStatus;
	}
	public void setFltStatus(String fltStatus) {
		this.fltStatus = fltStatus;
	}
	public String getBrdBtm() {
		return brdBtm;
	}
	public void setBrdBtm(String brdBtm) {
		this.brdBtm = brdBtm;
	}
	public String getBrdEtm() {
		return brdEtm;
	}
	public void setBrdEtm(String brdEtm) {
		this.brdEtm = brdEtm;
	}
	public String getInFltNum() {
		return inFltNum;
	}
	public void setInFltNum(String inFltNum) {
		this.inFltNum = inFltNum;
	}
	public String getOutFltNum() {
		return outFltNum;
	}
	public void setOutFltNum(String outFltNum) {
		this.outFltNum = outFltNum;
	}
	public String getSonAirline() {
		return sonAirline;
	}
	public void setSonAirline(String sonAirline) {
		this.sonAirline = sonAirline;
	}
	public String getInActstandCode() {
		return inActstandCode;
	}
	public void setInActstandCode(String inActstandCode) {
		this.inActstandCode = inActstandCode;
	}
	public String getOutActstandCode() {
		return outActstandCode;
	}
	public void setOutActstandCode(String outActstandCode) {
		this.outActstandCode = outActstandCode;
	}
	public String getAllOperator() {
		return allOperator;
	}
	public void setAllOperator(String allOperator) {
		this.allOperator = allOperator;
	}
	public String getWater() {
		return water;
	}
	public void setWater(String water) {
		this.water = water;
	}
	public String getBag() {
		return bag;
	}
	public void setBag(String bag) {
		this.bag = bag;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getInVipFlag() {
		return inVipFlag;
	}
	public void setInVipFlag(String inVipFlag) {
		this.inVipFlag = inVipFlag;
	}
	public String getOutVipFlag() {
		return outVipFlag;
	}
	public void setOutVipFlag(String outVipFlag) {
		this.outVipFlag = outVipFlag;
	}
	public String getHx() {
		return hx;
	}
	public void setHx(String hx) {
		this.hx = hx;
	}

}
