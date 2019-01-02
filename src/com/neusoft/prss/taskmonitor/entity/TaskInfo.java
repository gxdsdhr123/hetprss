package com.neusoft.prss.taskmonitor.entity;

public class TaskInfo {
	// 任务ID
	private String id;
	// fltid
	private String fltid;
	// 作业类型
	private String jobType;
	// 任务开始时间
	private String startTime;
	// 任务结束时间
	private String endTime;
	// 任务操作人
	private String operator;
	// 进港航班号
	private String inFltNum;
	// 出港航班号
	private String outFltNum;
	// ETA
	private String eta;
	// ETD
	private String etd;
	// 机型
	private String acttypeCode;
	// 机位
	private String actstandCode;
	// 任务类型
	private String taskType;
	// 任务状态
	private String nodeState;
	// 节点ID
	private String nodeId;
	// 消息模板ID
	private  String proId;
	// 流程模板ID
	private String orderId;
	// 任务状态
	private String jobState;
	// 排序
	private Long sort;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInFltNum() {
		return inFltNum;
	}

	public void setInFltNum(String inFltNum) {
		this.inFltNum = inFltNum;
	}

	public String getOutFltNum() {
		return outFltNum;
	}

	public void setOutFltNum(String outFltNum) {
		this.outFltNum = outFltNum;
	}

	public String getEta() {
		return eta;
	}

	public void setEta(String eta) {
		this.eta = eta;
	}

	public String getEtd() {
		return etd;
	}

	public void setEtd(String etd) {
		this.etd = etd;
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

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getNodeState() {
		return nodeState;
	}

	public void setNodeState(String nodeState) {
		this.nodeState = nodeState;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public Long getSort() {
		return sort;
	}

	public void setSort(Long sort) {
		this.sort = sort;
	}

	public String getProId() {
		return proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getJobState() {
		return jobState;
	}

	public void setJobState(String jobState) {
		this.jobState = jobState;
	}

	public String getFltid() {
		return fltid;
	}

	public void setFltid(String fltid) {
		this.fltid = fltid;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}
	
	
}
