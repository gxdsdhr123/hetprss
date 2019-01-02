/**
 *application name:bt-plan-service
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2018 Neusoft LTD.
 *company:Neusoft
 *time:2018年4月12日 上午11:38:14
 *@author:neusoft
 *@version:[v1.0]
 */
package com.neusoft.prss.plan.entity;

import java.io.Serializable;

public class CAACMainEntity implements Serializable {

    /**
     *Discription:字段功能描述.
     */
    private static final long serialVersionUID = 1L;

    private int id ;//id
    
    private String fltNo;//航班号
    
    private String fltWeek;//班期
    
    private String planDateBegin;//执行日期
    
    private String planDateEnd;//终止日期
    
    private String createDate;//创建时间
    
    private String createUser;//创建人
    
    private String departApt;//起场
    
    private String arrivalApt;//落场
    
    private String std;//计起
    
    private String sta;//计落
    
    private String shareFltNo;//共享航班号
    
    private String attrCode;//属性
    
    private String actType;//机型
    
    private String remark;//备注
    
    public String getDepartApt() {
        return departApt;
    }

    public void setDepartApt(String departApt) {
        this.departApt = departApt;
    }

    public String getArrivalApt() {
        return arrivalApt;
    }

    public void setArrivalApt(String arrivalApt) {
        this.arrivalApt = arrivalApt;
    }

    public String getStd() {
        return std;
    }

    public void setStd(String std) {
        this.std = std;
    }

    public String getSta() {
        return sta;
    }

    public void setSta(String sta) {
        this.sta = sta;
    }

    public String getShareFltNo() {
        return shareFltNo;
    }

    public void setShareFltNo(String shareFltNo) {
        this.shareFltNo = shareFltNo;
    }

    public String getAttrCode() {
        return attrCode;
    }

    public void setAttrCode(String attrCode) {
        this.attrCode = attrCode;
    }

    public String getActType() {
        return actType;
    }

    public void setActType(String actType) {
        this.actType = actType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFltNo() {
        return fltNo;
    }

    public void setFltNo(String fltNo) {
        this.fltNo = fltNo;
    }

    public String getFltWeek() {
        return fltWeek;
    }

    public void setFltWeek(String fltWeek) {
        this.fltWeek = fltWeek;
    }

    public String getPlanDateBegin() {
        return planDateBegin;
    }

    public void setPlanDateBegin(String planDateBegin) {
        this.planDateBegin = planDateBegin;
    }

    public String getPlanDateEnd() {
        return planDateEnd;
    }

    public void setPlanDateEnd(String planDateEnd) {
        this.planDateEnd = planDateEnd;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
    
}
