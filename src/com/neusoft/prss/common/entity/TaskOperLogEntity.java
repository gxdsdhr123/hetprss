package com.neusoft.prss.common.entity;

import java.io.Serializable;

public class TaskOperLogEntity implements Serializable {
	
	private static final long serialVersionUID = 4412417952102862957L;
	private String id;// 日志Id
	private String taskId;// jm_task table id
	private String orderId;// wf_order table id
	private int operType;// 1、创建 2、分配 3、接收 4、转交 5、回退 6、释放 7、恢复 8、终止 9、删除
	private String workerId;// 作业人
	private int termType;// 操作终端 1、PC端 2、手持端
	private String remark;// 备注
	private String userId;// 操作人

	public TaskOperLogEntity() {

	}

	public TaskOperLogEntity(String userId,String taskId, String orderId, int operType, String workerId, int termType) {
		this.userId = userId;
		this.taskId = taskId;
		this.orderId = orderId;
		this.operType = operType;
		this.workerId = workerId;
		this.termType = termType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public int getOperType() {
		return operType;
	}

	public void setOperType(int operType) {
		this.operType = operType;
	}

	public String getWorkerId() {
		return workerId;
	}

	public void setWorkerId(String workerId) {
		this.workerId = workerId;
	}

	public int getTermType() {
		return termType;
	}

	public void setTermType(int termType) {
		this.termType = termType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
