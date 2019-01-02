package com.neusoft.prss.imax.bean;

import java.util.List;
import java.util.Map;

public class ImaxRun2Bean {

	// 航班运行情况占比左侧文字
	private Map<String, Object> runText;
	// 航班运行情况占比图
	private List<Map<String, Object>> runTable;
	// 航空公司占比
	private List<Map<String, Object>> runChart;
	// 航班延误图形
	private Map<String, Object> ywChart;
	// 航班延误文字
	private Map<String, Object> ywText;
	
	public Map<String, Object> getRunText() {
		return runText;
	}
	public void setRunText(Map<String, Object> runText) {
		this.runText = runText;
	}
	public List<Map<String, Object>> getRunTable() {
		return runTable;
	}
	public void setRunTable(List<Map<String, Object>> runTable) {
		this.runTable = runTable;
	}
	public List<Map<String, Object>> getRunChart() {
		return runChart;
	}
	public void setRunChart(List<Map<String, Object>> runChart) {
		this.runChart = runChart;
	}
	public Map<String, Object> getYwChart() {
		return ywChart;
	}
	public void setYwChart(Map<String, Object> ywChart) {
		this.ywChart = ywChart;
	}
	public Map<String, Object> getYwText() {
		return ywText;
	}
	public void setYwText(Map<String, Object> ywText) {
		this.ywText = ywText;
	}
	
	
	
}
