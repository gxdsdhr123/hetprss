package com.neusoft.prss.statisticalanalysis.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.neusoft.framework.common.service.BaseService;
import com.neusoft.prss.statisticalanalysis.dao.FlightCountDao;

@Service
public class FlightCountService extends BaseService {
	
	@Resource
	private FlightCountDao flightCountDao;

	public HashMap<String, Object> getFirstCount(Map<String, Object> map) {
		HashMap<String, Object> first = flightCountDao.getFirstCount(map);
		return first;
	}

	public HashMap<String, Object> getSecondCount(Map<String, Object> map) {
		HashMap<String, Object> second = flightCountDao.getSecondCount(map);
		return second;
	}

	public List<HashMap<String, Object>> getBeginSaAlnOscarDay(Map<String, Object> map) {
		List<HashMap<String, Object>> begin = flightCountDao.getBeginSaAlnOscarDay(map);
		return begin;
	}

	public List<HashMap<String, Object>> getEndSaAlnOscarDay(Map<String, Object> map) {
		List<HashMap<String, Object>> end = flightCountDao.getEndSaAlnOscarDay(map);
		return end;
	}

}
