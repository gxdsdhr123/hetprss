package com.neusoft.prss.flightdynamic.entity;

import java.util.List;

public class PersonEvent {

	// 流程事件
	private List<TimeData> flow;
	// 消息事件
	private List<TimeData> message;
	// 不正常事件
	private List<TimeData> abnormal;
	
	
	
	public List<TimeData> getFlow() {
		return flow;
	}



	public void setFlow(List<TimeData> flow) {
		this.flow = flow;
	}



	public List<TimeData> getMessage() {
		return message;
	}



	public void setMessage(List<TimeData> message) {
		this.message = message;
	}



	public List<TimeData> getAbnormal() {
		return abnormal;
	}



	public void setAbnormal(List<TimeData> abnormal) {
		this.abnormal = abnormal;
	}






	public static class TimeData{
		// 时间
		private String time;
		// 内容
		private String eventDes;
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
		public String getEventDes() {
			return eventDes;
		}
		public void setEventDes(String eventDes) {
			this.eventDes = eventDes;
		}
		
		public TimeData(){}
		
		public TimeData(String time, String eventDes) {
			this.time = time;
			this.eventDes = eventDes;
		}
		
		
	}
}
