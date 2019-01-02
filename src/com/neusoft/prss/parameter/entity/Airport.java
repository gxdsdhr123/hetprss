/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年9月4日 下午6:40:52
 *@author:baochl
 *@version:[v1.0]
 */
package com.neusoft.prss.parameter.entity;

import java.io.Serializable;
import com.neusoft.framework.common.annotation.Column;
/**
 * 
 *Description:机场实体.
 *@author:baochl
 *@version:版本号
 */
public class Airport implements Serializable {

    /**
     *Discription:字段功能描述.
     */
    private static final long serialVersionUID = 8621125476391544555L;

    @Column(name = "AIRPORT_CODE4")
    private String aptCode4;//四字码

    @Column(name = "AIRPORT_NAME")
    private String aptName;//名称

    public String getAptCode4() {
        return aptCode4;
    }

    public void setAptCode4(String aptCode4) {
        this.aptCode4 = aptCode4;
    }

    public String getAptName() {
        return aptName;
    }

    public void setAptName(String aptName) {
        this.aptName = aptName;
    }
}
