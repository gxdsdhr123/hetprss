package com.neusoft.prss.statisticalanalysis.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * 关舱门后机坪等待超60分钟以上航班情况即时报告表
 */
public interface SaFltGcmWaitingService {

    /**
     * 获取航班情况信息
     */
    public JSONObject getDataList(Map<String, Object> params);

    /**
     * 获取航班情况下载信息
     */
    public List<Map<String,String>> getDownDataList(Map<String, Object> params);
}
