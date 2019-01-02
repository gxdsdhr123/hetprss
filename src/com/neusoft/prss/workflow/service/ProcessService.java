package com.neusoft.prss.workflow.service;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.prss.workflow.access.QueryFilter;
import com.neusoft.prss.workflow.entity.Process;

public interface ProcessService {
	/**
	 * 获取流程模板
	 * @param filter
	 * @return
	 */
	JSONArray getProcess(QueryFilter filter);
	/**
	 * 根据id获取模板
	 * @param id
	 * @return
	 */
	JSONObject getProcessById(String id);
	/**
	 * 根据模板ID获取模板节点
	 * @param processId
	 * @return
	 */
	JSONArray getProcessNodes(String processId);
	/**
	 * 部署流程模板
	 * @param xml
	 * @param creator 创建人
	 * @return
	 * @throws Exception
	 */
	String deploy(String xml,String creator) throws Exception;
	
	/**
	 * 重新部署流程模板
	 * @param id
	 * @param xml
	 * @throws Exception
	 */
	void redeploy(String id, String xml) throws Exception;
	/**
	 * 谨慎使用.数据恢复非常痛苦，你懂得~~
	 * 级联删除指定流程定义的所有数据：
	 * 1.wf_process
	 * 2.wf_order,wf_hist_order
	 * 3.wf_task,wf_hist_task
	 * 4.wf_task_actor,wf_hist_task_actor
	 * 5.wf_cc_order
	 * @param id
	 */
	void cascadeRemove(String id);
	/**
	 * 根据节点模板id获取流程模板
	 * @param nodeId
	 * @return
	 */
	List<Process> getProcessByNode(String nodeId);
	/**
	 * 根据节点模板id获取流程模板
	 * @param nodeId
	 * @param jobKind 保障类型
	 * @param jobType 作业类型
	 * @return
	 */
	List<Process> getProcessByNode(String nodeId,String jobKind,String jobType);
	/**
	 * 校验是否有模板使用该节点
	 * @param nodeId
	 * @return
	 */
	boolean isContainNode(String nodeId,String jobKind,String jobType);
	/**
	 * 修改流程模板节点
	 * @param nodeId
	 * @return
	 * @throws Exception 
	 */
	void redeployByNode(String nodeId,String jobKind,String jobType) throws Exception;
	
	void syncNodeToProcess();
}
