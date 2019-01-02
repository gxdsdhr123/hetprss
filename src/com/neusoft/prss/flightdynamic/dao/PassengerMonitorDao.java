package com.neusoft.prss.flightdynamic.dao;

import java.util.List;
import java.util.Map;

import com.neusoft.framework.common.persistence.annotation.MyBatisDao;
@MyBatisDao
public interface PassengerMonitorDao {
	
    public List<Map<String, Object>> getDataList(Map<String, Object> param);
	
	public int getListCount(Map<String, Object> param);
	
	public List<Map<String, Object>> getAirlines();
	
	public List<Map<String, Object>> getAirports();

}
