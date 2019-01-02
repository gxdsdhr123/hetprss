package com.neusoft.prss.rule.dao;

import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSONArray;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;
import com.neusoft.prss.rule.entity.RuleInfo;
import com.neusoft.prss.rule.entity.RuleSetUpVO;

@MyBatisDao
public interface SetUpRuleDao {

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
	
    public void insertSetUp(RuleSetUpVO setUpVO);

    public void updateSetUp(RuleSetUpVO setUpVO);

    public void deleteSetUpInfo(String ruleId);

    public RuleSetUpVO getSetUp(String ruleId);

    public JSONArray loadFlightNumber(Map<String,Object> param);

    public JSONArray loadAircraftNumber(Map<String,Object> param);
}
