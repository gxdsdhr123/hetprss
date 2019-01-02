/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年12月12日 下午3:32:40
 *@author:Maxx
 *@version:[v1.0]
 */
package com.neusoft.prss.rule.entity;

public class RuleWaterFillVO {

    private String id ;
    
    private String ruleId;
    
    private String addedWater;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getAddedWater() {
        return addedWater;
    }

    public void setAddedWater(String addedWater) {
        this.addedWater = addedWater;
    }
    
    
}
