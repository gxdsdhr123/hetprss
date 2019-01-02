package com.neusoft.prss.statisticalanalysis.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * 机务统计表
 */
public interface SaFltJiwuService {

    /**
     * 获取机务统计信息
     */
    public JSONObject getDataList(Map<String, Object> params);

    /**
     * 获取机务统计信息下载信息
     */
    public List<Map<String,String>> getDownDataList(Map<String, Object> params);
}
