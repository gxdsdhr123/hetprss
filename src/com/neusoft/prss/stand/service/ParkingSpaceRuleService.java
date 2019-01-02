package com.neusoft.prss.stand.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONArray;

public interface ParkingSpaceRuleService {
	
	
	public Map<String,Object> getLeftDataList(Map<String,Object> param);
	
	public JSONArray getAllActType();
	
	public JSONArray getActTypeByCode(@Param("code")String code);
	
	public boolean saveEdit(String code,List<String> selectDataList);

}
