package com.neusoft.prss.equipmen.service;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.neusoft.prss.equipmen.dao.EquipmentDao;

/**
 * 资源管理的Service
 * @author douxf
 * @version 2017-12-5
 */
@Service
@Transactional(readOnly = true)

public class EquipmentService implements EquipmenService{
	@Autowired
	EquipmentDao  equipmentDao;


	@Override
	public List<Map<String, Object>> transformII() {
		List<Map<String, Object>> equipment = equipmentDao.listFacilityII();
		return equipment;
	}

	@Override
	public JSONArray transform() {
		
		JSONArray arr = equipmentDao.listFacility();
		JSONArray arrPid = equipmentDao.listFacilityPid();
		JSONArray arrPidPid = equipmentDao.listFacilityPidPid();
		for(int i=0;i<arrPid.size();i++){
			JSONObject job  = arrPid.getJSONObject(i);
			arr.add(job);
		}
		for(int i=0;i<arrPidPid.size();i++){
			JSONObject job  = arrPidPid.getJSONObject(i);
			arr.add(job);
		}
		return arr;
	}
	
	/**
	 * Ztree的回调事件
	 */
	public Map<String,Object> transformIV(Map<String, Object> param){
		//bootstrap-table要求服务器返回的json须包含：totlal，rows
		Map<String,Object> result = new HashMap<String,Object>();
		int total =  equipmentDao.listFacilityCount(param);
		JSONArray rows = equipmentDao.listFacilityCountList(param);
		for(int i=0;i<rows.size();i++){
			
			JSONObject job  = rows.getJSONObject(i);
			if(job.get("CREATE_DATE")!=null && job.get("CREATE_DATE")!=""){
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String startTime = sdf.format(job.get("CREATE_DATE"));
				
				job.put("CREATE_DATEI", startTime);	
			}
			
			if(String.valueOf(job.get("DEVICE_STATUS")).equals("1")){
				job.put("DEVICE_STATUSS", "可用");
			}else if(String.valueOf(job.get("DEVICE_STATUS")).equals("2")){
				job.put("DEVICE_STATUSS", "停用");
			}else if(String.valueOf(job.get("DEVICE_STATUS")).equals("3")){
				job.put("DEVICE_STATUSS", "维修中");
			}
		}
		result.put("total",total);
        result.put("rows",rows);
		return result;
	}
	
	/**
	 * 筛选框的事件
	 */
	@Override
	public Map<String, Object> transformScreen(Map<String, Object> param) {
		Map<String,Object> result = new HashMap<String,Object>();
		JSONArray rows = equipmentDao.listFacilityCountListScreen(param);
		result.put("rows", rows);
		return result;
	}

	@Override
	public int newSave(Map<String,Object> map) {
		int resault = equipmentDao.saveEquipment(map);
		return resault;
	}

	@Override
	public int newUpe(Map<String, Object> map) {
		int result = equipmentDao.updateEquipment(map);
		equipmentDao.updateEquipmentDevice(map);
		return result;
	}
	
	public int updateZtreeDept(Map<String, Object> map){
		return equipmentDao.updateZtreeDept(map);
	}
	@Override
	public int newDel(Map<String, Object> map) {
		int resultI = equipmentDao.DelEquipment(map);
		int resultII =equipmentDao.DelEquipmentDevice(map);
		return resultI + resultII;
	}

	@Override
	public List<Map<String, Object>> getselDeot() {
		return equipmentDao.equipmentFilterName();
		 
	}

	/**
	 * 筛选
	 * @authoe douxf
	 */
	@Override
	public List<Map<String, Object>> equipmentFilterTypeName() {
		return equipmentDao.equipmentFilterTypeName();
		
	}

	@Override
	public int saveJmDevice(Map<String, Object> map) {
		int result= equipmentDao.saveJmDevice(map);
		return result;
	}

	@Override
	public int updateRevamp(Map<String, Object> map) {
		int result = equipmentDao.updateRevamp(map);
		return result;
	}

	@Override
	public int delRevamp(String[] id) {
		int result = 0;
		for(int i=0;i<id.length;i++){
			result = equipmentDao.delRevamp(id[i]);
		}
		return result;
	}

	@Override
	public Map<String, Object> compileFind(String id) {
		Map<String, Object> result = equipmentDao.compileFind(id);
		return result;
	}

	@Override
	public List<Map<String, Object>> createType() {
		List<Map<String, Object>> data = equipmentDao.createType();
		return data;
	}

	@Override
	public Map<String, Object> findEdit(String id) {
		Map<String, Object> data = equipmentDao.findEdit(id);
		return data;
	}
	
	@Override
	public Map<String, Object> findEditDept(String id) {
		Map<String, Object> data = equipmentDao.findEditDept(id);
		return data;
	}

	@Override
	public List<Map<String, Object>> equipmentFilterTypeNameI() {
		return equipmentDao.equipmentFilterTypeNameI();
		
	}

	@Override
	public List<Map<String, Object>> equipmentFilterTypeNameII() {
		return equipmentDao.equipmentFilterTypeNameII();
	}

	

}
