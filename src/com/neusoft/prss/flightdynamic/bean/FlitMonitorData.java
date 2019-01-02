package com.neusoft.prss.flightdynamic.bean;

import java.util.HashMap;
import java.util.Map;
/**
 * 保障图节点数据
 * @author xuhw
 *
 */
public class FlitMonitorData {

	// 主流程数据
	private Map<String, MainNode> mainDatas;
	// 分支流程数据
	private Map<String, OtherNode> otherDatas;
	// 时间节点数据
	private Map<String, TimeNode> timeDatas;

	public FlitMonitorData(){
		this.mainDatas = new HashMap<String, MainNode>();
		this.otherDatas = new HashMap<String, OtherNode>();
		this.timeDatas = new HashMap<String, TimeNode>();
	}
	
	public Map<String, MainNode> getMainDatas() {
		return mainDatas;
	}

	public void setMainDatas(Map<String, MainNode> mainDatas) {
		this.mainDatas = mainDatas;
	}

	public Map<String, OtherNode> getOtherDatas() {
		return otherDatas;
	}

	public void setOtherDatas(Map<String, OtherNode> otherDatas) {
		this.otherDatas = otherDatas;
	}

	public Map<String, TimeNode> getTimeDatas() {
		return timeDatas;
	}

	public void setTimeDatas(Map<String, TimeNode> timeDatas) {
		this.timeDatas = timeDatas;
	}

	public static class MainNode {
		private String id;
		private String nodeTime;
		private Integer status;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getNodeTime() {
			return nodeTime;
		}

		public void setNodeTime(String nodeTime) {
			this.nodeTime = nodeTime;
		}

		public Integer getStatus() {
			return status;
		}

		public void setStatus(Integer status) {
			this.status = status;
		}

		public MainNode(){
			super();
		}
		
		public MainNode(String id, String nodeTime, Integer status) {
			super();
			this.id = id;
			this.nodeTime = nodeTime;
			this.status = status;
		}

	}

	public static class OtherNode {
		private String id;
		private String nodeTime;
		private Integer status;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getNodeTime() {
			return nodeTime;
		}

		public void setNodeTime(String nodeTime) {
			this.nodeTime = nodeTime;
		}

		public Integer getStatus() {
			return status;
		}

		public void setStatus(Integer status) {
			this.status = status;
		}
		
		public OtherNode(){}
		
		public OtherNode(String id, String nodeTime, Integer status) {
			super();
			this.id = id;
			this.nodeTime = nodeTime;
			this.status = status;
		}

	}

	public static class TimeNode {
		private String id;
		private String nodeText;
		private Integer aTime;
		private Integer eTime;
		private Integer status;

		public TimeNode(){}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getNodeText() {
			return nodeText;
		}

		public void setNodeText(String nodeText) {
			this.nodeText = nodeText;
		}

		public Integer getaTime() {
			return aTime;
		}

		public void setaTime(Integer aTime) {
			this.aTime = aTime;
		}

		public Integer geteTime() {
			return eTime;
		}

		public void seteTime(Integer eTime) {
			this.eTime = eTime;
		}

		public Integer getStatus() {
			return status;
		}

		public void setStatus(Integer status) {
			this.status = status;
		}

		public TimeNode(String id, String nodeText, Integer aTime,
				Integer eTime, Integer status) {
			super();
			this.id = id;
			this.nodeText = nodeText;
			this.aTime = aTime;
			this.eTime = eTime;
			this.status = status;
		}


	}
}
