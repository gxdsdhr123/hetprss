/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年9月1日 下午1:34:23
 *@author:Maxx
 *@version:[v1.0]
 */
package com.neusoft.prss.message.entity;

public class MessageTemplFromVO {

    /**
     *Discription:字段功能描述.
     */
    private static final long serialVersionUID = 1L;
    
    private String  id=""  ;   //  ID
    private String  mid="" ;   
    private String  tid="" ;  //  模板ID
    private String  mfromtype=""   ;   //  发送者类型
    private String  mfromer="" ;   //  发送者ID
    private String  mfromername="" ;   //  发送者名称
    private String  ifprocfrom="" ;   //  是否发送触发处理
    private String  proceclsfrom=""    ;   //  触发处理类
    private String  procdefparamfrom=""    ;   //  触发处理参数
    private String   mfromtypename="";//发送者名字
    private String  ifprocfromname="";
    private String proceclsfromname="";
    
	
    
    
    
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getProceclsfromname() {
		return proceclsfromname;
	}
	public void setProceclsfromname(String proceclsfromname) {
		this.proceclsfromname = proceclsfromname;
	}
	public String getIfprocfromname() {
		return ifprocfromname;
	}
	public void setIfprocfromname(String ifprocfromname) {
		this.ifprocfromname = ifprocfromname;
	}
	public String getMfromtypename() {
		return mfromtypename;
	}
	public void setMfromtypename(String mfromtypename) {
		this.mfromtypename = mfromtypename;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getMfromtype() {
		return mfromtype;
	}
	public void setMfromtype(String mfromtype) {
		this.mfromtype = mfromtype;
	}
	public String getMfromer() {
		return mfromer;
	}
	public void setMfromer(String mfromer) {
		this.mfromer = mfromer;
	}
	public String getMfromername() {
		return mfromername;
	}
	public void setMfromername(String mfromername) {
		this.mfromername = mfromername;
	}

	
	public String getIfprocfrom() {
		return ifprocfrom;
	}
	public void setIfprocfrom(String ifprocfrom) {
		this.ifprocfrom = ifprocfrom;
	}
	public String getProceclsfrom() {
		return proceclsfrom;
	}
	public void setProceclsfrom(String proceclsfrom) {
		this.proceclsfrom = proceclsfrom;
	}
	public String getProcdefparamfrom() {
		return procdefparamfrom;
	}
	public void setProcdefparamfrom(String procdefparamfrom) {
		this.procdefparamfrom = procdefparamfrom;
	}

    
}
