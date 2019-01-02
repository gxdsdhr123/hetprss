package com.neusoft.prss.stand.entity;

import com.alibaba.fastjson.JSONObject;

public class ResultByCus {
	
	String code;
	String msg;
	JSONObject data;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public JSONObject getData() {
		return data;
	}
	public void setData(JSONObject data) {
		this.data = data;
	}
	

}
