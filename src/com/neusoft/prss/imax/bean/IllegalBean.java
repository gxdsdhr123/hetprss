package com.neusoft.prss.imax.bean;

import java.util.List;
import java.util.Map;

public class IllegalBean {

	// 折线图
	private Map<String, Integer[]> lineChart;
	
	// 折线图下方文字
	private Map<String, Object> lineText;
	
	// 柱状图
	private Map<String, Map<String, Integer>> barChart;
	
	// 人员列表
	private List<Map<String, Object>> personList;
	
	// 车辆列表
	private List<Map<String, Object>> carList;

	public Map<String, Integer[]> getLineChart() {
		return lineChart;
	}

	public Map<String, Object> getLineText() {
		return lineText;
	}

	public void setLineText(Map<String, Object> lineText) {
		this.lineText = lineText;
	}

	public void setLineChart(Map<String, Integer[]> lineChart) {
		this.lineChart = lineChart;
	}

	public Map<String, Map<String, Integer>> getBarChart() {
		return barChart;
	}

	public void setBarChart(Map<String, Map<String, Integer>> barChart) {
		this.barChart = barChart;
	}

	public List<Map<String, Object>> getPersonList() {
		return personList;
	}

	public void setPersonList(List<Map<String, Object>> personList) {
		this.personList = personList;
	}

	public List<Map<String, Object>> getCarList() {
		return carList;
	}

	public void setCarList(List<Map<String, Object>> carList) {
		this.carList = carList;
	}
	
}
