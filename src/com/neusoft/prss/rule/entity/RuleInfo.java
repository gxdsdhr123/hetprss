package com.neusoft.prss.rule.entity;

import java.io.Serializable;

/**
 * 规则详情
 * @author XC
 */
public class RuleInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String id;
	
	/**
	 * 规则中参数的ID
	 */
	private String colids;
	/**
	 * 规则
	 */
	private String rule;
	/**
	 * 是否手动分配
	 */
	private Integer ifManual;
	/**
	 * 状态 是否生效
	 */
	private Integer ifValid;
	private String createTime;
	private String createUser;
	private String updateTime;
	private String updateUser;
	/**
	 * 规则名称
	 */
	private String name;
	/**
	 * 规则描述
	 */
	private String description;
	
	/**
	 * 规则展示信息
	 */
	private String text;
	
	/**
	 * drools可解析字符串
	 */
	private String drlStr;
	
	/**
	 * 扩展规则
	 */
	private String ruleExt;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getColids() {
		return colids;
	}
	public void setColids(String colids) {
		this.colids = colids;
	}
	public String getRule() {
		return rule;
	}
	public void setRule(String rule) {
		this.rule = rule;
	}
	public Integer getIfManual() {
		return ifManual;
	}
	public void setIfManual(Integer ifManual) {
		this.ifManual = ifManual;
	}
	public Integer getIfValid() {
		return ifValid;
	}
	public void setIfValid(Integer ifValid) {
		this.ifValid = ifValid;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getDrlStr() {
		return drlStr;
	}
	public void setDrlStr(String drlStr) {
		this.drlStr = drlStr;
	}
	public String getRuleExt() {
		return ruleExt;
	}
	public void setRuleExt(String ruleExt) {
		this.ruleExt = ruleExt;
	}
}
