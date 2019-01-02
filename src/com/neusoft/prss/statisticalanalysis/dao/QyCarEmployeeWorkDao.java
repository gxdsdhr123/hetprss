package com.neusoft.prss.statisticalanalysis.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.neusoft.framework.common.persistence.BaseDao;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface QyCarEmployeeWorkDao extends BaseDao {

	int getDataCount(Map<String, Object> param);

	JSONArray getDataList(Map<String, Object> param);

	List<HashMap<String, Object>> getBanZu(String name);

	List<HashMap<String, Object>> getZuYuan(Map<String, Object> param);

	List<HashMap<String, Object>> getPrintList(Map<String, Object> map);

}
