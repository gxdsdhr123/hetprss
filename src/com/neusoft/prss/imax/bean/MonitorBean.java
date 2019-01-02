package com.neusoft.prss.imax.bean;

import java.util.List;
import java.util.Map;

public class MonitorBean {

	// 条状图
	private List<Integer> barList;
	
	// 表格
	private List<Map<String, Object>> tableList;
	
	// 标准块
	private Map<String, Double> bzObj;

	public List<Integer> getBarList() {
		return barList;
	}

	public void setBarList(List<Integer> barList) {
		this.barList = barList;
	}

	public List<Map<String, Object>> getTableList() {
		return tableList;
	}

	public void setTableList(List<Map<String, Object>> tableList) {
		this.tableList = tableList;
	}

	public Map<String, Double> getBzObj() {
		return bzObj;
	}

	public void setBzObj(Map<String, Double> bzObj) {
		this.bzObj = bzObj;
	}
	
	
}
