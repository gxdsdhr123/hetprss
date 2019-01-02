/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年10月26日 下午4:54:56
 *@author:Heqg
 *@version:[v1.0]
 */
package com.neusoft.prss.arrange.entity;

public class AmbulatoryShiftsType {
	
	private String shiftsId;
    private String shiftsName;
    private String startime;
    private String endtime;
    private String bindFlt;
    private String weekCode;
    private String officeId;

    public String getShiftsId() {
        return shiftsId;
    }

    public void setShiftsId(String shiftsId) {
        this.shiftsId = shiftsId;
    }

    public String getShiftsName() {
        return shiftsName;
    }

    public void setShiftsName(String shiftsName) {
        this.shiftsName = shiftsName == null ? null : shiftsName.trim();
    }

    public String getStartime() {
        return startime;
    }

    public void setStartime(String startime) {
        this.startime = startime == null ? null : startime.trim();
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime == null ? null : endtime.trim();
    }

    public String getBindFlt() {
        return bindFlt;
    }

    public void setBindFlt(String bindFlt) {
        this.bindFlt = bindFlt == null ? null : bindFlt.trim();
    }

    public String getWeekCode() {
        return weekCode;
    }

    public void setWeekCode(String weekCode) {
        this.weekCode = weekCode == null ? null : weekCode.trim();
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId == null ? null : officeId.trim();
    }

}
