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



public class MessageTemplToVO {

    /**
     *Discription:字段功能描述.
     */
    private static final long serialVersionUID = 1L;
    
    private String  id  ;   //  ID
    private String  mid ;   //  模板ID
    private String tid;  //  模板ID
    private String  mtotype ;   //  接收者类型
    private String  mtoer   ;   //  接收者ID
    private String  ifprocto    ;   //  启用接受处理过程
    private String  proceclsto  ;   //  启用接受处理类
    private String  procdefparamto  ;   //  启用接受处理参数
    private String  iftrans ;   //  是否启用转发
    private String  transtemplid    ;   //  转发启用的模板ID
    private String  transtemplname  ;   //  转发启用的模板名称
    private String  mtoername   ;   //  接受者名称
    private String  iftoallrole ;   //  同角色岗位的在线用户是否都接收
    private String  ifsms   ;   //  手机短信
    private String  condition    ;   //  接收条件
    private String  mtotypename;
    private String  iftransname;
    private String  proceclstoname;
    private String  ifproctoname;
    private String  ifsmsname;
    private String  condtionname   ; 
    private String  colids;
    private String  drlStr;
    private String  drools;
    private String  ruleId;
    private String  userId;
    
    
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getRuleId() {
        return ruleId;
    }
    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }
    public String getDrools() {
        return drools;
    }
    public void setDrools(String drools) {
        this.drools = drools;
    }
    public String getColids() {
        return colids;
    }
    public void setColids(String colids) {
        this.colids = colids;
    }
    public String getDrlStr() {
        return drlStr;
    }
    public void setDrlStr(String drlStr) {
        this.drlStr = drlStr;
    }
    public String getCondition() {
        return condition;
    }
    public void setCondition(String condition) {
        this.condition = condition;
    }
    public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}
	public String getCondtionname() {
		return condtionname;
	}
	public void setCondtionname(String condtionname) {
		this.condtionname = condtionname;
	}
	public String getMtotypename() {
		return mtotypename;
	}
	public void setMtotypename(String mtotypename) {
		this.mtotypename = mtotypename;
	}
	
	public String getProceclstoname() {
		return proceclstoname;
	}
	public void setProceclstoname(String proceclstoname) {
		this.proceclstoname = proceclstoname;
	}
	
	public String getIfproctoname() {
		return ifproctoname;
	}
	public void setIfproctoname(String ifproctoname) {
		this.ifproctoname = ifproctoname;
	}
	
	public String getIftransname() {
		return iftransname;
	}
	public void setIftransname(String iftransname) {
		this.iftransname = iftransname;
	}
	public String getIfsmsname() {
		return ifsmsname;
	}
	public void setIfsmsname(String ifsmsname) {
		this.ifsmsname = ifsmsname;
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
	public String getMtotype() {
        return mtotype;
    }
    public void setMtotype(String mtotype) {
        this.mtotype = mtotype;
    }
    public String getMtoer() {
        return mtoer;
    }
    public void setMtoer(String mtoer) {
        this.mtoer = mtoer;
    }
    public String getIfprocto() {
        return ifprocto;
    }
    public void setIfprocto(String ifprocto) {
        this.ifprocto = ifprocto;
    }
    public String getProceclsto() {
        return proceclsto;
    }
    public void setProceclsto(String proceclsto) {
        this.proceclsto = proceclsto;
    }
    public String getProcdefparamto() {
        return procdefparamto;
    }
    public void setProcdefparamto(String procdefparamto) {
        this.procdefparamto = procdefparamto;
    }
    public String getIftrans() {
        return iftrans;
    }
    public void setIftrans(String iftrans) {
        this.iftrans = iftrans;
    }
    public String getTranstemplid() {
        return transtemplid;
    }
    public void setTranstemplid(String transtemplid) {
        this.transtemplid = transtemplid;
    }
    public String getTranstemplname() {
        return transtemplname;
    }
    public void setTranstemplname(String transtemplname) {
        this.transtemplname = transtemplname;
    }
    public String getMtoername() {
        return mtoername;
    }
    public void setMtoername(String mtoername) {
        this.mtoername = mtoername;
    }
    public String getIftoallrole() {
        return iftoallrole;
    }
    public void setIftoallrole(String iftoallrole) {
        this.iftoallrole = iftoallrole;
    }
    public String getIfsms() {
        return ifsms;
    }
    public void setIfsms(String ifsms) {
        this.ifsms = ifsms;
    }

}
