package com.neusoft.prss.telegraph.dao;

import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.persistence.annotation.MyBatisDao;


@MyBatisDao
public interface TelegraphHistoryDao {
	
    public List<Map<String,String>> getList(Map<String, Object> param);
	
	public int getCount(Map<String, Object> param);
	
	public JSONObject searchHisDetail(Map<String,String> map);

    public List<Map<String,Object>> loadAirline();

    public List<Map<String,Object>> loadTelegraphType();

    public void insertFavorite(Map<String,Object> param);

    public void deleteFavorite(Map<String,Object> param);

    public void isRead(Map<String,Object> param);

    public void updatePrint(Map<String,String> map);

    public int getFlightCount(Map<String,Object> param);

    public JSONArray getFlightList(Map<String,Object> param);

    public void pigeonhole(Map<String,String> map);

    public JSONObject getInfo(Map<String,String> map);

}
