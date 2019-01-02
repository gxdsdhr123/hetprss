package com.neusoft.prss.workflow.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
/**
 * 任务处理
 * @author baochl
 *
 */
public interface TaskService {
	/**
	 * 执行任务
	 * @param taskId 任务ID
	 * @param operator 操作人
	 * @return String succeed
	 */
	String executeTask(String taskId,String operator);
	/**
	 * 转办
	 * @param taskId 任务ID
	 * @param operator 操作人
	 * @param actors 任务接收人
	 * @return String succeed
	 */
	String transferMajor(String taskId,String operator,String actor);
	/**
	 * 完成指定任务（终止）
	 * 该方法仅仅结束活动任务，并不能驱动流程继续执行
	 * @param taskId
	 * @param operator
	 * @return String succeed
	 */
	String complete(String taskId,String operator);
	/**
	 * 获取待办、已办
	 * @param state 状态0、已办 1、待办
	 * @param operator 用户id
	 * @param pageSize 每页显示条数
	 * @param pageIndex 页码
	 * @return
	 */
	JSONArray getWorkItems(int state,String operator,int pageSize,int pageNo);
	/**
	 * 获取当前活动节点按钮集合
	 * @param taskId 活动节点ID
	 * @return
	 */
	JSONArray getButtonsByTask(String taskId);
	/**
	 * 驳回到上一步
	 * @param taskId
	 * @param operator
	 * @return String succeed
	 */
	String executeAndJumpTask(String taskId, String operator);
	/**
	 * 根据作业任务id获取当前流程实例节点
	 * 
	 * @param jobTaskId
	 *            作业任务id
	 * @return
	 */
	JSONObject getFlowTaskByJob(String jobTaskId);
	/**
	 * 更改保障状态
	 * @param nodeId
	 * @return
	 */
	void updateJobState(String jobTaskId,String state);
	/**
	 * 根据流程实例获取活动节点id
	 * @param orderId
	 */
	String getTaskIdByOrder(String orderId,String actor);
	/**
	 * 根据作业任务id获取航班信息
	 * @param jobTaskId 作业任务id
	 * @return
	 */
	JSONObject getFltByJob(String jobTaskId);
}
