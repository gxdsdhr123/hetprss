package com.neusoft.prss.produce.service;

import com.alibaba.fastjson.JSONArray;
import com.neusoft.prss.produce.entity.BillAirEntity;

import java.util.List;
import java.util.Map;

public interface BillAirService {

    JSONArray getResType();

    JSONArray getSysUser();

    JSONArray getDataList(String dateStart);

    List<Map<String ,String>> getDataTotal(String startDate);
    List<Map<String ,String>> getDataTotal(String startDate, String endDate);

    JSONArray getFltInfo(Map<String, String> param);

    boolean saveAdd(BillAirEntity entity);

    boolean saveEdit(BillAirEntity entity);

    JSONArray getBillById(String id);

    JSONArray getExportWordData(String id);

    boolean delBillById(String id);
}
