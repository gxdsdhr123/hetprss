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

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.neusoft.prss.produce.entity.BillAblqChargeEntity;

@Service
@Transactional(readOnly = true)
public interface BillAblqChargeService{

   public JSONArray getResType();
    
    public JSONArray getSysUser();
    
    public JSONArray getDataList();
    
    public List<Map<String ,String>> getDataTotal(String startDate,String endDate);
    
    public JSONArray getFltInfo(Map<String,String> param);
    
    public boolean saveAdd(BillAblqChargeEntity entity);
    
    public boolean saveEdit(BillAblqChargeEntity entity);
    
    public JSONArray getBillById(String id);
    
    public JSONArray getExportWordData(String id);
    
    public boolean delBillById(String id);

}
