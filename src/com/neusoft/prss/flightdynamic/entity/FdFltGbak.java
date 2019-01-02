/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年9月26日 下午7:03:16
 *@author:yu-zd
 *@version:[v1.0]
 */
package com.neusoft.prss.flightdynamic.entity;

import java.io.Serializable;

import com.alibaba.fastjson.JSONArray;
/**
 * 
 *Description:fd_flt_gbak航程信息实体类.
 *@author:SunJ
 *@version:版本号
 */
public class FdFltGbak implements Serializable {
	
	private static final long serialVersionUID = 1L;

	//主键
	private String id;
	//次日计划id
	private String plnGbakid;
	//航班日期
	private String flightDate;
	//进港（回程）航班号
	private String bakFltno;
	//出港（去程）航班号
	private String goFltno;
	//进港航班性质
	private String bakProperty;
	//出港航班性质
	private String goProperty;
	//航站楼
	private String terminal;
	//外航、国内标识
	private String alnFlag;
	//创建人
	private String createUser;
	//创建时间
	private String createTime;
	//修改人
	private String updateUser;
	//修改时间
	private String updateTime;
	//是否客运
	private String ifPax;
	//是否过夜
	private String ifStay;
	//是否结束
	private String ifOver;
	//是否经停
	private String ifTransit;
	//详细信息列表
	private JSONArray detailArray;

	public JSONArray getDetailArray() {
		return detailArray;
	}

	public void setDetailArray(JSONArray detailArray) {
		this.detailArray = detailArray;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPlnGbakid() {
		return plnGbakid;
	}

	public void setPlnGbakid(String plnGbakid) {
		this.plnGbakid = plnGbakid;
	}

	public String getFlightDate() {
		return flightDate;
	}

	public void setFlightDate(String flightDate) {
		this.flightDate = flightDate == null ? null : flightDate.trim();
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal == null ? null : terminal.trim();
	}

	public String getAlnFlag() {
		return alnFlag;
	}

	public void setAlnFlag(String alnFlag) {
		this.alnFlag = alnFlag == null ? null : alnFlag.trim();
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser == null ? null : createUser.trim();
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser == null ? null : updateUser.trim();
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getBakFltno() {
		return bakFltno;
	}

	public void setBakFltno(String bakFltno) {
		this.bakFltno = bakFltno;
	}

	public String getGoFltno() {
		return goFltno;
	}

	public void setGoFltno(String goFltno) {
		this.goFltno = goFltno;
	}

	public String getBakProperty() {
		return bakProperty;
	}

	public void setBakProperty(String bakProperty) {
		this.bakProperty = bakProperty;
	}

	public String getGoProperty() {
		return goProperty;
	}

	public void setGoProperty(String goProperty) {
		this.goProperty = goProperty;
	}

	public String getIfPax() {
		return ifPax;
	}

	public void setIfPax(String ifPax) {
		this.ifPax = ifPax == null ? null : ifPax.trim();
	}

	public String getIfStay() {
		return ifStay;
	}

	public void setIfStay(String ifStay) {
		this.ifStay = ifStay == null ? null : ifStay.trim();
	}

	public String getIfOver() {
		return ifOver;
	}

	public void setIfOver(String ifOver) {
		this.ifOver = ifOver == null ? null : ifOver.trim();
	}

	public String getIfTransit() {
		return ifTransit;
	}

	public void setIfTransit(String ifTransit) {
		this.ifTransit = ifTransit == null ? null : ifTransit.trim();
	}
}