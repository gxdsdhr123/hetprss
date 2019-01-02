package com.neusoft.prss.job.service;

import com.alibaba.fastjson.JSONObject;
import com.xxl.job.core.biz.model.ReturnT;

public interface JobManagerService {
	
	/**
	 *Discription:增加调度任务.
	 *@param json
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年10月29日 gaojingdan [变更描述]
	 */
	public int addJob(JSONObject json);
	
	/**
	 *Discription:删除调度任务
	 *@param jobId
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年10月29日 gaojingdan [变更描述]
	 */
	public ReturnT<String> deleteJob(int jobId);
	
	 /**
	  * 
	  *Discription:执行job
	  *@param jobId
	  *@return
	  *@return:返回值意义
	  *@author:gaojingdan
	  *@update:2017年10月29日 gaojingdan [变更描述]
	  */
    public ReturnT<String> triggerJob(int jobId);
    
    /**
     * 
     *Discription:暂停job
     *@param jobId
     *@return
     *@return:返回值意义
     *@author:gaojingdan
     *@update:2017年10月29日 gaojingdan [变更描述]
     */
	public ReturnT<String> pauseJob(int jobId) ;
	/**
	 * 
	 *Discription:恢复job
	 *@param jobId
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年10月29日 gaojingdan [变更描述]
	 */
	public ReturnT<String> resumeJob(int jobId) ;
}
