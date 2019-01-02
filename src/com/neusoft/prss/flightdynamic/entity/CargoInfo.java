package com.neusoft.prss.flightdynamic.entity;

/**
 * 货邮信息
 * @author xuhw
 *
 */
public class CargoInfo {
	//	出港货邮重量
	private String outCargoNum;
	//	出港集装器/散斗数量
	private String otherNum;
	//	进港货邮重量
	private String inCargoNum;
	//	进港集装器/散斗数量
	private String inNum;
	
	public String getOutCargoNum() {
		return outCargoNum;
	}
	public void setOutCargoNum(String outCargoNum) {
		this.outCargoNum = outCargoNum;
	}
	public String getOtherNum() {
		return otherNum;
	}
	public void setOtherNum(String otherNum) {
		this.otherNum = otherNum;
	}
	public String getInCargoNum() {
		return inCargoNum;
	}
	public void setInCargoNum(String inCargoNum) {
		this.inCargoNum = inCargoNum;
	}
	public String getInNum() {
		return inNum;
	}
	public void setInNum(String inNum) {
		this.inNum = inNum;
	}

	
}
