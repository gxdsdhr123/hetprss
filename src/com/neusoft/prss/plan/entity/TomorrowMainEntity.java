/**
 *application name:bt-plan-service
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2018 Neusoft LTD.
 *company:Neusoft
 *time:2018年4月12日 上午11:38:14
 *@author:neusoft
 *@version:[v1.0]
 */
package com.neusoft.prss.plan.entity;

import java.io.Serializable;
import java.util.List;

public class TomorrowMainEntity implements Serializable {

    /**
     *Discription:字段功能描述.
     */
    private static final long serialVersionUID = 1L;

    private int id ;//id
    
    private String flt_no;//航班号
    
    private String property_code;//性质
    
    private String attr_code;//属性
    
    private String flt_date;//航班日期
    
    private int data_source;//数据来源
    
    private int msg_id;//报文ID
    
    private int data_state;//数据状态
    
    private String create_date;//创建时间
    
    private String create_user;//创建人
    
    private List<TomorrowInfoEntity> infoList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFlt_no() {
        return flt_no;
    }

    public void setFlt_no(String flt_no) {
        this.flt_no = flt_no;
    }


    public String getFlt_date() {
        return flt_date;
    }

    public void setFlt_date(String flt_date) {
        this.flt_date = flt_date;
    }

    public int getData_source() {
        return data_source;
    }

    public void setData_source(int data_source) {
        this.data_source = data_source;
    }

    public int getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(int msg_id) {
        this.msg_id = msg_id;
    }

    public int getData_state() {
        return data_state;
    }

    public void setData_state(int data_state) {
        this.data_state = data_state;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getCreate_user() {
        return create_user;
    }

    public void setCreate_user(String create_user) {
        this.create_user = create_user;
    }

    public List<TomorrowInfoEntity> getInfoList() {
        return infoList;
    }

    public void setInfoList(List<TomorrowInfoEntity> infoList) {
        this.infoList = infoList;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getProperty_code() {
        return property_code;
    }

    public void setProperty_code(String property_code) {
        this.property_code = property_code;
    }

    public String getAttr_code() {
        return attr_code;
    }

    public void setAttr_code(String attr_code) {
        this.attr_code = attr_code;
    }
    
    
}
