package com.neusoft.prss.progress.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.persistence.BaseDao;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;
@MyBatisDao
public interface ProgressDao extends BaseDao{

	/**
	 * 获取任务列表
	 * @param params
	 * @return
	 */
	JSONArray getTaskList(Map<String, Object> params);
	/**
	 * 获取任务预警信息
	 * @param params
	 * @return
	 */
	JSONArray getTaskAlarms(@Param("jobTypeList")List<String> jobType,@Param("fltids")List<String> fltids);
	/**
	 * 获取航班上的任务
	 * @param params
	 * @return
	 */
	JSONArray getTaskByFlt(Map<String, Object> params);
	/**
	 * 获取航班扩展属性配置ID
	 * @param params
	 * @return
	 */
	public String getFltPlusAttrId(Map<String,String> params);
	/**
	 * 获取航班备注
	 * @param params
	 * @return
	 */
	public JSONObject getFltRemark(Map<String,String> params);
	/**
	 * 保存航班备注
	 * @param params
	 */
	public void saveFltRemark(Map<String,String> params);

}
