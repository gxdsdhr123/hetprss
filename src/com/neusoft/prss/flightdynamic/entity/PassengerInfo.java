package com.neusoft.prss.flightdynamic.entity;
/**
 * 旅客信息——保障图
 * @author xuhw
 *
 */
public class PassengerInfo {
	//旅客人数
	private String passengerNum;
	//订座人数
	private String bookNum;
	//已值机人数
	private String operatedNum;
	//网上值机人数
	private String onlineOperateNum;
	//自助值机
	private String selfhelpOperateNum;
	//柜台值机
	private String counterOperateNum;
	//值机关闭人数
	private String unoperateNum;
	//已过检人数
	private String seizedNum;
	//已登机人数
	private String boardingNum;
	//结束登机人数
	private String unboardingNum;
	//减客数
	private String reduceNum;
	//VIP和头等舱
	private String vipNum;
	// 要客类型
	private String vipFlagText;
	
	public String getPassengerNum() {
		return passengerNum;
	}
	public void setPassengerNum(String passengerNum) {
		this.passengerNum = passengerNum;
	}
	public String getBookNum() {
		return bookNum;
	}
	public void setBookNum(String bookNum) {
		this.bookNum = bookNum;
	}
	public String getOperatedNum() {
		return operatedNum;
	}
	public void setOperatedNum(String operatedNum) {
		this.operatedNum = operatedNum;
	}
	public String getOnlineOperateNum() {
		return onlineOperateNum;
	}
	public void setOnlineOperateNum(String onlineOperateNum) {
		this.onlineOperateNum = onlineOperateNum;
	}
	public String getSelfhelpOperateNum() {
		return selfhelpOperateNum;
	}
	public void setSelfhelpOperateNum(String selfhelpOperateNum) {
		this.selfhelpOperateNum = selfhelpOperateNum;
	}
	public String getCounterOperateNum() {
		return counterOperateNum;
	}
	public void setCounterOperateNum(String counterOperateNum) {
		this.counterOperateNum = counterOperateNum;
	}
	public String getUnoperateNum() {
		return unoperateNum;
	}
	public void setUnoperateNum(String unoperateNum) {
		this.unoperateNum = unoperateNum;
	}
	public String getSeizedNum() {
		return seizedNum;
	}
	public void setSeizedNum(String seizedNum) {
		this.seizedNum = seizedNum;
	}
	public String getBoardingNum() {
		return boardingNum;
	}
	public void setBoardingNum(String boardingNum) {
		this.boardingNum = boardingNum;
	}
	public String getUnboardingNum() {
		return unboardingNum;
	}
	public void setUnboardingNum(String unboardingNum) {
		this.unboardingNum = unboardingNum;
	}
	public String getReduceNum() {
		return reduceNum;
	}
	public void setReduceNum(String reduceNum) {
		this.reduceNum = reduceNum;
	}
	public String getVipNum() {
		return vipNum;
	}
	public void setVipNum(String vipNum) {
		this.vipNum = vipNum;
	}
	public String getVipFlagText() {
		return vipFlagText;
	}
	public void setVipFlagText(String vipFlagText) {
		this.vipFlagText = vipFlagText;
	}
	
	
}
