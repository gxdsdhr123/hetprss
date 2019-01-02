package com.neusoft.prss.produce.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.framework.common.service.BaseService;
import com.neusoft.prss.file.service.FileService;
import com.neusoft.prss.produce.dao.FltCleanDao;
import com.neusoft.prss.produce.entity.FltCleanServiceEntity;

/**
 * 航空器客舱保洁服务记录单据
 */
@Service
@Transactional(readOnly = true)
public class FltCleanService extends BaseService{
	@Autowired
	private FltCleanDao fltCleanDao;
	
	@Autowired
    private FileService fileService;

	public JSONObject getDataList(Map<String, Object> param) {
		JSONObject rs = new JSONObject();
        int total= fltCleanDao.getDataCount(param);
        List<JSONObject> rows = fltCleanDao.getDataList(param);
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
		JSONObject obj = fltCleanDao.getDataById(id);
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
		return fltCleanDao.getDownDataList(param);
	}
	 
	
	public JSONObject getFlightDetail(Map<String, Object> param){
		return fltCleanDao.getFlightDetail(param);
	}
	
	public JSONArray getSysUser(){
		return fltCleanDao.getSysUser();
	}

	public int save(List<FltCleanServiceEntity> saveDataList) {
		return fltCleanDao.save(saveDataList);
	}

	public int delete(String id) {
		return fltCleanDao.delete(id);
	}

	public int update(FltCleanServiceEntity updateData) {
		return fltCleanDao.update(updateData);
	}
}
