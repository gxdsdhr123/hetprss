package com.neusoft.prss.rule.dao;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface DelayTimeRuleDao {

	public int insertRuleInfo(JSONObject ruleInfo);
	
	public int updateRuleInfo(JSONObject ruleInfo);
	
	public int deleteRuleInfo(String ruleId);
	
	public List<Map<String, Object>> getRuleList(Map<String, Object> param);
	
	public int getRuleListCount(Map<String, Object> param);
	
    public void insertDelayTimeInfo(JSONObject delayTimeInfo);

    public void updateDelayTimeInfo(JSONObject delayTimeInfo);

    public void deleteDelayTimeInfo(String ruleId);

    public JSONObject getDelayTimeInfo(String ruleId);

    public JSONArray loadActtypeCode(Map<String,Object> param);

    public JSONArray loadAlnCode(Map<String,Object> param);

    public List<Map<String,Object>> getDetailList(Map<String,Object> param);

    public void insertDelayTimeDetail(JSONObject detail);

    public void deleteDelayTimeDetail(String ruleId);

    public int filterInfo(JSONObject formData);

    public JSONArray getVarList();

    public JSONObject getColumn(@Param("colids")String con_flag);

    public void updateFdPlus(JSONObject delayTimeInfo);
    
    public void deleteFdPlus(String ruleId);
}
