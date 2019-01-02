package com.neusoft.prss.mobile.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.neusoft.framework.common.persistence.BaseDao;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface MobileRecordDao extends BaseDao{

	Map<String, Object> getTableData(Map<String, Object> param);

	List<HashMap<String, Object>> SearchBuMen();

	int getDataCount(Map<String, Object> param);

	JSONArray getDataList(Map<String, Object> param);

	List<HashMap<String, Object>> SelectMobileLog(Map<String, Object> map);

	List<HashMap<String, Object>> getPrintList(Map<String, Object> map);

}
