package com.neusoft.prss.message.entity;

import java.io.Serializable;

public class HistoryMessageVO implements Serializable{
	
	 private static final long serialVersionUID = 1L;
	 
	 
	private String id	;//ID	NUMBER(14)	序列MM_INFO_S
	private String mtype	;	//VARCHAR2(10)	对应MM_TYPE主表id
	private String mtitle	;	//VARCHAR2(50)	
	private String mtime	;	//VARCHAR2(20)	
	private String mflightdate;		//VARCHAR2(10)	
	private String mtext	;	//VARCHAR2(1000)	
	private String sender	;	//VARCHAR2(20)	
	private String senderdep	;	//VARCHAR2(20)	
	private String senduid	;	//VARCHAR2(20)	
	private String ifflight	;	//NUMBER(2)	
	private String rfid		;//NUMBER(14)	动态航班表B_HANGBAN_TODAY_SUB主表id
	private String flightnumber	;	//VARCHAR2(20)	
	private String ifautosend	;	//NUMBER(2)	
	private String autosendtime	;	//VARCHAR2(20)	
	private String issended	;	//NUMBER(2)	
	private String ifproc	;	//NUMBER(2)	
	private String procparam;		//VARCHAR2(100)	
	private String mtemplid	;	//NUMBER(14)	对应MM_TEMPLATE主表ID
	private String ifreply	;	//NUMBER(2)	
	private String ifsound	;	//NUMBER(2)	
	private String soundtxt	;	//VARCHAR2(50)	
	private String isallreplyed	;	//NUMBER(2)	
	private String sendtime	;	//VARCHAR2(20)	
	private String isproced	;	//NUMBER(2)	
	private String sendercn	;	//VARCHAR2(50)	
	private String realtime	;	//VARCHAR2(20)	
	private String mfromtype;		//NUMBER(4)	
	private String mfromer	;	//VARCHAR2(20)	
	private String extact	;	//VARCHAR2(100)	
	private String transsubid;		//NUMBER(14)	
	private String procecls;
	private String pushflag;
	private String message;
	private String mtempid;
	private String mtotype;
	private String r;
	
	
	
	
	public String getR() {
		return r;
	}
	public void setR(String r) {
		this.r = r;
	}
	public String getMtotype() {
		return mtotype;
	}
	public void setMtotype(String mtotype) {
		this.mtotype = mtotype;
	}
	public String getMtempid() {
		return mtempid;
	}
	public void setMtempid(String mtempid) {
		this.mtempid = mtempid;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMtype() {
		return mtype;
	}
	public void setMtype(String mtype) {
		this.mtype = mtype;
	}
	public String getMtitle() {
		return mtitle;
	}
	public void setMtitle(String mtitle) {
		this.mtitle = mtitle;
	}
	public String getMtime() {
		return mtime;
	}
	public void setMtime(String mtime) {
		this.mtime = mtime;
	}
	public String getMflightdate() {
		return mflightdate;
	}
	public void setMflightdate(String mflightdate) {
		this.mflightdate = mflightdate;
	}
	public String getMtext() {
		return mtext;
	}
	public void setMtext(String mtext) {
		this.mtext = mtext;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getSenderdep() {
		return senderdep;
	}
	public void setSenderdep(String senderdep) {
		this.senderdep = senderdep;
	}
	public String getSenduid() {
		return senduid;
	}
	public void setSenduid(String senduid) {
		this.senduid = senduid;
	}
	public String getIfflight() {
		return ifflight;
	}
	public void setIfflight(String ifflight) {
		this.ifflight = ifflight;
	}
	public String getRfid() {
		return rfid;
	}
	public void setRfid(String rfid) {
		this.rfid = rfid;
	}
	public String getFlightnumber() {
		return flightnumber;
	}
	public void setFlightnumber(String flightnumber) {
		this.flightnumber = flightnumber;
	}
	public String getIfautosend() {
		return ifautosend;
	}
	public void setIfautosend(String ifautosend) {
		this.ifautosend = ifautosend;
	}
	public String getAutosendtime() {
		return autosendtime;
	}
	public void setAutosendtime(String autosendtime) {
		this.autosendtime = autosendtime;
	}
	public String getIssended() {
		return issended;
	}
	public void setIssended(String issended) {
		this.issended = issended;
	}
	public String getIfproc() {
		return ifproc;
	}
	public void setIfproc(String ifproc) {
		this.ifproc = ifproc;
	}
	public String getProcparam() {
		return procparam;
	}
	public void setProcparam(String procparam) {
		this.procparam = procparam;
	}
	public String getMtemplid() {
		return mtemplid;
	}
	public void setMtemplid(String mtemplid) {
		this.mtemplid = mtemplid;
	}
	public String getIfreply() {
		return ifreply;
	}
	public void setIfreply(String ifreply) {
		this.ifreply = ifreply;
	}
	public String getIfsound() {
		return ifsound;
	}
	public void setIfsound(String ifsound) {
		this.ifsound = ifsound;
	}
	public String getSoundtxt() {
		return soundtxt;
	}
	public void setSoundtxt(String soundtxt) {
		this.soundtxt = soundtxt;
	}
	public String getIsallreplyed() {
		return isallreplyed;
	}
	public void setIsallreplyed(String isallreplyed) {
		this.isallreplyed = isallreplyed;
	}
	public String getSendtime() {
		return sendtime;
	}
	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
	}
	public String getIsproced() {
		return isproced;
	}
	public void setIsproced(String isproced) {
		this.isproced = isproced;
	}
	public String getSendercn() {
		return sendercn;
	}
	public void setSendercn(String sendercn) {
		this.sendercn = sendercn;
	}
	public String getRealtime() {
		return realtime;
	}
	public void setRealtime(String realtime) {
		this.realtime = realtime;
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
	public String getExtact() {
		return extact;
	}
	public void setExtact(String extact) {
		this.extact = extact;
	}
	public String getTranssubid() {
		return transsubid;
	}
	public void setTranssubid(String transsubid) {
		this.transsubid = transsubid;
	}
	public String getProcecls() {
		return procecls;
	}
	public void setProcecls(String procecls) {
		this.procecls = procecls;
	}
	public String getPushflag() {
		return pushflag;
	}
	public void setPushflag(String pushflag) {
		this.pushflag = pushflag;
	}

	
	

}
