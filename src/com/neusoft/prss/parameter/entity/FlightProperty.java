/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年9月4日 下午6:49:27
 *@author:baochl
 *@version:[v1.0]
 */
package com.neusoft.prss.parameter.entity;

import java.io.Serializable;
import com.neusoft.framework.common.annotation.Column;

public class FlightProperty implements Serializable {

    /**
     *Discription:字段功能描述.
     */
    private static final long serialVersionUID = 7811041225606491695L;

    @Column(name = "PROPERTY_CODE")
    private String code;

    @Column(name = "PROPERTY_SHORTNAME")
    private String shortName;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
