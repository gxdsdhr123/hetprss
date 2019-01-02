package com.neusoft.prss.statisticalanalysis.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.neusoft.framework.common.service.BaseService;
import com.neusoft.prss.statisticalanalysis.dao.QyCarEmployeeWorkDao;

@Service
public class QyCarEmployeeWorkService extends BaseService {

	@Resource
	private QyCarEmployeeWorkDao qyCarEmployeeWorkDao;

	public Map<String, Object> getData(Map<String, Object> param) {
		Map<String,Object> result = new HashMap<String,Object>();
		int total =  qyCarEmployeeWorkDao.getDataCount(param);
		JSONArray rows = qyCarEmployeeWorkDao.getDataList(param);
		result.put("total",total);
        result.put("rows",rows);
		return result;
	}

	public List<HashMap<String, Object>> getBanZu(String name) {
		return qyCarEmployeeWorkDao.getBanZu(name);
	}

	public List<HashMap<String, Object>> getZuYuan(Map<String, Object> param) {
		return qyCarEmployeeWorkDao.getZuYuan(param);
	}

	public List<HashMap<String, Object>> getPrintList(Map<String, Object> map) {
		return qyCarEmployeeWorkDao.getPrintList(map);
	}
}
