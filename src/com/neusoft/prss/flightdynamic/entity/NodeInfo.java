package com.neusoft.prss.flightdynamic.entity;

import java.util.Date;

import com.neusoft.framework.common.utils.DateUtils;

/**
 * 节点详情 —— 保障图
 * 
 * @author xuhw
 *
 */
public class NodeInfo implements Comparable<NodeInfo>{

	private String fltid;
	private Integer id;
	private String eventTime;
	private String typeComment;
	private String personCode;
	private String personName;
	private String taskId;

	public String getFltid() {
		return fltid;
	}

	public void setFltid(String fltid) {
		this.fltid = fltid;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEventTime() {
		return DateUtils.formatToFltDate(eventTime);
	}

	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}

	public String getTypeComment() {
		return typeComment;
	}

	public void setTypeComment(String typeComment) {
		this.typeComment = typeComment;
	}

	public String getPersonCode() {
		return personCode;
	}

	public void setPersonCode(String personCode) {
		this.personCode = personCode;
	}

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	@Override
	public int compareTo(NodeInfo target) {
		Date thisEventTime = DateUtils.parseFromFltDate(this.getEventTime());
		Date targetEventTime = DateUtils.parseFromFltDate(target.getEventTime());
		if(targetEventTime == null){
			return 1;
		}
		if(thisEventTime == null){
			return -1;
		}
		return Long.valueOf(thisEventTime.getTime() - targetEventTime.getTime()).intValue();
	}

}
