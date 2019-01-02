package com.neusoft.prss.plan.service;

import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 公务机/通航长期计划
 */
public interface SpecialPlanService {
	/**
	 * 查询公务通航计划
	 */
	public JSONObject getSpecialPlan(Map<String, Object> paramMap);
	
	/**
	 * 根据id查询公务通航计划
	 */
	public JSONArray getSpecialPlanByIds(Map<String, Object> paramMap);
	
	/**
	 * 移动公务通航计划数据到回收站
	 */
	public int delSpecialPlan(Map<String, Object> params);
	
	/**
	 * 删除恢复公务通航计划
	 */
	public int operateSpecialPlan(Map<String, Object> params);
	
	/**
	 * 新增/更新公务通航计划
	 */
	public int saveSpecialPlan(Map<String, Object> paramMap);
	
	/**
	 * 根据报文id获取报文原文数据
	 * @param msgId
	 */
	public String getMsgById(String msgId);
}
