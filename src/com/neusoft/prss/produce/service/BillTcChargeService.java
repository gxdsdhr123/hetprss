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

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.service.BaseService;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.prss.file.service.FileService;
import com.neusoft.prss.produce.dao.BillTcChargeDao;
import com.neusoft.prss.produce.entity.BillTcChargeEntity;

@Service
@Transactional(readOnly = true)
public class BillTcChargeService extends BaseService {

    @Autowired
    private BillTcChargeDao billTcChargeDao;
    @Autowired
    private FileService fileService;
    
    public JSONArray getResType(){
    	return billTcChargeDao.getResType();
    }
    
    public JSONArray getSysUser(){
    	return billTcChargeDao.getSysUser();
    }
    
    public JSONArray getDataList(){
     	JSONArray dataList= billTcChargeDao.getDataList();
     	for(int i=0;i<dataList.size();i++){
     		JSONObject obj=(JSONObject) dataList.get(i);
    			// 签名转Base64
        	String pId = obj.getString("SIGN");
        		if(!StringUtils.isEmpty(pId)){
        			// 获取字节
        			try {
    					byte[] is = fileService.doDownLoadFile(pId);
    					obj.put("PICTURE", new String(Base64.encodeBase64(is)));
    				} catch (Exception e) {
    					logger.error(e.getMessage(), e);
    				}
        		}
		}
    	return dataList;
    }
    public List<Map<String ,String>> getDataTotal(String startDate,String endDate){
    	return billTcChargeDao.getDataTotal(startDate,endDate);
    }
    public JSONArray getFltInfo(Map<String,String> param){
    	return billTcChargeDao.getFltInfo(param);
    }
    public boolean saveAdd(BillTcChargeEntity entity){
    	int count=billTcChargeDao.saveAdd(entity);
    	if(count==1){
    		return true;
    	}else{
    		return false;
    	}
    }
    public boolean saveEdit(BillTcChargeEntity entity){
    	int count=billTcChargeDao.saveEdit(entity);
    	if(count==1){
    		return true;
    	}else{
    		return false;
    	}
    }
    public JSONArray getBillById(String id){
    	return billTcChargeDao.getBillById(id);
    }
    
    public JSONArray getExportWordData(String id){
    	return billTcChargeDao.getExportWordData(id);
    }
    
    public boolean delBillById(String id){
    	int count= billTcChargeDao.delBillById(id);
    	if(count==1){
    		return true;
    	}else{
    		return false;
    	}
    }
  

}
