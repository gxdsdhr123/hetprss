package com.neusoft.prss.produce.appearPortLuggage.service;

import java.util.List;
import java.util.Map;

public interface AppearPortLuggageService {
	
	Map<String, Object> listDouList(Map<String,Object> param);
	int saveMain(Map<String,Object> param,List<Map<String,Object>> list);
	int delMainAndB(String id);
	Map<String,Object> selMain(String id);
	List<Map<String,Object>>selB(String id);
	int updateMinaB(Map<String,Object> param,List<Map<String,Object>> list);
	int delBGufl(String id);
	int delB(String id);
	String countAppGufl(Map<String,Object> param);
}
