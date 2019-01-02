package com.neusoft.prss.telegraph.dao;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;


@MyBatisDao
public interface TelegraphAnalysisDao {

    int getAnalysisListCount(JSONObject param);

    JSONArray getAnalysisList(JSONObject param);

    JSONObject getAnalysisById(String id);

	
}
