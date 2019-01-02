
package com.neusoft.prss.arrange.entity;

import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmpPlanMain implements Serializable {
    private static final long serialVersionUID = 5571678019934672966L;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private String id;

    private String officeId;

    private String workerId;

    private String pdate;

    private String loginName;

    private String stime1Label = "";

    private String etime1Label = "";

    private String stime1 = "";

    private String etime1 = "";

    private String stime2Label = "";

    private String etime2Label = "";

    private String stime2 = "";

    private String etime2 = "";

    private String stime3Label = "";

    private String etime3Label = "";

    private String stime3 = "";

    private String etime3 = "";

    private String busyInterval = "0";

    private String idleInterval = "0";

    private String sortnum;

    private String shiftsType;

    private String bindFlight;

    private String updateTime;

    private String shiftsId = "";

    private String shiftsName = "";

    private String label1 = "";

    private String label2 = "";

    private String label3 = "";

    private String blockupTime = "";

    private String blockupReason = "";

    public String getStime1Label() {
        return stime1Label;
    }

    public void setStime1Label(String stime1Label) {
        this.stime1Label = stime1Label;
    }

    public String getEtime1Label() {
        return etime1Label;
    }

    public void setEtime1Label(String etime1Label) {
        this.etime1Label = etime1Label;
    }

    public String getStime1() {
        return stime1;
    }

    public void setStime1(String stime1) {
        this.stime1 = stime1;
    }

    public String getEtime1() {
        return etime1;
    }

    public void setEtime1(String etime1) {
        this.etime1 = etime1;
    }

    public String getStime2Label() {
        return stime2Label;
    }

    public void setStime2Label(String stime2Label) {
        this.stime2Label = stime2Label;
    }

    public String getEtime2Label() {
        return etime2Label;
    }

    public void setEtime2Label(String etime2Label) {
        this.etime2Label = etime2Label;
    }

    public String getStime2() {
        return stime2;
    }

    public void setStime2(String stime2) {
        this.stime2 = stime2;
    }

    public String getEtime2() {
        return etime2;
    }

    public void setEtime2(String etime2) {
        this.etime2 = etime2;
    }

    public String getStime3Label() {
        return stime3Label;
    }

    public void setStime3Label(String stime3Label) {
        this.stime3Label = stime3Label;
    }

    public String getEtime3Label() {
        return etime3Label;
    }

    public void setEtime3Label(String etime3Label) {
        this.etime3Label = etime3Label;
    }

    public String getStime3() {
        return stime3;
    }

    public void setStime3(String stime3) {
        this.stime3 = stime3;
    }

    public String getEtime3() {
        return etime3;
    }

    public void setEtime3(String etime3) {
        this.etime3 = etime3;
    }

    public String getBusyInterval() {
        return busyInterval;
    }

    public void setBusyInterval(String busyInterval) {
        this.busyInterval = busyInterval;
    }

    public String getIdleInterval() {
        return idleInterval;
    }

    public void setIdleInterval(String idleInterval) {
        this.idleInterval = idleInterval;
    }

    public String getSortnum() {
        return sortnum;
    }

    public void setSortnum(String sortnum) {
        this.sortnum = sortnum;
    }

    public String getShiftsType() {
        return shiftsType;
    }

    public void setShiftsType(String shiftsType) {
        this.shiftsType = shiftsType;
    }

    public String getBindFlight() {
        return bindFlight;
    }

    public void setBindFlight(String bindFlight) {
        this.bindFlight = bindFlight;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

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
        this.shiftsName = shiftsName;
    }

    public String getLabel1() {
        return label1;
    }

    public void setLabel1(String label1) {
        this.label1 = label1;
    }

    public String getLabel2() {
        return label2;
    }

    public void setLabel2(String label2) {
        this.label2 = label2;
    }

    public String getLabel3() {
        return label3;
    }

    public void setLabel3(String label3) {
        this.label3 = label3;
    }

    public String getBlockupTime() {
        return blockupTime;
    }

    public void setBlockupTime(String blockupTime) {
        this.blockupTime = blockupTime;
    }

    public String getBlockupReason() {
        return blockupReason;
    }

    public void setBlockupReason(String blockupReason) {
        this.blockupReason = blockupReason;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public String getPdate() {
        return pdate;
    }

    public void setPdate(String pdate) {
        this.pdate = pdate;
    }

}
