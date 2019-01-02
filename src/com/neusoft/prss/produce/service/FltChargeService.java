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
import com.neusoft.prss.produce.dao.FltChargeDao;
import com.neusoft.prss.produce.entity.FltChargeServiceEntity;

/**
 * 航空器加清排污记录表单据
 */
@Service
@Transactional(readOnly = true)
public class FltChargeService extends BaseService{
	@Autowired
	private FltChargeDao fltChargeDao;
	
	@Autowired
    private FileService fileService;
	
	public JSONObject getDataList(Map<String, Object> param) {
		JSONObject rs = new JSONObject();
        int total= fltChargeDao.getDataCount(param);
        List<JSONObject> rows = fltChargeDao.getDataList(param);
		for(int i=0;i<rows.size();i++){
	 		JSONObject obj=(JSONObject) rows.get(i);
			// 签名转Base64
	    	String pId = obj.getString("sign");
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
		rs.put("total", total);
		rs.put("rows", rows);
        return rs;
	}
	
	public JSONObject getDataById(String id) {
		JSONObject obj= fltChargeDao.getDataById(id);
		// 签名转Base64
    	String pId = obj.getString("sign");
    	if(!StringUtils.isEmpty(pId)){
    		// 获取字节
    		try {
    			byte[] is = fileService.doDownLoadFile(pId);
    			obj.put("PICTURE", new String(Base64.encodeBase64(is)));
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
    	}
		return obj;
	}
	
	public List<Map<String ,String>> getDownDataList(Map<String, Object> param){
		return fltChargeDao.getDownDataList(param);
	}
	 
	
	public JSONObject getFlightDetail(Map<String, Object> param){
		return fltChargeDao.getFlightDetail(param);
	}
	
	public JSONArray getSysUser(){
		return fltChargeDao.getSysUser();
	}

	public int save(List<FltChargeServiceEntity> saveDataList) {
		return fltChargeDao.save(saveDataList);
	}

	public int delete(String id) {
		return fltChargeDao.delete(id);
	}

	public int update(FltChargeServiceEntity updateData) {
		return fltChargeDao.update(updateData);
	}
}
