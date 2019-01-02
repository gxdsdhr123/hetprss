/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年11月15日 上午9:36:30
 *@author:Heqg
 *@version:[v1.0]
 */
package com.neusoft.prss.produce.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.neusoft.prss.produce.entity.BillPassengerNumEntity;

public interface BillPassengerNumService {

	JSONArray getResType();
    JSONArray getSysUser();
    JSONArray getDataList(String dateStart);
    List<Map<String ,String>> getDataTotal(String dateStart);
    JSONArray getFltInfo(Map<String, String> param);
    boolean saveAdd(BillPassengerNumEntity entity);
    boolean saveEdit(BillPassengerNumEntity entity);
    JSONArray getBillById(String id);
    JSONArray getFileID(String id);
    JSONArray getExportWordData(String id);
    boolean delBillById(String id);

}
