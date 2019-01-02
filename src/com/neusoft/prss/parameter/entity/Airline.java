/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年9月4日 上午10:53:30
 *@author:baochl
 *@version:[v1.0]
 */
package com.neusoft.prss.parameter.entity;

import java.io.Serializable;
import com.neusoft.framework.common.annotation.Column;
/**
 * 
 *Description:航空公司.
 *@author:baochl
 *@version:版本号
 */
public class Airline implements Serializable {

    /**
     *Discription:字段功能描述.
     */
    private static final long serialVersionUID = 8044587399047487891L;

    @Column(name = "AIRLINES_CODE2")
    private String airCode2;//航空公司二字码

    @Column(name = "AIRLINES_SHORTNAME")
    private String shortName;//简称

    public String getAirCode2() {
        return airCode2;
    }

    public void setAirCode2(String airCode2) {
        this.airCode2 = airCode2;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
