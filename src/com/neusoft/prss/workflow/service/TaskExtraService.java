package com.neusoft.prss.workflow.service;

/**
 * 任务释放
 * @author xuhw
 *
 */
public interface TaskExtraService {
	
	/**
	 * 释放任务中的操作人员,相当于将任务从已分配变成待分配
	 * @param taskId 任务id
	 * @param autoDispatch 是否自动分配
	 * @return
	 */
	public int releaseTaskOperator(String taskId,boolean autoDispatch);
	
	public int deleteTaskById(String taskId);
}
