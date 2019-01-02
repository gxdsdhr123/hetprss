package com.neusoft.prss.mobile.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.neusoft.framework.common.service.BaseService;
import com.neusoft.prss.mobile.dao.MobileRecordDao;

@Service
public class MobileRecordService extends BaseService{

	@Resource
	private MobileRecordDao mobileRecordDao;

	public Map<String, Object> getTableData(Map<String, Object> param) {
		Map<String,Object> result = new HashMap<String,Object>();
		int total =  mobileRecordDao.getDataCount(param);
		JSONArray rows = mobileRecordDao.getDataList(param);
		result.put("total",total);
        result.put("rows",rows);
		return result;
	}

	public List<HashMap<String, Object>> SearchBuMen() {
		return mobileRecordDao.SearchBuMen();
	}

	public List<HashMap<String, Object>> SelectMobileLog(Map<String, Object> map) {
		return mobileRecordDao.SelectMobileLog(map);
	}

	public List<HashMap<String, Object>> getPrintList(Map<String, Object> map) {
		return mobileRecordDao.getPrintList(map);
	}
}
