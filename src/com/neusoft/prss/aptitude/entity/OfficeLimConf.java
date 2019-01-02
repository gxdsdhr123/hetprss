/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年10月30日 上午11:04:08
 *@author:Heqg
 *@version:[v1.0]
 */
package com.neusoft.prss.aptitude.entity;

public class OfficeLimConf {
	
	private String officeId;
    private Integer jxLimit;
    private Integer jwLimit;
    private Integer hsLimit;

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId == null ? null : officeId.trim();
    }

    public Integer getJxLimit() {
        return jxLimit;
    }

    public void setJxLimit(Integer jxLimit) {
        this.jxLimit = jxLimit;
    }

    public Integer getJwLimit() {
        return jwLimit;
    }

    public void setJwLimit(Integer jwLimit) {
        this.jwLimit = jwLimit;
    }

    public Integer getHsLimit() {
        return hsLimit;
    }

    public void setHsLimit(Integer hsLimit) {
        this.hsLimit = hsLimit;
    }

}
