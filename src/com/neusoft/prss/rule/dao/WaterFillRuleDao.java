package com.neusoft.prss.rule.dao;

import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;
import com.neusoft.prss.rule.entity.RuleInfo;
import com.neusoft.prss.rule.entity.RuleWaterFillVO;

@MyBatisDao
public interface WaterFillRuleDao {

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
     * 
     *Discription:清水加注规则插入.
     *@param formData
     *@return:返回值意义
     *@author:l.ran@neusoft.com
     *@update:2017年12月12日 Maxx [变更描述]
     */
    public void insertWaterFill(JSONObject formData);

    public void insertWaterFill(RuleWaterFillVO waterFillVO);

    public void updateWaterFill(RuleWaterFillVO waterFillVO);

    public RuleWaterFillVO getWaterFill(String ruleId);

    public void deleteWaterFillInfo(String ruleId);
}
