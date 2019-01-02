package com.neusoft.prss.arrange.service;

import java.util.List;

public interface WorkerArrangeService {
	 
	/**
     * 任务分发
     * @param tasks
     * @param emp
     * @return
     */
	String doDispatch();
	
	/**
     * 手动选择司机
     * @param fltid 航班id
     * @param job_type 作业类型
     * @param startTm  预计任务开始时间
     * @param endTm   预计任务结束时间
     * @return
     */
	List<String> manualSelectDriver(String param);
}
