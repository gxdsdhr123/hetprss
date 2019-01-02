/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年9月4日 下午6:44:20
 *@author:baochl
 *@version:[v1.0]
 */
package com.neusoft.prss.parameter.entity;

import java.io.Serializable;
import com.neusoft.framework.common.annotation.Column;

/**
 * 
 *Description:机型实体.
 *@author:baochl
 *@version:版本号
 */
public class AircraftType implements Serializable {
    /**
     *Discription:字段功能描述.
     */
    private static final long serialVersionUID = -2256284117759756095L;

    @Column(name = "AIRCRAFT_TYPE")
    private String actType;

    @Column(name = "AIRCRAFT_TYPE_SHORTNAME")
    private String actShortName;

    public String getActType() {
        return actType;
    }

    public void setActType(String actType) {
        this.actType = actType;
    }

    public String getActShortName() {
        return actShortName;
    }

    public void setActShortName(String actShortName) {
        this.actShortName = actShortName;
    }
}
