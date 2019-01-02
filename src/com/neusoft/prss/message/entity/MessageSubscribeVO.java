package com.neusoft.prss.message.entity;

public class MessageSubscribeVO {
	 private static final long serialVersionUID = 1L;
	 
	 
	 private String id;	                //ID        	NUMBER(14)	序列MM_SCHE_S
	 private String mtemplid;	//模板ID    	NUMBER(14)	对应MM_TEMPLATE主表ID
	 private String crtime;	//创建时间  	VARCHAR2(20)	
	 private String cruserid;	//创建人ID  	VARCHAR2(20)	
	 private String cruseren;	//创建人    	VARCHAR2(50)	
	 private String totype;	//接收类型  	NUMBER(4)	
	 private String torange;	//接收范围ID	VARCHAR2(200)	
	 private String mtext;	//内容      	VARCHAR2(1000)	
	 private String schtime;	//定时时间  	VARCHAR2(20)	
	 private String hbiotype;	//进出类型  	VARCHAR2(20)	
	 private String flightnumber;	//航班号    	VARCHAR2(20)	
	 private String torangenames;	//接收范围名	VARCHAR2(1000)	
	 private String hbevent;	//触发事件  	VARCHAR2(20)	
	 private String disable;//失效状态  	NUMBER(4)	
	 private String condition;	//触发条件	VARCHAR2(1000)	
	 private String ifsms;	//短信      	NUMBER(4)	
	 private String mfromtype;
	 private String mtemplname;
	 private String hbeventname;
	 private String hbiotypename;
	 private String disablename;
	 private String totypename;
	 private String searchdata;
	 private String ruleid;
	 private String colids;
	 private String varcols;
	 private String drlStr;
	 private String drools;
	 private int jobId;
	 private int ifpopup;
	 private String sendDate;
	 
	 public String getSendDate() {
		return sendDate;
	}
	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}
	private String kindname;
	 
	 
	 public String getKindname() {
        return kindname;
    }
    public void setKindname(String kindname) {
        this.kindname = kindname;
    }
    public int getIfpopup() {
        return ifpopup;
    }
    public void setIfpopup(int ifpopup) {
        this.ifpopup = ifpopup;
    }
    public int getJobId() {
        return jobId;
    }
    public void setJobId(int jobId) {
        this.jobId = jobId;
    }
    public String getDrools() {
        return drools;
    }
    public void setDrools(String drools) {
        this.drools = drools;
    }
    public String getDrlStr() {
        return drlStr;
    }
    public void setDrlStr(String drlStr) {
        this.drlStr = drlStr;
    }
    private int start;
	 private int limit;
	 
	 
	public String getVarcols() {
		return varcols;
	}
	public void setVarcols(String varcols) {
		this.varcols = varcols;
	}
	public String getColids() {
		return colids;
	}
	public void setColids(String colids) {
		this.colids = colids;
	}
	public String getRuleid() {
		return ruleid;
	}
	public void setRuleid(String ruleid) {
		this.ruleid = ruleid;
	}
	public String getSearchdata() {
		return searchdata;
	}
	public void setSearchdata(String searchdata) {
		this.searchdata = searchdata;
	}
	public String getTotypename() {
		return totypename;
	}
	public void setTotypename(String totypename) {
		this.totypename = totypename;
	}
	public String getHbiotypename() {
		return hbiotypename;
	}
	public void setHbiotypename(String hbiotypename) {
		this.hbiotypename = hbiotypename;
	}
	public String getDisablename() {
		return disablename;
	}
	public void setDisablename(String disablename) {
		this.disablename = disablename;
	}
	public String getHbeventname() {
		return hbeventname;
	}
	public void setHbeventname(String hbeventname) {
		this.hbeventname = hbeventname;
	}
	public String getMtemplname() {
		return mtemplname;
	}
	public void setMtemplname(String mtemplname) {
		this.mtemplname = mtemplname;
	}
	public String getMfromtype() {
		return mfromtype;
	}
	public void setMfromtype(String mfromtype) {
		this.mfromtype = mfromtype;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMtemplid() {
		return mtemplid;
	}
	public void setMtemplid(String mtemplid) {
		this.mtemplid = mtemplid;
	}
	public String getCrtime() {
		return crtime;
	}
	public void setCrtime(String crtime) {
		this.crtime = crtime;
	}
	public String getCruserid() {
		return cruserid;
	}
	public void setCruserid(String cruserid) {
		this.cruserid = cruserid;
	}
	public String getCruseren() {
		return cruseren;
	}
	public void setCruseren(String cruseren) {
		this.cruseren = cruseren;
	}
	public String getTotype() {
		return totype;
	}
	public void setTotype(String totype) {
		this.totype = totype;
	}
	public String getTorange() {
		return torange;
	}
	public void setTorange(String torange) {
		this.torange = torange;
	}
	public String getMtext() {
		return mtext;
	}
	public void setMtext(String mtext) {
		this.mtext = mtext;
	}
	public String getSchtime() {
		return schtime;
	}
	public void setSchtime(String schtime) {
		this.schtime = schtime;
	}
	public String getHbiotype() {
		return hbiotype;
	}
	public void setHbiotype(String hbiotype) {
		this.hbiotype = hbiotype;
	}
	public String getFlightnumber() {
		return flightnumber;
	}
	public void setFlightnumber(String flightnumber) {
		this.flightnumber = flightnumber;
	}
	public String getTorangenames() {
		return torangenames;
	}
	public void setTorangenames(String torangenames) {
		this.torangenames = torangenames;
	}
	public String getHbevent() {
		return hbevent;
	}
	public void setHbevent(String hbevent) {
		this.hbevent = hbevent;
	}
	public String getDisable() {
		return disable;
	}
	public void setDisable(String disable) {
		this.disable = disable;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getIfsms() {
		return ifsms;
	}
	public void setIfsms(String ifsms) {
		this.ifsms = ifsms;
	}
	 
	 

	 
	 
	 
}
