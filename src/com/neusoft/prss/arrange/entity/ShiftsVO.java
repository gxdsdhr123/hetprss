/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年10月27日 下午8:28:54
 *@author:lirr
 *@version:[v1.0]
 */
package com.neusoft.prss.arrange.entity;

import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShiftsVO implements Serializable {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    private static final long serialVersionUID = -4599458026115473400L;

    private String id;

    private String shiftsName;

    private String stime;

    private String etime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShiftsName() {
        return shiftsName;
    }

    public void setShiftsName(String shiftsName) {
        this.shiftsName = shiftsName;
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

}
