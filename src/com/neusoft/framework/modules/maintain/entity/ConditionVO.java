package com.neusoft.framework.modules.maintain.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class ConditionVO  implements Serializable {
	private static final long serialVersionUID = -1683507793350016493L;
	private String codeId;
	private String condType;
	private String condName;
	private String condCode;
	private int condRow;
	private int condCol;
	private String condDefault;
	private Map<String,String> dataMap;
	private List<JSONObject> dataList;
	private String condStyle;
	private String condSQL;
	
	public String getCondSQL() {
		return condSQL;
	}
	public void setCondSQL(String condSQL) {
		this.condSQL = condSQL;
	}
	public List<JSONObject> getDataList() {
		return dataList;
	}
	public void setDataList(List<JSONObject> dataList) {
		this.dataList = dataList;
	}
	public String getCondStyle() {
		return condStyle;
	}
	public void setCondStyle(String condStyle) {
		this.condStyle = condStyle;
	}
	public String getCodeId() {
		return codeId;
	}
	public void setCodeId(String codeId) {
		this.codeId = codeId;
	}
	public String getCondCode() {
		return condCode;
	}
	public void setCondCode(String condCode) {
		this.condCode = condCode;
	}
	public int getCondRow() {
		return condRow;
	}
	public void setCondRow(int condRow) {
		this.condRow = condRow;
	}
	public int getCondCol() {
		return condCol;
	}
	public void setCondCol(int condCol) {
		this.condCol = condCol;
	}
	public String getCondDefault() {
		return condDefault;
	}
	public void setCondDefault(String condDefault) {
		this.condDefault = condDefault;
	}
	public String getCondType() {
		return condType;
	}
	public void setCondType(String condType) {
		this.condType = condType;
	}
	public String getCondName() {
		return condName;
	}
	public void setCondName(String condName) {
		this.condName = condName;
	}
	public Map<String, String> getDataMap() {
		return dataMap;
	}
	public void setDataMap(Map<String, String> dataMap) {
		this.dataMap = dataMap;
	}
}
