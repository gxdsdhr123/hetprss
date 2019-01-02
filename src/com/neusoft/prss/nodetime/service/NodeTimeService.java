package com.neusoft.prss.nodetime.service;

public interface NodeTimeService {

	/**
	 * 根据任务ID,流程模板节点ID获取该节点的标准时间
	 * 
	 * @author shuyx
	 * @param taskId
	 *            任务ID
	 * @param nodeId
	 *            流程模板ID
	 * @return 标准时间
	 * @update 2017年10月30日11:10:40
	 */
	String getNodeTime(String taskId, String nodeId);

}
