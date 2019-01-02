package com.neusoft.prss.produce.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CleanServerService {


	public ArrayList<HashMap<String, Object>> getPeople();

	public void save(Map<String, Object> map);

	public ArrayList<HashMap<String, Object>> getAirline();

	public Map<String, Object> getDataList(Map<String, Object> param);

	public void delete(String id);

	public HashMap<String, Object> getDataById(String id);

	public void update(Map<String, Object> map);

	public List<HashMap<String, Object>> getTotalData(String id);
}
