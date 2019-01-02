package com.neusoft.prss.telegraph.entity;

public class TeleGraphTempl {

	private String id;
	private String accept_time; //接收时间
	private String xmldata;     //报文信息
	private String tg_resource; //来源
	private String tg_type;     //报文大类
	private String tg_type_id;  //报文类型小类
	private String flight_date; //航班日期
	private String flight_number;//航班号
	private String aircraft;    //机号
	private String aln_2code;   //航空公司二字码
	private String depart_apt4code;//起场
	private String arrival_apt4code;//落场
	private String tg_time;     //报文时间
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAccept_time() {
		return accept_time;
	}
	public void setAccept_time(String accept_time) {
		this.accept_time = accept_time;
	}
	public String getXmldata() {
		return xmldata;
	}
	public void setXmldata(String xmldata) {
		this.xmldata = xmldata;
	}
	public String getTg_resource() {
		return tg_resource;
	}
	public void setTg_resource(String tg_resource) {
		this.tg_resource = tg_resource;
	}
	public String getTg_type() {
		return tg_type;
	}
	public void setTg_type(String tg_type) {
		this.tg_type = tg_type;
	}
	public String getTg_type_id() {
		return tg_type_id;
	}
	public void setTg_type_id(String tg_type_id) {
		this.tg_type_id = tg_type_id;
	}
	public String getFlight_date() {
		return flight_date;
	}
	public void setFlight_date(String flight_date) {
		this.flight_date = flight_date;
	}
	public String getFlight_number() {
		return flight_number;
	}
	public void setFlight_number(String flight_number) {
		this.flight_number = flight_number;
	}
	public String getAircraft() {
		return aircraft;
	}
	public void setAircraft(String aircraft) {
		this.aircraft = aircraft;
	}
	public String getAln_2code() {
		return aln_2code;
	}
	public void setAln_2code(String aln_2code) {
		this.aln_2code = aln_2code;
	}
	public String getDepart_apt4code() {
		return depart_apt4code;
	}
	public void setDepart_apt4code(String depart_apt4code) {
		this.depart_apt4code = depart_apt4code;
	}
	public String getArrival_apt4code() {
		return arrival_apt4code;
	}
	public void setArrival_apt4code(String arrival_apt4code) {
		this.arrival_apt4code = arrival_apt4code;
	}
	public String getTg_time() {
		return tg_time;
	}
	public void setTg_time(String tg_time) {
		this.tg_time = tg_time;
	}
	
}
