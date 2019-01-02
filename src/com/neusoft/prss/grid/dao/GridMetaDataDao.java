package com.neusoft.prss.grid.dao;

import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.neusoft.framework.common.persistence.BaseDao;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface GridMetaDataDao extends BaseDao {
	JSONArray getSchemas();

	JSONArray getTables(Map<String, String> params);
}
