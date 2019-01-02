package com.neusoft.prss.common.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class JobKind implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5429766289957126118L;
	private String kindCode;//保障类型编码
	private String kindName;//保障类型名称
	List<JobType> typeList = new ArrayList<JobType>();

	public String getKindCode() {
		return kindCode;
	}

	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}

	public String getKindName() {
		return kindName;
	}

	public void setKindName(String kindName) {
		this.kindName = kindName;
	}

	public List<JobType> getTypeList() {
		return typeList;
	}

	public void setTypeList(List<JobType> typeList) {
		this.typeList = typeList;
	}
}
