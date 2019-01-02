package com.neusoft.prss.telegraph.service;

import java.util.List;
import java.util.Map;

public interface TelegraphFlightService {
	
	public Map<String, Object> getDataList(Map<String, Object> param);

	public List<Map<String, Object>> getTelType();
}
