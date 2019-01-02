package com.neusoft.prss.statisticalanalysis.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neusoft.framework.common.persistence.BaseDao;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface FlightRunStateDao extends BaseDao {

	HashMap<String, Object> getPageData(String beginTime);

	int getHighTime(String beginTime);

	HashMap<String, Object> getHighData(Map<String, Object> paramMap);

	List<Map<String, Object>> getImageData(String beginTime);

}
