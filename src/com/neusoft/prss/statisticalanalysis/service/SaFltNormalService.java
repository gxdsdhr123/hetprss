package com.neusoft.prss.statisticalanalysis.service;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * 航班正常率统计表
 */
public interface SaFltNormalService {

    /**
     * 获取航班正常率统计信息
     */
    public JSONObject getDataList(Map<String, Object> params);

    /**
     * 导出航班正常率统计信息数据
     */
    public byte[] getExcelByte(Map<String, Object> params);
}
