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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.service.BaseService;
import com.neusoft.framework.common.utils.StringUtils;
import com.neusoft.prss.file.service.FileService;
import com.neusoft.prss.produce.dao.BillPassengerNumDao;
import com.neusoft.prss.produce.entity.BillPassengerNumEntity;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class BillPassengerNumServiceBackup extends BaseService {

    @Autowired
    private BillPassengerNumDao billPassengerNumDao;
    @Autowired
    private FileService fileService;
    
    public JSONArray getResType(){
    	return billPassengerNumDao.getResType();
    }
    
    public JSONArray getSysUser(){
    	return billPassengerNumDao.getSysUser();
    }
    
    public JSONArray getDataList(){
     	JSONArray dataList= billPassengerNumDao.getDataList();
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
    public List<Map<String ,String>> getDataTotal(){
    	return billPassengerNumDao.getDataTotal();
    }
    public JSONArray getFltInfo(Map<String,String> param){
    	return billPassengerNumDao.getFltInfo(param);
    }
    public boolean saveAdd(BillPassengerNumEntity entity){
    	int count=billPassengerNumDao.saveAdd(entity);
    	if(count==1){
    		return true;
    	}else{
    		return false;
    	}
    }
    public boolean saveEdit(BillPassengerNumEntity entity){
    	int count=billPassengerNumDao.saveEdit(entity);
    	if(count==1){
    		return true;
    	}else{
    		return false;
    	}
    }
    public JSONArray getBillById(String id){
    	return billPassengerNumDao.getBillById(id);
    }
    
    public JSONArray getExportWordData(String id){
    	return billPassengerNumDao.getExportWordData(id);
    }
    
    public boolean delBillById(String id){
    	int count= billPassengerNumDao.delBillById(id);
    	if(count==1){
    		return true;
    	}else{
    		return false;
    	}
    }
  

}
