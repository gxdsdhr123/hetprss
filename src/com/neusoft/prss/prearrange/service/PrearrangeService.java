package com.neusoft.prss.prearrange.service;

public interface PrearrangeService {
	 
	/**
     * @title 任务分发
     * @param tasks
     * @param emp
     * @return
     */
	String doDispatch(String beginTm, String endTm, String jobKind);
	
	/**
	 * @Title 启动实例
	 */
	String startFlow(String reskind);
}
