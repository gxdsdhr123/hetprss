package com.neusoft.prss.produce.service;

import java.util.List;
import java.util.Map;

public interface BillInUnloaderService {

	public Map<String, Object> getDataList(Map<String, Object> param);

	public List<Map<String, Object>> getDataById(String fltid);

	public List<Map<String, Object>> getXIData(String fltid);

	public List<Map<String, Object>> getHYData(String fltid);
}
