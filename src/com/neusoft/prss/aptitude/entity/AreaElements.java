/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年11月1日 上午10:17:27
 *@author:Heqg
 *@version:[v1.0]
 */
package com.neusoft.prss.aptitude.entity;

import com.neusoft.framework.common.utils.StringUtils;

public class AreaElements {
	
	private String id;
    private String elementCode;
    private String updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getElementCode() {
        return elementCode;
    }

    public void setElementCode(String elementCode) {
        this.elementCode = StringUtils.isEmpty(elementCode)? " " : elementCode.trim();
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

}
