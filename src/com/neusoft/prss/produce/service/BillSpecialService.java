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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.neusoft.framework.common.service.BaseService;
import com.neusoft.prss.produce.dao.BillSpecialDao;
import com.neusoft.prss.produce.entity.BillSpecialEntity;

@Service
@Transactional(readOnly = true)
public class BillSpecialService extends BaseService {

    @Autowired
    private BillSpecialDao billSpecialDao;
    
    public JSONArray getResType(){
    	return billSpecialDao.getResType();
    }
    
    public JSONArray getSysUser(){
    	return billSpecialDao.getSysUser();
    }
    
    public JSONArray getDataList(Map<String, Object> map){
    	return billSpecialDao.getDataList(map);
    }
    public List<Map<String ,String>> getDataTotal(String startDate,String endDate){
    	return billSpecialDao.getDataTotal(startDate,endDate);
    }
    public JSONArray getFltInfo(Map<String,String> param){
    	return billSpecialDao.getFltInfo(param);
    }
    public boolean saveAdd(BillSpecialEntity entity){
    	int count=billSpecialDao.saveAdd(entity);
    	if(count==1){
    		return true;
    	}else{
    		return false;
    	}
    }
    public boolean saveEdit(BillSpecialEntity entity){
    	int count=billSpecialDao.saveEdit(entity);
    	if(count==1){
    		return true;
    	}else{
    		return false;
    	}
    }
    public JSONArray getBillById(String id){
    	return billSpecialDao.getBillById(id);
    }
    
    public JSONArray getExportWordData(String id){
    	return billSpecialDao.getExportWordData(id);
    }
    
    public boolean delBillById(String id){
    	int count= billSpecialDao.delBillById(id);
    	if(count==1){
    		return true;
    	}else{
    		return false;
    	}
    }
  

}
