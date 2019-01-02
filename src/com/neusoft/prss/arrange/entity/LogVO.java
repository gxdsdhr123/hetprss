/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年10月29日 下午3:19:54
 *@author:lirr
 *@version:[v1.0]
 */
package com.neusoft.prss.arrange.entity;

import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogVO implements Serializable {
    private static final long serialVersionUID = -4333867914874902326L;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private String id;

    private String officeId;

    private String workerId;

    private String workerName;

    private String optType;

    private String stime;

    private String etime;

    private String rtime;

    private String rworkerId;

    private String blockupReason;

    private String workTime;

    public String getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    public String getBlockupReason() {
        return blockupReason;
    }

    public void setBlockupReason(String blockupReason) {
        this.blockupReason = blockupReason;
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

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getOptType() {
        return optType;
    }

    public void setOptType(String optType) {
        this.optType = optType;
    }

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    public String getEtime() {
        return etime;
    }

    public void setEtime(String etime) {
        this.etime = etime;
    }

    public String getRtime() {
        return rtime;
    }

    public void setRtime(String rtime) {
        this.rtime = rtime;
    }

    public String getRworkerId() {
        return rworkerId;
    }

    public void setRworkerId(String rworkerId) {
        this.rworkerId = rworkerId;
    }

}
