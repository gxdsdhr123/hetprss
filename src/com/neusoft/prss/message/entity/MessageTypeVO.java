/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年8月29日 下午6:56:31
 *@author:Maxx
 *@version:[v1.0]
 */
package com.neusoft.prss.message.entity;

import com.neusoft.framework.common.persistence.DataEntity;

public class MessageTypeVO extends DataEntity<MessageTypeVO>{

    /**
     *Discription:字段功能描述.
     */
    private static final long serialVersionUID = 1L;

    
    private String id;//ID

    private String tcode="";//类型代码

    private String tname="";//类型名称

    private int ifreply=0;//是否需要回复 0 不需要

    private int ifflight=0;//是否与航班关联 0 不需要

    private String disc="";//描述

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTcode() {
        return tcode;
    }

    public void setTcode(String tcode) {
        this.tcode = tcode;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public int getIfreply() {
        return ifreply;
    }

    public void setIfreply(int ifreply) {
        this.ifreply = ifreply;
    }

    public int getIfflight() {
        return ifflight;
    }

    public void setIfflight(int ifflight) {
        this.ifflight = ifflight;
    }

    public String getDisc() {
        return disc;
    }

    public void setDisc(String disc) {
        this.disc = disc;
    }
    
    
}
