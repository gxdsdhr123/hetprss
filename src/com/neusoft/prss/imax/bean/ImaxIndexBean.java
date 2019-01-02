package com.neusoft.prss.imax.bean;

import java.util.List;
import java.util.Map;

public class ImaxIndexBean {

	/**
	 * 当前时间
	 */
	private String nowTime;
	/**
	 * 安全运行时间
	 */
	private Map<String, Object> safeTime;
	
	/**
	 * 值班领导
	 */
	private String leader;
	
	/**
	 * 车辆占用情况
	 */
	private Map<String, Map<String, Integer>> carNums;
	
	/**
	 * 人员占用
	 */
	private Map<String, Map<String, Integer>> personNums;
	
	/**
	 * 航班运行情况（图）
	 */
	private Map<String, Integer[]> flightNums;
	
	/**
	 * 航班运行情况
	 */
	private Map<String, Object> flightMap;
	
	/**
	 * 航班占比
	 */
	private List<Map<String, Object>> flightRate;
	
	/**
	 * 航班保障进度
	 */
	private Map<String, Object> monitorNums;
	
	/**
	 * 部门违规
	 */
	private List<Map<String, Object>> departmentIllegal;
	
	/**
	 * 人员违规
	 */
	private List<Map<String, Object>> personIllegal;

	public Map<String, Map<String, Integer>> getCarNums() {
		return carNums;
	}

	public void setCarNums(Map<String, Map<String, Integer>> carNums) {
		this.carNums = carNums;
	}

	public Map<String, Map<String, Integer>> getPersonNums() {
		return personNums;
	}

	public void setPersonNums(Map<String, Map<String, Integer>> personNums) {
		this.personNums = personNums;
	}

	public Map<String, Integer[]> getFlightNums() {
		return flightNums;
	}

	public void setFlightNums(Map<String, Integer[]> flightNums) {
		this.flightNums = flightNums;
	}

	public Map<String, Object> getFlightMap() {
		return flightMap;
	}

	public void setFlightMap(Map<String, Object> flightMap) {
		this.flightMap = flightMap;
	}

	public List<Map<String, Object>> getFlightRate() {
		return flightRate;
	}

	public void setFlightRate(List<Map<String, Object>> flightRate) {
		this.flightRate = flightRate;
	}

	public Map<String, Object> getMonitorNums() {
		return monitorNums;
	}

	public void setMonitorNums(Map<String, Object> monitorNums) {
		this.monitorNums = monitorNums;
	}

	public List<Map<String, Object>> getDepartmentIllegal() {
		return departmentIllegal;
	}

	public void setDepartmentIllegal(List<Map<String, Object>> departmentIllegal) {
		this.departmentIllegal = departmentIllegal;
	}

	public List<Map<String, Object>> getPersonIllegal() {
		return personIllegal;
	}

	public void setPersonIllegal(List<Map<String, Object>> personIllegal) {
		this.personIllegal = personIllegal;
	}

	public Map<String, Object> getSafeTime() {
		return safeTime;
	}

	public void setSafeTime(Map<String, Object> safeTime) {
		this.safeTime = safeTime;
	}

	public String getLeader() {
		return leader;
	}

	public void setLeader(String leader) {
		this.leader = leader;
	}

	public String getNowTime() {
		return nowTime;
	}

	public void setNowTime(String nowTime) {
		this.nowTime = nowTime;
	}
	
	
}
