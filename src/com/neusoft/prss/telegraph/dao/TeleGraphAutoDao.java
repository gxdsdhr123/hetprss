package com.neusoft.prss.telegraph.dao;

import org.apache.ibatis.annotations.Param;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;


@MyBatisDao
public interface TeleGraphAutoDao {

    int getAutoListCount(JSONObject param);

    JSONArray getAutoList(JSONObject param);

    JSONArray getEventList();

    JSONArray getAirlineList();

    JSONObject queryAutoById(@Param("id")String id);

    JSONArray getTelegraphTempl(JSONObject param);

    String getTelegraphAutoId();

    void insertAuto(@Param("vo")JSONObject vo);

    void insertAutoRule(@Param("vo")JSONObject vo);

    void updateAuto(@Param("vo")JSONObject vo);

    void updateAutoRule(@Param("vo")JSONObject vo);

    String getAutoRuleId();

    void deleteAutoList(String[] ids);

    void deleteAutoRuleList(String[] ids);

    void updateManualInfo(JSONObject vo);

    void insertManualInfo(JSONObject vo);

    void updateManualStop(JSONObject json);

    JSONObject getFltInfo(String fltid);

    JSONObject getSendAuto(String fltid);
	
}
