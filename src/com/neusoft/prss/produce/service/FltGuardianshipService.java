package com.neusoft.prss.produce.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.prss.produce.dao.FltGuardianshipDao;
import com.neusoft.prss.produce.entity.FltGuardianshipEntity;

/**
 * 航班监护记录单据
 */
@Service
@Transactional(readOnly = true)
public class FltGuardianshipService {
	@Autowired
	private FltGuardianshipDao fltGuardianshipDao;

	public JSONObject getDataList(Map<String, Object> param) {
		JSONObject rs = new JSONObject();
        int total= fltGuardianshipDao.getDataCount(param);
        List<JSONObject> rows = fltGuardianshipDao.getDataList(param);
        rs.put("total", total);
		rs.put("rows", rows);
        return rs;
	}
	
	public JSONObject getDataById(String id) {
		return fltGuardianshipDao.getDataById(id);
	}
	
	public List<Map<String ,String>> getDownDataList(Map<String, Object> param){
		return fltGuardianshipDao.getDownDataList(param);
	}
	 
	
	public JSONObject getFlightDetail(Map<String, Object> param){
		return fltGuardianshipDao.getFlightDetail(param);
	}
	
	public JSONArray getSysUser(){
		return fltGuardianshipDao.getSysUser();
	}

	public int save(List<FltGuardianshipEntity> saveDataList) {
		return fltGuardianshipDao.save(saveDataList);
	}

	public int delete(String id) {
		return fltGuardianshipDao.delete(id);
	}

	public int update(FltGuardianshipEntity updateData) {
		return fltGuardianshipDao.update(updateData);
	}
}
