package com.neusoft.prss.workflow.service;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * 工作流按钮事件处理
 * 
 * @author baochl
 *
 */
public interface WFBtnEventService {
	/**
	 * 启动实例
	 * 
	 * @param params
	 * @return
	 */
	public String startInstance(String jobTaskId, String procId, String operator, String actor,
			Map<String, String> args);

	/**
	 * 下一步
	 * @param params
	 * @return
	 */
	public String next(String taskId,String operator,Map<String, String> args);
	/**
	 * 完成
	 * @param params
	 * @return
	 */
	public String complete(String taskId,String operator,Map<String, String> args);
	/**
	 * 转办
	 * @param params
	 * @return
	 */
	public String transferMajor(String taskId,String operator,String actor,Map<String, String> args);
	/**
	 * 退回
	 * @param params
	 * @return
	 */
	public String back(String taskId, String operator,Map<String, String> args);
	/**
	 * 终止
	 * @param params
	 * @return
	 */
	public String termination(String taskId,String operator,Map<String, String> args);
	/**
	 * 根据按钮id获取按钮参数
	 * @param btnId
	 * @return
	 */
	public JSONObject getBtnVariables(String btnId);
}
