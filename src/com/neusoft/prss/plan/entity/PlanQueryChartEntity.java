package com.neusoft.prss.plan.entity;

import java.util.Map;

public class PlanQueryChartEntity {
	
	private Map<String, Integer[]> planChartData;
	
	private Map<String,String[]> planChartX;
	
	public Map<String, Integer[]> getPlanChartData() {
		return planChartData;
	}
	public void setPlanChartData(Map<String, Integer[]> planChartData) {
		this.planChartData = planChartData;
	}
	public Map<String, String[]> getPlanChartX() {
		return planChartX;
	}
	public void setPlanChartX(Map<String, String[]> planChartX) {
		this.planChartX = planChartX;
	}
	

}
