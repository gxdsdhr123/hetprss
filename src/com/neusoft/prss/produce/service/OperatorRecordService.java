/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年11月15日 上午9:36:30
 *@author:Heqg
 *@version:[v1.0]
 */
package com.neusoft.prss.produce.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.fastjson.JSONArray;
import com.neusoft.framework.common.service.BaseService;
import com.neusoft.prss.produce.dao.OperatorRecordDao;

@Service
@Transactional(readOnly = true)
public class OperatorRecordService extends BaseService {

	@Autowired
	private OperatorRecordDao OperatorRecordDao;

    public JSONArray queryData(Map<String,Object> param) {
        return OperatorRecordDao.queryData(param);
    }

    public Map<String,Object> getList(Map<String,Object> param) {
        //bootstrap-table要求服务器返回的json须包含：totlal，rows
        Map<String,Object> result = new HashMap<String,Object>();
        int total= OperatorRecordDao.getListCount(param);
        List<Map<String, Object>> rows = OperatorRecordDao.getList(param);
        result.put("total",total);
        result.put("rows",rows);
        return result;
    }

	
}
