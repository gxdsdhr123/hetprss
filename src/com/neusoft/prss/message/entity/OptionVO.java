/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年11月8日 下午2:28:19
 *@author:Maxx
 *@version:[v1.0]
 */
package com.neusoft.prss.message.entity;

public class OptionVO {

    private String parent;
    private int flag;
    private String mtoid;
    private int num;
    private String name;
    private String mid;
    private String trans_mid;
    private String sendtime;
    private String receivtime;
    
    public String getReceivtime() {
        return receivtime;
    }
    public void setReceivtime(String receivtime) {
        this.receivtime = receivtime;
    }
    public String getParent() {
        return parent;
    }
    public void setParent(String parent) {
        this.parent = parent;
    }
    public int getFlag() {
        return flag;
    }
    public void setFlag(int flag) {
        this.flag = flag;
    }
    public String getMtoid() {
        return mtoid;
    }
    public void setMtoid(String mtoid) {
        this.mtoid = mtoid;
    }
    public int getNum() {
        return num;
    }
    public void setNum(int num) {
        this.num = num;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getMid() {
        return mid;
    }
    public void setMid(String mid) {
        this.mid = mid;
    }
    public String getTrans_mid() {
        return trans_mid;
    }
    public void setTrans_mid(String trans_mid) {
        this.trans_mid = trans_mid;
    }
    public String getSendtime() {
        return sendtime;
    }
    public void setSendtime(String sendtime) {
        this.sendtime = sendtime;
    }
    
}
