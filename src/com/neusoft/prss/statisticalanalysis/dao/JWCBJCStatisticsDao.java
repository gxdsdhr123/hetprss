package com.neusoft.prss.statisticalanalysis.dao;

import com.neusoft.framework.common.persistence.BaseDao;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;

import java.util.List;
import java.util.Map;

@MyBatisDao
public interface JWCBJCStatisticsDao extends BaseDao {

	List<Map<String, Object>> getDataList(Map<String, Object> map);

	int getDataListCount(Map<String, Object> map);

	List<Map<String, Object>> getCBPriceConf(Map<String, Object> map);

	int getCBPriceConfCount(Map<String, Object> map);

	void addCBPriceConf(Map<String, Object> map);

	void updateCBPriceConf(Map<String, Object> map);

	void deleteCBPriceConf(Map<String, Object> map);

	List<Map<String, Object>> getAirLines();
}
