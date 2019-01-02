package com.neusoft.prss.produce.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public interface BillOperatorZXService {

    JSONArray getDataList(String dateStart, String flightNumber, String operator);

    JSONObject getDataInfo(String fltid);

    JSONArray getDataDetail(String fltid);
}
