package com.neusoft.prss.produce.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.neusoft.prss.produce.entity.BillInPickEntity;


public interface BillInPickService{
	    
    JSONArray getResType();

    JSONArray getSysUser();

    JSONArray getDataList(String dateStart);

    List<Map<String ,String>> getDataTotal(String startDate);
    List<Map<String ,String>> getDataTotal(String startDate, String endDate);

    JSONArray getFltInfo(Map<String, String> param);

    boolean saveAdd(BillInPickEntity entity);

    boolean saveEdit(BillInPickEntity entity);

    JSONArray getBillById(String id);

    JSONArray getExportWordData(String id);

    boolean delBillById(String id);

    JSONArray getFileID(String id);

}
