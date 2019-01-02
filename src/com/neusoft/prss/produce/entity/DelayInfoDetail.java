package com.neusoft.prss.produce.entity;

import java.io.Serializable;

public class DelayInfoDetail implements Serializable{

	private static final long serialVersionUID = 966093765991310547L;
	
	
	/** 主键 **/
	private Integer id;
	/** 早餐（预计） **/
	private Integer eBreakfast;
	/** 午餐（预计） **/
	private Integer eLunch;
	/** 晚餐（预计） **/
	private Integer eDinner;
	/** 住宿（预计） **/
	private Integer eAccommodation;
	/** 交通（预计） **/
	private Integer eTraffic;
	/** 饮料（预计） **/
	private Integer eDrinks;
	/** 夜宵（预计） **/
	private Integer eNightSnack;
	/** 早餐（实际） **/
	private Integer aBreakfast;
	/** 午餐（实际） **/
	private Integer aLunch;
	/** 晚餐（实际） **/
	private Integer aDinner;
	/** 住宿（实际） **/
	private Integer aAccommodation;
	/** 交通（实际） **/
	private Integer aTraffic;
	/** 饮料（实际） **/
	private Integer aDrinks;
	/** 夜宵（实际） **/
	private Integer aNightSnack;
	/** 安排类型（1.旅客，2.机组） **/
	private String arrangeType;
	/** 日期类型（0.当日，1.次日） **/
	private String dateType;
	/** fltid **/
	private Integer fltid;
	/** 航延单据ID **/
	private Integer delayId;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer geteBreakfast() {
		return eBreakfast;
	}
	public void seteBreakfast(Integer eBreakfast) {
		this.eBreakfast = eBreakfast;
	}
	public Integer geteLunch() {
		return eLunch;
	}
	public void seteLunch(Integer eLunch) {
		this.eLunch = eLunch;
	}
	public Integer geteDinner() {
		return eDinner;
	}
	public void seteDinner(Integer eDinner) {
		this.eDinner = eDinner;
	}
	public Integer geteAccommodation() {
		return eAccommodation;
	}
	public void seteAccommodation(Integer eAccommodation) {
		this.eAccommodation = eAccommodation;
	}
	public Integer geteTraffic() {
		return eTraffic;
	}
	public void seteTraffic(Integer eTraffic) {
		this.eTraffic = eTraffic;
	}
	public Integer geteDrinks() {
		return eDrinks;
	}
	public void seteDrinks(Integer eDrinks) {
		this.eDrinks = eDrinks;
	}
	public Integer geteNightSnack() {
		return eNightSnack;
	}
	public void seteNightSnack(Integer eNightSnack) {
		this.eNightSnack = eNightSnack;
	}
	public Integer getaBreakfast() {
		return aBreakfast;
	}
	public void setaBreakfast(Integer aBreakfast) {
		this.aBreakfast = aBreakfast;
	}
	public Integer getaLunch() {
		return aLunch;
	}
	public void setaLunch(Integer aLunch) {
		this.aLunch = aLunch;
	}
	public Integer getaDinner() {
		return aDinner;
	}
	public void setaDinner(Integer aDinner) {
		this.aDinner = aDinner;
	}
	public Integer getaAccommodation() {
		return aAccommodation;
	}
	public void setaAccommodation(Integer aAccommodation) {
		this.aAccommodation = aAccommodation;
	}
	public Integer getaTraffic() {
		return aTraffic;
	}
	public void setaTraffic(Integer aTraffic) {
		this.aTraffic = aTraffic;
	}
	public Integer getaDrinks() {
		return aDrinks;
	}
	public void setaDrinks(Integer aDrinks) {
		this.aDrinks = aDrinks;
	}
	public Integer getaNightSnack() {
		return aNightSnack;
	}
	public void setaNightSnack(Integer aNightSnack) {
		this.aNightSnack = aNightSnack;
	}
	public String getArrangeType() {
		return arrangeType;
	}
	public void setArrangeType(String arrangeType) {
		this.arrangeType = arrangeType;
	}
	public String getDateType() {
		return dateType;
	}
	public void setDateType(String dateType) {
		this.dateType = dateType;
	}
	public Integer getFltid() {
		return fltid;
	}
	public void setFltid(Integer fltid) {
		this.fltid = fltid;
	}
	public Integer getDelayId() {
		return delayId;
	}
	public void setDelayId(Integer delayId) {
		this.delayId = delayId;
	}
	
}
