package com.neusoft.prss.common.entity;

import java.io.Serializable;

public class OperationLogEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 735569303092885726L;
	private String fltid;// 航班ID
	private String batchNo;// 批次号
	private int operType;// 操作类型：-1、其他，1、新增 ，2、修改， 3、删除，4、导入
	private String module;// 模块名称
	private String function;// 功能点名称
	private String propertyName;// 属性名称
	private String originalValue;// 原始值
	private String newValue;// 新值
	private String operUser;// 操作人
	private String operTm;// 操作时间
	private String remark;// 备注

	public OperationLogEntity() {

	}
	/**
	 * @param operType 操作类型：-1、其他，1、新增 ，2、修改， 3、删除，4、导入
	 * @param module 模块名称
	 * @param function 功能点名称
	 * @param operUser 操作人
	 * @param remark 备注
	 */
	public OperationLogEntity(int operType, String module, String function,
			String operUser, String remark) {
		this.operType = operType;
		this.module = module;
		this.function = function;
		this.operUser = operUser;
		this.remark = remark;
	}
	/**
	 * 
	 * @param fltid 航班ID
	 * @param operType 操作类型：-1、其他，1、新增 ，2、修改， 3、删除，4、导入
	 * @param module 模块名称
	 * @param function 功能点名称
	 * @param propertyName 属性名称
	 * @param originalValue 原始值
	 * @param newValue 新值
	 * @param operUser 操作人
	 */
	public OperationLogEntity(String fltid, int operType, String module, String function, String propertyName,
			String originalValue, String newValue, String operUser) {
		this.fltid = fltid;
		this.operType = operType;
		this.module = module;
		this.function = function;
		this.originalValue = originalValue;
		this.newValue = newValue;
		this.operUser = operUser;
		this.propertyName = propertyName;
	}
	/**
	 * 
	 * @param fltid 航班ID
	 * @param operType 操作类型：-1、其他，1、新增 ，2、修改， 3、删除，4、导入
	 * @param module 模块名称
	 * @param function 功能点名称
	 * @param propertyName 属性名称
	 * @param originalValue 原始值
	 * @param newValue 新值
	 * @param operUser 操作人
	 * @param remark 备注
	 */
	public OperationLogEntity(String fltid, int operType, String module, String function, String propertyName,
			String originalValue, String newValue, String operUser, String remark) {
		this.fltid = fltid;
		this.operType = operType;
		this.module = module;
		this.function = function;
		this.originalValue = originalValue;
		this.newValue = newValue;
		this.operUser = operUser;
		this.remark = remark;
		this.propertyName = propertyName;
	}

	public int getOperType() {
		return operType;
	}

	public void setOperType(int operType) {
		this.operType = operType;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getOriginalValue() {
		return originalValue;
	}

	public void setOriginalValue(String originalValue) {
		this.originalValue = originalValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public String getOperUser() {
		return operUser;
	}

	public void setOperUser(String operUser) {
		this.operUser = operUser;
	}

	public String getOperTm() {
		return operTm;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getFltid() {
		return fltid;
	}

	public void setFltid(String fltid) {
		this.fltid = fltid;
	}
}
