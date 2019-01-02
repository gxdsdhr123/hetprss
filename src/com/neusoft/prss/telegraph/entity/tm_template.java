package com.neusoft.prss.telegraph.entity;

public class tm_template {

	private String id;           //ID
	private String tg_type_id;   //报文类型
	private String tg_text;      //消息报文
	private String tg_name;      //报文名称
	private String createuser;   //创建
	private String createtime;   //创建时间
	private String varcols;      //参数IDs
	private String aln_2code;    //航空公司二字码
	private String update_time;  //更新时间
	private String update_user;  //更新人
	private String fio_type;     //航班进出港类型
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTg_type_id() {
		return tg_type_id;
	}
	public void setTg_type_id(String tg_type_id) {
		this.tg_type_id = tg_type_id;
	}
	public String getTg_text() {
		return tg_text;
	}
	public void setTg_text(String tg_text) {
		this.tg_text = tg_text;
	}
	public String getTg_name() {
		return tg_name;
	}
	public void setTg_name(String tg_name) {
		this.tg_name = tg_name;
	}
	public String getCreateuser() {
		return createuser;
	}
	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getVarcols() {
		return varcols;
	}
	public void setVarcols(String varcols) {
		this.varcols = varcols;
	}
	public String getAln_2code() {
		return aln_2code;
	}
	public void setAln_2code(String aln_2code) {
		this.aln_2code = aln_2code;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public String getUpdate_user() {
		return update_user;
	}
	public void setUpdate_user(String update_user) {
		this.update_user = update_user;
	}
	public String getFio_type() {
		return fio_type;
	}
	public void setFio_type(String fio_type) {
		this.fio_type = fio_type;
	}
	
}
