package com.neusoft.prss.message.entity;

public class HistoryDetailVO {
	 private static final long serialVersionUID = 1L;
	private String id;	//id	number(14)
	private String mid;	//指令消息id	number(14)
	private String mtotype;	//接收类型	number(4)
	private String mtoer;	//接收人	varchar2(20)
	private String receiver;	//实际接收	varchar2(20)
	private String receivtime;	//接收时间	varchar2(20)
	private String isreced;	//是否接收	number(2)
	private String replytime;	//回复时间	varchar2(20)
	private String replytext;	//回复内容	varchar2(50)
	private String isrepled;	//是否已回复	number(2)
	private String isproced;	//是否已处理	number(2)
	private String sounded;	//语音提示	number(2)
	private String ifspeak;	//语音	number(2)
	private String transtempl;	//转发模板	varchar2(20)
	private String istransed;	//是否已转发	number(2)
	private String procparamto;	//接收触发处理	varchar2(200)
	private String ifprocto;	//是否已触发处理	number(2)
	private String proceclsto;	//触发处理类	varchar2(200)
	private String iftrans;	//是否转发	number(2)
	private String receiveruid;	//接收人id	number(14)
	private String ifsms;	//是否短信	number(2)
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
	public String getReceiver() {
		return receiver;
	}
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	public String getReceivtime() {
		return receivtime;
	}
	public void setReceivtime(String receivtime) {
		this.receivtime = receivtime;
	}
	public String getIsreced() {
		return isreced;
	}
	public void setIsreced(String isreced) {
		this.isreced = isreced;
	}
	public String getReplytime() {
		return replytime;
	}
	public void setReplytime(String replytime) {
		this.replytime = replytime;
	}
	public String getReplytext() {
		return replytext;
	}
	public void setReplytext(String replytext) {
		this.replytext = replytext;
	}
	public String getIsrepled() {
		return isrepled;
	}
	public void setIsrepled(String isrepled) {
		this.isrepled = isrepled;
	}
	public String getIsproced() {
		return isproced;
	}
	public void setIsproced(String isproced) {
		this.isproced = isproced;
	}
	public String getSounded() {
		return sounded;
	}
	public void setSounded(String sounded) {
		this.sounded = sounded;
	}
	public String getIfspeak() {
		return ifspeak;
	}
	public void setIfspeak(String ifspeak) {
		this.ifspeak = ifspeak;
	}
	public String getTranstempl() {
		return transtempl;
	}
	public void setTranstempl(String transtempl) {
		this.transtempl = transtempl;
	}
	public String getIstransed() {
		return istransed;
	}
	public void setIstransed(String istransed) {
		this.istransed = istransed;
	}
	public String getProcparamto() {
		return procparamto;
	}
	public void setProcparamto(String procparamto) {
		this.procparamto = procparamto;
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
	public String getIftrans() {
		return iftrans;
	}
	public void setIftrans(String iftrans) {
		this.iftrans = iftrans;
	}
	public String getReceiveruid() {
		return receiveruid;
	}
	public void setReceiveruid(String receiveruid) {
		this.receiveruid = receiveruid;
	}
	public String getIfsms() {
		return ifsms;
	}
	public void setIfsms(String ifsms) {
		this.ifsms = ifsms;
	}


}
