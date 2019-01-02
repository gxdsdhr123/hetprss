package com.neusoft.prss.statisticalanalysis.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.neusoft.framework.common.service.BaseService;
import com.neusoft.prss.statisticalanalysis.dao.QcDailyGuaranteeRecordDao;

@Service
public class QcDailyGuaranteeRecordService extends BaseService {

	@Resource
	private QcDailyGuaranteeRecordDao qcDailyGuaranteeRecordDao;

	public List<Map<String, Object>> getFlightNumsList_index(String beginTime) {
		return qcDailyGuaranteeRecordDao.getFlightNumsList_index(beginTime);
	}
	public List<HashMap<String, Object>> getTotalData(Map<String, Object> paramMap) {
		return qcDailyGuaranteeRecordDao.getTotalData(paramMap);
	}
	public int getHighTime(String beginTime) {
		return qcDailyGuaranteeRecordDao.getHighTime(beginTime);
	}
	

}
