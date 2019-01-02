package com.neusoft.prss.common.entity;

import java.io.Serializable;

public class JobType implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3811167674802455469L;
	private String typeCode;//作业类型编码
	private String typeName;//作业类型名称

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
}
