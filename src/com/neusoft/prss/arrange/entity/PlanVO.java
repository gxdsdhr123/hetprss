/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年10月26日 下午8:10:23
 *@author:lirr
 *@version:[v1.0]
 */
package com.neusoft.prss.arrange.entity;

import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlanVO implements Serializable {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    private static final long serialVersionUID = -5476133637537807006L;

    private String title;

    private String start;

    private String end;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

}
