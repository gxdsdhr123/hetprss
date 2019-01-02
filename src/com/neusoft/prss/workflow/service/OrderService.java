package com.neusoft.prss.workflow.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.neusoft.prss.workflow.access.Page;
import com.neusoft.prss.workflow.access.QueryFilter;
import com.neusoft.prss.workflow.entity.HistoryOrder;
import com.neusoft.prss.workflow.entity.HistoryTask;
import com.neusoft.prss.workflow.entity.Order;

public interface OrderService {
	/**
	 * 获取流程实例
	 */
	public Page<HistoryOrder> getHistoryOrders(Page<HistoryOrder> page, QueryFilter filter);

	/**
	 * 级联删除指定流程实例的所有数据：
	 * 1.wf_order,wf_hist_order
	 * 2.wf_task,wf_hist_task
	 * 3.wf_task_actor,wf_hist_task_actor
	 * 4.wf_cc_order
	 * @param id
	 */
	void cascadeRemove(String id);
	/**
	 * 
	 * 启动流程实例
	 * @param jobTaskId 作业任务ID
	 * @param procId 模板id
	 * @param operator 操作人
	 * @param actor	任务处理人
	 * @return
	 */
	Order startInstance(String jobTaskId,String procId, String operator, String actor);
	/**
	 * 根据id获取流程实例
	 * @param orderId
	 * @return
	 */
	public HistoryOrder getHistOrder(String orderId);
	/**
	 * 根据实例id获取节点
	 * @param filter
	 * @return
	 */
	public List<HistoryTask> getHistoryTasks(QueryFilter filter);
	/**
	 * 获取流程实例查看json
	 * @param filter
	 * @return
	 */
	public JSONObject getOrderJson(String processId,String orderId);
}
