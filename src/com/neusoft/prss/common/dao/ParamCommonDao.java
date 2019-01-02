/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年10月28日 下午5:12:09
 *@author:Maxx
 *@version:[v1.0]
 */
package com.neusoft.prss.common.dao;

import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;

@MyBatisDao
public interface ParamCommonDao {

    JSONObject getColumn(@Param("colids")String varcols);

    JSONArray getVariable(Map<String,String> map);

    String getAutoRuleId();
    
    void insertAutoRule(@Param("vo")JSONObject vo);

    void updateAutoRule(@Param("vo")JSONObject vo);

    void deleteAutoRuleList(String[] ids);

    JSONArray getRuleList(Map<String,String> map);

    JSONObject getRuleById(@Param("id")String id);
    

}
