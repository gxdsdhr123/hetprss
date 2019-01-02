/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年11月15日 上午9:22:54
 *@author:Heqg
 *@version:[v1.0]
 */
package com.neusoft.prss.produce.dao;

import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSONArray;
import com.neusoft.framework.common.persistence.BaseDao;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface OperatorRecordDao extends BaseDao {
	
    JSONArray queryData(Map<String,Object> param);

    int getListCount(Map<String,Object> param);

    List<Map<String,Object>> getList(Map<String,Object> param);
	
}
