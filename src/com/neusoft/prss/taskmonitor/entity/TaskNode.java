package com.neusoft.prss.taskmonitor.entity;

import java.util.List;

public class TaskNode {

	// 任务ID
	private String id;
	// 任务名称
	private String name;
	// 航班号
	private String flightNumber;
	// 航班号（二字码）
	private String flightNumber2;
	// 机型
	private String acttypeCode;
	// 机位
	private String actstandCode;
	// 人员姓名
	private String personName;
	// 车辆编号
	private String carId;
	// 任务状态
	private String status;
	// 预计到位
	private String eArrive;
	//实际到位
	private String aArrive;
	//预计上客
	private String eBoarding;
	//实际上客
	private String aBoarding;
	//预计发车
	private String eStart;
	//实际发车
	private String aStart;
	//预计开始2
	private String eStart2;
	//实际开始2
	private String aStart2;
	//预计完成
	private String eEnd;
	//实际完成
	private String aEnd;
	// 当是1时展示拖出机位、拖入机位
	private Integer ifActstand;
	// 拖出机位
	private String fromActstand;
	// 拖入机位
	private String toActstand; 
	// 任务节点
	private List<Node> nodes;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFlightNumber() {
		return flightNumber;
	}
	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}
	public String getFlightNumber2() {
		return flightNumber2;
	}
	public void setFlightNumber2(String flightNumber2) {
		this.flightNumber2 = flightNumber2;
	}
	public String getActtypeCode() {
		return acttypeCode;
	}
	public void setActtypeCode(String acttypeCode) {
		this.acttypeCode = acttypeCode;
	}
	public String getActstandCode() {
		return actstandCode;
	}
	public void setActstandCode(String actstandCode) {
		this.actstandCode = actstandCode;
	}
	public String getCarId() {
		return carId;
	}
	public void setCarId(String carId) {
		this.carId = carId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String geteArrive() {
		return eArrive;
	}
	public void seteArrive(String eArrive) {
		this.eArrive = eArrive;
	}
	public String getaArrive() {
		return aArrive;
	}
	public void setaArrive(String aArrive) {
		this.aArrive = aArrive;
	}
	public String geteBoarding() {
		return eBoarding;
	}
	public void seteBoarding(String eBoarding) {
		this.eBoarding = eBoarding;
	}
	public String getaBoarding() {
		return aBoarding;
	}
	public void setaBoarding(String aBoarding) {
		this.aBoarding = aBoarding;
	}
	public String geteStart() {
		return eStart;
	}
	public void seteStart(String eStart) {
		this.eStart = eStart;
	}
	public String getaStart() {
		return aStart;
	}
	public void setaStart(String aStart) {
		this.aStart = aStart;
	}
	public String geteEnd() {
		return eEnd;
	}
	public void seteEnd(String eEnd) {
		this.eEnd = eEnd;
	}
	public String getaEnd() {
		return aEnd;
	}
	public void setaEnd(String aEnd) {
		this.aEnd = aEnd;
	}
	public Integer getIfActstand() {
		return ifActstand;
	}
	public void setIfActstand(Integer ifActstand) {
		this.ifActstand = ifActstand;
	}
	public String getFromActstand() {
		return fromActstand;
	}
	public void setFromActstand(String fromActstand) {
		this.fromActstand = fromActstand;
	}
	public String getToActstand() {
		return toActstand;
	}
	public void setToActstand(String toActstand) {
		this.toActstand = toActstand;
	}
	public String geteStart2() {
		return eStart2;
	}
	public void seteStart2(String eStart2) {
		this.eStart2 = eStart2;
	}
	public String getaStart2() {
		return aStart2;
	}
	public void setaStart2(String aStart2) {
		this.aStart2 = aStart2;
	}
	public List<Node> getNodes() {
		return nodes;
	}
	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}


	public static class Node{
		
		// 节点ID
		private Integer id;
		// 节点名
		private String name;
		// 计划时间
		private String eVal;
		// 实际时间
		private String aVal;
		
		
		public Integer getId() {
			return id;
		}
		public void setId(Integer id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String geteVal() {
			return eVal;
		}
		public void seteVal(String eVal) {
			this.eVal = eVal;
		}
		public String getaVal() {
			return aVal;
		}
		public void setaVal(String aVal) {
			this.aVal = aVal;
		}
		
		
		
	}
}
