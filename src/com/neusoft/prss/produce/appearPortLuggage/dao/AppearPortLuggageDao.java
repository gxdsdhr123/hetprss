package com.neusoft.prss.produce.appearPortLuggage.dao;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface AppearPortLuggageDao {
	
	JSONArray listDou(Map<String,Object> param);
	int listDouCount(Map<String,Object> param);
	int saveMain(Map<String,Object> param);
	int saveB(Map<String,Object> param);
	int delMian(String id);
	int delB(String id);
	Map<String,Object> selMain(String id);
	List<Map<String,Object>>selB(String id);
	int revampMain(Map<String,Object> param);
	int revampB(Map<String,Object> param);
	int delBGufl(String id);
	String countAppGufl(Map<String,Object> param);
}
