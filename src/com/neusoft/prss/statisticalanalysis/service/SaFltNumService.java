package com.neusoft.prss.statisticalanalysis.service;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * 航班架次统计表
 */
public interface SaFltNumService {

    /**
     * 获取航班架次统计信息
     */
    public JSONObject getDataList(Map<String, Object> params);
    
    /**
     * 导出航班架次统计信息数据
     */
    public byte[] getExcelByte(Map<String, Object> params);
}
