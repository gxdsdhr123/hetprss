package com.neusoft.prss.rule.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;
import com.neusoft.prss.rule.entity.RuleInfo;
import com.neusoft.prss.rule.entity.RuleRelProcVO;

@MyBatisDao
public interface TaskAllotRuleDao {

	public int insertRuleInfo(RuleInfo ruleInfo);
	
	public int updateRuleInfo(RuleInfo ruleInfo);
	
	public int deleteRuleInfo(String ruleId);
	
	public int insertRuleDatavalueRel(List<Map<String, Object>> tabdata);
	
	public void deleteRuleDatavalueRel(String ruleId);
	
	public RuleInfo loadRuleInfo(String ruleId);
	
	public List<Map<String, Object>> loadAtcactype();
	
	public List<Map<String, Object>> loadAirline();
	
	public List<Map<String, Object>> loadRuleDataValueRel(String ruleId);
	
	public List<Map<String, Object>> getRuleList(Map<String, Object> param);
	
	public int getRuleListCount(Map<String, Object> param);
	
	/**
	 *Discription:规则流程关系
	 *@param ruleRelProcVO
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年11月4日 gaojingdan [变更描述]
	 */
	public int addRuleRelProcInfo(RuleRelProcVO ruleRelProcVO);
	
	/**
	 *Discription:更新规则流程关系
	 *@param ruleRelProcVO
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年11月4日 gaojingdan [变更描述]
	 */
	public int updateRuleRelProcInfo(RuleRelProcVO ruleRelProcVO);
	
	
	/**
	 *Discription:规则节点关系.
	 *@param nodeList
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年11月4日 gaojingdan [变更描述]
	 */
	public int addRuleRelNodeInfoAll(List<JSONObject> nodeList);
	/**
	 * 
	 *Discription:增加规则节点关系
	 *@param node
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年11月5日 gaojingdan [变更描述]
	 */
	public int addRuleRelNodeInfo(JSONObject node);
	/**
	 * 
	 *Discription:更新规则节点关系
	 *@param node
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年11月5日 gaojingdan [变更描述]
	 */
	public int updateRuleRelNodeInfo(JSONObject node);
	
	/**
	 * 
	 *Discription:删除规则流程节点关系.
	 *@param ruleId
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年11月4日 gaojingdan [变更描述]
	 */
	public int delRuleRelNodeInfo(String ruleId);
	/**
	 *Discription:删除规则流程关系
	 *@param ruleId
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年11月4日 gaojingdan [变更描述]
	 */
	public int delRuleRelProcInfo(String ruleId);
	
	/**
	 *Discription:获取规则流程节点
	 *@param ruleId
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年11月4日 gaojingdan [变更描述]
	 */
	public List<JSONObject> getRuleProcNodeInfo(@Param("ruleId")String ruleId);
	
	/**
	 *Discription:批量删除.
	 *@param ruleProcId
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年11月6日 gaojingdan [变更描述]
	 */
	public int delRuleRelProcInfoBatch(@Param("ruleProcId")String ruleProcId);
	/**
	 *Discription:批量删除.
	 *@param ruleProcId
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年11月6日 gaojingdan [变更描述]
	 */
	public int delRuleRelNodeInfoBatch(@Param("ruleProcId")String ruleProcId);
	
	/**
	 *Discription:判断规则是否已生成任务
	 *@param ruleId
	 *@return
	 *@return:返回值意义
	 *@author:gaojingdan
	 *@update:2017年11月8日 gaojingdan [变更描述]
	 */
	public int checkRuleIfHaveTask(@Param("ruleId")String ruleId);
	/**
	 * 根据流程模板id获取绑定规则个数
	 * @param procId
	 * @return
	 */
	public int getRuleCountByProc(@Param("procId")String procId);
	/**
	 * 
	 *Discription:获取任务分配时间所用参数列表
	 *@return
	 *@return:返回值意义	
	 *@author:gaojingdan
	 *@update:2018年1月12日 gaojingdan [变更描述]
	 */
	public JSONArray getVarList();
}
