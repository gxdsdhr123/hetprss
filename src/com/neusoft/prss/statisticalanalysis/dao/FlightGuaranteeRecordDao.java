package com.neusoft.prss.statisticalanalysis.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neusoft.framework.common.persistence.BaseDao;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface FlightGuaranteeRecordDao extends BaseDao {

	List<Map<String, Object>> getFlightNumsList_index(String beginTime);

	List<HashMap<String, Object>> getTotalData(Map<String, Object> paramMap);

	int getHighTime(String beginTime);

	List<Map<String, Object>> getgjDataList(String beginTime);

}
