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

public class TomorrowInfoEntity implements Serializable {

    /**
     *Discription:字段功能描述.
     */
    private static final long serialVersionUID = 1L;

    private int id ;//id
    
    private int main_id;//main_id
    
    private String std;//计起
    
    private String sta;//计落
    
    private String etd;//预起
    
    private String eta;//预落
    
    private String depart_apt;//起场
    
    private String arrival_apt;//落场
    
    private String share_flt_no;//共享航班号
    
    private String aircraft_number;//机号
    
    private String act_type;//机型
    
    private String airline_code;//航空公司

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMain_id() {
        return main_id;
    }

    public void setMain_id(int main_id) {
        this.main_id = main_id;
    }

    public String getStd() {
        return std;
    }

    public void setStd(String std) {
        this.std = std;
    }

    public String getSta() {
        return sta;
    }

    public void setSta(String sta) {
        this.sta = sta;
    }

    public String getEtd() {
        return etd;
    }

    public void setEtd(String etd) {
        this.etd = etd;
    }

    public String getEta() {
        return eta;
    }

    public void setEta(String eta) {
        this.eta = eta;
    }

    public String getDepart_apt() {
        return depart_apt;
    }

    public void setDepart_apt(String depart_apt) {
        this.depart_apt = depart_apt;
    }

    public String getArrival_apt() {
        return arrival_apt;
    }

    public void setArrival_apt(String arrival_apt) {
        this.arrival_apt = arrival_apt;
    }

    public String getShare_flt_no() {
        return share_flt_no;
    }

    public void setShare_flt_no(String share_flt_no) {
        this.share_flt_no = share_flt_no;
    }

    public String getAircraft_number() {
        return aircraft_number;
    }

    public void setAircraft_number(String aircraft_number) {
        this.aircraft_number = aircraft_number;
    }

    public String getAct_type() {
        return act_type;
    }

    public void setAct_type(String act_type) {
        this.act_type = act_type;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getAirline_code() {
        return airline_code;
    }

    public void setAirline_code(String airline_code) {
        this.airline_code = airline_code;
    }
    
    
}
