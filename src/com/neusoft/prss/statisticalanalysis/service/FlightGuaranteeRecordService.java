package com.neusoft.prss.statisticalanalysis.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.neusoft.framework.common.service.BaseService;
import com.neusoft.prss.statisticalanalysis.dao.FlightGuaranteeRecordDao;

@Service
public class FlightGuaranteeRecordService extends BaseService {

	@Resource
	private FlightGuaranteeRecordDao flightGuaranteeRecordDao;

	public List<Map<String, Object>> getFlightNumsList_index(String beginTime) {
		return flightGuaranteeRecordDao.getFlightNumsList_index(beginTime);
	}

	public List<HashMap<String, Object>> getTotalData(Map<String, Object> paramMap) {
		return flightGuaranteeRecordDao.getTotalData(paramMap);
	}

	public int getHighTime(String beginTime) {
		return flightGuaranteeRecordDao.getHighTime(beginTime);
	}

	public List<Map<String, Object>> getgjDataList(String beginTime) {
		return flightGuaranteeRecordDao.getgjDataList(beginTime);
	}

}
