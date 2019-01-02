package com.neusoft.prss.imax.bean;

import java.util.List;
import java.util.Map;

public class ImaxResourceBean {

	/**
	 * 人员保障图
	 */
	private Map<String, Integer[]> monitorChart;
	/**
	 * 人员保障文字
	 */
	private Map<String, Object> monitorText;
	/**
	 * 人员占用列表
	 */
	private List<Map<String, Object>> occupyTable;
	/**
	 * 人员占用图
	 */
	private Map<String, Object> occupyChart;
	
	
	public Map<String, Integer[]> getMonitorChart() {
		return monitorChart;
	}
	public void setMonitorChart(Map<String, Integer[]> monitorChart) {
		this.monitorChart = monitorChart;
	}
	public Map<String, Object> getMonitorText() {
		return monitorText;
	}
	public void setMonitorText(Map<String, Object> monitorText) {
		this.monitorText = monitorText;
	}
	public List<Map<String, Object>> getOccupyTable() {
		return occupyTable;
	}
	public void setOccupyTable(List<Map<String, Object>> occupyTable) {
		this.occupyTable = occupyTable;
	}
	public Map<String, Object> getOccupyChart() {
		return occupyChart;
	}
	public void setOccupyChart(Map<String, Object> occupyChart) {
		this.occupyChart = occupyChart;
	}
	
}
