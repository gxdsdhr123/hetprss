package com.neusoft.prss.stats.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * 拖飞机记录
 */
public interface DragPlaneRecordService {

    /**
     * 获取拖飞机记录信息
     */
    public JSONObject getDataList(Map<String, Object> params);

    /**
     * 获取拖飞机记录下载信息
     */
    public List<Map<String,String>> getDownDataList(Map<String, Object> params);
}
