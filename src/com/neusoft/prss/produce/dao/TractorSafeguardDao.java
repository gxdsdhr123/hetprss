package com.neusoft.prss.produce.dao;

import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface TractorSafeguardDao {

    public JSONObject loadInfo(String ruleId);
    
	public List<Map<String, Object>> loadAtcactype();
	
	public List<Map<String, Object>> loadAirline();

    public List<Map<String,Object>> loadAircraft();
    
	public List<Map<String, Object>> getList(Map<String, Object> param);
	
	public int getListCount(Map<String, Object> param);

    public void delete(String ruleId);

    public JSONArray getDimData(JSONObject object);

    public void insert(JSONObject info);

    public void update(JSONObject info);

}
