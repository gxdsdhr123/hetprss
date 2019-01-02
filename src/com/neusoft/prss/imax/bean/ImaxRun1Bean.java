package com.neusoft.prss.imax.bean;

import java.util.List;
import java.util.Map;

public class ImaxRun1Bean {
	
	/**
	 * 航班运行情况图
	 */
	private Map<String, Map<String, Integer[]>> flightChart;
	/**
	 * 航班运行文字
	 */
	private Map<String, Map<String, Object>> flightText;
	/**
	 * 航班运行表格
	 */
	private List<Map<String, Object>> flightTable;
	
	public Map<String, Map<String, Integer[]>> getFlightChart() {
		return flightChart;
	}
	public void setFlightChart(Map<String, Map<String, Integer[]>> flightChart) {
		this.flightChart = flightChart;
	}
	public Map<String, Map<String, Object>> getFlightText() {
		return flightText;
	}
	public void setFlightText(Map<String, Map<String, Object>> flightText) {
		this.flightText = flightText;
	}
	public List<Map<String, Object>> getFlightTable() {
		return flightTable;
	}
	public void setFlightTable(List<Map<String, Object>> flightTable) {
		this.flightTable = flightTable;
	}
	
	
}
