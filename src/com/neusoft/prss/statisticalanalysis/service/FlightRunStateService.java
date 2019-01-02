package com.neusoft.prss.statisticalanalysis.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.neusoft.framework.common.service.BaseService;
import com.neusoft.prss.statisticalanalysis.dao.FlightRunStateDao;

@Service
public class FlightRunStateService extends BaseService {

	@Resource
	private FlightRunStateDao flightRunStateDao;

	public HashMap<String, Object> getPageData(String beginTime) {
		return flightRunStateDao.getPageData(beginTime);
	}

	public int getHighTime(String beginTime) {
		return flightRunStateDao.getHighTime(beginTime);
	}

	public HashMap<String, Object> getHighData(Map<String, Object> paramMap) {
		return flightRunStateDao.getHighData(paramMap);
	}

	public List<Map<String, Object>> getImageData(String beginTime) {
		return flightRunStateDao.getImageData(beginTime);
	}
}
