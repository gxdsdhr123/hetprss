package com.neusoft.prss.flightdynamic.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.neusoft.prss.flightdynamic.dao.PassengerMonitorDao;


@Service
@Transactional(readOnly=true)
public class PassengerMonitorService {
	@Autowired
	private PassengerMonitorDao PassengerMonitorDao ;
	
	public Map<String,Object> getDataList(Map<String,Object> param){
		Map<String,Object> result = new HashMap<String,Object>();
		int total= PassengerMonitorDao.getListCount(param);
		List<Map<String, Object>> rows = PassengerMonitorDao.getDataList(param);
        result.put("total",total);
        result.put("rows",rows);
        return result;
	}
	public List<Map<String, Object>> getAirlines() {
		return PassengerMonitorDao.getAirlines();
	}
	public List<Map<String, Object>> getAirports() {
		return PassengerMonitorDao.getAirports();
	}
	
	


	
}
