package com.neusoft.prss.flightdynamic.entity;

/**
 * 行李信息
 * @author xuhw
 *
 */
public class LuggageInfo {
	
	//出港行李重量
	private String outWeight;
	//	出港行李件数
	private String outLuggageNum;
	//	出港集装器/散斗数量
	private String	outOtherNum;
	//	进港行李重量
	private String inWeight;
	//	进港行李件数
	private String inLuggageNum;
	//	进港集装器/散斗数量
	private String	inOtherNum;
	
	public String getOutWeight() {
		return outWeight;
	}
	public void setOutWeight(String outWeight) {
		this.outWeight = outWeight;
	}
	public String getOutLuggageNum() {
		return outLuggageNum;
	}
	public void setOutLuggageNum(String outLuggageNum) {
		this.outLuggageNum = outLuggageNum;
	}
	public String getOutOtherNum() {
		return outOtherNum;
	}
	public void setOutOtherNum(String outOtherNum) {
		this.outOtherNum = outOtherNum;
	}
	public String getInWeight() {
		return inWeight;
	}
	public void setInWeight(String inWeight) {
		this.inWeight = inWeight;
	}
	public String getInLuggageNum() {
		return inLuggageNum;
	}
	public void setInLuggageNum(String inLuggageNum) {
		this.inLuggageNum = inLuggageNum;
	}
	public String getInOtherNum() {
		return inOtherNum;
	}
	public void setInOtherNum(String inOtherNum) {
		this.inOtherNum = inOtherNum;
	}
	
}
