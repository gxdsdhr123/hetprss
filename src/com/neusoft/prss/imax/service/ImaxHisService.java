package com.neusoft.prss.imax.service;

import java.util.List;
import java.util.Map;

public interface ImaxHisService {

	Map<String, Integer[]> getLineChart_illegal(String date, String officeId) throws Exception;
	
	Map<String, Map<String, Integer>> getBarChart_illegal(String date, String officeId,String targetDate) throws Exception;
	
	List<Map<String, Object>> getPersonList_illegal(String date, String officeId) throws Exception;
	
	Map<String, Object> getLineText_illegal(String date, String officeId)throws Exception;
	
}
