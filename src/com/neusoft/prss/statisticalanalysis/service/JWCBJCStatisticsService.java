package com.neusoft.prss.statisticalanalysis.service;

import com.neusoft.framework.common.service.BaseService;
import com.neusoft.prss.statisticalanalysis.dao.JWCBJCStatisticsDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class JWCBJCStatisticsService extends BaseService {

	@Resource
	private JWCBJCStatisticsDao jwcbjcStatisticsDao;

	public List<Map<String, Object>> getDataList(Map<String, Object> map) {

		List<Map<String, Object>> data = jwcbjcStatisticsDao.getDataList(map);
		return data;
	}

	public int getDataListCount(Map<String, Object> map) {

		return jwcbjcStatisticsDao.getDataListCount(map);
	}

	public List<Map<String, Object>> getCBPriceConf(Map<String, Object> map) {

		List<Map<String, Object>> data = jwcbjcStatisticsDao.getCBPriceConf(map);
		return data;
	}

	public int getCBPriceConfCount(Map<String, Object> map) {

		return jwcbjcStatisticsDao.getCBPriceConfCount(map);
	}

	public void addCBPriceConf(Map<String, Object> map) {

		jwcbjcStatisticsDao.addCBPriceConf(map);
	}

	public void updateCBPriceConf(Map<String, Object> map) {

		jwcbjcStatisticsDao.updateCBPriceConf(map);
	}

	public void deleteCBPriceConf(Map<String, Object> map) {

		jwcbjcStatisticsDao.deleteCBPriceConf(map);
	}

	public List<Map<String, Object>> getAirLines() {

		List<Map<String, Object>> data = jwcbjcStatisticsDao.getAirLines();
		return data;
	}
}
