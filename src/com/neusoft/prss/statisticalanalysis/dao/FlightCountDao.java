package com.neusoft.prss.statisticalanalysis.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neusoft.framework.common.persistence.BaseDao;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface FlightCountDao extends BaseDao {

	HashMap<String, Object> getFirstCount(Map<String, Object> map);

	HashMap<String, Object> getSecondCount(Map<String, Object> map);

	List<HashMap<String, Object>> getBeginSaAlnOscarDay(Map<String, Object> map);

	List<HashMap<String, Object>> getEndSaAlnOscarDay(Map<String, Object> map);

}
