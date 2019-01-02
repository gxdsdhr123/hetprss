package com.neusoft.prss.produce.service;

import com.alibaba.fastjson.JSONArray;
import com.neusoft.prss.produce.entity.BillDeicingEntity;

import java.util.List;
import java.util.Map;

public interface BillDeicingService {

    JSONArray getResType();

    JSONArray getSysUser();

    JSONArray getDataList(Map<String, Object> map);

    List<Map<String ,String>> getDataTotal(Map<String, Object> map);

    JSONArray getFltInfo(Map<String, String> param);

    boolean saveAdd(BillDeicingEntity entity);

    boolean saveEdit(BillDeicingEntity entity);

    JSONArray getBillById(String id);

    JSONArray getExportWordData(String id);

    boolean delBillById(String id);
}
