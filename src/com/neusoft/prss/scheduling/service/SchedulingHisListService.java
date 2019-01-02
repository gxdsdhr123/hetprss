/**
 *application name:history-service
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年11月27日 下午3:29:00
 *@author:yu-zd
 *@version:[v1.0]
 */
package com.neusoft.prss.scheduling.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.prss.scheduling.entity.JobTaskEntity;

public interface SchedulingHisListService {

	/**
	 * 
	 * Discription:获取航班动态数据.
	 * 
	 * @param userId
	 * @param schemaId
	 * @return
	 * @return:
	 * @author:yu-zd
	 * @update:2017年10月20日 yu-zd [变更描述]
	 */
	public JSONArray getDynamic(String hisDate, String switches,String flagBGS,String reskind,String schema,String suffix,String userId,
            Map<String,Object>... params);
	
	public List<String> getFileId(String id, String type);
	
	/**
	 * 根据航班ID获取航班详情
	 * 
	 * @param fltid
	 * @return
	 */
	public JSONObject getFltById(String fltid);
	
	/**
	 * 获取保障任务列表
	 * 
	 * @param fltid
	 * @return
	 */
	public List<JobTaskEntity> getJobTaskList(Map<String, String> params);
	
	/**
	 * 获取扩展列头
	 * @param params
	 * @return
	 */
	public JSONArray getPlusColumns(Map<String, String> params);

	/**
	 * 查看任务节点时间
	 * @param idInt
	 * @param jobKind
	 * @return
	 */
	public List<Map<String, Object>> getNodeTime(int idInt, String jobKind);
	
}
