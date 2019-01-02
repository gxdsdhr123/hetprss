package com.neusoft.prss.telegraph.service;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.prss.telegraph.dao.TelegraphAnalysisDao;

@Transactional(readOnly = true)
@Service
public class TelegraphAnalysisService {

	@Resource
	private TelegraphAnalysisDao telegraphAnalysisDao;
	
    public Map<String,Object> getAnalysisList(JSONObject param) {
        Map<String,Object> result = new HashMap<String,Object>();
        int total= telegraphAnalysisDao.getAnalysisListCount(param);
        JSONArray rows=telegraphAnalysisDao.getAnalysisList(param);
        result.put("total",total);
        result.put("rows",rows);
        return result;
    }

    public JSONObject getAnalysisById(String id) {
        return telegraphAnalysisDao.getAnalysisById(id);
    }
	
	
}
