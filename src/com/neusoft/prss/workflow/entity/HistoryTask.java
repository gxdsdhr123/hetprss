/* Copyright 2013-2015 www.snakerflow.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.neusoft.prss.workflow.entity;

import java.io.Serializable;

/**
 * 历史任务实体类
 * 
 * @author yuqs
 * @since 1.0
 */
public class HistoryTask implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6814632180050362450L;
	/**
	 * 主键ID
	 */
	private String id;
	/**
	 * 流程实例ID
	 */
	private String orderId;
	/**
	 * 任务名称
	 */
	private String taskName;
	/**
	 * 任务显示名称
	 */
	private String displayName;
	/**
	 * 参与方式（0：普通任务；1：参与者fork任务[即：如果10个参与者，需要每个人都要完成，才继续流转]）
	 */
	private Integer performType;
	/**
	 * 任务类型
	 */
	private Integer taskType;
	/**
	 * 任务状态（0：结束；1：活动）
	 */
	private Integer taskState;
	/**
	 * 任务处理者ID
	 */
	private String operator;
	/**
	 * 任务处理者ID
	 */
	private String operatorName;
	/**
	 * 任务创建时间
	 */
	private String createTime;
	/**
	 * 任务完成时间
	 */
	private String finishTime;
	/**
	 * 期望任务完成时间
	 */
	private String expireTime;
	/**
	 * 任务关联的表单url
	 */
	private String actionUrl;
	/**
	 * 任务参与者列表
	 */
	private String[] actorIds;
	/**
	 * 父任务Id
	 */
	private String parentTaskId;
	/**
	 * 历史变量
	 */
	// baochl_20171027
	private String nodeId;
	private String jobKind;
	private String jobType;
	private String aftMsg1;
	private String aftMsg2;
	private String notifyMsg;
	private String alarmMsgLv1;
	private String alarmMsgLv2;
	private String alarmMsgLv3;
	private String alarmMsgLv4;
	private String alarmMsgLv5;
	private String notifyTm;
	private String alarmTm1;
	private String alarmTm2;
	private String alarmTm3;
	private String alarmTm4;
	private String alarmTm5;
	private String icon;

	public HistoryTask() {

	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Integer getTaskType() {
		return taskType;
	}

	public void setTaskType(Integer taskType) {
		this.taskType = taskType;
	}

	public Integer getTaskState() {
		return taskState;
	}

	public void setTaskState(Integer taskState) {
		this.taskState = taskState;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	public String getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}

	public String getActionUrl() {
		return actionUrl;
	}

	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Integer getPerformType() {
		return performType;
	}

	public void setPerformType(Integer performType) {
		this.performType = performType;
	}

	public String[] getActorIds() {
		return actorIds;
	}

	public void setActorIds(String[] actorIds) {
		this.actorIds = actorIds;
	}

	public String getParentTaskId() {
		return parentTaskId;
	}

	public void setParentTaskId(String parentTaskId) {
		this.parentTaskId = parentTaskId;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("HistoryTask(id=").append(this.id);
		sb.append(",orderId=").append(this.orderId);
		sb.append(",taskName=").append(this.taskName);
		sb.append(",displayName").append(this.displayName);
		sb.append(",taskType=").append(this.taskType);
		sb.append(",createTime").append(this.createTime);
		sb.append(",performType=").append(this.performType).append(")");
		return sb.toString();
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getJobKind() {
		return jobKind;
	}

	public void setJobKind(String jobKind) {
		this.jobKind = jobKind;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getAftMsg1() {
		return aftMsg1;
	}

	public void setAftMsg1(String aftMsg1) {
		this.aftMsg1 = aftMsg1;
	}

	public String getAftMsg2() {
		return aftMsg2;
	}

	public void setAftMsg2(String aftMsg2) {
		this.aftMsg2 = aftMsg2;
	}

	public String getNotifyMsg() {
		return notifyMsg;
	}

	public void setNotifyMsg(String notifyMsg) {
		this.notifyMsg = notifyMsg;
	}

	public String getAlarmMsgLv1() {
		return alarmMsgLv1;
	}

	public void setAlarmMsgLv1(String alarmMsgLv1) {
		this.alarmMsgLv1 = alarmMsgLv1;
	}

	public String getAlarmMsgLv2() {
		return alarmMsgLv2;
	}

	public void setAlarmMsgLv2(String alarmMsgLv2) {
		this.alarmMsgLv2 = alarmMsgLv2;
	}

	public String getAlarmMsgLv3() {
		return alarmMsgLv3;
	}

	public void setAlarmMsgLv3(String alarmMsgLv3) {
		this.alarmMsgLv3 = alarmMsgLv3;
	}

	public String getAlarmMsgLv4() {
		return alarmMsgLv4;
	}

	public void setAlarmMsgLv4(String alarmMsgLv4) {
		this.alarmMsgLv4 = alarmMsgLv4;
	}

	public String getAlarmMsgLv5() {
		return alarmMsgLv5;
	}

	public void setAlarmMsgLv5(String alarmMsgLv5) {
		this.alarmMsgLv5 = alarmMsgLv5;
	}

	public String getNotifyTm() {
		return notifyTm;
	}

	public void setNotifyTm(String notifyTm) {
		this.notifyTm = notifyTm;
	}

	public String getAlarmTm1() {
		return alarmTm1;
	}

	public void setAlarmTm1(String alarmTm1) {
		this.alarmTm1 = alarmTm1;
	}

	public String getAlarmTm2() {
		return alarmTm2;
	}

	public void setAlarmTm2(String alarmTm2) {
		this.alarmTm2 = alarmTm2;
	}

	public String getAlarmTm3() {
		return alarmTm3;
	}

	public void setAlarmTm3(String alarmTm3) {
		this.alarmTm3 = alarmTm3;
	}

	public String getAlarmTm4() {
		return alarmTm4;
	}

	public void setAlarmTm4(String alarmTm4) {
		this.alarmTm4 = alarmTm4;
	}

	public String getAlarmTm5() {
		return alarmTm5;
	}

	public void setAlarmTm5(String alarmTm5) {
		this.alarmTm5 = alarmTm5;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
}
