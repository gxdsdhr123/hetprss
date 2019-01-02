package com.neusoft.prss.telegraph.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.prss.telegraph.dao.TelegraphHistoryDao;



@Service
public class TelegraphHistoryService {

    @Resource
    private TelegraphHistoryDao telegraphHistoryDao;


    public Map<String,Object> getListInfo(Map<String, Object> param) {
        //bootstrap-table要求服务器返回的json须包含：totlal，rows
        Map<String,Object> result = new HashMap<String,Object>();
        int total= telegraphHistoryDao.getCount(param);
        List<Map<String,String>> rows=telegraphHistoryDao.getList(param);
        result.put("total",total);
        result.put("rows",rows);
        return result;
    }

    public JSONObject searchHisDetail(Map<String, String> map) {
        return telegraphHistoryDao.searchHisDetail(map);
    }

    public List<Map<String,Object>> loadAirline() {
        return telegraphHistoryDao.loadAirline();
    }

    public List<Map<String,Object>> loadTelegraphType() {
        return telegraphHistoryDao.loadTelegraphType();
    }

    public void insertFavorite(Map<String,Object> param) {
        telegraphHistoryDao.insertFavorite(param);
    }

    public void deleteFavorite(Map<String,Object> param) {
        telegraphHistoryDao.deleteFavorite(param);
    }

    public void isRead(Map<String,Object> param) {
        telegraphHistoryDao.isRead(param);
    }

    public List<Map<String,String>> getList(Map<String,Object> param) {
        return telegraphHistoryDao.getList(param);
    }

    public JSONArray getFlightList(Map<String, Object> param) {
        return telegraphHistoryDao.getFlightList(param);
    }

    public void pigeonhole(Map<String,String> map) {
        telegraphHistoryDao.pigeonhole(map);
    }

    public JSONObject getInfo(Map<String,String> map) {
        return telegraphHistoryDao.getInfo(map);
    }

}
