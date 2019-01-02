package com.neusoft.prss.statisticalanalysis.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 手持机异常点击、违规点击统计,人员统计
 */
public interface AbnormalClickRecordService {

    /**
     * 获取手持机异常点击、违规点击统计记录
     */
    public JSONObject getReskindClickList(Map<String, Object> params);

    /**
     * 获取手持机异常点击、违规点击统计记录下载信息
     */
    public List<Map<String,String>> getReskindClickDownList(Map<String, Object> params);
    
    /**
     * 获取手持机异常点击、违规点击统计记录详情
     */
    public JSONObject getReskindClickDetail(Map<String, Object> params);
    
    /**
     * 获取手持机异常点击、违规点击人员统计记录
     */
    public JSONObject getUserClickList(Map<String, Object> params);

    /**
     * 获取手持机异常点击、违规点击人员统计记录下载信息
     */
    public List<Map<String,String>> getUserClickDownList(Map<String, Object> params);
    
    /**
     * 获取保障类型
     */
    public JSONArray getReskindList();
}
